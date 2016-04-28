package rbh9dm.cs4720.com.scavengerhunt;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import com.google.android.gms.maps.model.LatLng;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;

public class DataLongOperationAsynchTask extends AsyncTask<String, Void, String[]> {

    private Context context;

    public DataLongOperationAsynchTask(Context context){
        this.context = context;
    }

    @Override
    protected String[] doInBackground(String... params) {
        String response;
        try {
            response = getLatLongByURL(params[0]);
            Log.d("response", params[0] +" ");
            return new String[]{response};
        } catch (Exception e) {
            Log.d("On", "Background");
            return null;
        }
    }

    @Override
    protected void onPostExecute(String[] results) {
        Add_Hunt_Item.itemList.clear();
        Add_Hunt_Item.coordinates.clear();
        try {
            JSONObject jsonObj = new JSONObject(results[0]);
            JSONArray jsonResult = jsonObj.getJSONArray("results");
            for (int j = 0; j < jsonResult.length(); j++){
                JSONObject jsonObject = jsonResult.getJSONObject(j);
                String address;
                JSONArray addresses = jsonObject.getJSONArray("address_components");
                //String output = (addresses.getJSONObject(0).getJSONArray("types").getString(0).substring(0, 1).toUpperCase()
                 //       + addresses.getJSONObject(0).getJSONArray("types").getString(0).substring(1)).replaceAll("_", " ");
                address = addresses.getJSONObject(0).getString("long_name");
                for(int i = 0; i < addresses.length(); i++) {
                    if(addresses.getJSONObject(i).getJSONArray("types").getString(0).equals("administrative_area_level_1"))
                        address += ", " +addresses.getJSONObject(i).getString("long_name");
                    if(addresses.getJSONObject(i).getJSONArray("types").getString(0).equals("country"))
                        address += ", " +addresses.getJSONObject(i).getString("long_name");
                }
                Log.d("ENDERECO: ", address );
                Log.d("Coordenadas de"+address, +jsonObject.getJSONObject("geometry").getJSONObject("location").getDouble("lng")+ ", " +jsonObject.getJSONObject("geometry").getJSONObject("location").getDouble("lat"));
                Add_Hunt_Item.itemList.add(address);
                Add_Hunt_Item.coordinates.add(new LatLng(jsonObject.getJSONObject("geometry").getJSONObject("location").getDouble("lat"),
                                                        jsonObject.getJSONObject("geometry").getJSONObject("location").getDouble("lng")));
            }
            if (Add_Hunt_Item.itemList.size() == 0) {
                CharSequence text = "No results were found. Please, change the search";
                Add_Hunt_Item.itemList.clear();
                Add_Hunt_Item.itemAdapter.notifyDataSetChanged();
                Add_Hunt_Item.coordinates.clear();
                int duration = Toast.LENGTH_LONG;

                Toast toast = Toast.makeText(this.context, text, duration);
                toast.show();
            }
            else
                Add_Hunt_Item.itemAdapter.notifyDataSetChanged();

        } catch (JSONException e) {
            CharSequence text = "Wi-Fi off";
            Add_Hunt_Item.itemList.clear();
            Add_Hunt_Item.itemAdapter.notifyDataSetChanged();
            Add_Hunt_Item.coordinates.clear();
            int duration = Toast.LENGTH_LONG;

            Toast toast = Toast.makeText(this.context, text, duration);
            //toast.show();
            e.printStackTrace();
        }

    }

    public String getLatLongByURL(String requestURL) {
        URL url;
        String response = "";
        try {
            url = new URL(requestURL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            conn.setDoOutput(true);
            int responseCode = conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = br.readLine()) != null) {
                    response += line;
                }
            } else {
                response = "";
            }

        } catch (Exception e) {
            Log.d("On", "LatLongURL");
            e.printStackTrace();
        }
        return response;
    }
}