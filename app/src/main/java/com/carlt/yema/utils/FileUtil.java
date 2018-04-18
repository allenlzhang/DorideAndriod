
package com.carlt.yema.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;
import android.webkit.MimeTypeMap;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 作者：秋良 描述：工具类 主要包括读写文件，数据流等方法
 */
public class FileUtil {

    /**
     * 获取扩展卡路径
     */
    public static String getExCardPath() {
        return Environment.getExternalStorageDirectory().toString();
    }

    /**
     * 扩展卡是否存在
     */
    public static boolean isExCardExist() {
        return Environment.getExternalStorageDirectory().exists();
    }

    /**
     * 获取SD卡空间
     * 
     * @return
     */
    public static long getTotalExternalMemorySize() {
        File path = Environment.getExternalStorageDirectory();
        StatFs stat = new StatFs(path.getPath());

        long blockSize = stat.getBlockSize();
        long blocks = stat.getAvailableBlocks();
        long availableSpare = (blocks * blockSize);
        return availableSpare;

    }

    /**
     * 判断文件或文件夹是否存在
     * 
     * @param path
     * @return
     */
    public static boolean isExist(String path) {
        boolean exist = false;
        if (null == path) {
            return false;
        }
        try {
            File file = new File(path);
            exist = file.exists();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("FileUtil",e.getMessage());
        }
        return exist;
    }

    /**
     * 某路径是否存在，不存在则创建 返回 true: 文件夹存在，或创建成功 false: 不存在
     */
    public static boolean openOrCreatDir(String path) {
        File file = new File(path);

        file.isDirectory();

        if (false == file.exists()) {
            return file.mkdirs();
        }

        return true;
    }

