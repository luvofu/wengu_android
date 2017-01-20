package com.culturebud;

import android.os.Environment;
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

    public static final String USER_AVATAR_DEFAULT_URL = "http://www.mywengu.com/img/user/avatar/lpic/default/default.jpg";

//    public static final String API_HOST = "http://192.168.1.26:8080/";

    public static final String API_HOST = "http://mywengu.com:80/";

    public static final String PATH_HOME = "api/common/home";

    public static final String PATH_LOGIN = "api/user/login";

    public static final String PATH_REGIST = "api/user/signup";

    public static final String PATH_RETRIEVE_PWD = "api/user/forgotpsw";

    public static final String PATH_SECURITY_CODE = "api/user/sendValidcode";

    public static final String PATH_USER_EDIT_PROFILE = "api/user/editProfile";

    public static final String PATH_MY_FRIENDS = "api/user/myFriends";

    public static final String PATH_AUTO_LOGIN = "api/user/autoLogin";

    public static final String PATH_CHANGE_PASSWORD = "api/user/resetpsw";

    public static final String PATH_USER_FEEDBACK = "api/user/feedback";

    public static final String PATH_USER_SEARCH = "api/user/search";

    public static final String PATH_USER_PROFILE = "api/user/profile";

    public static final String PATH_USER_MSG_INVITE_FRIEND = "api/userMessage/friendInvite";

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

    public static final String PATH_BOOK_SHEET_DETAIL = "api/bookSheet/detail";

    public static final String PATH_BOOK_SHEET_ADD_BOOK = "api/bookSheet/addBook";

    public static final String PATH_BOOK_SHEET_DEL_BOOK = "api/bookSheet/deleteBook";

    public static final String PATH_USER_BOOKS = "api/userBook/personal";

    public static final String PATH_USER_BOOK_SHEETS = "api/bookSheet/personal";

    public static final String PATH_COLLECT_ADD = "api/userCollection/add";

    public static final String PATH_COLLECT_DELETE = "api/userCollection/delete";

    public static final String PATH_MSG_INVITE = "api/userMessage/invite";

    public static final String PATH_COLLECTED_BOOKS = "api/userCollection/book";

    public static final String PATH_COLLECTED_BOOK_SHEETS = "api/userCollection/bookSheet";

    public static final String PATH_USER_NOTEBOOKS = "api/notebook/personal";

    public static final String PATH_NOTEBOOK_DETAIL = "api/notebook/detail";

    public static final String PATH_NOTES_FOR_NOTEBOOK = "api/notebook/note";

    public static final String PATH_NOTEBOOK_CREATE = "api/notebook/add";

    public static final String PATH_NOTEBOOK_PERMISSION_EDIT = "api/notebook/editPermission";

    public static final String PATH_NOTEBOOK_EDIT = "api/notebook/edit";

    public static final String PATH_NOTE_CREATE = "api/notebook/addNote";

    public static final String PATH_NOTE_EDIT = "api/notebook/editNote";

    public static final String PATH_NOTE_DELETE = "api/notebook/deleteNote";

    public static final String PATH_SCAN_BOOK_ENTRY = "api/userBook/scanSearch";

    public static final String PATH_MANUAL_BOOK_ENTRY = "api/bookCheck/entry";

    public static final String PATH_BOOK_SHEET_CREATE = "/api/bookSheet/add_v101";

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
    }

    public final class ContentPermission {
        //公开Open(0),好友Friend(1),个人 Personal(2);
        public static final int PERMISSION_PUBLIC = 0;
        public static final int PERMISSION_FRIEND = 1;
        public static final int PERMISSION_PERSONAL = 2;

        public static final String PER_DES_PUBLIC = "公开";
        public static final String PER_DES_FRIEND = " 好友";
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

    public static String getRootPath() {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            Log.d(TAG, "扩展存储可用，状态为" + Environment.getExternalStorageState());
            return Environment.getExternalStorageDirectory().getPath();
        } else {
            Log.d(TAG, "扩展存储没法儿用，状态为" + Environment.getExternalStorageState());
            return BaseApp.getInstance().getCacheDir().getPath();
        }
    }

    public static final String CAPTURE_PATH = "file://" + Environment.getExternalStorageDirectory().getPath() + "/culture/capture";
}
