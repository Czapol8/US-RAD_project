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
public class Refresher extends Thread
{

    javax.swing.JProgressBar progressBar;
    javax.swing.JLabel status;

    Algorytm actualAlgo;
    boolean isWorking;

    public Refresher(javax.swing.JProgressBar progressBar, javax.swing.JLabel status)
    {
        this.progressBar = progressBar;
        this.status = status;
        isWorking = true;
        start();
    }

    public void SetAlgo(Algorytm algo)
    {
        synchronized (this)
        {
            this.actualAlgo = algo;
        }
    }

    public void Disable()
    {
        isWorking = false;
    }

    @Override
    public void run()
    {
        long sleepTime = 1000 / 25;
        try
        {
            while (isWorking)
            {
                if (actualAlgo != null)
                {

                    progressBar.setValue((int) (100 * actualAlgo.GetActualProgressStatus()));
                    status.setText(actualAlgo.GetActualStatus());
                }
                synchronized (this)
                {
                    wait(sleepTime);
                }
            }
        }
        catch (Exception e)
        {
            System.err.println("refresher crash!");
        }
    }
}
