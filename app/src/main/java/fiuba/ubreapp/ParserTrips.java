package fiuba.ubreapp;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

class ParserTrips {

    String json;
    JSONArray atrips;
    JSONObject otrips;
    List<String> trips;
    List<String> passengers;
    List<String> ids;
    int i,j;

    public ParserTrips(String json){
        this.json = json;
        trips = new ArrayList<>();
        passengers = new ArrayList<>();
        ids = new ArrayList<>();
        try {
            otrips = new JSONObject(this.json);
            parser();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void parser(){
        try {
            atrips = otrips.getJSONArray("trips");
            for (i = 0; i < atrips.length(); i++){
                passengers.add(atrips.getJSONObject(i).getString("passenger"));
                trips.add(atrips.getJSONObject(i).getString("trip"));
                ids.add(String.valueOf(atrips.getJSONObject(i).getInt("id")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public List<String> getTrips(){
        return this.trips;
    }

    public List<String> getPassengers(){
        return this.passengers;
    }

    public List<String> getIds() { return this.ids; }
}
