package converter;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.input.SAXBuilder;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

@Remote(IConverter.class)
@Stateless
public class ConverterEjbBean {
    public ConverterEjbBean() {
    }
    public double euroToOtherCurrency(double amount, String currencyCode) throws IOException, JDOMException {
        SAXBuilder sxb = new SAXBuilder();
        URL url = new URL("https://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml");
        HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
        Document document = sxb.build(con.getInputStream());
        Element racine = document.getRootElement();
        //http://www.ecb.int/vocabulary/2002-08-01/eurofxref
        Namespace ns = Namespace.getNamespace("http://www.ecb.int/vocabulary/2002-08-01/eurofxref");
        Element elem = racine.getChild("Cube", ns);
        elem = elem.getChild("Cube",ns);
        List<Element> listCube = elem.getChildren("Cube",ns);
        Iterator<Element> i = listCube.iterator();
        boolean trouver = false;
        String rate ="";
        Double currencyRate = 1.;
        while(i.hasNext() && !trouver) {
            Element courant = i.next();
            String currency = courant.getAttributeValue("currency");
            if (currency.equals(currencyCode)) {
                trouver = true;
                rate = courant.getAttribute("rate").getValue();
            }
        }
        try{
            currencyRate = Double.parseDouble(rate);
            return amount*currencyRate;
        }catch(NumberFormatException e){
            return -1.;
        }

    }
}
