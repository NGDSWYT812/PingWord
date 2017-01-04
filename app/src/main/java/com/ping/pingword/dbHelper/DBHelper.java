package com.ping.pingword.dbHelper;

import android.content.Context;

import com.ping.pingword.dao.DaoMaster;
import com.ping.pingword.dao.DaoSession;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import ping.Lib.Utils.CommonUtil;


/**
 * 数据库控制类
 */
public class DBHelper {
    private static DaoSession daoSessionEcmc;
    private static DaoSession daoSessionEcmc1;

    /**
     * 数据库名称:localdata
     */
    public static final String DATABASE_NAME = "pingword.db";
    public static final String DATABASE_NAME1 = "localdata.db";

    private static DaoMaster obtainMaster(Context context, String dbName) {
        return new DaoMaster(new DaoMaster.DevOpenHelper(context, dbName, null).getWritableDatabase());
    }

    private static DaoMaster getDaoMaster(Context context, String dbName) {
        if (dbName == null)
            return null;
        return obtainMaster(context, dbName);
    }

    /**
     * 取得DaoSession
     *
     * @return
     */
    public static DaoSession getDaoSession(String dbName) {

        if (daoSessionEcmc1 == null) {
            daoSessionEcmc1 = getDaoMaster(CommonUtil.getApplicationContext(), dbName).newSession();
        }
        return daoSessionEcmc1;
    }

    /**
     * 默认操作localdata数据库
     */
    public static DaoSession getDaoSession() {

        if (daoSessionEcmc == null) {
            daoSessionEcmc = getDaoMaster(CommonUtil.getApplicationContext(), DATABASE_NAME).newSession();
        }
        return daoSessionEcmc;
    }

    /**
     * 将assets文件夹下/db的本地库拷贝到/data/data/下
     */
    public static void copyDbFile() {
        InputStream in = null;
        FileOutputStream out = null;
        /**data/data/路径*/
        String path = "/data/data/" + CommonUtil.getApplicationContext().getPackageName() + "/databases";
        File file = new File(path + "/" + DATABASE_NAME);
        try {
            //创建文件夹
            File file_ = new File(path);
            if (!file_.exists())
                file_.mkdirs();
            if (file.exists())//删除已经存在的
                file.deleteOnExit();
            //创建新的文件
            if (!file.exists())
                file.createNewFile();
            in = CommonUtil.getApplicationContext().getAssets().open("Word"); // 从assets目录下复制
            out = new FileOutputStream(file);
            int length = -1;
            byte[] buf = new byte[1024];
            while ((length = in.read(buf)) != -1) {
                out.write(buf, 0, length);
            }
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) in.close();
                if (out != null) out.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }
}