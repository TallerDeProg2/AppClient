package fiuba.ubreapp;

public class User {
    private String name;
    private String lastname;
    private String email;
    private String password;

    private String namecard;
    private String numbercard;
    private String typecard;
    private String expiremonthcard;
    private String expireyearcard;

    private String modelcar;
    private String colourcar;
    private String platecar;
    private String yearcar;
    private String statecar;
    private String musiccar;
    private Boolean airconditioner;

    public User(String name, String lastname, String email, String password){
        this.name = name;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
    }

    public void setNameCard(String namecard){
        this.namecard = namecard;
    }

    public void setNumberCard(String numbercard){
        this.numbercard = numbercard;
    }

    public void setTypeCard(String typecard){
        this.typecard = typecard;
    }

    public void setExpireMonthCard(String expiremonthcard){
        this.expiremonthcard = expiremonthcard;
    }

    public void setExpireYearCard(String expireyearcard){
        this.expireyearcard = expireyearcard;
    }

    public void setModelCar(String modelcar){
        this.modelcar = modelcar;
    }

    public void setColourCar(String colourcar){
        this.colourcar = colourcar;
    }

    public void setPlateCar(String platecar){
        this.platecar = platecar;
    }

    public void setYearCar(String yearcar){
        this.yearcar = yearcar;
    }

    public void setStateCar(String statecar){
        this.statecar = statecar;
    }

    public void setMusicCar(String musiccar){
        this.musiccar = musiccar;
    }

    public void setAirConditioner(Boolean airconditioner){
        this.airconditioner = airconditioner;
    }

    public String getName(){ return this.name;}
    public String getLastName(){ return this.lastname;}
    public String getEmail(){ return this.email;}
    public String getPassword(){ return this.password;}

    public String getNameCard(){
        return this.namecard;
    }

    public String getNumberCard(){
        return this.numbercard;
    }

    public String getTypeCard(){
        return this.typecard;
    }

    public String getExpireMonthCard(){
        return this.expiremonthcard;
    }

    public String getExpireYearCard(){
        return this.expireyearcard;
    }

    public String getModelCar(){
        return this.modelcar;
    }

    public String getColourCar(){
        return this.colourcar;
    }

    public String getPlateCar(){
        return this.platecar;
    }

    public String getYearCar(){
        return this.yearcar;
    }

    public String getStateCar(){
        return this.statecar;
    }

    public String getMusicCar(){
        return this.musiccar;
    }

    public Boolean getAirConditioner(){
        return this.airconditioner;
    }

}
