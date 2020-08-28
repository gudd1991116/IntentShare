# IntentShare
这是一个关于本地分享的项目；提供使用Intent分享微信、QQ、QQ空间、新浪微博的一些方法。


[关于这个项目的一些简单介绍](https://www.jianshu.com/p/9522e24713e1)

### 基本说明  
关于微信分享

``` 
1.微信在6.6.7版本以后，已经不支持往朋友圈分享标题了  
2.微信在6.7.3版本以后，已经不支持往朋友圈分享多图了 
```

***

#### 这是Demo首页  
<img src="https://github.com/gudd1991116/Raw/blob/master/ShareIntent/device-2019-06-11-100936.png" width="30%"/>

### 使用方法
* 分享文本给微信好友
```
NativeShareTool.getInstance(this).shareWechatFriend("Test Test test!!!");
```
* 分享图片给微信好友
```
NativeShareTool.getInstance(this).shareWechatFriend(Resource.getInstance(mContext).getPicFile(),true);
```
* 分享文件给微信好友
```
NativeShareTool.getInstance(this).shareWechatFriend(Resource.getInstance(mContext).getDocFile("contract.docx"),false);
```
* 分享一张图片到微信朋友圈（朋友圈现已不支持分享多图和图文同时分享）
```
NativeShareTool.getInstance(this).shareWechatMoment(Resource.getInstance(mContext).getPicFile());
```
* 分享文本给QQ好友
```
NativeShareTool.getInstance(this).shareQQ("Test test test!!!");
```
* 分享图片给QQ好友
```
NativeShareTool.getInstance(this).shareImageToQQ(getPicBit());
```
* 分享文件给QQ好友
```
NativeShareTool.getInstance(this).shareImageToQQ(Resource.getInstance(mContext).getDocFile("newpdf.pdf"));
```
* 分享图片到QQ空间
```
NativeShareTool.getInstance(this).shareImageToQQZone(Resource.getInstance(mContext).getPicFile().getAbsolutePath());
```
* 分享图片到新浪好友
```
NativeShareTool.getInstance(this).shareToSinaFriends(MainActivity.this, true,Resource.getInstance(mContext).getPicFile().getAbsolutePath());
```
* 分享图片到新浪微博
```
NativeShareTool.getInstance(this).shareToSinaFriends(MainActivity.this, false,Resource.getInstance(mContext).getPicFile().getAbsolutePath());
```
### 将自己加入到QQ群
加入QQ群可以参考网站：https://qun.qq.com/join.html， 可以获取到QQ群相对应的key,使用提供的代码直接添加进入群
```
/****************
* 该群目前为我个人新建测试群
* 发起添加群流程。群号：610194891 的 key 为： CXaQmSGNixYtgpaRuUlxd0CwyMhQYkd_
* 调用 joinQQGroup(CXaQmSGNixYtgpaRuUlxd0CwyMhQYkd_) 即可发起手Q客户端申请加群 Android学习交流(610194891)
*
* @param key 由官网生成的key
* @return 返回true表示呼起手Q成功，返回fals表示呼起失败
******************/
public boolean joinQQGroup(String key) {
    Intent intent = new Intent();
    intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D" + key));
   // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    try {
        startActivity(intent);
        return true;
    } catch (Exception e) {
        // 未安装手Q或安装的版本不支持
        return false;
    }
}

```

