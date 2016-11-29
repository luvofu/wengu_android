package com.culturebud;

/**
 * Created by XieWei on 2016/10/25.
 */

public final class ApiErrorCode {

    public static final int CODE_SUCCESS = 200;

    //common error code 10000-10099
    public static final int ERROR_PARAM = 10000;
    public static final String ERROR_PARAM_INFO = "参数错误！";
    public static final int NULL_KEYWORD = 10001;
    public static final String NULL_KEYWORD_INFO = "关键字空！";
    public static final int ILLEGAL_USER = 10002;
    public static final String ILLEGAL_USER_INFO = "非法用户！";
    public static final int ERROR_TYPE = 10003;
    public static final String ERROR_TYPE_INFO = "类型错误！";
    public static final int DELETE_IMAGE_FAILD = 10004;
    public static final String DELETE_IMAGE_FAILD_INFO = "删除图片失败！";
    public static final int EDIT_IMAGE_FAILD = 10005;
    public static final String EDIT_IMAGE_FAILD_INFO = "编辑图片失败！";

    //user error code 10100-10199
    public static final int NOT_LOGIN = 10100;
    public static final String NOT_LOGIN_INFO = "用户未登录！";
    public static final int ILLEGAL_MOBILE = 10101;
    public static final String ILLEGAL_MOBILE_INFO = "不合法手机号！";
    public static final int REGED_MOBILE = 10102;
    public static final String REGED_MOBILE_INFO = "手机号已被注册！";
    public static final int NOT_REGED_MOBILE = 10103;
    public static final String NOT_REGED_MOBILE_INFO = "手机号未注册！";
    public static final int BINDED_MOBILE = 10104;
    public static final String BINDED_MOBILE_INFO = "手机号已被绑定！";
    public static final int NOT_BIND_MOBILE = 10105;
    public static final String NOT_BIND_MOBILE_INFO = "手机号未绑定！";
    public static final int ERROR_LOGIN = 10106;
    public static final String ERROR_LOGIN_INFO = "用户名或密码错误！";
    public static final int EXPIRETIME_TOKEN = 10107;
    public static final String EXPIRETIME_TOKEN_INFO = "令牌过期！";
    public static final int BINDED_THIRD_ACC = 10108;
    public static final String BINDED_THIRD_ACC_INFO = "第三方账号已被绑定！";
    public static final int ALREADY_BIND_THIRD = 10109;
    public static final String ALREADY_BIND_THIRD_INFO = "账号已绑定第三方！";
    public static final int NOT_SET_ACC = 10110;
    public static final String NOT_SET_ACC_INFO = "未设置平台账号！";
    public static final int EXIST_ACC = 10111;
    public static final String ALREADY_SET_PSW_INFO = "已设置密码！";
    public static final int ERROR_ORIGIN_PSW = 10112;
    public static final String ERROR_ORIGIN_PSW_INFO = "原密码错误！";
    public static final int EQUAL_ORIGIN_PSW = 10113;
    public static final String EQUAL_ORIGIN_PSW_INFO = "与原密码相同！";
    public static final int ACC_NOT_EXIST = 10114;
    public static final String EXIST_ACC_INFO = "账号已存在！";
    public static final int ALREADY_SET_PSW = 10115;
    public static final String ACC_NOT_EXIST_INFO = "账号不存在！";
    public static final int USER_NOT_EXIST = 10116;
    public static final String USER_NOT_EXIST_INFO = "用户不存在！";
    public static final int EXIST_NICKNAME = 10117;
    public static final String EXIST_NICKNAME_INFO = "昵称已存在！";
    public static final int EXCEPT_ACC = 10118;
    public static final String EXCEPT_ACC_INFO = "账号异常，请重新登陆！";
    public static final int ERROR_VALIDCODE = 10119;
    public static final String ERROR_VALIDCODE_INFO = "验证码错误！";
    public static final int SEND_VALIDCODE_FAIL = 10120;
    public static final String SEND_VALIDCODE_FAIL_INFO = "发送验证码失败，请重新请求！";
    public static final int EMPTY_SEND_VALIDCODE_NUM = 10121;
    public static final String EMPTY_SEND_VALIDCODE_NUM_INFO = "验证次数使用完！";


    //book error code 10200-10299
    public static final int BOOK_NOT_EXIST = 10200;
    public static final String BOOK_NOT_EXIST_INFO = "书籍社区不存在！";
    public static final int BOOK_SCAN_NOT_EXIST = 10203;
    public static final String BOOK_SCAN_NOT_EXIST_INFO = "未查询到书籍！";

