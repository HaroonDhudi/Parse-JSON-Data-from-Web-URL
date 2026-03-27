package com.example.parsejsondatafromweburl;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    TextView textView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);

        // Allow network operation on main thread (for demo only, use AsyncTask/Retrofit in production)
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        String urlString = "https://jsonplaceholder.typicode.com/users"; // Example API
        String jsonResponse = getJsonFromUrl(urlString);

        if (jsonResponse != null) {
            parseJson(jsonResponse);
        }
    }

    private String getJsonFromUrl(String urlString) {
        StringBuilder result = new StringBuilder();
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            reader.close();
            return result.toString();

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void parseJson(String jsonResponse) {
        try {
            JSONArray jsonArray = new JSONArray(jsonResponse);

            StringBuilder parsedData = new StringBuilder();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject userObject = jsonArray.getJSONObject(i);
                String name = userObject.getString("name");
                String email = userObject.getString("email");

                parsedData.append("Name: ").append(name).append("\n");
                parsedData.append("Email: ").append(email).append("\n\n");
            }

            textView.setText(parsedData.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}