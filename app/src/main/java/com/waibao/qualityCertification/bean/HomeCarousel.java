package com.waibao.qualityCertification.bean;

//主页的viewPager实体类
public class HomeCarousel {
    private int imageView;
    private String title;
    private int count;

    public HomeCarousel(int imageView, String title, int count) {
        this.imageView = imageView;
        this.title = title;
        this.count = count;
    }

    public int getImageView() {
        return imageView;
    }

    public String getTitle() {
        return title;
    }

    public int getCount() {
        return count;
    }

    public void setImageView(int imageView) {
        this.imageView = imageView;
    }

    public void setTitle(String link) {
        this.title = link;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
