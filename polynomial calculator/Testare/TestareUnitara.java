package Testare;

import Operatii.OperatiiPolinom;
import Polinoame.Monom;
import Polinoame.Polinom;
import org.junit.Test;
import java.util.Collections;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by chira on 11.03.2018.
 */
public class TestareUnitara {
    private OperatiiPolinom testare = new OperatiiPolinom();
    @Test
    //functie de test pentru verificare corectitudinii inputului
    public void verificareInput() {
        assertTrue(testare.regexVerif("") == null);
        assertTrue(testare.regexVerif("3*x^2") != null);
        assertTrue(testare.regexVerif("x^2+1-2") != null);
        assertTrue(testare.regexVerif("x^2+1-2+4*x^500") != null);
        assertTrue(testare.regexVerif("xx") == null);
        assertTrue(testare.regexVerif("x-") == null);
        assertTrue(testare.regexVerif("x^2") != null);
        assertTrue(testare.regexVerif("-x^3") != null);
        assertTrue(testare.regexVerif("4") != null);
        assertTrue(testare.regexVerif("4-") == null);
        assertTrue(testare.regexVerif("-4-") == null);
        assertTrue(testare.regexVerif("x^") == null);
        assertTrue(testare.regexVerif("^x^") == null);
        assertTrue(testare.regexVerif("^x") == null);
        assertTrue(testare.regexVerif("^") == null);
        assertTrue(testare.regexVerif("-4-x") != null);
        assertTrue(testare.regexVerif("-4-4") != null);
        assertTrue(testare.regexVerif("2*x") != null);
        assertTrue(testare.regexVerif("-4*x") != null);
        assertTrue(testare.regexVerif("2*x^2--") == null);
        assertTrue(testare.regexVerif("31a") == null);
    }

    @Test
    //verificarea adunarii
    public void adunare() {
        assertEquals(testare.afisarePolinom(testare.adunare(testare.extract(testare.regexVerif("3*x^2")),testare.extract(testare.regexVerif("5*x^2")))),"8.0*x^2");
        assertEquals(testare.afisarePolinom(testare.adunare(testare.extract(testare.regexVerif("3*x^2")),testare.extract(testare.regexVerif("5*x^2-15*x")))),"8.0*x^2-15.0*x");
    }

    @Test
    //verificarea scaderii
    public void scadere() {
        assertEquals(testare.afisarePolinom(testare.scadere(testare.extract(testare.regexVerif("x^2-2")),testare.extract(testare.regexVerif("2*x^2-15*x")))),"-x^2+15.0*x-2.0");
        assertEquals(testare.afisarePolinom(testare.scadere(testare.extract(testare.regexVerif("5*x")),testare.extract(testare.regexVerif("5*x^7+4*x")))),"-5.0*x^7+x");
    }

    @Test
    //verificare inmultirii
    public void inmultire(){
        assertEquals(testare.afisarePolinom(testare.inmultire(testare.extract(testare.regexVerif("x^2")),testare.extract(testare.regexVerif("5*x^2-15*x")))),"5.0*x^4-15.0*x^3");
        assertEquals(testare.afisarePolinom(testare.inmultire(testare.extract(testare.regexVerif("5*x")),testare.extract(testare.regexVerif("5*x^7-15*x")))),"25.0*x^8-75.0*x^2");
    }

    @Test
    //verificare derivarii
    public void derivare(){
        assertEquals(testare.afisarePolinom(testare.derivare(testare.extract(testare.regexVerif("3*x^2+5*x^7")))),"35.0*x^6+6.0*x");
    }

    @Test
    //verificare integrarii
    public void integrare(){
        assertEquals(testare.afisarePolinom(testare.integrare(testare.extract(testare.regexVerif("3*x^2+5*x")))),"x^3+2.5*x^2");
    }

}
