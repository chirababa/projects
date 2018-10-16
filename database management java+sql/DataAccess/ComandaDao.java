package DataAccess;
import Model.Comanda;

/**
 * Created by chira on 17.04.2018.
 */
public class ComandaDao extends AbstractDao<Comanda> {

    public String toString(Comanda c){
        return c.toString();
    }
}
