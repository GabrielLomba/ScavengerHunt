package rbh9dm.cs4720.com.scavengerhunt;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Student User on 4/13/2016.
 */
public class MoreInfoDBHelper  extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "MoreInfo.db";
    public static final String ITEMS_TABLE_NAME = "pics";
    public static final String ITEMS_COLUMN_NAMEOFHUNT = "nameOfHunt";
    public static final String ITEMS_COLUMN_NAME = "name";
    public static final String ITEMS_COLUMN_PIC = "pic";

    private HashMap hp;

    public MoreInfoDBHelper(Context context)
    {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table pics " +
                        "(nameOfHunt text, name text, pic blob)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS pics");
        onCreate(db);
    }

    public boolean insertHunt  (String nameOfHunt, String name, Bitmap pic)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ITEMS_COLUMN_NAMEOFHUNT, nameOfHunt);
        contentValues.put(ITEMS_COLUMN_NAME, name);
        byte[] data = getBitmapAsByteArray(pic);
        contentValues.put(ITEMS_COLUMN_PIC, data);
        db.insert(ITEMS_TABLE_NAME, null, contentValues);
        return true;
    }

    public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }

    public Cursor getData(String nameOfHunt){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from pics where nameOfHunt="+nameOfHunt+"", null );
        return res;
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, ITEMS_TABLE_NAME);
        return numRows;
    }

    public boolean updateHunt (String nameOfHunt, String name, Bitmap pic)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ITEMS_COLUMN_NAME, name);
        byte[] data = getBitmapAsByteArray(pic);
        contentValues.put(ITEMS_COLUMN_PIC, data);
        db.update(ITEMS_TABLE_NAME, contentValues, "nameOfHunt = ? ", new String[] { nameOfHunt } );
        return true;
    }

    public Integer deleteHunt (String nameOfHunt, String name)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("pics",
                "id = ? ",
                new String[] { nameOfHunt });
    }


    public Bitmap getImage(String nameOfHunt, String nameTask)
    {
        byte[] data = null;
        Bitmap img = null;

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from pics where nameOfHunt= ? and name = ?", new String[] { nameOfHunt, nameTask } );
        res.moveToFirst();

        if(!res.isAfterLast()) {
            data = res.getBlob(res.getColumnIndex(ITEMS_COLUMN_PIC));
            BitmapFactory.Options options = new BitmapFactory.Options();
            img = BitmapFactory.decodeByteArray(data, 0, data.length, options);
        }
        return img;
    }

}
