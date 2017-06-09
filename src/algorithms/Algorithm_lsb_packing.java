package algorithms;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;
import src.Algorytm;
import src.Util;

/**
 *
 * @author Czapol
 */
public class Algorithm_lsb_packing implements Algorytm
{

    enum workType
    {

        none, code, decode
    };

    public Algorithm_lsb_packing(boolean masking)
    {
        this.masking = masking;
        work = workType.none;
        gen = new Random();

    }

    private boolean masking;
    private static final long maskSeed = 7218642864267L;

    private Random gen;

    private workType work;

    private String status;
    private float progress;

    BufferedImage source;
    BufferedImage coded;
    File file;
    String destiny;

    @Override
    public void Process()
    {
        if (work != workType.none)
        {
            if (work == workType.code)
                ProcessCode();
            else if (work == workType.decode)
                ProcessDecode();
            work = workType.none;
        }
    }

    private void ProcessCode()
    {
        long time = System.nanoTime();
        BufferedInputStream stream;

        try
        {
            status = "Rezerwowanie pamięci...";
            progress = 0;
            stream = new BufferedInputStream(new FileInputStream(file));

            int h = source.getHeight();
            int w = source.getWidth();
            int[] dataBufferedInt = Util.GetDataBuffer(source);
            int[][] buffered = new int[3][];
            buffered[0] = new int[dataBufferedInt.length];
            buffered[1] = new int[dataBufferedInt.length];
            buffered[2] = new int[dataBufferedInt.length];

            gen = new Random(maskSeed);
            status = "Przygotowywanie obrazu...";
            for (int i = 0; i < dataBufferedInt.length; i++)
            {
                progress = (float) i / dataBufferedInt.length;
                int[] tmp = Util.GetPixelColors(dataBufferedInt[i]);
                buffered[0][i] = tmp[0];
                buffered[1][i] = tmp[1];
                buffered[2][i] = tmp[2];
            }

            //inserting file name
            status = "Tworzenie metadanych...";
            progress = 0;
            String name = file.getName();
            System.out.println(name.length());
            int vect = InsertInt(buffered, name.length(), 0);
            for (int i = 0; i < name.length(); i++)
            {
                progress = (float) i / name.length();
                vect = InsertChar(buffered, name.charAt(i), vect);
            }
            //inserting filesystem
            status = "Wypełnianie danymi...";
            int value = (int) file.length();
            vect = InsertInt(buffered, value, vect);
            for (int i = 0; i < value; i++)
            {
                progress = (float) i / value;
                vect = InsertByte(buffered, stream.read(), vect);
            }

            if (masking)
            {
                status = "Maskowanie reszty obszaru...";

                try
                {
                    while (true)
                        vect = InsertInt(buffered, 0, vect);
                }
                catch (Exception e)
                {
                }

            }

            status = "Przygotowywanie do zapisu...";
            for (int i = 0; i < dataBufferedInt.length; i++)
            {
                Color test = new Color(buffered[0][i], buffered[1][i], buffered[2][i]);
                dataBufferedInt[i] = test.getRGB();
                progress = (float) i / dataBufferedInt.length;
            }

            BufferedImage nowy = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
            nowy.setRGB(0, 0, w, h, dataBufferedInt, 0, w);

            stream.close();
            status = "Zapisywanie... proszę czekać";
            Util.SaveImage(nowy, destiny);

            status = "Zakończono w " + (System.nanoTime() - time) / 1000000 + "ms";
            progress = 1;

        }
        catch (Exception e)
        {
            status = "Error!";
            e.printStackTrace();
        }
    }

    @Override
    public void Code(BufferedImage source, File file, String destiny)
    {
        synchronized (this)
        {
            System.out.println("Into synchro");
            if (work == workType.none)
            {
                System.out.println("applying");
                this.source = source;
                this.file = file;
                this.destiny = destiny;
                work = workType.code;
            }
        }
    }

    private int InsertInt(int[][] buffered, int value, int vect)
    {
        if (masking)
            value += gen.nextInt();
        for (int i = 0; i < Integer.SIZE / 4; i++)
        {
            int actual = (value >> i * 4) & 0x0F;
            int poz = vect / 3;
            if (buffered[vect % 3][poz] > 127)
                buffered[vect % 3][poz] -= actual;
            else
                buffered[vect % 3][poz] += actual;
            vect++;
        }
        return vect;
    }

    private int InsertByte(int[][] buffered, int value, int vect)
    {
        if (masking)
            value = (value + gen.nextInt(256)) % 256;
        for (int i = 0; i < Byte.SIZE / 4; i++)
        {
            int actual = (value >> i * 4) & 0x0F;
            int poz = vect / 3;
            if (buffered[vect % 3][poz] > 127)
                buffered[vect % 3][poz] -= actual;
            else
                buffered[vect % 3][poz] += actual;
            vect++;
        }
        return vect;
    }

    private int InsertChar(int[][] buffered, char value, int vect)
    {
        if (masking)
            value += gen.nextInt(Character.MAX_VALUE);
        for (int i = 0; i < Character.SIZE / 4; i++)
        {
            int actual = (value >> i * 4) & 0x0F;
            int poz = vect / 3;
            if (buffered[vect % 3][poz] > 127)
                buffered[vect % 3][poz] -= actual;
            else
                buffered[vect % 3][poz] += actual;
            vect++;
        }
        return vect;
    }

