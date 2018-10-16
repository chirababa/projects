package Model;


public class Comanda {
    private int idComanda;
    private int idClient;
    private String orderDate;

    public Comanda(){}

    public Comanda(int idComanda, int idClient, String orderDate) {
        this.idComanda = idComanda;
        this.idClient = idClient;
        this.orderDate = orderDate;
    }

    public Comanda(int idClient,String orderDate) {
        this.idClient = idClient;
        this.orderDate=orderDate;
    }

    public int getIdComanda() {
        return idComanda;
    }

    public int getIdClient() {
        return idClient;
    }


    public void setIdComanda(int idComanda) {
        this.idComanda = idComanda;
    }

    public void setIdClient(int idClient) {
        this.idClient = idClient;
    }


    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    @Override
    public String toString() {
        return "Comanda{" +
                "idComanda=" + idComanda +
                ", idClient=" + idClient +
                ", orderDate='" + orderDate + '\'' +
                '}';
    }
}
