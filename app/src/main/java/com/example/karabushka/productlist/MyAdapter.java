package com.example.karabushka.productlist;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import static com.example.karabushka.productlist.DatabaseContract.NeighborList;
import static com.example.karabushka.productlist.DatabaseContract.ProductList;


public class MyAdapter extends Adapter<MyAdapter.MyViewHolder>{
    private Cursor mCursor;

    public MyAdapter(Cursor cursor){
        this.mCursor = cursor;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout,parent,false);
        MyViewHolder myHolder = new MyViewHolder(v);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        if (!mCursor.moveToPosition(position))
            return;

        String productName = mCursor.getString(mCursor.getColumnIndex(ProductList.PRODUCT_NAME));
        int productNumber = mCursor.getInt(mCursor.getColumnIndex(ProductList.NUMBER));
        String date = mCursor.getString(mCursor.getColumnIndex(ProductList.DATE));
        String neighborName = mCursor.getString(mCursor.getColumnIndex(NeighborList.NEIGHBORS_NAME));
        long id = mCursor.getLong(mCursor.getColumnIndex(ProductList._ID));

        holder.productName.setText(productName);
        holder.productNumber.setText(String.valueOf(productNumber));
        holder.date.setText(date+", bought by "+neighborName);
        holder.itemView.setTag(id);

    }

    public void swapCursor(Cursor cursor){
        if (this.mCursor !=null) this.mCursor.close();
            this.mCursor = cursor;
        if(cursor!=null) this.notifyDataSetChanged();

    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    public static final class MyViewHolder extends RecyclerView.ViewHolder{
        TextView productName;
        TextView productNumber;
        TextView date;
        public MyViewHolder(View itemView) {
            super(itemView);
            productName = (TextView) itemView.findViewById(R.id.list_item);
            productNumber = (TextView) itemView.findViewById(R.id.item_number);
            date = (TextView) itemView.findViewById(R.id.date_item);
        }
    }


}
