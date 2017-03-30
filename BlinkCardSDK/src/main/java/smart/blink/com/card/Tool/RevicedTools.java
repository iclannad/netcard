package smart.blink.com.card.Tool;


import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;

import smart.blink.com.card.API.BlinkLog;
import smart.blink.com.card.API.BlinkWeb;
import smart.blink.com.card.API.ErrorNo;
import smart.blink.com.card.API.Protocol;
import smart.blink.com.card.BlinkNetCardCall;
import smart.blink.com.card.Tcp.TcpSocket;
import smart.blink.com.card.Tcp.TcpUtils;
import smart.blink.com.card.Udp.UdpSocket;
import smart.blink.com.card.bean.ChangePcPwdRsp;
import smart.blink.com.card.bean.ChangePwdRsp;
import smart.blink.com.card.bean.ConnectPcRsp;
import smart.blink.com.card.bean.DownLoadStartRsp;
import smart.blink.com.card.bean.DownLoadingRsp;
import smart.blink.com.card.bean.FeedbackRsp;
import smart.blink.com.card.bean.GetUploadDirRsp;
import smart.blink.com.card.bean.HeartRsp;
import smart.blink.com.card.bean.HelloRsp;
import smart.blink.com.card.bean.LookFileRsp;
import smart.blink.com.card.bean.LookPCRsp;
import smart.blink.com.card.bean.RelayMsgRsp;
import smart.blink.com.card.bean.RestartRsp;
import smart.blink.com.card.bean.SetUploadDirRsp;
import smart.blink.com.card.bean.ShutdownRsp;
import smart.blink.com.card.bean.UploadStartReq;
import smart.blink.com.card.bean.WantRsp;

/**
 * Created by Ruanjiahui on 2016/11/29.
 * <p/>
 * <p/>
 * 解析接收数据的工具类
 */
public class RevicedTools {

    private static final String TAG = RevicedTools.class.getSimpleName();
    private static int position = 0;

    public RevicedTools(int position, byte[] buffer, int length, BlinkNetCardCall call) {
        RevicedTools.position = position;

        Log.e(TAG, "RevicedTools: " + buffer[0]);

        switch (buffer[0]) {
            //解析want 的数据
            case Protocol.WANTReviced:
                WANT(buffer, call);
                break;
            //发送hello返回的数据
            case Protocol.HELLOReviced:
                HELLO(call);
                break;
            //发送心跳返回的数据
            case Protocol.HeartReviced:
                Heart(call);
                break;
            //发送锁屏返回的数据
            case Protocol.LOOKPCReviced:
                LOOKPC(call);
                break;
            //发送重启返回的数据
            case Protocol.RestartReviced:
                Restart(call);
                break;
            //发送关机返回的数据
            case Protocol.ShutdownReviced:
                Shutdown(call);
                break;
            //修改登录密码返回的数据
            case Protocol.ChangePwdReviced:
                ChangePwd(buffer, call);
                break;
            //获取上传路径返回的数据
            case Protocol.GetUploadDirReviced:
                GetUploadDir(buffer, call);
                break;
            //设置上传路径返回的数据
            case Protocol.SetUploadDirReviced:
                SetUploadDir(call);
                break;
            //修改PC端的登录密码
            case Protocol.ChangePcPwdReviced:
                ChangePcPwd(call);
                break;
            //修改PC端的登录密码失败 (原密码错误)
            case Protocol.ChangePcPwdFair:
                ChangePcPwdErro(call);
                break;
            //查看文件
            case Protocol.LookFileMsgReviced:
                Log.e(TAG, "RevicedTools: LookFileMsgReviced");
                LookFileMsg(buffer, call);
                break;
            //开始下载的返回的数据
//            case Protocol.DownloadStartReviced:
//                DownloadStart(buffer, length, call);
//                break;
//            //下载中的返回的数据
//            case Protocol.DownloadingReviced:
//                Downloading(buffer, length , call);
//                break;
            //下载中的返回的数据
//            case Protocol.DownloadingReviced1:
//                Downloading(buffer, length, call);
//                break;
            //申请子服务器
            case Protocol.RelayMsgReviced:
                if (RevicedTools.position == Protocol.CONNECT_SERVER) {
                    ConnectPc(buffer, call);
                } else
                    RelayMsg(buffer, call);
                break;
            case Protocol.FEEDBACK:
                Log.e(TAG, "RevicedTools: " + buffer[4]);
                FEEDBACK(buffer, call);
                break;
            case Protocol.HELLO_FAILED:
                HELLO_FAILED(call);
                break;

        }
    }

