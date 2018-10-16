package InterfataSiControl;
import OperatiiGestionare.Casa;
import OperatiiGestionare.ClientGenerator;
import Strategie.RandomStrategy;
import Strategie.ShortestQueue;
import Strategie.ShortestTime;
import Strategie.Strategy;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.util.*;
import java.util.List;

/**
 * Created by chira on 31.03.2018.
 */

public class Interface extends Canvas implements Runnable {
       private List<Casa> cozi= new LinkedList<>();
       private List<Casa> listaCozi = Collections.synchronizedList(cozi);
       private ClientGenerator generator=new ClientGenerator(listaCozi);
       public static Strategy strategy;
       private int numberQ=0;
       private Thread t= new Thread(generator);
       private Thread thread =new Thread(this);
       Interface()
        {
            new Window(this);
        }

        synchronized void start() {
            //alegem strategie dorita
            if(Window.shortestQueue.isSelected()){
                strategy=new ShortestQueue();
            }
            if(Window.shortestTime.isSelected()) {
                strategy=new ShortestTime();
            }
            if(Window.randomStrategy.isSelected()) {
                strategy=new RandomStrategy();
            }
            numberQ=Integer.parseInt(Window.nrQ.getText());//citim numarul de case
            //parcurgem lista de case si pornim thredurile pentru fiecare in parte
            for (int i = 0; i < numberQ; i++) {//numar de case
                listaCozi.add(new Casa(generator,i));
                Thread t1 = new Thread(listaCozi.get(i));
                t1.start();
            }
            //pornim threadul de la generator si threadul acestei clase
            t.start();
            thread.start();
        }

        private void rendCozi(Graphics g){
            //se randeaza intreaga coada(casa+clienti)
            for(int i=0;i<numberQ;i++) {
                listaCozi.get(i).render(g,i+1);
            }
        }

        private void render(){
            //folosim un buffer pentru a crea spatiul in care vom vedea animatia
            BufferStrategy bs = this.getBufferStrategy();

            if (bs == null) {
                this.createBufferStrategy(2);
                return;
            }

            Graphics g  = bs.getDrawGraphics();
            g.setColor(Color.BLACK);
            g.fillRect(0,0,900,500);
            this.rendCozi(g);
            g.dispose();
            bs.show();
        }

        @Override
        public void run() {
            while(true){
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (this.listaCozi) {
                    //apelam funtie de reder
                    render();
                }
            }

        }
    }
