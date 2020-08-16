package com.example.parkassistapp.DAL;

import android.provider.BaseColumns;

public class SavedParkingSpot {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private SavedParkingSpot(){}

    public static class SavedSpot implements BaseColumns {
        public static final String TABLE_NAME = "SavedSpot";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_PARKEDNR = "parkingNumber";
    }
}
