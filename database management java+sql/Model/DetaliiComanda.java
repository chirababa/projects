package Model;


public class DetaliiComanda {
    private int idDetaliiComanda;
    private int idComanda;
    private int idProdus;
    private int cantitate;

    public DetaliiComanda(){}

    public DetaliiComanda(int idDetaliiComanda, int idComanda, int idProdus, int cantitate) {
        this.idDetaliiComanda = idDetaliiComanda;
        this.idComanda = idComanda;
        this.idProdus = idProdus;
        this.cantitate = cantitate;
    }

    public DetaliiComanda(int idComanda, int idProdus, int cantitate) {
        this.idComanda = idComanda;
        this.idProdus = idProdus;
        this.cantitate = cantitate;
    }

    public int getIdDetaliiComanda() {
        return idDetaliiComanda;
    }

    public int getIdComanda() {
        return idComanda;
    }

    public int getIdProdus() {
        return idProdus;
    }

    public int getCantitate() {
        return cantitate;
    }

    public void setIdDetaliiComanda(int idDetaliiComanda) {
        this.idDetaliiComanda = idDetaliiComanda;
    }

    public void setIdComanda(int idComanda) {
        this.idComanda = idComanda;
    }

    public void setIdProdus(int idProdus) {
        this.idProdus = idProdus;
    }

    public void setCantitate(int cantitate) {
        this.cantitate = cantitate;
    }

    @Override
    public String toString() {
        return "DetaliiComanda{" +
                "idDetaliiComanda=" + idDetaliiComanda +
                ", idComanda=" + idComanda +
                ", idProdus=" + idProdus +
                ", cantitate=" + cantitate +
                '}';
    }
}
