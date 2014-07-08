package calendar;


import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ICal {

    private String name;
    private String timeZone;
    private String comment;
    private List<Evento> eventos;

    public ICal(Calendar calendar) {
        this.parseCalendar(calendar);
    }

    private void parseCalendar(Calendar calendar) {

        //properties...
        this.name = calendar.getProperty("X-WR-CALNAME").getValue();
        this.timeZone = calendar.getProperty("X-WR-TIMEZONE").getValue();
        this.comment = calendar.getProperty("COMMENT").getValue();

        //events....
        eventos = new ArrayList<Evento>();
        Iterator<Component> iterator = calendar.getComponents().iterator();
        while (iterator.hasNext()) {
            eventos.add(
                    new Evento(iterator.next())
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

    public List<Evento> getEventos() {
        return eventos;
    }

    public void setEventos(List<Evento> eventos) {
        this.eventos = eventos;
    }

    @Override
    public String toString() {

        String string = "";
        for (Evento item : this.eventos)
            string += item.toString();

        return "ICal{" +
                "name='" + name + '\'' +
                ", timeZone=" + timeZone +
                ", comment='" + comment + '\'' +
                ", events=" + string +
                '}';
    }
}
