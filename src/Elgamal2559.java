
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import javax.imageio.ImageIO;

/**
 * Created by snow_ on 06-Dec-16.
 */
public class Elgamal2559 {

    private long n = 32, k = 5;
    private String filePath = "/Users/Mar-Kii/Elgamal2559/src/src/res/lumyai.png";

    public Elgamal2559() {
    }

    public Elgamal2559(long n, long k, String filePath) {
        this.n = n;
        this.k = k;
        this.filePath = filePath;
    }

    public void startUserInterface() {
        System.out.println("Welcome to Elgamal2559 CryptoEngine");
        Scanner sc = new Scanner(System.in);
        String strInput = "";
        int intInput = 0;
        int function = 0;
        while (true) {
            System.out.println("\nWhat do you want to do? (Enter 1-12)");
            System.out.println("-1.[W-FILE] Encryption");
            System.out.println("2.[W-TEXT] Encryption");
            System.out.println("3.GetCipherText");
            System.out.println("-4.[W-FILE] Decryption");
            System.out.println("5.[W-TEXT] Decryption");
            System.out.println("6.[W-FILE] Signature");
            System.out.println("7.GetSignature");
            System.out.println("8.Verify Signature");
            System.out.println("9.CryptoHash");
            System.out.println("10.[W-FILE] GeneratePrivateKey");
            System.out.println("11.GetPrivateKey");
            System.out.println("12.GetPublicKey");
            System.out.println("0.Exit");
            System.out.print("> ");
            function = sc.nextInt();
            switch (function) {
                case 0:
                    System.exit(0);
                    break;
                case 1:
                    System.out.print("Enter file path : ");
                    strInput = sc.nextLine();
                    strInput = sc.nextLine();
                    encryptionFileToFile(strInput);
                    System.out.println("Encrypted..");
                    break;
                case 2:
                    System.out.print("Enter text to Encryption : ");
                    strInput = sc.nextLine();
                    strInput = sc.nextLine();
                    encryptionTextToFile(strInput);
                    System.out.println("Encrypted..");
                    break;
                case 3:
                    printCipherText();
                    break;
                case 4:
                    System.out.print("Enter file destination path : ");
                    strInput = sc.nextLine();
                    strInput = sc.nextLine();
                    decryptionFiletoFile(strInput);
                    System.out.println("Decrypted..");
                    break;
                case 5:
                    System.out.println("Decrytping...");
                    decryptionFileToText();
                    break;
                case 6:
                    writeSignatureToFile();
                    System.out.println("CipherText signed..");
                    break;
                case 7:
                    System.out.println(retrieveSignatureFromFile());
                    break;
                case 8:
                    System.out.println("Verify result = " + verifySignature());
                    break;
                case 9:
                    cryptoHash();
                    break;
                case 10:
                    writePrivateKeyToFile();
                    System.out.print(retrievePrivateKeyFromFile());
                    break;
                case 11:
                    System.out.print(retrievePrivateKeyFromFile());
                    break;
                case 12:
                    writePublicKeyToFile();
                    System.out.print(retrievePublicKeyFromFile());
                    break;
                default:
                    continue;
            }
        }
    }

    public void printCipherText() {
        ArrayList<CipherText> c = retrieveCipherTextFromFile();
        for (CipherText temp : c) {
            System.out.println("A : " + temp.getA() + " B : " + temp.getB());
        }
    }

    private long generateP() {
        File file = new File(filePath);
        FileInputStream fileInputStream = null;
        long input = 0;
        try {
            fileInputStream = new FileInputStream(filePath);
            while (safePrimeTest(input) == false) {
                input = getNextBitsFromFile(n, fileInputStream);
            }
        } catch (IOException e) {
            System.out.println("Generate P Error: " + e + " " + file.getPath());
            System.exit(0);
        }
        return input;
    }

