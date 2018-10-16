package InterefataSiControl;

import Operatii.OperatiiPolinom;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by chira on 10.03.2018.
 */
class ControlAdunare implements ActionListener {

    private OperatiiPolinom testare= new OperatiiPolinom();

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if (testare.regexVerif(Gui.polinom1.getText()) != null && testare.regexVerif(Gui.polinom2.getText()) != null) {
            Gui.rezultat.setText(testare.afisarePolinom(testare.adunare(testare.extract(testare.regexVerif(Gui.polinom1.getText())), testare.extract(testare.regexVerif(Gui.polinom2.getText())))));
        }
        else {
            //cazul in care se introduce cel putin un polinom incorect
            Gui.rezultat.setText("Polinomul introdus nu este corect");
        }
    }
}