package OperatiiGestionare;
import TimerCounter.Timer1;
import InterfataSiControl.Window;
import java.awt.*;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static java.lang.Thread.sleep;

/**
 * Created by chira on 31.03.2018.
 */

public class Casa implements Runnable {

    private List<Client> casaUnsafe=new LinkedList<>();
    public List<Client> casa = Collections.synchronizedList(casaUnsafe);
    private ClientGenerator generator;
    private int numarOrdine=0;
    public Casa(ClientGenerator generator,int numarOrdine)
    {
        this.generator=generator;
        this.numarOrdine=numarOrdine;
    }

    public void render(Graphics g,int i){//metoda de render a caselor de marcat(sub forma de dreptunghi) si a clientilor
        for(Client c:casa){
            c.render(g);
        }
        g.setColor(Color.red);
        g.fillRect(i*80,400,40,20);
    }
    synchronized void addClient(Client c)
    {
        casa.add(c);
    }

    @Override
    public void run() {
        while(Timer1.secondPassed<Timer1.simulationTime)
        {
            if(casa.size()>0) {
            try {
                sleep((casa.get(0).getServiceTime() *1000)/Window.sliderValue);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (this.casa) {
                //stergem din coada dupa ce acest thred a facut un sleep de service time*unitate
                Window.textArea.append("Clientul "+ this.casa.get(0).numarClient +" a plecat de la casa "+(this.numarOrdine+1)+" la secunda "+ Timer1.secondPassed+"\n");
                Window.textArea.setCaretPosition(Window.textArea.getDocument().getLength());
                this.casa.remove(0);
            }
                for(Client c:this.casa)
                {
                    c.setPozy(c.getPozy()+30);
                }
            }
        }
    }
}

