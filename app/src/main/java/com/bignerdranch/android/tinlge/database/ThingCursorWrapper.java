package com.bignerdranch.android.tinlge.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.bignerdranch.android.tinlge.Thing;

import static com.bignerdranch.android.tinlge.database.ThingDbSchema.*;

/**
 * Created by Omer on 13.03.2016.
 */
public class ThingCursorWrapper extends CursorWrapper {
    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */
    public ThingCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Thing getThing() {
        String what = getString(getColumnIndex(ThingTable.Cols.WHAT));
        String where = getString(getColumnIndex(ThingTable.Cols.WHERE));
        Integer id = getInt(getColumnIndex(ThingTable.Cols.ID));

        //Thing thing = new Thing(UUID.fromString(id));
        Thing thing = new Thing();
        thing.setmWhat(what);
        thing.setmWhere(where);
        thing.setId(id);

        return thing;
    }

}
