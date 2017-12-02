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


/**Clase encargada de realizar un PUT a una determinada REST API.*/
class PutRestApi extends AsyncTask<Info,Integer,Boolean> {
    private static final String TAG = "PUT";

    PutRestApi(){
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

            urlConnection.setFixedLengthStreamingMode(data.length());

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

            params[2].setInfo(convertStreamToString(inputStream));


        } catch (Exception e) {
            Log.d(TAG, e.getLocalizedMessage());
        }
        return null;
    }

    protected void onPostExecute(Boolean result) {

    }
}
