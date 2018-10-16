package PersonInfo;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Random;

public abstract class Account extends Observable implements Serializable {

    private int idTitular;
    private int numarCont;
    private int money;
    private String numeTitular;
    private String tip;

    public Account() {
        Random random = new Random();
        int nrCont= random.nextInt(9999999+1-1245321)+1245321;
        this.money = 0;
        this.numarCont=nrCont;
    }

    public int getMoney() {
        return money;
    }

    public String getNumeTitular() {
        return numeTitular;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public void setNumeTitular(String numeTitular) {
        this.numeTitular = numeTitular;
    }

    public int getNumarCont() {
        return numarCont;
    }

    public void setNumarCont(int numarCont) {
        this.numarCont = numarCont;
    }

    public int getIdTitular() {
        return idTitular;
    }

    public void setIdTitular(int idTitular) {
        this.idTitular = idTitular;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    @Override
    public String toString() {
        return "Account{" +
                "idTitular=" + idTitular +
                ", numarCont=" + numarCont +
                ", money=" + money +
                ", numeTitular='" + numeTitular + '\'' +
                ", tip='" + tip + '\'' +
                '}';
    }

    public abstract int withdraw(int money);
    public abstract int deposit(int money);
}
