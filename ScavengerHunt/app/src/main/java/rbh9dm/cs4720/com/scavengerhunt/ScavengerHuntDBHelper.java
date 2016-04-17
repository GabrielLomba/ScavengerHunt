package rbh9dm.cs4720.com.scavengerhunt;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;


public class ScavengerHuntDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "ScavengerHuntApp.db";
    public static final String HUNTS_TABLE_NAME = "hunts";
    public static final String HUNTS_COLUMN_NAME = "name";
    public static final String HUNTS_COLUMN_DONE = "done";

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
        return db.rawQuery( "select * from hunts where name="+name+"", null );
    }


    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        return (int) DatabaseUtils.queryNumEntries(db, HUNTS_TABLE_NAME);
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

    public boolean exists (String name)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from hunts where name = ? ", new String[] { name } );
        res.moveToFirst();
        res.close();
        return !res.isAfterLast();
    }

    public void deleteHunt (String name)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("hunts",
                "name = ? ",
                new String[] { name });
        Tab1.huntList.remove(name);
        Tab1.huntsAdapter.notifyDataSetChanged();
    }

    public ArrayList<String> getAllHunts()
    {
        ArrayList<String> array_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from hunts", null );
        res.moveToFirst();

        while(!res.isAfterLast()){
            array_list.add(res.getString(res.getColumnIndex(HUNTS_COLUMN_NAME)));
            res.moveToNext();
        }

        db.close();
        res.close();
        return array_list;
    }
}
