import java.io.Serializable;

/**
 * Created by snow_ on 07-Dec-16.
 */
public class PrivateKey implements Serializable{

    private static final long serialVersionUID = 1L;

    private long p,g,y,u;

    public PrivateKey(long p, long g, long y, long u) {
        this.p = p;
        this.g = g;
        this.y = y;
        this.u = u;
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

    public long getU() {
        return u;
    }

    public PublicKey getPublicKey(){
        return new PublicKey(p,g,y);
    }

    @Override
    public String toString() {
        return "\nPrivate key is: " +
                "\n\tp: "+p +
                "\n\tg: "+g +
                "\n\ty: "+y +
                "\n\tu: "+u;
    }
}
