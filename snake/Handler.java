import java.awt.*;
import java.util.LinkedList;
import java.util.Random;

/**
 * Created by chira on 29.11.2017.
 */
public class Handler {
    static int score;
    int cntAparitieBonus;//dupa 5 mere mancate
    int flag;
    static int timer=50;//timpul care sta bonusul in window
    int scoreBonus=100;

    LinkedList<Forma> shape = new LinkedList<Forma>();
    RandomForme generareRandom=new RandomForme(this);
    public void addForma(Forma f) {
        shape.add(f);
    }

    public void render(Graphics g) {
        for (int i = 0; i < shape.size(); i++) {
            shape.get(i).render(g);
        }
    }

    public void move() {
        int a = 0, b = 0;
        Cap p=(Cap)shape.get(0);
        a = shape.get(0).getPozx();
        b = shape.get(0).getPozy();
        p.movecap();
        if(p.getVelx()!=0||p.getVely()!=0) {
            if (shape.get(0).getPozx() > 899)//conditii pentru revenirea in window in cazul in care sarpele iese din fereastra
            {
                shape.get(0).setPozx(0);
            }
            if (shape.get(0).getPozx() < 0) {
                shape.get(0).setPozx(900);
            }
            if (shape.get(0).getPozy() > 599) {
                shape.get(0).setPozy(0);
            }
            if (shape.get(0).getPozy() < 0) {
                shape.get(0).setPozy(600);
            }
            for (int i = 1; i < shape.size(); i++) {
                if (shape.get(i).getId() == ID.Coada) {
                    int pozcoadax = shape.get(i).getPozx();
                    int pozcoaday = shape.get(i).getPozy();
                    shape.get(i).setPozx(a);
                    shape.get(i).setPozy(b);
                    a = pozcoadax;
                    b = pozcoaday;
                }
            }
        }
    coliziuneMar();
    }
    public void coliziuneMar() {
        Bonus b=null;
        Cap c = (Cap) shape.get(0);
        Coada coada = (Coada) shape.get(3);
        for (int i = shape.size() - 1; i >= 1; i--) {
            if (shape.get(i).getId() == ID.Coada) {
                coada = (Coada) shape.get(i);
                break;
            }
        }
        for (int i = 0; i < shape.size(); i++) {
            if (shape.get(i).getId() == ID.Mar) {
                Mar m = (Mar) shape.get(i);
                if (c.getPozx() == m.getPozx() && c.getPozy() == m.getPozy()) {
                    shape.remove(m);
                    shape.add(new Coada(coada.getPozx() + 30, coada.getPozy()));
                    generareRandom.randomMar();
                    shape.add(new Mar(generareRandom.getMx(), generareRandom.getMy()));
                    score+=10;
                    cntAparitieBonus++;
                    if (cntAparitieBonus % 5 == 0) {
                        generareRandom.randomMar();
                        b = new Bonus(generareRandom.getMx(), generareRandom.getMy());
                        shape.add(b);
                        flag=1;
                    }
                }
            }
            if (shape.get(i).getId() == ID.Bonus) {
                b = (Bonus) shape.get(i);
                if (c.getPozx() == b.getPozx() && c.getPozy() == b.getPozy()) {
                    shape.remove(b);
                    flag=0;
                    timer=50;
                    shape.add(new Coada(coada.getPozx() + 30, coada.getPozy()));
                    score += (scoreBonus+10);
                }
            }
        }
        if(flag==1)
        {
            timer--;
            scoreBonus--;
            if(timer==0)
            {
                shape.remove(b);
                flag=0;
                scoreBonus=50;
                timer=50;
            }
        }
    }

}

