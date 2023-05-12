package com.mesi.auction.dao;

public class DAO {


    private int item_id;
    private String item_name;
    private String price;
    private String description;
    private String category;
    private String start_date;
    private String end_date;
    private String img_path;
    private String rate;
    private String user_id;
    private int sold_item_id;


    public DAO(int item_id, String item_name, String start_date, String end_date, String img_path, String category, String user_id, int sold_item_id) {
        this.item_id = item_id;
        this.item_name = item_name;
        this.price = price;
        this.description = description;
        this.category = category;
        this.start_date = start_date;
        this.end_date = end_date;
        this.img_path = img_path;
        this.rate = rate;
        this.user_id = user_id;
        this.sold_item_id = sold_item_id;
    }

    public int getItem_id() {
        return item_id;
    }

    public void setItem_id(int item_id) {
        this.item_id = item_id;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setStart_date(String start_date)
    {
        this.start_date = start_date;
    }

    public String getStart_date()
    {
        return start_date;
    }
    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getImg_path() {
        return img_path;
    }

    public void setImg_path(String img_path) {
        this.img_path = img_path;
    }

    public void setRate(String rate)
    {
        this.rate = rate;
    }
    public String getRate()
    {
        return rate;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public int getSold_item_id()
    {
        return sold_item_id;
    }
}
