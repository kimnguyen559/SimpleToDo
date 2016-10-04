
package edu.scu.klnguyen.simpletodo.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import edu.scu.klnguyen.simpletodo.Model.Item;

/**
 * Created by Kim Nguyen on 9/24/2016.
 */
public class DBHelper extends SQLiteOpenHelper {
    // Database Info
    private static final String DATABASE_NAME = "simpleToDo";
    private static final int DATABASE_VERSION = 1;

    // Table Info
    private static final String TABLE_NAME = "tasks";
    private static final String ID = "_id";
    private static final String NAME = "name";
    private static final String DUE_DATE = "date";
    private static final String NOTES = "notes";
    private static final String PRIORITY = "priority";
    private static final String STATUS = "status";

    //SQL strings
    static private final String SQL_CREATE_TABLE =
                                    "CREATE TABLE " + TABLE_NAME + "(" +
                                    ID+" INTEGER PRIMARY KEY AUTOINCREMENT," +
                                    NAME+ " TEXT," +
                                    DUE_DATE+" INTEGER," +
                                    NOTES+" TEXT," +
                                    PRIORITY+" INTEGER," +
                                    STATUS+" INTEGER);";

    static private final String SQL_DROP_TABLE = "DROP TABLE "+ TABLE_NAME+ ";";

    Context context;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);     // we use default cursor factory (null, 3rd arg)
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            db.execSQL(SQL_DROP_TABLE);
            db.execSQL(SQL_CREATE_TABLE);
        }
    }

    // Add a record to the DB
    public void addItem(Item item) {
        SQLiteDatabase db = this.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put(NAME, item.getName());
        values.put(DUE_DATE, item.getDueDate().getTime());
        values.put(NOTES, item.getNotes());
        values.put(PRIORITY, item.getPriority());
        values.put(STATUS, item.getStatus());

        db.insert(TABLE_NAME, null, values);
     }

    // Update a record in the DB
    public void updateItem (Item item) {
        try {
            String[] whereArgs = { ""+item.getId() };               // get record's ID

            ContentValues updValues = new ContentValues();
            updValues.put(NAME, item.getName());
            updValues.put(DUE_DATE, item.getDueDate().getTime());
            updValues.put(NOTES, item.getNotes());
            updValues.put(PRIORITY, item.getPriority());
            updValues.put(STATUS, item.getStatus());

            SQLiteDatabase db = getWritableDatabase();
            db.update(TABLE_NAME, updValues,ID+"=?", whereArgs);    // update the record with matched ID

        } catch (Exception e) {

        }
    }

    // Delete a record with matched ID
    public void deleteItem(Item item) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete(TABLE_NAME, ID+"=?", new String[]{String.valueOf(item.getId())});
    }

    // Get all records in the database
    public List<Item> getAllItems() {
        List<Item> items = new ArrayList<>();

        String SELECT_QUERY ="SELECT * FROM "+ TABLE_NAME+ ";";

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {                                          // Read each record
                do {
                    String name = cursor.getString(cursor.getColumnIndex(NAME));
                    Date dueDate = new Date(cursor.getLong(cursor.getColumnIndex(DUE_DATE)));
                    String notes = cursor.getString(cursor.getColumnIndex(NOTES));
                    int priority = cursor.getInt(cursor.getColumnIndex(PRIORITY));
                    int status = cursor.getInt(cursor.getColumnIndex(STATUS));
                    int id = cursor.getInt(cursor.getColumnIndex(ID));

                    Item item = new Item(name, dueDate, notes, priority, status);   // create an item with data
                    item.setId(id);
                    items.add(item);            // add new item to list
                } while(cursor.moveToNext());
            }
        }
        catch (Exception e) {
            //Log.d(TAG, "Error while trying to get posts from database");
        }
        finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return items;                           // return list of item
    }

    private void toast(String msg){
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}
