package smart.blink.com.card.Tool;

/**
 * Created by Administrator on 2017/3/31.
 */
public class DataConverter {

    public static int lastNullIndex(byte[] b) {
        int index = 0;
        for (int i = 0; i < b.length - 1; i++) {
            if (b[i] != 0 && b[i] != -52) {
                index = i;
            }
        }
        return index;
    }


    public static int byteArrayToInt(byte[] b) {
        int result = 0;
        for (int i = 0; i < b.length; i++) {
            result = (int) (result + ((b[i] & 0xff) << ((b.length - i - 1) * 8) & 0xFFFFFFFFL));
        }
        return result;
    }


    public static int byteArrayToInt2(byte[] b) {
        int result = 0;
        for (int i = b.length; i >= 0; i--) {
            result = (int) (result + ((b[i] & 0xff) << ((b.length - i - 1) * 8) & 0xFFFFFFFFL));
        }
        return result;
    }

    public static long byteArray2Long(byte[] tmp) {
        byte[] b = new byte[8];
        for (int i = 0; i < 8; i++) {
            b[i] = tmp[7 - i];
        }
        long result = 0;
        for (int i = 0; i < b.length; i++) {
            result = (long) (result + ((b[i] & 0xff) << ((b.length - i - 1) * 8) & 0xFFFFFFFFL));
        }
        return result;
    }

    public static int byteArrayToIntTcp(byte[] tmp) {
        byte[] b = new byte[4];
        b[0] = tmp[3];
        b[1] = tmp[2];
        b[2] = tmp[1];
        b[3] = tmp[0];
        int result = 0;
        for (int i = 0; i < b.length; i++) {
            result = (int) (result + ((b[i] & 0xff) << ((b.length - i - 1) * 8) & 0xFFFFFFFFL));
        }
        return result;
    }

    public static byte[] intToByteArray(int a) {
        byte result[] = new byte[4];
        for (int i = 0; i < 4; i++)
            result[i] = 0;
        result[3] = (byte) ((a >> 24) & 0xff);
        result[2] = (byte) ((a >> 16) & 0xff);
        result[1] = (byte) ((a >> 8) & 0xff);
        result[0] = (byte) (a & 0xff);
        return result;
    }

    public static byte[] intToBytes2(int value) {
        byte[] src = new byte[4];
        src[0] = (byte) ((value >> 24) & 0xFF);
        src[1] = (byte) ((value >> 16) & 0xFF);
        src[2] = (byte) ((value >> 8) & 0xFF);
        src[3] = (byte) (value & 0xFF);
        return src;
    }


    public static byte[] shortToByteArray(short a) {
        byte result[] = new byte[2];
        result[1] = (byte) ((a >> 8) & 0xff);
        result[0] = (byte) (a & 0xff);
        return result;
    }


    public static int byteArrayToShort(byte[] b, int offset) {
        int value = (b[offset + 1] & 0x00ff);
        return ((value << 8) | (b[offset] & 0x00ff));
    }
}
