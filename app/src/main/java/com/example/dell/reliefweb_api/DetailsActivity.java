package com.example.dell.reliefweb_api;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class DetailsActivity extends AppCompatActivity {

    TextView idd, score, link, title;
    String s_idd, s_score, s_link, s_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Toast.makeText(getBaseContext(),"Here are the details", Toast.LENGTH_LONG).show();

        s_idd = getIntent().getStringExtra("id");
        s_score = getIntent().getStringExtra("score");
        s_link = getIntent().getStringExtra("href");
        s_title = getIntent().getStringExtra("title");

        idd = (TextView)findViewById(R.id.idd);
        score = (TextView)findViewById(R.id.score);
        link = (TextView)findViewById(R.id.link);
        title = (TextView)findViewById(R.id.title2);

        idd.append(s_idd);
        score.append(s_score);
        link.append(s_link);
        title.append(s_title);
    }
}
