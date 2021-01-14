package converter;

import domain.Monnaie;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.input.SAXBuilder;

import javax.ejb.Remote;
import javax.ejb.Stateful;
import javax.ejb.Stateless;
import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

@Remote(IConverter.class)
@Stateless
public class ConverterEjbBean {
    private List<Monnaie> listMonnaies;
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
        elem = elem.getChild("Cube", ns);
        List<Element> listCube = elem.getChildren("Cube", ns);
        Iterator<Element> i = listCube.iterator();
        boolean trouver = false;
        String rate = "";
        Double currencyRate = 1.;
        while (i.hasNext() && !trouver) {
            Element courant = i.next();
            String currency = courant.getAttributeValue("currency");
            if (currency.equals(currencyCode)) {
                trouver = true;
                rate = courant.getAttribute("rate").getValue();
            }
        }
        try {
            currencyRate = Double.parseDouble(rate);
            return amount * currencyRate;
        } catch (NumberFormatException e) {
            return -1.;
        }
    }

    public void setListMonnaies(List<Monnaie> listMonnaies) {
        this.listMonnaies = listMonnaies;
    }

    public List<Monnaie> getAvailableCurrencies() throws IOException, JDOMException {
        List<Monnaie> res = new ArrayList<>();
        SAXBuilder sxb = new SAXBuilder();
        URL url = new URL("https://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml");
        HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
        Document document = sxb.build(con.getInputStream());
        Element racine = document.getRootElement();
        //http://www.ecb.int/vocabulary/2002-08-01/eurofxref
        Namespace ns = Namespace.getNamespace("http://www.ecb.int/vocabulary/2002-08-01/eurofxref");
        Element elem = racine.getChild("Cube", ns);
        elem = elem.getChild("Cube", ns);
        List<Element> listCube = elem.getChildren("Cube", ns);
        Iterator<Element> i = listCube.iterator();
        while (i.hasNext()) {
            Element courant = i.next();
            Monnaie cur = new Monnaie();
            cur.setCurrencyCode(courant.getAttributeValue("currency"));
            cur.setRate(Double.parseDouble(courant.getAttributeValue("rate")));
            res.add(cur);
        }
        SAXBuilder sxb2 = new SAXBuilder();
        URL url2 = new URL("https://www.currency-iso.org/dam/downloads/lists/list_one.xml");
        HttpsURLConnection con2 = (HttpsURLConnection) url.openConnection();
        Document document2 = sxb.build(con2.getInputStream());
        Element racine2 = document2.getRootElement();
        Element elem2 = racine2.getChild("CcyTbl");
        List<Element> listCube2 = elem2.getChildren();
        Iterator<Element> i2 = listCube2.iterator();
        while (i2.hasNext()) {
            Element courant = i.next();
            String code = courant.getChildText("Ccy");
            for(Monnaie m : res){
                if(m.getCurrencyCode() == code){
                    if(courant.getChildText("CcyNM") != "") {
                        m.setCurrencyName(courant.getChildText("CcyNM"));
                    }
                    if(courant.getChildText("CtryNm") != "") {
                        m.addListCountries(courant.getChildText("CtryNm"));
                    }
                }
            }
        }
        return res;
    }

    public Map<Monnaie, Double> euroToOtherCurrencies(double amount) throws IOException, JDOMException {
        Map<Monnaie, Double> res = new HashMap<Monnaie, Double>();
        for(Monnaie m : listMonnaies){
            Double calc = euroToOtherCurrency(amount,m.getCurrencyCode());
            res.put(m,calc);
        }
        return res;
    }
}

