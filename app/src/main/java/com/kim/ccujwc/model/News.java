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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        News news = (News) o;

        if (newsTitle != null ? !newsTitle.equals(news.newsTitle) : news.newsTitle != null)
            return false;
        if (newsTag != null ? !newsTag.equals(news.newsTag) : news.newsTag != null) return false;
        if (newsType != null ? !newsType.equals(news.newsType) : news.newsType != null)
            return false;
        return sendTime != null ? sendTime.equals(news.sendTime) : news.sendTime == null;

    }

    @Override
    public int hashCode() {
        int result = newsTitle != null ? newsTitle.hashCode() : 0;
        result = 31 * result + (newsTag != null ? newsTag.hashCode() : 0);
        result = 31 * result + (newsType != null ? newsType.hashCode() : 0);
        result = 31 * result + (sendTime != null ? sendTime.hashCode() : 0);
        return result;
    }
}
