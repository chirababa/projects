package JUnit;
import Bank.Bank;
import PersonInfo.Person;
import PersonInfo.SavingAccount;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
public class JUnit {
    private Bank bank= new Bank();

    @Test
        public void add(){
        bank.addPerson(new Person(1,"Mihai",20));
        bank.viewAllPerson();
        assertEquals(bank.listp.get(0).getName(), "Mihai");
        bank.addAccount(new SavingAccount(),1);
        bank.viewAllAccount();
        assertEquals(bank.lista.get(0).getTip(),"SavingAccount");
        assertTrue(!bank.lista.isEmpty());
    }

    @Test
    public void delete(){
        bank.addPerson(new Person(1,"Mihai",20));
        bank.addAccount(new SavingAccount(),1);
        bank.viewAllAccount();
        bank.viewAllPerson();
        bank.removeAccount(1,bank.lista.get(0).getNumarCont());
        bank.viewAllAccount();
        assertTrue(bank.lista.isEmpty());
        bank.removePerson(1);
        bank.viewAllPerson();
        assertTrue(bank.listp.isEmpty());
    }

    @Test
    public void update(){
        bank.addPerson(new Person(1,"Andrei",20));
        bank.viewAllPerson();
        bank.updatePerson(1,"Codrut",String.valueOf(25));
        assertEquals(bank.findPerson(1).getName(),"Codrut");
        assertEquals(bank.findPerson(1).getAge(),25);
    }

}
