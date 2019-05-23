package com.share.gudd.intentshare.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;

import com.share.gudd.intentshare.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by gudongdong on 2018/2/6.
 */

public class ShareToolUtil {

    public static final String AUTHORITY = "com.share.gudd.fileprovider";
    private static String sharePicName = "share_pic.jpg";
    private static String sharePicPath = Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator+"intentShare"+ File.separator+"sharepic"+ File.separator;
    public static final int REQUEST_PERMISSION_CODE  = 15;

    public static File saveSharePic(Context context, Bitmap bitmap){

        if (isSDcardExist()){
            File file = new File(sharePicPath);
            if (!file.exists()){
                file.mkdirs();
            }
            File filePic = new File(sharePicPath,sharePicName);
            if (filePic.exists()){
                filePic.delete();
            }
            try {
                FileOutputStream out = new FileOutputStream(filePic);
                if (bitmap == null) {
                    bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.share_pic_horse);
                }
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
                try {
                    out.flush();
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return filePic;
        }

        return null;
    }

    /**
     * 判断存储卡是否存在
     */
    public static boolean isSDcardExist() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    public static void getPermission(Context context) {
        PackageManager packageManager = context.getPackageManager();
        boolean permission = PackageManager.PERMISSION_GRANTED  == packageManager.checkPermission("android.permission.WRITE_EXTERNAL_STORAGE","com.share.gudd.intentshare");
        if (permission) {
            // 有这个权限
        }else{
            // 没有这个权限
            // 如果android版本大于6.0，需要动态申请权限
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                ActivityCompat.requestPermissions((Activity) context,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_PERMISSION_CODE);
            }
        }

        permission = PackageManager.PERMISSION_GRANTED  == packageManager.checkPermission("android.permission.READ_EXTERNAL_STORAGE","com.share.gudd.intentshare");
        if (permission) {
            // 有这个权限
        }else{
            // 没有这个权限
            // 如果android版本大于6.0，需要动态申请权限
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                ActivityCompat.requestPermissions((Activity) context,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_PERMISSION_CODE);
            }
        }
    }

}
