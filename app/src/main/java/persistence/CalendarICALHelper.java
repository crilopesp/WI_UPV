package persistence;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CalendarICALHelper extends SQLiteOpenHelper{

    private final String DB_CREATE_EVENTS = "CREATE TABLE Event (id INTEGER PRIMARY KEY AUTOINCREMENT, uid TEXT, calendar_id INTEGER, building TEXT, allDay INTEGER, summary TEXT, description TEXT, location TEXT, dtstart INTEGER, dtend INTEGER, dtendFormat TEXT, dtstartFormat TEXT, dtendOriginal TEXT, dtstartOriginal TEXT)";
    private final String DB_CREATE_DIARY = "CREATE TABLE Calendar (id INTEGER PRIMARY KEY AUTOINCREMENT, uid TEXT, timeZone TEXT, comment TEXT, name TEXT)";

    private final String DB_DROP_EVENTS = "DROP TABLE IF EXISTS Event";
    private final String DB_DROP_DIARY = "DROP TABLE IF EXISTS Calendar";

    public CalendarICALHelper(Context context){
        super(context,"welcomeIncomingDiary.db",null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DB_CREATE_EVENTS);
        db.execSQL(DB_CREATE_DIARY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < newVersion){
            db.execSQL(DB_DROP_EVENTS);
            db.execSQL(DB_DROP_DIARY);
            onCreate(db);
        }
    }
}
