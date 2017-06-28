package co.edu.udea.compumovil.gr08_20171.Lab3.Models.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;
import co.edu.udea.compumovil.gr08_20171.Lab3.Models.pojo.Event;
import co.edu.udea.compumovil.gr08_20171.Lab3.Models.pojo.user;
import co.edu.udea.compumovil.gr08_20171.Lab3.R;


/**
 * Created by Milena Cárdenas on 06-mar-17.
 */

public class DbHelper extends SQLiteOpenHelper {

    private static final String TAG = DbHelper.class.getSimpleName();
    private SQLiteDatabase myDb=null;
    public DbHelper(Context context) {
        super(context, DBInfomation.DB_NAME, null, DBInfomation.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        myDb=db;
        /*
        String sql = String
                .format("create table %s (%s int primary key, %s text, %s text, %s text, %s text, %s int, %s BLOB)",
                        DBInfomation.USER_TABLE, DBInfomation.ColumnUser.ID,
                        DBInfomation.ColumnUser.USERNAME, DBInfomation.ColumnUser.EMAIL,
                        DBInfomation.ColumnUser.PASSWORD, DBInfomation.ColumnUser.NAME,
                        DBInfomation.ColumnUser.AGE, DBInfomation.ColumnUser.PHOTO);
        Log.d(TAG, "onCreate with SQL: " + sql);
        db.execSQL(sql); */

        String sql2 = String
                .format("create table %s (%s int primary key, %s text, %s text, %s text, %s int, %s int, %s int, %s text, %s float, %s text)",
                        DBInfomation.EVENT_TABLE, DBInfomation.ColumnEvent.ID,
                        DBInfomation.ColumnEvent.NAME, DBInfomation.ColumnEvent.PHOTO,
                        DBInfomation.ColumnEvent.DESCRIPTION, DBInfomation.ColumnEvent.DATE,
                        DBInfomation.ColumnEvent.LATITUDE, DBInfomation.ColumnEvent.LONGITUDE,
                        DBInfomation.ColumnEvent.RESPONSIBLE, DBInfomation.ColumnEvent.SCORE,
                        DBInfomation.ColumnEvent.GENERALINFORMATION);
        Log.d(TAG, "onCreate with SQL: " + sql2);
        db.execSQL(sql2);
        loadEvent(db);
    }

    private void loadEvent(SQLiteDatabase db){
        ContentValues balonmano = new ContentValues();
        balonmano.put(DBInfomation.ColumnEvent.ID,"1");
        balonmano.put(DBInfomation.ColumnEvent.NAME, "Balonmano"); // Contact Name
        balonmano.put(DBInfomation.ColumnEvent.PHOTO ,"R.drawable.balonmano");
        balonmano.put(DBInfomation.ColumnEvent.DESCRIPTION,"XXV Campeonato Mundial de Balonmano Femenino");
        balonmano.put(DBInfomation.ColumnEvent.DATE ,"01/12/2017");
        balonmano.put(DBInfomation.ColumnEvent.LATITUDE,"51.0");
        balonmano.put(DBInfomation.ColumnEvent.LONGITUDE ,"9.0");
        balonmano.put(DBInfomation.ColumnEvent.RESPONSIBLE, "Alemania");
        balonmano.put(DBInfomation.ColumnEvent.SCORE ,"2");
        balonmano.put(DBInfomation.ColumnEvent.GENERALINFORMATION,"Un total de veinticuatro selecciones nacionales de cuatro confederaciones continentales competiran por el titulo de campeón mundial, cuyo actual portador es el equipo de Noruega, vencedor del Mundial de 2015.");

        //
        db.insert(DBInfomation.EVENT_TABLE, null, balonmano);

        ContentValues tiroDeportivo = new ContentValues();
        tiroDeportivo.put(DBInfomation.ColumnEvent.ID,"2");
        tiroDeportivo.put(DBInfomation.ColumnEvent.NAME, "Tiro Deportivo"); // Contact Name
        tiroDeportivo.put(DBInfomation.ColumnEvent.PHOTO, "R.drawable.tirodeportivo");
        tiroDeportivo.put(DBInfomation.ColumnEvent.DESCRIPTION,"Copa del Mundo de Shotgun");
        tiroDeportivo.put(DBInfomation.ColumnEvent.DATE ,"19/03/2017");
        tiroDeportivo.put(DBInfomation.ColumnEvent.LATITUDE,"16.8633600");
        tiroDeportivo.put(DBInfomation.ColumnEvent.LONGITUDE ,"-99.8901000");
        tiroDeportivo.put(DBInfomation.ColumnEvent.RESPONSIBLE, "Mexico");
        tiroDeportivo.put(DBInfomation.ColumnEvent.SCORE ,"4");
        tiroDeportivo.put(DBInfomation.ColumnEvent.GENERALINFORMATION,"La ciudad de Acapulco será la sede de la Copa del Mundo de Shotgun 2017 de la ISSF");

        db.insert(DBInfomation.EVENT_TABLE, null, tiroDeportivo);

        ContentValues patinajeArtístico = new ContentValues();
        patinajeArtístico.put(DBInfomation.ColumnEvent.ID,"3");
        patinajeArtístico.put(DBInfomation.ColumnEvent.NAME, "Patinaje Artistico"); // Contact Name
        patinajeArtístico.put(DBInfomation.ColumnEvent.PHOTO, "R.drawable.campeonato_mundial_de_patinaje_artstico_junior_isu");
        patinajeArtístico.put(DBInfomation.ColumnEvent.DESCRIPTION,"Campeonato Mundial Junior 2017");
        patinajeArtístico.put(DBInfomation.ColumnEvent.DATE ,"15/03/2017");
        patinajeArtístico.put(DBInfomation.ColumnEvent.LATITUDE,"25.0477600");
        patinajeArtístico.put(DBInfomation.ColumnEvent.LONGITUDE ,"121.5318500");
        patinajeArtístico.put(DBInfomation.ColumnEvent.RESPONSIBLE, "Taiwan");
        patinajeArtístico.put(DBInfomation.ColumnEvent.SCORE ,"1");
        patinajeArtístico.put(DBInfomation.ColumnEvent.GENERALINFORMATION,"Este campeonato es el segundo en importancia tras los Juegos Olimpicos de Invierno y el Campeonato mundial de patinaje artístico tanto en tamanho como importancia.");

        db.insert(DBInfomation.EVENT_TABLE, null, patinajeArtístico);

        ContentValues formula1 = new ContentValues();
        formula1.put(DBInfomation.ColumnEvent.ID,"4");
        formula1.put(DBInfomation.ColumnEvent.NAME, "Formula 1"); // Contact Name
        formula1.put(DBInfomation.ColumnEvent.PHOTO, "R.drawable.australia_20122");
        formula1.put(DBInfomation.ColumnEvent.DESCRIPTION,"Gran Premio de Australia");
        formula1.put(DBInfomation.ColumnEvent.DATE ,"26/03/2017");
        formula1.put(DBInfomation.ColumnEvent.LATITUDE,"-27.0");
        formula1.put(DBInfomation.ColumnEvent.LONGITUDE ,"133.0");
        formula1.put(DBInfomation.ColumnEvent.RESPONSIBLE, "Alemania");
        formula1.put(DBInfomation.ColumnEvent.SCORE ,"3");
        formula1.put(DBInfomation.ColumnEvent.GENERALINFORMATION,"El Gran Premio de Australia de 2017 será la primera carrera de la temporada 2017 de Fórmula 1, que se disputara en el Circuito de Albert Park, en Melbourne (Australia).");

        db.insert(DBInfomation.EVENT_TABLE, null, formula1);

        ContentValues patinaje = new ContentValues();
        patinaje.put(DBInfomation.ColumnEvent.ID,"5");
        patinaje.put(DBInfomation.ColumnEvent.NAME, "Patinaje sobre hielo"); // Contact Name
        patinaje.put(DBInfomation.ColumnEvent.PHOTO, "R.drawable.hockey");
        patinaje.put(DBInfomation.ColumnEvent.DESCRIPTION,"Campeonato Mundial femenino 2017");
        patinaje.put(DBInfomation.ColumnEvent.DATE ,"31/03/2017");
        patinaje.put(DBInfomation.ColumnEvent.LATITUDE,"38.0");
        patinaje.put(DBInfomation.ColumnEvent.LONGITUDE ,"-97.0");
        patinaje.put(DBInfomation.ColumnEvent.RESPONSIBLE, "EEUU");
        patinaje.put(DBInfomation.ColumnEvent.SCORE ,"2");
        patinaje.put(DBInfomation.ColumnEvent.GENERALINFORMATION,"Es la maxima competición internacional entre selecciones nacionales de hockey sobre hielo.");

        db.insert(DBInfomation.EVENT_TABLE, null, patinaje);
        Log.d("DBINFO","Inserting all Events");
    }

    public user consultarUsuario(String user) {
        String[] campos = new String[]{DBInfomation.ColumnUser.EMAIL, DBInfomation.ColumnUser.PHOTO};
        String[] argumentos = new String[]{user};
        String consulta = DBInfomation.ColumnUser.USERNAME + "=?";
        SQLiteDatabase db = this.getWritableDatabase();
        user usuario = null;
        try {
            Cursor c = db.query(DBInfomation.USER_TABLE, campos, consulta, argumentos, null, null, null);
            //Nos aseguramos de que existe al menos {un registro
            if (c.moveToFirst()) {
                //Recorremos el cursor hasta que no haya más registros
                do {
                    usuario = new user();
                    usuario.setName(user);
                    usuario.setEmail(c.getString(c.getColumnIndex(DBInfomation.ColumnUser.EMAIL)));
                    usuario.setPhoto(c.getString(c.getColumnIndex(DBInfomation.ColumnUser.PHOTO)));
                    //usuario.setPhoto(c.getBlob(c.getColumnIndex(DBInfomation.ColumnUser.PHOTO)));
                } while (c.moveToNext());
                Log.d(TAG, "Se ha consultado en la base de datos");
            }else{
                Log.d(TAG,"No hay registro");
            }
            c.close();
        } catch (Exception ex) {
            Log.e(TAG, "Error al consultar en la base de datos");
        } finally {
            db.close();
        }
        return usuario;
    }

    public List<Event> getAllEvents() {
        List<Event> SportsEventsList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + DBInfomation.EVENT_TABLE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // Agregar a la lista
        if (cursor.moveToFirst()) {
            do {
                Event sportEvent = new Event();
                //sportEvent.setId(Integer.parseInt(cursor.getString(0)));
                sportEvent.setName(cursor.getString(1));
                sportEvent.setPhoto(cursor.getString(2));
                sportEvent.setDescription(cursor.getString(3));
                sportEvent.setDate(cursor.getString(4));
                sportEvent.setLatitude(Double.parseDouble(cursor.getString(5)));
                sportEvent.setLongitude(Double.parseDouble(cursor.getString(6)));
                sportEvent.setResponsible(cursor.getString(7));
                sportEvent.setScore(Integer.parseInt(cursor.getString(8)));
                sportEvent.setGeneralInformation(cursor.getString(9));

                // Adding contact to list
                SportsEventsList.add(sportEvent);
            } while (cursor.moveToNext());
        }
        // return contact list
        cursor.close();
        return SportsEventsList;
    }

    // Adding new SportEvent
    public void addSportEvent(Event sportevent) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DBInfomation.ColumnEvent.NAME, sportevent.getName());
        values.put(DBInfomation.ColumnEvent.PHOTO, sportevent.getPhoto());
        values.put(DBInfomation.ColumnEvent.DESCRIPTION, sportevent.getDescription());
        values.put(DBInfomation.ColumnEvent.DATE, sportevent.getDate());
        values.put(DBInfomation.ColumnEvent.LATITUDE, sportevent.getLatitude());
        values.put(DBInfomation.ColumnEvent.LONGITUDE, sportevent.getLongitude());
        values.put(DBInfomation.ColumnEvent.RESPONSIBLE, sportevent.getResponsible());
        values.put(DBInfomation.ColumnEvent.SCORE, sportevent.getScore());
        values.put(DBInfomation.ColumnEvent.GENERALINFORMATION, sportevent.getGeneralInformation());

