package DataAccess;
import Model.DetaliiComanda;

/**
 * Created by chira on 17.04.2018.
 */
public class DetaliiComandaDao extends AbstractDao<DetaliiComanda> {
    public String toString(DetaliiComanda c){
        return c.toString();
    }
}
