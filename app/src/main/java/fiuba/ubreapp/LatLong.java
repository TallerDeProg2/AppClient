package fiuba.ubreapp;

/**
 * Created by alan on 28/11/17.
 */

class LatLong {
    double lat;
    double lon;

    public LatLong(double lat, double lon){
        this.lat = lat;
        this.lon = lon;
    }

    public void setLat(double lat){ this.lat = lat; }
    public void setLon(double lon){ this.lon = lon; }
    public double getLat(){ return this.lat; }
    public double getLon(){ return this.lon; }

}
