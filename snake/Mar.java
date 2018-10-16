import java.awt.*;
import java.util.Random;

/**
 * Created by chira on 29.11.2017.
 */
public class Mar extends Forma {
    public Mar(int pozx, int pozy) {
        super(pozx, pozy,ID.Mar);
    }

    @Override
    public void render(Graphics g) {
        g.setColor(new Color(75,255,23));
        g.fillOval(pozx,pozy,30,30);
    }

}
