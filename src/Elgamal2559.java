import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by snow_ on 06-Dec-16.
 */
public class Elgamal2559 {
    private long n = 32,k;
    private String filePath = "./src/res/lumyai.png";

    public Elgamal2559(){}
    public Elgamal2559(long n, long k, String filePath){
        this.n = n;
        this.k = k;
        this.filePath = filePath;
    }

    public long generateP(){
        File file = new File(filePath);
        FileInputStream fileInputStream = null;
        long input = 0;
        try {
            fileInputStream = new FileInputStream(filePath);
            while(safePrimeTest(input)==false){
                input = getNextBitsFromFile(n,fileInputStream);
            }
        }catch (IOException e){
            System.out.println("Generate P Error: "+ e +" "+ file.getPath());System.exit(0);
        }
        return input;
    }

    public PrivateKey generatePrivateKey(){
        long p = generateP();
        long g = genGenerator(p);
        long u = (long)(Math.random()*(p-2))+2;
        long y = fastExponential(g,u,p);
        return new PrivateKey(p,g,y,u);
    }

    private long log2of(long number){
        return (long)(Math.log10(number)/Math.log10(2));
    }

    private long getNextBitsFromFile(long length,FileInputStream fileInputStream) throws IOException{
        long input = 0;
        while(input==0){
            input = fileInputStream.read();
        }
        for(long numberOfByteNeeded = (length/8); numberOfByteNeeded > 0 ; numberOfByteNeeded--){
            input = input << 8;
            input += fileInputStream.read();
        }
        long maxIntOfBitLength = (long)Math.pow(2,length);
        while(input > maxIntOfBitLength){
            input >>= 1;
        }
        return input;
    }

    private boolean lehmanTest(long a, long n) {
        if (gcdFromEuclidExtended(a, n) > 1) {
            return false;
        } else if (fastExponential(a, (n - 1) / 2, n) == 1 || fastExponential(a, (n - 1) / 2, n) == n - 1) {
            return true;
        } else {
            return false;
        }
    }

    private boolean primeTest(long n) {
        if (n % 2 == 0 || n == 1) {
            return false;
        }
        for (int i = 0; i < 100; i++) {
            long a = (long) (Math.random() * (n - 2) + 1);
            if (!lehmanTest(a, n)) {
                return false;
            }
        }
        return true;
    }

    private boolean safePrimeTest(long n) {
        if (primeTest(n)) {
            long q = (n - 1) / 2;
            if (primeTest(q)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public long fastExponential(long base, long power, long mod){
        long increasingPower = 1;
        long value = base;
        long answer = 1;
        Map<Long,Long> map = new HashMap();

        while(increasingPower<=power){
            map.put(increasingPower,value);
            value = (value*value)%mod;
            increasingPower *= 2;
        }

        long biggestPower = increasingPower;
        while(power > 0){
            while(biggestPower > power) {
                biggestPower /= 2;
            }
            answer *= map.get(biggestPower);
            answer %= mod;
            power -= biggestPower;
        }
        return answer;
    }

    public long inverseFromEuclidExtended(long number, long mod){
        long n1 = mod;
        long n2 = number%mod;
        long a1 = 1,b1 = 0, a2 = 0, b2 = 1;
        long r,q,temp;

        r = n1%n2;
        q = n1/n2;

        while(r!=0){
            n1 = n2;
            n2 = r;
            temp = a2;
            a2 = a1-(q*a2);
            a1 = temp;
            temp = b2;
            b2 = b1-(q*b2);
            b1 = temp;

            r = n1%n2;
            q = n1/n2;
        }
        return b2;
    }

    public long gcdFromEuclidExtended(long number, long mod){
        long n1 = mod;
        long n2 = number%mod;;
        long a1 = 1,b1 = 0, a2 = 0, b2 = 1;
        long r,q,temp;

        if(n2==0){
            return mod;
        }

        r = n1%n2;
        q = n1/n2;

        while(r!=0){
            n1 = n2;
            n2 = r;
            temp = a2;
            a2 = a1-(q*a2);
            a1 = temp;
            temp = b2;
            b2 = b1-(q*b2);
            b1 = temp;

            r = n1%n2;
            q = n1/n2;
        }
        return n2;
    }

    public long genGenerator(long p){
        long generator = (long)(Math.random()*(p-1))+1;
        while(fastExponential(generator,(p-1)/2,p)==1){
            generator = (long)(Math.random()*(p-1))+1;
        }
        return generator;
    }
}
