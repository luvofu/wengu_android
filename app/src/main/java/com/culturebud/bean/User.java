package com.culturebud.bean;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by XieWei on 2016/10/25.
 */
@DatabaseTable(tableName = "t_user")
public class User {

    @SerializedName("userId")
    @DatabaseField(id = true, columnName = "user_id")
    private long userId;//用户id

    @SerializedName("mailbox")
    @DatabaseField(columnName = "mailbox")
    private String mailbox;//邮箱

    @SerializedName("nickname")
    @DatabaseField(columnName = "nick_name")
    private String nickname;//nic

    @SerializedName("sex")
    @DatabaseField(columnName = "sex")
    private int sex;//性别

    @SerializedName("avatar")
    @DatabaseField(columnName = "avatar")
    private String avatar;//头像

    @SerializedName("background")
    @DatabaseField(columnName = "background")
    private String background;//背景

    @SerializedName("autograph")
    @DatabaseField(columnName = "autograph")
    private String autograph;//个性签名

    @SerializedName("country")
    @DatabaseField(columnName = "country")
    private String country;//国

    @SerializedName("province")
    @DatabaseField(columnName = "province")
    private String province;//省

    @SerializedName("city")
    @DatabaseField(columnName = "city")
    private String city;//市

    @SerializedName("county")
    @DatabaseField(columnName = "county")
    private String county;//县

    @SerializedName("birthday")
    @DatabaseField(columnName = "birthday")
    private String birthday;//生日

    @SerializedName("tag")
    @DatabaseField(columnName = "tag")
    private String tag;//标签

    @SerializedName("userName")
    @DatabaseField(columnName = "user_name")
    private String userName;//用户名

    @SerializedName("regMobile")
    @DatabaseField(columnName = "reg_mobile")
    private String regMobile;//手机号

    @SerializedName("weixinId")
    @DatabaseField(columnName = "weixin_id")
    private String wechatId;//微信id

    @SerializedName("weiboId")
    @DatabaseField(columnName = "weibo_id")
    private String weiboId;//微博id

    @SerializedName("weixinNickname")
    @DatabaseField(columnName = "wechat_nick")
    private String wechatNick;

    @SerializedName("weiboNickname")
    @DatabaseField(columnName = "weibo_nick")
    private String weiboNick;

    @SerializedName("token")
    @DatabaseField(columnName = "token")
    private String token;//令牌

    @SerializedName("relationType")
    @DatabaseField(columnName = "relation_type")
    private int relationType;//关系类型：陌生人Stranger(0),好友Friend(1),个人Personal(2);

    private String spellFirst;

    public String getSpellFirst() {
        return spellFirst;
    }

