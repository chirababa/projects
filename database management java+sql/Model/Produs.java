package Model;

public class Produs {
    private int idProdus;
    private String nameProd;
    private int stoc;
    private String distributor;
    private int pret;

    public Produs(){}

    public Produs(String nameProd, int stoc, String distributor,int pret) {
        this.nameProd = nameProd;
        this.stoc = stoc;
        this.distributor = distributor;
        this.pret=pret;
    }

    public int getIdProdus() {
        return idProdus;
    }

    public String getNameProd() {
        return nameProd;
    }

    public int getStoc() {
        return stoc;
    }

    public String getDistributor() {
        return distributor;
    }

    public void setIdProdus(int idProdus) {
        this.idProdus = idProdus;
    }

    public void setNameProd(String nameProd) {
        this.nameProd = nameProd;
    }

    public void setStoc(int stoc) {
        this.stoc = stoc;
    }

    public void setDistributor(String distributor) {
        this.distributor = distributor;
    }

    public int getPret() {
        return pret;
    }

    public void setPret(int pret) {
        this.pret = pret;
    }

    @Override
    public String toString() {
        return "Produs{" +
                "idProdus=" + idProdus +
                ", nameProd='" + nameProd + '\'' +
                ", stoc=" + stoc +
                ", distributor='" + distributor + '\'' +
                ", pret=" + pret +
                '}';
    }
}
