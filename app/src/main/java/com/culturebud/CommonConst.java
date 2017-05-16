package com.culturebud;

import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

/**
 * Created by XieWei on 2016/10/20.
 */

public final class CommonConst {
    private static final String TAG = CommonConst.class.getSimpleName();

    public static final String TOKEN_KEY = "token";

    public static final String PLATFORM_KEY = "platform";

    public static final String PLATFORM = "Android";

    public static final String DEVICE_TOKEN_KEY = "deviceToken";

    public static final String DEVICE_TOKEN = BaseApp.getDeviceId(BaseApp.getInstance());

    public static final String VERSION_NAME_KEY = "version";

    public static final String USER_AVATAR_DEFAULT_URL = "http://www.mywengu.com/img/user/avatar/lpic/default/default" +
            ".jpg";

//    public static final String API_HOST = "http://192.168.1.26:8080/";

//    public static final String API_HOST = getHost();

    public static String getHost() {
        Log.i(TAG, "_url = " + _URL);
        return TextUtils.isEmpty(_URL) ? "http://mywengu.com:80/" : _URL;
    }
    private static String _URL;

    public static void initHost() {
        _URL = BuildConfig.HOST;
    }

    public static final String PATH_HOME = "api/common/choiceness";

    public static final String PATH_LOGIN = "api/user/login";

    public static final String PATH_REGIST = "api/user/signup";

    public static final String PATH_THIRD_LOGIN = "api/user/thirdLogin_v103";

    public static final String PATH_THIRD_BIND_LOGIN = "api/user/thirdBindLogin";

    public static final String PATH_THIRD_BIND = "api/user/bindThird";

    public static final String PATH_THIRD_UNBIND = "api/user/unbindThird";

    public static final String PATH_RETRIEVE_PWD = "api/user/forgotpsw";

    public static final String PATH_SECURITY_CODE = "api/user/sendValidcode";

    public static final String PATH_CHANGE_MOBILE = "api/user/changeMobilephone";

    public static final String PATH_CHECK_MOBILE = "api/user/checkMobilephone";

    public static final String PATH_USER_EDIT_PROFILE = "api/user/editProfile";

    public static final String PATH_USER_EDIT_USERNAME = "api/user/setUserName";

    public static final String PATH_MY_FRIENDS = "api/user/myFriends";

    public static final String PATH_AUTO_LOGIN = "api/user/autoLogin";

    public static final String PATH_CHANGE_PASSWORD = "api/user/resetpsw";

    public static final String PATH_USER_FEEDBACK = "api/user/feedback";

    public static final String PATH_USER_SEARCH = "api/user/search";

    public static final String PATH_USER_PROFILE = "api/user/profile";

    public static final String PATH_USER_MSG_INVITE_FRIEND = "api/userMessage/friendInvite";

    public static final String PATH_USER_MSG_AGREE_INVITE = "api/userMessage/agreeFriendInvite";

    public static final String PATH_EDIT_IMG = "api/common/editIMG";

    public static final String PATH_DELETE_IMG = "api/common/deleteIMG";

    public static final String PATH_COMMUNITY_COMMENTS = "api/comment/lib";

    public static final String PATH_BOOK_CIRCLE_DYNAMIC = "api/bookCircle/dynamic";

    public static final String PATH_BOOK_CIRCLE_DYNAMIC_DETAIL = "api/dynamic/detail";

    public static final String PATH_BOOK_CIRCLE_DYNAMIC_ADD = "api/bookCircle/addDynamic";

    public static final String PATH_BOOK_CIRCLE_DYNAMIC_MY_PUBLISHED = "api/userMessage/dynamicPublish";

    public static final String PATH_BOOK_CIRCLE_DYNAMIC_MY_RELATIONS = "api/userMessage/dynamicRelativeToMe";

    public static final String PATH_BOOK_CIRCLE_DYNAMIC_REPLY = "api/dynamic/addReply";

    public static final String PATH_BOOK_COMMUNITY_SEARCH = "api/bookCommunity/search";

    public static final String PATH_BOOK_COMMUNITY_BOOK = "api/bookCommunity/book";

    public static final String PATH_BOOK_COMMUNITY_DETAIL = "api/bookCommunity/detail";

    public static final String PATH_BOOK_COMMUNITY_COMMENTS = "api/bookCommunity/comment";

    public static final String PATH_BOOK_COMMUNITY_ADD_COMMENT = "api/bookCommunity/addComment";

    public static final String PATH_COMMENT_REPLY = "api/comment/reply";

    public static final String PATH_COMMENT_ADD_REPLY = "api/comment/addReply";

    public static final String PATH_COMMENT_MY_PUBLISHED = "api/userMessage/commentPublish";