    public void setSpellFirst(String spellFirst) {
        this.spellFirst = spellFirst;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getMailbox() {
        return mailbox;
    }

    public void setMailbox(String mailbox) {
        this.mailbox = mailbox;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public String getAutograph() {
        return autograph;
    }

    public void setAutograph(String autograph) {
        this.autograph = autograph;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRegMobile() {
        return regMobile;
    }

    public void setRegMobile(String regMobile) {
        this.regMobile = regMobile;
    }

    public String getWechatId() {
        return wechatId;
    }

    public void setWechatId(String wechatId) {
        this.wechatId = wechatId;
    }

    public String getWeiboId() {
        return weiboId;
    }

    public void setWeiboId(String weiboId) {
        this.weiboId = weiboId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getRelationType() {
        return relationType;
    }

    public void setRelationType(int relationType) {
        this.relationType = relationType;
    }

    public String getWechatNick() {
        return wechatNick;
    }

    public void setWechatNick(String wechatNick) {
        this.wechatNick = wechatNick;
    }

    public String getWeiboNick() {
        return weiboNick;
    }

    public void setWeiboNick(String weiboNick) {
        this.weiboNick = weiboNick;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (userId != user.userId) return false;
        if (sex != user.sex) return false;
        if (relationType != user.relationType) return false;
        if (mailbox != null ? !mailbox.equals(user.mailbox) : user.mailbox != null) return false;
        if (nickname != null ? !nickname.equals(user.nickname) : user.nickname != null) return false;
        if (avatar != null ? !avatar.equals(user.avatar) : user.avatar != null) return false;
        if (background != null ? !background.equals(user.background) : user.background != null) return false;
        if (autograph != null ? !autograph.equals(user.autograph) : user.autograph != null) return false;
        if (country != null ? !country.equals(user.country) : user.country != null) return false;
        if (province != null ? !province.equals(user.province) : user.province != null) return false;
        if (city != null ? !city.equals(user.city) : user.city != null) return false;
        if (county != null ? !county.equals(user.county) : user.county != null) return false;
        if (birthday != null ? !birthday.equals(user.birthday) : user.birthday != null) return false;
        if (tag != null ? !tag.equals(user.tag) : user.tag != null) return false;
        if (userName != null ? !userName.equals(user.userName) : user.userName != null) return false;
        if (regMobile != null ? !regMobile.equals(user.regMobile) : user.regMobile != null) return false;
        if (wechatId != null ? !wechatId.equals(user.wechatId) : user.wechatId != null) return false;
        if (weiboId != null ? !weiboId.equals(user.weiboId) : user.weiboId != null) return false;
        if (wechatNick != null ? !wechatNick.equals(user.wechatNick) : user.wechatNick != null) return false;
        if (weiboNick != null ? !weiboNick.equals(user.weiboNick) : user.weiboNick != null) return false;
        if (token != null ? !token.equals(user.token) : user.token != null) return false;
        return spellFirst != null ? spellFirst.equals(user.spellFirst) : user.spellFirst == null;

    }

    @Override
    public int hashCode() {
        int result = (int) (userId ^ (userId >>> 32));
        result = 31 * result + (mailbox != null ? mailbox.hashCode() : 0);
        result = 31 * result + (nickname != null ? nickname.hashCode() : 0);
        result = 31 * result + sex;
        result = 31 * result + (avatar != null ? avatar.hashCode() : 0);
        result = 31 * result + (background != null ? background.hashCode() : 0);
        result = 31 * result + (autograph != null ? autograph.hashCode() : 0);
        result = 31 * result + (country != null ? country.hashCode() : 0);
        result = 31 * result + (province != null ? province.hashCode() : 0);
        result = 31 * result + (city != null ? city.hashCode() : 0);
        result = 31 * result + (county != null ? county.hashCode() : 0);
        result = 31 * result + (birthday != null ? birthday.hashCode() : 0);
        result = 31 * result + (tag != null ? tag.hashCode() : 0);
        result = 31 * result + (userName != null ? userName.hashCode() : 0);
        result = 31 * result + (regMobile != null ? regMobile.hashCode() : 0);
        result = 31 * result + (wechatId != null ? wechatId.hashCode() : 0);
        result = 31 * result + (weiboId != null ? weiboId.hashCode() : 0);
        result = 31 * result + (wechatNick != null ? wechatNick.hashCode() : 0);
        result = 31 * result + (weiboNick != null ? weiboNick.hashCode() : 0);
        result = 31 * result + (token != null ? token.hashCode() : 0);
        result = 31 * result + relationType;
        result = 31 * result + (spellFirst != null ? spellFirst.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", mailbox='" + mailbox + '\'' +
                ", nickname='" + nickname + '\'' +
                ", sex=" + sex +
                ", avatar='" + avatar + '\'' +
                ", background='" + background + '\'' +
                ", autograph='" + autograph + '\'' +
                ", country='" + country + '\'' +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", county='" + county + '\'' +
                ", birthday='" + birthday + '\'' +
                ", tag='" + tag + '\'' +
                ", userName='" + userName + '\'' +
                ", regMobile='" + regMobile + '\'' +
                ", wechatId='" + wechatId + '\'' +
                ", weiboId='" + weiboId + '\'' +
                ", wechatNick='" + wechatNick + '\'' +
                ", weiboNick='" + weiboNick + '\'' +
                ", token='" + token + '\'' +
                ", relationType=" + relationType +
                ", spellFirst='" + spellFirst + '\'' +
                '}';
    }

    @Override
    public User clone() throws CloneNotSupportedException {
        User user = new User();
        user.setUserId(userId);
        user.setMailbox(mailbox);
        user.setNickname(nickname);
        user.setSex(sex);
        user.setAvatar(avatar);
        user.setBackground(background);
        user.setAutograph(autograph);
        user.setCountry(country);
        user.setProvince(province);
        user.setCity(city);
        user.setCounty(county);
        user.setBirthday(birthday);
        user.setTag(tag);
        user.setUserName(userName);
        user.setRegMobile(regMobile);
        user.setWechatId(wechatId);
        user.setWeiboId(weiboId);
        user.setWechatNick(wechatNick);
        user.setWeiboNick(weiboNick);
        user.setToken(token);
        user.setSpellFirst(spellFirst);
        user.setRelationType(relationType);
        return user;
    }

}
