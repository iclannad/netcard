package smart.blink.com.card.Tool;


import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import smart.blink.com.card.API.BlinkLog;
import smart.blink.com.card.API.Protocol;
import smart.blink.com.card.bean.UploadReq;

/**
 * Created by Ruanjiahui on 2016/11/29.
 * <p/>
 * <p/>
 * 云网卡SDK 发送数据格式拼接 工具类
 */
public class SendTools {

    private static final String TAG = SendTools.class.getSimpleName();

    /**
     * 发送反馈的的命令
     *
     * @param username
     * @param usercontent
     * @return
     */
    public static byte[] FEEDBACK(String username, String usercontent) {
        byte[] msg = new byte[308];
        // CMD
        msg[0] = Protocol.FEEDBACK;
        msg[1] = 0;
        msg[2] = 0;
        msg[3] = 0;

        byte[] tcontacts = new byte[0];
        try {
            tcontacts = username.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < tcontacts.length; i++) {
            Arrays.fill(msg, i + 4, i + 5, tcontacts[i]);
        }
        byte[] bmsg = new byte[0];
        try {
            bmsg = usercontent.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < bmsg.length; i++) {
            Arrays.fill(msg, i + 52, i + 53, bmsg[i]);
        }
        return msg;
    }

    /**
     * 获取外网IP和内网IP的命令
     *
     * @param id       用户ID
     * @param password 用户密码
     * @return
     */
    public static byte[] WANT(String id, String password) {
//        byte[] msg = new byte[68];
//        int sum = 0;
//        //CMD
//        msg[0] = Protocol.WANT;
//        msg[1] = 0;
//        msg[2] = 0;
//        msg[3] = 0;
//        //UID :
//        byte[] b = id.getBytes();
//        for (int i = 0; i < b.length; i++) {
//            Arrays.fill(msg, 4 + i, 5 + i, b[i]);
//        }
//        sum = b.length + 4;
//        if (sum >= 68) {
//            BlinkLog.Error("输入的长度过长");
//            return null;
//        }
//        b = password.getBytes();
//        for (int i = 0; i < b.length; i++) {
//            Arrays.fill(msg, 52 + i, 53 + i, b[i]);
//        }
//        sum += b.length;
//        if (sum >= 68) {
//            BlinkLog.Error("输入的长度过长");
//            return null;
//        }
//        return msg;

        byte[] msg = new byte[68];
        int sum = 0;
        //CMD
        msg[0] = Protocol.WANT;
        msg[1] = 0;
        msg[2] = 0;
        msg[3] = 0;
        //UID :
        byte[] b = id.getBytes();
        for (int i = 0; i < b.length; i++) {
            // ?
            Arrays.fill(msg, 4 + i, 5 + i, b[i]);
        }
        sum = b.length + 4;
        if (sum >= 68) {
            Log.d("run", "error-----------" + sum);

            return null;
        }
        b = password.getBytes();
        for (int i = 0; i < b.length; i++) {
            Arrays.fill(msg, 52 + i, 53 + i, b[i]);
        }
        sum += b.length;
        if (sum >= 68) {
            Log.d("run", "error-----------" + sum);

            return null;
        }
        BlinkLog.Print("心跳包: " + Arrays.toString(msg));

        return msg;
    }

    /**
     * 封装打洞的数据
     *
     * @return
     */
    public static byte[] HELLO() {
        byte[] msg = new byte[4];
        msg[0] = Protocol.HELLO;
        msg[1] = 0;
        msg[2] = 0;
        msg[3] = 0;
        return msg;
    }


    /**
     * 定时关机
     *
     * @param msg
     * @return
     */
    public static byte[] Shutdown(String msg) {
        byte[] restart = new byte[8];
        restart[0] = Protocol.Shutdown;
        restart[1] = 0;
        restart[2] = 0;
        restart[3] = 0;
        int time = Integer.valueOf(msg);
        byte[] b = intToByteArray(time);
        for (int i = 0; i < b.length; i++) {
            Arrays.fill(restart, i + 4, i + 5, b[i]);
        }
        return restart;
    }


