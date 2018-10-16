package Polinoame;
/**
 * Created by chira on 11.03.2018.
 */


public class Monom implements Comparable{
    private double coeficient;
    private int grad;

    public Monom(double coeficient,int grad)
    {
        this.coeficient=coeficient;
        this.grad=grad;
    }

    public double getCoeficient() {
        return this.coeficient;
    }

    public int getGrad() {return this.grad; }
    public void setGrad(int grad) { this.grad=grad; }
    public void setCoeficient(double coeficient) { this.coeficient=coeficient; }

    @Override
    //metoda suprascrisa pentru a realiza sortarea
    public int compareTo(Object o) {
        Monom m=(Monom) o;
        return Double.compare(m.getGrad(),this.getGrad());
    }

    public String afisare(boolean first){
        String s="";//stringul la care se concateaza fiecare caracter dintr-un monom
        if(this.getCoeficient()>0) {
            if (!first) {
                s += "+";
            }
        }
        if (this.getGrad() > 1) {
            if(this.getCoeficient()==1) {
                s+="x^"+this.getGrad();
            }
            else if(this.getCoeficient()==-1) {
                s+="-x^"+this.getGrad();
            }
            else{
                s+=this.getCoeficient()+"*x^"+this.getGrad();
            }
        }
        else if (this.getGrad() == 0) {
            s += this.getCoeficient();
        }
        else {
            if(this.getCoeficient()==1) {
                s+="x";
            }
            else if(this.getCoeficient()==-1) {
                s+="-x";
            }
            else {
                s += this.getCoeficient() + "*x";
            }
        }
        return s;
    }
}