        // Inserting Row
        db.insert(DBInfomation.EVENT_TABLE, null, values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL( "DROP TABLE IF EXISTS " + DBInfomation.USER_TABLE);//Borrar tabla
        db.execSQL("DROP TABLE IF EXISTS " + DBInfomation.EVENT_TABLE);
        onCreate(db); //Crear tabla de nuevo
    }

    public String loginQuery(SQLiteDatabase db, String username, String password) {
        String[] campos = new String[]{DBInfomation.ColumnUser.USERNAME, DBInfomation.ColumnUser.PASSWORD, DBInfomation.ColumnUser.EMAIL};
        String[] args = new String[]{username};
        Cursor result;
        try{
           result = db.query(DBInfomation.USER_TABLE, campos, "username=?", args, null, null, null);
            if (result.moveToFirst()) {
                Log.i("DBINFO",result.getString(0));
                Log.i("DBINFO",result.getString(1));
                if(result.getString(0).equals(username) && result.getString(1).equals(password)){
                    return result.getString(2);
                }else{
                    return null;
                }
            }
        }catch (Exception e){
            Log.i("DBINFO", "Error: "+e);

        }
        return null;
    }
    public boolean registerQuery(SQLiteDatabase db,String username, String email, String password, String age){
        try{
            ContentValues values = new ContentValues();
            //values.put(user.Column.ID,); //Se pasan pares nombre-valor
            values.put(DBInfomation.ColumnUser.USERNAME, username);
            values.put(DBInfomation.ColumnUser.EMAIL, email);
            values.put(DBInfomation.ColumnUser.PASSWORD, password);
            values.put(DBInfomation.ColumnUser.AGE, age);
            db.insertWithOnConflict(DBInfomation.USER_TABLE, null, values, SQLiteDatabase.CONFLICT_IGNORE);
            return true;
        }catch(Exception e){
            return false;
        }
    }

    /*public boolean createEventDB(SQLiteDatabase db,
                                 String nameEvent,
                                 String responsable,
                                 float score,
                                 String date,
                                 String descripcion,
                                 String informacionGeneral){
        try{
            ContentValues values = new ContentValues();
            values.put(DBInfomation.ColumnEvent.NAME, nameEvent); // Contact Name
            values.put(DBInfomation.ColumnEvent.DESCRIPTION,descripcion);
            values.put(DBInfomation.ColumnEvent.DATE ,date);
            values.put(DBInfomation.ColumnEvent.RESPONSIBLE, responsable);
            values.put(DBInfomation.ColumnEvent.SCORE ,score);
            values.put(DBInfomation.ColumnEvent.GENERALINFORMATION, informacionGeneral);

            db.insertWithOnConflict(DBInfomation.EVENT_TABLE, null, values, SQLiteDatabase.CONFLICT_IGNORE);
            return true;
        }catch(Exception e){
            return false;
        }
    }*/


}