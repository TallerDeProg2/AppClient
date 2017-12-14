package fiuba.ubreapp;

/**
 * Created by alan on 13/12/17.
 */

public class Singleton {

    private static Singleton instance = null;

    private String user,card,car,type,idtrip,url,route;

    private Singleton(){
        user = "";
        card = "";
        car = "";
        type = "";
        idtrip = "";
        url = "";
        route = "";
    }

    public static Singleton getInstance(){
        if (instance == null)
            instance = new Singleton();
        return  instance;
    }

    public void setUser(String user){ this.user = user; }
    public void setCard(String card){ this.card = card; }
    public void setCar(String car){ this.car = car; }
    public void setType(String type){ this.type = type; }
    public void setIdtrip(String idtrip){ this.idtrip = idtrip; }
    public void setUrl(String url){ this.url = url; }
    public void setRoute(String route){ this.route = route; }

    public String getUser(){ return this.user; }
    public String getCard(){ return this.card; }
    public String getCar(){ return this.car; }
    public String getType(){ return this.type; }
    public String getIdtrip(){ return this.idtrip; }
    public String getUrl(){ return this.url; }
    public String getRoute(){ return this.route; }

}