    private void ProcessDecode()
    {
        BufferedOutputStream stream;
        long time = System.nanoTime();
        try
        {
            status = "Rezerwowanie pamięci...";
            gen = new Random(maskSeed);
            progress = 0;
            int h = source.getHeight();
            int w = source.getWidth();
            int[] dataBufferedInt = Util.GetDataBuffer(source);
            int[][] buffered = new int[3][];
            buffered[0] = new int[dataBufferedInt.length];
            buffered[1] = new int[dataBufferedInt.length];
            buffered[2] = new int[dataBufferedInt.length];

            int[] dataBufferedInt2 = Util.GetDataBuffer(coded);
            int[][] buffered2 = new int[3][];
            buffered2[0] = new int[dataBufferedInt2.length];
            buffered2[1] = new int[dataBufferedInt2.length];
            buffered2[2] = new int[dataBufferedInt2.length];

            status = "Zapełnianie pamięci...";
            for (int i = 0; i < dataBufferedInt.length; i++)
            {
                progress = (float) i / dataBufferedInt.length;
                int[] tmp = Util.GetPixelColors(dataBufferedInt[i]);
                buffered[0][i] = tmp[0];
                buffered[1][i] = tmp[1];
                buffered[2][i] = tmp[2];
            }

            for (int i = 0; i < dataBufferedInt2.length; i++)
            {
                int[] tmp = Util.GetPixelColors(dataBufferedInt2[i]);
                buffered2[0][i] = tmp[0];
                buffered2[1][i] = tmp[1];
                buffered2[2][i] = tmp[2];
                progress = (float) i / dataBufferedInt.length;
            }

            System.out.println("Odczyt");
            progress = 0;
            status = "Odczytywanie metadanych...";
            long vect = 0;
            //wczytywanie nazwy
            int nameLenght = ReadInt(buffered, buffered2, 0);
            System.out.println(nameLenght);
            vect += Integer.SIZE / 4;
            String name = "";
            for (int i = 0; i < nameLenght; i++)
            {
                progress = (float) i / nameLenght;
                name += ReadChar(buffered, buffered2, vect);
                vect += Character.SIZE / 4;
            }
            System.out.println("encoded name: " + name);
            stream = new BufferedOutputStream(new FileOutputStream(file.getParent() + "/_" + name));

            long size = ReadInt(buffered, buffered2, vect);
            vect += Integer.SIZE / 4;

            System.out.println("size:" + size + "b");
            status = "Odczytywanie pliku: " + name;
            long counter = 0;
            while (counter < size)
                try
                {
                    progress = (float) counter / size;
                    vect = ReadByte(buffered, buffered2, vect, stream);
                    counter++;
                }
                catch (IndexOutOfBoundsException e)
                {
                    System.out.println("OUT OF BOUNDS at: " + vect);
                    vect = Long.MAX_VALUE;
                } // stream.write(value);
            // System.out.println(vect + " - " + buffered[0].length);
            stream.close();

            status = "Zakończono w " + (System.nanoTime() - time) / 1000000 + "ms";
            progress = 1;
        }
        catch (Exception e)
        {
            status = "Error!";
            e.printStackTrace();
        }
        System.out.println("deszyfrowano!");
    }

    @Override
    public void Restore(BufferedImage source, BufferedImage coded, File file)
    {
        synchronized (this)
        {
            if (work == workType.none)
            {
                this.source = source;
                this.coded = coded;
                this.file = file;
                work = workType.decode;
            }
        }
    }

    private int ReadInt(int[][] buffered, int[][] buffered2, long vect)
    {

        int value = 0;
        for (int i = 0; i < Integer.SIZE / 4; i++)
        {
            int poz1 = (int) vect % 3;
            int poz2 = (int) vect / 3;
            if (buffered[poz1][poz2] != buffered2[poz1][poz2])
                value += Math.abs(buffered[poz1][poz2] - buffered2[poz1][poz2]) << (i * 4);
            vect++;
        }
        if (masking)
            return value - gen.nextInt();
        return value;
    }

    private long ReadByte(int[][] buffered, int[][] buffered2, long vect, BufferedOutputStream stream) throws IOException
    {
        int value = 0;
        for (int i = 0; i < Byte.SIZE / 4; i++)
        {
            int poz1 = (int) vect % 3;
            int poz2 = (int) vect / 3;
            if (buffered[poz1][poz2] != buffered2[poz1][poz2])
                value += Math.abs(buffered[poz1][poz2] - buffered2[poz1][poz2]) << (i * 4);
            vect++;
        }
        if (masking)
        {
            value = value - gen.nextInt(256) % 256;
            stream.write(value);
        }

        else
            stream.write(value);
        return vect;
    }

    private char ReadChar(int[][] buffered, int[][] buffered2, long vect)
    {
        char value = 0;
        for (int i = 0; i < Character.SIZE / 4; i++)
        {
            int poz1 = (int) vect % 3;
            int poz2 = (int) vect / 3;
            if (buffered[poz1][poz2] != buffered2[poz1][poz2])
                value += Math.abs(buffered[poz1][poz2] - buffered2[poz1][poz2]) << (i * 4);
            vect++;
        }
        if (masking)
            return (char) (value - gen.nextInt(Character.MAX_VALUE));
        return value;
    }

    @Override
    public int GetMaxData(BufferedImage image, File toSave)
    {
        if (toSave.exists())
            return (image.getHeight() * image.getWidth() * 3) / 2 - Integer.BYTES * 2 - Character.BYTES * toSave.getName().length();
        else
            return (image.getHeight() * image.getWidth() * 3) / 2 - Integer.BYTES;
    }

    @Override
    public String toString()
    {
        if (masking)
            return "LSB upakowujący 12b na Pixel z maską szumiącą";
        return "LSB upakowujący 12b na Pixel";
    }

    @Override
    public String GetActualStatus()
    {
        return status;
    }

    @Override
    public float GetActualProgressStatus()
    {
        return progress;
    }

}
