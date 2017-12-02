package fiuba.ubreapp;

/**Datos del auto del conductor.*/
class Car{

    private String brand;
    private String model;
    private String colour;
    private String plate;
    private String year;
    private String state;
    private String radio;
    private Boolean airconditioner;

    Car(){
        this.brand = "";
        this.model = "";
        this.colour = "";
        this.plate = "";
        this.year = "";
        this.state = "";
        this.radio = "";
        this.airconditioner = false;
    }

    void setBrand(String brand){ this.brand = brand; }

    void setModel(String model){
        this.model = model;
    }

    void setColour(String colour){
        this.colour = colour;
    }

    void setPlate(String plate){
        this.plate = plate;
    }

    void setYear(String year){
        this.year = year;
    }

    void setState(String state){
        this.state = state;
    }

    void setRadio(String radio){
        this.radio = radio;
    }

    void setAirconditioner(Boolean airconditioner){
        this.airconditioner = airconditioner;
    }

    String getBrand() { return this.brand; }

    String getModel(){
        return this.model;
    }

    String getColour(){
        return this.colour;
    }

    String getPlate(){
        return this.plate;
    }

    String getYear(){
        return this.year;
    }

    String getState(){
        return this.state;
    }

    String getRadio(){
        return this.radio;
    }

    Boolean getAirconditioner(){
        return this.airconditioner;
    }
}
