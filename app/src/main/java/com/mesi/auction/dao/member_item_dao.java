package com.mesi.auction.dao;

public class member_item_dao {

    private  int item_id;
    private String item_name;
    private String item_state;
    private String item_img;

    public member_item_dao(int item_id, String item_name, String item_state, String item_img) {
        this.item_id = item_id;
        this.item_name = item_name;
        this.item_state = item_state;
        this.item_img = item_img;
    }

    public void setItem_id(int item_id)
    {
        this.item_id = item_id;
    }
    public  int getItem_id()
    {
        return item_id;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getItem_state() {
        return item_state;
    }

    public void setItem_state(String item_state) {
        this.item_state = item_state;
    }

    public String getItem_img() {
        return item_img;
    }

    public void setItem_img(String item_img) {
        this.item_img = item_img;
    }
}
