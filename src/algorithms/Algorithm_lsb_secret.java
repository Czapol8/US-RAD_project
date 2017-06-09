package algorithms;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Random;
import src.Algorytm;
import src.Util;

/**
 *
 * @author Czapol
 */
public class Algorithm_lsb_secret implements Algorytm
{

    enum workType
    {

        none, code, decode
    };

    public Algorithm_lsb_secret()
    {
        work = workType.none;
        r = new Random();
    }
    private Random r;

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
            System.out.println(name);
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

    private int InsertByte(int[][] buffered, int value, int vect)
    {
        for (int i = 0; i < Byte.SIZE; i++)
        {
            int actual = value >> i & 0x01;
            int biggest = -1;
            int bigValue = 0;
            for (int j = 0; j < 3; j++)
            {
                int tmp = Math.abs(buffered[j][vect] - buffered[j][vect + 1]);
                if (tmp > bigValue)
                {
                    bigValue = tmp;
                    biggest = j;
                }
            }
            if (biggest == -1)
                biggest = r.nextInt(3);
            if (buffered[biggest][vect] > 127)
                buffered[biggest][vect] -= actual;
            else
                buffered[biggest][vect] += actual;
            vect++;
        }
        return vect;
    }

    private int InsertInt(int[][] buffered, int value, int vect)
    {
        for (int i = 0; i < Integer.SIZE; i++)
        {
            int actual = value >> i & 0x01;
            int biggest = r.nextInt(3);
            int bigValue = 0;
            for (int j = 0; j < 3; j++)
            {
                int tmp = Math.abs(buffered[j][vect] - buffered[j][vect + 1]);
                if (tmp > bigValue)
                {
                    bigValue = tmp;
                    biggest = j;
                }
            }
            if (buffered[biggest][vect] > 127)
                buffered[biggest][vect] -= actual;
            else
                buffered[biggest][vect] += actual;
            vect++;
        }
        return vect;
    }

    private int InsertChar(int[][] buffered, char value, int vect)
    {
        for (int i = 0; i < Character.SIZE; i++)
        {
            int actual = value >> i & 0x01;
            int biggest = -1;
            int bigValue = 0;
            for (int j = 0; j < 3; j++)
            {
                int tmp = Math.abs(buffered[j][vect] - buffered[j][vect + 1]);
                if (tmp > bigValue)
                {
                    bigValue = tmp;
                    biggest = j;
                }
            }
            if (biggest == -1)
                biggest = r.nextInt(3);
            if (buffered[biggest][vect] > 127)
                buffered[biggest][vect] -= actual;
            else
                buffered[biggest][vect] += actual;
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
            progress = 0;
            int h = source.getHeight();
            int w = source.getWidth();
            int[] dataBufferedInt = Util.GetDataBuffer(source);
            int[] dataBufferedInt2 = Util.GetDataBuffer(coded);

            System.out.println("Odczyt");
            progress = 0;
            status = "Odczytywanie metadanych...";
            int vect = 0;
            //wczytywanie nazwy
            int nameLenght = ReadInt(dataBufferedInt, dataBufferedInt2, 0);
            vect += Integer.SIZE;
            String name = "";
            for (int i = 0; i < nameLenght; i++)
            {
                progress = (float) i / nameLenght;
                name += ReadChar(dataBufferedInt, dataBufferedInt2, vect);
                vect += Character.SIZE;
            }
            System.out.println("encoded name: " + name);
            stream = new BufferedOutputStream(new FileOutputStream(file.getParent() + "/_" + name));
            status = "Odczytywanie pliku: " + name;
            //wczytywanie pliku
            long size = ReadInt(dataBufferedInt, dataBufferedInt2, vect);
            vect += Integer.SIZE;

            size = size * 8;
            size += Integer.SIZE * 2 + Character.SIZE * nameLenght;

            System.out.println("size:" + size + "b");

            while (vect < size)
            {
                int value = 0;
                try
                {
                    progress = (float) vect / size;
                    value = ReadByte(dataBufferedInt, dataBufferedInt2, vect);
                    vect += Byte.SIZE;
                }
                catch (IndexOutOfBoundsException e)
                {
                    System.out.println("OUT OF BOUNDS at: " + vect);
                    vect = Integer.MAX_VALUE;
                }
                stream.write(value);
            }
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

    private int ReadInt(int[] buffered, int[] buffered2, int vect)
    {
        int value = 0;
        for (int i = 0; i < Integer.SIZE; i++)
        {
            if (buffered[vect] != buffered2[vect])
                value += 0x01 << i;
            vect++;
        }
        return value;
    }

    private int ReadByte(int[] buffered, int[] buffered2, int vect)
    {
        int value = 0;
        for (int i = 0; i < Byte.SIZE; i++)
        {
            if (buffered[vect] != buffered2[vect])
                value += 0x01 << i;
            vect++;
        }
        return value;
    }

    private char ReadChar(int[] buffered, int[] buffered2, int vect)
    {
        char value = 0;
        for (int i = 0; i < Character.SIZE; i++)
        {

            if (buffered[vect] != buffered2[vect])
                value += 0x01 << i;
            vect++;
        }
        return value;
    }

    @Override
    public int GetMaxData(BufferedImage image, File toSave)
    {
        if (toSave.exists())
            return (image.getHeight() * image.getWidth()) / 8 - Integer.BYTES * 2 - Character.BYTES * toSave.getName().length();
        else
            return (image.getHeight() * image.getWidth()) / 8 - Integer.BYTES;
    }

    @Override
    public String toString()
    {
        return "LSB zaszywający 1b na Pixel";
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
