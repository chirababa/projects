package PresentationLayer;
import BussinesLogic.Factura;
import DataAccess.*;
import Model.Client;
import Model.Comanda;
import Model.DetaliiComanda;
import Model.Produs;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;
import java.util.jar.Pack200;

import static javax.swing.JFrame.EXIT_ON_CLOSE;


/**
 * Clasa Gui este una dintre cele mai complexe cotinand atat partea grafica,dar si o parte din controlul aplicatiei
 */
public class Gui {
    private AbstractDao clientDao = new ClientDao();
    private AbstractDao produsDao = new ProdusDao();
    private AbstractDao comadaDao = new ComandaDao();
    private AbstractDao detaliiDao = new DetaliiComandaDao();
    private Factura fac= new Factura(clientDao,produsDao,comadaDao,detaliiDao);

    private  String[] tableComboBox={"","Client","Comanda","DetaliiComanda","Produs"};
    private  JComboBox table=new JComboBox(tableComboBox);

    //string care imi arata ce se afla pe comboBox
    private  String item;

    private  JTextField field1= new JTextField(10);
    private  JTextField field2= new JTextField(10);
    private  JTextField field3= new JTextField(10);
    private  JTextField field4= new JTextField(10);
    private  JTextField field5= new JTextField(10);
    private  JTextField afisareRezultatFind= new JTextField(52);

    private JLabel label1 = new JLabel();
    private JLabel label2 = new JLabel();
    private JLabel label3 = new JLabel();
    private JLabel label4 = new JLabel();
    private JLabel label5 = new JLabel();

    private JPanel principalPanel= new JPanel();//panel variabil care schimba fieldurile in functie de tabel

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

        //panel pentru butonul de finalizare comanda

        JLabel tableLabel= new JLabel("Tabel:");
        window.setDefaultCloseOperation(EXIT_ON_CLOSE);
        window.setSize(700,220);

        panel.setLayout(new GridLayout(0,1));
        window.setContentPane(panel);
        panel1.add(tableLabel);
        panel1.add(table);
        panel.add(panel1);
        panel.add(afisareRezultat);

        //butoane de operatii
        JButton insertb=new JButton("Insert");
        JButton updateb=new JButton("Update");
        JButton findbyIDb=new JButton("FindbyID");
        JButton deleteb=new JButton("DeletebyID");
        JButton viewTableb=new JButton("ViewTable");
        JButton finalizare=new JButton("Finalizare Comanda");

        insertb.addActionListener(new ControlInsert());
        updateb.addActionListener(new ControlUpdate());
        findbyIDb.addActionListener(new ControlFindById());
        deleteb.addActionListener(new ControlDelete());
        viewTableb.addActionListener(new ControlViewTable());
        finalizare.addActionListener(new Finalizare());

        panel2.add(insertb);
        panel2.add(updateb);
        panel2.add(findbyIDb);
        panel2.add(deleteb);
        panel2.add(viewTableb);
        panel2.add(finalizare);
        panel.add(panel2);

