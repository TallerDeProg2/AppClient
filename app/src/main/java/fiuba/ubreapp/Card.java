package fiuba.ubreapp;

/**Datos de la tarjeta de credito.*/
class Card {
    private String name;
    private String number;
    private String type;
    private String expiremonth;
    private String expireyear;
    private String ccvv;

    Card(){
        this.name = "";
        this.number = "";
        this.type = "";
        this.expiremonth = "";
        this.expireyear = "";
        this.ccvv = "";
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

    void setCcvv(String ccvv) { this.ccvv = ccvv; }

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

    String getCcvv() { return this.ccvv; }
}
