package fiuba.ubreapp;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

class ParserDrivers {
    String json;
    JSONArray adrivers;
    JSONObject jsonobj, odrivers, oposition;
    List<LatLng> listlocation;
    List<String> listdriver;
    double lat,lng;
    int i,j;

    public ParserDrivers(String json){
        this.json = json;
        listdriver = new ArrayList<>();
        listlocation = new ArrayList<>();
        try {
            jsonobj = new JSONObject(this.json);
            parser();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void parser(){
        try {
//            odrivers = jsonobj.getJSONObject("drivers");
            adrivers = jsonobj.getJSONArray("drivers");
            for(i = 0; i < adrivers.length(); i++){
                listdriver.add(adrivers.getJSONObject(i).getString("driver"));
                oposition = adrivers.getJSONObject(i).getJSONObject("position");
                lat = oposition.getDouble("lat");
                lng = oposition.getDouble("lon");
                listlocation.add(new LatLng(lat,lng));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public List<LatLng> getListlocation() {
        return this.listlocation;
    }

    public List<String> getListdriver() {
        return this.listdriver;
    }
}
