package DataAccess;
import Model.Produs;

public class ProdusDao extends AbstractDao<Produs> {
    public String toString(Produs c){
        return c.toString();
    }
}
