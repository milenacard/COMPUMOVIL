package co.edu.udea.compumovil.gr08_20171.Lab4.Models.dao;

import android.provider.BaseColumns;

/**
 * Created by Milena Cardenas on 02-mar-17.
 */

public class DBInfomation {

    public static final String DB_NAME = "Lab2.db";
    public static final int DB_VERSION = 1;
    public static final String USER_TABLE = "users";
    public static final String EVENT_TABLE = "events";
    //public static final String PREF_FILE_NAME = "data";


    //Columnas de la tabla
    public class ColumnUser {
        public static final String ID = BaseColumns._ID;
        public static final String USERNAME = "username";
        public static final String EMAIL = "email";
        public static final String PASSWORD = "password";
        public static final String NAME = "nombre";
        public static final String AGE = "edad";
        public static final String PHOTO = "foto";
    }


    public class ColumnEvent {
        public static final String ID = BaseColumns._ID;
        public static final String PHOTO = "photo";
        public static final String NAME = "name";
        public static final String DESCRIPTION = "description";
        public static final String SCORE = "score";
        public static final String RESPONSIBLE = "responsible";
        public static final String DATE = "date";
        public static final String LATITUDE = "latitude";
        public static final String LONGITUDE = "Longitude";
        public static final String GENERALINFORMATION = "generalInformation";

    }

}
