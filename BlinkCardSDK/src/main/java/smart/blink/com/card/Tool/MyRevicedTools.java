package smart.blink.com.card.Tool;

import smart.blink.com.card.API.ErrorNo;
import smart.blink.com.card.BlinkNetCardCall;
import smart.blink.com.card.bean.DownLoadingRsp;

/**
 * Created by Administrator on 2017/4/13.
 */
public class MyRevicedTools {
    private int flag;
    private int position;
    private byte[] buffer;
    private BlinkNetCardCall call;
    DownLoadingRsp downLoadingRsp = null;

    public MyRevicedTools(int flag, int position, byte[] buffer, BlinkNetCardCall call) {
        this.flag = flag;
        this.position = position;
        this.buffer = buffer;
        this.call = call;

        handlerDownload();
    }

    public void handlerDownload() {
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
            int parseInt = 0;
            try {
                parseInt = Integer.parseInt(Size);
            } catch (NumberFormatException e) {
                parseInt = 0;
            }
            downLoadingRsp.setBlockLength(parseInt);
            //downLoadingRsp.setBlockLength(Integer.parseInt(Size));

            call.onSuccess(flag, downLoadingRsp);
        } else {
            call.onFail(ErrorNo.ErrorCheck);
        }
    }
}
