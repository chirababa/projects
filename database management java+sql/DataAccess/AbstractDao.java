package DataAccess;

import javax.swing.table.DefaultTableModel;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Aceasta clasa este una generica. Este folosita pentru a efectua operatii pe orice tip de tabela, indiferent daca acestea au numarul variabil de campuri.Intreaga clasa cotine metode
 * care au fost implementate folosind tehnici de reflexie
 * @param <T>
 */
public class AbstractDao<T> {
    private static final Logger LOGGER = Logger.getLogger(AbstractDao.class.getName());
    public List<T> listTable = new ArrayList<T>();
    public DefaultTableModel model;
    private final Class<T> type;

    @SuppressWarnings("unchecked")
    AbstractDao() {
        this.type = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    private StringBuilder generateInsertStatementString(T object) {
        List<String> retFieldsname = retFieldsName(object);
        StringBuilder s = new StringBuilder();
        s.append("Insert into ").append(object.getClass().getSimpleName()).append("(");
        for (int i = 1; i < retFieldsname.size(); i++) {
            if (i == retFieldsname.size() - 1) {
                s.append(retFieldsname.get(i));
            } else {
                s.append(retFieldsname.get(i)).append(",");
            }
        }
        s.append(")" + "VALUES(");
        for (int i = 0; i < retFieldsname.size() - 1; i++) {
            if (i == retFieldsname.size() - 2) {
                s.append("?");
            } else {
                s.append("?,");
            }
        }
        s.append(")");
        return s;
    }

    private String generateUpdateStatementString(int id) {
        int cnt = 0;
        StringBuilder s = new StringBuilder();
        s.append("UPDATE ").append(type.getSimpleName()).append(" SET ");
        for (Field field : type.getDeclaredFields()) {
            if (cnt < type.getDeclaredFields().length - 1) {
                if(cnt>=1) {
                    s.append(field.getName()).append("=?, ");
                }
            } else {
                s.append(" ").append(field.getName()).append("=?");
            }
            cnt++;
        }
        s.append(" WHERE id").append(type.getSimpleName()).append("=").append(id);
        return s.toString();
    }

    private String generateDeleteStatementString() {
        return "DELETE FROM " + type.getSimpleName() + " where id" + type.getSimpleName() + " =?";
    }

    private String generateSelectStatementString() {
        return "SELECT * FROM " + type.getSimpleName() + " where id" + type.getSimpleName() + "=?";
    }

    private String generateSelectAllStatementString() {
        return "SELECT * FROM " + type.getSimpleName();
    }

    /**
     * Aceasta returneaza numele campurilor obiectului dat ca parametru.
      * @param o
     * @return
     */
    public  List<String> retFieldsName(Object o) {
        ArrayList<String> ret = new ArrayList<String>();
        for (Field field : o.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            ret.add(field.getName());
        }
        return ret;
    }

    /**
     * Aceasta metoda primeste ca parametru un obiect si returneaza o lista cu toate valorile campurilor obiectului dat ca parametru.In acest caz am folosit reflexie
     * @param o
     * @return
     */
    public List<String> retFieldsValue(Object o) {
        ArrayList<String> ret = new ArrayList<String>();
        for (Field field : o.getClass().getDeclaredFields()) {
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

    /**
     * Aceasta returneaza o lista de obiecte cu tot cu caracteristicile sale, folosindu-se tehnica de reflexie
     * @param resultSet
     * @return
     */
    private List<T> createObject(ResultSet resultSet) {
        List<T> list = new ArrayList<T>();
        T instance;
        try {
            while (resultSet.next()) {
                instance = type.newInstance();
                for (Field field : type.getDeclaredFields()) {
                    Object value = resultSet.getObject(field.getName());
                    PropertyDescriptor pd = new PropertyDescriptor(field.getName(), type);
                    Method method = pd.getWriteMethod();
                    method.invoke(instance, value);
                }
                if (instance != null) {
                    list.add(instance);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IntrospectionException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * findAll este o functie care are la baza o interogare a bazei de date si are rolul de a returna intreaga informatie dintr-un tabel.
     * @return
     */
    public List<T> findAll() {
        Connection dbConnection = DatabaseConnection.getConnection();
        PreparedStatement findStatement;
        ResultSet rs;
        try {
            findStatement = dbConnection.prepareStatement(generateSelectAllStatementString());
            rs = findStatement.executeQuery();
            this.listTable = createObject(rs);
            while (rs.next()) {
                this.listTable = createObject(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            LOGGER.log(Level.WARNING, "CliendDAO:findAllId " + e.getMessage());
        }
        return this.listTable;
    }

    /**
     * Folosind tehnica de reflexie, aceasta metoda gaseste un obiect dupa un id specificat si returneaza acel obiect
     * @param id
     * @return
     */
    public T findById(int id) {
        Connection dbConnection = DatabaseConnection.getConnection();
        PreparedStatement findStatement = null;
        ResultSet rs = null;
        List<T> object;
        try {
            findStatement = dbConnection.prepareStatement(generateSelectStatementString());
            findStatement.setInt(1, id);
            rs = findStatement.executeQuery();
            object = createObject(rs);
            if (object.size() != 0) {
                return object.get(0);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "CliendDAO:findById " + e.getMessage());
        } finally {
            DatabaseConnection.close(rs);
            DatabaseConnection.close(findStatement);
            DatabaseConnection.close(dbConnection);
        }
        return null;
    }

    /**
     * Are la baza ,de asemenea o interogare specifica bazelor de date, avand rolul de a insera in orice tabela, indiferent de forma,intrucat am folosit reflexie
     * @param object
     * @return
     */
    public int insert(T object) {
        List<String> retFieldsvalue = retFieldsValue(object);
        Connection dbConnection = DatabaseConnection.getConnection();
        PreparedStatement insertStatement = null;
        int insertedId = -1;
        try {
            insertStatement = dbConnection.prepareStatement(String.valueOf(generateInsertStatementString(object)), Statement.RETURN_GENERATED_KEYS);
            for (int i = 1; i < retFieldsvalue.size(); i++) {
                insertStatement.setString(i, retFieldsvalue.get(i));
            }
            insertStatement.executeUpdate();
            ResultSet rs = insertStatement.getGeneratedKeys();
            if (rs.next()) {
                insertedId = rs.getInt(1);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, object.getClass().getName() + ":insert " + e.getMessage());
        } finally {
            DatabaseConnection.close(insertStatement);
            DatabaseConnection.close(dbConnection);
        }
        return insertedId;
    }

    /**
     * Operatie inversa inserarii, aceasta metoda sterge un obiect dintr-un model
     * @param id
     */
    public void delete(int id) {
        Connection dbConnection = DatabaseConnection.getConnection();
        PreparedStatement findStatement = null;
        try {
            findStatement = dbConnection.prepareStatement(generateDeleteStatementString());
            findStatement.setLong(1, id);
            findStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "Table:delete " + e.getMessage());
        } finally {
            DatabaseConnection.close(findStatement);
            DatabaseConnection.close(dbConnection);
        }
    }

    /**
     * Updateza campurile unui obiect specificat printr-un id
     * @param gasit
     * @param id
     * @param s
     */
    public void update(T gasit, int id,String[] s) {
        if(gasit!=null) {
            List<String> retFieldsvalue = retFieldsValue(gasit);
            Connection dbConnection = DatabaseConnection.getConnection();
            PreparedStatement updateStatement;
            try {
                updateStatement = dbConnection.prepareStatement(generateUpdateStatementString(id));
                for (int i = 1; i < retFieldsvalue.size(); i++) {
                    updateStatement.setString(i, s[i-1]);
                }
                updateStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public String toString(T object){
        return object.toString();
    }
}

