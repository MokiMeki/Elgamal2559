/**
 * Created by snow_ on 06-Dec-16.
 */
public class Main {
    public static void main(String[] args){
        System.out.println("Starting Elgamal2559");
        Elgamal2559 cryptoEngine = new Elgamal2559();
//        System.out.println("FastExpoTest: "+cryptoEngine.fastExponential(1000000,1000000,2000001));
//        System.out.println("InverseFromExtendedEuclidTest: "+cryptoEngine.inverseFromEuclidExtended(39,11));
//        System.out.println("GCDFromExtendedEuclidTest: "+cryptoEngine.gcdFromEuclidExtended(2074000000,2000001000));
//        System.out.println("GeneratorTest: "+cryptoEngine.genGenerator(391287787));
//        System.out.println("Generate P Test " + cryptoEngine.generateP());
        PrivateKey privateKey = cryptoEngine.generatePrivateKey();
        System.out.println(privateKey);
    }
}
