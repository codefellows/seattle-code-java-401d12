package com.zork.class27demo.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.zork.class27demo.dao.ProductDao;
import com.zork.class27demo.models.Product;


@Database(entities = {Product.class}, version = 1) // if we update the version, it will delete the db!
@TypeConverters({TaskMasterDatabaseConverters.class})
public abstract class TaskMasterDatabase extends RoomDatabase {
    public abstract ProductDao productDao();
}