    public static final String PATH_COMMENT_MY_RELATED = "api/userMessage/commentRelativeToMe";

    public static final String PATH_COMMON_DEL = "api/common/delete";

    public static final String PATH_GOOD = "api/common/good";

    public static final String PATH_BOOKS = "api/book/lib";

    public static final String PATH_BOOK_DETAIL = "api/book/detail";

    public static final String PATH_BOOK_SEARCH = "api/book/search";

    public static final String PATH_BOOK_SHEETS = "api/bookSheet/lib";

    public static final String PATH_BOOK_SHEET_DELETE = "api/bookSheet/delete";

    public static final String PATH_BOOK_SHEET_DETAIL = "api/bookSheet/detail";

    public static final String PATH_BOOK_SHEET_ADD_BOOK = "api/bookSheet/addBook";

    public static final String PATH_BOOK_SHEET_DEL_BOOK = "api/bookSheet/deleteBook";

    public static final String PATH_BOOK_SHEET_EDIT = "api/bookSheet/edit";

    public static final String PATH_BOOK_SHEET_EDIT_RECOMMEND = "/api/bookSheet/editRecommend";

    public static final String PATH_USER_BOOKS = "api/userBook/personal";

    public static final String PATH_USER_BOOKS_V104 = "api/userBook/personal_v104";

    public static final String PATH_USER_BOOK_DETAIL = "api/userBook/detail";

    public static final String PATH_USER_BOOK_CATEGORY_STATISTICS = "api/userBook/categoryStatis_v104";

    public static final String PATH_USER_BOOK_DELETE = "api/userBook/delete";

    public static final String PATH_USER_MANUAL_ENTERING_BOOKS = "api/bookCheck/personal";

    public static final String PATH_CUSTOM_CATEGORIES = "api/userBook/customCategory";

    public static final String PATH_CUSTOM_CATEGORY_ADD = "api/userBook/addCategory";

    public static final String PATH_CUSTOM_CATEGORY_DEL = "api/userBook/deleteCategory";

    public static final String PATH_CUSTOM_CATEGORY_EDIT = "api/userBook/editCategory";

    public static final String PATH_MOVE_BOOK_TO_CATEGORY = "api/userBook/moveToCategory";

    public static final String PATH_MOVE_BOOK_SORT_CATEGORY = "api/userBook/sortCategory";

    public static final String PATH_BOOK_TAGS = "api/common/bookTag";

    public static final String PATH_BOOK_SHEET_TAGS = "api/common/bookSheetTag";

    public static final String PATH_USER_BOOK_SHEETS = "api/bookSheet/personal";

    public static final String PATH_COLLECT_ADD = "api/userCollection/add";

    public static final String PATH_COLLECT_DELETE = "api/userCollection/delete";

    public static final String PATH_MSG_INVITE = "api/userMessage/invite";

    public static final String PATH_MSG_DELETE = "api/userMessage/delete";

    public static final String PATH_COLLECTED_BOOKS = "api/userCollection/book";

    public static final String PATH_ALTER_BOOK_READ_STATUS = "api/userBook/editReadStatus";

    public static final String PATH_EDIT_USER_BOOK_INFO = "api/userBook/edit";

    public static final String PATH_COLLECTED_BOOK_SHEETS = "api/userCollection/bookSheet";

    public static final String PATH_USER_NOTEBOOKS = "api/notebook/personal";

    public static final String PATH_NOTEBOOK_DETAIL = "api/notebook/detail";

    public static final String PATH_NOTES_FOR_NOTEBOOK = "api/notebook/note_v104";

    public static final String PATH_NOTEBOOK_CREATE = "api/notebook/add";

    public static final String PATH_NOTEBOOK_DELETE = "api/notebook/delete";

    public static final String PATH_NOTEBOOK_PERMISSION_EDIT = "api/notebook/editPermission";

    public static final String PATH_NOTEBOOK_EDIT = "api/notebook/edit";

    public static final String PATH_NOTE_CREATE = "api/notebook/addNote_v104";

    public static final String PATH_NOTE_EDIT = "api/notebook/editNote";

    public static final String PATH_NOTE_DELETE = "api/notebook/deleteNote";

    public static final String PATH_SCAN_BOOK_ENTRY = "api/userBook/scanSearch";

    public static final String PATH_SCAN_BOOK_ADD = "api/userBook/addScanBooks";

    public static final String PATH_MANUAL_BOOK_ENTRY = "api/bookCheck/entry";

    public static final String PATH_MANUAL_BOOK_CHECK = "api/bookCheck/edit";

    public static final String PATH_BOOK_SHEET_CREATE = "/api/bookSheet/add_v101";

    public static final String PATH_MY_BOOK_MARKS = "api/bookmark/personal_v103";

