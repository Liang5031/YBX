package com.ybx.guider.utils;


import android.content.Context;
import android.net.Uri;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.kobjects.base64.Base64;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

/**
 * * 实现文件上传的工具类
 */
public class FileImageUpload {
    private static final String TAG = "uploadFile";
    private static final int TIME_OUT = 10 * 10000000; //超时时间
    private static final String CHARSET = "utf-8"; //设置编码

    /**
     * android上传文件到服务器
     */
    public static int uploadFile(Context ctx, Uri srcUri, String RequestURL, String fileName) {
        int res = 0;
        String BOUNDARY = UUID.randomUUID().toString(); //边界标识 随机生成
        String PREFIX = "--";
        String LINE_END = "\r\n";
        String CONTENT_TYPE = "multipart/form-data"; //内容类型
        try {
            URL url = new URL(RequestURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(TIME_OUT);
            conn.setConnectTimeout(TIME_OUT);
            conn.setDoInput(true); //允许输入流
            conn.setDoOutput(true); //允许输出流
            conn.setUseCaches(false); //不允许使用缓存
            conn.setRequestMethod("POST"); //请求方式
            conn.setRequestProperty("Charset", CHARSET);
            //设置编码
            conn.setRequestProperty("connection", "keep-alive");
            conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary=" + BOUNDARY);
            /** * 当文件不为空，把文件包装并且上传 */
            OutputStream outputSteam = conn.getOutputStream();
            DataOutputStream dos = new DataOutputStream(outputSteam);
            StringBuffer sb = new StringBuffer();
            sb.append(PREFIX);
            sb.append(BOUNDARY);
            sb.append(LINE_END);
            /**
             * 这里重点注意：
             * name里面的值为服务器端需要key 只有这个key 才可以得到对应的文件
             * filename是文件的名字，包含后缀名的 比如:abc.png
             */
//                sb.append("Content-Disposition: form-data; name=\"img\"; filename=\"" + file.getName() + "\"" + LINE_END);
            sb.append("Content-Disposition: form-data; name=\"photo\"; filename=\"" + fileName + "\"" + LINE_END);
            sb.append("Content-Type: application/octet-stream; charset=" + CHARSET + LINE_END);
//            sb.append("Content-Type: application/x-jpg; charset=" + CHARSET + LINE_END);
            sb.append(LINE_END);

            dos.write(sb.toString().getBytes());

//          InputStream is = new FileInputStream(file);
            InputStream is = ctx.getContentResolver().openInputStream(srcUri);

            byte[] bytes = new byte[1024];
            int len = 0;
            while ((len = is.read(bytes)) != -1) {
                dos.write(bytes, 0, len);
            }
            is.close();
            dos.write(LINE_END.getBytes());
            byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END).getBytes();
            dos.write(end_data);
            dos.flush();
            /**
             * 获取响应码 200=成功
             * 当响应成功，获取响应的流
             */
            res = conn.getResponseCode();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * 通过ftp上传文件
     *
     * @param url          ftp服务器地址 如： 192.168.1.110
     * @param port         端口如 ： 21
     * @param username     登录名
     * @param password     密码
     * @param remotePath   上到ftp服务器的磁盘路径
     * @param fileNamePath 要上传的文件路径
     * @param fileName     要上传的文件名
     * @return
     */
    public String ftpUpload(String url, String port, String username, String password, String remotePath, String fileNamePath, String fileName) {
        FTPClient ftpClient = new FTPClient();
        FileInputStream fis = null;
        String returnMessage = "0";
        try {
            ftpClient.connect(url, Integer.parseInt(port));
            boolean loginResult = ftpClient.login(username, password);
            int returnCode = ftpClient.getReplyCode();
            if (loginResult && FTPReply.isPositiveCompletion(returnCode)) {// 如果登录成功
                ftpClient.makeDirectory(remotePath);
                // 设置上传目录
                ftpClient.changeWorkingDirectory(remotePath);
                ftpClient.setBufferSize(1024);
                ftpClient.setControlEncoding("UTF-8");
                ftpClient.enterLocalPassiveMode();
                fis = new FileInputStream(fileNamePath + fileName);
                ftpClient.storeFile(fileName, fis);

                returnMessage = "1";   //上传成功
            } else {// 如果登录失败
                returnMessage = "0";
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("FTP客户端出错！", e);
        } finally {
            //IOUtils.closeQuietly(fis);
            try {
                ftpClient.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("关闭FTP连接发生异常！", e);
            }
        }
        return returnMessage;
    }


    public static boolean uploadfileByWebService() {
        return false;
    }

    private static String getUploadBuffer(Context ctx, Uri imgUri) {
        String Base64EncodedStr = "";
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        InputStream is = null;
        int len = 0;
        try {
            is = ctx.getContentResolver().openInputStream(imgUri);
            while ((len = is.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            Base64EncodedStr = new String(Base64.encode(baos.toByteArray()));
            is.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Base64EncodedStr;
    }

    public static boolean callWebService(Context ctx, Uri srcUri, String filename) {
        boolean result = false;
        // 命名空间
        String nameSpace = "http://DigitalScience.WebService";
        // 调用的方法名称
        String methodName = "UploadFileByBase64";
        // SOAP Action
        String soapAction = "http://DigitalScience.WebService/UploadFileByBase64";
        // EndPoint
        String endPoint = "http://121.40.94.228:8081/source/YUNPhoto_UploadService.asmx";

        // 指定WebService的命名空间和调用的方法名
        SoapObject rpc = new SoapObject(nameSpace, methodName);

        rpc.addProperty("GuiderID", filename);       //参数1   图片名
        rpc.addProperty("base64Data", getUploadBuffer(ctx, srcUri));
        // 生成调用WebService方法的SOAP请求信息,并指定SOAP的版本
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER10);

        envelope.bodyOut = rpc;
        // 设置是否调用的是dotNet开发的WebService
        envelope.dotNet = true;
        // 等价于envelope.bodyOut = rpc;
        envelope.setOutputSoapObject(rpc);

        HttpTransportSE transport = new HttpTransportSE(endPoint);
        try {
            // 调用WebService
            transport.call(soapAction, envelope);
            Object ret = envelope.getResponse();
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
            result = false;
        }
        return result;
    }
}