package fiuba.ubreapp;

import android.location.Address;

import java.util.List;

/**
 * Created by alan on 26/11/17.
 */

public class Addresses {

    List<Address> addresses;

    public Addresses(){}

    public void setAddresses(List<Address> list) { this.addresses = list; }
    public List<Address> getAddresses() { return this.addresses; }

}
