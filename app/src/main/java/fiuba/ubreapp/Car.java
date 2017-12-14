package fiuba.ubreapp;

/**Datos del auto del conductor.*/
class Car{

    private String brand;
    private String model;
    private String color;
    private String plate;
    private String year;
    private String status;
    private String radio;
    private Boolean airconditioner;

    Car(){
        this.brand = "";
        this.model = "";
        this.color = "";
        this.plate = "";
        this.year = "";
        this.status = "";
        this.radio = "";
        this.airconditioner = false;
    }

    void setBrand(String brand){ this.brand = brand; }

    void setModel(String model){
        this.model = model;
    }

    void setColor(String color){
        this.color = color;
    }

    void setPlate(String plate){
        this.plate = plate;
    }

    void setYear(String year){
        this.year = year;
    }

    void setStatus(String status){
        this.status = status;
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

    String getColor(){
        return this.color;
    }

    String getPlate(){
        return this.plate;
    }

    String getYear(){
        return this.year;
    }

    String getStatus(){
        return this.status;
    }

    String getRadio(){
        return this.radio;
    }

    Boolean getAirconditioner(){
        return this.airconditioner;
    }
}
