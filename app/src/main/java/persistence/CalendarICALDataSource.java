package persistence;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import CalendarUPV.*;

public class CalendarICALDataSource {

    private final String[] calendarColumns = { "id", "uid", "name", "timeZone", "comment" };
    private final String[] eventColumns = { "id", "uid", "dtend", "dtstart", "dtendFormat", "dtstartFormat", "dtendOriginal", "dtstartOriginal", "summary", "description", "location", "building", "allDay" };

    private final CalendarICALHelper diaryHelper;
    private SQLiteDatabase database;

    public CalendarICALDataSource(Context context){

        diaryHelper = new CalendarICALHelper(context);

    }

    public void close(){
        this.diaryHelper.close();
    }

    public void open() throws SQLException {
        if(this.database==null)
            this.database = this.diaryHelper.getWritableDatabase();
    }

    public CalendarICAL insertCalendar(CalendarICAL calendarICAL){

        CalendarICAL returnCalendar = calendarICAL;

        try{

            open();

            //propeties...
            ContentValues contentValues = new ContentValues();
            contentValues.put("uid", returnCalendar.getUid());
            contentValues.put("timeZone", returnCalendar.getTimeZone());
            contentValues.put("name", returnCalendar.getName());
            contentValues.put("comment", returnCalendar.getComment());

            returnCalendar.setId(
                    this.database.insert("Calendar", null, contentValues)
            );

            //events
            Iterator<EventICAL> iterator = returnCalendar.getEvents().iterator();

            while(iterator.hasNext()) {
                this.insertEventICAL(
                        iterator.next(),
                        returnCalendar
                );
            }

        }catch (SQLException e){
            Log.w(((Object) this).getClass().getName(), "Exception", e);
        }

        return returnCalendar;
    }

    public CalendarICAL getCalendarThisDayEvents(String uid){

        CalendarICAL calendarICAL = new CalendarICAL(uid);

        try{

            open();
            Cursor cursor = this.database.query("Calendar", this.calendarColumns, "uid='" + uid + "'", null, null, null, null);

            if(cursor.moveToFirst()){

                calendarICAL.setId(cursor.getLong(cursor.getColumnIndex("id")));
                calendarICAL.setUid(cursor.getString(cursor.getColumnIndex("uid")));
                calendarICAL.setComment(cursor.getString(cursor.getColumnIndex("comment")));
                calendarICAL.setName(cursor.getString(cursor.getColumnIndex("name")));
                calendarICAL.setTimeZone(cursor.getString(cursor.getColumnIndex("timeZone")));

                calendarICAL.setEvents(this.getEventsThisDayEvents(calendarICAL));

            }else{
                calendarICAL = null;
            }

        }catch (Exception e){
            Log.w(((Object) this).getClass().getName(), "Exception", e);
        }

        return calendarICAL;
    }

    public List<CalendarICAL> getInfoCalendars(){

        List<CalendarICAL> calendarICALList = new ArrayList<CalendarICAL>();

        try{

            open();
            Cursor cursor = this.database.query("Calendar", this.calendarColumns, null, null, null, null, null);

            CalendarICAL calendarICAL;

            while(cursor.moveToNext()) {

                calendarICAL = new CalendarICAL(cursor.getString(cursor.getColumnIndex("uid")));
                calendarICAL.setId(cursor.getLong(cursor.getColumnIndex("id")));
                calendarICAL.setComment(cursor.getString(cursor.getColumnIndex("comment")));
                calendarICAL.setName(cursor.getString(cursor.getColumnIndex("name")));
                calendarICAL.setTimeZone(cursor.getString(cursor.getColumnIndex("timeZone")));

                calendarICALList.add(calendarICAL);
            }

        }catch (Exception e){
            Log.w(((Object) this).getClass().getName(), "Exception", e);
        }

        return calendarICALList;
    }

    public CalendarICAL getCalendar(String uid){

        CalendarICAL calendarICAL = new CalendarICAL(uid);

        try{

            open();
            Cursor cursor = this.database.query("Calendar", this.calendarColumns, "uid='" + uid + "'", null, null, null, null);

            if(cursor.moveToFirst()){

                calendarICAL.setId(cursor.getLong(cursor.getColumnIndex("id")));
                calendarICAL.setUid(cursor.getString(cursor.getColumnIndex("uid")));
                calendarICAL.setComment(cursor.getString(cursor.getColumnIndex("comment")));
                calendarICAL.setName(cursor.getString(cursor.getColumnIndex("name")));
                calendarICAL.setTimeZone(cursor.getString(cursor.getColumnIndex("timeZone")));

                calendarICAL.setEvents(this.getEvents(calendarICAL));

            }else{
                calendarICAL = null;
            }

        }catch (Exception e){
            Log.w(((Object) this).getClass().getName(), "Exception", e);
        }

        return calendarICAL;
    }

