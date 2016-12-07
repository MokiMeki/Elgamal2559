/**
 * Created by snow_ on 07-Dec-16.
 */
public class PublicKey {
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
}
