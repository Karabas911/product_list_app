package com.example.karabushka.productlist;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class RecyclerFragment extends Fragment{
    public static final String FRAGMENT_TAG = "fragment1";
    public static final String VOVKA = "Vovka";
    public static final String VICTOR = "Viktor";
    public static final String SEROZHKA = "Serozhka";
    private SQLiteDatabase myDatabase;
    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(FRAGMENT_TAG,"---------Fragment onCreate-----------");

        myDatabase = new DatabaseHelper(getActivity()).getWritableDatabase();
        if(!getNeighbor().moveToNext()){
            myDatabase.execSQL("DElETE FROM "+ DatabaseContract.NeighborList.TABLE_NAME);
//            myDatabase.execSQL("DElETE FROM "+ DatabaseContract.ProductList.TABLE_NAME);
            addNeighborsToTable();}

        new LoadAllProductAsyncTask().execute();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.recycler_layout,container,false);
        Log.d(FRAGMENT_TAG,"---------Fragment onCreateView-----------");

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerID);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                long id = (long) viewHolder.itemView.getTag();
                int k = myDatabase.delete(DatabaseContract.ProductList.TABLE_NAME, DatabaseContract.ProductList._ID+" = "+id,null);
                new LoadAllProductAsyncTask().execute();
                Log.d(FRAGMENT_TAG,"removing " + k);
            }
        }).attachToRecyclerView(mRecyclerView);
        return view;
    }

    public void addProductToList(String product, String number, String neighborName) {
        ContentValues cv = new ContentValues();
        cv.put(DatabaseContract.ProductList.PRODUCT_NAME,product);
        cv.put(DatabaseContract.ProductList.NUMBER,number);
        cv.put(DatabaseContract.ProductList.NEIGHBOR_ID,findNeighborID(neighborName));
        myDatabase.insert(DatabaseContract.ProductList.TABLE_NAME,null,cv);
        new LoadAllProductAsyncTask().execute();
        Toast toast = Toast.makeText(getActivity(),"The Product is added",Toast.LENGTH_LONG);
        toast.show();
    }
     public void removeAllProducts(){
         myDatabase.execSQL("DElETE FROM "+ DatabaseContract.ProductList.TABLE_NAME);
         new LoadAllProductAsyncTask().execute();
         Toast.makeText(getActivity(),"List cleaned",Toast.LENGTH_SHORT).show();
     }

    public void addNeighborsToTable(){

        ContentValues cv = new ContentValues();
        cv.put(DatabaseContract.NeighborList.NEIGHBORS_NAME,VOVKA);
        cv.put(DatabaseContract.NeighborList.ID,1);
        myDatabase.insert(DatabaseContract.NeighborList.TABLE_NAME,null,cv);

        cv = new ContentValues();
        cv.put(DatabaseContract.NeighborList.NEIGHBORS_NAME,VICTOR);
        cv.put(DatabaseContract.NeighborList.ID,2);
        myDatabase.insert(DatabaseContract.NeighborList.TABLE_NAME,null,cv);

        cv = new ContentValues();
        cv.put(DatabaseContract.NeighborList.NEIGHBORS_NAME,SEROZHKA);
        cv.put(DatabaseContract.NeighborList.ID,3);
        myDatabase.insert(DatabaseContract.NeighborList.TABLE_NAME,null,cv);

        Log.d(FRAGMENT_TAG,"----------Neighbors is added---------");

    }
//  public void showAllProducts(View view){
//      Log.d(TAG, "========All products========");
//
//      Cursor cursor = getAllProducts();
//
//      while(cursor.moveToNext()){
//          String productName = cursor.getString(cursor.getColumnIndex(ProductList.PRODUCT_NAME));
//          int number = cursor.getInt(cursor.getColumnIndex(ProductList.NUMBER));
//          String date = cursor.getString(cursor.getColumnIndex(ProductList.DATE));
//          String neighborName = cursor.getString(cursor.getColumnIndex(NeighborList.NEIGHBORS_NAME));
//
//          Log.d(TAG,number+" "+productName+" was bought on "+date+ " by "+neighborName);
//      }
//  }
//  public void showNeighbors(View view){
//      Log.d(TAG,"===========All neighbors============");
//      Cursor cursor = myDatabase.query(NeighborList.TABLE_NAME,
//              null,
//              null,
//              null,
//              null,
//              null,
//              NeighborList.ID);
//      while(cursor.moveToNext()){
//      String name = cursor.getString(cursor.getColumnIndex(NeighborList.NEIGHBORS_NAME));
//      int id = cursor.getInt(cursor.getColumnIndex(NeighborList.ID));
//          Log.d(TAG,id+" "+name);
//      }
//  }

    public Cursor getNeighbor(String name){
        String whereClause = DatabaseContract.NeighborList.NEIGHBORS_NAME+" = ?";
        String [] nameArgs = {name};
        Cursor cursor = myDatabase.query(DatabaseContract.NeighborList.TABLE_NAME,
                null,
                whereClause,
                nameArgs,
                null,
                null,
                DatabaseContract.NeighborList.ID);
        return cursor;
    }
    public Cursor getNeighbor(){
        Cursor cursor = myDatabase.query(DatabaseContract.NeighborList.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                DatabaseContract.NeighborList.ID);
        return cursor;
    }

    public int findNeighborID(String neighborName){
        Cursor cursor = getNeighbor(neighborName);
        cursor.moveToNext();
        return cursor.getInt(cursor.getColumnIndex(DatabaseContract.NeighborList.ID));
    }

    private class LoadAllProductAsyncTask extends AsyncTask<Void,Void,Cursor> {

        @Override
        protected Cursor doInBackground(Void... params) {
            Log.d(FRAGMENT_TAG,"---------background task------------");
            String SQL_INNER_JOIN = DatabaseContract.ProductList.TABLE_NAME+" as PR inner join "+
                    DatabaseContract.NeighborList.TABLE_NAME+" as NE on PR."+ DatabaseContract.ProductList.NEIGHBOR_ID+
                    " = NE."+ DatabaseContract.NeighborList.ID;
            String SQL_COLUMS []= {"PR."+ DatabaseContract.ProductList._ID,
                    "PR."+ DatabaseContract.ProductList.PRODUCT_NAME,
                    "PR."+ DatabaseContract.ProductList.NUMBER,
                    "PR."+ DatabaseContract.ProductList.DATE,
                    "NE."+ DatabaseContract.NeighborList.NEIGHBORS_NAME };
            Cursor cursor = myDatabase.query(SQL_INNER_JOIN,
                    SQL_COLUMS,
                    null,
                    null,
                    null,
                    null,
                    DatabaseContract.ProductList._ID);
            return cursor;
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            if(mAdapter ==null){
                mAdapter = new MyAdapter(cursor);
                mRecyclerView.setAdapter(mAdapter);
            }
            else mAdapter.swapCursor(cursor);
        }
    }
}
