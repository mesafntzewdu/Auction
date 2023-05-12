package com.mesi.auction.dao;

public class singleDAO {

    private singleDAO() {

    }

    private static singleDAO singledao = null;

    public static singleDAO getSingleInstance() {
        if (singledao == null) {
            return singledao = new singleDAO();
        }

        return singledao;
    }

    private String userId_id;
    private int item_id;
    private String item_name;
    private String price;
    private String description;
    private String category;

    private String start_date;
    private String end_date;
    private String img_path;
    private String user_name;
    private String rate;
    private String user_id = "empty";

    private String generalCat;

    private String hPrice;

    public String getUser_session() {
        return user_id;
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

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_id) {
        this.user_name = user_id;
    }

    public String getGeneralCat() {
        return generalCat;
    }

    public void setGeneralCat(String generalCat) {
        this.generalCat = generalCat;
    }

    public String getUserIdSession() {
        return user_id;
    }

    public String getRate()
    {
        return rate;
    }

    public void setRate(String rate)
    {
        this.rate = rate;
    }

    public void setUserIdSession(String userId) {

        this.user_id = userId;
    }

    public String getHighestPrice() {

        return  hPrice;
    }

    public void setHighestPrice(String price) {
        this.hPrice = hPrice;
    }


    public void setUserIdItemOwner(String userId_id) {
        this.userId_id = userId_id;
    }

    public String getUser_id_db()
    {
        return userId_id;
    }
}
