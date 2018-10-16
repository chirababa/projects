package BussinesLogic;

import DataAccess.AbstractDao;
import Model.Client;
import Model.Comanda;
import Model.DetaliiComanda;
import Model.Produs;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Factura {
    private AbstractDao clientDao;
    private AbstractDao produsDao;
    private AbstractDao comadaDao;
    private AbstractDao detaliiDao;

    public Factura(AbstractDao clientDao, AbstractDao produsDao, AbstractDao comadaDao, AbstractDao detaliiDao) {
        this.clientDao = clientDao;
        this.produsDao = produsDao;
        this.comadaDao = comadaDao;
        this.detaliiDao = detaliiDao;
    }

    /**
     * Metoda cu care se termina o comanda. Prin aceasta am scris intr-un PDF toate informatiile necesare legate de comanda.Aceasta face parte din bussinesLogic intrucat se ocupa de
     * o parte din logica aplicatiei
     * @param id
     * @throws IOException
     */
    public void factura(int id) throws IOException {
        Random random = new Random();
        int x= random.nextInt(1000);
        int pretTotal = 0;

        //pentru a identifica comada si a obtine in functie de aceasta celelalte informatii
        Comanda c= (Comanda)comadaDao.findById(id);
        Client client=(Client) clientDao.findById(c.getIdClient());

        //lista care retine informatii despre comanda
        List<DetaliiComanda> detaliiComanda;
        detaliiComanda=detaliiDao.findAll();

        //pentru a sterge produsele din baza de date comanda impreuna cu detaliile acesteia
        List<DetaliiComanda> aux= new ArrayList<DetaliiComanda>();


        List<DetaliiComanda> list = null;
        System.out.println("Create a recipe");
        String fileName="factura.pdf";
        PDDocument doc= new PDDocument();
        PDPage page= new PDPage();

        doc.addPage(page);
        PDPageContentStream content= new PDPageContentStream(doc,page);

        content.beginText();
        content.setFont(PDType1Font.HELVETICA,25);
        content.moveTextPositionByAmount(250,550);
        content.drawString("Factura nr."+x);
        content.endText();

        content.beginText();
        content.setFont(PDType1Font.TIMES_ROMAN,16);
        content.moveTextPositionByAmount(400,700);
        content.drawString("Data plasarii :");
        content.endText();

        content.beginText();
        content.setFont(PDType1Font.TIMES_ROMAN,14);
        content.moveTextPositionByAmount(500,700);
        content.drawString(c.getOrderDate());
        content.endText();

        content.beginText();
        content.setFont(PDType1Font.TIMES_ROMAN,16);
        content.moveTextPositionByAmount(50,700);
        content.drawString("Nume Cumparator:");
        content.endText();

        content.beginText();
        content.setFont(PDType1Font.HELVETICA,14);
        content.moveTextPositionByAmount(180,700);
        content.drawString(client.getName()+"");
        content.endText();

        content.beginText();
        content.setFont(PDType1Font.TIMES_ROMAN,16);
        content.moveTextPositionByAmount(50,670);
        content.drawString("Adresa:");
        content.endText();

        content.beginText();
        content.setFont(PDType1Font.TIMES_ROMAN,16);
        content.moveTextPositionByAmount(40,450);
        content.drawString("Denumirea produselor ");
        content.endText();

        content.beginText();
        content.setFont(PDType1Font.TIMES_ROMAN,16);
        content.moveTextPositionByAmount(220,450);
        content.drawString("Distribuitor");
        content.endText();

        content.beginText();
        content.setFont(PDType1Font.TIMES_ROMAN,16);
        content.moveTextPositionByAmount(340,450);
        content.drawString("Cantitatea ");
        content.endText();

        content.beginText();
        content.setFont(PDType1Font.TIMES_ROMAN,16);
        content.moveTextPositionByAmount(480,450);
        content.drawString("Pret(lei)");
        content.endText();


        for(int i=0;i<detaliiComanda.size();i++)
        {
            if(detaliiComanda.get(i).getIdComanda()==c.getIdComanda()) {
                Produs p= (Produs) produsDao.findById(detaliiComanda.get(i).getIdProdus());
                aux.add(detaliiComanda.get(i));
                content.beginText();
                content.setFont(PDType1Font.TIMES_ROMAN,16);
                content.moveTextPositionByAmount(240,380-i*20);
                content.drawString(p.getDistributor()+"");
                content.endText();

                content.beginText();
                content.setFont(PDType1Font.HELVETICA,14);
                content.moveTextPositionByAmount(100,670);
                content.drawString(client.getAddress()+"");
                content.endText();

                content.beginText();
                content.setFont(PDType1Font.HELVETICA,14);
                content.moveTextPositionByAmount(80,380-i*20);
                content.drawString(p.getNameProd()+"");
                content.endText();

                content.beginText();
                content.setFont(PDType1Font.HELVETICA,14);
                content.moveTextPositionByAmount(500,380-i*20);
                content.drawString(p.getPret()+"");
                content.endText();

                content.beginText();
                content.setFont(PDType1Font.HELVETICA,14);
                content.moveTextPositionByAmount(370,380-i*20);
                content.drawString(detaliiComanda.get(i).getCantitate()+"");
                content.endText();
                pretTotal+=p.getPret()*detaliiComanda.get(i).getCantitate();
            }
        }

        content.beginText();
        content.setFont(PDType1Font.HELVETICA,14);
        content.moveTextPositionByAmount(450,380-(detaliiComanda.size()+1)*20);
        content.drawString("Total:");
        content.endText();

        content.beginText();
        content.setFont(PDType1Font.HELVETICA,14);
        content.moveTextPositionByAmount(500,380-(detaliiComanda.size()+1)*20);
        content.drawString(pretTotal+"");
        content.endText();
        //this.detaliiDao.listTable.removeAll(aux);
        content.close();
        doc.save(fileName);
        doc.close();

        System.out.println("Your file created in :" +System.getProperty("user.dir"));
    }
}
