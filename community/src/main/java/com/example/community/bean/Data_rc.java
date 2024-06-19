package com.example.community.bean;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.example.community.db.StringConverter;

import java.util.List;

@Entity(tableName = "community_data")
@TypeConverters(StringConverter.class)
public class Data_rc {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    private int id;

    public Data_rc() {
    }

    //头像
    private String head;
    //昵称
    private String name = "";
    //内容
    private String content = "";
    //图片
    private List<String> list;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }


    @Override
    public String toString() {
        return "Data_rc{" +
                "id=" + id +
                ", head='" + head + '\'' +
                ", name='" + name + '\'' +
                ", content='" + content + '\'' +
                ", list=" + list +
                '}';
    }
}
