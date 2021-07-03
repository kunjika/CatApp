package com.exaple.kittyapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    ImageButton download ,refresh , info ;
    ImageView catImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        download = findViewById(R.id.download);
        info = findViewById(R.id.info);
        refresh = findViewById(R.id.refresh);
        catImageView = findViewById(R.id.kittyimage);
        getImage(getResources().getString(R.string.api_url));

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this , "refresh button is clicked" , Toast.LENGTH_SHORT).show();
                getImage(getResources().getString(R.string.api_url));
            }
        });





    }
    public void getImage(String url){
        //extract the json data
        RequestQueue queue = new Volley().newRequestQueue(this);

        JsonArrayRequest arrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                //Log.i("RESPONSEVALIDDATA" , response.toString());
                try{
                    JSONObject kittyData = response.getJSONObject(0);
                    //Log.d("RESPONSEVLAUEDATA" ,kittyData.toString());

                    final String catUrl = kittyData.getString("url");
                    Log.d("RESPONSEVALUEDATA" , catUrl);
                    Picasso.get().load(catUrl).into(catImageView);

                    download.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(MainActivity.this , "download button is clicked" , Toast.LENGTH_SHORT).show();

                            Uri caturi = Uri.parse(catUrl);
                            Intent browser = new Intent(Intent.ACTION_VIEW,caturi);
                            startActivity(browser);
                        }
                    });

                }catch (JSONException e){
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this,error.getMessage() , Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(arrayRequest);
    }
}