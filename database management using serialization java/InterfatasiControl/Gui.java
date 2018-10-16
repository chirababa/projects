package InterfatasiControl;
import Bank.Bank;
import PersonInfo.Account;
import PersonInfo.Person;
import PersonInfo.SavingAccount;
import PersonInfo.SpendingAccount;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import static javax.swing.JFrame.EXIT_ON_CLOSE;

/**
 * Clasa Gui este una dintre cele mai complexe cotinand atat partea grafica,dar si o parte din controlul aplicatiei
 */

public class Gui{
    private Bank bank= new Bank();
    private DefaultTableModel modela;
    private DefaultTableModel modelp;
    private int deserialization=0;

    private  String[] tableComboBox={"","Persoane","Accounts"};
    private JComboBox<String> table=new JComboBox<>(tableComboBox);

    private  String[] fieldComboBoxString={"","SavingAccount","SpendingAccount"};
    private JComboBox<String> fieldComboBox=new JComboBox<>(fieldComboBoxString);

    //string care imi arata ce se afla pe comboBox
    private  String item;
    private String comboBoxField;

    private  JTextField field1= new JTextField(10);
    private  JTextField field2= new JTextField(10);
    private  JTextField field3= new JTextField(10);
    private  JTextField field4= new JTextField(10);
    private static JTextArea afisareRezultatFind= new JTextArea();

    private JLabel label1 = new JLabel();
    private JLabel label2 = new JLabel();
    private JLabel label3 = new JLabel();
    private JLabel label4 = new JLabel();

    private JPanel principalPanel= new JPanel();//panel variabil care schimba fieldurile in functie de tabel

    private JTable table1=null;

