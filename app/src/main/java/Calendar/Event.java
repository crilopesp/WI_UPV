package Calendar;

import android.text.format.DateFormat;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.component.VEvent;

import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Event
{

    private final Pattern DATE_PATTERN = Pattern.compile("(\\d{4,})(\\d\\d)(\\d\\d)(?:T([0-1]\\d|2[0-3])([0-5]\\d)([0-5]\\d)(Z)?)?");

    private final SimpleDateFormat simpleDateFormatDateTime = new SimpleDateFormat("EEE, dd MMM yyyy - HH:mm", Locale.getDefault());
    private final SimpleDateFormat simpleDateFormatDate = new SimpleDateFormat("EEE, dd MMM yyyy", Locale.getDefault());

    private Matcher matcherEnd;
    private Matcher matcherStart;

    private boolean isAllDay;

    private String dtend;
    private String dtstart;
    private String summary;
    private String uid;
    private String description;
    private String location;
    private String dtstamp;

    public Event(Component component){
        this.parseComponent(component);
    }

    private void parseComponent(Component component){

        this.uid = component.getProperty("UID").getValue();
        this.dtend = component.getProperty("DTEND").getValue();
        this.dtstart = component.getProperty("DTSTART").getValue();
        this.dtstamp = component.getProperty("DTSTAMP").getValue();
        this.summary = component.getProperty("SUMMARY").getValue();
        this.description = component.getProperty("DESCRIPTION").getValue();
        this.location = component.getProperty("LOCATION").getValue();
        this.isAllDay = this.isAllDay();

        try {

            if(this.isAllDay) {

                this.dtstart = simpleDateFormatDate.format(new DateTime(this.dtstart));
                this.dtend = simpleDateFormatDate.format(new DateTime(this.dtend));

            }else{

                this.dtstart = simpleDateFormatDateTime.format(new DateTime(this.dtstart));
                this.dtend = simpleDateFormatDateTime.format(new DateTime(this.dtend));
            }

        } catch (ParseException e) {
            Log.w(((Object) this).getClass().getName(), "Exception", e);
        }

    }

    private boolean isAllDay()
    {
        matcherStart = DATE_PATTERN.matcher(this.dtstart);
        matcherEnd = DATE_PATTERN.matcher(this.dtend);

        if ((matcherStart.matches()) && (matcherEnd.matches()) && (matcherStart.group(4) == null) && (matcherEnd.group(4) == null)) {
            while ((this.dtend != null) && (this.dtstart.indexOf("T000000") > -1) && (this.dtend.indexOf("T000000") > -1))
                return true;
        }

        return false;
    }

    @Override
    public String toString() {
        return "Event{" +
                ", isAllDay=" + isAllDay +
                ", dtend='" + dtend + '\'' +
                ", dtstart='" + dtstart + '\'' +
                ", summary='" + summary + '\'' +
                ", uid='" + uid + '\'' +
                ", description='" + description + '\'' +
                ", location='" + location + '\'' +
                ", dtstamp='" + dtstamp + '\'' +
                '}';
    }
}