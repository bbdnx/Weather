package com.example.jingyuanzhao.weather;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    TextView text,text2;
    Spinner province_spinner, city_spinner;
    Button button_next;

    String province_name;
    String city_code;
    DbManage DbManager;
    SQLiteDatabase DB;
    Cursor cursor_for_province,cursor_for_city;
    ArrayAdapter<String> adapter2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        province_spinner=(Spinner)findViewById(R.id.Spinner01);
        city_spinner=(Spinner)findViewById(R.id.Spinner02);
        text =(TextView)findViewById(R.id.spinnerText);
        text2 = (TextView)findViewById(R.id.spinnerText2);
        text.setText("choose a province\n");
        text2.setText("choose a city\n");

        DbManager=new DbManage(this);
        DbManager.OpenDataBase();
        DB=DbManager.getDataBase();

        cursor_for_province = DB.rawQuery("select name from provinces",null);


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        while(cursor_for_province.moveToNext())
        {
            adapter.add(cursor_for_province.getString(0));
        }
        /*adapter2 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);*/

        province_spinner.setAdapter(adapter);

        province_spinner.setVisibility(View.VISIBLE);
        city_spinner.setVisibility(View.VISIBLE);
        text2.setVisibility(View.VISIBLE);
        province_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                province_name=(String)province_spinner.getSelectedItem();

                city_spinner.setVisibility(View.VISIBLE);
                text2.setVisibility(View.VISIBLE);

                adapter2 = new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_spinner_item);
                adapter2.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

                cursor_for_city=DB.rawQuery("select name,city_num from citys where province_id=?",new String[]{String.valueOf(position)});
                while(cursor_for_city.moveToNext())
                {
                    adapter2.add(cursor_for_city.getString(0));
                    //System.out.println(cursor.getString(0));
                }
                city_spinner.setAdapter(adapter2);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        city_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cursor_for_city.moveToPosition(position);
                city_code=cursor_for_city.getString(1);
                Log.i("warning",city_code);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        button_next=(Button)findViewById(R.id.Button_next);
        button_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(MainActivity.this,WeatherActivity.class);
                intent.putExtra("city_code",city_code);
                startActivity(intent);
            }
        });
    }
}
