/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src;

import java.awt.image.BufferedImage;
import java.io.File;

/**
 *
 * @author Czapol
 */
public interface Algorytm
{

    public void Code(BufferedImage source, File file,String destiny);

    public void Restore(BufferedImage source, BufferedImage coded, File file);
    
    public int GetMaxData(BufferedImage image,File toSave);
    
    @Override
    public String toString();
    
    public String GetActualStatus();
    
    public float GetActualProgressStatus();
    
    public void Process();
    
   

}