    /**
     * 解析feedback 的数据
     *
     * @param buffer
     * @param call
     */
    public static void FEEDBACK(byte[] buffer, BlinkNetCardCall call) {
        FeedbackRsp feedbackRsp = new FeedbackRsp();
        if (buffer[4] == 0) {
            feedbackRsp.setSuccess(0);
            call.onSuccess(position, feedbackRsp);
        } else {
            feedbackRsp.setSuccess(1);
            call.onFail(position);
        }


    }

    /**
     * 解析want 的数据   获取内外网IP和端口
     *
     * @param buffer
     */
    public static void WANT(byte[] buffer, BlinkNetCardCall call) {
        WantRsp wantRsp = new WantRsp();
        wantRsp.setSuccess(buffer[4]);
        switch (buffer[4]) {
            case 0: {
                byte[] bb = new byte[4];
                bb = Arrays.copyOfRange(buffer, 8, 12);
                bb[0] = buffer[11];
                bb[1] = buffer[10];
                bb[2] = buffer[9];
                bb[3] = buffer[8];
                try {
                    BlinkWeb.wIP = InetAddress.getByAddress(bb).toString().substring(1);
                    BlinkWeb.wPORT = byteArrayToShort(buffer, 16);

                    BlinkLog.Error("IP:" + BlinkWeb.nIP + ",PORT:" + BlinkWeb.nPORT);
                    BlinkLog.Error("IP:" + BlinkWeb.wIP + ",PORT:" + BlinkWeb.wPORT);

                    byte[] c = new byte[4];
                    c = Arrays.copyOfRange(buffer, 12, 16);
                    c[0] = buffer[15];
                    c[1] = buffer[14];
                    c[2] = buffer[13];
                    c[3] = buffer[12];
                    BlinkWeb.nIP = InetAddress.getByAddress(c).toString().substring(1);
                    BlinkWeb.nPORT = byteArrayToShort(buffer, 18);

                    wantRsp.setnIP(BlinkWeb.nIP);
                    wantRsp.setwIP(BlinkWeb.wIP);
                    wantRsp.setnPORT(BlinkWeb.nPORT);
                    wantRsp.setwPORT(BlinkWeb.wPORT);
                } catch (UnknownHostException e) {
                    BlinkLog.Error(e.toString());
                    call.onFail(ErrorNo.ErrorIP);
                }
            }
        }
        call.onSuccess(position, wantRsp);
    }

    /**
     * 打洞失败处理
     *
     * @param call
     */
    public static void HELLO_FAILED(BlinkNetCardCall call) {

        // 打洞失败将网络访问改成TCP连接
        BlinkWeb.STATE = BlinkWeb.TCP;
        // 这里参数无所谓
        call.onFail(1);
    }

    /**
     * 打洞处理的数据
     *
     * @param call
     */
    public static void HELLO(BlinkNetCardCall call) {
        HelloRsp hellpRsp = new HelloRsp();
        hellpRsp.setSuccess(0);

        //打洞成功说明是内网访问将状态设置成UDP
        BlinkWeb.STATE = BlinkWeb.UDP;

        call.onSuccess(position, hellpRsp);
    }


    /**
     * 心跳处理的数据
     *
     * @param call
     */
    public static void Heart(BlinkNetCardCall call) {
        HeartRsp heartRsp = new HeartRsp();
        heartRsp.setSuccess(0);
        call.onSuccess(position, heartRsp);
    }


    /**
     * 锁屏处理的数据
     *
     * @param call
     */
    public static void LOOKPC(BlinkNetCardCall call) {
        LookPCRsp lookPCRsp = new LookPCRsp();
        lookPCRsp.setSuccess(0);
        call.onSuccess(position, lookPCRsp);
    }


    /**
     * 重启处理的数据
     *
     * @param call
     */
    public static void Restart(BlinkNetCardCall call) {
        RestartRsp restartRsp = new RestartRsp();
        restartRsp.setSuccess(0);
        call.onSuccess(position, restartRsp);
    }


