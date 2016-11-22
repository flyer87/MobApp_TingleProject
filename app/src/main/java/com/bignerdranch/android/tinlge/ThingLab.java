package com.bignerdranch.android.tinlge;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bignerdranch.android.tinlge.database.ThingBaseHelper;
import com.bignerdranch.android.tinlge.database.ThingCursorWrapper;

import java.util.ArrayList;
import java.util.List;

import static com.bignerdranch.android.tinlge.database.ThingDbSchema.*;

/**
 * Created by Omer on 17.02.2016.
 */
public class ThingLab {
    private static ThingLab sThingLab;

    private Context mContext;
    private static SQLiteDatabase mDatabase;

    private ThingLab(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new ThingBaseHelper(mContext)
                .getWritableDatabase(); // creating a new database file if it does not already exist.

        //mThingsDB= new ArrayList<Thing>();

        /*mThingsDB.add(new Thing("Android Phone", "Desk"));
        mThingsDB.add(new Thing("Jacket", "Wardrobe"));
        mThingsDB.add(new Thing("Laptop", "School bag"));
        mThingsDB.add(new Thing("Big Nerd book", "Desk"));
        mThingsDB.add(new Thing("thing1", "place1"));
        mThingsDB.add(new Thing("thing2", "place2"));
        mThingsDB.add(new Thing("thing3", "place3"));
        mThingsDB.add(new Thing("thing4", "place4"));
        mThingsDB.add(new Thing("thing5", "place5"));
        mThingsDB.add(new Thing("thing6", "place6"));
        mThingsDB.add(new Thing("thing7", "place7"));
        mThingsDB.add(new Thing("thing8", "place8"));
        mThingsDB.add(new Thing("thing9", "place9"));
        mThingsDB.add(new Thing("thing10", "place10"));*/

    }

    public static ThingLab get(Context context) {
        if (sThingLab == null){
            sThingLab = new ThingLab(context);
        }

        return sThingLab;
    }

     public List<Thing> getThings(){
        List<Thing> things = new ArrayList<Thing>();
        ThingCursorWrapper cursor = queryThings(null, null);

        try{
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                things.add(cursor.getThing());
                cursor.moveToNext();
            }
        }
        finally {
            cursor.close();
        }

        return  things;
    }

    public List<Thing> searchThingsByNameAndOrder(String name, String orderClause){

        List<Thing> things = new ArrayList<Thing>();
        String whereClause = "lower("+ThingTable.Cols.WHAT + ") LIKE ?";
        String[] whereArgs = new String[] {"%"+  name.toLowerCase() + "%" };
        //Log.i("searchQuery", whereClause + " ->" + whereArgs[0] );

        ThingCursorWrapper cursor = queryThingsAndOrder(whereClause, whereArgs, orderClause);
        try{
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                things.add(cursor.getThing());
                cursor.moveToNext();
            }
        }
        finally {
            cursor.close();
        }

        return  things;
    }

    public List<Thing> searchThingsByName(String name){
        List<Thing> things = new ArrayList<Thing>();
        String whereClause = "lower("+ThingTable.Cols.WHAT + ") LIKE ?";
        String[] whereArgs = new String[] {"%"+  name.toLowerCase() + "%" };
        //Log.i("searchQuery", whereClause + " ->" + whereArgs[0] );

        ThingCursorWrapper cursor = queryThings(whereClause, whereArgs);
        try{
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                things.add(cursor.getThing());
                cursor.moveToNext();
            }
        }
        finally {
            cursor.close();
        }

        return  things;
    }


    public int size(){
        //return  this.mThingsDB.size();
        //TODO - get the size
        return 0;
    }

    public void removeAtPos(int pos, String whatToDelete, String whereToDelete) {
        String rawQuery = "Select " + ThingTable.Cols.ID +", " + ThingTable.Cols.WHAT +", " + ThingTable.Cols.WHERE +
                " from " + ThingTable.NAME +" limit 1 offset " + pos;

        Cursor cursor = mDatabase.rawQuery(rawQuery, null);
        if (cursor.getCount() > 0) {
            String id, what, where;
            try {
                cursor.moveToFirst();
                id = cursor.getString(cursor.getColumnIndex(ThingTable.Cols.ID));
                what = cursor.getString(cursor.getColumnIndex(ThingTable.Cols.WHAT));
                where = cursor.getString(cursor.getColumnIndex(ThingTable.Cols.WHERE));
            } finally {
                cursor.close();
            }

            if (what.equals(whatToDelete) && where.equals(whereToDelete)) {
                mDatabase.delete(ThingTable.NAME,
                        ThingTable.Cols.ID + " = ?",
                        new String[]{id});
            }
        }
    }

    public void addThing(Thing newThing){
        ContentValues values = getContentValues(newThing);
        mDatabase.insert(ThingTable.NAME, null, values);
    }

    public Thing getThing(Integer id){
        //return mThingsDB.get(i);
        // TODO - get thing by "i"

        ThingCursorWrapper cursor = queryThings(
                ThingTable.Cols.ID + " = ?",
                new String[]{id.toString()}
        );

        try {
            if(cursor.getCount() == 0){ return  null;}

            cursor.moveToFirst();
            return cursor.getThing();
        }
        finally {
            cursor.close();
        }
    }

    public static Thing getLastThing(){
        ThingCursorWrapper cursor = queryThings(
                  "_id DESC"
        );

        try {
            if(cursor.getCount() == 0){ return null;}

            cursor.moveToFirst();
            return cursor.getThing();
        }
        finally {
            cursor.close();
        }
    }

    public void updateThing(Thing thing) {
        String id =  thing.getId().toString();
        ContentValues values = getContentValues(thing);

        mDatabase.update(ThingTable.NAME, values,
                ThingTable.Cols.ID + " = ?",
                new String[] { id });
    }

    /*public void deleteThingById(Integer id){
        //String id =  thing.getId().toString();
        //ContentValues values = getContentValues(thing);

        mDatabase.delete(ThingTable.NAME,
                ThingTable.Cols.ID + " = ?",
                new String[]{Integer.toString(id)});
    }*/

    private static ContentValues getContentValues(Thing thing) {
        ContentValues values = new ContentValues();

        //values.put(ThingTable.Cols.ID, thing.getId().toString());
        values.put(ThingTable.Cols.WHAT, thing.getmWhat().toString());
        values.put(ThingTable.Cols.WHERE, thing.getmWhere().toString());
        values.put(ThingTable.Cols.IMPORTANT, thing.getImportant().toString());

        return values;
    }

        /*

    mDb.query(true, DATABASE_NAMES_TABLE, new String[] { KEY_ROWID,
            KEY_NAME }, KEY_NAME + " LIKE ?",
            new String[] {"%"+ filter+ "%" }, null, null, null,
            null);

    * */

    private static ThingCursorWrapper queryThings(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                ThingTable.NAME,
                null, // Columns - null selects all columns
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                null // orderBy
        );

        return new ThingCursorWrapper(cursor);
    }

    private static ThingCursorWrapper queryThingsAndOrder(String whereClause, String[] whereArgs, String orderClause) {
        Cursor cursor = mDatabase.query(
                ThingTable.NAME,
                null, // Columns - null selects all columns
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                orderClause // orderBy
        );

        return new ThingCursorWrapper(cursor);
    }

    private static ThingCursorWrapper queryThings(String orderClause) {
        Cursor cursor = mDatabase.query(
                ThingTable.NAME,
                null, // Columns - null selects all columns
                null,
                null,
                null, // groupBy
                null, // having
                orderClause // orderBy
        );

        return new ThingCursorWrapper(cursor);
    }
}
