package com.mesi.auction.dao;

public class bought_item_single_dao {


    private String imgPath;
    private String itemName;
    private String higherPrice;
    private String description;
    private String soldBy;
    private String confNumber;
    private  String item_id;
    private int u_id;

    public String getItem_id(){
        return item_id;
    }
    public void setItem_id(String item_id){
        this.item_id = item_id;
    }
    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getHigherPrice() {
        return higherPrice;
    }

    public void setHigherPrice(String higherPrice) {
        this.higherPrice = higherPrice;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSoldBy() {
        return soldBy;
    }

    public void setSoldBy(String soldBy) {
        this.soldBy = soldBy;
    }

    public String getConfNumber() {
        return confNumber;
    }

    public void setConfNumber(String confNumber) {
        this.confNumber = confNumber;
    }


    private static  bought_item_single_dao bought;


    private bought_item_single_dao()
    {

    }

    public static  bought_item_single_dao getBoughtInstance()
    {
        if (bought == null)
        {

            return bought = new bought_item_single_dao();
        }

        return bought;
    }


    public void setUserId(int u_id) {
        
        this.u_id = u_id;
        
    }
    
    public int getUserId()
    {
        return u_id;
    }
}
