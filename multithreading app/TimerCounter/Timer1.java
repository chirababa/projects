package TimerCounter;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;
import InterfataSiControl.Window;
/**
 * Created by chira on 01.04.2018.
 */
public class Timer1 {
    public static int secondPassed = 0;
    public static int simulationTime=Integer.parseInt(Window.simTime.getText());
    private Timer timer = new Timer();
    private TimerTask task=new TimerTask() {
        @Override
        public void run() {
            secondPassed++;
            //System.out.println("SecondPassed:"+secondPassed);
            if(secondPassed==simulationTime)
            {
                timer.cancel();
            }
        }
    };
    public void start(){
        timer.scheduleAtFixedRate(task,0,1000/Window.sliderValue);
    }
}