    /**
     * 关机处理的数据
     *
     * @param call
     */
    public static void Shutdown(BlinkNetCardCall call) {
        ShutdownRsp shutdownRsp = new ShutdownRsp();
        shutdownRsp.setSuccess(0);
        call.onSuccess(position, shutdownRsp);
    }

    /**
     * 修改登录处理的数据
     *
     * @param buffer
     * @param call
     */
    public static void ChangePwd(byte[] buffer, BlinkNetCardCall call) {
        ChangePwdRsp changePwdRsp = new ChangePwdRsp();
        changePwdRsp.setSuccess(buffer[4]);
        call.onSuccess(position, changePwdRsp);
    }

    /**
     * 获取上传路径
     *
     * @param buffer
     * @param call
     */
    public static void GetUploadDir(byte[] buffer, BlinkNetCardCall call) {
        GetUploadDirRsp getUploadDirRsp = new GetUploadDirRsp();
        getUploadDirRsp.setSuccess(0);
        try {
            /**
             * 他封装的sdk有错误，我自己再修改,注释的是原来他的代码
             */
            //getUploadDirRsp.setPath(new String(buffer, 0, StringEnd(buffer), "UTF-8"));
            buffer = Arrays.copyOfRange(buffer, 4, buffer.length);
            getUploadDirRsp.setPath(new String(buffer, 0, StringEnd(buffer), "UTF-8"));
        } catch (UnsupportedEncodingException e) {

        }
        call.onSuccess(position, getUploadDirRsp);
    }

    /**
     * 设置上传路径
     *
     * @param call
     */
    public static void SetUploadDir(BlinkNetCardCall call) {
        SetUploadDirRsp setUploadDirRsp = new SetUploadDirRsp();
        setUploadDirRsp.setSuccess(0);
        call.onSuccess(position, setUploadDirRsp);
    }


    /**
     * 修改pc密码成功
     *
     * @param call
     */
    public static void ChangePcPwd(BlinkNetCardCall call) {
        ChangePcPwdRsp changePcPwdRsp = new ChangePcPwdRsp();
        changePcPwdRsp.setSuccess(0);
        call.onSuccess(position, changePcPwdRsp);
    }

    /**
     * 修改pc密码失败 原密码错误
     *
     * @param call
     */
    public static void ChangePcPwdErro(BlinkNetCardCall call) {
        ChangePcPwdRsp changePcPwdRsp = new ChangePcPwdRsp();
        changePcPwdRsp.setSuccess(1);
        call.onSuccess(position, changePcPwdRsp);
    }