    //community error code 10300-10399
    public static final int BOOKCOMMUNITY_NOT_EXIST = 10300;
    public static final String BOOKCOMMUNITY_NOT_EXIST_INFO = "书籍社区不存在！";
    public static final int COMMENT_NOT_EXIST = 10301;
    public static final String COMMENT_NOT_EXIST_INFO = "评论不存在！";
    public static final int REPLY_OBJ_NOT_EXIST = 10302;
    public static final String REPLY_OBJ_NOT_EXIST_INFO = "回复对象不存在！";

    //bookdesk error code 10400-10499
    public static final int BOOKDESK_NOT_EXIST = 10400;
    public static final String BOOKDESK_NOT_EXIST_INFO = "书桌不存在！";
    public static final int NOT_DESKUSER = 10401;
    public static final String NOT_DESKUSER_INFO = "非书桌用户！";

    //booksheet error code 10500-10599
    public static final int BOOKSHEET_NOT_EXIST = 10500;
    public static final String BOOKSHEET_NOT_EXIST_INFO = "书单不存在！";
    public static final int NULL_BOOKSHEET_NAME = 10501;
    public static final String NULL_BOOKSHEET_NAME_INFO = "书单名不能为空！";
    public static final int BOOK_ALREADY_IN_SHEET = 10502;
    public static final String BOOK_ALREADY_IN_SHEET_INFO = "书单已存在该书籍！";
    public static final int BOOK_NOT_IN_SHEET = 10503;
    public static final String BOOK_NOT_IN_SHEET_INFO = "书单不存在该书籍！";

    //usermessage error code 10600-10699
    public static final int WAS_FRIEND = 10600;
    public static final String WAS_FRIEND_INFO = "已是好友关系！";
    public static final int INVITE_NOT_FRIEND = 10601;
    public static final String INVITE_NOT_FRIEND_INFO = "邀请非好友！";
    public static final int REINVITE_DESKUSER = 10602;
    public static final String REINVITE_DESKUSER_INFO = "重复邀请书桌用户！";
    public static final int ALREADY_A_DESKUSER = 10603;
    public static final String ALREADY_A_DESKUSER_INFO = "已是书桌用户！";

    //bookcircle error code 10700-10799
    public static final int DYNAMIC_NOT_EXIST = 10700;
    public static final String DYNAMIC_NOT_EXIST_INFO = "动态不存在！";

    //user collection error code 10800-10899
    public static final int DUPLICATE_COLLECT = 10800;
    public static final String DUPLICATE_COLLECT_INFO = "重复收藏！";
    public static final int UNKNOWN_COLLECTION_TYPE = 10801;
    public static final String UNKNOWN_COLLECTION_TYPE_INFO = "未知收藏类型！";
    public static final int COLLECTION_NOT_EXIST = 10802;
    public static final String COLLECTION_NOT_EXIST_INFO = "收藏不存在！";

    //userbook bookmark error code 10900-10999
    public static final int USERBOOK_ALREADY_EXIST = 10900;
    public static final String USERBOOK_ALREADY_EXIST_INFO = "藏书已存在！";
    public static final int ISBN_ALREADY_EXIST = 10901;
    public static final String ISBN_ALREADY_EXIST_INFO = "ISBN已存在！";
    public static final int ERROR_ISBN = 10902;
    public static final String ERROR_ISBN_INFO = "ISBN错误！";
    public static final int NULL_AUTHOR_OR_TITLE = 10903;
    public static final String NULL_AUTHOR_OR_TITLE_INFO = "书名、作者不能为空！";
    public static final int USERBOOK_NOT_EXIST = 10904;
    public static final String USERBOOK_NOT_EXIST_INFO = "藏书不存在！";
    public static final int BOOKMARK_ALREADY_EXIST = 10905;
    public static final String BOOKMARK_ALREADY_EXIST_INFO = "书签已存在！";
    public static final int BOOKMARK_NOT_EXIST = 10906;
    public static final String BOOKMARK_NOT_EXIST_INFO = "书签不存在！";
    public static final int MINUS_PAGE = 10907;
    public static final String MINUS_PAGE_INFO = "页码不能为负数！";

    //notebook error code 11000-11099
    public static final int NOTEBOOK_NOT_EXIST = 11000;
    public static final String NOTEBOOK_NOT_EXIST_INFO = "笔记本不存在！";
    public static final int NOTE_NOT_EXIST = 11001;
    public static final String NOTE_NOT_EXIST_INFO = "笔记不存在！";

}
