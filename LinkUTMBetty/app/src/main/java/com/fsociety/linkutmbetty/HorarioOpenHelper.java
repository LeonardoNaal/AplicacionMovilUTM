package com.fsociety.linkutmbetty;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by Emmanuel on 14/03/2017.
 */

public class HorarioOpenHelper extends SQLiteOpenHelper {
    public HorarioOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE HORARIO(USUARIO TEXT,DIA TEXT,MATERIA TEXT,PROFESOR TEXT,LABORATORIO TEXT,HORA TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS HORARIO");
        sqLiteDatabase.execSQL("CREATE TABLE HORARIO(USUARIO TEXT,DIA TEXT ,MATERIA TEXT,PROFESOR TEXT,LABORATORIO TEXT,HORA TEXT)");
    }
}