    /**
     * 查看文件或者文件夹
     *
     * @param buffer
     * @param call
     */
    public static void LookFileMsg(byte[] buffer, BlinkNetCardCall call) {

        LookFileRsp lookFileRsp = new LookFileRsp();
        //1返回失败 0返回成功
//        lookFileRsp.setSuccess(1);
        //传输完毕 tcp
        lookFileRsp.setSuccess(0);

        // 先暂时这么处理，不过代码有点多,两种情况，tcp和udp
        if (BlinkWeb.STATE == BlinkWeb.TCP) {

            lookFileRsp.setProtrolList(TcpSocket.controlList);
            int control;
            ArrayList<String> list = new ArrayList<>();
            for (int r = 0; r < TcpSocket.controlList.size(); r++) {
                //获取列表的格式
                control = TcpSocket.controlList.get(r);
                //获取列表的内容
                byte[] buf = TcpSocket.bufferList.get(r);
                //获取字节数组的列表
                int length = FileEnd(buf, 8);
                switch (control) {
                    //盘符
                    case Protocol.controlPAN:
                        for (int i = 8; i < buf.length; i++) {
                            if (buf[i] == 0)
                                break;
                            list.add(String.valueOf((char) buf[i]));
                        }
                        break;
                    //文件夹
                    case Protocol.controlDIR:
                        try {
                            list.add(new String(buf, 8, length, "UTF-8"));
                        } catch (UnsupportedEncodingException e) {
                        }
                        break;
                    //文件
                    case Protocol.controlFL:
                        try {
                            list.add(new String(buf, 8, length, "UTF-8"));
                        } catch (UnsupportedEncodingException e) {
                        }
                        break;
                    //没有找到文件夹
                    case Protocol.controlNOTFOUND:
                        break;
                }
            }
            lookFileRsp.setList(list);
            call.onSuccess(position, lookFileRsp);

            //清除缓存
            TcpSocket.bufferList.clear();
            TcpSocket.controlList.clear();
        } else {
            lookFileRsp.setProtrolList(UdpSocket.controlList);
            int control;
            ArrayList<String> list = new ArrayList<>();
            for (int r = 0; r < UdpSocket.controlList.size(); r++) {
                //获取列表的格式
                control = UdpSocket.controlList.get(r);
                //获取列表的内容
                byte[] buf = UdpSocket.bufferList.get(r);
                //获取字节数组的列表
                int length = FileEnd(buf, 8);
                switch (control) {
                    //盘符
                    case Protocol.controlPAN:
                        for (int i = 8; i < buf.length; i++) {
                            if (buf[i] == 0)
                                break;
                            list.add(String.valueOf((char) buf[i]));
                        }
                        break;
                    //文件夹
                    case Protocol.controlDIR:
                        try {
                            list.add(new String(buf, 8, length, "UTF-8"));
                        } catch (UnsupportedEncodingException e) {
                        }
                        break;
                    //文件
                    case Protocol.controlFL:
                        try {
                            list.add(new String(buf, 8, length, "UTF-8"));
                        } catch (UnsupportedEncodingException e) {
                        }
                        break;
                    //没有找到文件夹
                    case Protocol.controlNOTFOUND:
                        break;
                }
            }
            lookFileRsp.setList(list);
            call.onSuccess(position, lookFileRsp);
            //清除缓存
            UdpSocket.bufferList.clear();
            UdpSocket.controlList.clear();
        }
//        } else {
//            bufferList.add(buffer);
//            controlList.add((int) buffer[4]);
//        }
    }

