import java.awt.*;

/**
 * Created by chira on 29.11.2017.
 */
public class Cap extends Forma {
    public Cap(int pozx, int pozy) {
        super(pozx, pozy,ID.Cap);
    }

    @Override
    public void render(Graphics g) {
        g.setColor(Color.ORANGE);
        g.fillOval(pozx,pozy,30,30);
    }

    public  void movecap(){
        this.setPozx(getPozx()+getVelx());
        this.setPozy(getPozy()+getVely());
    }
}
