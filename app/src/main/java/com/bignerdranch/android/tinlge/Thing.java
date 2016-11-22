package com.bignerdranch.android.tinlge;

import java.util.UUID;

/**
 * Created by Omer on 10.02.2016.
 */
public class Thing {
    public static final String preText = "Item: ";
    public static final String postText = " is here: ";

    public void setId(Integer id) {
        mId = id;
    }

    private Integer mId;
    private String mWhat;
    private String mWhere;
    private Integer mImportant;

    public Integer getImportant() {
        return mImportant;
    }

    public Thing(String mWhat, String mWhere, Integer mImportant) {
        //this.mId = UUID.randomUUID();
        this.mWhat = mWhat;
        this.mWhere = mWhere;
        this.mImportant = mImportant;
    }

    public Thing() {
        //this(UUID.randomUUID());
    }

/*    public Thing(UUID id){
        this.mId = id;
    } */

    public Integer getId() {
        return mId;
    }

    public String getmWhat() {
        return mWhat;
    }

    public String getmWhere() {
        return mWhere;
    }

    public void setmWhat(String mWhat) {
        this.mWhat = mWhat;
    }

    public void setmWhere(String mWhere) {
        this.mWhere = mWhere;
    }

    public String OneLine(String pre, String post){
        return pre+this.mWhat + " " + post+this.mWhere;
    }

    @Override
    public String toString() {
        return this.OneLine( preText, postText);
        //return (context.getString( R.string.item_pre_value));
    }
}
