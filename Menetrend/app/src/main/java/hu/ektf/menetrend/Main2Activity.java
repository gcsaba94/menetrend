package hu.ektf.menetrend;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

import hu.ektf.menetrend.R;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, getIntent().getIntExtra("year", 0));
        cal.set(Calendar.MONTH, getIntent().getIntExtra("month", 0));
        cal.set(Calendar.DAY_OF_MONTH, getIntent().getIntExtra("day", 0));
        Date dateRepresentation = cal.getTime();
        Integer id = getId(dateRepresentation);
        String start = getIntent().getStringExtra("start"), end = getIntent().getStringExtra("end");
        if (start.contains(end)) {
            Intent intent = new Intent(Main2Activity.this, MainActivity.class);
            startActivity(intent);
        }
        ListView ls = (ListView) findViewById(R.id.lista);
        ArrayList<Jarat> list = new ArrayList<Jarat>();
        ArrayList<String> var = new ArrayList<>();
        var.add(start);
        var.add(end);
        Calendar calendar = (Calendar)cal.clone();
        Calendar cal2 = (Calendar)cal.clone();
        //TODO read file
        String sdCard = Environment.getExternalStorageDirectory().getAbsolutePath();
        File file = new File(sdCard+"/MenetrendData/menetrend.txt");
        try {
            Integer szamlalo = 0;
            InputStream is = new FileInputStream(file);
            Reader reader = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(reader);
            //String[] lines = Load(file);
            String line;
            while ((line = br.readLine()) != null) {
                if ((line.endsWith(" " + id.toString()) && line.startsWith(start + "#"))) {
                    String[] p = line.split(" ");
                    String[] pp = p[0].split("#");
                    String[] ido = pp[1].split(":");
                    cal = (Calendar) calendar.clone();
                    cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(ido[0]));
                    cal.set(Calendar.MINUTE, Integer.parseInt(ido[1]));
                    for (Integer j = 1; j < p.length; j++) {
                        if (p[j].startsWith(end)) {
                            pp = p[j].split("#");
                            ido = pp[1].split(":");
                            cal2 = (Calendar) calendar.clone();
                            cal2.set(Calendar.HOUR_OF_DAY, Integer.parseInt(ido[0]));
                            cal2.set(Calendar.MINUTE, Integer.parseInt(ido[1]));
                            list.add(szamlalo, new Jarat(var, cal, cal2));
                            szamlalo++;
                            break;
                        }
                    }
                }
            }
            br.close();
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        Log.v("Read", "Done");
        JaratAdapter adapter = new JaratAdapter(Main2Activity.this, list);
        ls.setAdapter(adapter);
    }
    /** /
     * Get ID from the Date */
    int getId(Date date) {
        String mmdd = DateFormat.format("mm-dd", date).toString();
        String dayOfWeek = DateFormat.format("EE", date).toString();

        if (mmdd == "12-24" || mmdd == "12-25" || mmdd == "12-26" || mmdd == "12-31" || mmdd == "01-01" || mmdd == "08-20" || mmdd == "10-23")
            return 4;
        else if (dayOfWeek.toLowerCase() == "sun")
            return 3;
        else if (dayOfWeek.toLowerCase() == "sat")
            return 2;
        else return 1;
    }

   /* public static String[] Load(File file){
        FileInputStream fis = null;
        try
        {
            fis = new FileInputStream(file);
        }
        catch (FileNotFoundException e) {e.printStackTrace();}
        InputStreamReader isr = new InputStreamReader(fis);
        BufferedReader br = new BufferedReader(isr);
        String test;
        int seged=0;
        try
        {
            while ((test=br.readLine()) != null)
            {
                seged++;
            }
        }
        catch (IOException e) {e.printStackTrace();}
        try
        {
            fis.getChannel().position(0);
        }
        catch (IOException e) {e.printStackTrace();}
        String[] array = new String[seged];
        String line;
        int i = 0;
        try
        {
            while((line=br.readLine())!=null)
            {
                array[i] = line;
                i++;
            }
        }
        catch (IOException e) {e.printStackTrace();}
        return array;
    }*/

    @Override
    public void onStart() {
        super.onStart();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main2 Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://hu.ektf.menetrend/http/host/path")
        );
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main2 Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://hu.ektf.menetrend/http/host/path")
        );
    }
}

