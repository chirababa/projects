package OperatiiGestionare;
import InterfataSiControl.Interface;
import TimerCounter.Timer1;
import InterfataSiControl.Window;
import java.util.*;

import static java.lang.Thread.sleep;

/**
 * Created by chira on 01.04.2018.
 */
public class ClientGenerator implements Runnable {
    private int arriveTime;
    private int serviceTime=0;
    private Random random= new Random();
    private List<Casa> listaCozi;
    private int contorClienti;//pentru a afisa numarul de total de clienti din simulare
    private int[] waitingTime=new int[15];//vector care retine waiting timeul pentru fiecare al fiecare case
    private int[] clientsNumber=new int[15];//vector care retine numarul de  clienti din fiecare coada
    private int picktime=0;
    private int max=0;

    public ClientGenerator(List<Casa> listaCozi){
        this.listaCozi=listaCozi;
    }

    public int getServiceTime() {
        return this.serviceTime;
    }

    public int getArriveTime() {return this.arriveTime;}


    //funtie care genereaza random intre 2 valori timpii necesare
    private void randomData(){
        int minArriveTime = Integer.parseInt(Window.minArr.getText());
        int maxArriveeTime = Integer.parseInt(Window.maxArr.getText());
        int minServiceTime = Integer.parseInt(Window.minServ.getText());
        int maxServiceTime = Integer.parseInt(Window.maxServ.getText());
        this.serviceTime=random.nextInt(maxServiceTime - minServiceTime)+ minServiceTime;
        this.arriveTime=random.nextInt(maxArriveeTime - minArriveTime)+ minArriveTime;
    }

    //metoda pentru a calcula pick-timeul
    private int pickTimeMethod() {
        int numarClienti=0;
        for (int i = 0; i < listaCozi.size(); i++){
            for(int j=0;j<listaCozi.get(i).casa.size();j++)
            {
                numarClienti++;
            }
        }
        return numarClienti;
    }
    @Override
    public void run() {
        while(Timer1.secondPassed<Timer1.simulationTime) {
            randomData();
            System.out.println(getServiceTime() + " " + getArriveTime());
            try {
                sleep((this.arriveTime * 1000)/ Window.sliderValue);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
                synchronized (this.listaCozi) {
                    int strategy= Interface.strategy.addTask(listaCozi);//aceasta variabila tina indexul cozii unde urmeaza sa fie introdusi clientii, comfort strategiei ales
                    //calculam waiting time-ul pentru fiecare coada la momentul curent si rezulatatul il punem intr-un vector
                    contorClienti++; //pentru a afisa in texfield numarul clientului din simulare
                    int waitingTimeEachClient = 0;
                    if (listaCozi.get(strategy).casa.size() > 0) {
                        for (int i = 0; i < listaCozi.get(strategy).casa.size(); i++) {
                                waitingTime[strategy] += listaCozi.get(strategy).casa.get(i).getServiceTime();
                                waitingTimeEachClient += listaCozi.get(strategy).casa.get(i).getServiceTime();
                        }
                    } else {
                        waitingTimeEachClient =0;
                        waitingTime[strategy] +=0;
                    }
                    //afisam datele necesare in textBoxul din frame
                    if (strategy == this.listaCozi.size()) {
                        Window.textArea.append("Clientul " + contorClienti + " a intrat la coada " + listaCozi.size() + " la secunda " + Timer1.secondPassed + " si astepta " + waitingTimeEachClient + "secunde" + "\n");
                    } else {
                        Window.textArea.append("Clientul " + contorClienti + " a intrat la coada " + (strategy + 1) + " la secunda " + Timer1.secondPassed + " si astepta " + waitingTimeEachClient + " secunde" + "\n");
                    }
                    Window.textArea.setCaretPosition(Window.textArea.getDocument().getLength());
                    clientsNumber[strategy]++;//incrementam pentru fiecare vector numarul de clienti

                    //adaugam clienti in coada
                    listaCozi.get(strategy).addClient(new Client(80 * (strategy + 1), 360 - 30 * listaCozi.get(strategy).casa.size(), this.serviceTime, this.arriveTime, this.contorClienti, waitingTimeEachClient));
                }
            //cautam timpul in care s-au aflat cei mai multi clienti la coada
            if(max<pickTimeMethod()) {
                max=pickTimeMethod();
                this.picktime=Timer1.secondPassed;
            }
            }
        //calcul average waiting time
        for(int i=0;i<listaCozi.size();i++) {
            if(clientsNumber[i]!=0) {
                Window.textArea.append("Average waiting pentru casa " + (i + 1) + " este " + (waitingTime[i] / clientsNumber[i]) + "\n");
            }
            else {
                Window.textArea.append("Average waiting pentru casa " + (i + 1) + " este 0 pentru ca niciun client nu a stat la aceasta coada\n");
            }
            Window.textArea.setCaretPosition(Window.textArea.getDocument().getLength());
        }
        //afisam pick-timeul
        Window.textArea.append("Pick Time-ul este "+picktime+"\n");
    }
}
