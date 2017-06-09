/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;

/**
 *
 * @author Czapol
 */
public class Util
{

    public static int[] GetDataBuffer(BufferedImage obraz)
    {
        int[] dataBufferInt = null;
        if (obraz != null)
        {
            int w = obraz.getWidth();
            int h = obraz.getHeight();
            dataBufferInt = obraz.getRGB(0, 0, w, h, null, 0, w);
        }
        return dataBufferInt;
    }

    public static int[] GetPixelColors(int bufferInt)
    {
        int[] kolory = new int[3];
        Color c = new Color(bufferInt);
        kolory[0] = c.getRed();
        kolory[1] = c.getGreen();
        kolory[2] = c.getBlue();
        return kolory;
    }

    public static BufferedImage GetImage(String adres)
    {
        return GetImage(new File(adres));
    }

    public static BufferedImage GetImage(File file)
    {
        BufferedImage obraz = null;
        try
        {
            BufferedInputStream stream = new BufferedInputStream(new FileInputStream(file));
            obraz = ImageIO.read(stream);
            stream.close();
        }
        catch (IOException e)
        {
            System.err.println(e.getMessage());
        }
        return obraz;
    }

    public static void SaveImage(BufferedImage obraz, String patch)
    {
        try
        {
            File ImageFile = new File(patch);
            BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(ImageFile));
            ImageIO.write(obraz, "png", stream);
            stream.close();
            
           
           
            
            System.out.println("Zapisano plik: " + patch);
        }
        catch (Exception e)
        {
            System.err.println("SAVE IMAGE ERROR!");
            System.err.println(e.getMessage());
        }
    }

    public static long CheckFileSize(String path)
    {
        File file = new File(path);
        if (file.exists())
        {
            return file.length();
        }
        return 0;
    }
}
