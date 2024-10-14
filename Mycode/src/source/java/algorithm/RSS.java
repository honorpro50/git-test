package algorithm;

import Result.RSSResult;

import java.math.BigInteger;

public class RSS {
    public BigInteger pho11, pho12, pho13;
    public BigInteger pho21, pho22, pho23;
    public BigInteger a1, a2, a3;
    public BigInteger b1, b2, b3;

    public RSSResult RSS_numa(BigInteger pho11, BigInteger pho12, BigInteger pho13){
        a1 = pho11.subtract(pho13);
        a2 = pho12.subtract(pho11);
        a3 = pho13.subtract(pho12);
        RSSResult A = new RSSResult(a1, a2, a3);

        return A;
    }
    public RSSResult RSS_numb(BigInteger pho21, BigInteger pho22, BigInteger pho23){
        b1 = pho21.subtract(pho23);
        b2 = pho22.subtract(pho21);
        b3 = pho23.subtract(pho22);
        RSSResult B = new RSSResult(b1, b2, b3);

        return B;
    }
}
