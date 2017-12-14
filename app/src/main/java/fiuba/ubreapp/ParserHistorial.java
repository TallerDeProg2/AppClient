package fiuba.ubreapp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

class ParserHistorial {

    String json;
    JSONArray atrips;
    JSONObject jsonobj, otrip, ocost,ostart,oend;
    List<String> waittime,traveltime,totaltime,starttime,start,end,cost,otheruser;
    String type;
    int i;

    public ParserHistorial(String type,String json){
        this.type = type;
        this.json = json;
        waittime = new ArrayList<>();
        traveltime = new ArrayList<>();
        totaltime = new ArrayList<>();
        starttime = new ArrayList<>();
        start = new ArrayList<>();
        end = new ArrayList<>();
        cost = new ArrayList<>();
        otheruser = new ArrayList<>();

        try {
            jsonobj = new JSONObject(this.json);
            parser();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void parser(){
        try {
            atrips = jsonobj.getJSONArray("trips");
            for(i = 0; i < atrips.length(); i++){
                otrip = atrips.getJSONObject(i);
                ocost = otrip.getJSONObject("cost");
                cost.add(ocost.getString("value")+" "+ ocost.getString("currency"));
                starttime.add(otrip.getString("startTime"));
                waittime.add(otrip.getString("waitTime"));
                traveltime.add(otrip.getString("travelTime"));
                totaltime.add(otrip.getString("totalTime"));
                ostart = otrip.getJSONObject("start");
                start.add(ostart.getString("street"));
                oend = otrip.getJSONObject("end");
                end.add(oend.getString("street"));
                otheruser.add(otrip.getString(type));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public List<String> getStarttime() { return  this.starttime; }
    public List<String> getWaittime() { return this.waittime; }
    public List<String> getTraveltime() { return this.traveltime; }
    public List<String> getTotaltime() { return this.totaltime; }
    public List<String> getStart() { return this.start; }
    public List<String> getEnd() { return this.end; }
    public List<String> getCost() { return this.cost; }
    public List<String> getOtheruser() { return this.otheruser; }
}
