package smart.blink.com.card.Tool;

/**
 * Created by Ruanjiahui on 2016/12/14.
 */
public class Checksum {

    /**
     * 检测检验码
     * @param b
     * @param size
     * @return
     */
    public static int ckecksum(byte[] b, int size) {
        int result = 0;
        for (int i = 0; i < size; i++) {
            result += Math.abs((int) b[i]);
            result %= 100;
        }
        return result;
    }
}
