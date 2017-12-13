package com.rubys.android.chefsspecial.utils;


import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetworkUtils {
    private static final String TAG = "NetworkUtils";

    public static String getResponseFromHttpUrl(URL url) throws IOException {

        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.connect();
        Log.i(TAG, "StatusCode: " + urlConnection.getResponseCode());
        try{

            InputStream stream = urlConnection.getInputStream();

            StringBuilder content = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            String line;

            while((line = reader.readLine()) != null){
                content.append(line);

            }
            reader.close();

            Log.d(TAG,"content: "+ content);
            return content.toString();

        }finally {
            urlConnection.disconnect();
        }

    }
}
