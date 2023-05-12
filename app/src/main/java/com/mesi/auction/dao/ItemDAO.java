package com.mesi.auction.dao;

public class ItemDAO {

    private String item_name;
    private String item_bid_by;
    private String item_bid_price;
    private String item_bid_date;
    private String item_rate_value;

    public ItemDAO(String item_name, String item_bid_by, String item_bid_price, String item_bid_date, String item_rate_value) {
        this.item_name = item_name;
        this.item_bid_by = item_bid_by;
        this.item_bid_price = item_bid_price;
        this.item_bid_date = item_bid_date;
        this.item_rate_value = item_rate_value;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getItem_bid_by() {
        return item_bid_by;
    }

    public void setItem_bid_by(String item_bid_by) {
        this.item_bid_by = item_bid_by;
    }

    public String getItem_bid_price() {
        return item_bid_price;
    }

    public void setItem_bid_price(String item_bid_price) {
        this.item_bid_price = item_bid_price;
    }

    public String getItem_bid_date() {
        return item_bid_date;
    }

    public void setItem_bid_date(String item_bid_date) {
        this.item_bid_date = item_bid_date;
    }

    public String getItem_rate_value() {
        return item_rate_value;
    }

    public void setItem_rate_value(String item_rate_value) {
        this.item_rate_value = item_rate_value;
    }
}
