package lib;

public enum eDomen {
    LEARN("https://playground.learnqa.ru/api/"),
    DEV("https://playground.learnqa.ru/api_dev/");

    private eDomen(String domen){
        this.domen = domen;
    }

    public String getDomen(){
        return domen;
    }

    private String domen;
}
