package fiuba.ubreapp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alan on 29/11/17.
 */

class DriversPosition {

    List<LatLong> positions;

    public DriversPosition(){
        positions = new ArrayList<>();
    }

    public void setPositions(List<LatLong> positions){ this.positions = positions; }
    public List<LatLong> getPositions(){ return this.positions; }

}