    /**
     * 重启关机
     *
     * @param msg
     * @return
     */
    public static byte[] Restart(String msg) {
        byte[] restart = new byte[8];
        restart[0] = Protocol.Restart;
        restart[1] = 0;
        restart[2] = 0;
        restart[3] = 0;
        int time = Integer.valueOf(msg);
        byte[] b = intToByteArray(time);
        for (int i = 0; i < b.length; i++) {
            Arrays.fill(restart, i + 4, i + 5, b[i]);
        }
        return restart;
    }

    /**
     * 发心跳包
     *
     * @return
     */
    public static byte[] Heart() {
        byte[] msg = new byte[4];
        msg[0] = Protocol.Heart;
        msg[1] = 0;
        msg[2] = 0;
        msg[3] = 0;

        return msg;
    }


    /**
     * 锁频
     *
     * @return
     */
    public static byte[] LOOKPC() {
        byte[] restart = new byte[4];
        restart[0] = Protocol.LOOKPC;
        restart[1] = 0;
        restart[2] = 0;
        restart[3] = 0;
        return restart;
    }

    /**
     * 修改登录密码
     *
     * @param id
     * @param oldPassword
     * @param newPassword
     * @return
     */
    public static byte[] ChangePwd(String id, String oldPassword, String newPassword) {
        byte[] data = new byte[84];
        data[0] = Protocol.ChangePwd;
        byte[] uid_bytes = id.getBytes();
        for (int i = 0; i < uid_bytes.length; i++) {
            Arrays.fill(data, 4 + i, 5 + i, uid_bytes[i]);
        }
        byte[] pass_original_bytes = oldPassword.getBytes();
        for (int i = 0; i < pass_original_bytes.length; i++) {
            Arrays.fill(data, 52 + i, 53 + i, pass_original_bytes[i]);
        }
        byte[] pass_new_bytes = newPassword.getBytes();
        for (int i = 0; i < pass_new_bytes.length; i++) {
            Arrays.fill(data, 68 + i, 69 + i, pass_new_bytes[i]);
        }
        return data;
    }

    /**
     * 获取上传路径
     *
     * @return
     */
    public static byte[] GetUploadDir() {
        byte[] restart = new byte[4];
        restart[0] = Protocol.GetUploadDir;
        restart[1] = 0;
        restart[2] = 0;
        restart[3] = 0;
        return restart;
    }

    /**
     * 设置上传路径
     *
     * @param path
     * @return
     */
    public static byte[] SetUploadDir(String path) {
        byte[] restart = new byte[500];
        restart[0] = Protocol.SetUploadDir;
        restart[1] = 0;
        restart[2] = 0;
        restart[3] = 0;

        for (int i = 0; i < path.getBytes().length; i++)
            restart[i + 4] = path.getBytes()[i];

        return restart;
    }

    /**
     * 修改PC的登录密码 我自己封装，有udp和tcp，两种，我先只测试udp的
     *
     * @param newPassword
     * @return
     */
    public static byte[] ChangePcPwd(String oldPassworld, String newPassword) {
        byte[] restart = new byte[104];
        restart[0] = (byte) 31;
        restart[1] = 0;
        restart[2] = 0;
        restart[3] = 0;

        byte[] bnewpw = newPassword.getBytes();
        for (int i = 0; i < bnewpw.length; i++) {
            Arrays.fill(restart, i + 4, i + 5, bnewpw[i]);
        }
        byte[] boldpw = oldPassworld.getBytes();
        for (int i = 0; i < boldpw.length; i++) {
            Arrays.fill(restart, i + 54, i + 55, boldpw[i]);
        }

        return restart;
    }

