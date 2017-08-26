package com.doopp.gauss.api.entity;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;

/**
 *  用戶實體類
 * @author Administrator
 *
 */
public class UserEntity implements Serializable {

    // 编号
    private Long id;

    // 用户名
    private String account;

    // 密码
    private String password;

    // 昵称
    private String nickname;

    // 性别
    private int gender;

    // 创建时间
    private int created_at;

    // 加密密码的盐
    private String salt;

    // 头像
    private String portrait;

    // 好友
    private String friends;


    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccount() {
        return this.account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getNickname() {
        return this.nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPortrait() {
        return this.portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public String getFriends() {
        return this.friends;
    }

    public void setFriends(String friends) {
        this.friends = friends;
    }

    public boolean addFriend(Long friendId) {
        String _friends  = "," + this.friends + ",";
        String _friendId = "," + friendId + ",";
        // 查找这个好友的 id
        if (!_friends.contains(_friendId)) {
            this.friends = this.friends.equals("") ? String.valueOf(friendId) : this.friends + "," + String.valueOf(friendId);
            return true;
        }
        return false;
    }

    public boolean delFriend(Long friendId) {
        String _friends  = "," + this.friends + ",";
        String _friendId = "," + friendId + ",";
        // 查找这个好友的 id
        if (!_friends.contains(_friendId)) {
            int friendIndex  = _friends.lastIndexOf(_friendId);
            int friendLength = _friendId.length();
            int beginIndex   = 0;
            int endIndex     = friendIndex + friendLength;
            String beginFriends = _friends.substring(beginIndex, friendIndex);
            String endFriends = _friends.substring(endIndex);
            this.friends = beginFriends + "," + endFriends;
            return true;
        }
        return false;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getCreate_at() {
        return this.created_at;
    }

    public void setCreate_at(int created_at) {
        this.created_at = created_at;
    }

    public JSONObject toJsonObject() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", this.id);
        jsonObject.put("account", this.account);
        jsonObject.put("nickname", this.nickname);
        jsonObject.put("gender", this.gender);
        jsonObject.put("portrait", this.portrait);
        return jsonObject;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEntity user = (UserEntity) o;
        if (id != null ? !id.equals(user.id) : user.id != null) return false;
        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "User{" +
            "id=" + id +
            ", account='" + this.account + '\'' +
            ", password='" + this.password + '\'' +
            ", salt='" + this.salt + '\'' +
            '}';
    }
}
