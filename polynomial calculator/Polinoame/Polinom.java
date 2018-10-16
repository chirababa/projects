package Polinoame;
/**
 * Created by chira on 11.03.2018.
 */

import java.util.ArrayList;
public class Polinom {
    public ArrayList <Monom> listaMonoame = new ArrayList<>();//lista de monoame care reprezinta defapt polinomul

    public void addMonom(Monom mon) {
        listaMonoame.add(mon);
    }
}