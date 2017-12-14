package fiuba.ubreapp;

/**Datos de la tarjeta de credito.*/
class Card {
    private String number;
    private String type;
    private String expiration_month;
    private String expiration_year;
    private String ccvv;
    private String method;

    Card(){
        this.number = "";
        this.type = "";
        this.expiration_month = "";
        this.expiration_year = "";
        this.ccvv = "";
        this.method = "card";
    }

    void setNumber(String number){
        this.number = number;
    }

    void setType(String type){
        this.type = type;
    }

    void setExpireMonth(String expiremonth){
        this.expiration_month = expiremonth;
    }

    void setExpireYear(String expireyear){
        this.expiration_year = expireyear;
    }

    void setCcvv(String ccvv) { this.ccvv = ccvv; }

    String getNumber(){
        return this.number;
    }

    String getType(){
        return this.type;
    }

    String getExpireMonthCard(){
        return this.expiration_month;
    }

    String getExpireYearCard(){
        return this.expiration_year;
    }

    String getCcvv() { return this.ccvv; }
}
