package com.example.jingyuanzhao.weather;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.solidfire.gson.GsonBuilder;


import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by jingyuanzhao on 2016/11/27.
 */

public class WeatherActivity extends AppCompatActivity{
    private String city_code;
    public final static String Tag="WeatherActivity"; 
     public final static String  TAG="WeatherActivity";
    public static  String request;
    public static URL url;
    public TextView weather_text;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_activity);

        Intent intent=getIntent();
        city_code=intent.getStringExtra("city_code");
        Log.i(Tag,city_code);
        weather_text=(TextView)findViewById(R.id.weather_show);

        request="https://free-api.heweather.com/v5/now?"+"city="+"CN"+city_code+"&key=a36b4120508743979e0b7e1df3f8dd69";
        Log.i(Tag, "onCreate: "+request);
        try
        {
            url=new URL(request);
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        StringRequest stringRequest=new StringRequest(Request.Method.GET,request,  new Response.Listener<String>()
        {
            @Override
            public void onResponse(String s) {
                System.out.println(s);
                WeatherBean wb=trans_json(s);

                if(wb!=null)
                {
                    Log.i(TAG, "onResponse: get json");
                    List<WeatherBean.HeWeather5Bean>  list =wb.getHeWeather5();
                    weather_text.setText("当地气温为： "+list.get(0).getNow().getTmp());
                }

                //weather_text.setText(list.get(0).getAqi().getCity().toString());
                //Log.i(Tag, "onResponse: "+list.get(0).getAqi().getCity().toString());
            }
        },new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.i(Tag, "onErrorResponse: "+"volley error");
                weather_text.setText("error");

            }
        }
        );
        requestQueue.add(stringRequest);


        


    }

    private WeatherBean trans_json(String s)
    {
        //StringBuilder sb=new StringBuilder(s);

        //Log.i(Tag, "trans_json: "+sb.toString());
        WeatherBean javaBean=null;
        try {
            javaBean= new GsonBuilder().create().fromJson(s.toString(),WeatherBean.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i(TAG, "trans_json: ");
        return javaBean;

    }

}
