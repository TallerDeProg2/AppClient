package fiuba.ubreapp;

import java.util.List;

/**
 * Created by alan on 29/11/17.
 */

class TripList {
    List<Trip> trips;

    public TripList(){}

    public void setTrips(List<Trip> trips){ this.trips = trips; }
    public List<Trip> getTrips(){ return this.trips; }

}
