package InterfataSiControl;
import MonitoredData.Operation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class Gui {
     private Operation op= new Operation();
     private static JTextArea textArea= new JTextArea();

    public Gui() {
        JFrame window = new JFrame("Calculator Polinoame");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setMinimumSize(new Dimension(500, 100));
        window.setPreferredSize(new Dimension(500, 100));
        window.setMaximumSize(new Dimension(500, 100));
        window.setResizable(false);
        window.setLocationRelativeTo(null);

        JPanel panel = new JPanel();//panel principal
        JPanel panel1= new JPanel();
        JPanel panel2= new JPanel();

        JLabel labelText=new JLabel("Distinct days:");

        JButton butonTask1= new JButton("Task1");
        JButton butonTask2= new JButton("Task2");
        JButton butonTask3= new JButton("Task3");
        JButton butonTask4= new JButton("Task4");
        JButton butonTask5= new JButton("Task5");

        panel.setLayout(new GridLayout(0,1));
        panel1.add(labelText);
        panel1.add(textArea);
        panel2.add(butonTask1);
        panel2.add(butonTask2);
        panel2.add(butonTask3);
        panel2.add(butonTask4);
        panel2.add(butonTask5);
        panel.add(panel1);
        panel.add(panel2);
        window.setContentPane(panel);
        window.setVisible(true);

        butonTask1.addActionListener(new ButonTask1());
        butonTask2.addActionListener(new ButonTask2());
        butonTask3.addActionListener(new ButonTask3());
        butonTask4.addActionListener(new ButonTask4());
        butonTask5.addActionListener(new ButonTask5());

    }

    private class ButonTask1 implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            textArea.setText(String.valueOf(op.countDifferentData()));
        }
    }

    private class ButonTask2 implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            op.writeInfoAboutActivity1();
            File file= new File("task2.txt");
            try {
                Desktop.getDesktop().open(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class ButonTask3 implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            op.writeInfoAboutActivity2();
            File file= new File("task3.txt");
            try {
                Desktop.getDesktop().open(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class ButonTask4 implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            op.writeInfoAboutActivity3();
            File file= new File("task4.txt");
            try {
                Desktop.getDesktop().open(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class ButonTask5 implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            op.writeInfoAboutActivity4();
            File file= new File("task5.txt");
            try {
                Desktop.getDesktop().open(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args)
    {
        Gui gui= new Gui();
    }
}
