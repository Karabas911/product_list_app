package com.example.karabushka.productlist;


import android.provider.BaseColumns;

public class DatabaseContract {
public static final String DATABASE_NAME = "product_db";

    public static final class NeighborList implements BaseColumns {
        public static final String TABLE_NAME= "Neighbors";
        public static final String NEIGHBORS_NAME = "name";
        public static final String ID = "id";

    }

    public static final class ProductList implements BaseColumns{
        public static final String TABLE_NAME = "Products";
        public static final String NEIGHBOR_ID = "neighbor_id";
        public static final String PRODUCT_NAME = "product_name";
        public static final String NUMBER = "number";
        public static final String DATE = "date";

    }
}
