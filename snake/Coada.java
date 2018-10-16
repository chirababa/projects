import java.awt.*;

/**
 * Created by chira on 29.11.2017.
 */
public class Coada extends Forma {
    public Coada(int pozx, int pozy) {
        super(pozx, pozy,ID.Coada);
    }

    @Override
    public void render(Graphics g) {
        g.setColor(Color.RED);
        g.fillOval(pozx,pozy,30,30);
    }

}
