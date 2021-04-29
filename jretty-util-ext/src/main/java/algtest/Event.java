/**
 * 
 */
package algtest;

/**
 * 
 * @author zollty
 * @since 2021-1-9
 */
abstract public class Event {
    
    abstract public EventType type();
    
    abstract public TargetType target();
    
    abstract public void change(Hero h);
}
