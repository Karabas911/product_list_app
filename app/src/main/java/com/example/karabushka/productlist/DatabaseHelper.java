package com.example.karabushka.productlist;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static com.example.karabushka.productlist.DatabaseContract.*;


public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String TAG = "fragment1";
    public static int VERSION = 1;
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null,VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_NEIGHBOR_TABLE = "CREATE TABLE " + NeighborList.TABLE_NAME+
                " ("+ NeighborList.ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                + NeighborList.NEIGHBORS_NAME+" TEXT NOT NULL)";

        String SQL_CREATE_PRODUCT_TABLE = "CREATE TABLE " + DatabaseContract.ProductList.TABLE_NAME+
                " ("+ ProductList._ID+" INTEGER PRIMARY KEY, "
                + ProductList.NEIGHBOR_ID + " INTEGER NOT NULL, "
                + ProductList.PRODUCT_NAME+" TEXT NOT NULL, "
                + ProductList.NUMBER+ " INTEGER NOT NULL, "
                + ProductList.DATE+ " TIMESTAMP DEFAULT CURRENT_DATE," +
                "FOREIGN KEY (" +ProductList.NEIGHBOR_ID+") REFERENCES "+ NeighborList.TABLE_NAME + "("+NeighborList._ID+"))";

        db.execSQL(SQL_CREATE_NEIGHBOR_TABLE);
        db.execSQL(SQL_CREATE_PRODUCT_TABLE);

        Log.d(TAG, "--------------Database created-------------");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        final String SQL_DROP_N= "DROP TABLE IF EXISTS "+ NeighborList.TABLE_NAME;
        final String SQL_DROP_P= "DROP TABLE IF EXISTS "+ ProductList.TABLE_NAME;
        db.execSQL(SQL_DROP_N);
        db.execSQL(SQL_DROP_P);
        Log.d(TAG, "--------------Database Upgrate-------------");
        onCreate(db);
    }
}
