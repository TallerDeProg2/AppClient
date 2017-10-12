package fiuba.ubreapp;

/**Datos del pasajero. La clase hereda de la clase User que contiene los datos basicos del usuario.*/
class Passenger extends User{
    private String namecard;
    private String numbercard;
    private String typecard;
    private String expiremonthcard;
    private String expireyearcard;

    Passenger(String username, String name, String lastname, String password){
        super(username,name,lastname,password);
        this.namecard = "";
        this.numbercard = "";
        this.typecard = "";
        this.expiremonthcard = "";
        this.expireyearcard = "";
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

    User getUser(){
        User user = new User(this.username, this.name,this.lastname, this.password);
        user.setCountry(this.country);
        user.setEmail(this.email);
        user.setBirthdate(this.birthdate);
        user.setImages(this.images);
        return user;
    }
}