    private List<EventICAL> getEvents(CalendarICAL calendarICAL){

        List<EventICAL> eventICALs = new ArrayList<EventICAL>();

        Cursor cursor = this.database.query("Event", this.eventColumns, "calendar_id=" + calendarICAL.getId(), null, null, null, null);
        EventICAL eventICAL;

        while(cursor.moveToNext()){

            eventICAL = new EventICAL();

            eventICAL.setId(cursor.getLong(cursor.getColumnIndex("id")));
            eventICAL.setUid(cursor.getString(cursor.getColumnIndex("uid")));
            eventICAL.setSummary(cursor.getString(cursor.getColumnIndex("summary")));
            eventICAL.setAllDay(cursor.getInt(cursor.getColumnIndex("allDay")) > 0 ? true : false);
            eventICAL.setDescription(cursor.getString(cursor.getColumnIndex("description")));
            eventICAL.setLocation(cursor.getString(cursor.getColumnIndex("location")));
            eventICAL.setDtend(cursor.getLong(cursor.getColumnIndex("dtend")));
            eventICAL.setDtstart(cursor.getLong(cursor.getColumnIndex("dtstart")));
            eventICAL.setDtendFormat(cursor.getString(cursor.getColumnIndex("dtendFormat")));
            eventICAL.setDtstartOriginal(cursor.getString(cursor.getColumnIndex("dtstartOriginal")));
            eventICAL.setDtstartFormat(cursor.getString(cursor.getColumnIndex("dtstartFormat")));
            eventICAL.setDtendOriginal(cursor.getString(cursor.getColumnIndex("dtendOriginal")));

            eventICALs.add(eventICAL);
        }

        return eventICALs;
    }

    private List<EventICAL> getEventsThisDayEvents(CalendarICAL calendarICAL){

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis( System.currentTimeMillis());

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyMMdd", Locale.getDefault());
        String today = simpleDateFormat.format(calendar.getTime());

        List<EventICAL> eventICALs = new ArrayList<EventICAL>();

        Cursor cursor = this.database.rawQuery("SELECT e.id, e.uid, e.dtend, e.dtstart, e.dtendFormat, e.dtstartFormat, e.dtendOriginal, e.dtstartOriginal, e.summary, e.description, e.location, e.building, e.allDay FROM Event AS e INNER JOIN Calendar AS c ON (e.calendar_id = c.id) WHERE dtstart" + "<" + today + "000000 AND " + "dtend" + ">" + today + "246060 ORDER BY " + "dtstart" + " ASC", null);
        EventICAL eventICAL;

        while(cursor.moveToNext()){

            eventICAL = new EventICAL();

            eventICAL.setId(cursor.getLong(cursor.getColumnIndex("id")));
            eventICAL.setUid(cursor.getString(cursor.getColumnIndex("uid")));
            eventICAL.setSummary(cursor.getString(cursor.getColumnIndex("summary")));
            eventICAL.setAllDay(cursor.getInt(cursor.getColumnIndex("allDay")) > 0 ? true : false);
            eventICAL.setDescription(cursor.getString(cursor.getColumnIndex("description")));
            eventICAL.setLocation(cursor.getString(cursor.getColumnIndex("location")));
            eventICAL.setDtend(cursor.getLong(cursor.getColumnIndex("dtend")));
            eventICAL.setDtstart(cursor.getLong(cursor.getColumnIndex("dtstart")));
            eventICAL.setDtendFormat(cursor.getString(cursor.getColumnIndex("dtendFormat")));
            eventICAL.setDtstartOriginal(cursor.getString(cursor.getColumnIndex("dtstartOriginal")));
            eventICAL.setDtstartFormat(cursor.getString(cursor.getColumnIndex("dtstartFormat")));
            eventICAL.setDtendOriginal(cursor.getString(cursor.getColumnIndex("dtendOriginal")));

            eventICALs.add(eventICAL);
        }

        return eventICALs;
    }

    private EventICAL insertEventICAL(EventICAL eventICAL, CalendarICAL calendarICAL){

        EventICAL returnEventICAL = eventICAL;

        ContentValues contentValues = new ContentValues();
        contentValues.put("calendar_id", calendarICAL.getId());
        contentValues.put("uid", returnEventICAL.getUid());
        contentValues.put("building", returnEventICAL.getBuilding());
        contentValues.put("description", returnEventICAL.getDescription());
        contentValues.put("dtend", returnEventICAL.getDtend());
        contentValues.put("dtendFormat", returnEventICAL.getDtendFormat());
        contentValues.put("dtendOriginal", returnEventICAL.getDtendOriginal());
        contentValues.put("dtstart", returnEventICAL.getDtstart());
        contentValues.put("dtstartFormat", returnEventICAL.getDtstartFormat());
        contentValues.put("dtstartOriginal", returnEventICAL.getDtstartOriginal());
        contentValues.put("location", returnEventICAL.getLocation());
        contentValues.put("summary", returnEventICAL.getSummary());
        contentValues.put("allDay", returnEventICAL.getAllDay());

        returnEventICAL.setId(
                this.database.insert("Event", null, contentValues)
        );

        return returnEventICAL;
    }


}
