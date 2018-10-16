import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;

/**
 * Created by catalin on 28.11.2017.
 */
public class Joc extends Canvas implements Runnable{
    private Thread thread;
    Handler handler = new Handler();
    RandomForme generareForme= new  RandomForme(handler);
    public Joc(){
        new Window(this);
        this.addKeyListener(new Keyboard(handler));
        handler.addForma(new Cap(420,300));
        handler.addForma(new Coada(450,300));
        handler.addForma(new Coada(480,300));
        handler.addForma(new Coada(510,300));
        generareForme.randomMar();
        handler.addForma(new Mar(generareForme.getMx(),generareForme.getMy()));
    }
    public synchronized void start() {
        thread=new Thread(this);
        thread.start();
    }
    public synchronized void stop() {
        try{
            thread.join();
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    public void move(){
        handler.move();
        coliziuneCoada();
    }
    public void coliziuneCoada()
    {
            Cap c = (Cap) handler.shape.get(0);
            for (int i = 0; i < handler.shape.size(); i++) {
                if (handler.shape.get(i).getId() == ID.Coada && c.getPozx() == handler.shape.get(i).getPozx() && c.getPozy() == handler.shape.get(i).getPozy() ) {
                    System.exit(1);
                }
            }
        }

    public void render(){
        BufferStrategy bs = this.getBufferStrategy();

        if (bs == null) {
            this.createBufferStrategy(2);
            return;
        }
        Graphics g  = bs.getDrawGraphics();
        g.setColor(Color.BLACK);
        g.fillRect(0,0,900,600);
        handler.render(g);
        HUD.renderScore(g);
        g.dispose();
        bs.show();
    }

    @Override
    public void run() {
        this.requestFocus();
        while(true){
            try {
                Thread.sleep(60);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            render();
            move();
        }
    }
}