    /**
     * 修改PC的登录密码
     *
     * @param newPassword
     * @return
     */
    public static byte[] ChangePcPwd(String newPassword) {
        byte[] restart = new byte[500];
        restart[0] = Protocol.ChangePcPwd;
        restart[1] = 0;
        restart[2] = 0;
        restart[3] = 0;

        for (int i = 0; i < newPassword.getBytes().length; i++)
            restart[i + 4] = newPassword.getBytes()[i];

        return restart;
    }

    /**
     * 查看文件
     *
     * @param path
     * @return
     */
    public static byte[] LookFileMsg(String path) {
        byte[] restart = new byte[500];
        restart[0] = Protocol.LookFileMsg;
        restart[1] = 0;
        restart[2] = 0;
        restart[3] = 0;

        for (int i = 0; i < path.getBytes().length; i++)
            restart[i + 4] = path.getBytes()[i];

        return restart;
    }


    /**
     * 申请子服务器
     *
     * @return
     */
    public static byte[] RelayMsg() {
        byte[] restart = new byte[4];
        restart[0] = Protocol.RelayMsg;
        restart[1] = 0;
        restart[2] = 0;
        restart[3] = 0;

        return restart;
    }

    /**
     * 申请子服务器与PC通讯
     *
     * @param uuid
     * @return
     */
    public static byte[] ConnectPc(byte[] uuid) {
        byte[] restart = new byte[264];
        restart[0] = Protocol.CONNECT_SERVER;
        restart[1] = 0;
        restart[2] = 0;
        restart[3] = 0;
        restart[4] = Protocol.mobileCmdSock;
        restart[5] = 0;
        restart[6] = 0;
        restart[7] = 0;

        for (int i = 0; i < uuid.length; i++) {
            restart[i + 8] = uuid[i];
        }

        return restart;
    }

    /**
     * 开始下载的指令
     *
     * @param path
     * @return
     */
    public static byte[] DownloadStart(String path) {
        byte[] restart = new byte[path.getBytes().length + 3];
        restart[0] = Protocol.DownloadStart;
        restart[1] = 0x00;
        for (int i = 0; i < path.getBytes().length; i++)
            restart[i + 2] = path.getBytes()[i];
        restart[restart.length - 1] = 0x00;

        return restart;
    }

    /**
     * 开始下载的指令
     *
     * @param path
     * @return
     */
    public static byte[] DownloadStartBySubServer(String path) {
        byte[] res = new byte[260];
        byte[] cmd = new byte[4];
        cmd[0] = 9;
        cmd[1] = cmd[2] = cmd[3];
        for (int i = 0; i < 4; i++) {
            Arrays.fill(res, i, i + 1, cmd[i]);
        }
        byte[] sfile = path.getBytes();
        for (int i = 0; i < sfile.length; i++) {
            Arrays.fill(res, i + 4, i + 5, sfile[i]);
        }
        return res;
    }

    /**
     * 下载中的指令
     *
     * @param filename
     * @return
     */
    public static byte[] Downloading(int wantblock, String filename) {

        byte[] restart = new byte[5 + filename.getBytes().length];
        restart[0] = Protocol.Downloading;
        restart[1] = 0x00;
        for (int i = 0; i < filename.getBytes().length; i++)
            restart[i + 2] = filename.getBytes()[i];
        restart[2 + filename.getBytes().length] = 0x00;
        restart[3 + filename.getBytes().length] = (byte) wantblock;
        restart[4 + filename.getBytes().length] = 0x00;

        return restart;
    }

