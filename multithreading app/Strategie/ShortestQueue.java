package Strategie;
import OperatiiGestionare.Casa;
import java.util.List;
/**
 * Created by chira on 03.04.2018.
 */
public class ShortestQueue implements Strategy {
    @Override
    public int addTask(List<Casa> lista) {//metoda care introduce in coada cu cei mai putini clienti
        int index=0;
        int min=lista.get(0).casa.size();
        for(int i=1;i<lista.size();i++)
        {
            if(min>lista.get(i).casa.size()) {
                min = lista.get(i).casa.size();
                index=i;
            }
        }
        return index;
    }
}
