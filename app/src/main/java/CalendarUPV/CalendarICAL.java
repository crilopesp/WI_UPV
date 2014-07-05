package CalendarUPV;


import android.util.Log;

import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class CalendarICAL {

    private long id;
    private String uid;
    private String name;
    private String timeZone;
    private String comment;
    private List<EventICAL> events;

    public CalendarICAL(net.fortuna.ical4j.model.Calendar calendar, String uid) {
        this.uid = uid;
        this.parseCalendar(calendar);
    }

    public CalendarICAL(String uid) {
        this.uid = uid;
    }

    private void parseCalendar(Calendar calendar) {

        //properties...
        this.name = calendar.getProperty("X-WR-CALNAME").getValue();
        this.timeZone = calendar.getProperty("X-WR-TIMEZONE").getValue();
        this.comment = calendar.getProperty("COMMENT").getValue();

        //events....
        events = this.getEvetsAfterToday(calendar.getComponents());
    }

    private List<EventICAL> getEvetsAfterToday(List<Component> componentList) {

        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
        String today = simpleDateFormat.format(calendar.getTime());

        List<EventICAL> returnEventICALList1 = new ArrayList<EventICAL>();

        Iterator<Component> iterator = componentList.iterator();

        EventICAL eventICAL = null;

        while (iterator.hasNext()) {

            eventICAL = new EventICAL(iterator.next());
            today = "20131201";
            if (eventICAL.getDtstartOriginal().compareTo(today) > 0) {
                returnEventICALList1.add(eventICAL);
            }
        }
        Collections.sort(returnEventICALList1, new comparatorEvent());

        return returnEventICALList1;
    }

    private List<EventICAL> getEvetsToday(List<Component> componentList) {

        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
        String today = simpleDateFormat.format(calendar.getTime());

        List<EventICAL> returnEventICALList1 = new ArrayList<EventICAL>();

        Iterator<Component> iterator = componentList.iterator();

        EventICAL eventICAL = null;

        Log.w(((Object) this).getClass().getName(), "Today: " + today);

        while (iterator.hasNext()) {

            eventICAL = new EventICAL(iterator.next());

            if (eventICAL.getDtstartOriginal() == today) {
                returnEventICALList1.add(eventICAL);
            }
        }
        return returnEventICALList1;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<EventICAL> getEvents() {
        return events;
    }

    public void setEvents(List<EventICAL> events) {
        this.events = events;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @Override
    public String toString() {

        String string = "";
        for (EventICAL item : this.events)
            string += item.toString();

        return "ICal{" +
                "uid='" + uid + '\'' +
                ", name='" + name + '\'' +
                ", timeZone=" + timeZone +
                ", comment='" + comment + '\'' +
                ", events=" + string +
                '}';
    }
}
