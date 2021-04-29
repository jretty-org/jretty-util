/**
 * 
 */
package algtest.hero;

import algtest.Event;
import algtest.EventType;
import algtest.Hero;
import algtest.Main;
import algtest.TargetType;

/**
 * 
 * @author zollty
 * @since 2021-1-9
 */
public class Yisheng extends Hero {

    public Yisheng() {
        super(11, 111);
    }

    @Override
    public Event[] recvEvent(Event event) {
        switch(event.type()) {
        case BAN_XUE: 
            Main.eventQ.add(new Event() {
                @Override
                public EventType type() {
                    return EventType.CHANGE;
                }
                @Override
                public TargetType target() {
                    return TargetType.ALL;
                }
                @Override
                public void change(Hero h) {
                    //h.gongj
                }
            });
            break;
        default:
                ;
        }
             
        return null;
    }
    

}
