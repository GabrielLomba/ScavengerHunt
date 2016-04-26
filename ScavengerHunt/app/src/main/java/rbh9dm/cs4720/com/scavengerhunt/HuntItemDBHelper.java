package rbh9dm.cs4720.com.scavengerhunt;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;


public class HuntItemDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "HuntItem2.db";
    public static final String ITEMS_TABLE_NAME = "items2";
    public static final String ITEMS_COLUMN_NAMEOFHUNT = "nameOfHunt";
    public static final String ITEMS_COLUMN_NAME = "name";
    public static final String ITEMS_COLUMN_DESCRIPTION = "description";
    public static final String ITEMS_COLUMN_PICREQ = "picReq";
    public static final String ITEMS_COLUMN_LOCREQ = "locReq";
    public static final String ITEMS_COLUMN_PICOK = "picOk";
    public static final String ITEMS_COLUMN_LOCOK = "locOk";
    public static final String ITEMS_COLUMN_NAMEOFLOCATION = "nameOfLoc";
    public static final String ITEMS_COLUMN_LATITUDE = "latitude";
    public static final String ITEMS_COLUMN_LONGITUDE = "longitude";
    public static final String ITEMS_COLUMN_COMPLETE = "complete";


    public HuntItemDBHelper(Context context)
    {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table items2 " +
                        "(nameOfHunt text, name text, description text, picReq integer, locReq integer, picOk integer, locOk integer, nameOfLoc text, latitude double, longitude double, complete integer)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS items2");
        onCreate(db);
    }

    public boolean insertHunt  (String nameOfHunt, String name, String description, boolean picReq, boolean locReq, boolean picOk, boolean locOk, String nameOfLocation, double latitude, double longitude, boolean complete)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ITEMS_COLUMN_NAMEOFHUNT, nameOfHunt);
        contentValues.put(ITEMS_COLUMN_NAME, name);
        contentValues.put(ITEMS_COLUMN_DESCRIPTION, description);
        contentValues.put(ITEMS_COLUMN_PICREQ, picReq);
        contentValues.put(ITEMS_COLUMN_LOCREQ, locReq);
        contentValues.put(ITEMS_COLUMN_PICOK, picOk);
        contentValues.put(ITEMS_COLUMN_LOCOK, locOk);
        contentValues.put(ITEMS_COLUMN_NAMEOFLOCATION, nameOfLocation);
        contentValues.put(ITEMS_COLUMN_LATITUDE, latitude);
        contentValues.put(ITEMS_COLUMN_LONGITUDE, longitude);
        contentValues.put(ITEMS_COLUMN_COMPLETE, complete);
        /*
        byte[] data = getBitmapAsByteArray(pic);
        contentValues.put(ITEMS_COLUMN_PIC, data);
        */
        db.insert(ITEMS_TABLE_NAME, null, contentValues);
        return true;
    }


    public Cursor getData(String nameOfHunt){
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery( "select * from items2 where nameOfHunt="+nameOfHunt+"", null );
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        return (int) DatabaseUtils.queryNumEntries(db, ITEMS_TABLE_NAME);
    }

    public boolean updateHunt (String previousName, String newName)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ITEMS_COLUMN_NAMEOFHUNT, newName);

        db.update(ITEMS_TABLE_NAME, contentValues, "nameOfHunt = ? ", new String[] { previousName } );
        return true;
    }

    public boolean updateItem (String nameOfHunt, String previousName, String newName, String newDescription)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ITEMS_COLUMN_NAME, newName);
        contentValues.put(ITEMS_COLUMN_DESCRIPTION, newDescription);

        db.update(ITEMS_TABLE_NAME, contentValues, "nameOfHunt = ? and name = ? ", new String[] { nameOfHunt, previousName } );
        return true;
    }

    public boolean updatePicOk (String nameOfHunt, String name, boolean picOk)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ITEMS_COLUMN_NAMEOFHUNT, nameOfHunt);
        contentValues.put(ITEMS_COLUMN_NAME, name);
        contentValues.put(ITEMS_COLUMN_PICOK, picOk);

        db.update(ITEMS_TABLE_NAME, contentValues, "nameOfHunt = ? and name = ? ", new String[] { nameOfHunt, name } );
        return true;
    }

    public boolean updateLocOk (String nameOfHunt, String name, boolean locOk)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ITEMS_COLUMN_NAMEOFHUNT, nameOfHunt);
        contentValues.put(ITEMS_COLUMN_NAME, name);
        contentValues.put(ITEMS_COLUMN_LOCOK, locOk);

        db.update(ITEMS_TABLE_NAME, contentValues, "nameOfHunt = ? and name = ? ", new String[] { nameOfHunt, name } );
        return true;
    }

    public boolean updateComplete (String nameOfHunt, String name, boolean complete)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ITEMS_COLUMN_NAMEOFHUNT, nameOfHunt);
        contentValues.put(ITEMS_COLUMN_NAME, name);
        contentValues.put(ITEMS_COLUMN_COMPLETE, complete);
        db.update(ITEMS_TABLE_NAME, contentValues, "nameOfHunt = ? and name = ?", new String[] { nameOfHunt, name } );
        return true;
    }

    public Integer deleteHunt (String nameOfHunt)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("items2",
                "nameOfHunt = ?",
                new String[] { nameOfHunt});
    }

    public Integer deleteHunt (String nameOfHunt, String nameOfTask)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("items2",
                "nameOfHunt = ? AND name = ?",
                new String[] { nameOfHunt, nameOfTask });
    }

    public boolean exists (String nameOfHunt, String name)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from items2 where nameOfHunt= ? and name = ?", new String[] { nameOfHunt, name });
        res.moveToFirst();
        if(!res.isAfterLast()){
            res.close();
            return true;
        }
        else{
            res.close();
            return false;
        }
    }

    public ArrayList<LineItem> getAllItems(String nameOfHunt)
    {
        ArrayList<LineItem> array_list = new ArrayList<>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from items2 where nameOfHunt= ? ", new String[] { nameOfHunt } );
        res.moveToFirst();

        while(!res.isAfterLast()){
            array_list.add(new LineItem(res.getString(res.getColumnIndex(ITEMS_COLUMN_NAME)), res.getString(res.getColumnIndex(ITEMS_COLUMN_DESCRIPTION)), res.getInt(res.getColumnIndex(ITEMS_COLUMN_PICREQ)), res.getInt(res.getColumnIndex(ITEMS_COLUMN_LOCREQ)), res.getInt(res.getColumnIndex(ITEMS_COLUMN_PICOK)), res.getInt(res.getColumnIndex(ITEMS_COLUMN_LOCOK)), res.getString(res.getColumnIndex(ITEMS_COLUMN_NAMEOFLOCATION)), res.getFloat(res.getColumnIndex(ITEMS_COLUMN_LATITUDE)), res.getFloat(res.getColumnIndex(ITEMS_COLUMN_LONGITUDE)), res.getInt(res.getColumnIndex(ITEMS_COLUMN_COMPLETE))));
            res.moveToNext();
        }

        db.close();
        res.close();

        return array_list;
    }

}