    /**
     * 获取文件大小
     * 
     * @param path
     * @return
     */
    public static long getFileLength(String path) {
        long fileLength = -1;
        if (path == null || path.length() <= 0) {
            return fileLength;
        } else {
            File file = new File(path);
            try {
                if (file.exists()) {
                    FileInputStream fis = null;
                    fis = new FileInputStream(file);
                    fileLength = fis.available();
                } else {
                    Log.e("info", "文件不存在");
                }
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
        return fileLength;
    }

    /**
     * 文件重命名
     * 
     * @param oldPath
     * @param newPath
     * @return
     */
    public static File renameFile(String oldPath, String newPath) {
        File oldFile = new File(oldPath); // 要重命名的文件或文件夹
        File newFile = new File(newPath); // 重命名为
        boolean success = oldFile.renameTo(newFile); // 执行重命名
        if (success) {
            return newFile;
        } else {
            return null;
        }
    }

    /**
     * 将流写到文件
     */
    public static boolean saveToFile(InputStream inputStream, String absFileName) {
        Log.e("info", "absFileName==" + absFileName);

        final int FILESIZE = 1024 * 4;
        try {
            FileOutputStream mFileOutputStream = new FileOutputStream(absFileName);
            byte[] buffer = new byte[FILESIZE];
            int count = 0;
            while ((count = inputStream.read(buffer)) > 0) {
                mFileOutputStream.write(buffer, 0, count);
            }
            mFileOutputStream.flush();
            inputStream.close();

        } catch (Exception e) {
            Log.e("info", "e==" + e);
            e.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * String --> InputStream
     */
    public static InputStream ToStream(String str) {
        ByteArrayInputStream stream = new ByteArrayInputStream(str.getBytes());
        return stream;
    }

    /**
     * InputStream --> String
     */
    public static String ToString(InputStream is) {
        BufferedReader in = new BufferedReader(new InputStreamReader(is));
        StringBuffer buffer = new StringBuffer();
        String line = "";

        try {
            while ((line = in.readLine()) != null) {
                buffer.append(line);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return buffer.toString();
    }

    /**
     * 删除文件或是整个目录
     * 
     * @param file
     */
    public static void deleteFile(File file) {
        if (file.exists()) {
            if (file.isFile()) {
                file.delete();
            } else if (file.isDirectory()) {
                File files[] = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    deleteFile(files[i]);
                }
            }
            file.delete();
        }
    }

    /**
     * 复制文件(以超快的速度复制文件)
     * 
     * @author: suhj 2006-8-31
     * @param srcFile 源文件File
     * @param destDir 目标目录File
     * @param newFileName 新文件名
     * @return 实际复制的字节数，如果文件、目录不存在、文件为null或者发生IO异常，返回-1
     */
    public static long copyFile(File srcFile, File destDir, String newFileName) {
        long copySizes = 0;

        if (!srcFile.exists() || !destDir.exists() || null == newFileName) {
            copySizes = -1;
        }

        try {
            FileChannel fcin = new FileInputStream(srcFile).getChannel();
            FileChannel fcout = new FileOutputStream(new File(destDir, newFileName)).getChannel();

            long size = fcin.size();
            fcin.transferTo(0, fcin.size(), fcout);

            fcin.close();
            fcout.close();
            copySizes = size;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return copySizes;
    }

    /**
     * 创建空文件
     */
    public static boolean creatEmptyFile(File file) {
        if (file.length() == 0)
            return true;

        try {
            return file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();

            return false;
        }
    }

    /**
     * 将字节数转换成KB
     */
    public static String byteToKB(long bt) {
        if (bt <= 0) {
            return "0";
        }
        DecimalFormat df = (DecimalFormat) NumberFormat.getInstance();
        df.setMaximumFractionDigits(2);
        return df.format(bt / 1024.0) + "KB";
    }

    public static String byteTOString(long bt) {
        if (bt < 1024 * 1024) {
            return byteToKB(bt);
        } else {
            return byteToMB(bt);
        }

    }

    /**
     * 将字节数转换成MB
     */
    public static String byteToMB(long bt) {
        if (bt <= 0) {
            return "0";
        }
        DecimalFormat df = (DecimalFormat) NumberFormat.getInstance();
        df.setMaximumFractionDigits(2);
        return df.format(bt / (1024.0 * 1024)) + "M";
    }

    /**
     * 将字节数转换成展示的String
     */
    public static String byteToShow(long bt) {
        if (bt <= 0) {
            return "0";
        }
        double k = 1024;
        double m = 1024 * 1024;
        double g = 1024 * 1024 * 1024;

        double value;

        DecimalFormat df = new DecimalFormat("####.0");

        String show = "";
        if (bt > 0 && bt < k) {
            show = df.format(bt) + "B";
        } else if (bt >= k && bt < m) {
            value = bt / k;
            show = df.format(value) + "KB";
        } else if (bt >= m && bt < g) {
            value = bt / m;
            show = df.format(value) + "MB";
        }
        return show;
    }

    /**
     * 将字符串转成MD5值
     * 
     * @param string
     * @return
     */
    public static String stringToMD5(String string) {
        byte[] hash;

        try {
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10)
                hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }

        return hex.toString();
    }


    /**
     * 根据文件获取文件 MimeType
     *
     * @return
     */
    public static String getFileMimeType(File file) {
        String suffix = "";
        String name = file.getName();
        final int idx = name.lastIndexOf(".");
        if (idx > 0) {
            suffix = name.substring(idx + 1);
        }

        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(suffix);
    }

    /**
     * 将Bitmap 图片保存到本地路径，并返回路径
     * @param fileName 文件名称
     * @param bitmap   图片
     * @param资源类型，参照 MultimediaContentType 枚举，根据此类型，保存时可自动归类
     */
    public static String saveFile(Context c, String fileName, Bitmap bitmap) {
        return saveFile(c, "", fileName, bitmap);
    }

    public static String saveFile(Context c, String filePath, String fileName, Bitmap bitmap) {
        byte[] bytes = bitmapToBytes(bitmap);
        return saveFile(c, filePath, fileName, bytes);
    }

    public static byte[] bitmapToBytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(CompressFormat.JPEG, 100, baos);
        return baos.toByteArray();
    }

    public static String saveFile(Context c, String filePath, String fileName, byte[] bytes) {
        String fileFullName = "";
        FileOutputStream fos = null;
        String dateFolder = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA)
                .format(new Date());
        try {
            if (filePath == null || filePath.trim().length() == 0) {
                filePath = LocalConfig.mImageCacheSavePath_SD;
            }
            File file = new File(filePath);
            if (!file.exists()) {
                file.mkdirs();
            }
            File fullFile = new File(filePath, fileName );
            fileFullName = fullFile.getPath();
            fos = new FileOutputStream(new File(filePath, fileName ));
            fos.write(bytes);
        } catch (Exception e) {
            fileFullName = "";
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    fileFullName = "";
                }
            }
        }
        return fileFullName;
    }

}
