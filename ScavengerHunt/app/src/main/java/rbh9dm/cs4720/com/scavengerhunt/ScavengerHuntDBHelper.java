package rbh9dm.cs4720.com.scavengerhunt;

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
public class ScavengerHuntDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "ScavengerHuntApp.db";
    public static final String HUNTS_TABLE_NAME = "hunts";
    public static final String HUNTS_COLUMN_NAME = "name";
    public static final String HUNTS_COLUMN_DONE = "done";
    private HashMap hp;

    public ScavengerHuntDBHelper(Context context)
    {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table hunts " +
                        "(name text primary key, done boolean)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS hunts");
        onCreate(db);
    }

    public boolean insertHunt  (String name, boolean done)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(HUNTS_COLUMN_NAME, name);
        contentValues.put(HUNTS_COLUMN_DONE, done);
        db.insert(HUNTS_TABLE_NAME, null, contentValues);
        return true;
    }

    public Cursor getData(String name){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from hunts where name="+name+"", null );
        return res;
    }


    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, HUNTS_TABLE_NAME);
        return numRows;
    }

    public boolean updateHunt (String name, boolean done)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(HUNTS_COLUMN_NAME, name);
        contentValues.put(HUNTS_COLUMN_DONE, done);
        db.update(HUNTS_TABLE_NAME, contentValues, "name = ? ", new String[] { name } );
        return true;
    }

    public Integer deleteHunt (String name)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("hunts",
                "name = ? ",
                new String[] { name });
    }

    public ArrayList<ScavengerHunt> getAllHunts()
    {
        ArrayList<ScavengerHunt> array_list = new ArrayList<ScavengerHunt>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from hunts", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(new ScavengerHunt(res.getString(res.getColumnIndex(HUNTS_COLUMN_NAME))));
            res.moveToNext();
        }
        return array_list;
    }
}
