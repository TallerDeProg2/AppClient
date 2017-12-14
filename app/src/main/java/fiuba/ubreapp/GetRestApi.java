package fiuba.ubreapp;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**Clase encargada de realizar un GET a una determinada REST API.*/
class GetRestApi extends AsyncTask<Info,Integer,Boolean> {

    private static final String TAG = "GET";

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

    @Override
    protected Boolean doInBackground(Info... params) {

        try {
            URL url = new URL(params[0].getInfo());

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setDoInput(true);
//            urlConnection.setDoOutput(true);

            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestMethod(TAG);

//            Para agregar Header.
            if(params.length > 2){
                urlConnection.setRequestProperty("token",params[2].getInfo());
            }
            urlConnection.connect();

            int statusCode = urlConnection.getResponseCode();

            InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());

            params[1].setStatus(statusCode);

            params[1].setInfo(convertStreamToString(inputStream));

            Log.i(TAG, "Status Code: " + Integer.toString(statusCode));

        } catch (Exception e) {
            Log.d(TAG, e.getLocalizedMessage());
        }
        return null;
    }

    protected void onPostExecute(Boolean result) {
    }
}