    /**
     * 申请获取子服务器
     *
     * @param buffer
     * @param call
     */
    public static void RelayMsg(byte[] buffer, BlinkNetCardCall call) {
        RelayMsgRsp relayMsgRsp = new RelayMsgRsp();

        byte[] b = new byte[4];
        b[3] = buffer[4];
        b[2] = buffer[5];
        b[1] = buffer[6];
        b[0] = buffer[7];
        try {
            relayMsgRsp.setIP(InetAddress.getByAddress(b).toString().substring(1));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        b = new byte[2];
        b[1] = buffer[9];
        b[0] = buffer[8];

        relayMsgRsp.setPORT(byteArrayToShort(b, 0));

        b = new byte[256];
        int n = 0;
        for (int i = 0; i < 256; i++) {
            b[i] = buffer[i + 10];
        }
        relayMsgRsp.setUUID(b);

        BlinkLog.Error("IP:" + relayMsgRsp.getIP() + "--" + "PORT:" + relayMsgRsp.getPORT() + "--uuid:" + Arrays.toString(relayMsgRsp.getUUID()));
        //将控制的状态设置为外网
        BlinkWeb.STATE = BlinkWeb.TCP;
        //设置子服务器IP和端口和uuid
        //现在这两个暂时不对外开发
        BlinkWeb.zIP = relayMsgRsp.getIP();
        BlinkWeb.zPORT = relayMsgRsp.getPORT();
        BlinkWeb.uuid = relayMsgRsp.getUUID();

        call.onSuccess(Protocol.RelayMsg, relayMsgRsp);

    }


    /**
     * 申请获取子服务器
     *
     * @param buffer
     * @param call
     */
    public static void ConnectPc(byte[] buffer, BlinkNetCardCall call) {
        ConnectPcRsp connectPcRsp = new ConnectPcRsp();
        BlinkLog.Print(buffer[4]);
        connectPcRsp.setSuccess(buffer[4]);
        call.onSuccess(position, connectPcRsp);
    }

    /**
     * 启动开始下载
     *
     * @param buffer
     * @param call
     */
    public static void DownloadStart(byte[] buffer, int length, int position, BlinkNetCardCall call) {
        DownLoadStartRsp downLoadStartRsp = new DownLoadStartRsp();
        downLoadStartRsp.setSuccess(buffer[0]);
        if (buffer[0] == Protocol.DownloadStartReviced) {
            downLoadStartRsp.setSuccess(0);

            // 文件大小
            byte[] fileSize = new byte[length];
            for (int i = 2; i < length; i++) {
                fileSize[i - 2] = buffer[i];
            }

            String Size = "";
            for (int i = 0; i < fileSize.length; i++) {
                if (fileSize[i] != 0) {
                    char c = (char) fileSize[i];
                    Size += Character.getNumericValue((int) c);
                }
            }

            downLoadStartRsp.setFilesize(Long.parseLong(Size));
            // 设置请求块的大小
            int block;
            if (Long.parseLong(Size) % 1024 == 0) {
                block = (int) Long.parseLong(Size) / 1024;
            } else {
                block = (int) Long.parseLong(Size) / 1024 + 1;
            }
            downLoadStartRsp.setTotalblock(block);
        }
        call.onSuccess(position, downLoadStartRsp);
    }

    private static DownLoadingRsp downLoadingRsp = null;

    public static void Downloading(int flag, int position, byte[] buffer, BlinkNetCardCall call) {

        if (downLoadingRsp == null)
            downLoadingRsp = new DownLoadingRsp();
        //处理现在返回来的数据
        //检验码相同说明数据正确

        //获取真实数据的长度
        byte[] len = new byte[4];
        for (int i = 4; i < 8; i++) {
            if (buffer[i] == 0x00)
                break;
            len[i - 4] = buffer[i];
        }
        //获取实际的长度
        String Size = "";
        for (int i = 0; i < len.length; i++) {
            if (len[i] != 0) {
                char c = (char) len[i];
                Size += Character.getNumericValue((int) c);
            }
        }

        //获取实际的数据
        byte[] msg = new byte[Integer.parseInt(Size)];
        for (int i = 0; i < msg.length; i++)
            msg[i] = buffer[376 + i];

        // 校验和
        if (Checksum.ckecksum(msg, msg.length) == buffer[2]) {
            downLoadingRsp.setData(msg);
            downLoadingRsp.setBlockId(position);
            downLoadingRsp.setBlockLength(Integer.parseInt(Size));

            call.onSuccess(flag, downLoadingRsp);
        } else {
            call.onFail(ErrorNo.ErrorCheck);
        }
//        } else {
//            call.onFail(flag);
//        }
    }

    /**
     * 请求上传的方法
     *
     * @param buffer   请求回来的字节数组
     * @param position 请求的标识
     * @param call
     */
    public static void UploadStart(byte[] buffer, int position, BlinkNetCardCall call) {
        UploadStartReq uploadStartReq = new UploadStartReq();

        if (buffer[0] == Protocol.UploadStartReviced)
            uploadStartReq.setSuccess(0);
        if (buffer[0] == Protocol.ReUploadStart) {
            uploadStartReq.setSuccess(Protocol.ReUploadStart);
        }
        if (buffer[0] == Protocol.ErrorUploadStart)
            uploadStartReq.setSuccess(Protocol.ErrorUploadStart);

        call.onSuccess(position, uploadStartReq);
    }


    public static void Uploading(byte[] buffer, BlinkNetCardCall call) {

    }


    /**
     * 字节数组转int
     *
     * @param b
     * @return
     */
    public static int byteArrayToInt(byte[] b) {
        int result = 0;
        for (int i = 0; i < b.length; i++) {
            result = (int) (result + ((b[i] & 0xff) << ((b.length - i - 1) * 8) & 0xFFFFFFFFL));
        }
        return result;
    }


    public static int byteArrayToShort(byte[] b, int offset) {
        int value = (b[offset + 1] & 0x00ff);
        return ((value << 8) | (b[offset] & 0x00ff));
    }


    public static int StringEnd(byte[] b) {
        int index = 0;
        for (int i = 0; i < b.length - 1; i++) {
            index = i;
            if (b[i] == 0) {
                return index;
            }
        }
        return index;
    }

    public static int FileEnd(byte[] b, int start) {
        int index = 0;
        for (int i = start; i < b.length; i++) {
            if (b[i] == 0) {
                return index;
            }
            index++;
        }
        return index;
    }
}
