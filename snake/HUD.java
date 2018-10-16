import java.awt.*;

/**
 * Created by chira on 04.12.2017.
 */
public class HUD {
    public static void renderScore(Graphics g){
        g.setColor(Color.WHITE);
        g.setFont(new Font("arial",1,20));
        g.drawString("Score : "+ Handler.score,20,30);

    }
}
