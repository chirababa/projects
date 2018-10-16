package Bank;
import PersonInfo.Account;
import PersonInfo.Person;

import java.util.List;

public interface BankProc {
    public Person findPerson(int idTitular);

    public int addPerson(Person p);

    public int removePerson(int IdTitular);

    public void updatePerson(int idTitular,String name,String age);

    public List<Person> viewAllPerson();

    public Account findAccount(int idTitular, int numarCont);

    public int addAccount(Account a,int idTitular);

    public int removeAccount(int idTitular,int numarCont);

//    public void updateAccount(int idTitular,int numarCont,int money);

    public List<Account> viewAllAccount();


}
