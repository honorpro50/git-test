import java.math.BigInteger;

public class Pack {
    public static void main(String[] args) {
        // 假设有一个打包后的值
        BigInteger packedValue = new BigInteger("83");

        // 解包
        int t = 4; // 每个整数占据的位数
        int m = 2; // 整数的个数
        BigInteger mask = BigInteger.valueOf((1 << t) - 1); // 用于获取每个 xᵢ 的掩码

        for (int i = 0; i < m; i++) {
            BigInteger x_i = packedValue.shiftRight(t * (m - i - 1)).and(mask);
            System.out.println("Unpacked x_" + (i + 1) + ": " + x_i.intValue());
        }
    }
}
