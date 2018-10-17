
package com.carlt.chelepie.ftp;

import android.util.Log;


import com.carlt.chelepie.data.recorder.PieInfo;
import com.carlt.doride.systemconfig.URLConfig;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class FtpConnector {

    // 未知错误
    public final static int CODE_ERRO = 0;

    // 连接FTP错误
    public final static int CODE_UNARRIVE = 1;

    // 登录FTP错误
    public final static int CODE_IDENTITY = 2;

    // 登录FTP成功
    public final static int CODE_SUCCESS = 200;

    // FTP连接.
    private FTPClient ftpClient;

    public FtpConnector() {
        ftpClient = new FTPClient();
        ftpClient.setConnectTimeout(5 * 1000);
    }

    /**
     * 下载单个文件，可实现断点下载.
     * 
     * @param serverPath Ftp目录及文件路径
     * @param startPos 断点续传起始点
     */
    public InputStream downloadFile(String serverPath, long startPos) {
        InputStream input = null;

        try {
            // 先判断服务器文件是否存在
        	Log.e("info", "FtpConnector_downloadFile_serverPath=="+serverPath);
            FTPFile[] files = ftpClient.listFiles(serverPath);
            if (files.length == 0) {
                Log.e("info", "Ftp文件不存在_down==" + serverPath);
                return input;
            }
            ftpClient.setRestartOffset(startPos);
            input = ftpClient.retrieveFileStream(serverPath);
        } catch (Exception e) {
            Log.e("info", "Ftp下载出错==" + e);
        }

        return input;

    }

    /**
     * 获取文件大小
     * 
     * @param serverPath
     */
    public long getFileSize(String serverPath) {
        long serverSize = -1;
        try {
            // 先判断服务器文件是否存在

            FTPFile[] files = ftpClient.listFiles(serverPath);
            serverSize = files[0].getSize(); // 获取远程文件的长度
        } catch (Exception e) {
            Log.e("info", "Ftp获取文件大小出错==" + e);
        }

        return serverSize;

    }

    public FTPFile[] getFile(String serverPath) {
        FTPFile[] files = null;
        try {
            // 先判断服务器文件是否存在
            Log.e("info", "FtpCpnnector_serverPath==" + serverPath);
            files = ftpClient.listFiles(serverPath);
            if (files.length == 0) {
                Log.e("info", "Ftp文件不存在_getFile==" + serverPath);
                return null;
            }
        } catch (IOException e) {
            Log.e("info", "Ftp获取出错==" + e);
        }

        return files;

    }

    // 上传文件
    public int uploadFtp(String filePath, String pieDirectory) {
        int result = -1;
        File mFile = new File(filePath);
        Log.e("info", "ftp上传_filePath=="+mFile);
        Log.e("info", "ftp上传_pieDirectory=="+pieDirectory);
        String fileName = mFile.getName();
        FileInputStream fis = null;
        // 创建输入流
        try {
            // 创建目录
            ftpClient.makeDirectory(pieDirectory);
            // 改变FTP目录
            ftpClient.changeWorkingDirectory(pieDirectory);
            // 缓冲区
            ftpClient.setBufferSize(1024);
            // 被动模式
            ftpClient.enterLocalPassiveMode();

            fis = new FileInputStream(mFile);
            ftpClient.storeFile(fileName, fis);

            result = CODE_SUCCESS;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.e("info", "ftp上传失败==" + e);
            result = CODE_ERRO;
        }
        return result;
    }

    // -------------------------------------------------------打开关闭连接------------------------------------------------

    /**
     * 打开FTP服务.
     */
    public int openConnect() {
        int code = -1; // 服务器响应值
        if (ftpClient.isConnected()) {
            return CODE_SUCCESS;
        }
        // 中文转码
        ftpClient.setControlEncoding("UTF-8");

        try {
            // 连接至服务器
            ftpClient.connect(URLConfig.host, URLConfig.FtpPort);
            code = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(code)) {
                // 断开连接
                ftpClient.disconnect();
                Log.e("info", "ftp连接失败==" + code);
                return CODE_UNARRIVE;

            }
            // 登录到服务器
            // 用户名.
            String userName = "admin";
            // userName="pieftp";
            // 密码.
            String password = PieInfo.getInstance().getDeviceName();
            Log.e("info", "ftp-userName==" + userName);
            password = "0010012345678910";
            // password="ChelerPie4657";
            Log.e("info", "ftp-password==" + password);

            ftpClient.login(userName, password);
            code = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(code)) {
                // 断开连接
                ftpClient.disconnect();
                Log.e("info", "ftp身份验证失败==" + code);
                return CODE_IDENTITY;
            } else {
                // 获取登录信息
                FTPClientConfig config = new FTPClientConfig(
                        ftpClient.getSystemType().split(" ")[0]);
                config.setServerLanguageCode("zh");
                ftpClient.configure(config);
                // 使用被动模式设为默认
                ftpClient.enterLocalPassiveMode();
                // 二进制文件支持
                ftpClient.setFileType(org.apache.commons.net.ftp.FTP.BINARY_FILE_TYPE);
                Log.e("info", "ftp连接成功==" + code);
                return CODE_SUCCESS;
            }

        } catch (Exception e) {
            Log.e("info", "ftp错误==" + e);
            return CODE_ERRO;
        }

    }

    /**
     * 关闭FTP服务.
     * 
     * @throws IOException
     */
    public void closeConnect() {
        if (ftpClient != null) {
            try {
                // 退出FTP
                ftpClient.logout();
                // 断开连接
                ftpClient.disconnect();
            } catch (IOException e) {
                Log.e("info", "ftp断开错误==" + e);
            }

        }
    }

    public void logout() {
        if (ftpClient != null) {
            try {
                // 退出FTP
                ftpClient.logout();
            } catch (IOException e) {
                Log.e("info", "ftp断开错误==" + e);
            }

        }
    }

}
