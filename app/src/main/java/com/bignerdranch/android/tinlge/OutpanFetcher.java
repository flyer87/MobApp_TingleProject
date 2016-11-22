package com.bignerdranch.android.tinlge;

import android.net.Uri;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Omer on 04.04.2016.
 */
public class OutpanFetcher {
    private static final String TAG = "OutpanFetcher";
    private static final String APIKEY = "e4e8422300f8fa46154208f0e357301c";

    public String getProductInfo(String barcode) {
        String name = "";

        try {
            String url = Uri.parse("https://api.outpan.com/v2/products/"+ barcode + "/")
                    .buildUpon()
                    .appendQueryParameter("apikey", APIKEY)
                    .build().toString();

            Log.i(TAG, "Link constructed: " + url);

            String jsonString = getUrlString(url);
            Log.i(TAG, "Received JSON: " + jsonString);
            JSONObject jsonBody = new JSONObject(jsonString);
            name = productInfo(jsonBody);

        } catch (JSONException je){
            Log.e(TAG, "Failed to parse JSON", je);
        } catch (IOException ioe) {
            Log.e(TAG, "Failed to fetch items", ioe);
        }

        return name;
    }

    public byte[] getUrlBytes(String urlSpec) throws IOException {

        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            InputStream in = connection.getInputStream();
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(connection.getResponseMessage() +
                        ": with " +
                        urlSpec);
            }

            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();

            return out.toByteArray();
        } finally {
            connection.disconnect();
        }
    }

    public String getUrlString(String urlSpec) throws IOException {
        try {
            return new String(getUrlBytes(urlSpec));
        } catch (IOException e){
            return "Unable to retrieve web page. URL may be invalid.";
        }
    }

    private String productInfo(JSONObject jsonBody) throws IOException, JSONException {
        JSONObject productAttribute = jsonBody.getJSONObject("attributes");
        String productTitle =  productAttribute.getString("Title");

        Log.i("fetched title", productTitle);
        return productTitle;
    }
}