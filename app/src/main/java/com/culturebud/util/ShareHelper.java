package com.culturebud.util;

import android.content.Context;
import android.text.TextUtils;

import com.culturebud.CommonConst;

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

    public static void share(Context context, String title, String content, String imageUrl, String linkUrl) {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(title);
        // text是分享文本，所有平台都需要这个字段
        oks.setText(content);
        // url仅在微信（包括好友和朋友圈）中使用

        if (!TextUtils.isEmpty(imageUrl)) {
            oks.setImageUrl(imageUrl);
        }

        if (linkUrl == null || TextUtils.isEmpty(linkUrl)) {
            oks.setUrl(CommonConst.getHost());
        } else {
            oks.setUrl(linkUrl);
        }
        // 启动分享GUI
        oks.show(context);
    }

}
