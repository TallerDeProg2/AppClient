package fiuba.ubreapp;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**Clase encargada de realizar un POST a una determinada REST API.*/
class PostRestApi extends AsyncTask<Info,Integer,Boolean> {

    private static final String TAG = "POST";

    PostRestApi(){
    }

    private String convertStreamToString(InputStream is) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        reader.close();
        return sb.toString();
    }

    protected Boolean doInBackground(Info... params) {

        try {
            // This is getting the url from the string we passed in
            URL url = new URL(params[0].getInfo());

            // Create the urlConnection
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);

            urlConnection.setRequestProperty("Content-Type", "application/json");


            urlConnection.setRequestMethod(TAG);

            if(params.length > 3){
                urlConnection.setRequestProperty("token",params[3].getInfo());
            }

            String data = params[1].getInfo();

            Log.i(TAG,"size: "+ data.length());

            urlConnection.setFixedLengthStreamingMode(data.length());
//            if (data.length() > 1000)
//                urlConnection.setFixedLengthStreamingMode(data.length()+1);
//            else
//                urlConnection.setFixedLengthStreamingMode(data.length());
            // Send the post body
            if (data != null) {
                OutputStreamWriter writer = new OutputStreamWriter(urlConnection.getOutputStream());
                writer.write(data);
                writer.flush();
                writer.close();
            }

            int statusCode = urlConnection.getResponseCode();

            params[2].setStatus(statusCode);

            InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());

            data = convertStreamToString(inputStream);

            params[2].setInfo(data);

        } catch (Exception e) {
            Log.d(TAG, e.getLocalizedMessage());
        }
        return null;
    }

    protected void onPostExecute(Boolean result) {

    }
}