    public void encryptionTextToFile(String text) {
        PublicKey publicKey = retrievePublicKeyFromFile();
        ArrayList<CipherText> c = new ArrayList<CipherText>();
        byte[] b = text.getBytes();
        long input = 0;
        for (int i = 0; i < b.length; i++) {
            input += b[i];
            if (i % 3 == 0 || i == b.length - 1) {
                CipherText temp = encryption(input, publicKey.getP(), publicKey.getG(), publicKey.getY());
                c.add(temp);
                input = 0;
            }
            input = input << 8;
        }
        System.out.println(publicKey.toString());
        writeCipherTextToFile(c);
    }

    public void decryptionFileToText() {
        ArrayList<CipherText> c = retrieveCipherTextFromFile();
        PrivateKey privateKey = retrievePrivateKeyFromFile();
        System.out.println(privateKey.toString());
        int[] result = new int[c.size()];
        int count = 0;
        for (CipherText temp : c) {
            long d = decryption(temp, privateKey.getU(), privateKey.getP());
            //System.out.println("DDD"+d);
            result[count++] = (int) d;
            //System.out.println("DDD"+result[count-1]);
        }
        int i = 0;
        byte[] x = new byte[result.length * 4];
        for (int j = 0; j < result.length; j++) {
            byte[] t2 = convertIntToByte(result[j]);
            for (int k = 0; k < 4; k++) {
                if (t2[k] != 0) {
                    x[i++] = t2[k];
                }
            }
        }
        String str = new String(x);
        System.out.println("Message decrypted = " + str.trim());
    }

    public void encryptionFileToFile(String filePath) {
        File file = new File(filePath);
        FileInputStream fileInputStream = null;
        ArrayList<CipherText> c = new ArrayList<CipherText>();
        PublicKey publicKey = retrievePublicKeyFromFile();
        try {
            fileInputStream = new FileInputStream(filePath);
            while (fileInputStream.available() > 0) {
                long input = 0;
                for (int i = 0; i < 4; i++) {
                    input = input << 8;
                    input += fileInputStream.read();
                    if (fileInputStream.available() == 0) {
                        //input = input << (8 * (3 - i));
                        break;
                    }
                }
                CipherText temp = encryption(input, publicKey.getP(), publicKey.getG(), publicKey.getY());
                c.add(temp);
            }
            System.out.println(publicKey.toString());
            writeCipherTextToFile(c);
        } catch (IOException e) {
            System.out.println("Encryption file Error: " + e + " " + file.getPath());
            System.exit(0);
        }
    }

