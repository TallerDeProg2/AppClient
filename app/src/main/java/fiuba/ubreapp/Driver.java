package fiuba.ubreapp;

/**Datos del conductor. La clase hereda de la clase User que contiene los datos basicos del usuario.*/
class Driver extends User{

    private String modelcar;
    private String colourcar;
    private String platecar;
    private String yearcar;
    private String statecar;
    private String musiccar;
    private Boolean airconditioner;

    Driver(String username, String name, String lastname, String password){
        super(username,name,lastname,password);
        this.modelcar = "";
        this.colourcar = "";
        this.platecar = "";
        this.yearcar = "";
        this.statecar = "";
        this.musiccar = "";
        this.airconditioner = false;
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

    User getUser(){
        User user = new User(this.username, this.name,this.lastname, this.password);
        user.setCountry(this.country);
        user.setEmail(this.email);
        user.setBirthdate(this.birthdate);
        user.setImages(this.images);
        return user;
    }
}