    public static final String PATH_ADD_BOOK_MARK = "api/bookmark/add_v103";

    public static final String PATH_ALTER_BOOK_MARK = "api/bookmark/edit_v103";

    public static final String PATH_DEL_BOOK_MARK = "api/bookmark/delete";

    public static final String PATH_IMPORT_BOOK_SEARCH = "api/userBook/bookSearch";

    public static final String PATH_IMPORT_BOOK_ENTRY = "api/userBook/entry";

    public static final String PATH_BOOK_SEARCH_FILTER = "api/common/bookClass";

    public static final String PATH_LOGIN_OUT = "api/user/loginOut";

    public final class UploadImgType {
        public static final int TYPE_USER_AVATAR = 0;
        public static final int TYPE_USER_BG = 1;
        public static final int TYPE_USER_BOOK_SHEET_COVER = 2;
        public static final int TYPE_USER_NOTEBOOK_CONTENT = 3;
        public static final int TYPE_USER_DYNAMIC_CONTENT = 4;
    }

    public final class ThumbUpType {
        public static final int TYPE_COMMENT = 0;//短评
        public static final int TYPE_DYNAMIC = 1;//书圈动态
    }

    public final class LinkType {
        public static final int TYPE_DELETED = 255;//内容已被删除
        public static final int TYPE_COMMON = 0;
        public static final int TYPE_BOOK = 1;
        public static final int TYPE_BOOK_SHEET = 2;
        public static final int TYPE_COMMENT = 3;
    }

    public final class DeleteType {
        public static final int TYPE_COMMENT = 0;
        public static final int TYPE_COMMENT_REPLY = 1;
        public static final int TYPE_DYNAMIC = 2;
        public static final int TYPE_DYNAMIC_REPLY = 3;
    }

    public final class CommentOperaType {
        public static final int TYPE_THUMB_UP = 0;
        public static final int TYPE_REPLY = 1;
        public static final int TYPE_SHARE = 2;
        public static final int TYPE_DELETE = 3;
    }

    public final class MessageDealStatus {
        public static final int STATUS_NOT_DEAL = 0;
        public static final int STATUS_ACCEPT = 1;
        public static final int STATUS_REFUSE = 2;
    }

    public final class UserMsgType {
        //好友邀请FriendInvite(0),书桌邀请DeskInvite(1),
        // 社区回复CommunityReply(2),书圈回复BookCircleReply(3);
        public static final int TYPE_FRIEND_INVITE = 0;
        public static final int TYPE_DESK_INVITE = 1;
        public static final int TYPE_COMMUNITY_REPLY = 2;
        public static final int TYPE_CIRCLE_REPLY = 3;
    }

    public final class SucrityCodeType {
//        注册Signup(0),忘记密码ForgetPsw(1),
//        查验手机号CheckMobile(2),绑定手机号BindMobile(3);

        public static final int TYPE_REGIST = 0;
        public static final int TYPE_FORGOT_PWD = 1;
        public static final int TYPE_CHECK_MOBILE = 2;
        public static final int TYPE_BIND_MOBILE = 3;
        public static final int TYPE_THIRD_BIND = 4;
    }

    public final class ThirdType {
        public static final int TYPE_NONE = -1;
        public static final int TYPE_WECHAT = 0;
        public static final int TYPE_SINA_WEIBO = 1;
    }

    public final class ContentPermission {
        //公开Open(0),好友Friend(1),个人 Personal(2);
        public static final int PERMISSION_PUBLIC = 0;
        public static final int PERMISSION_FRIEND = 1;
        public static final int PERMISSION_PERSONAL = 2;

        public static final String PER_DES_PUBLIC = "公开";
        public static final String PER_DES_FRIEND = "好友";
        public static final String PER_DES_PERSONAL = "私密";
    }

    public final class RelationType {
        public static final int STRANGER = 0;
        public static final int FRIEND = 1;
        public static final int PERSONAL = 2;
    }

    public final class DynamicReplyType {
        public static final int TYPE_DYNAMIC = 0;
        public static final int TYPE_REPLY = 1;
    }

    public final class UserBookCategoryType {
//        categoryType	是	int	分类类型： All(0, “全部”),Normal(1, “中图法”),Custom(2, “自定义”),Other(3, “其它”);
//        category	是	string	类别（例如：文学 哲学 未读 私密）
        public static final int TYPE_ALL = 0;
        public static final int TYPE_NORMAL = 1;
        public static final int TYPE_CUSTOM = 2;
        public static final int TYPE_OTHER = 3;

        public static final String CATEGORY_LITERATURE = "文学";
        public static final String CATEGORY_PHILOSOPHY = "哲学";
        public static final String CATEGORY_UNREAD = "未读";
        public static final String CATEGORY_PRIVACY = "私密";
    }

