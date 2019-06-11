package com.share.gudd.intentshare;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * @ProjectName: IntentShare
 * @Package: com.share.gudd.intentshare
 * @Description: 与QQ相关的一些其他功能
 * @Author: gudongdong
 * @CreateDate: 2019/6/11 22:45
 */
public class AboutQQUtil {

    /**
     * 获取自己要设置的key的话请登录：https://qun.qq.com/join.html
     *
     * 该群是我用来测试的群，也可以加入讨论讨论技术啥的，虽然现在还没啥人
     * 发起添加群流程。群号：Android学习交流(610194891) 的 key 为： CXaQmSGNixYtgpaRuUlxd0CwyMhQYkd_
     * 调用 joinQQGroup(CXaQmSGNixYtgpaRuUlxd0CwyMhQYkd_) 即可发起手Q客户端申请加群 Android学习交流(610194891)
     *
     * @param key 由官网生成的key
     * @return 返回true表示呼起手Q成功，返回fals表示呼起失败
     */
    public static boolean joinQQGroup(Context context,String key) {
        Intent intent = new Intent();
        intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D" + key));
        // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面
         intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            context.startActivity(intent);
            return true;
        } catch (Exception e) {
            // 未安装手机QQ或安装的版本不支持
            return false;
        }
    }
}
