package InterefataSiControl;

import Testare.TestareUnitara;
import javax.swing.*;
import java.awt.*;

import static javax.swing.JFrame.EXIT_ON_CLOSE;

/**
 * Created by chira on 10.03.2018.
 */

public class Gui {
    static JTextField polinom1= new JTextField(20);//camp in care se indroduce polinomul 1
    static JTextField polinom2= new JTextField(20);//camp in care se introduce polinomul 2
    static JTextField rezultat= new JTextField(30);//camp in care va aparea rezultatul sau mesajul de eroare

    public Gui(){
        JFrame window= new JFrame("Calculator Polinoame");
        window.setDefaultCloseOperation(EXIT_ON_CLOSE);
        window.setSize(700,200);
        JPanel panel=new JPanel();//paneulul principal in care se gasesc asezate celelalte
        JPanel panel1 =new JPanel();
        JPanel panel2=new JPanel();
        JPanel panel3= new JPanel();
        JPanel panel4= new JPanel();
        JPanel panel5= new JPanel();

        //butoane de operatii
        JButton adunare=new JButton("Adunare");
        JButton scadere=new JButton("Scadere");
        JButton derivare=new JButton("Derivare");
        JButton integrare=new JButton("Integrare");
        JButton inmultire=new JButton("Inmultire");

        panel.setLayout(new GridLayout(0,1));
        window.setContentPane(panel);

        JLabel labelPol1=new JLabel("Polinom 1:");
        JLabel labelPol2=new JLabel("Polinom 2:");
        JLabel labelRezultat=new JLabel("Rezultat:");

        panel.add(panel1);
        panel.add(panel5);
        panel.add(panel2);
        panel.add(panel3);
        panel.add(panel4);

        panel1.add(labelPol1);
        panel1.add(polinom1);
        panel1.add(labelPol2);
        panel1.add(polinom2);

        panel5.add(labelRezultat);
        panel5.add(rezultat);

        panel2.add(adunare);
        panel2.add(scadere);

        panel3.add(integrare);
        panel3.add(derivare);

        panel4.add(inmultire);

        adunare.addActionListener(new ControlAdunare());
        scadere.addActionListener(new ControlScadere());
        inmultire.addActionListener(new ControlInmultire());
        integrare.addActionListener(new ControlIntegrare());
        derivare.addActionListener(new ControlDerivare());

        window.setVisible(true);
    }

    public static void main(String[] args)
    {
        Gui start=new Gui();
        TestareUnitara test=new TestareUnitara();
        test.verificareInput();
        test.adunare();
        test.scadere();
        test.inmultire();
        test.derivare();
        test.integrare();
    }

}
