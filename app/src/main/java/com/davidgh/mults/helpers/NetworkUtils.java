package com.davidgh.mults.helpers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by davidgh on 2/24/18.
 */

public class NetworkUtils {

    private String stream = null;

    public NetworkUtils(){}

    public String getHttpData(String urlStr){

        try {
            URL url = new URL(urlStr);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

            if (httpURLConnection.getResponseCode() == 200) { // OK

                BufferedReader reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));

                StringBuilder stringBuilder = new StringBuilder();
                String line;

                while (null != (line = reader.readLine())){
                    stringBuilder.append(line);
                }
                stream = stringBuilder.toString();
                httpURLConnection.disconnect();
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return stream;
    }

    public static boolean isNetworkAvailable(Context context){

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
