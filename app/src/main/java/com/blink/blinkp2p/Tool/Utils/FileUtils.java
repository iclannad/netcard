package com.blink.blinkp2p.Tool.Utils;

import android.os.Environment;
import android.util.Log;

import com.blink.blinkp2p.Tool.Adapter.FileListAdapter;
import com.blink.blinkp2p.Tool.Protocol;

import java.io.File;
import java.util.ArrayList;

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


    public static final ArrayList<FileListAdapter.Pair<String, Integer>> GetFilechild(File f, ArrayList<FileListAdapter.Pair<String, Integer>> list) {

        ArrayList<String> nlist = new ArrayList<>();
        nlist = Utils.sortFileList(f.getPath());
        list.clear();
        if (nlist == null) {
            nlist = new ArrayList<>();
            return list;
        }
        for (int i = 0; i < nlist.size(); i++) {
            FileListAdapter.Pair<String, Integer> pair = new FileListAdapter.Pair<>();
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
