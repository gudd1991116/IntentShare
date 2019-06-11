package com.share.gudd.intentshare;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.widget.Toast;

import com.share.gudd.intentshare.utils.PlatformUtil;
import com.share.gudd.intentshare.utils.ShareToolUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiangrikui on 2018/3/23.
 * 使用原生Intent进行分享
 */

public class NativeShareTool {

    @SuppressLint("StaticFieldLeak")
    private static NativeShareTool instance;

    private Context mContext;
    private Activity mActivity;

    private NativeShareTool(Activity context) {
        this.mContext = context;
        this.mActivity = context;
    }

    public static NativeShareTool getInstance(Activity context) {
        if (instance == null) {
            instance = new NativeShareTool(context);
        }
        return instance;
    }

    /**
     * 直接分享文本到微信好友
     */
    public void shareWechatFriend(String content) {
        if (PlatformUtil.isWeChatAvailable(mActivity)) {
            Intent intent = new Intent();
            ComponentName cop = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareImgUI");
            intent.setComponent(cop);
            intent.setAction(Intent.ACTION_SEND);
            intent.putExtra("android.intent.extra.TEXT", content);
            intent.putExtra("Kdescription", !TextUtils.isEmpty(content) ? content : "");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mActivity.startActivity(intent);
        } else {
            Toast.makeText(mActivity, "您需要安装微信客户端", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 直接分享图片或文件给微信好友
     *
     * @param file      文件
     * @param isPicFile 是否为图片文件
     */
    public void shareWechatFriend(File file, boolean isPicFile) {
        if (PlatformUtil.isWeChatAvailable(mActivity)) {
            Intent intent = new Intent();
            ComponentName cop = new ComponentName(PlatformUtil.PACKAGE_WECHAT, PlatformUtil.ACTIVITY_SHARE_WECHAT_FRIEND);
            intent.setComponent(cop);
            intent.setAction(Intent.ACTION_SEND);
            if (isPicFile) {
                intent.setType("image/*");
            } else {
                intent.setType("*/*");
            }
            if (file != null) {
                if (file.isFile() && file.exists()) {
                    Uri uri;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        uri = FileProvider.getUriForFile(mContext, ShareToolUtil.AUTHORITY, file);
                    } else {
                        uri = Uri.fromFile(file);
                    }
                    intent.putExtra(Intent.EXTRA_STREAM, uri);
                }
            }
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mActivity.startActivity(intent);
        } else {
            Toast.makeText(mActivity, "您需要安装微信客户端", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 直接分享文本和图片到微信朋友圈
     * 微信6.6.7以后已经不支持分享标题了，详情查看：https://www.jianshu.com/p/57935d90bf75
     * 微信6.7.3以后已经不支持多图分享了，详情查看：https://www.jianshu.com/p/1158d7c20a8b
     */
    public void shareWechatMoment(File picFile) {
        if (PlatformUtil.isWeChatAvailable(mActivity)) {
            Intent intent = new Intent();
            //分享精确到微信的页面，朋友圈页面，或者选择好友分享页面
            ComponentName comp = new ComponentName(PlatformUtil.PACKAGE_WECHAT, PlatformUtil.ACTIVITY_SHARE_WECHAT_MOMENT);
            intent.setComponent(comp);
            // 微信在6.7.3以后已经对多图分享进行了封杀了，请看网友分析：https://www.jianshu.com/p/1158d7c20a8b
//            intent.setAction(Intent.ACTION_SEND_MULTIPLE);// 分享多张图片时使用
            intent.setAction(Intent.ACTION_SEND);
            intent.setType("image/*");
            if (picFile != null) {
                if (picFile.isFile() && picFile.exists()) {
                    Uri uri;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        uri = FileProvider.getUriForFile(mActivity, ShareToolUtil.AUTHORITY, picFile);
                    } else {
                        uri = Uri.fromFile(picFile);
                    }
                    intent.putExtra(Intent.EXTRA_STREAM, uri);
                }
            }

            // 微信6.6.7版本以后已经无法使用如下Kdescription来分享标题了，详情可查看网友分析：https://www.jianshu.com/p/57935d90bf75
//            intent.putExtra("Kdescription", !TextUtils.isEmpty(content) ? content : "");
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);// 如果不在activity中打开该界面需要加这行代码，否则crash
            mActivity.startActivityForResult(intent, 101);
        } else {
            Toast.makeText(mActivity, "您需要安装微信客户端", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 直接分享纯文本内容至QQ好友
     *
     * @param content 分享文本
     */
    public void shareQQ(String content) {
        if (PlatformUtil.isQQClientAvailable(mContext)) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
            intent.putExtra(Intent.EXTRA_TEXT, content);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setType("text/plain");
            intent.setComponent(new ComponentName(PlatformUtil.PACKAGE_QQ, PlatformUtil.ACTIVITY_SHARE_QQ_FRIEND));
            mContext.startActivity(intent);
        } else {
            Toast.makeText(mContext, "您需要安装QQ客户端", Toast.LENGTH_LONG).show();
        }
    }


    /**
     * 分享图片给QQ好友
     *
     * @param bitmap
     */
    public void shareImageToQQ(Bitmap bitmap) {
        if (PlatformUtil.isQQClientAvailable(mContext)) {
            try {
                Uri uriToImage = Uri.parse(MediaStore.Images.Media.insertImage(
                        mContext.getContentResolver(), bitmap, null, null));
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_STREAM, uriToImage);
                shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                shareIntent.setType("image/*");
                // 遍历所有支持发送图片的应用。找到需要的应用
                ComponentName componentName = new ComponentName(PlatformUtil.PACKAGE_QQ, PlatformUtil.ACTIVITY_SHARE_QQ_FRIEND);

                shareIntent.setComponent(componentName);
                mContext.startActivity(shareIntent);
            } catch (Exception e) {
//            ContextUtil.getInstance().showToastMsg("分享图片到**失败");
            }
        }
    }

    /**
     * 分享文件给QQ好友
     *
     * @param file
     */
    public void shareImageToQQ(File file) {
        if (PlatformUtil.isQQClientAvailable(mContext)) {
            try {
//                Uri uriToImage = Uri.parse(MediaStore.Images.Media.insertImage(
//                        mContext.getContentResolver(), bitmap, null, null));
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                shareIntent.setType("*/*");
                // 遍历所有支持发送图片的应用。找到需要的应用
                ComponentName componentName = new ComponentName(PlatformUtil.PACKAGE_QQ, PlatformUtil.ACTIVITY_SHARE_QQ_FRIEND);

                shareIntent.setComponent(componentName);

                if (file != null) {
                    if (file.isFile() && file.exists()) {
                        Uri uri;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            uri = FileProvider.getUriForFile(mContext, ShareToolUtil.AUTHORITY, file);
                        } else {
                            uri = Uri.fromFile(file);
                        }
                        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                    }
                }
                mContext.startActivity(shareIntent);
            } catch (Exception e) {
//            ContextUtil.getInstance().showToastMsg("分享图片到**失败");
            }
        }
    }

    /**
     * description : 分享到QQ空间
     * created at 2018/7/9 17:04
     *
     * @param photoPath 照片路径
     */
    public void shareImageToQQZone(String photoPath) {
        try {
            if (PlatformUtil.isInstalledSpecifiedApp(mActivity, PlatformUtil.PACKAGE_QZONG)) {
                File file = new File(photoPath);
                if (!file.exists()) {
                    String tip = "文件不存在";
                    Toast.makeText(mActivity, tip + " path = " + photoPath, Toast.LENGTH_LONG).show();
                    return;
                }

                Intent intent = new Intent();
                ComponentName componentName = new ComponentName(PlatformUtil.PACKAGE_QZONG, PlatformUtil.ACTIVITY_SHARE_QQ_ZONE);
                intent.setComponent(componentName);
                intent.setAction("android.intent.action.SEND");
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_TEXT, "I'm so tired!!");
                if (file.isFile() && file.exists()) {
                    Uri uri;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        uri = FileProvider.getUriForFile(mContext, ShareToolUtil.AUTHORITY, file);
                    } else {
                        uri = Uri.fromFile(file);
                    }
                    intent.putExtra(Intent.EXTRA_STREAM, uri);
                }
                mActivity.startActivity(intent);
            } else {
                Toast.makeText(mActivity, "您需要安装QQ空间客户端", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * description : 微博分享
     * created at 2018/7/10 13:58
     */
    public void shareToSinaFriends(Context context, boolean isFriends, String photoPath) {
        if (PlatformUtil.isInstalledSpecifiedApp(context, PlatformUtil.PACKAGE_SINA)) {

            File file = new File(photoPath);
            if (!file.exists()) {
                String tip = "文件不存在";
                Toast.makeText(context, tip + " path = " + photoPath, Toast.LENGTH_LONG).show();
                return;
            }

            Intent intent = new Intent(Intent.ACTION_SEND);
            // 使用以下两种type有一定的区别，"text/plain"分享给指定的粉丝或好友 ；"image/*"分享到微博内容
            if (isFriends) {
                intent.setType("text/plain");
            } else {
                intent.setType("image/*");// 分享文本|文本+图片|图片 到微博内容时使用
            }
            PackageManager packageManager = context.getPackageManager();
            List<ResolveInfo> matchs = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
            ResolveInfo resolveInfo = null;
            for (ResolveInfo each : matchs) {
                String pkgName = each.activityInfo.applicationInfo.packageName;
                if (PlatformUtil.PACKAGE_SINA.equals(pkgName)) {
                    resolveInfo = each;
                    break;
                }
            }
            // type = "image/*"--->com.sina.weibo.composerinde.ComposerDispatchActivity
            // type = "text/plain"--->com.sina.weibo.weiyou.share.WeiyouShareDispatcher
            intent.setClassName(PlatformUtil.PACKAGE_SINA, resolveInfo.activityInfo.name);// 这里在使用resolveInfo的时候需要做判空处理防止crash
            intent.putExtra(Intent.EXTRA_TEXT, "Test Text String !!");
            if (file.isFile() && file.exists()) {
                Uri uri;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    uri = FileProvider.getUriForFile(mContext, ShareToolUtil.AUTHORITY, file);
                } else {
                    uri = Uri.fromFile(file);
                }
                intent.putExtra(Intent.EXTRA_STREAM, uri);
            }
            context.startActivity(intent);
        } else {
            Toast.makeText(mContext, "您需要安装sina客户端", Toast.LENGTH_LONG).show();
        }
    }

}