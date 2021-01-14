package domain;

import java.io.Serializable;
import java.util.List;

public class Monnaie implements Serializable {
    private List<String> listCountries;
    private String currencyName;
    private String currencyCode;
    private Double rate;

    public void setListCountries(List<String> listCountries) {
        this.listCountries = listCountries;
    }
    public void addListCountries(String country) {
        this.listCountries.add(country);
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public List<String> getListCountries() {
        return listCountries;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public Double getRate() {
        return rate;
    }
}
