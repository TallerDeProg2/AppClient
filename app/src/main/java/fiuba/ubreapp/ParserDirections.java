package fiuba.ubreapp;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

class ParserDirections {

    String json;
    JSONArray aroads,alegs,asteps;
    JSONObject oroads,olegs,location,end_location;
    List<LatLng> listlocation;
    List<List<LatLng>> listroutes;
    List<String> times;
    double lat,lng;
    int i,j;
    List<String> origen,destino;

    public ParserDirections(String json){
        this.json = json;
        listlocation = new ArrayList<>();
        listroutes = new ArrayList<>();
        times = new ArrayList<>();
        origen = new ArrayList<>();
        destino = new ArrayList<>();
        try {
            oroads = new JSONObject(this.json);
            aroads = oroads.getJSONArray("routes");
            parser();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void parser(){
        for(i = 0; i < aroads.length(); i++){
            try {
                listroutes.add(new ArrayList<LatLng>());
                alegs = aroads.getJSONObject(i).getJSONArray("legs");
                olegs = alegs.getJSONObject(0);
                times.add(olegs.getJSONObject("duration").getString("text"));
                end_location = olegs.getJSONObject("end_location");
                origen.add(olegs.getString("start_address"));
                destino.add(olegs.getString("end_address"));
                asteps = olegs.getJSONArray("steps");
                for(j = 0; j < asteps.length(); j++){
                    location = asteps.getJSONObject(j).getJSONObject("start_location");
                    lat = location.getDouble("lat");
                    lng = location.getDouble("lng");
                    listroutes.get(i).add(new LatLng(lat,lng));
                }
                lat = end_location.getDouble("lat");
                lng = end_location.getDouble("lng");
                listroutes.get(i).add(new LatLng(lat,lng));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    public List<List<LatLng>> getListroutes() {
        return this.listroutes;
    }

    public JSONObject getSelectedRoute(int i){
        try {
            return this.aroads.getJSONObject(i);
//            return this.aroads.get(i).toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<String> getTimes() {
        return this.times;
    }

    public String getSelectedTime(int i) {
        return this.times.get(i);
    }

    public String getEndAddress(int i) {
        return this.destino.get(i);
    }

    public String getStartAddress(int i){
        return this.origen.get(i);
    }
}
