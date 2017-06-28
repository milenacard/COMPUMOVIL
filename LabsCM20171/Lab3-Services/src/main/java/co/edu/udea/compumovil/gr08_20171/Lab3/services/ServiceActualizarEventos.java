package co.edu.udea.compumovil.gr08_20171.Lab3.services;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.preference.ListPreference;
import android.preference.PreferenceManager;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import co.edu.udea.compumovil.gr08_20171.Lab3.IpDispositivoServicio;
import co.edu.udea.compumovil.gr08_20171.Lab3.Models.dao.DbHelper;
import co.edu.udea.compumovil.gr08_20171.Lab3.Models.pojo.Event;

public class ServiceActualizarEventos extends Service {

    public DbHelper dbHelper;
    private SQLiteDatabase db;

    public ServiceActualizarEventos() {
    }

    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        actualizarEventos();

        return super.onStartCommand(intent, flags, startId);
    }

    public void actualizarEventos() {
        Thread hilo = new Thread(new Runnable() {

            @Override
            public void run() {
                final boolean[] nuevosEventos = {false};
                while (true) {
                    String urlContar = "http://" + IpDispositivoServicio.ip + ":3000/api/events/count";
                    RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

                    dbHelper = new DbHelper(getApplicationContext());
                    db  = dbHelper.getWritableDatabase();
                    List<Event> eventos = dbHelper.getAllEvents();

                    final int numEventosDB = eventos.size();

                    if (nuevosEventos[0]) {
                        String urlAllEvents = "http://" + IpDispositivoServicio.ip + ":3000/api/events";

                        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, urlAllEvents, new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                JsonParser jsonParser = new JsonParser();
                                JsonArray jsonArray = (JsonArray) jsonParser.parse(response.toString());

                                Gson gson;
                                gson = new Gson();
                                Event[] events = gson.fromJson(jsonArray, Event[].class);

                                if (events != null) {
                                    for (int i = numEventosDB; i < events.length; i++) {
                                        dbHelper.addSportEvent(events[i]);
                                    }


                                } else
                                    Log.d("Actualizar Eventos", "eventos es nulo");

                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("Actualizar Eventos", "Error: " + error.toString());
                            }
                        });
                        queue.add(jsonArrayRequest);
                    }

                    Log.i("Actualizar Eventos", "Se ha intentado actualizar los eventos");

                    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    String strTiempo = sharedPref.getString("tiempo", "60000");
                    long tiempo = Long.valueOf(strTiempo);

                    try {
                        Thread.sleep(tiempo);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        hilo.start();
    }

}
