package smart.blink.com.card.Tcp.File;

import java.io.File;
import java.math.BigDecimal;

/**
 * Created by Ruanjiahui on 2016/3/14.
 * <p/>
 * 这个类是管理所以关于文件的操作类
 * <p/>
 * 比如有
 * 文本的创建，文件夹的创建，文件的读取，文件的写入，文件的判断，文件夹的判断，配置文件的读取，配置文件的写入，配置文件的创建等等
 */
public class FileTool {

    /**
     * 这个方法是创建文件
     *
     * @param path
     */
    public static void CreateFolder(String path) {
        //如果该路径存在此文件夹则不创建否则就创建
        if (!FilePath.FileFail(path)) {
            File file = new File(path);
            //创建文件夹
            file.mkdirs();
        }
    }

    /**
     * 这个方法是判断文件的是否存在进行创建和删除自动备份的功能
     *
     * @param filename
     * @param path
     */
    public static void JudgeFile(String filename, String path) {
        //如果该文件不存在则自动创建该路径
        if (!FilePath.FileFail(path + "/" + filename)) {
            CreateFolder(path);
            return;
        }
        //否则则删除原来的文件
        //为了安全默认自动备份可以手动删除
        //前面一个是初始地址，后面一个是新地址
        new File(path + "/" + filename).delete();
    }

    /**
     * 格式化单位
     *
     * @param size
     * @return
     */
    public static String getFormatSize(double size) {
        double kiloByte = size / 1024;
        if (kiloByte < 1) {
            return size + "B";
        }

        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "KB";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "MB";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "TB";
    }
}
