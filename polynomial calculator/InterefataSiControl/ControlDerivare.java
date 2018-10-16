package InterefataSiControl;

import Operatii.OperatiiPolinom;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
/**
 * Created by chira on 15.03.2018.
 */

class ControlDerivare implements ActionListener {

    private OperatiiPolinom testare = new OperatiiPolinom();

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if (testare.regexVerif(Gui.polinom1.getText()) != null) {
            Gui.rezultat.setText(testare.afisarePolinom(testare.derivare(testare.extract(testare.regexVerif(Gui.polinom1.getText())))));
        }
        else{
            Gui.rezultat.setText("Polinomul introdus nu este corect");
        }
    }
}
