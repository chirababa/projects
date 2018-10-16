package OperatiiGestionare;
import java.awt.*;

/**
 * Created by chira on 31.03.2018.
 */
public class Client {
    private int pozx,pozy;
    int numarClient,clientWaintingTime;
    private int serviceTime,arrivingTime;
    Client(int pozx, int pozy, int serviceTime, int arrivingTime, int numarClient, int clientWaintingTime){
        this.pozx=pozx;
        this.pozy=pozy;
        this.serviceTime=serviceTime;
        this.arrivingTime=arrivingTime;
        this.numarClient=numarClient;
        this.clientWaintingTime=clientWaintingTime;
    }

    public int getServiceTime() {
        return serviceTime;
    }

    int getPozy() {
        return pozy;
    }

    void setPozy(int pozy) {
        this.pozy = pozy;
    }

    void render(Graphics g) {//functie care rendeaza pe ecran clientul sub forma unui cerc,iar in dreptul lui arrivingtime-ul si service-timeul
        g.setColor(Color.blue);
        g.fillOval(this.pozx,this.pozy,20,20);
        g.setColor(Color.white);
        //g.setFont(new Font("arial",8,12));
        //g.drawString("("+this.arrivingTime+","+this.serviceTime+")",this.pozx+25,this.pozy+15);
        g.drawString(this.serviceTime+"",this.pozx+25,this.pozy+15);
    }
}
