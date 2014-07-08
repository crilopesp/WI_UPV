package calendarupv;

import java.util.Comparator;

/**
 * Created by Cristian on 02/07/2014.
 */
public class ComparadorEventos implements Comparator {
    public int compare(Object o1, Object o2) {
        EventICAL event1 = (EventICAL) o1;
        EventICAL event2 = (EventICAL) o2;
        return event1.compareTo(event2);
    }
}
