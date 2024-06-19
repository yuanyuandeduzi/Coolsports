package com.example.baselibs.net.network.bean;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "user")
public class User {

    @NonNull    //主键不能为null，必须添加这个注解
    @PrimaryKey(autoGenerate = true)    //主键是否自动增长，默认为false
    private int id;

    private String phone = "123456789";
    private String password = "";
    private String userName = "";

    private String age = "";
    private String gender = "";
    private String email = "";
    private String uid = "";
    private String headUrl = "";

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "User{" +
                "phone='" + phone + '\'' +
                ", passWord='" + password + '\'' +
                ", useName='" + userName + '\'' +
                ", age='" + age + '\'' +
                ", gender='" + gender + '\'' +
                ", email='" + email + '\'' +
                ", uid='" + uid + '\'' +
                ", headUrl='" + headUrl + '\'' +
                '}';
    }
}
