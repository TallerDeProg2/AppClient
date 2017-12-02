package fiuba.ubreapp;

/**
 * Created by alan on 30/11/17.
 */

class PaymentRoute {
    String paymethod;
    String trip;

    public PaymentRoute(String paymethod,String trip){
        this.paymethod = paymethod;
        this.trip = trip;
    }

    public void setPaymethod(String paymethod){
        this.paymethod = paymethod;
    }
    public void setTrip(String trip){
        this.trip = trip;
    }
    public String getPaymethod(){ return this.paymethod; }
    public String getTrip(){ return this.trip; }

}
