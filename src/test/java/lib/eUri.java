package lib;

public enum eUri {
    LogsUSER("user/login/"),
    USER("user/"),
    AuthUSER("user/auth");

    private eUri(String uri){
        this.uri = uri;
    }

    public String getUri(){
        return uri;
    }

    private String uri;
}
