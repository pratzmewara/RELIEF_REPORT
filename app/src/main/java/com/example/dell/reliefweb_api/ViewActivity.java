package com.example.dell.reliefweb_api;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ViewActivity extends AppCompatActivity {

    RecyclerView recycle;
    FirebaseDatabase db;
    List<API_model> list = new ArrayList<>();
    API_model pojo;
    DatabaseReference reference;
    ProgressBar spinner;

    String year,month,day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        spinner = (ProgressBar)findViewById(R.id.progressBar1);

        spinner.setVisibility(View.VISIBLE);

        recycle = (RecyclerView) findViewById(R.id.recycle);
        db = FirebaseDatabase.getInstance();

        String URL = "/API";
        reference = db.getReference(URL);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot dataSnapshot1 :dataSnapshot.getChildren()){

                    API_model value = dataSnapshot1.getValue(API_model.class);
                    API_model fire = new API_model();
                    String title = value.getTitle();
                    String score = value.getScore();
                    String href = value.getHref();
                    String id = value.getId();
                    fire.setTitle(title);
                    fire.setHref(href);
                    fire.setId(id);
                    fire.setScore(score);
                    list.add(fire);

                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Hello", "Failed to read value.", error.toException());
            }
        });

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                RecyclerAdapter recyclerAdapter = new RecyclerAdapter(list,ViewActivity.this);
                RecyclerView.LayoutManager recyce = new GridLayoutManager(ViewActivity.this,1);
                recycle.setLayoutManager(recyce);
                recycle.setItemAnimator( new DefaultItemAnimator());
                recycle.setAdapter(recyclerAdapter);

                spinner.setVisibility(View.INVISIBLE);

            }
        },5000);


        recycle.addOnItemTouchListener(new RecyclerTouchListener(getBaseContext(),
                recycle, new ClickListener() {
            @Override
            public void onClick(View view, final int position) {

                pojo = list.get(position);
                String v1,v2,v3,v4;
                v1 = pojo.getHref();
                v2 = pojo.getId();
                v3 = pojo.getScore();
                v4 = pojo.getTitle();

                Intent intent = new Intent(getBaseContext(),DetailsActivity.class);
                intent.putExtra("title",v4);
                intent.putExtra("id",v2);
                intent.putExtra("score",v3);
                intent.putExtra("href",v1);
                startActivity(intent);

            }

            @Override
            public void onLongClick(View view, int position) {
                Toast.makeText(ViewActivity.this, "",
                        Toast.LENGTH_LONG).show();
            }
        }));





    }

    public static interface ClickListener{
        public void onClick(View view,int position);
        public void onLongClick(View view,int position);
    }


    class RecyclerTouchListener implements RecyclerView.OnItemTouchListener{

        private ClickListener clicklistener;
        private GestureDetector gestureDetector;

        public RecyclerTouchListener(Context context, final RecyclerView recycleView, final ClickListener clicklistener){

            this.clicklistener=clicklistener;
            gestureDetector=new GestureDetector(context,new GestureDetector.SimpleOnGestureListener(){
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child=recycleView.findChildViewUnder(e.getX(),e.getY());
                    if(child!=null && clicklistener!=null){
                        clicklistener.onLongClick(child,recycleView.getChildAdapterPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child=rv.findChildViewUnder(e.getX(),e.getY());
            if(child!=null && clicklistener!=null && gestureDetector.onTouchEvent(e)){
                clicklistener.onClick(child,rv.getChildAdapterPosition(child));
            }

            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

}
