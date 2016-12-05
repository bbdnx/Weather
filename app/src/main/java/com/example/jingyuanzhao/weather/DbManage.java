package com.example.jingyuanzhao.weather;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by jingyuanzhao on 2016/11/27.
 */

public class DbManage {
    private final int buff_size = 40000;
    public static final String Db_Name="db_weather.db";
    public static final String Package_Name="com.example.jingyuanzhao.weather";
    public static final String Db_Path="/data"+ Environment.getDataDirectory().getAbsolutePath()+"/"+Package_Name;

    private SQLiteDatabase DB;
    private Context context;

    public DbManage(Context context)
    {
        this.context=context;
    }

    public void setDataBase(SQLiteDatabase DB)
    {
        this.DB=DB;
    }

    public SQLiteDatabase getDataBase()
    {
        return DB;
    }

    public void OpenDataBase()
    {
        Log.i("DB_path",Db_Path+"/"+Db_Name);
        DB=OpenDataBase(Db_Path+"/"+Db_Name);
    }

    public SQLiteDatabase OpenDataBase(String path)
    {
        try
        {
            if(!new File(path).exists())
            {
                InputStream is = context.getResources().openRawResource(R.raw.db_weather);
                FileOutputStream fos = new FileOutputStream(path);
                int count=0;
                byte [] buffer = new byte[buff_size];
                while((count=is.read(buffer))>0)
                {
                    fos.write(buffer,0,count);
                }
                fos.close();
                is.close();

            }
            SQLiteDatabase db=SQLiteDatabase.openOrCreateDatabase(path,null);
            return db;

        }catch(FileNotFoundException e)
        {
            Log.e("Db","FileNotFound");
            e.printStackTrace();

        }catch(IOException e)
        {
            Log.e("DB","IOexception");
            e.printStackTrace();;
        }
        return null;
    }

    void Close_DataBase ()
    {
        DB.close();
    }
}
