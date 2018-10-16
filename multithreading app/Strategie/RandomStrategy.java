package Strategie;
import OperatiiGestionare.Casa;
import java.util.List;
import java.util.Random;

/**
 * Created by chira on 04.04.2018.
 */
public class RandomStrategy implements Strategy{
    @Override
    public int addTask(List<Casa> lista) {//metoda care introduce intr-o coada random
        Random random =new Random();
        return random.nextInt(lista.size()-1);
    }
}
