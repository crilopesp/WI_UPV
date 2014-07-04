package Calendar;


import android.util.Log;

import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.PropertyList;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;

public class ICal {

    private String name;
    private String timeZone;
    private String comment;
    private List<Event> events;

    public ICal(Calendar calendar){
        this. parseCalendar(calendar);
    }

    private void parseCalendar(Calendar calendar) {

        //properties...
        this.name = calendar.getProperty("X-WR-CALNAME").getValue();
        this.timeZone = calendar.getProperty("X-WR-TIMEZONE").getValue();
        this.comment = calendar.getProperty("COMMENT").getValue();

        //events....
        events = new ArrayList<Event>();
        Iterator<Component> iterator =  calendar.getComponents().iterator();
        while(iterator.hasNext()){
            events.add(
                    new Event(iterator.next())
            );
        }

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

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    @Override
    public String toString() {

        String string = "";
        for(Event item : this.events)
            string+=item.toString();

        return "ICal{" +
                "name='" + name + '\'' +
                ", timeZone=" + timeZone +
                ", comment='" + comment + '\'' +
                ", events=" + string +
                '}';
    }
}
