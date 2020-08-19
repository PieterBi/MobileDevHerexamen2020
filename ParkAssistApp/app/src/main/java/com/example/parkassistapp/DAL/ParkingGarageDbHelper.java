package com.example.parkassistapp.DAL;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.util.Log;

import com.example.parkassistapp.Model.ParkingGarage;
import com.example.parkassistapp.Activities.BaseActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class ParkingGarageDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "ParkingGarages.db";
    public static final String PARKING_TABLE_NAME = "parkinggarages";
    public static final String PARKING_COLUMN_ID = "id";
    public static final String PARKING_COLUMN_COMPANY = "company";
    public static final String PARKING_COLUMN_NAME = "name";
    public static final String PARKING_COLUMN_COORDINATESX = "coordinatesX";
    public static final String PARKING_COLUMN_COORDINATESY = "coordinatesY";
    private HashMap hp;

//    private static final String SQL_CREATE_ENTRIES =
//            "CREATE TABLE " + ParkingGarages.TABLE_NAME + " (" +
//                    ParkingGarages._ID + " INTEGER PRIMARY KEY," +
//                    ParkingGarages.COLUMN_NAME_COMPANY + " TEXT," +
//                    ParkingGarages.COLUMN_NAME_NAME + " TEXT," +
//                    ParkingGarages.COLUMN_NAME_COORDINATESX + " TEXT," +
//                    ParkingGarages.COLUMN_NAME_COORDINATESY + " TEXT)";
//    private static final String SQL_DELETE_ENTRIES =
//            "DROP TABLE IF EXISTS " + ParkingGarages.TABLE_NAME;

    public ParkingGarageDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(
                "create table parkinggarages" +
                "(id integer primary key, company text, name text, coordinatesx text, coordinatesy text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //drop and remake db
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS parking");
        onCreate(sqLiteDatabase);
    }

    public void insertGarage(ParkingGarage p, Activity a) {
        Params params = new Params(p, a);
        new InsertTask().execute(params);
        Log.d("SQL", "starting task");
    }

    public void deleteGarage(ParkingGarage p, Activity a)
    {
        Log.d("SQL", "Executing remove task");
        Params params = new Params(p, a);
        new RemoveTask().execute(params);
    }

    public ArrayList<ParkingGarage> getAllParkingGarages() {
        ArrayList<ParkingGarage> parkingGaragesList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        // Select All Query
        String selectQuery = "SELECT  * FROM parkinggarages";
        Cursor cursor = db.rawQuery(selectQuery, null);

        // Run through the cursor
        while(cursor.moveToNext()) {

            int id = cursor.getInt(cursor.getColumnIndexOrThrow(PARKING_COLUMN_ID));
            String company = cursor.getString(cursor.getColumnIndexOrThrow(PARKING_COLUMN_COMPANY));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(PARKING_COLUMN_NAME));
            String coordinatesX = cursor.getString(cursor.getColumnIndexOrThrow(PARKING_COLUMN_COORDINATESX));
            String coordinatesY = cursor.getString(cursor.getColumnIndexOrThrow(PARKING_COLUMN_COORDINATESY));
            ParkingGarage p = new ParkingGarage(id, company, name, coordinatesX, coordinatesY);

            parkingGaragesList.add(p);
        }
        cursor.close();

        return parkingGaragesList;
    }

    private class InsertTask extends AsyncTask<Params, Void, Params> {

        @Override
        protected Params doInBackground(Params... params) {
            // Gets the data repository in write mode
            SQLiteDatabase sqLiteDatabase = getWritableDatabase();

            ParkingGarage p = params[0].getParkingGarage();

            // Create a new map of values, where column names are the keys
            ContentValues contentValues = new ContentValues();
            contentValues.put("company", p.getCompany());
            contentValues.put("name", p.getName());
            contentValues.put("coordinatesx", p.getCoordinateX());
            contentValues.put("coordinatesy", p.getCoordinateY());
            sqLiteDatabase.insert("parkinggarages", null, contentValues);

            Log.d("SQL", "inserting new row");

            return params[0];
        }

        @Override
        protected void onPostExecute(Params aParams) {

            ((BaseActivity)aParams.getActivity()).update();
        }
    }

    private class RemoveTask extends AsyncTask<Params, Void, Activity> {

        @Override
        protected Activity doInBackground(Params... params) {
            // Gets the data repository in write mode
            SQLiteDatabase db = getWritableDatabase();

            // Get the book from Params
            ParkingGarage parkingGarage = params[0].getParkingGarage();

            db.delete("contacts",
                    "id = ? ",
                    new String[] { Integer.toString(parkingGarage.getId()) });

            return params[0].getActivity();
        }

        @Override
        protected void onPostExecute(Activity aActivity) {
            ((BaseActivity)aActivity).update();
        }
    }

    private class Params {
        private ParkingGarage _parkingGarage;
        private Activity _activity;

        public Params(ParkingGarage p, Activity a) {
            _parkingGarage = p;
            _activity = a;
        }

        public ParkingGarage getParkingGarage() {
            return _parkingGarage;
        }

        public Activity getActivity() {
            return _activity;
        }
    }
}

