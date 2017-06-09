/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src;

/**
 *
 * @author Czapol
 */
public class AlgorytmRunner extends Thread
{

    private Algorytm algorytm;
    private boolean working;

    public AlgorytmRunner()
    {
        working = true;
        start();
    }

    public void SetAlgorytm(Algorytm algorytm)
    {
        synchronized (this)
        {
            this.algorytm = algorytm;
        }
    }

    public Algorytm GetAlgorytm()
    {
        synchronized (this)
        {
            return algorytm;
        }
    }

    public void run()
    {
        while (working)
        {
            try
            {
                if (algorytm != null)
                {
                    algorytm.Process();
                }

                synchronized (this)
                {
                    wait(500);
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
                working = false;
            }
        }
    }

}
