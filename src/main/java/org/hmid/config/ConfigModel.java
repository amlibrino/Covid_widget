package org.hmid.config;

public class ConfigModel {
    private int refreshIntervalInSeconds;
    private String countryName;
    private String countryCode;

    //constructeur par defaut valeur renvoyé par defaut
    public ConfigModel() {
        refreshIntervalInSeconds = 30;//delais de rafraichissement des donnes
        countryName = "France";
        countryCode = "FR";
    }

    public int getRefreshIntervalInSeconds() {
        return refreshIntervalInSeconds;
    }

    public void setRefreshIntervalInSeconds(int refreshIntervalInSeconds) {
        this.refreshIntervalInSeconds = refreshIntervalInSeconds;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }
}
