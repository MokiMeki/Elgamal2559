import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by snow_ on 06-Dec-16.
 */
public class Elgamal2559 {
    private long n = 32,k = 5;
    private String filePath = "./src/res/lumyai.png";

    public Elgamal2559(){}
    public Elgamal2559(long n, long k, String filePath){
        this.n = n;
        this.k = k;
        this.filePath = filePath;
    }

    public void startUserInterface(){
        System.out.println("Welcome to Elgamal2559 CryptoEngine");
        Scanner sc = new Scanner(System.in);
        String strInput = "";
        int intInput = 0;
        int function = 0;
        while(true){
            System.out.println("\nWhat do you want to do? (Enter 1-5)");
            System.out.println("1.Encryption");
            System.out.println("2.Decryption");
            System.out.println("3.Signature");
            System.out.println("4.Verify");
            System.out.println("5.CryptoHash");
            System.out.println("6.GeneratePrivateKey");
            System.out.println("7.getPublicKey");
            System.out.println("0.Exit");
            System.out.print("> ");
            function = sc.nextInt();
            switch (function){
                case 0: System.exit(0);
                    break;
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    break;
                case 5:
                    break;
                case 6: writePrivateKeyToFile();
                    System.out.print(retrievePrivateKeyFromFile());
                    break;
                case 7:
                    break;
                default: continue;
            }
        }
    }

    private long generateP(){
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

    private PrivateKey generatePrivateKey(){
        long p = generateP();
        long g = genGenerator(p);
        long u = (long)(Math.random()*(p-2))+2;
        long y = fastExponential(g,u,p);
        return new PrivateKey(p,g,y,u);
    }

    public void writePrivateKeyToFile(){
        PrivateKey privateKey = generatePrivateKey();
        try (ObjectOutputStream oos =
                     new ObjectOutputStream(new FileOutputStream("./Private.key"))) {
            oos.writeObject(privateKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public PrivateKey retrievePrivateKeyFromFile(){
        PrivateKey privateKey = null;
        try (ObjectInputStream ois
                     = new ObjectInputStream(new FileInputStream("./Private.key"))) {
            privateKey = (PrivateKey) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return privateKey;
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

    private long fastExponential(long base, long power, long mod){
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

    private long inverseFromEuclidExtended(long number, long mod){
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

    private long gcdFromEuclidExtended(long number, long mod){
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

    private long genGenerator(long p){
        long generator = (long)(Math.random()*(p-1))+1;
        while(fastExponential(generator,(p-1)/2,p)==1){
            generator = (long)(Math.random()*(p-1))+1;
        }
        return generator;
    }

    private Signature sign(PrivateKey sk,long hashValue){
        long p = sk.getP();
        long k = randomK(p);
        long r = fastExponential(sk.getG(),k,p);

        long kInverse = inverseFromEuclidExtended(k,p-1);

        // k^(-1) (X-xr) mod (p-1)
        long s = (kInverse*((hashValue-((sk.getU()*r)%(p-1)))%(p-1)))%(p-1);

        return new Signature(r,s);
    }

    private long randomK(long p) {
        while (true) {
            long k = (long) (Math.random() * (p - 1) + 1);
            if (gcdFromEuclidExtended(k, p - 1) == 1) {
                return k;
            }
        }
    }

    private boolean verifyHash(PublicKey pk, Signature sign, long hashValue){
        long g = pk.getG();
        long p = pk.getP();
        long y = pk.getY();

        long r = sign.getR();
        long s = sign.getS();
        if(fastExponential(g,hashValue,p)==((fastExponential(y,r,p)*fastExponential(r,s,p))%p)){
            return true;
        }
        return false;
    }

    private CipherText encryption(long message, long p, long g, long y) {
        CipherText c = new CipherText();
        long k = randomK(p);
        c.setA(fastExponential(g, k, p));
        long temp = fastExponential(y, k, p);
        temp = (temp * message) % p;
        c.setB(temp);
        return c;
    }

    private long decryption(CipherText c, long u, long p) {
        long message = 0;
        long temp = fastExponential(c.getA(), p - 1 - u, p);
        message = (temp * c.getB()) % p;
        return message;
    }

    private long roundHash(long ti, ArrayList<Long> c, long p) {
        long hi = 0;
        long sum = 0;
        for (int i = 0; i < c.size(); i++) {
            sum += fastExponential(c.get(i), i + 2, p);
        }
        hi = (ti ^ sum) % p;
        return hi;
    }

    private long polyHash(long p, ArrayList<CipherText> c, long alpha) {
        long hashValue = 0;
        ArrayList<Long> c2 = new ArrayList<Long>();
        for (CipherText temp : c) {
            c2.add(temp.getA());
            c2.add(temp.getB());
        }
        long hi = 0;
        for (int i = 0; i <= c2.size() / (k - 1); i++) {
            ArrayList<Long> Bi = new ArrayList<Long>();
            for (int j = 0; j < c2.size() - i * (k - 1); j++) {
                int index = (i * ((int) k - 1)) + j;
                Bi.add(c2.get(index));
            }
            if (i == 0) {
                hi = roundHash(alpha, Bi, p);
            } else {
                hi = roundHash(hi, Bi, p);
            }
        }
        return hi;
    }
}