    public void writeCipherTextToFile(ArrayList<CipherText> c) {
        try (ObjectOutputStream oos
                = new ObjectOutputStream(new FileOutputStream("./CipherText.ct"))) {
            oos.writeObject(c);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void decryptionFiletoFile(String filePath) {
        File file = new File(filePath);
        ArrayList<CipherText> c = retrieveCipherTextFromFile();
        PrivateKey privateKey = retrievePrivateKeyFromFile();
        System.out.println(privateKey.toString());
        String content = new String();
        int[] result = new int[c.size()];
        int i = 0;
        for (CipherText temp : c) {
            long output = decryption(temp, privateKey.getU(), privateKey.getP());
            result[i++] = (int) output;
        }
        i = 0;
        try (FileOutputStream fop = new FileOutputStream(file)) {
            if (!file.exists()) {
                file.createNewFile();
            }
            
            byte[] out = new byte[result.length * 4];
            for (int j = 0; j < result.length; j++) {
                byte[] t2 = convertIntToByte(result[j]);
                for (int k = 0; k < 4; k++) {
                  //  if (t2[k] != 0) {
                        out[i++] = t2[k];
                  //  }
                }
            }
//            InputStream inp = new InputStream();
//            image = ImageIO.read(inp.read(out));
//
//            ImageIO.write(image, "jpg",new File("C:\\out.jpg"));
//
//            BufferedImage bi = getMyImage();
//            File outputfile = new File("saved.png");
//            ImageIO.write(bi, "png", outputfile);

//            InputStream in = new ByteArrayInputStream(out);
//            BufferedImage bImageFromConvert = ImageIO.read(in);
//            ImageIO.write(bImageFromConvert,"png",file);
///////////////////
            ByteArrayOutputStream baos = null;
            DataOutputStream dos = null;

            dos = new DataOutputStream(fop);

            dos.write(out, 0, out.length);
            dos.flush();
            dos.close();
            //fop.write(out, 0, out.length);
            //fop.flush();
            //fop.close();
        } catch (IOException e) {
            System.out.println("Decryption file Error: " + e + " " + file.getPath());
            System.exit(0);
        }
    }

    private byte[] convertIntToByte(int value) {
        return ByteBuffer.allocate(4).putInt(value).array();
    }

    private Signature signCipherText() {
        ArrayList<CipherText> c = retrieveCipherTextFromFile();
        PrivateKey privateKey = retrievePrivateKeyFromFile();
        long hash = polyHash(privateKey.getP(), c, (long) c.size());
        Signature sig = sign(privateKey, hash);
        return sig;
    }

    public void cryptoHash() {
        PrivateKey privateKey = retrievePrivateKeyFromFile();
        ArrayList<CipherText> c = retrieveCipherTextFromFile();
        long hash = polyHash(privateKey.getP(), c, (long) c.size());
        System.out.println("CryptoHash value = " + hash);
    }

    private boolean verifySignature() {
        ArrayList<CipherText> c = retrieveCipherTextFromFile();
        Signature sig = retrieveSignatureFromFile();
        PublicKey publicKey = retrievePublicKeyFromFile();
        System.out.println(sig.toString());
        long hash = polyHash(publicKey.getP(), c, (long) c.size());
        System.out.println("Hash : " + hash);
        boolean result = verifyHash(publicKey, sig, hash);
        return result;
    }

    public void writeSignatureToFile() {
        Signature sig = signCipherText();
        try (ObjectOutputStream oos
                = new ObjectOutputStream(new FileOutputStream("./Signature.sig"))) {
            oos.writeObject(sig);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Signature retrieveSignatureFromFile() {
        Signature sig = null;
        try (ObjectInputStream ois
                = new ObjectInputStream(new FileInputStream("./Signature.sig"))) {
            sig = (Signature) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sig;
    }

    public ArrayList<CipherText> retrieveCipherTextFromFile() {
        ArrayList<CipherText> c = null;
        try (ObjectInputStream ois
                = new ObjectInputStream(new FileInputStream("./CipherText.ct"))) {
            c = (ArrayList<CipherText>) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return c;
    }

    private PrivateKey generatePrivateKey() {
        long p = generateP();
        long g = genGenerator(p);
        long u = (long) (Math.random() * (p - 2)) + 2;
        long y = fastExponential(g, u, p);
        return new PrivateKey(p, g, y, u);
    }

    public void writePrivateKeyToFile() {
        PrivateKey privateKey = generatePrivateKey();
        try (ObjectOutputStream oos
                = new ObjectOutputStream(new FileOutputStream("./Private.key"))) {
            oos.writeObject(privateKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void writePublicKeyToFile() {
        PrivateKey privateKey = retrievePrivateKeyFromFile();
        PublicKey publicKey = privateKey.getPublicKey();
        try (ObjectOutputStream oos
                = new ObjectOutputStream(new FileOutputStream("./Public.key"))) {
            oos.writeObject(publicKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public PrivateKey retrievePrivateKeyFromFile() {
        PrivateKey privateKey = null;
        try (ObjectInputStream ois
                = new ObjectInputStream(new FileInputStream("./Private.key"))) {
            privateKey = (PrivateKey) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return privateKey;
    }

    public PublicKey retrievePublicKeyFromFile() {
        PublicKey publicKey = null;
        try (ObjectInputStream ois
                = new ObjectInputStream(new FileInputStream("./Public.key"))) {
            publicKey = (PublicKey) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return publicKey;
    }

    private long log2of(long number) {
        return (long) (Math.log10(number) / Math.log10(2));
    }

    private long getNextBitsFromFile(long length, FileInputStream fileInputStream) throws IOException {
        long input = 0;
        while (input == 0) {
            input = fileInputStream.read();
        }
        for (long numberOfByteNeeded = (length / 8); numberOfByteNeeded > 0; numberOfByteNeeded--) {
            input = input << 8;
            input += fileInputStream.read();
        }
        long maxIntOfBitLength = (long) Math.pow(2, length);
        while (input > maxIntOfBitLength) {
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

    private long fastExponential(long base, long power, long mod) {
        long increasingPower = 1;
        long value = base;
        long answer = 1;
        Map<Long, Long> map = new HashMap();

        while (increasingPower <= power) {
            map.put(increasingPower, value);
            value = (value * value) % mod;
            increasingPower *= 2;
        }

        long biggestPower = increasingPower;
        while (power > 0) {
            while (biggestPower > power) {
                biggestPower /= 2;
            }
            answer *= map.get(biggestPower);
            answer %= mod;
            power -= biggestPower;
        }
        return answer;
    }

    private long inverseFromEuclidExtended(long number, long mod) {
        long n1 = mod;
        long n2 = number % mod;
        long a1 = 1, b1 = 0, a2 = 0, b2 = 1;
        long r, q, temp;

        r = n1 % n2;
        q = n1 / n2;

        while (r != 0) {
            n1 = n2;
            n2 = r;
            temp = a2;
            a2 = a1 - (q * a2);
            a1 = temp;
            temp = b2;
            b2 = b1 - (q * b2);
            b1 = temp;

            r = n1 % n2;
            q = n1 / n2;
        }
        return b2;
    }

    private long gcdFromEuclidExtended(long number, long mod) {
        long n1 = mod;
        long n2 = number % mod;;
        long a1 = 1, b1 = 0, a2 = 0, b2 = 1;
        long r, q, temp;

        if (n2 == 0) {
            return mod;
        }

        r = n1 % n2;
        q = n1 / n2;

        while (r != 0) {
            n1 = n2;
            n2 = r;
            temp = a2;
            a2 = a1 - (q * a2);
            a1 = temp;
            temp = b2;
            b2 = b1 - (q * b2);
            b1 = temp;

            r = n1 % n2;
            q = n1 / n2;
        }
        return n2;
    }

    private long genGenerator(long p) {
        long generator = (long) (Math.random() * (p - 1)) + 1;
        while (fastExponential(generator, (p - 1) / 2, p) == 1) {
            generator = (long) (Math.random() * (p - 1)) + 1;
        }
        return generator;
    }

    private Signature sign(PrivateKey sk, long hashValue) {
        long p = sk.getP();
        long k = randomK(p);
        long r = fastExponential(sk.getG(), k, p);

        long kInverse = inverseFromEuclidExtended(k, p - 1);

        // k^(-1) (X-xr) mod (p-1)
        long s = (kInverse * ((hashValue - ((sk.getU() * r) % (p - 1))) % (p - 1))) % (p - 1);

        return new Signature(r, s);
    }

    private long randomK(long p) {
        while (true) {
            long k = (long) (Math.random() * (p - 1) + 1);
            if (gcdFromEuclidExtended(k, p - 1) == 1) {
                return k;
            }
        }
    }

    private boolean verifyHash(PublicKey pk, Signature sign, long hashValue) {
        long g = pk.getG();
        long p = pk.getP();
        long y = pk.getY();

        long r = sign.getR();
        long s = sign.getS();
        if (fastExponential(g, hashValue, p) == ((fastExponential(y, r, p) * fastExponential(r, s, p)) % p)) {
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
        return hi % p;
    }
}
