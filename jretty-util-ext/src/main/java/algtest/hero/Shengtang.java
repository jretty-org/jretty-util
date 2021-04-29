/**
 * 
 */
package algtest.hero;

import algtest.Event;
import algtest.EventType;
import algtest.Hero;

/**
 * 
 * @author zollty
 * @since 2021-1-9
 */
public class Shengtang extends Hero {

    public Shengtang() {
        super(1000000, 10000);
    }

    public Event[] recvEvent(Event event) {
        switch(event.type()) {
        case BAN_XUE: 
            break;
        default:
                ;
        }
             
        return null;
    }

}
