package InterfataSiControl;
import TimerCounter.Timer1;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by chira on 31.03.2018.
 */
public class Window extends Canvas{
    public static JTextField minArr= new JTextField(10);
    public static JTextField maxArr= new JTextField(10);
    public static JTextField minServ= new JTextField(10);
    public static JTextField maxServ= new JTextField(10);
    static JTextField nrQ= new JTextField(10);
    public static JTextField simTime= new JTextField(10);

    public static JTextArea textArea=new JTextArea(11,80);

    static JRadioButton shortestQueue= new JRadioButton("ShortestQueue");
    static JRadioButton shortestTime= new JRadioButton("ShortestTime");
    static JRadioButton randomStrategy= new JRadioButton("RandomStrategy");

    static JSlider slider=new JSlider(JSlider.HORIZONTAL,0,4,0);

    public static int sliderValue;
    private Interface patrat;
    public Window(Interface patrat){
            this.patrat=patrat;
            JFrame frame= new JFrame("Simulare market");
            frame.setMaximumSize(new Dimension(900,800));
            frame.setPreferredSize(new Dimension(900,800));
            frame.setMinimumSize(new Dimension(900,800));
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);
            frame.setLocationRelativeTo(null);

            JPanel panel=new JPanel();

            JLabel labelArriving=new JLabel("Arriving time interval:");
            JLabel labelService=new JLabel(" Service time interval:");
            JLabel numberOfQueues=new JLabel("Number of queues:");
            JLabel simulationInterval=new JLabel("Simulation Time:");
            JLabel strategyLabel=new JLabel("Strategy:");
            JLabel speed=new JLabel("Speed adjustment:");

            JButton run=new JButton("Run");

            JScrollPane scroll = new JScrollPane(textArea,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);//scroll bar

            //slider
            slider.setMinimum(1);
            slider.setValue(1);
            slider.setMajorTickSpacing(1);
            slider.setPaintTicks(true);
            slider.setPaintLabels(true);

            ButtonGroup bg=new ButtonGroup();//pentru RadioBox
            bg.add(shortestQueue);
            bg.add(shortestTime);
            bg.add(randomStrategy);

            frame.setLayout(new FlowLayout());
            panel.setPreferredSize(new Dimension(900,100));
            patrat.setSize(new Dimension(900,500));
            textArea.setSize(new Dimension(900,200));
            frame.add(panel);

            panel.setLayout(new FlowLayout());
            panel.add(labelArriving);
            panel.add(minArr);
            panel.add(maxArr);

            panel.add(labelService);
            panel.add(minServ);
            panel.add(maxServ);

            panel.add(numberOfQueues);
            panel.add(nrQ);

            panel.add(simulationInterval);
            panel.add(simTime);

            panel.add(speed);
            panel.add(slider);
            slider.addChangeListener(new ControlSlider());

            panel.add(strategyLabel);
            panel.add(shortestQueue);
            panel.add(shortestTime);
            panel.add(randomStrategy);

            panel.add(run);
            run.addActionListener(new ControlRun());

            frame.add(patrat);
            frame.add(scroll);
            frame.setVisible(true);
    }
    public static void main(String[] args) {
        new Interface();
    }

    private class ControlRun implements ActionListener {//metoda in care se porneste intreg programul la apasarea butonului run
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
           patrat.start();
            Timer1 countSeconds = new Timer1();
            countSeconds.start();
        }
    }

    private class ControlSlider implements ChangeListener{
        @Override
        public void stateChanged(ChangeEvent changeEvent) {//metoda prin care controlam sliderul
            sliderValue=slider.getValue();
        }
    }
}