    /**
     * 下载中的指令 TCP 旧版的请求方法
     *
     * @param filename
     * @return
     */
    public static byte[] DownloadingOldVersion(int wantblock, String filename) {

        Log.e(TAG, "DownloadingOldVersion: want==" + wantblock + " filename==" + filename);

        byte[] head = new byte[4];
        head[0] = 71;                // 这里为了方便先这么写
        head[1] = head[2] = head[3] = 0;
        byte[] blockid = new byte[4];
        int i_blockid = wantblock;  // 要下载的块数
        blockid = DataConverter.intToByteArray(i_blockid);
        byte[] filename_old = new byte[256];
        byte[] temp = filename.getBytes();
        for (int i = 0; i < temp.length; i++) {
            filename_old[i] = temp[i];
        }
        for (int j = temp.length; j < filename_old.length; j++) {
            filename_old[j] = 0;
        }
        byte[] msg = new byte[4 + 4 + 256];
        for (int i = 0; i < 4; i++) {
            msg[i] = head[i];
        }
        for (int i = 4; i < 8; i++)
            msg[i] = blockid[i - 4];
        for (int i = 8; i < 264; i++)
            msg[i] = filename_old[i - 8];
        Log.d("----kuai", "i_blockid==" + i_blockid);
        return msg;
    }


    /**
     * 下载中的指令
     *
     * @return
     */
    public static byte[] Downloading() {
        byte[] restart = new byte[2];
        restart[0] = Protocol.Downloading1;
        restart[1] = 0x00;
        return restart;
    }


    /**
     * 下载结束发送的东西
     *
     * @param filename
     * @return
     */
    public static byte[] DownloadEnd(String filename) {
        byte[] restart = new byte[300];
        restart[0] = Protocol.DownloadEnd;
        restart[1] = 0;
        restart[2] = 0;
        restart[3] = 0;

        for (int r = 0; r < filename.getBytes().length; r++)
            restart[4 + r] = filename.getBytes()[r];

        return restart;
    }

    /**
     * 开始上传 有作修改，请注意
     *
     * @param filename 文件名
     * @return
     */
    public static byte[] UploadStart(String filePath, String filename) {
        Log.e(TAG, "UploadStart: " + filename);
//        byte[] load = new byte[filename.getBytes().length + 3];
//
//        load[0] = Protocol.UploadStart;
//        load[1] = 0x00;
//        for (int i = 0; i < filename.getBytes().length; i++) {
//            load[2 + i] = filename.getBytes()[i];
//        }
//        load[load.length - 1] = 0x00;
//
//        return load;

        // TODO: 2017/3/29
        File file = new File(filePath, filename);
        int size = 1024 * 1024 * 10;
        long length = file.length();
        int totalBlock = 0;
        totalBlock = (int) (length / size);
        if (length % size != 0) {
            totalBlock += 1;
        }

        Log.e(TAG, "UploadStart: " + file.getName() + "---" + file.length());
        Log.e(TAG, "UploadStart: totalBlock值为：" + totalBlock);

        byte[] load = new byte[filename.getBytes().length + 5];

        load[0] = Protocol.UploadStart;
        load[1] = 0x00;
        for (int i = 0; i < filename.getBytes().length; i++) {
            load[2 + i] = filename.getBytes()[i];
        }
        load[load.length - 3] = 0x00;
        load[load.length - 2] = (byte) totalBlock;     // 先用小文件数据上传试试,查看返回的结果对不对
        load[load.length - 1] = 0x00;

        return load;
    }

    /**
     * 发送上传数据的方法
     *
     * @param wantblock 请求上传的线程数
     * @param filename  请求上传的文件名称
     * @return
     */
    public static byte[] Uploading(int wantblock, String filename) {
        byte[] upload = new byte[5 + filename.getBytes().length];

        upload[0] = Protocol.Uploading;
        upload[1] = 0x00;
        for (int i = 0; i < filename.getBytes().length; i++) {
            upload[i + 2] = filename.getBytes()[i];
        }
        upload[2 + filename.getBytes().length] = 0x00;
        upload[3 + filename.getBytes().length] = (byte) wantblock;
        upload[4 + filename.getBytes().length] = 0x00;

        return upload;
    }

