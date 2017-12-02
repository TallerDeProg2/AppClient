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
    double lat,lng;
    int i,j;

    public ParserDirections(String json){
        this.json = json;
        listlocation = new ArrayList<>();
        listroutes = new ArrayList<>();
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
                alegs = aroads.getJSONObject(i).getJSONArray("legs");
                olegs = alegs.getJSONObject(0);
                end_location = olegs.getJSONObject("end_location");
                asteps = olegs.getJSONArray("steps");
                for(j = 0; j < asteps.length(); j++){
                    location = asteps.getJSONObject(j).getJSONObject("start_location");
                    lat = location.getDouble("lat");
                    lng = location.getDouble("lng");
                    listlocation.add(new LatLng(lat,lng));
                }
                lat = end_location.getDouble("lat");
                lng = end_location.getDouble("lng");
                listlocation.add(new LatLng(lat,lng));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            listroutes.add(listlocation);
            listlocation.clear();
        }

    }

    public List<List<LatLng>> getListroutes() {
        return this.listroutes;
    }

    public String getSelectedRoute(int i){
        try {
            return this.aroads.get(i).toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

}
