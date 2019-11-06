package com.ahnsafety.ex59rssfeed;

public class Item {

    String title;
    String link;
    String desc;
    String imgUrl;
    String date;

    public Item() {
    }

    public Item(String title, String link, String desc, String imgUrl, String date) {
        this.title = title;
        this.link = link;
        this.desc = desc;
        this.imgUrl = imgUrl;
        this.date = date;
    }
    //Getter & Setter Method..

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl= imgUrl;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
