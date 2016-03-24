package com.kim.ccujwc.model;

/**
 * Created by kim on 16-3-23.
 */
public class News {

    private String newsTitle;
    private String newsTag;
    private String newsType;
    private String sendTime;

    public String getNewsTitle() {
        return newsTitle;
    }

    public void setNewsTitle(String newsTitle) {
        this.newsTitle = newsTitle;
    }

    public String getNewsTag() {
        return newsTag;
    }

    public void setNewsTag(String newsTag) {
        this.newsTag = newsTag;
    }

    public String getNewsType() {
        return newsType;
    }

    public void setNewsType(String newsType) {
        this.newsType = newsType;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    @Override
    public String toString() {
        return "News{" +
                "newsTitle='" + newsTitle + '\'' +
                ", newsTag='" + newsTag + '\'' +
                ", newsType='" + newsType + '\'' +
                ", sendTime='" + sendTime + '\'' +
                '}';
    }
}
