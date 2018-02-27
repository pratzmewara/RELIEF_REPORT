package com.example.dell.reliefweb_api;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    String JsonStr;
    TextView abc;
    Button a;
    String score, id, href, title;
    FirebaseDatabase db;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        abc = (TextView)findViewById(R.id.textView);
        db = FirebaseDatabase.getInstance();
        String URL = "/API";
        reference = db.getReference(URL);

        a = (Button)findViewById(R.id.button2);

        a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getBaseContext(),ViewActivity.class);
                startActivity(intent);

            }
        });

        new RequestTask().execute();


    }

    class RequestTask extends AsyncTask<String, String, String> {

        String myurl = "https://api.reliefweb.int/v1/reports?appname=apidoc&limit=10";

        @Override
        protected String doInBackground(String... uri) {
            try {
                URL url = new URL(myurl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setRequestMethod("GET");
                conn.connect();
                InputStream inputStream = conn.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;

                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {

                }

                JsonStr = buffer.toString();

            } catch (Exception e) {
                //TODO Handle problems..
                Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_LONG).show();
            }

            return JsonStr;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                for(int i=0;i<jsonArray.length();i++) {
                    JSONObject json_data = jsonArray.getJSONObject(i);
                    score = String.valueOf(json_data.getInt("score"));
                    href = json_data.getString("href");
                    id = json_data.getString("id");
                    JSONObject fields = new JSONObject(json_data.getString("fields"));
                    title = fields.getString("title");

                    API_model model = new API_model();
                    model.href = href;
                    model.id = id;
                    model.score = score;
                    model.title = title;

                    reference.child(String.valueOf(i)).setValue(model);

                }

            }catch (Exception e){
                Toast.makeText(getBaseContext(),"Poor Internet Connection",Toast.LENGTH_LONG).show();
            }



        }

    }
}
