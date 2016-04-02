package rbh9dm.cs4720.com.scavengerhunt;

/**
 * Created by Student User on 3/30/2016.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Student User on 3/28/2016.
 */
public class HuntItemDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "HuntItem.db";
    public static final String ITEMS_TABLE_NAME = "items";
    public static final String ITEMS_COLUMN_NAMEOFHUNT = "nameOfHunt";
    public static final String ITEMS_COLUMN_NAME = "name";
    public static final String ITEMS_COLUMN_DESCRIPTION = "description";
    public static final String ITEMS_COLUMN_PICREQ = "picReq";
    public static final String ITEMS_COLUMN_LOCREQ = "locReq";
    private HashMap hp;

    public HuntItemDBHelper(Context context)
    {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table items " +
                        "(nameOfHunt text, name text, description text, picReq integer, locReq integer)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS items");
        onCreate(db);
    }

    public boolean insertHunt  (String nameOfHunt, String name, String description, boolean picReq, boolean locReq)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ITEMS_COLUMN_NAMEOFHUNT, nameOfHunt);
        contentValues.put(ITEMS_COLUMN_NAME, name);
        contentValues.put(ITEMS_COLUMN_DESCRIPTION, description);
        contentValues.put(ITEMS_COLUMN_PICREQ, picReq);
        contentValues.put(ITEMS_COLUMN_LOCREQ, locReq);
        db.insert(ITEMS_TABLE_NAME, null, contentValues);
        return true;
    }

    public Cursor getData(String nameOfHunt){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from items where nameOfHunt="+nameOfHunt+"", null );
        return res;
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, ITEMS_TABLE_NAME);
        return numRows;
    }

    public boolean updateHunt (String nameOfHunt, String name, String description, boolean picReq, boolean locReq)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ITEMS_COLUMN_NAME, name);
        contentValues.put(ITEMS_COLUMN_DESCRIPTION, description);
        contentValues.put(ITEMS_COLUMN_PICREQ, picReq);
        contentValues.put(ITEMS_COLUMN_LOCREQ, locReq);
        db.update(ITEMS_TABLE_NAME, contentValues, "nameOfHunt = ? ", new String[] { nameOfHunt } );
        return true;
    }

    public Integer deleteHunt (String nameOfHunt, String name)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("items",
                "id = ? ",
                new String[] { nameOfHunt });
    }

    public ArrayList<LineItem> getAllItems(String nameOfHunt)
    {
        ArrayList<LineItem> array_list = new ArrayList<LineItem>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from items where nameOfHunt=\'"+nameOfHunt+"\'", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(new LineItem(res.getString(res.getColumnIndex(ITEMS_COLUMN_NAME)), res.getString(res.getColumnIndex(ITEMS_COLUMN_DESCRIPTION)), res.getInt(res.getColumnIndex(ITEMS_COLUMN_PICREQ)), res.getInt(res.getColumnIndex(ITEMS_COLUMN_LOCREQ))));
            res.moveToNext();
        }
        return array_list;
    }
}
