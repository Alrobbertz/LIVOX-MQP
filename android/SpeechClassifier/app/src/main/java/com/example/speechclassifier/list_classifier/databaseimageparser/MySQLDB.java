package com.example.speechclassifier.list_classifier.databaseimageparser;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;
import java.lang.Double;
import com.example.speechclassifier.R;
import com.example.speechclassifier.list_classifier.utils.ResourceLoader;

/**
 * MySQlDB class handles creation of local SQLite database loaded from source files.
 *
 * @author WPI Team 2019-2020
 * @since 02-10-2020
 */
public class MySQLDB extends SQLiteOpenHelper {

    public static final String TAG = "MySQLDB";
    public static final String DATABASE_NAME = "imagelabels.db";
    Context context;

    public MySQLDB(Context context) {
        super(context, DATABASE_NAME, null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.createTableStatements(db);
        this.loadTableValues(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        this.dropTable(db,"Images");
        this.dropTable(db,"Labels");
        this.dropTable(db, "Tags");
        onCreate(db);

    }

    /**
     * loads database values, (used in cases, where onCreate did not initiate properly
     * @param db database to reinitialize
     */
    public void loadValues(SQLiteDatabase db) {
        Log.d(TAG, "onOpen Called");
        this.dropTable(db,"Images");
        this.dropTable(db,"Labels");
        this.dropTable(db, "Tags");
        onCreate(db);
    }

    /**
     * checks if the Labels, Tags and Images tables have been created to authenticate if
     * database was created
     * @param db database to check
     * @return True if tables exist and False if tables do not exist
     */
    public boolean dataLoaded(SQLiteDatabase db) {
        ArrayList<String> arrTblNames = new ArrayList<String>();
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        if (c.moveToFirst()) {
            while ( !c.isAfterLast() ) {
                arrTblNames.add( c.getString( c.getColumnIndex("name")) );
                c.moveToNext();
            }
        }


        Log.d(TAG, arrTblNames.toString());
        return arrTblNames.size() == 4;
    }

    /**
     * creates Images, Labels and Tags tables in the database
     * @param db database to create tables
     */
    private void createTableStatements(SQLiteDatabase db) {
        db.execSQL(ResourceLoader.loadSQL(R.raw.images, context));
        db.execSQL(ResourceLoader.loadSQL(R.raw.labels, context));
        db.execSQL(ResourceLoader.loadSQL(R.raw.tags, context));

    }

    /**
     * loads all data into database tables
     * @param db database to load data into
     */
    private void loadTableValues(SQLiteDatabase db) {
        loadImageValues(db);
        loadTagValues(db);
        loadLabelValues(db);
    }

    /**
     * load all of the Image table data
     * @param db database to load data into
     */
    private void loadImageValues(SQLiteDatabase db) {
        db.beginTransaction();
        int resourceID = R.raw.image_data;
        List<List<String>> values = ResourceLoader.loadTSVList(resourceID, context);
        for(List<String> row: values) {
            insertImage(row.get(0), row.get(1), db);
        }
        db.setTransactionSuccessful();
        db.endTransaction();

    }

    /**
     * load all of the Label table data
     * @param db database to load data into
     */
    private void loadLabelValues(SQLiteDatabase db) {
        db.beginTransaction();
        int resourceID = R.raw.label_data;
        List<List<String>> values = ResourceLoader.loadTSVList(resourceID, context);
        for(List<String> row: values) {
            insertLabel(row.get(0), row.get(1), Double.valueOf(row.get(2)), db);
        }
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    /**
     * load all of the Tag table data
     * @param db database to load data into
     */
    private void loadTagValues(SQLiteDatabase db) {
        db.beginTransaction();
        int resourceID = R.raw.tag_data;
        List<List<String>> values = ResourceLoader.loadTSVList(resourceID, context);
        for(List<String> row: values) {
            insertTag(row.get(0), row.get(1), db);
        }
        db.setTransactionSuccessful();
        db.endTransaction();
    }


    /**
     * insert row into Image table
     * @param image_id image_id column data of table a UUID
     * @param location location column data of table a filepath
     * @param db database to load row into
     */
    private void insertImage(String image_id, String location, SQLiteDatabase db) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("image_id", image_id);
        contentValues.put("location", location);
        long id = db.insert("images", null, contentValues);
        Log.d(TAG, Long.toString(id));
    }

    /**
     * insert row into Label table
     * @param image_id image_id column data of table a UUID
     * @param label label of image created from Google Image Labels
     * @param confidence confidence of label on image
     * @param db database to load row into
     */
    private void insertLabel(String image_id, String label, double confidence, SQLiteDatabase db) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("image_id", image_id);
        contentValues.put("label", label);
        contentValues.put("confidence", confidence);
        long id = db.insert("labels", null, contentValues);
        Log.d(TAG, Long.toString(id));
    }

    /**
     * insert row into Tag table
     * @param image_id image_id column data of table a UUID
     * @param tag tag of image from Livox image tags
     * @param db database to load row into
     */
    private void insertTag(String image_id, String tag, SQLiteDatabase db) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("image_id", image_id);
        contentValues.put("tag", tag);
        long id = db.insert("tags", null, contentValues);
        Log.d(TAG, Long.toString(id));
    }

    /**
     * helper function to drop a table
     * @param db database to drop table
     * @param tableName table to drop
     */
    private void dropTable(SQLiteDatabase db, String tableName) {
        String stmt =  "DROP TABLE IF EXISTS " + tableName;
        db.execSQL(stmt);
    }
}