    /**
     * 拼装上传的数据
     *
     * @param uploadReq
     * @return
     */
    public static byte[] Uploading(UploadReq uploadReq) {
        byte[] msg = uploadReq.getData();
        int len = uploadReq.getBlockLength();
        byte[] upload = new byte[376 + len];

        upload[0] = Protocol.Uploading1;
        upload[1] = 0x00;
        upload[2] = (byte) ckecksum(msg, len);
        upload[3] = 0x00;
        String length = String.valueOf(len);
        for (int i = 0; i < length.length(); i++) {
            upload[4 + i] = (byte) length.charAt(i);
        }
        upload[5 + length.length()] = 0x00;
        for (int i = 0; i < len; i++) {
            upload[376 + i] = msg[i];
        }
        return upload;
    }

    /**
     * 拼装上传的数据  根据旧版的方法改过来
     *
     * @param reqBlockId 请求上传的块数
     * @param wantblock  总块数
     * @param filename   请求文件的名字
     * @param file
     */
    public static byte[] UploadingOldVersion(int reqBlockId, long wantblock, String filename, File file) {
        byte[] msg_file_name = new byte[1300];
        //CMD
        msg_file_name[0] = 79;
        msg_file_name[1] = 0;
        msg_file_name[2] = 0;
        msg_file_name[3] = 0;

        //block_id
        byte[] b = DataConverter.intToByteArray(reqBlockId);
        for (int i = 0; i < b.length; i++) {
            Arrays.fill(msg_file_name, i + 4, i + 5, b[i]);
        }

        //total_block
        int mTotalBlock = (int) wantblock;
        byte[] bt = DataConverter.intToByteArray(mTotalBlock);
        for (int i = 0; i < bt.length; i++) {
            Arrays.fill(msg_file_name, i + 8, i + 9, bt[i]);
        }

        byte[] data = readFile(reqBlockId,file);
        //block_length
        byte[] blen = new byte[4];
        blen = DataConverter.intToByteArray(blockidlen);
        for (int i = 0; i < blen.length; i++) {
            Arrays.fill(msg_file_name, i + 12, i + 13, blen[i]);
        }
        byte[] temp = filename.getBytes();
        for (int i = 0; i < temp.length; i++) {
            Arrays.fill(msg_file_name, i + 16, i + 17, temp[i]);
        }

        //data
        for (int i = 0; i < data.length; i++) {
            Arrays.fill(msg_file_name, i + 272, i + 273, data[i]);
        }

        //checksum
        byte[] cksum = DataConverter.intToByteArray(ckecksum(msg_file_name, msg_file_name.length - 4));
        for (int i = 0; i < cksum.length; i++) {
            Arrays.fill(msg_file_name, i + 1296, i + 1297, cksum[i]);
        }

        return msg_file_name;
    }


    private static int blockidlen = 0;

    private static byte[] readFile(int index, File file) {
        try {
            RandomAccessFile raf = new RandomAccessFile(file, "rw");
            raf.seek(index * 1024);
            byte[] data = new byte[1024];
            blockidlen = raf.read(data);
            if (blockidlen == -1)
                blockidlen = 0;
            raf.close();
            return data;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 发送结束的指令
     *
     * @return
     */
    public static byte[] UploadClose() {
        byte[] upload = new byte[2];

        upload[0] = Protocol.UploadEnd;
        upload[1] = 0x00;

        return upload;
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

    /**
     * 合校验算法
     *
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

    /**
     * 分步读取文件的字节
     *
     * @param file
     * @param uploadReq
     * @return
     */
    public static UploadReq readFile(File file, UploadReq uploadReq) {
        int blockidlen;
        try {
            RandomAccessFile raf = new RandomAccessFile(file, "rw");
            raf.seek(uploadReq.getBlockID() * 1024);
            byte[] data = new byte[1024];
            blockidlen = raf.read(data);
            if (blockidlen == -1)
                blockidlen = 0;
            raf.close();

            uploadReq.setData(data);
            uploadReq.setBlockSize(blockidlen);
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        }

        return uploadReq;
    }
}
