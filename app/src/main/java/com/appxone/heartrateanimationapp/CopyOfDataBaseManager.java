package com.appxone.heartrateanimationapp;

/**
 * DataBase Management Class
 *
 * @author Nadeem Iqbal
 * @author Nadeem Iqbal
 * @author Nadeem Iqbal
 * @author Nadeem Iqbal
 */

/**
 * @author Nadeem Iqbal
 *
 */

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class CopyOfDataBaseManager {


    public static final String DB_Path = "/data/data/com.appxone.heartrateanimationapp/databases/";

    public static final String DATABASE_NAME = "heartrate";
    public static final String TABLE_CATEGORY = "Category";
    public static final String TABLE_SUBCATEGORY = "Subcategory";
    public static final String TABLE_VERSION = "Version";
    public static final String TABLE_FILENAMES = "Filenames";

    public static final int DATABASE_VERSION = 1;

    public static String KEY_ID = "id";
    public static String KEY_CAT_ID = "cat_id";
    public static String KEY_CAT_NAME = "cat_name";
    public static String KEY_CAT_URL = "cat_url";
    public static String KEY_FILENAME = "filename";
    public static String KEY_VERSION = "version";
    public static String KEY_SUB_ID = "sub_id";
    public static String KEY_SUB_NAME = "sub_name";
    public static String KEY_SUB_DEF = "sub_def";
    public static String KEY_SUB_URL = "sub_url";
    public static String KEY_SUB_DESC = "sub_desc";
    public static String KEY_SUB_ISFAV = "sub_isfav";
    public static String KEY_VER_ID = "ver_id";
    public static String KEY_VER_NAME = "ver_name";

    private static final String TAG = "DBAdapter";

    private static Context context = null;

    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;
    private String version_name = "";

    public CopyOfDataBaseManager(Context ctx) {
        context = ctx;
        DBHelper = new DatabaseHelper(context);
    }

    //    private static final String CREATE_TABLE_CATEGORY = "CREATE TABLE IF NOT EXISTS \"bookmarks\" (\"id\" INTEGER PRIMARY KEY NOT NULL , \"video_name\" TEXT, \"video_url\" TEXT, \"video_thumbnail\" INTEGER, \"video_duration\" TEXT)";
//    private static final String CREATE_TABLE_CATEGORY = "CREATE TABLE IF NOT EXISTS \"videoslist_table\" (\"video_id\" INTEGER PRIMARY KEY NOT NULL , \"video_name\" TEXT, \"video_url\" TEXT, \"video_image\" INTEGER, \"video_duration\" TEXT, \"video_type\" TEXT, \"video_bookmark\" TEXT, \"video_url_480\" TEXT, \"video_url_720\" TEXT, \"video_url_1440\" TEXT)";
    private static final String CREATE_TABLE_CATEGORY = "CREATE TABLE IF NOT EXISTS \"records\" (\"id\" INTEGER PRIMARY KEY NOT NULL , \"heartrate\" TEXT, \"datetime\" TEXT)";

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db = context.openOrCreateDatabase(DATABASE_NAME, 0, null);
            db.execSQL(CREATE_TABLE_CATEGORY);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.e(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
        }
    }

    // ---opens the database---
    public CopyOfDataBaseManager open() throws SQLException {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    // ---closes the database---
    public void close() {
        DBHelper.close();
    }

    // ---retrieve records---
    public Cursor selectQuery(String query) throws SQLException {
        String myPath = DB_Path + DATABASE_NAME;
        db = SQLiteDatabase.openDatabase(myPath, null,
                SQLiteDatabase.OPEN_READWRITE);
//		db = SQLiteDatabase.openOrCreateDatabase(myPath, null);// (myPath, null,
        // SQLiteDatabase.OPEN_READWRITE);
        Cursor mCursor = db.rawQuery(query, null);
        mCursor.moveToFirst();
        db.close();

        return mCursor;
    }

//	public void delete(String query) throws SQLException {
//		String myPath = DB_Path + DATABASE_NAME;
//		SQLiteDatabase myData = SQLiteDatabase.openDatabase(myPath, null,
//				SQLiteDatabase.OPEN_READWRITE);
//		myData.execSQL(query);
//		myData.close();
//	}

    public void delete(String query) throws SQLException {
        String myPath = DB_Path + DATABASE_NAME;
        db = SQLiteDatabase.openDatabase(myPath, null,
                SQLiteDatabase.OPEN_READWRITE);
        db.execSQL(query);
        db.close();
    }

    public Cursor getDistinctCounts(String Table, String col) {
        String myPath = DB_Path + DATABASE_NAME;
        db = SQLiteDatabase.openDatabase(myPath, null,
                SQLiteDatabase.OPEN_READWRITE);
        String query = "SELECT " + col + " , count(" + col + ") from " + Table
                + " group by " + col;
        return db.rawQuery(query, null);
    }

    // //////// For Insert And Update Data ////////
    public void insert_update(String query) throws SQLException {
        String myPath = DB_Path + DATABASE_NAME;
        db = SQLiteDatabase.openDatabase(myPath, null,
                SQLiteDatabase.OPEN_READWRITE);
        db.execSQL(query);
        db.close();
    }

    /**
     * Creates a empty database on the system and rewrites it with your own
     * database.
     * */
    public void createDataBase() throws IOException {

        boolean dbExist = checkDataBase();
        if (dbExist) {
            // do nothing - database already exist
        } else {
            CopyFiles();
        }
    }

    private void CopyFiles() {
        try {
            InputStream is = context.getAssets().open(DATABASE_NAME);
            File outfile = new File(DB_Path, DATABASE_NAME);
            outfile.getParentFile().mkdirs();
            outfile.createNewFile();

            if (is == null)
                throw new RuntimeException("stream is null");
            else {
                FileOutputStream out = new FileOutputStream(outfile);
                byte buf[] = new byte[128];
                do {
                    int numread = is.read(buf);
                    if (numread <= 0)
                        break;
                    out.write(buf, 0, numread);
                } while (true);

                is.close();
                out.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Check if the database already exist to avoid re-copying the file each
     * time you open the application.
     *
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase() {

        SQLiteDatabase checkDB = null;

        try {
            String myPath = DB_Path + DATABASE_NAME;

            Log.e("Checking DB Path", "DB Path : " + myPath);

            checkDB = SQLiteDatabase.openDatabase(myPath, null,
                    SQLiteDatabase.OPEN_READWRITE);

        } catch (SQLiteException e) {
            e.printStackTrace();
        }

        if (checkDB != null) {
            checkDB.close();
        }

        return checkDB != null ? true : false;
    }

    public void deleteRows(String Number) {
        // String myPath = DB_Path
        // + DATABASE_NAME;
        // db = SQLiteDatabase.openDatabase(myPath, null,
        // SQLiteDatabase.OPEN_READWRITE);
        // db.delete( DATABASE_TABLE_2,
        // KEY_NUMBER + "=?", new String[] { Number });
    }
}
