package com.bignerdranch.android.tinlge.database;

import java.security.PublicKey;

/**
 * Created by Omer on 13.03.2016.
 */
public class ThingDbSchema {

    public static  final class ThingTable {
        public static final String NAME = "things";
        public static final class Cols {
            public static final String ID = "_id";
            public static final String WHAT = "_what";
            public static final String WHERE = "_where";
            public static final String IMPORTANT = "important";
        }
    }
}