    public Gui() {

        final JFrame window= new JFrame("Baza de date magazin online");
        final JPanel panel=new JPanel();
        JPanel panel1=new JPanel();
        JPanel panel2=new JPanel();

        //panel pentru afisarea rezultatului in cazul in care s-a gasit informatia cautata;
        JPanel afisareRezultat= new JPanel();
        JLabel labelRez= new JLabel("Info:");
        afisareRezultat.add(labelRez);
        afisareRezultat.add(afisareRezultatFind);


        JLabel tableLabel= new JLabel("Tabel:");
        window.setDefaultCloseOperation(EXIT_ON_CLOSE);
        window.setSize(700,240);

        panel.setLayout(new GridLayout(0,1));
        window.setContentPane(panel);
        panel1.add(tableLabel);
        panel1.add(table);
        panel.add(panel1);
        panel.add(afisareRezultat);

        //butoane de operatii
        JButton insertb=new JButton("Insert");
        JButton updateb=new JButton("Update");
        JButton deleteb=new JButton("Delete");
        JButton viewTableb=new JButton("ViewTable");
        JButton withdraw=new JButton("Withdraw");
        JButton deposit=new JButton("Deposit");

        insertb.addActionListener(new ControlInsert());
        updateb.addActionListener(new ControlUpdate());
        deleteb.addActionListener(new ControlDelete());
        viewTableb.addActionListener(new ControlViewTable());
        withdraw.addActionListener(new ControlWithdraw());
        deposit.addActionListener(new ControlDeposit());

        panel2.add(insertb);
        panel2.add(updateb);
        panel2.add(deleteb);
        panel2.add(viewTableb);
        panel2.add(withdraw);
        panel2.add(deposit);
        panel.add(panel2);
        panel.add(principalPanel);

        window.setVisible(true);
        table.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                item= table.getSelectedItem().toString();
                afisareRezultatFind.setText("");
                field1.setText("");
                field2.setText("");
                field3.setText("");
                field4.setText("");
                fieldComboBox.setSelectedIndex(0);
                if(item.equals("Persoane")) {
                    panel.remove(principalPanel);
                    panel.add(persoanePanel());
                    panel.revalidate();
                    panel.repaint();
                }
                else if(item.equals("Accounts")) {
                    panel.remove(principalPanel);
                    panel.add(accountsPanel());
                    panel.revalidate();
                    panel.repaint();
                }
            }
        });
    }

    /**
     * Aceasta metoda creaza Jtable si il populeaza cu niste informatii din liste
     * @param o
     */
    private void tablemethod(Object o)
    {
        JFrame frame = new JFrame();
        frame.setSize(700,300);
        frame.setVisible(true);
        JScrollPane js;
        String[] columns;
            if(o instanceof Person) {
                List<String >list=bank.retFieldsName(listpAux().get(0));
                 columns = new String[list.size()];
                for (int i = 0; i < list.size(); i++) {
                    columns[i] = list.get(i);
                }
                modelp = new DefaultTableModel(columns, 0);
                table1 = new JTable(modelp);
            }
            else if(o instanceof Account) {
                List<String >list=bank.retFieldsName(listaAux().get(0));
                columns = new String[list.size()];
                for (int i = 0; i < list.size(); i++) {
                    columns[i] = list.get(i);
                }
                modela = new DefaultTableModel(columns, 0);
                table1 = new JTable(modela);
            }
            table1.setPreferredScrollableViewportSize(new Dimension(500, 400));
            js = new JScrollPane(table1, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        table1.addMouseListener(new MouseAdapter() {
            @Override
        public void mouseClicked(MouseEvent mouseEvent) {
        super.mouseClicked(mouseEvent);
        Gui.afisareRezultatFind.setText("");
        if(mouseEvent.getClickCount()==1){
            JTable target=(JTable)mouseEvent.getSource();
            int row=target.getSelectedRow();
            int column=target.getSelectedColumn();
            if(column==0&&target.getModel().equals(modelp)) {
                Gui.afisareRezultatFind.setText("Persoana cu idTitular "+modelp.getValueAt(row,0)+" are "+bank.countAccount(Integer.parseInt(String.valueOf(modelp.getValueAt(row,0))))+" conturi");
            }
            else if(column==0&&target.getModel().equals(modela)){
                Person p=bank.findPerson(Integer.parseInt(String.valueOf(modela.getValueAt(row,0))));
                Gui.afisareRezultatFind.setText(p.toString());
            }
        }
    }
    });
        frame.add(js);
    }


    private List<Person> listpAux(){
        List<Person> list = new ArrayList<>();
        list.add(new Person(23131,"dSADA",12));
        return list;
    }

    private List<Account> listaAux(){
        List<Account> list = new ArrayList<>();
        list.add(new SavingAccount());
        return list;
    }

    private void pressView(Object o) {
            Object[] data = null;
            if (o instanceof Person && !bank.listp.isEmpty()) {
                if(modelp!=null) {
                    modelp.setRowCount(0);
                }
                data = new Object[bank.retFieldsName(bank.listp.get(0)).size()];
                for (int i = 0; i < bank.listp.size(); i++) {
                    for (int j = 0; j < data.length; j++) {
                        data[j] = bank.retFieldsValue(bank.listp.get(i)).get(j);
                    }
                    modelp.addRow(data);
                }
            } else if (o instanceof Account && !bank.lista.isEmpty()) {
                if (modela != null) {
                    modela.setRowCount(0);
                }
                data = new Object[bank.retFieldsName(bank.lista.get(0)).size()];
                for (int i = 0; i < bank.lista.size(); i++) {
                    for (int j = 0; j < data.length; j++) {
                        data[j] = bank.retFieldsValue(bank.lista.get(i)).get(j);
                    }
                    modela.addRow(data);
                }
            }
    }

    private JPanel persoanePanel(){
        label1.setText("IdTitular");
        label2.setText("Nume");
        label3.setText("Age");
        principalPanel.add(label1);
        principalPanel.add(field1);
        principalPanel.add(label2);
        principalPanel.add(field2);
        principalPanel.add(label3);
        principalPanel.add(field3);
        principalPanel.remove(label4);
        principalPanel.remove(fieldComboBox);
        principalPanel.remove(field4);
        return principalPanel;
    }

    private JPanel accountsPanel(){
        label1.setText("IdTitular");
        label2.setText("NrCont");
        label3.setText("Tip");
        label4.setText("                            Money");
        principalPanel.add(label1);
        principalPanel.add(field1);
        principalPanel.add(label2);
        principalPanel.add(field2);
        principalPanel.add(label3);
        principalPanel.add(fieldComboBox);
        principalPanel.add(label4);
        principalPanel.add(field4);
        principalPanel.remove(field3);
        return principalPanel;
    }

    //Inserarea intr-un tabel folosind un buton
    private class ControlInsert implements ActionListener {
        public void actionPerformed(ActionEvent actionEvent) {
            comboBoxField=fieldComboBox.getSelectedItem().toString();
            int i=0;
            if (item.equals("Persoane")) {
                i=bank.addPerson(new Person(Integer.parseInt(field1.getText()), field2.getText(), Integer.parseInt(field3.getText())));
                pressView(bank.viewAllPerson().get(0));
            }
             if (item.equals("Accounts")) {
                         if(comboBoxField.equals("SavingAccount")) {
                             i=bank.addAccount(new SavingAccount(),Integer.parseInt(field1.getText()));
                         }
                         else if(comboBoxField.equals("SpendingAccount")) {
                             i=bank.addAccount(new SpendingAccount(),Integer.parseInt(field1.getText()));
                         }

                     }
            writeError(i);
            pressView(bank.viewAllAccount().get(0));
            bank.serialization();
            }

        }

    //Update
    private class ControlUpdate implements ActionListener {
        public void actionPerformed(ActionEvent actionEvent) {
            if (item.equals("Persoane")) {
                bank.updatePerson(Integer.parseInt(field1.getText()), field2.getText(), field3.getText());
                pressView(bank.viewAllPerson().get(0));
                if(!bank.viewAllAccount().isEmpty()) {
                    pressView(bank.viewAllAccount().get(0));
                }
            }
            bank.serialization();
        }
    }

    //evenimentele ce au loc la apasarea butonului delete, si anume stergerea din hashMap
    private class ControlDelete implements ActionListener {
        public void actionPerformed(ActionEvent actionEvent) {
            int i=0;
            if(item.equals("Persoane")){
                 i=bank.removePerson(Integer.parseInt(field1.getText()));
                bank.viewAllPerson();
                bank.viewAllAccount();
                if(bank.listp.isEmpty()) {
                    modelp.setRowCount(0);
                    modela.setRowCount(0);
                }
                else {
                    pressView(bank.viewAllPerson().get(0));
                    if(bank.viewAllAccount().isEmpty()){
                        modela.setRowCount(0);
                    }
                    else {
                        pressView(bank.viewAllAccount().get(0));
                    }
                }
            }
            else if(item.equals("Accounts")){
                 i=bank.removeAccount(Integer.parseInt(field1.getText()),Integer.parseInt(field2.getText()));
                bank.viewAllAccount();
                if(bank.lista.isEmpty()) {
                    modela.setRowCount(0);
                }
                else {
                    pressView(bank.viewAllAccount().get(0));
                }
            }
            writeError(i);
            bank.serialization();
        }
    }

    //medota care afiseaza la apasarea butonului view continutul unui tabel
    private class ControlViewTable implements ActionListener {
        public void actionPerformed(ActionEvent actionEvent) {
            if(item.equals("Persoane")){
                bank.deserialization();
                if(deserialization==0 && !bank.listp.isEmpty()) {
                    tablemethod(bank.listp.get(0));
                    pressView(bank.listp.get(0));
                    deserialization=1;
                }
                else {
                    bank.viewAllPerson();
                    tablemethod(listpAux().get(0));
                    pressView(listpAux().get(0));
                }
            }
            else if(item.equals("Accounts")){
                bank.deserialization();
                if(deserialization==0 && !bank.lista.isEmpty()) {
                    tablemethod(bank.lista.get(0));
                    pressView(bank.lista.get(0));
                    deserialization=1;
                }
                else {
                    bank.viewAllAccount();
                    tablemethod(listaAux().get(0));
                    pressView(listaAux().get(0));
                }
            }
    }
    }

    //metoda care afiseaza un mesaj dupa fiecare operatie
    private void writeError(int i) {
        if(i==0) {
            Gui.afisareRezultatFind.setText("Operatiune esuata.Verificati datele de intrare");
        }
        else {
            Gui.afisareRezultatFind.setText("Operatiune efectuata cu succes");
        }
    }

    private class ControlWithdraw implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            int i=0;
            if(item.equals("Accounts")){
                Account a=bank.findAccount(Integer.parseInt(field1.getText()),Integer.parseInt(field2.getText()));
                i=a.withdraw(Integer.parseInt(field4.getText()));//variabila care verifica corectitudinea inputului
                if(!bank.viewAllAccount().isEmpty()) {
                    pressView(bank.viewAllAccount().get(0));
                }
            }
            writeError(i);
            bank.serialization();
        }
    }

    private class ControlDeposit implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            int i=0;
            if(item.equals("Accounts")){
                Account a=bank.findAccount(Integer.parseInt(field1.getText()),Integer.parseInt(field2.getText()));
                i=a.deposit(Integer.parseInt(field4.getText()));
                if(!bank.viewAllAccount().isEmpty()) {
                    pressView(bank.viewAllAccount().get(0));
                }
            }
            writeError(i);
            bank.serialization();
        }
    }
    public static void main(String args[]){
        Gui gui= new Gui();
    }
}

