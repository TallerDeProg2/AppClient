package fiuba.ubreapp;

/**Datos de la tarjeta de credito.*/
class Card {
    private String name;
    private String number;
    private String type;
    private String expiremonth;
    private String expireyear;

    Card(){
        this.name = "";
        this.number = "";
        this.type = "";
        this.expiremonth = "";
        this.expireyear = "";
    }

    void setName(String name){
        this.name = name;
    }

    void setNumber(String number){
        this.number = number;
    }

    void setType(String type){
        this.type = type;
    }

    void setExpireMonth(String expiremonth){
        this.expiremonth = expiremonth;
    }

    void setExpireYear(String expireyear){
        this.expireyear = expireyear;
    }

    String getName(){
        return this.name;
    }

    String getNumber(){
        return this.number;
    }

    String getType(){
        return this.type;
    }

    String getExpireMonthCard(){
        return this.expiremonth;
    }

    String getExpireYearCard(){
        return this.expireyear;
    }
}
