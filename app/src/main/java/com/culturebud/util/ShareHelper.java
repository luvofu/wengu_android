package com.culturebud.util;

import android.content.Context;
import android.text.TextUtils;

import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * Created by XieWei on 2016/12/4.
 */

public class ShareHelper {

    public static void share(Context context, String title, String name, String url) {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(title);
        // text是分享文本，所有平台都需要这个字段
        oks.setText(name);
        // url仅在微信（包括好友和朋友圈）中使用
        if (TextUtils.isEmpty(url)) {
            oks.setUrl("http://sharesdk.cn");
        } else {
            oks.setUrl(url);
        }
        // 启动分享GUI
        oks.show(context);
    }
}
