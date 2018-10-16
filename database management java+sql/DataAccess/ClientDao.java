package DataAccess;
import Model.Client;

/**
 * Created by chira on 17.04.2018.
 */
public class ClientDao extends AbstractDao<Client>{
    public String toString(Client c){
        return c.toString();
    }
}
