/**
 * Created by snow_ on 07-Dec-16.
 */
public class Signature {
    private long r,s;

    public Signature(long r, long s) {
        this.r = r;
        this.s = s;
    }

    public long getR() {
        return r;
    }

    public long getS() {
        return s;
    }

    @Override
    public String toString() {
        return "Signature{" +
                "r=" + r +
                ", s=" + s +
                '}';
    }
}
