package smart.blink.com.card.Tool;

import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Administrator on 2017/3/31.
 */
public class FileWriteStream {

    public static FileWriteStream Instance = null;
    public static final int DOWNLOAD = 0;
    public static final int UPLOAD = 1;

    public static FileWriteStream getInstance() {

        if (Instance == null) {
            Instance = new FileWriteStream();
        }
        return Instance;
    }

    private static FileOutputStream fos = null;

    public static void OpenFile(String sfile) {
        File file = new File(sfile);
        try {
            fos = new FileOutputStream(file);
        } catch (FileNotFoundException e) {

            e.printStackTrace();
        }
    }

    public static void CloseFile() {
        try {
            if (fos != null)
                fos.close();
        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    private static byte[][] data = new byte[100][1024];

    public static void writebigblockfile(int id, byte[] tdata) {

        try {
            data[id] = tdata;
            if (id == 99) {
                for (int i = 0; i < 100; i++) {
                    fos.write(data[i]);
                }
                fos.flush();
            }
        } catch (Exception e) {


        }
    }

    public static void writebigblockfileEnd(int id, byte[] tdata) {

        try {
            data[id] = tdata;
            for (int i = 0; i <= id; i++) {

                fos.write(data[i]);
            }
            fos.flush();

        } catch (Exception e) {

        }
    }

    public static void writefile(byte[] data) {

        try {
            fos.write(data);
            fos.flush();
        } catch (FileNotFoundException e) {

            e.printStackTrace();
        } catch (IOException e) {

            e.printStackTrace();
        }

    }

}
