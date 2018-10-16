import javax.swing.*;
import java.awt.*;

public class Window extends Canvas{
    public Window(Joc patrat){
        JFrame frame = new JFrame("Snake");
        frame.setMaximumSize(new Dimension(900,600));
        frame.setPreferredSize(new Dimension(900,600));
        frame.setMinimumSize(new Dimension(900,600));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.add(patrat);
        frame.setVisible(true);
        patrat.start();

    }
    public static void main(String[] args){
        new Joc();
    }
}