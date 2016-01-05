package hu.ektf.menetrend;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.provider.CalendarContract;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static android.support.v4.app.ActivityCompat.startActivity;

/**
 * Created by Csaba on 2015.12.13..
 */
public class JaratAdapter extends ArrayAdapter<Jarat> {
    private static final String TAG = "AsyncListApp_List";
    private static final int LAYOUT = android.R.layout.two_line_list_item;

    public JaratAdapter(Context context, ArrayList<Jarat> items) {
        super(context, LAYOUT, items);
    }

    static class ViewHolder {
        public TextView Indulo;
        public TextView Erkezo;
        public TextView IdoInd;
        public TextView IdoErk;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        if (rowView == null) {
            rowView = View.inflate(getContext(), LAYOUT, null);

            ViewHolder viewHolder = new ViewHolder();
            viewHolder.Indulo = (TextView) rowView.findViewById(android.R.id.text1);
            viewHolder.Erkezo = (TextView) rowView.findViewById(android.R.id.text2);
            rowView.setTag(viewHolder);
            Log.v(TAG, "List item created");
        }

        ViewHolder holder = (ViewHolder) rowView.getTag();
        final Jarat item = getItem(position);
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        Date i = item.getInduloIdo().getTime(),e=item.getErkezoIdo().getTime();
        String ind = format.format(i).toString(),erk=format.format(e).toString();
        holder.Erkezo.setText(getContext().getResources().getString(R.string.end) + ":  " + item.getErkezo() + "  " + erk);
        holder.Indulo.setText(getContext().getResources().getString(R.string.start) + ":  " + item.getIndulo() + "  " + ind);

        rowView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(Intent.ACTION_INSERT)
                        .setData(CalendarContract.Events.CONTENT_URI)
                        .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, item.getInduloIdo().getTimeInMillis()-20000)
                        .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, item.getInduloIdo().getTimeInMillis())
                        .putExtra(CalendarContract.Events.TITLE, "Busz")
                        .putExtra(CalendarContract.Events.DESCRIPTION, "Indul a busz")
                        .putExtra(CalendarContract.Events.EVENT_LOCATION, item.getIndulo())
                        .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY);
                //Toast.makeText(getContext(), item.getInduloIdo().toString(), Toast.LENGTH_SHORT).show();
                getContext().startActivity(intent);
                return true;
            }
        });
        Log.v(TAG, "List item returned");
        return rowView;
    }
}
