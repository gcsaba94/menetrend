package hu.ektf.menetrend;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Adapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Csaba on 2015.12.14..
 */
public class readtextfile extends AsyncTask<String, Integer, JaratAdapter> {

    private final static String FILE_URL = "http://ddl7.data.hu/get/0/9319860/menetrend.mtr";
    JaratAdapter jaratAdapter;
    String startVaros,endVaros;
    Integer Id;
    Calendar indDate,erkDate;
    ListView v;
    Context con;

    public readtextfile(String indVaros, String erkVaros, Date d , Integer id, ListView view, Context context){
        v = view;
        startVaros = indVaros;
        endVaros = erkVaros;
        Id = id;
        try {
            indDate = Calendar.getInstance();
            erkDate = Calendar.getInstance();
            indDate.set(Calendar.YEAR, d.getYear());
            indDate.set(Calendar.MONTH, d.getMonth());
            indDate.set(Calendar.DAY_OF_MONTH, d.getDay());
            erkDate.set(Calendar.YEAR, d.getYear());
            erkDate.set(Calendar.MONTH, d.getMonth());
            erkDate.set(Calendar.DAY_OF_MONTH, d.getDay());
            con = context;
            //Toast.makeText(context,indDate.toString(),Toast.LENGTH_LONG).show();
        }
        catch(Exception e){
            Toast.makeText(context,"na itt a baj",Toast.LENGTH_LONG).show();
        }
    }
    @Override
    protected JaratAdapter doInBackground(String... params) {
        try {
            URL url = new URL(FILE_URL);
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            Integer vege = 0;
            String line = "";
            ArrayList<Jarat> lista = new ArrayList<>();
            ArrayList<String> varosok = new ArrayList<>();
            varosok.add(startVaros);
            varosok.add(endVaros);
            /*while (!in.ready()) {
                Thread.sleep(100,10);
            }*/
            while ((line = in.readLine()) != null && vege != 2) {
                //get lines
                String[] darab = line.split("|");
                String[] indulo = darab[0].split("#");
                String[] erkezo = darab[darab.length-2].split("#");
                if(indulo[0] == startVaros){
                    vege = 1;
                    if(erkezo[0] == endVaros && darab[darab.length-1] == Id.toString()){
                        String[] indIdo = indulo[1].split(":");
                        String[] erkIdo = erkezo[1].split(":");
                        indDate.set(Calendar.HOUR,Integer.parseInt(indIdo[0]));
                        indDate.set(Calendar.MINUTE,Integer.parseInt(indIdo[1]));
                        erkDate.set(Calendar.HOUR,Integer.parseInt(erkIdo[0]));
                        erkDate.set(Calendar.MINUTE,Integer.parseInt(erkIdo[1]));
                        Jarat j = new Jarat(varosok,indDate,erkDate);
                        lista.add(j);
                        Toast.makeText(con,j.toString(),Toast.LENGTH_SHORT);
                    }
                }
                else if(vege == 1){
                    vege = 2;
                }
            }
            jaratAdapter = new JaratAdapter(con,lista);
            in.close();
            return jaratAdapter;
        } catch (MalformedURLException e) {

            e.printStackTrace();
        } catch (IOException e) {

            e.printStackTrace();
        }
        return null;
    }

    protected void onProgressUpdate() {
        //called when the background task makes any progress
    }

    protected void onPreExecute() {
        //called before doInBackground() is started
    }
    protected void onPostExecute(JaratAdapter result) {
        //called after doInBackground() has finished
        Toast.makeText(con,"PostEx",Toast.LENGTH_SHORT);
        super.onPostExecute(result);

    }
}
