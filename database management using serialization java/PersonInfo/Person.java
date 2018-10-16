package PersonInfo;
import java.io.Serializable;
import java.util.Observable;
import java.util.Observer;

public class Person implements Serializable,Observer {
    private int idTitular;
    private String name;
    private int age;

    public Person(int idTitular, String name, int age) {
        this.idTitular = idTitular;
        this.name = name;
        this.age = age;
    }

    public int getIdTitular() {
        return idTitular;
    }

    public void setIdTitular(int idTitular) {
        this.idTitular = idTitular;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Person person = (Person) o;
        return idTitular == person.idTitular;

    }

    @Override
    public int hashCode() {
        return idTitular;
    }

    @Override
    public String toString() {
        return "Person{" +
                "idTitular=" + idTitular +
                ", name='" + name + '\'' +
                ", age=" + age +
                '}';
    }

    @Override
    public void update(Observable observable, Object o) {
        System.out.println(o);
    }
}
