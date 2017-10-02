package fiuba.ubreapp;

class User {
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

    User(String name, String lastname, String email, String password){
        this.name = name;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.namecard = "";
        this.numbercard = "";
        this.typecard = "";
        this.expiremonthcard = "";
        this.expireyearcard = "";
        this.modelcar = "";
        this.colourcar = "";
        this.platecar = "";
        this.yearcar = "";
        this.statecar = "";
        this.musiccar = "";
        this.airconditioner = false;
    }

    void setNameCard(String namecard){
        this.namecard = namecard;
    }

    void setNumberCard(String numbercard){
        this.numbercard = numbercard;
    }

    void setTypeCard(String typecard){
        this.typecard = typecard;
    }

    void setExpireMonthCard(String expiremonthcard){
        this.expiremonthcard = expiremonthcard;
    }

    void setExpireYearCard(String expireyearcard){
        this.expireyearcard = expireyearcard;
    }

    void setModelCar(String modelcar){
        this.modelcar = modelcar;
    }

    void setColourCar(String colourcar){
        this.colourcar = colourcar;
    }

    void setPlateCar(String platecar){
        this.platecar = platecar;
    }

    void setYearCar(String yearcar){
        this.yearcar = yearcar;
    }

    void setStateCar(String statecar){
        this.statecar = statecar;
    }

    void setMusicCar(String musiccar){
        this.musiccar = musiccar;
    }

    void setAirConditioner(Boolean airconditioner){
        this.airconditioner = airconditioner;
    }

    String getName(){ return this.name;}
    String getLastName(){ return this.lastname;}
    String getEmail(){ return this.email;}
    String getPassword(){ return this.password;}

    String getNameCard(){
        return this.namecard;
    }

    String getNumberCard(){
        return this.numbercard;
    }

    String getTypeCard(){
        return this.typecard;
    }

    String getExpireMonthCard(){
        return this.expiremonthcard;
    }

    String getExpireYearCard(){
        return this.expireyearcard;
    }

    String getModelCar(){
        return this.modelcar;
    }

    String getColourCar(){
        return this.colourcar;
    }

    String getPlateCar(){
        return this.platecar;
    }

    String getYearCar(){
        return this.yearcar;
    }

    String getStateCar(){
        return this.statecar;
    }

    String getMusicCar(){
        return this.musiccar;
    }

    Boolean getAirConditioner(){
        return this.airconditioner;
    }

}
