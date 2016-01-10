package hu.ektf.menetrend;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;

import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

import hu.ektf.menetrend.R;

public class Main2Activity extends AppCompatActivity {
    private final static String FILE_URL = "http://data.hu/get/9319860/menetrend.mtr";
    private AsyncTask<String, Integer, JaratAdapter> task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Send reply", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, getIntent().getIntExtra("year",0));
        cal.set(Calendar.MONTH, getIntent().getIntExtra("month", 0));
        cal.set(Calendar.DAY_OF_MONTH, getIntent().getIntExtra("day", 0));
        Date dateRepresentation = cal.getTime();
        Integer id = getId(dateRepresentation);
        String start = getIntent().getStringExtra("start"),end = getIntent().getStringExtra("end");
        if(start == end){
            Intent intent = new Intent(Main2Activity.this,MainActivity.class);
            startActivity(intent);
        }
        ListView ls = (ListView) findViewById(R.id.lista);
        ArrayList<Jarat> list = new ArrayList<Jarat>();
        ArrayList<String> var = new ArrayList<>();
        var.add(start);
        var.add(end);
        Calendar cal2 = (Calendar)cal.clone();
            /*Random r = new Random();
            for (Integer i = 0; i < 20; i++) {
                cal = Calendar.getInstance();
                cal.set(Calendar.HOUR,r.nextInt(5) );
                cal.set(Calendar.MINUTE,r.nextInt(59));
                cal2 = (Calendar) cal.clone();
                cal2.add(Calendar.MINUTE, r.nextInt(30));
                list.add(i, new Jarat(var, cal, cal2));
            }*/
        //TODO read file
        File sdCard = Environment.getExternalStorageDirectory();
        File file = new File(sdCard,"menetrend.mtr");
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ( (line = br.readLine())!= null && !line.startsWith(start)){}
            line = br.readLine();
            while ( (line = br.readLine())!= null && line.startsWith(start)){
                if(line.endsWith(id.toString())){
                    String[] p = line.split("|");
                    cal.set(Calendar.HOUR,Integer.parseInt(p[0].split("#")[1].split(":")[0]));
                    cal.set(Calendar.MINUTE, Integer.parseInt(p[0].split("#")[1].split(":")[1]));
                    Integer i;
                    for (i=1; i< p.length && p[i].startsWith(end); i++){}
                    if(i<p.length){
                        cal2.set(Calendar.HOUR,Integer.parseInt(p[i].split("#")[1].split(":")[0]));
                        cal2.set(Calendar.MINUTE, Integer.parseInt(p[i].split("#")[1].split(":")[1]));
                        list.add(new Jarat(var, cal, cal2));
                    }
                }
            }
            br.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        JaratAdapter adapter = new JaratAdapter(Main2Activity.this, list);
        ls.setAdapter(adapter);
    }

    int getId(Date date) {
        String mmdd = DateFormat.format("mm-dd",date).toString();
        String dayOfWeek = DateFormat.format("EE",date).toString();
        if(mmdd == "12-24" || mmdd == "12-25" || mmdd == "12-26" || mmdd == "12-31" || mmdd == "01-01" || mmdd == "08-20" || mmdd == "10-23")
            return 4;
        else if(dayOfWeek.toLowerCase() == "sunday")
            return 3;
        else if(dayOfWeek.toLowerCase() == "saturday")
            return 2;
        else return 1;
    }
}

