import java.awt.*;

/**
 * Created by chira on 04.12.2017.
 */
public class Bonus extends Forma {

    public Bonus(int pozx, int pozy) {
        super(pozx, pozy, ID.Bonus);
    }

    @Override
    public void render(Graphics g) {
        g.setColor(new Color(125,76,234));
        g.fillOval(pozx,pozy,30,30);

        g.setColor(Color.GREEN);
        g.fillRect(95,45,Handler.timer,20);

        g.setColor(Color.BLUE);
        g.drawRect(96,46,Handler.timer,20);

        g.setColor(Color.WHITE);
        g.setFont(new Font("arial",1,20));
        g.drawString("Bonus: ",10,60);

    }
}
