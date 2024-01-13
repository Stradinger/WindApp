package de.ddnss.stradinger.myosmdroidmapapp;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import cz.msebera.android.httpclient.Header;

public class MapFragment extends Fragment {

    private MapView mMapView = null;
    private EditText editText = null;
    private static final String NOMINATIM_API_BASE_URL = "https://nominatim.openstreetmap.org/search";
    private static final String FORMAT = "json";
    private static final String TAG = "MapFragment";

    private static final String CUSTOM_USER_AGENT = "myOSMdroidmapApp/0.1";

    private View view = null;
    private TableLayout tableLayout = null;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_map, container, false);

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.view = view;
        //handle permissions first, before map is created. not depicted here

        editText = view.findViewById(R.id.editSearch);
        Button searchButton = view.findViewById(R.id.button_search);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performSearch();

            }
        });

        editText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    performSearch();
                    return true;
                }
                return false;
            }
        });

        mMapView = (MapView) view.findViewById(R.id.map);
        mMapView.setTileSource(TileSourceFactory.MAPNIK);


        // Zoom Buttons
        mMapView.setBuiltInZoomControls(true);
        mMapView.setMultiTouchControls(true);

        // Satteldorf als Punkt anzeigen
        IMapController mapController = mMapView.getController();
        mapController.setZoom(16);
        GeoPoint startPoint = new GeoPoint(49.16882259515287, 10.086843318646876);
        // Startpunkt Paris
        //GeoPoint startPoint = new GeoPoint(48.8583, 2.2944);
        mapController.setCenter(startPoint);


        Marker marker = new Marker(mMapView);
        marker.setPosition(startPoint);
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        mMapView.getOverlays().add(marker);
        mMapView.invalidate();
        //    marker.setIcon(context.getResources().getDrawable(R.drawable.ic_location));

        marker.setTitle(String.valueOf(""));

        tableLayout = view.findViewById(R.id.tableLayout);

        // Hier könntest du dynamisch Daten laden und hinzufügen
        // In diesem Beispiel werden statische Daten für eine Zeile hinzugefügt
   //     addRowToTable("Alice", "30", "Paris");
    }


    @Override
    public void onResume() {
        super.onResume();
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
        mMapView.onResume(); //needed for compass, my location overlays, v6.0.0 and up
    }

    @Override
    public void onPause() {
        super.onPause();
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().save(this, prefs);
        mMapView.onPause();  //needed for compass, my location overlays, v6.0.0 and up
    }


    private void queryCoordinatesForLocation(String locationName) {
        AsyncHttpClient client = new AsyncHttpClient();

        // Setze den benutzerdefinierten User-Agent-Header
        client.setUserAgent(CUSTOM_USER_AGENT);

        String apiUrl = buildNominatimUrl(locationName);


        client.get(apiUrl, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                try {
                    if (response.length() > 0) {
                        JSONObject location = response.getJSONObject(0);
                        double latitude = location.getDouble("lat");
                        double longitude = location.getDouble("lon");


                        // Satteldorf als Punkt anzeigen
                        IMapController mapController = mMapView.getController();
                        GeoPoint startPoint = new GeoPoint(latitude, longitude);
                        mapController.setCenter(startPoint);

                        getWeatherData(String.valueOf(latitude), String.valueOf(longitude));

                        //Toast.makeText(MainActivity.this, "Koordinaten für " + locationName + ": " + latitude + ", " + longitude, Toast.LENGTH_SHORT).show();
                    } else {
                        //Toast.makeText(MainActivity.this, "Koordinaten für " + locationName + " konnten nicht gefunden werden", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "Fehler bei der Verarbeitung der Nominatim-Antwort", e);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e(TAG, "Fehler bei der Nominatim-Anfrage: " + responseString, throwable);
            }
        });
    }

    private String buildNominatimUrl(String locationName) {
        return String.format("%s?q=%s&format=%s", NOMINATIM_API_BASE_URL, locationName, FORMAT);
    }

    /*

     */
    private void performSearch() {
        String searchQuery = editText.getText().toString();
        System.out.println("Butten Search: " + searchQuery);
        // Hier kannst du die Suchaktion durchführen, z.B. eine Kartenaktualisierung basierend auf der Suchanfrage.
        //   Toast.makeText(this, "Suche nach: " + searchQuery, Toast.LENGTH_SHORT).show();
        queryCoordinatesForLocation(searchQuery);
/*
        int newHeight = (int)0;
        if(tableLayout != null)
            newHeight = (int)tableLayout.getHeight();

        // Layout-Parameter der Map ändern
        ViewGroup.LayoutParams params = mMapView.getLayoutParams();
        params.height = mMapView.getHeight()-newHeight;
        mMapView.setLayoutParams(params);
*/
    }


    private List<de.ddnss.stradinger.myosmdroidmapapp.WeatherData.Temperature> aktTemperature;

    private void getWeatherData(String latitude, String longitude) {
        // Erstellt eine Queue, die alle Requests ausführt, die zu ihr hinzugefügt werden
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        // Die URL für den Request. Diese beinhaltet keine phrase, da die Function nur gestartet werden soll;
//        String url = "https://api.open-meteo.com/v1/forecast?latitude=49.168406&longitude=10.084367&current=temperature_2m,windspeed_10m&hourly=temperature_2m,relativehumidity_2m,windspeed_10m";
        //     String url = "https://api.open-meteo.com/v1/forecast?latitude=49.168406&longitude=10.084367";
        String url = "https://api.open-meteo.com/v1/forecast?latitude=" + latitude + "&longitude=" + longitude + "&current=temperature_2m,windspeed_10m&hourly=temperature_2m,relativehumidity_2m,windspeed_10m";
        System.out.println(url);

        // Erstellt den Request, der später abgesetzt werden soll
        StringRequest blankRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Das Ergebnis wird ignoriert
                System.out.println(response);
                // Der Code in dieser Methode wird ausgeführt, wenn der Request erfolgreich war.
                de.ddnss.stradinger.myosmdroidmapapp.WeatherData.Temperature temp = new Gson().fromJson(response, de.ddnss.stradinger.myosmdroidmapapp.WeatherData.Temperature.class);
                //aktTemperature = new Gson().fromJson(response, new TypeToken<List<Temperature>>() {}.getType());
                System.out.println("Elevation: " + temp.getElevation());
                System.out.println("CurrentUnit: " + temp.getCurrent_units());
                //       System.out.println(aktTemperature.toString());
                // Hier könntest du dynamisch Daten laden und hinzufügen
                // In diesem Beispiel werden statische Daten für eine Zeile hinzugefügt
                // Erst einmal alles löschen
                tableLayout.removeAllViews();
         /*       addRowToTable("Zeit: ",  temp.getCurrent().getActTime(),  temp.getCurrent_units().getTime());
                addRowToTable("Temparatur: ",  temp.getCurrent().getTemperature_2m().toString(),  temp.getCurrent_units().getTemperature_2m());
                addRowToTable("Windgeschwindigkeit: ",  temp.getCurrent().getWindspeed_10m().toString(),  temp.getCurrent_units().getWindspeed_10m());
*/
                addHoursJsonDatas(temp.getHourly().getTime());
                addTemperatureJsonDatas(temp.getHourly().getTemperature_2m(), temp.getCurrent_units().getTemperature_2m());
                addWindJsonDatas(temp.getHourly().getWindspeed_10m(), temp.getCurrent_units().getWindspeed_10m());
              //  addRowToTable("Windgeschwindigkeit: ",  temp.getHourly().toString(),  temp.getHourly_Units().toString());


            }
        }, null);

        // Fügt den Request zur RequestQueue hinzu um ihn abzusetzen
        requestQueue.add(blankRequest);
    }
    private void addRowToTable(String name, String age, String city) {


        // Erstelle eine neue TableRow
        TableRow row = new TableRow(view.getContext());
        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT
        );
        row.setLayoutParams(layoutParams);

        // Erstelle TextViews für Name, Alter und Stadt
        TextView nameTextView = new TextView(view.getContext());
        nameTextView.setText(name);

        TextView ageTextView = new TextView(view.getContext());
        ageTextView.setText(age);

        TextView cityTextView = new TextView(view.getContext());
        cityTextView.setText(city);

        // Füge TextViews zur TableRow hinzu
        row.addView(nameTextView);
        row.addView(ageTextView);
        row.addView(cityTextView);

        // Füge TableRow zur Tabelle hinzu
        tableLayout.addView(row);
    }

    private void addHoursJsonDatas(ArrayList<String> arr) {

        // Erstelle eine neue TableRow
        TableRow row = new TableRow(view.getContext());
        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT
        );
        row.setLayoutParams(layoutParams);



        // Erstelle eine neue TableRow für die Tage
        TableRow rowDay = new TableRow(view.getContext());
        TableRow.LayoutParams layoutParamsDay = new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT
                );
        row.setLayoutParams(layoutParamsDay);
        // Erstelle leeren TextView
        TextView emptyTextView = new TextView(view.getContext());
        emptyTextView.setText("");
        rowDay.addView(emptyTextView);

        // Erstelle leeren TextView
        TextView empty2TextView = new TextView(view.getContext());
        empty2TextView.setText("");
        rowDay.addView(empty2TextView);




        // Erstelle TextViews für Name, Alter und Stadt
        TextView hourTextView = new TextView(view.getContext());
        hourTextView.setText("Stunden");
        row.addView(hourTextView);

        // Erstelle TextViews mit einer Uhr
        TextView hourUnitTextView = new TextView(view.getContext());
        hourUnitTextView.setText("\uD83D\uDD53");
        row.addView(hourUnitTextView);

        // Aktuelles Datum und Uhrzeit mit der Calendar-Klasse abfragen
        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();

        // Format für das Datum und die Uhrzeit festlegen
  //      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

        // Formatieren des aktuellen Datums und der Uhrzeit
  //      dateFormat.format(currentDate);

        for(String s: arr) {
            TextView aktHourTextView = new TextView(view.getContext());


            // Wandele den Text in ein Date-Objekt um
            //Date date = convertTextToDate(s);
            Date date = convertTextToTime(s);

            if (date != null) {



                // Differenz zwischen den beiden Date-Objekten berechnen
                //long timeDifferenceMillis = currentDate.getTime() - date.getTime();
                long timeDifferenceMillis = date.getTime()- getTodayAtMidnight().getTime() ;
                // Differenz in Stunden umrechnen
                long hoursDifference = TimeUnit.MILLISECONDS.toHours(timeDifferenceMillis);
                aktHourTextView.setText(Long.toString(hoursDifference%24));

                if( hoursDifference % 24 == 0) {
                    // Erstelle Tages TextView
                    TextView dayTextView = new TextView(view.getContext());
                    dayTextView.setText(getDayOfWeek(date));

                    // Setze die Spans für die Zelle 2
                    TableRow.LayoutParams params = new TableRow.LayoutParams();
                    params.span = 24;
                    dayTextView.setLayoutParams(params);

                    rowDay.addView(dayTextView);
                }
            }


            // Füge TextViews zur TableRow hinzu
            row.addView(aktHourTextView);
        }


        // Füge TableRow zur Tabelle hinzu
        tableLayout.addView(rowDay);


        // Füge TableRow zur Tabelle hinzu
        tableLayout.addView(row);
    }

    private void addTemperatureJsonDatas(Double [] temperature_2m, String unit) {

        // Erstelle eine neue TableRow
        TableRow row = new TableRow(view.getContext());
        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT
        );
        row.setLayoutParams(layoutParams);

        // Erstelle TextViews für Name, Alter und Stadt
        TextView hourTextView = new TextView(view.getContext());
        hourTextView.setText("Temperatur");
        row.addView(hourTextView);

        // Erstelle TextViews für Name, Alter und Stadt
        TextView unitTextView = new TextView(view.getContext());
        unitTextView.setText(unit);
        row.addView(unitTextView);

        for(int i=0; i<temperature_2m.length; i++) {
            TextView aktTempTextView = new TextView(view.getContext());
            aktTempTextView.setText(" "+temperature_2m[i].toString());
            // Füge TextViews zur TableRow hinzu
            row.addView(aktTempTextView);
        }

        // Füge TableRow zur Tabelle hinzu
        tableLayout.addView(row);
    }

    private void addWindJsonDatas(Double [] windspeed_10m, String unit) {

        // Erstelle eine neue TableRow
        TableRow row = new TableRow(view.getContext());
        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT
        );
        row.setLayoutParams(layoutParams);

        // Erstelle TextViews für Name, Alter und Stadt
        TextView hourTextView = new TextView(view.getContext());
        hourTextView.setText("Wind");
        row.addView(hourTextView);

        // Erstelle TextViews für Name, Alter und Stadt
        TextView unitTextView = new TextView(view.getContext());
        unitTextView.setText(unit);
        row.addView(unitTextView);


        for(int i=0; i<windspeed_10m.length; i++) {
            TextView aktTempTextView = new TextView(view.getContext());
            aktTempTextView.setText(" "+windspeed_10m[i].toString());
            // Füge TextViews zur TableRow hinzu
            row.addView(aktTempTextView);
        }

        // Füge TableRow zur Tabelle hinzu
        tableLayout.addView(row);
    }

    private static Date convertTextToDate(String dateText) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        try {
            return dateFormat.parse(dateText);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Date convertTextToTime(String timeText) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.getDefault());
        try {
            // Hier setzen wir das Datum auf den 1. Januar 1970, da wir nur die Uhrzeit betrachten
            // Wenn das Datum im JSON vorhanden ist, kann es in der Umwandlung berücksichtigt werden
         //   Date baseDate = dateFormat.parse("00:00:00");
        //    Date parsedTime = dateFormat.parse(timeText);
            return dateFormat.parse(timeText); // new Date(baseDate.getTime() + parsedTime.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Calendar convertDateToCalendar(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    private static void subtractDaysFromCalendar(Calendar calendar, int days) {
        calendar.add(Calendar.DAY_OF_MONTH, -days);
    }

    private static String formatDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return dateFormat.format(date);
    }

    private static Date getTodayAtMidnight() {
        // Aktuelles Datum und Uhrzeit abrufen
        Calendar calendar = Calendar.getInstance();

        // Stunden, Minuten und Sekunden auf 0 setzen
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        // Das aktualisierte Calendar-Objekt als Date-Objekt zurückgeben
        return calendar.getTime();
    }

    private static String getDayOfWeek(Date date) {
        // Calendar-Instanz erstellen und das Date-Objekt setzen
      /*  Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        // Den Tag der Woche abfragen (1 für Sonntag, 2 für Montag, ..., 7 für Samstag)
        return calendar.get(Calendar.DAY_OF_WEEK);
*/
        // Locale für die gewünschte Sprache (z.B., Locale.GERMAN für Deutsch)
        Locale locale = Locale.getDefault();

        // Ein SimpleDateFormat-Objekt erstellen und das gewünschte Datumsformat festlegen
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE", locale);

        // Den Namen des Tages abfragen
        return dateFormat.format(date);
    }
}