        window.setVisible(true);
        table.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                afisareRezultatFind.setText("");
                field1.setText("");
                field2.setText("");
                field3.setText("");
                field4.setText("");
                field5.setText("");
                item= table.getSelectedItem().toString();
                if(item.equals("Client")) {
                    panel.remove(principalPanel);
                    panel.add(clientPanel());
                    panel.revalidate();
                    panel.repaint();
                }
                else if(item.equals("Comanda")) {
                    panel.remove(principalPanel);
                    panel.add(comandPanel());
                    panel.revalidate();
                    panel.repaint();
                }
                else if(item.equals("DetaliiComanda")) {
                    panel.remove(principalPanel);
                    panel.add(detailsPanel());
                    panel.revalidate();
                    panel.repaint();
                }
                else if(item.equals("Produs")) {
                    panel.remove(principalPanel);
                    panel.add(produsPanel());
                    panel.revalidate();
                    panel.repaint();
                }
            }
        });
    }

    private JFrame createFrameForTable(){
        JFrame frame = new JFrame();
        frame.setSize(700,300);
        frame.setVisible(true);
        return frame;
    }

    /**
     * Aceasta metoda este folosita pentru a crea un jTable si al introduce pe un scroll Pane , acesta din urma fiind returnat, pentru a fi adaugat in frame-ul mare
     * @param o
     * @return
     */
    private JScrollPane tablemethod(AbstractDao o)
    {
        JScrollPane js=null;
            o.findAll();
        if(o.listTable.size()>0) {
            List<String> list = o.retFieldsName(o.listTable.get(0));
            String[] colums = new String[list.size()];
            for (int i = 0; i < list.size(); i++) {
                colums[i] = list.get(i);
            }
            o.model = new DefaultTableModel(colums, 0);
            JTable table1 = new JTable(o.model);
            table1.setPreferredScrollableViewportSize(new Dimension(500, 400));
            js = new JScrollPane(table1, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        }
        return js;
    }

    /**
     * Aceasta medota care returneaza void este folosita pentru a reimprospata continutul unui tabel,fiin apelata de fiecare data cand se efectueaza o operatie pe tabele
     * @param o
     */
    private void pressView(AbstractDao o) {
        o.findAll();
        if(o.listTable.size()>0) {
            Object[] data = new Object[o.retFieldsName(o.listTable.get(0)).size()];
            if(o.model!=null) {
                o.model.setRowCount(0);
            }
            for (int i = 0; i < o.listTable.size(); i++) {
                for (int j = 0; j < data.length; j++) {
                    data[j] = o.retFieldsValue(o.listTable.get(i)).get(j);
                }
                o.model.addRow(data);
            }
        }
    }
    //3 paneluri variabile care se schimba de fiecare data cand schimbam comboBoxul
    private JPanel clientPanel(){
        label1.setText("IdClient");
        label2.setText("Name");
        label3.setText("Address");
        label4.setText("Email");
        label5.setText("Age");
        principalPanel.add(label1);
        principalPanel.add(field1);
        principalPanel.add(label2);
        principalPanel.add(field2);
        principalPanel.add(label3);
        principalPanel.add(field3);
        principalPanel.add(label4);
        principalPanel.add(field4);
        principalPanel.add(label5);
        principalPanel.add(field5);
        return principalPanel;
    }
    private JPanel comandPanel(){
        label1.setText("IdComanda");
        label2.setText("IdClient");
        label3.setText("OrderDate");
        principalPanel.add(label1);
        principalPanel.add(field1);
        principalPanel.add(label2);
        principalPanel.add(field2);
        principalPanel.add(label3);
        principalPanel.add(field3);
        principalPanel.remove(field4);
        principalPanel.remove(field5);
        principalPanel.remove(label4);
        principalPanel.remove(label5);
        return principalPanel;
    }

    private JPanel detailsPanel(){
        label1.setText("IdDetaliiComanda");
        label2.setText("IdComanda");
        label3.setText("IdProdus");
        label4.setText("Cantitate");
        principalPanel.add(label1);
        principalPanel.add(field1);
        principalPanel.add(label2);
        principalPanel.add(field2);
        principalPanel.add(label3);
        principalPanel.add(field3);
        principalPanel.add(label4);
        principalPanel.add(field4);
        principalPanel.remove(field5);
        principalPanel.remove(label5);
        return principalPanel;
    }

    private JPanel produsPanel(){
        label1.setText("IdProdus");
        label2.setText("NumeProdus");
        label3.setText("Stoc");
        label4.setText("Distribuitor");
        label5.setText("Pret");
        principalPanel.add(label1);
        principalPanel.add(field1);
        principalPanel.add(label2);
        principalPanel.add(field2);
        principalPanel.add(label3);
        principalPanel.add(field3);
        principalPanel.add(label4);
        principalPanel.add(field4);
        principalPanel.add(label5);
        principalPanel.add(field5);
        return principalPanel;
    }

    //Inserarea intr-un tabel folosind un buton
    private class ControlInsert implements ActionListener {
        public void actionPerformed(ActionEvent actionEvent) {
            if (item.equals("Client")) {
                Client c = new Client(field2.getText(), field3.getText(), field4.getText(), Integer.parseInt(field5.getText()));
                clientDao.insert(c);
                pressView(clientDao);
            } else if (item.equals("Produs")) {
                Produs p = new Produs(field2.getText(), Integer.parseInt(field3.getText()), field4.getText(), Integer.parseInt(field5.getText()));
                produsDao.insert(p);
                pressView(produsDao);
            }
           else if (item.equals("Comanda")) {
                Comanda c = new Comanda(Integer.parseInt(field2.getText()), field3.getText());
                comadaDao.insert(c);
                pressView(comadaDao);
            }
            else if (item.equals("DetaliiComanda")) {
                DetaliiComanda d = new DetaliiComanda(Integer.parseInt(field2.getText()), Integer.parseInt(field3.getText()), Integer.parseInt(field4.getText()));
                Produs p = (Produs) produsDao.findById(Integer.parseInt(field3.getText()));
                System.out.println(d.getCantitate());
                System.out.println(p.getStoc());
                if (d.getCantitate() <= p.getStoc()) {
                    detaliiDao.insert(d);
                    p.setStoc(p.getStoc() - d.getCantitate());
                   String[] s = {p.getNameProd(), String.valueOf(p.getStoc()), p.getDistributor(), String.valueOf(p.getPret())};
                    produsDao.update(p,p.getIdProdus(),s);
                    pressView(detaliiDao);
                    pressView(produsDao);
                    //System.out.println(d.getCantitate());
                    //System.out.println(p.getStoc());
                }
                else {
                    afisareRezultatFind.setText("Stoc insuficient");
                }
            }
        }
    }

    //Update
    private class ControlUpdate implements ActionListener {
        public void actionPerformed(ActionEvent actionEvent) {

            if (item.equals("Client")) {
                String[] s = {field2.getText(), field3.getText(), field4.getText(), field5.getText()};
                clientDao.update(clientDao.findById(Integer.parseInt(field1.getText())), Integer.parseInt((field1.getText())), s);
                pressView(clientDao);
            } else if (item.equals("Produs")) {
                String[] s = {field2.getText(), field3.getText(), field4.getText(), field5.getText()};
                produsDao.update(produsDao.findById(Integer.parseInt(field1.getText())), Integer.parseInt((field1.getText())), s);
                pressView(produsDao);
            }
            else if (item.equals("Comanda")) {
                String[] s = {field2.getText(), field3.getText()};
                comadaDao.update(comadaDao.findById(Integer.parseInt(field1.getText())), Integer.parseInt((field1.getText())), s);
                pressView(comadaDao);
            }
            else if (item.equals("DetaliiComanda")) {
                DetaliiComanda d= (DetaliiComanda) detaliiDao.findById(Integer.parseInt(field1.getText()));
                Produs p1= (Produs) produsDao.findById(d.getIdProdus());
                Produs p = (Produs) produsDao.findById(Integer.parseInt(field3.getText()));

                String[] s = {field2.getText(), field3.getText(), field4.getText()};
                if(p1.getIdProdus()==p.getIdProdus()) {
                    if (Integer.parseInt(field4.getText()) <= p.getStoc() + d.getCantitate()) {
                        p.setStoc(p.getStoc() + d.getCantitate() - Integer.parseInt(field4.getText()));
                        String[] produs = {p.getNameProd(), String.valueOf(p.getStoc()), p.getDistributor(), String.valueOf(p.getPret())};
                        detaliiDao.update(detaliiDao.findById(Integer.parseInt(field1.getText())), Integer.parseInt((field1.getText())), s);
                        produsDao.update(p, p.getIdProdus(), produs);
                        pressView(detaliiDao);
                        pressView(produsDao);
                    } else {
                        afisareRezultatFind.setText("Stoc insuficient");
                    }
                }
                else{
                    if(Integer.parseInt(field4.getText())<=p.getStoc())
                    {
                        p.setStoc(p.getStoc()-Integer.parseInt(field4.getText()));
                        p1.setStoc(p1.getStoc()+d.getCantitate());
                        String[] produs = {p.getNameProd(), String.valueOf(p.getStoc()), p.getDistributor(), String.valueOf(p.getPret())};
                        String[] produs1 = {p1.getNameProd(), String.valueOf(p1.getStoc()), p1.getDistributor(), String.valueOf(p1.getPret())};
                        detaliiDao.update(detaliiDao.findById(Integer.parseInt(field1.getText())), Integer.parseInt((field1.getText())), s);
                        produsDao.update(p,p.getIdProdus(),produs);
                        produsDao.update(p1,p1.getIdProdus(),produs1);
                        pressView(detaliiDao);
                        pressView(produsDao);
                    }
                    else {
                        afisareRezultatFind.setText("Stoc insuficient");
                    }
                }
            }
        }
    }

    // controlul metodei findByID
    private class ControlFindById implements ActionListener {
        public void actionPerformed(ActionEvent actionEvent) {
            if(item.equals("Client")){
                if(clientDao.findById(Integer.parseInt(field1.getText()))==null){
                    afisareRezultatFind.setText("Nu a fost gasit niciun rezultat");
                }
                else{
                    afisareRezultatFind.setText(clientDao.toString(clientDao.findById(Integer.parseInt(field1.getText()))));
                }
            }
            else if(item.equals("Produs")){
                if(produsDao.findById(Integer.parseInt(field1.getText()))==null){
                    afisareRezultatFind.setText("Nu a fost gasit niciun rezultat");
                }
                else{
                    afisareRezultatFind.setText(produsDao.toString(clientDao.findById(Integer.parseInt(field1.getText()))));
                }
            }
            else if(item.equals("Comanda")){
                if(comadaDao.findById(Integer.parseInt(field1.getText()))==null){
                    afisareRezultatFind.setText("Nu a fost gasit niciun rezultat");
                }
                else{
                    afisareRezultatFind.setText(comadaDao.toString(clientDao.findById(Integer.parseInt(field1.getText()))));
                }
            }
            else if(item.equals("DetaliiComanda")){
                if(detaliiDao.findById(Integer.parseInt(field1.getText()))==null){
                    afisareRezultatFind.setText("Nu a fost gasit niciun rezultat");
                }
                else{
                    afisareRezultatFind.setText(detaliiDao.toString(clientDao.findById(Integer.parseInt(field1.getText()))));
                }
            }
        }
    }

    //evenimentele ce au loc la apasarea butonului delete, si anume stergerea din baza de date
    private class ControlDelete implements ActionListener {
        public void actionPerformed(ActionEvent actionEvent) {
            if(item.equals("Client")){
               clientDao.delete(Integer.parseInt(field1.getText()));
                if(clientDao.listTable!=null) {
                    pressView(clientDao);
                }
            }
            else if(item.equals("Produs")){
                produsDao.delete(Integer.parseInt(field1.getText()));
                if(produsDao.listTable!=null) {
                    pressView(produsDao);
                }
            }
            else if(item.equals("Comanda")){
                comadaDao.delete(Integer.parseInt(field1.getText()));
                if(comadaDao.listTable!=null) {
                    pressView(comadaDao);
                }
            }
            else if(item.equals("DetaliiComanda")){
                DetaliiComanda d = (DetaliiComanda) detaliiDao.findById(Integer.parseInt(field1.getText()));
                Produs p = (Produs) produsDao.findById(d.getIdProdus());
                System.out.println(d.getCantitate());
                System.out.println(p.getStoc());
                detaliiDao.delete(Integer.parseInt(field1.getText()));
                    p.setStoc(p.getStoc() + d.getCantitate());
                    String[] s = {p.getNameProd(), String.valueOf(p.getStoc()), p.getDistributor(), String.valueOf(p.getPret())};
                    produsDao.update(p,p.getIdProdus(),s);
                    pressView(detaliiDao);
                    pressView(produsDao);
                if(detaliiDao.listTable!=null) {
                    pressView(detaliiDao);
                }
            }
        }
    }

    //medota care afiseaza la upasarea butonului view continutul unui tabel
    private class ControlViewTable implements ActionListener {
        public void actionPerformed(ActionEvent actionEvent) {
            if(item.equals("Client")){
                clientDao.findAll();
                if(clientDao.listTable.size()>0) {
                    createFrameForTable().add(tablemethod(clientDao));
                    pressView(clientDao);
                }
                else{
                    afisareRezultatFind.setText("Tabelul este gol");
                }
            }
            else if(item.equals("Produs")){
                produsDao.findAll();
                if(produsDao.listTable.size()>0) {
                    createFrameForTable().add(tablemethod(produsDao));
                    pressView(produsDao);
                }
                else{
                    afisareRezultatFind.setText("Tabelul este gol");
                }

            }
            else if(item.equals("Comanda")){
                comadaDao.findAll();
                if(comadaDao.listTable.size()>0) {
                    createFrameForTable().add(tablemethod(comadaDao));
                    pressView(comadaDao);
                }
                else{
                    afisareRezultatFind.setText("Tabelul este gol");
                }

            }
            else if(item.equals("DetaliiComanda")){
                detaliiDao.findAll();
                if(detaliiDao.listTable.size()>0) {
                    createFrameForTable().add(tablemethod(detaliiDao));
                    pressView(detaliiDao);
                }
                else{
                    afisareRezultatFind.setText("Tabelul este gol");
                }

            }
        }
    }
    public static void main(String args[]){
        Gui gui= new Gui();
    }

    private class Finalizare implements ActionListener {
        public void actionPerformed(ActionEvent actionEvent) {
            if(item.equals("Comanda")){
                try {
                    fac.factura(Integer.parseInt(field1.getText()));
                        pressView(detaliiDao);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
