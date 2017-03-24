package blink.com.blinkcard320.Tool.Utils;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;

import blink.com.blinkcard320.Tool.Adapter.FileListAdapter.Pair;
import blink.com.blinkcard320.Tool.Protocol;

/**
 * Created by Administrator on 2017/3/23.
 */
public class FileUtils {


    public static final String GetFilePath(String path, File positionFile) {
        if (path.equals(".")) {
            Log.d("run", "yes");
            return Environment.getExternalStorageDirectory().getPath();
        } else if (path.equals("..")) {
            return positionFile.getParent();
        } else {
            return path;
        }
    }


    public static final ArrayList<Pair<String, Integer>> GetFilechild(File f, ArrayList<Pair<String, Integer>> list) {

        ArrayList<String> nlist = new ArrayList<>();
        nlist = Utils.sortFileList(f.getPath());
        list.clear();
        if (nlist == null) {
            nlist = new ArrayList<>();
            return list;
        }
        for (int i = 0; i < nlist.size(); i++) {
            Pair<String, Integer> pair = new Pair<>();
            pair.setA(nlist.get(i));
            File fA = new File(nlist.get(i));
            if (fA.isDirectory()) {
                pair.setB(Protocol.DIR);
            } else {
                pair.setB(Protocol.FL);
            }
            list.add(pair);
        }
        return list;

    }

}
