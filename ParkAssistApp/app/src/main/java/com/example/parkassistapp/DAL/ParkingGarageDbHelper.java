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

public class ParkingGarageDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "ParkingGarages.db";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + ParkingGarages.TABLE_NAME + " (" +
                    ParkingGarages._ID + " INTEGER PRIMARY KEY," +
                    ParkingGarages.COLUMN_NAME_COMPANY + " TEXT," +
                    ParkingGarages.COLUMN_NAME_NAME + " TEXT," +
                    ParkingGarages.COLUMN_NAME_COORDINATESX + " DOUBLE," +
                    ParkingGarages.COLUMN_NAME_COORDINATESY + " DOUBLE)";
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + ParkingGarages.TABLE_NAME;

    public ParkingGarageDbHelper(Context context) {
        super(context, ParkingGarages.TABLE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //drop and remake db
        sqLiteDatabase.execSQL(SQL_DELETE_ENTRIES);
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
        String selectQuery = "SELECT  * FROM " + ParkingGarages.TABLE_NAME;
        Cursor cursor = db.rawQuery(selectQuery, null);

        // Run through the cursor
        while(cursor.moveToNext()) {

            int id = cursor.getInt(cursor.getColumnIndexOrThrow(ParkingGarages._ID));
            String company = cursor.getString(cursor.getColumnIndexOrThrow(ParkingGarages.COLUMN_NAME_COMPANY));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(ParkingGarages.COLUMN_NAME_NAME));
            Double coordinatesX = cursor.getDouble(cursor.getColumnIndexOrThrow(ParkingGarages.COLUMN_NAME_COORDINATESX));
            Double coordinatesY = cursor.getDouble(cursor.getColumnIndexOrThrow(ParkingGarages.COLUMN_NAME_RELEASEDATE));
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
            SQLiteDatabase db = getWritableDatabase();

            ParkingGarage ParkingGarage = params[0].getParkingGarage();

            // Create a new map of values, where column names are the keys
            ContentValues values = new ContentValues();
            values.put(ParkingGarages.COLUMN_NAME_COMPANY, ParkingGarage.getCompany());
            values.put(ParkingGarages.COLUMN_NAME_NAME, ParkingGarage.getName());
            values.put(ParkingGarages.COLUMN_NAME_COORDINATEX, ParkingGarage.getCoordinateX());
            values.put(ParkingGarages.COLUMN_NAME_COORDINATEY, ParkingGarage.getCoordinateY());


            Log.d("SQL", "inserting new row");
            // Insert the new row, returning the primary key value of the new row
            long newRowId = db.insert(ParkingGarages.TABLE_NAME, null, values);
            Log.d("SQL", "new row id" + newRowId);

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

            // Define 'where' part of query.
            String selection = ParkingGarages._ID + " LIKE ?";
            // Specify arguments in placeholder order.
            String[] selectionArgs = { Integer.toString(parkingGarage.getId()) };
            // Issue SQL statement.
            db.delete(ParkingGarages.TABLE_NAME, selection, selectionArgs);
            Log.d("SQL", "deleting ... ");

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

