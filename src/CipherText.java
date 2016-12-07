import java.io.Serializable;

public class CipherText implements Serializable {

    private static final long serialVersionUID = 1L;

    private long a;
    private long b;

    public CipherText() {
    }


    public long getA() {
        return a;
    }

    public void setA(long a) {
        this.a = a;
    }

    public long getB() {
        return b;
    }

    public void setB(long b) {
        this.b = b;
    }

}