    public final class BookVerifyStatus {
        public static final int STATUS_VERIFY = 0;
        public static final int STATUS_NO_PASS = 1;
        public static final int STATUS_PASS = 2;
    }

    public final class RequestCode {
        public static final int REQUEST_CODE_ALTER_NICK = 101;
        public static final int REQUEST_CODE_ALTER_EMAIL = 102;
        public static final int REQUEST_CODE_ALTER_PROFILE = 103;
        public static final int REQUEST_CODE_CHANGE_PWD = 107;
        public static final int REQUEST_CODE_WY_ACCOUNT = 108;

        public static final int REQUEST_CREATE_NOTEBOOK = 201;
        public static final int REQUEST_CODE_SELECT_COLLECTED_BOOK = 202;
        public static final int REQUEST_CODE_EDIT_NOTEBOOK_NAME = 206;
        public static final int REQUEST_CODE_CREATE_NOTE = 207;
        public static final int REQUEST_CODE_EDIT_NOTE = 208;

        public static final int REQUEST_CODE_LOGIN = 1001;
        public static final int REQUEST_CODE_SELECT_COMMUNITY = 1002;
        public static final int REQUEST_CODE_SELECT_IMAGE = 1003;
        public static final int REQUEST_CODE_TAKE_PHOTO = 1004;
        public static final int REQUEST_CODE_REGIST = 1005;
        public static final int REQUEST_CODE_RETRIEVE_PASSWORD = 1006;
        public static final int REQUEST_CODE_ADD_BOOK_MANUAL = 1007;
        public static final int REQUEST_CODE_BOOK_SHEET_DETAIL = 1008;
        public static final int REQUEST_CODE_BOOK_SHEET_CREATE = 1009;
        public static final int REQUEST_CODE_BOOK_SHEET_EDIT = 1010;
        public static final int REQUEST_CODE_PHOTO_CROP = 1011;
        public static final int REQUEST_CODE_SEARCH_USER = 1012;
        public static final int REQUEST_CODE_USER_PROFILE = 1013;
        public static final int REQUEST_CODE_ADD_FRIEND = 1014;
        public static final int REQUEST_CODE_SELECT_BOOK = 1019;
        public static final int REQUEST_CODE_SELECT_USER = 1020;
        public static final int REQUEST_CODE_BS_EDIT_RECOMMEND = 1021;
        public static final int REQUEST_CODE_EDIT_READ_PLACE = 1022;
        public static final int REQUEST_CODE_EDIT_OBTAIN_PLACE = 1023;
        public static final int REQUEST_CODE_ADD_BOOK_TAGS = 1024;
        public static final int REQUEST_CODE_EDIT_BOOK_RATING = 1025;
        public static final int REQUEST_CODE_EDIT_BOOK_OTHER_INFO = 1026;
        public static final int REQUEST_CODE_ENTERING_NEW_BOOK = 1027;

        public static final int REQUEST_CODE_ADD_CUSTOM_CATEGORY = 1028;
        public static final int REQUEST_CODE_MOVE_TO_NEW_CUSTOM_CATEGORY = 1029;

        public static final int REQUEST_CODE_ADD_SCAN_BOOKS = 1030;
        public static final int REQUEST_CODE_EDIT_BOOK_SHEET_NAME = 1031;
        public static final int REQUEST_CODE_EDIT_BOOK_SHEET_DESC = 1032;
        public static final int REQUEST_CODE_EDIT_BOOK_SHEET_TAG = 1033;

        public static final int REQUEST_CODE_SEARCH_BOOK_ADD_TO_BOOK_SHEET = 1034;

        public static final int REQUEST_CODE_PUBLISH_DYNAMIC = 1035;
        public static final int REQUEST_CODE_MANUAL_BOOK_CHECK = 1036;

        public static final int REQUEST_CODE_EDIT_NOTE_CONTNET= 1037;
        public static final int REQUEST_CODE_EDIT_NOTE_PAGE = 1038;
        public static final int REQUEST_CODE_EDIT_NOTE_CHAPTER = 1039;
        public static final int REQUEST_CODE_CHANGEMOBILEBIND = 1040;
        public static final int REQUEST_CODE_EDIT_CUSTOMCATEGORY = 1041;
    }

    public static String getRootPath() {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            Log.d(TAG, "扩展存储可用，状态为" + Environment.getExternalStorageState());
            return Environment.getExternalStorageDirectory().getPath();
        } else {
            Log.d(TAG, "扩展存储没法儿用，状态为" + Environment.getExternalStorageState());
            return BaseApp.getInstance().getCacheDir().getPath();
        }
    }

    public static final String CAPTURE_PATH = "file://" + Environment.getExternalStorageDirectory().getPath() +
            "/culture/capture";
}
