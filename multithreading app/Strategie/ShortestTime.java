package Strategie;
import OperatiiGestionare.Casa;
import java.util.List;
/**
 * Created by chira on 04.04.2018.
 */
public class ShortestTime implements Strategy {
    @Override
    public int addTask(List<Casa> lista) {//metoda care introduce in coada in care este cel mai putin timp de asteptare
        int index=0,sumServiceTime,min=0xFFFF;
        for(int i=0;i<lista.size();i++)
        {
            sumServiceTime=0;
            for(int j=0;j<lista.get(i).casa.size();j++)
            {
                sumServiceTime+=lista.get(i).casa.get(j).getServiceTime();
            }
            if(min>sumServiceTime)
            {
                min=sumServiceTime;
                index=i;
            }
        }
        return index;
    }
}
