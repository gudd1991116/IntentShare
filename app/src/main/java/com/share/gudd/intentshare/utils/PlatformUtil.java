package com.share.gudd.intentshare.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import java.util.List;

/**
 * Created by gudongdong on 2018/2/2.
 */

public class PlatformUtil {

    public static final String PACKAGE_WECHAT = "com.tencent.mm";
    public static final String PACKAGE_QQ = "com.tencent.mobileqq";
    public static final String PACKAGE_QZONG = "com.qzone";
    public static final String PACKAGE_SINA = "com.sina.weibo";

    public static final String ACTIVITY_SHARE_WECHAT_FRIEND = "com.tencent.mm.ui.tools.ShareImgUI";// 微信好友
    public static final String ACTIVITY_SHARE_WECHAT_MOMENT = "com.tencent.mm.ui.tools.ShareToTimeLineUI";// 朋友圈
    public static final String ACTIVITY_SHARE_QQ_FRIEND = "com.tencent.mobileqq.activity.JumpActivity";// QQ 分为图片和纯文本
    public static final String ACTIVITY_SHARE_QQ_ZONE = "com.qzonex.module.operation.ui.QZonePublishMoodActivity";// QQ空间
    public static final String ACTIVITY_SHARE_SINA_FRIEND = "com.sina.weibo.weiyou.share.WeiyouShareDispatcher";// 微博好友  setType("text/plain");
    public static final String ACTIVITY_SHARE_SINA_CONTENT = "com.sina.weibo.composerinde.ComposerDispatchActivity";// 微博内容  setType("image/*");

    /**
     * 判断是否安装了相应的应用程序
     * @param context
     * @param packageName
     * @return
     */
    public static boolean isInstalledSpecifiedApp(Context context, String packageName){
        try {
            if (TextUtils.isEmpty(packageName)) {
                throw new Exception("请传入指定包名！");
            }
            final PackageManager packageManager = context.getPackageManager();
            List<PackageInfo> pInfo = packageManager.getInstalledPackages(0);
            if (pInfo != null) {
                for (int i = 0; i < pInfo.size(); i++) {
                    String pn = pInfo.get(i).packageName;
                    if (packageName.equals(pn)) {
                        return true;
                    }
                }
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    // 是否存在微信客户端
    public static boolean isWeChatAvailable(Context context) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pInfo = packageManager.getInstalledPackages(0);
        if (pInfo != null) {
            for (int i = 0; i < pInfo.size(); i++) {
                String pn = pInfo.get(i).packageName;
                if (pn.equals("com.tencent.mm")) {
                    return true;
                }
            }
        }

        return false;
    }

    // 是否存在QQ客户端
    public static boolean isQQClientAvailable(Context context) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mobileqq")) {
                    return true;
                }
            }
        }
        return false;
    }

}

