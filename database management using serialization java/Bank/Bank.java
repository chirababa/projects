package Bank;
import PersonInfo.Account;
import PersonInfo.Person;

import java.io.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @invariant isWellFormed
 */
public class Bank implements BankProc,Serializable {
    private HashMap<Person,List<Account>> map= new HashMap<>();
    public List<Person> listp=new ArrayList<>();
    public List<Account> lista=new ArrayList<>();

    /**
     * @pre idTitular>0
     * @post return !=null
     * @param idTitular
     * @return
     */
    @Override
    public Person findPerson(int idTitular) {
        //assert(idTitular>0);
        //assert isWellFormed();
        Person p=null;
        for(Map.Entry<Person, List<Account>> entry:map.entrySet()){
            p=entry.getKey();
            if(p.getIdTitular()==idTitular)
            {
                break;
            }
            p=null;
        }
        //assert(p!=null);
        //assert isWellFormed();
        return p;
    }

    /**
     * @pre p!=null
     * @post map.size()=map@pre.size()+1
     * @param p
     * @return
     */
    @Override
    public int addPerson(Person p) {
        //assert(p!=null);
        //assert isWellFormed();
        if(p.getIdTitular()>0&&p.getAge()>=1) {
            List<Account> list= new ArrayList<>();
            map.put(p,list);
            //assert isWellFormed();
            return 1;
        }
        return 0;
    }

    /**
     * @pre idTitular>0
     * @post map.size()=map@pre.size()-1
     * @param IdTitular
     * @return
     */
    @Override
    public int removePerson(int IdTitular) {
        //assert(idTitular>0);
        Person p=findPerson(IdTitular);
        assert (p!=null);
        assert isWellFormed();
        if(map.containsKey(p)) {
            map.remove(p);
          //  isWellFormed();
            return 1;
        }
        return 0;
    }

    /**
     * @pre findPerson(idTitular)!=null;
     * @param idTitular
     * @param name
     * @param age
     */
    @Override
    public void updatePerson(int idTitular,String name,String age) {
        Person p=findPerson(idTitular);
        //assert(p!=null);
        //assert isWellFormed();
        if(name!=null) {
            p.setName(name);
            for (Account ac : map.get(p)) {
                ac.setNumeTitular(name);
            }
        }
        if(age!=null) {
            p.setAge(Integer.parseInt(age));
        }
        //assert isWellFormed();
    }

    /**
     * @post lisp.size()=c@pre.size()+1
     * @return
     */
    @Override
    public List<Person> viewAllPerson() {
        listp.clear();
        for(Map.Entry<Person, List<Account>> entry:map.entrySet()){
            Person p=entry.getKey();
            this.listp.add(p);
        }
        return this.listp;
    }

    /**
     * @pre findPerson(idTitular)!=null
     * @post return!=null;
     * @param idTitular
     * @param numarCont
     * @return
     */
    @Override
    public Account findAccount(int idTitular,int numarCont) {
        Account a=null;
        Person p=findPerson(idTitular);
        //assert (p!=null);
        if(p!=null&&map.get(p)!=null) {
            for (Account ac : map.get(p)) {
                if (ac.getNumarCont() == numarCont) {
                    a = ac;
                    break;
                }
            }
        }
        //assert(a!=null);
        return a;
    }

    /**
     * @pre findPerson(idTitular)
     * @post map.get(p).size()=map.get(p)@pre.size()+1
     * @param a
     * @param idTitular
     * @return
     */
    @Override
    public int addAccount(Account a, int idTitular) {
        Person p=findPerson(idTitular);
        // assert (p!=null);
        //assert isWellFormed();
            if (map.containsKey(p)) {
                a.setIdTitular(p.getIdTitular());
                a.setNumeTitular(p.getName());
                map.get(p).add(a);
                a.addObserver(p);
                //assert isWellFormed();
                return 1;
            }

        return 0;
    }

    /**
     * @pre findAccount(idTitular,numarCont)!=null
     * @post map.get(p).size()=map.get(p)@pre.size()-1
     * @param idTitular
     * @param numarCont
     * @return
     */
    @Override
    public int removeAccount(int idTitular,int numarCont) {
        Account a = findAccount(idTitular,numarCont);
        //assert(a!=null);
        //assert isWellFormed();
        if(a!=null) {
            map.get(findPerson(idTitular)).remove(a);
            //assert isWellFormed();
            return 1;
        }
        return 0;

    }

    /**
     * @post lisa.size()=c@pre.size()+1
     * @return
     */
    @Override
    public List<Account> viewAllAccount() {
        lista.clear();
        for(Map.Entry<Person, List<Account>> entry:map.entrySet()){
            Person p=entry.getKey();
            this.lista.addAll(map.get(p));
        }
        return this.lista;
    }

    public  List<String> retFieldsName(Object o) {
        ArrayList<String> ret = new ArrayList<String>();
        if(!o.getClass().getSuperclass().getSimpleName().equals("Object")){
            for (Field field : o.getClass().getSuperclass().getDeclaredFields()){
                field.setAccessible(true);
                ret.add(field.getName());
            }
        }
        else{
            for (Field field : o.getClass().getDeclaredFields()){
                field.setAccessible(true);
                ret.add(field.getName());
            }
        }
        return ret;
    }

    public List<String> retFieldsValue(Object o) {
        ArrayList<String> ret = new ArrayList<String>();
        Field[] listFields;
        if(!o.getClass().getSuperclass().getSimpleName().equals("Object")){
            listFields=o.getClass().getSuperclass().getDeclaredFields();
        }
        else {
            listFields=o.getClass().getDeclaredFields();
        }
        for (Field field : listFields) {
            field.setAccessible(true);
            Object value;
            try {
                value = field.get(o);
                ret.add(String.valueOf(value));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return ret;
    }
    public int countAccount(int idTitular){
        Person p= findPerson(idTitular);
        int size=0;
        if(p!=null) {
            if (map.containsKey(p)) {
               size= map.get(p).size();
            }
        }
        return size;
    }

    public void serialization(){
        try {
            FileOutputStream fout= new FileOutputStream("adress.ser",false);
            ObjectOutputStream oos= new ObjectOutputStream(fout);
            oos.writeObject(listp);
            oos.writeObject(lista);
            oos.close();
            fout.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void deserialization(){
        ObjectInputStream objectInputStream=null;
        try {
            FileInputStream streamIn= new FileInputStream("adress.ser");
            objectInputStream= new ObjectInputStream(streamIn);
            listp=(List<Person>) objectInputStream.readObject();
            lista=(List<Account>) objectInputStream.readObject();
            objectInputStream.close();
            streamIn.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        for(Person p:listp) {
            addPerson(p);
        }
        for(Account ac:lista) {
            addAccount(ac,ac.getIdTitular());
            ac.addObserver(findPerson(ac.getIdTitular()));
        }
    }
    private boolean isWellFormed(){
        Person p;
        for(Map.Entry<Person, List<Account>> entry:map.entrySet()){
            p=entry.getKey();
            if(p.getAge()<0&&p.getIdTitular()<0) {
                return false;
            }
            for (Account ac : map.get(p)) {
               if(ac.getMoney()<0&&(!ac.getTip().equals("SavingAccount")||!ac.getTip().equals("SpendingAccount"))){
                   return false;
                }
            }
        }
        return true;
    }
}
