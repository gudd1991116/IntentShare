package com.share.gudd.intentshare.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Resource {
    private static Resource instance;
    private Context mContext;

    private Resource(Context context) {
        this.mContext = context.getApplicationContext();
    }

    public static Resource getInstance(Context context) {
        if (instance == null) {
            instance = new Resource(context);
        }
        return instance;
    }

    /**
     * 获取图片文件资源
     * @return
     */
    public File getPicFile() {
        AssetManager assets = mContext.getResources().getAssets();
        InputStream open = null;
        File mFile = null;
        Bitmap mBitmap;
        try {
            open = assets.open("share_pic.jpg");

            BitmapFactory.Options bo  = new BitmapFactory.Options();
            bo.inScaled = false;
            bo.inPreferredConfig = Bitmap.Config.RGB_565;
            bo.inJustDecodeBounds = false;
            bo.inDither = true;
            mBitmap = BitmapFactory.decodeStream(open, null, bo);

            mFile = ShareToolUtil.saveSharePic(mContext, mBitmap);

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (open != null){
                try {
                    open.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return mFile;
    }

    /**
     * 获取一般文件资源
     * @param fileName 必须加上后缀  如：newpdf.pdf  | contract.docx, 这两个文件我写在assets目录中，我这里为方便参考写成这样，后面可自己修改
     * @return
     */
    public File getDocFile(String fileName) {
        AssetManager assetManager = mContext.getResources().getAssets();
        InputStream open;
        File shareFile = null;
        try {
            open = assetManager.open(fileName);
            String saveFilePath = Environment.getExternalStorageDirectory()+"/android/data/"+mContext.getPackageName()+"/shareFile/";
            File file = new File(saveFilePath);
            if (!file.exists()){
                file.mkdirs();
            }
            shareFile = new File(saveFilePath + fileName);
            if (!shareFile.exists()) {
                shareFile.createNewFile();
            }
            copyFile(open,shareFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return shareFile;
    }


    // 复制文件
    public static void copyFile(InputStream inputStream, File targetFile) throws IOException {
        BufferedInputStream inBuff = null;
        BufferedOutputStream outBuff = null;
        try {
            // 新建文件输入流并对它进行缓冲
            inBuff = new BufferedInputStream(inputStream);

            // 新建文件输出流并对它进行缓冲
            outBuff = new BufferedOutputStream(new FileOutputStream(targetFile));

            // 缓冲数组 5kb
            byte[] b = new byte[1024 * 5];
            int len;
            while ((len = inBuff.read(b)) != -1) {
                outBuff.write(b, 0, len);
            }
            // 刷新此缓冲的输出流
            outBuff.flush();
        } finally {
            // 关闭流
            if (inBuff != null)
                inBuff.close();
            if (outBuff != null)
                outBuff.close();
        }
    }

}