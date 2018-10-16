package Operatii;

import Polinoame.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * Created by chira on 11.03.2018.
 */
    public class OperatiiPolinom {

    public String regexVerif(String intrare) {//verifica daca intrare este valida
        String regex = "(^\\d+\\*x\\^\\d+)|([\\-+]\\d+\\*x\\^\\d+)|([+\\-]x\\^\\d+)|(^x\\^\\d+)|([+\\-]x)|(^x)|(^\\d+\\*x)|([+\\-]\\d+\\*x)|([+\\-]\\d+)|(^\\d+)";
    String regexConcatGroups = "";//string in care se concateneaza grupurile cu care se face match
        Pattern p = Pattern.compile(regex);
        Matcher regexMatcher = p.matcher(intrare);

        while (regexMatcher.find()) {
            regexConcatGroups += regexMatcher.group();
        }
        if (!regexConcatGroups.equals(intrare) | regexConcatGroups.equals("")) {
            return null;
        }
        return intrare;
    }

    public Polinom extract(String intrare) {//metoda de extragere a coeficientului si a gradului dintr-un string
        if (intrare != null) {
            Polinom p = new Polinom();
            double coeficient = 0;
            int grad = 0;
            String regexExtract = "([+\\-])?(\\d+)?(\\*)?(x)?(\\^)?(\\d+)?";
            Pattern regexPattern = Pattern.compile(regexExtract);
            Matcher regexExtractMatcher = regexPattern.matcher(intrare);
            String s="";//string in care se concateneaza grupurile cu care se face match pentru a putea afla daca s-a ajuns sau nu la finalul stringului de intrare
            while (regexExtractMatcher.find()&&!s.equals(intrare)) {
                s += regexExtractMatcher.group();
                if(regexExtractMatcher.group(2)!=null) {//verificam daca intrarea contine coeficient
                    coeficient = (regexExtractMatcher.group(1) != null && regexExtractMatcher.group(1).equals("-")) ?
                            -1 * Double.parseDouble(String.valueOf(regexExtractMatcher.group(2))) : Double.parseDouble(String.valueOf(regexExtractMatcher.group(2)));
                    //verificam daca coefientul are sau nu semn pentru a-l putea extrage
                        if (regexExtractMatcher.group(4) != null) { //verificam daca intrarea contine "x"
                                grad = (regexExtractMatcher.group(5) != null) ? Integer.parseInt(String.valueOf(regexExtractMatcher.group(6))) : 1;
                        }
                        else{
                            grad=0;
                        }
                    }
                else {
                    coeficient = (regexExtractMatcher.group(1) != null && regexExtractMatcher.group(1).equals("-")) ? -1 : 1;
                    grad = (regexExtractMatcher.group(5) != null) ? Integer.parseInt(String.valueOf(regexExtractMatcher.group(6))) : 1;
                }
                p.listaMonoame.add(new Monom(coeficient, grad));//adaugam monomul obtinut in lista de polinoame
                }
            return p;
        }
        return null;
    }

    private Polinom restrangere(Polinom p1) {
        if (p1 == null) {
            return null;
        }
        ArrayList<Monom> zeroArray = new ArrayList<>();//aray care contine doar monoamele cu coeficientul 0
        for (Monom m : p1.listaMonoame) {
                List<Monom> subList = p1.listaMonoame.subList(p1.listaMonoame.indexOf(m) + 1, p1.listaMonoame.size());//lista auxiliara care parcurge de la pozitia initiala+1 a listei principale
            for (Monom m1 : subList) {
                if (m.getGrad() == m1.getGrad()) {
                    m.setCoeficient(m.getCoeficient() + m1.getCoeficient());
                    m1.setCoeficient(0);
                    zeroArray.add(m1);
                }
            }
            if(m.getCoeficient()==0)
            {
                zeroArray.add(m);
            }
        }
        p1.listaMonoame.removeAll(zeroArray);//sterge din polinom monoamele cu coeficientul 0
        //in cazul in care toate elementele din polinom s-au sters introducem un monom cu coeficient si grad 0 pentru a afisa 0
        if(p1.listaMonoame.size()==0){
            p1.addMonom(new Monom(0,0));
        }
        return p1;
    }

    public Polinom adunare(Polinom p1, Polinom p2) {
        if (p1 == null || p2 == null) {
            return null;
        }
        Polinom p = new Polinom();
        //copiem monoamele din primul polinom in cel care urmeaza sa fie returnat
        for (Monom m : p1.listaMonoame) {
            p.addMonom(new Monom(m.getCoeficient(),m.getGrad()));
        }
        //copiem monoamele din al doilea polinom in cel care urmeaza sa fie returnat
        for (Monom m : p2.listaMonoame) {
            p.addMonom(new Monom(m.getCoeficient(),m.getGrad()));
        }
        restrangere(p);
        return p;
    }

    public Polinom scadere(Polinom p1, Polinom p2) {
        if (p1 == null || p2 == null) {
            return null;
        }
        Polinom p;
        //schimbam semnul coeficientilor celui de-al doilea polinom
        for (Monom m : p2.listaMonoame) {
            m.setCoeficient(-1 * m.getCoeficient());
        }
        p = adunare(p1, p2);
        return p;
    }

    public Polinom inmultire(Polinom p1, Polinom p2){
        if (p1 == null || p2 == null) {
            return null;
        }
        Polinom p=new Polinom();
        for(Monom m1:p1.listaMonoame)
        {
            for(Monom m2:p2.listaMonoame)
            {
                //pentru fiecare doua monoame inmultim coeficientii si adunam gradele;
                p.addMonom(new Monom((m1.getCoeficient()*m2.getCoeficient()),m1.getGrad()+m2.getGrad()));
            }
        }
        return p;
    }

    public Polinom derivare(Polinom p1)
    {
        if(p1==null){
            return null;
        }
        for(Monom m:p1.listaMonoame)
        {
            m.setCoeficient(m.getCoeficient()*m.getGrad());
            m.setGrad(m.getGrad()-1);
        }
        restrangere(p1);
        return p1;
    }

    public Polinom integrare(Polinom p1)
    {
        if(p1==null) {
            return null;
        }
        for(Monom m:p1.listaMonoame)
        {
            m.setCoeficient(m.getCoeficient()/(m.getGrad()+1));
            m.setGrad(m.getGrad()+1);
        }
        restrangere(p1);
        return p1;
    }

    public String afisarePolinom(Polinom polinom) {
        if (polinom == null) {
            return null;
        }
        Collections.sort(polinom.listaMonoame);//sortam polinomul descrescator in functie de grad
        String s = "";
        boolean first=true;//variabila care detecteaza primul monom din polinom
        for(Monom m:polinom.listaMonoame) {
            s+=m.afisare(first);
            first=false;
        }
        return s;
    }
}


