import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Created by chira on 29.11.2017.
 */
public class Keyboard extends KeyAdapter {
    private Handler h;
    int flagFirstMove;
    public Keyboard(Handler h){
        this.h=h;
    }
    public void keyPressed(KeyEvent e){
        int key=e.getKeyCode();
        for(int i=0;i<h.shape.size();i++)
        {
            if(h.shape.get(i).getId()==ID.Cap){
                if(h.shape.get(i).getVelx()==0 && key ==KeyEvent.VK_LEFT){
                    h.shape.get(i).setVelx(-30);
                    h.shape.get(i).setVely(0);
                    flagFirstMove=1;
                }
                if(h.shape.get(i).getVelx()==0 && key ==KeyEvent.VK_RIGHT && flagFirstMove==1){
                    h.shape.get(i).setVelx(30);
                    h.shape.get(i).setVely(0);
                }
                if(h.shape.get(i).getVely()==0 && key ==KeyEvent.VK_UP){
                    h.shape.get(i).setVelx(0);
                    h.shape.get(i).setVely(-30);
                    flagFirstMove=1;
                }
                if(h.shape.get(i).getVely()==0 && key ==KeyEvent.VK_DOWN){
                    h.shape.get(i).setVelx(0);
                    h.shape.get(i).setVely(30);
                    flagFirstMove=1;
                }
            }
            else {
                h.shape.get(i).setVelx(0);
                h.shape.get(i).setVely(0);
            }
        }
    }
}
