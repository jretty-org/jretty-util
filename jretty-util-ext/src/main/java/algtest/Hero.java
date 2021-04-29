/**
 * 
 */
package algtest;

/**
 * 
 * @author zollty
 * @since 2021-1-9
 */
abstract public class Hero {
    public int id;
    public long xuel;
    public long gongj;
    public int sudu; //速度
    public int zhw; //站位次序1~6
    public int xj; //星级1~14

    public Hero(long xuel, long gongj) {
        super();
        this.xuel = xuel;
        this.gongj = gongj;
    }

    abstract public Event[] recvEvent(Event event); 
}
