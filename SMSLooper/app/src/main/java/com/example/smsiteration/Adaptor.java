package com.example.smsiteration;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

public class Adaptor extends CursorAdapter {

    private Context context;
    public Adaptor(Context context, Cursor c, boolean autoRequery) {
        super(context, c, false);
        this.context=context;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return null;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

    }
}
