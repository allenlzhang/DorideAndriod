
package com.carlt.sesame.utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ObjectIO {
    /*
     * 向指定目录写一个Object
     */
    public static boolean saveObject(Object object, String absFileName) {
        if (null == object || null == absFileName || absFileName.length() <= 0) {
            return false;
        }

        FileOutputStream os = null;
        ObjectOutputStream oos = null;

        try {
            os = new FileOutputStream(absFileName);

            oos = new ObjectOutputStream(os);
            oos.writeObject(object);

            oos.close();
            os.close();
        } catch (FileNotFoundException e) {
            Log.e("info", "e11==" + e);
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            Log.e("info", "e22==" + e);
            return false;
        }

        return true;

    }

    /*
     * 从指定目录读取一个Object
     */
    public static Object readObject(String absFileName) {
        File f = new File(absFileName);
        if (!f.exists()) {
            return null;
        }

        Object obj = null;
        try {
            FileInputStream is = new FileInputStream(absFileName);
            ObjectInputStream ois = new ObjectInputStream(is);

            obj = ois.readObject();

            ois.close();
            is.close();
        } catch (Exception e) {
            Log.e("info", "e==" + e);
            obj = null;
            e.printStackTrace();
        }

        return obj;
    }

}
