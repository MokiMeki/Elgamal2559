
import java.io.Serializable;

/**
 * Created by snow_ on 07-Dec-16.
 */
public class PublicKey implements Serializable{
    private static final long serialVersionUID = 1L;
    
    private long p,g,y;

    public PublicKey(long p, long g, long y) {
        this.p = p;
        this.g = g;
        this.y = y;
    }

    public long getP() {
        return p;
    }

    public long getG() {
        return g;
    }

    public long getY() {
        return y;
    }
    @Override
    public String toString() {
        return "\nPublic key is: " +
                "\n\tp: "+p +
                "\n\tg: "+g +
                "\n\ty: "+y;
    }
}
