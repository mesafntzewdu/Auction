package com.mesi.auction.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.mesi.auction.dao.DAO;
import com.mesi.auction.dao.ItemDAO;
import com.mesi.auction.dao.bought_item_single_dao;
import com.mesi.auction.dao.member_item_dao;
import com.mesi.auction.dao.singleDAO;
import com.mesi.auction.dao.userDAO;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DBHelper extends SQLiteOpenHelper {

    SQLiteDatabase db;


    public DBHelper(Context context) {
        super(context, "auction_db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table users(user_id INTEGER primary key, user_name TEXT, father_name TEXT, email TEXT, phone TEXT, sex TEXT, password TEXT,balance INTEGER default 1000, profile_img_path TEXT)");

        db.execSQL("create table paid_users(p_id INTEGER primary key, is_paid TEXT, item_id INTEGER, foreign key(item_id) references users(item_id))");

        db.execSQL("create table item(item_id INTEGER primary key,item_name TEXT, price TEXT, description TEXT, category TEXT,start_date TEXT, end_date TEXT, item_img_path TEXT,state TEXT default 'Pending', user_id INTEGER," +
                " foreign key (user_id) references users(user_id) on delete cascade on update cascade)");

        db.execSQL("create table bid_history(bid_id INTEGER primary key,  item_id INTEGER, user_id INTEGER, price FLOAT,bid_time TEXT," +
                " foreign key (user_id) references users(user_id) on delete cascade," +
                " foreign key (item_id) references item(item_id) on delete cascade)");

        db.execSQL("create table report(rep_id INTEGER primary key, report TEXT, user_id INTEGER, item_id INTEGER, " +
                " foreign key (user_id) references users(user_id) on delete cascade on update cascade," +
                " foreign key (item_id) references item(item_id) on delete cascade on update cascade)");

        db.execSQL("create table catg_admin(admin_id INTEGER primary key, user_name TEXT, father_name TEXT, email TEXT, password TEXT, profile_img_path TEXT)");

        db.execSQL("create table status(state_id INTEGER primary key, admin_id INTEGER, item_id INTEGER, state TEXT, " +
                " foreign key (admin_id) references catg_admin(admin_id) on delete cascade on update cascade," +
                " foreign key (item_id) references item(item_id) on delete cascade on update cascade)");

        db.execSQL("create table sold_item(sold_id INTEGER primary key, item_id INTEGER, user_id INTEGER, secret_key TEXT, " +
                " foreign key (item_id) references item(item_id) on delete cascade on update cascade," +
                " foreign key (user_id) references users(user_id) on delete cascade on update cascade)");

        db.execSQL("create table vis_sub(sub_id INTEGER primary key, email TEXT, category TEXT)");

        db.execSQL("create table memb_sub(sub_id INTEGER primary key, email TEXT, category TEXT, user_id INTEGER, " +
                " foreign key (user_id) references users(user_id) on delete cascade on update cascade)");

        db.execSQL("create table rating(rate_id INTEGER primary key, rank FLOAT, user_id INTEGER, item_id INTEGER, foreign key(user_id) references users(user_id) on delete cascade," +
                " foreign key (item_id) references item(item_id) on delete cascade)");

        db.execSQL("create table user_acc(acc_id INTEGER primary key, acc_number TEXT, user_id INTEGER, foreign key(user_id) references users(user_id) on delete cascade)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

        db.execSQL("drop table if exists users");
        db.execSQL("drop table if exists item");
        db.execSQL("drop table if exists catg_admin");
        db.execSQL("drop table if exists bid_history");
        db.execSQL("drop table if exists sold_item");
        db.execSQL("drop table if exists rating");
        db.execSQL("drop table if exists category");
        db.execSQL("drop table if exists report");
        db.execSQL("drop table if exists vis_sub");
        db.execSQL("drop table if exists memb_sub");
        db.execSQL("drop table if exists status");

    }

    //MEMBER AND VISITOR DB
    public boolean insertUser(String user_name, String father_name, String email, String phone, String sex, String password, String imageAbsolutePath) {

        ContentValues c = new ContentValues();
        c.put("user_name", user_name);
        c.put("father_name", father_name);
        c.put("email", email);
        c.put("phone", phone);
        c.put("sex", sex);
        c.put("password", password);
        c.put("phone", phone);
        c.put("profile_img_path", imageAbsolutePath);

        try {
            db = this.getWritableDatabase();
            long x = db.insert("users", null, c);

            return x != -1;
        } catch (Exception e) {

        } finally {
            if (db.isOpen())
                db.close();
        }


        return false;
    }

    //Insert into item table
    public boolean insertItem(String item_name, String price, String description, String category, String start_date, String end_date, String item_img_path, String user_id) {

        ContentValues c = new ContentValues();
        c.put("item_name", item_name);
        c.put("price", price);
        c.put("description", description);
        c.put("category", category);
        c.put("start_date", start_date);
        c.put("end_date", end_date);
        c.put("item_img_path", item_img_path);
        c.put("user_id", user_id);

        try {
            db = this.getWritableDatabase();
            long x = db.insert("item", null, c);

            return x != -1;
        } catch (Exception e) {

        } finally {
            if (db.isOpen())
                db.close();
        }

        return false;

    }

    public boolean updateFname(String f_name, String id) {

        ContentValues c = new ContentValues();
        c.put("father_name", f_name);

        try {
            db = this.getWritableDatabase();
            long x = db.update("users", c, "user_id=?", new String[]{id});
            Log.d("Id cheking", id);

            return x != -1;
        } catch (Exception e) {


            e.printStackTrace();
        } finally {
            if (db.isOpen())
                db.close();
        }

        return false;

    }

    public boolean updateUserName(String u_name, String id) {

        ContentValues c = new ContentValues();
        c.put("user_name", u_name);

        try {
            db = this.getWritableDatabase();
            long x = db.update("users", c, "user_id=?", new String[]{id});
            Log.d("Id cheking", id);

            return x != -1;
        } catch (Exception e) {


            e.printStackTrace();
        } finally {
            if (db.isOpen())
                db.close();
        }

        return false;

    }

    public boolean checkOldPassword(String oldPassword, String id) {
        try {
            db = this.getReadableDatabase();

            Cursor c = db.rawQuery("select password from users where user_id=? ", new String[]{id});

            if (c.getString(6).equals(oldPassword))
                return false;

            c.close();
        } catch (Exception e) {
            return true;
        } finally {
            db.close();

        }

        return true;
    }

    public boolean updatePassword(String password, String id) {

        ContentValues c = new ContentValues();
        c.put("password", password);

        try {
            db = this.getWritableDatabase();
            long x = db.update("users", c, "user_id=?", new String[]{id});
            Log.d("Id cheking", id);

            return x != -1;
        } catch (Exception e) {


            e.printStackTrace();
        } finally {
            if (db.isOpen())
                db.close();
        }

        return false;
    }

    public int getUserBalance(String id) {
        int x = 0;

        try {

            db = this.getReadableDatabase();

            Cursor c = db.rawQuery("select balance from users where user_id=? ", new String[]{id});

            if (c.getCount() > 0) {
                c.moveToFirst();
                x = c.getInt(0);
            }
            c.close();
            return x;
        } catch (Exception e) {

            e.printStackTrace();
        } finally {
            db.close();

        }

        return x;
    }

    //get all user for list
    public ArrayList<DAO> getAllItems() {
        ArrayList<DAO> uList = new ArrayList<>();

        Cursor c = null;
        try {
            db = this.getReadableDatabase();

            c = db.rawQuery("select item.item_id,item.item_name, item.start_date, item.end_date, item.item_img_path, item.category,  item.user_id , sold_item.sold_id from item  left join sold_item on item.item_id = sold_item.item_id where state = ?", new String[]{"Approved"});
            if (c.getCount() > 0)
                c.moveToFirst();
            while (!c.isAfterLast()) {
                DAO U = new DAO(c.getInt(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4), c.getString(5), c.getString(6), c.getInt(7));
                uList.add(U);
                c.moveToNext();
            }

        } catch (Exception e) {

            e.printStackTrace();
        } finally {

            db.close();
            assert c != null;
            c.close();

        }

        return uList;
    }


    public String getUser(String id) {

        String User_name = null;
        Cursor c;
        try {
            db = this.getReadableDatabase();

            c = db.rawQuery("select user_name from users where user_id = ?", new String[]{id});
            if (c.getCount() > 0) {

                c.moveToFirst();
                User_name = c.getString(0);
            }

            c.close();

        } catch (Exception e) {

            e.printStackTrace();
        } finally {
            db.close();

        }

        //  Log.d("user name", String.valueOf(c.getCount()));
        return User_name;
    }


    public void getsingleItem(long itemId) {

        Cursor c = null;
        try {
            c = null;

            db = this.getReadableDatabase();
            c = db.rawQuery("select * from item where item_id=?", new String[]{String.valueOf(itemId)});
            if (c.getCount() > 0)
                c.moveToFirst();

            singleDAO.getSingleInstance().setItem_id(c.getInt(0));
            singleDAO.getSingleInstance().setItem_name(c.getString(1));
            singleDAO.getSingleInstance().setPrice(c.getString(2));
            singleDAO.getSingleInstance().setDescription(c.getString(3));
            singleDAO.getSingleInstance().setCategory(c.getString(4));
            singleDAO.getSingleInstance().setStart_date(c.getString(5));
            singleDAO.getSingleInstance().setEnd_date(c.getString(6));
            singleDAO.getSingleInstance().setImg_path(c.getString(7));
            singleDAO.getSingleInstance().setRate(String.valueOf(c.getInt(8)));
            singleDAO.getSingleInstance().setUser_name(getUser(c.getString(9)));
            singleDAO.getSingleInstance().setUserIdItemOwner(c.getString(9));


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
            assert c != null;
            c.close();

        }

    }

    public boolean checkUserIfExists(String email) {

        Cursor c = null;
        try {

            c = null;

            db = this.getReadableDatabase();
            c = db.rawQuery("select password from users where email=?", new String[]{String.valueOf(email)});
            if (c.getCount() > 0)
                return true;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
            assert c != null;
            c.close();

        }

        return false;
    }

    public boolean addSubVisitor(String email, String category) {

        ContentValues c = new ContentValues();

        c.put("email", email);
        c.put("category", category);


        try {
            db = this.getWritableDatabase();
            long x = db.insert("vis_sub", null, c);

            return x != -1;
        } catch (Exception e) {

        } finally {
            if (db.isOpen())
                db.close();
        }

        return false;

    }

    public boolean insertBuyerInfo(String user_id, String item_id, String price, String bidTime) {

        ContentValues c = new ContentValues();

        c.put("user_id", user_id);
        c.put("item_id", item_id);
        c.put("price", price);
        c.put("bid_time", bidTime);

        try {
            db = this.getWritableDatabase();
            long x = db.insert("bid_history", null, c);

            return x != -1;
        } catch (Exception e) {

        } finally {
            if (db.isOpen())
                db.close();
        }

        return false;

    }

    //get user id from users table
    public String getUserId(String email) {

        String user_id;
        Cursor c = null;
        try {

            c = null;

            db = this.getReadableDatabase();
            c = db.rawQuery("select user_id from users where email=?", new String[]{String.valueOf(email)});

            c.moveToFirst();

            user_id = c.getString(0);

            return user_id;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
            assert c != null;
            c.close();

        }

        return null;
    }

    //get all bidding history for single item
    public List<ItemDAO> getBiddingInfo(String item_id) {
        ArrayList<ItemDAO> itemList = new ArrayList<>();

        Cursor c;
        try {
            db = this.getReadableDatabase();

            c = db.rawQuery("select * from bid_history  where item_id = ?", new String[]{item_id});
            if (c.getCount() > 0) {
                c.moveToFirst();

                while (!c.isAfterLast()) {
                    ItemDAO itemd = new ItemDAO(getItemName(c.getString(1)), getUserNameFromId(c.getString(2)), c.getString(3), c.getString(4), String.valueOf(getRateAveg(item_id)));
                    itemList.add(itemd);
                    c.moveToNext();
                }

            }

            c.close();

        } catch (Exception e) {

            e.printStackTrace();
        } finally {
            db.close();

        }

        return itemList;
    }

    private float getRateAveg(String item_id) {

        db = this.getReadableDatabase();

        Cursor c = db.rawQuery("select rank from rating where item_id=?", new String[]{item_id});

        if (c.getCount() <= 0) {
            return 0;
        }

        float sum = 0;
        c.moveToFirst();
        while (!c.isAfterLast()) {
            sum = sum + c.getInt(0);
            c.moveToNext();
        }

        float avg = sum / c.getCount();

        c.close();
        db.close();

        DecimalFormat df = new DecimalFormat("#.##");

        return Float.parseFloat(df.format(avg));
    }

    private String getUserNameFromId(String user_id) {

        String userid;
        try {

            db = this.getReadableDatabase();
            Cursor c = db.rawQuery("select user_name from users where user_id=?", new String[]{String.valueOf(user_id)});
            c.moveToFirst();

            userid = c.getString(0);
            c.close();
            return userid;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();

        }

        return null;

    }

    private String getItemName(String item_id) {

        String itemName;
        Cursor c;
        try {

            db = this.getReadableDatabase();
            c = db.rawQuery("select item_name from item where item_id=?", new String[]{String.valueOf(item_id)});

            c.moveToFirst();
            itemName = c.getString(0);

            c.close();

            return itemName;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();

        }
        return null;
    }

    public List<member_item_dao> getMemberItem(String user_id) {

        ArrayList<member_item_dao> itemList = new ArrayList<>();

        Cursor c;
        try {
            db = this.getReadableDatabase();

            c = db.rawQuery("select item_id, item_name, state, item_img_path   from item where user_id=?", new String[]{user_id});
            if (c.getCount() > 0) {
                c.moveToFirst();

                while (!c.isAfterLast()) {
                    member_item_dao itemd = new member_item_dao(c.getInt(0), c.getString(1), c.getString(2), c.getString(3));
                    itemList.add(itemd);
                    c.moveToNext();
                }

            }

            c.close();

        } catch (Exception e) {

            e.printStackTrace();
        } finally {
            db.close();

        }

        return itemList;
    }

    public boolean deleteItem(int item_id) {

        if (checkIfTheItemIsActive(item_id)) {
            return false;
        }

        try {
            db = this.getWritableDatabase();
            db.setForeignKeyConstraintsEnabled(true);
            long x = db.delete("item", "item_id=?", new String[]{String.valueOf(item_id)});
            return x != -1;
        } catch (Exception e) {

        } finally {
            db.close();
        }

        return false;
    }

    public boolean updateItem(int item_id, String item_name, String member_item_price, String member_item_description, String member_item_category, String member_item_enddate, String updateImagePath) {

        if (checkIfTheItemIsActive(item_id)) {
            return false;
        }
        ContentValues c = new ContentValues();
        c.put("item_name", item_name);
        c.put("price", member_item_price);
        c.put("description", member_item_description);
        c.put("category", member_item_category);
        c.put("end_date", member_item_enddate);
        c.put("item_img_path", updateImagePath);

        try {
            db = this.getWritableDatabase();
            long x = db.update("item", c, "item_id=?", new String[]{String.valueOf(item_id)});

            return x != -1;
        } catch (Exception e) {

        } finally {
            db.close();
        }

        return false;
    }

    private boolean checkIfTheItemIsActive(int item_id) {

        Cursor c = null;
        try {
            db = this.getReadableDatabase();

            c = db.rawQuery("select * from bid_history where item_id = ?", new String[]{String.valueOf(item_id)});
            c.moveToFirst();

            return c.getCount() > 0;
        } catch (Exception e) {

        } finally {
            db.close();
            if (c != null) {
                c.close();
            }
        }

        return false;
    }

    //update rate table
    public void updateRate(float rating, int item_id, String user_id) {

        try {
            db = this.getWritableDatabase();

            ContentValues c = new ContentValues();
            c.put("rank", rating);
            db.update("rating", c, "item_id=? and user_id=?", new String[]{String.valueOf(item_id), user_id});
        } catch (Exception e) {

        } finally {
            db.close();
        }

    }

    public boolean checkIfUserRate(String item_id, String user_id_db) {

        Cursor c = null;
        try {

            db = this.getReadableDatabase();
            c = db.rawQuery("select * from rating where user_id=? and item_id=?", new String[]{user_id_db, item_id});

            if (c.getCount() > 0) {
                return true;
            }

            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            db.close();

            if (c != null) {
                c.close();
            }
        }
        return false;

    }

    public void insertRate(float rating, int item_id, String user_id_db) {

        ContentValues c = new ContentValues();
        c.put("rank", rating);
        c.put("user_id", user_id_db);
        c.put("item_id", item_id);


        try {
            db = this.getWritableDatabase();
            db.insert("rating", null, c);

        } catch (Exception e) {

        } finally {
            db.close();
        }


    }

    public float getHignerPriceFromBidHistory(String item_id) {

        Cursor c = null;

        try {
            db = this.getReadableDatabase();

            c = db.rawQuery("select MAX(price) from bid_history where item_id = ?", new String[]{item_id});

            if (c.getCount() > 0) {
                c.moveToFirst();

                return c.getFloat(0);
            }
        } catch (Exception e) {

        } finally {
            if (c != null) {
                c.close();
            }

            db.close();
        }

        return 0;
    }

    public void insertSoldInfo(int item_id) {

        if (!checkIfUserBids(item_id))
            return;

        if (checkIfDateIsChecked(item_id)) {
            try {
                ContentValues c = new ContentValues();
                c.put("item_id", item_id);
                c.put("user_id", getHighestBidder(String.valueOf(item_id)));

                Random rand = new Random();

                c.put("secret_key", rand.nextInt(900000) + 100000);
                db = this.getWritableDatabase();

                db.insert("sold_item", null, c);
            } catch (Exception e) {

            } finally {
                db.close();
            }

        }
    }

    private boolean checkIfUserBids(int item_id) {

        Cursor c = null;
        boolean a = false;
        try {
            db = this.getReadableDatabase();

            c = db.rawQuery("select * from bid_history where item_id = ?", new String[]{String.valueOf(item_id)});

            a = c.getCount() > 0;
        } catch (Exception e) {

        } finally {
            if (c != null) {
                c.close();
            }

            db.close();
        }


        return a;
    }

    private boolean checkIfDateIsChecked(int item_id) {

        Cursor c = null;
        try {
            db = this.getReadableDatabase();
            c = db.rawQuery("select * from sold_item where item_id = ?", new String[]{String.valueOf(item_id)});

            if (c.getCount() > 0) {
                c.close();
                db.close();
                return false;
            }
        } catch (Exception e) {

        } finally {
            if (c != null) {
                c.close();
            }

            db.close();
        }

        return true;
    }

    //get the highest bidder user id from bid_history table
    public int getHighestBidder(String item_id) {

        int user_id = 0;
        Cursor c = null;
        try {
            db = this.getReadableDatabase();

            c = db.rawQuery("select user_id from bid_history where price = (select  MAX(price) from bid_history where item_id = ?)", new String[]{item_id});

            if (c.getCount() > 0) {
                c.moveToFirst();
                user_id = c.getInt(0);
            }
        } catch (Exception e) {

        } finally {
            if (c != null) {
                c.close();
            }

            db.close();
        }


        return user_id;
    }


    public ArrayList<DAO> getAllBoughtItemForUser(String user_id) {

        ArrayList<DAO> uList = new ArrayList<>();

        Cursor c = null;
        try {
            db = this.getReadableDatabase();

            c = db.rawQuery("select item.item_id,item.item_name, item.start_date, item.end_date, item.item_img_path, item.category,  item.user_id  , item.user_id from item inner join sold_item on item.item_id = sold_item.item_id where sold_item.user_id  = ?", new String[]{user_id});
            if (c.getCount() > 0)
                c.moveToFirst();
            while (!c.isAfterLast()) {
                DAO U = new DAO(c.getInt(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4), c.getString(5), c.getString(6), c.getInt(7));
                uList.add(U);
                c.moveToNext();
            }

        } catch (Exception e) {

        } finally {

            db.close();
            if (c != null) {
                c.close();
            }

        }

        return uList;
    }

    public void getBoughtItemDetailSingleItem(long itemId) {

        Cursor c = null;

        try {

            db = this.getReadableDatabase();

            c = db.rawQuery("select item.item_img_path, item.item_name, bid_history.item_id, item.description, item.user_id, sold_item.secret_key from item inner join bid_history on bid_history.item_id = item.item_id inner join sold_item on sold_item.item_id = bid_history.item_id where item.item_id = ?", new String[]{String.valueOf(itemId)});

            c.moveToFirst();

            bought_item_single_dao.getBoughtInstance().setImgPath(c.getString(0));
            bought_item_single_dao.getBoughtInstance().setItemName(c.getString(1));
            bought_item_single_dao.getBoughtInstance().setHigherPrice(getHigherPriceForItem(String.valueOf(c.getInt(2))));
            bought_item_single_dao.getBoughtInstance().setDescription(c.getString(3));
            bought_item_single_dao.getBoughtInstance().setSoldBy(getUserNameFromId(String.valueOf(c.getInt(4))));
            bought_item_single_dao.getBoughtInstance().setConfNumber(c.getString(5));

            bought_item_single_dao.getBoughtInstance().setUserId(c.getInt(4));

            bought_item_single_dao.getBoughtInstance().setItem_id(String.valueOf(c.getInt(2)));


        } catch (Exception e) {

            e.printStackTrace();
        } finally {
            if (c != null) {
                c.close();
            }

            db.close();
        }

    }

    private String getHigherPriceForItem(String item_id) {

        Cursor c = null;
        try {
            db = this.getReadableDatabase();

            c = db.rawQuery("select max(price) from bid_history where item_id = ? ", new String[]{item_id});
            c.moveToFirst();

            return c.getString(0);
        } catch (Exception e) {

        } finally {
            db.close();
            if (c != null) {
                c.close();
            }
        }

        return null;
    }

    public ArrayList<userDAO> getSellerDetail(int userId) {

        ArrayList<userDAO> userRow = new ArrayList<>();

        Cursor c = null;

        try {
            db = this.getReadableDatabase();
            c = db.rawQuery("select user_name, father_name, email, phone, profile_img_path from users where user_id = ? ", new String[]{String.valueOf(userId)});

            if (c.getCount() > 0) {
                c.moveToFirst();
                userDAO u = new userDAO(c.getString(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4));
                userRow.add(u);
            }
        } catch (Exception e) {

        } finally {
            if (c != null) {
                c.close();
                db.close();
            }
        }

        return userRow;
    }


    public void updateUserBalance(String itemId, String user_id, Float bid_price) {

        Cursor c = null;

        try {

            Float maxprice = null;
            int userId = 0;

            db = this.getReadableDatabase();

            //Get the recent max user and give there money back

            c = db.rawQuery("select MAX(price), user_id from bid_history where item_id = ?", new String[]{itemId});

            if (c.getCount() > 0) {
                c.moveToFirst();

                maxprice = c.getFloat(0);
                userId = c.getInt(1);

            }

            //Update the user balance to the previous value
            Cursor add = db.rawQuery("select balance from users where user_id = ?", new String[]{String.valueOf(userId)});
            add.moveToFirst();

            if (add.getCount() > 0) {
                ContentValues a = new ContentValues();
                a.put("balance", maxprice + add.getFloat(0));

                db.update("users", a, "user_id = ?", new String[]{String.valueOf(userId)});
                //Done updating the user balance back to the recent higher bidder

            }

            //Here we subtract from the user balance who goes for bidding with the higher price
            Cursor sub = db.rawQuery("select balance from users where user_id = ?", new String[]{String.valueOf(user_id)});

            if (sub.getCount() > 0) {
                sub.moveToFirst();

                ContentValues s = new ContentValues();
                s.put("balance", sub.getFloat(0) - bid_price);

                db.update("users", s, "user_id = ?", new String[]{String.valueOf(user_id)});
            }
        } catch (Exception e) {

            e.printStackTrace();
        } finally {
            if (c != null) {
                c.close();
            }

            db.close();
        }

    }

    public boolean updateUserProfile(String user_id, String imageAbsolutePath) {


        try {

            db = this.getWritableDatabase();

            ContentValues c = new ContentValues();
            c.put("profile_img_path", imageAbsolutePath);
            return 0 < db.update("users", c, "user_id = ? ", new String[]{user_id});

        } catch (Exception e) {

        } finally {
            db.close();
        }


        return false;
    }

    public boolean insertUserAccNo(String u_id, String acc) {

        if (!checkIfAccountForUserExit(u_id)) {
            try {
                db = this.getWritableDatabase();

                ContentValues c = new ContentValues();
                c.put("acc_number", acc);
                c.put("user_id", Integer.parseInt(u_id));

                return 0 < db.insert("user_acc", null, c);

            } catch (Exception e) {
                // e.printStackTrace();
            } finally {
                db.close();
            }
        }

        return false;

    }

    private boolean checkIfAccountForUserExit(String u_id) {
        Cursor c = null;
        try {
            db = this.getReadableDatabase();

            c = db.rawQuery("select acc_number from user_acc where user_id = ?", new String[]{u_id});
            c.moveToFirst();

            return c.getCount() > 0;
        } catch (Exception e) {

        } finally {
            db.close();
            if (c != null) {
                c.close();
            }
        }

        return true;
    }


    public boolean insertReportOnce(String user_id, int item_id, String message) {

        if (!checkIfItemIsReportedByTheUser(user_id, item_id)) {
            try {
                db = this.getWritableDatabase();

                ContentValues c = new ContentValues();
                c.put("item_id", item_id);
                c.put("user_id", user_id);
                c.put("report", message);

                return 0 < db.insert("report", null, c);

            } catch (Exception e) {
                // e.printStackTrace();
            } finally {
                db.close();
            }
        }

        return false;

    }

    private boolean checkIfItemIsReportedByTheUser(String user_id, int item_id) {

        Cursor c = null;
        try {
            db = this.getReadableDatabase();

            c = db.rawQuery("select report from report where user_id = ? and item_id = ?", new String[]{user_id, String.valueOf(item_id)});
            c.moveToFirst();

            return c.getCount() > 0;
        } catch (Exception e) {

        } finally {
            db.close();
            if (c != null) {
                c.close();
            }
        }

        return true;
    }

    public boolean checkIfItemIsSold(int item_id) {

        Cursor c = null;
        try {
            db = this.getReadableDatabase();

            c = db.rawQuery("select item_id from sold_item where  item_id = ?", new String[]{String.valueOf(item_id)});
            c.moveToFirst();

            return c.getCount() > 0;
        } catch (Exception e) {

        } finally {
            db.close();
            if (c != null) {
                c.close();
            }
        }

        return false;
    }

    public boolean transferBuyerMoney(int userId, String item_id) {

        Cursor c = null;
        double userCurrentBalance = 0.0;
        double itemMaxPrice = 0.0;
        try
        {
            db = this.getWritableDatabase();

            c = db.rawQuery("select balance from users where user_id = ?", new String[]{String.valueOf(userId)});
            if (c.getCount()>0)
            {
                c.moveToFirst();
                userCurrentBalance = c.getFloat(0);
            }

            c.close();



            Cursor c1;
            c1 = db.rawQuery("select MAX(price) from bid_history where item_id = ?", new String[]{item_id});

            if (c1.getCount()>0)
            {
                c1.moveToFirst();
                itemMaxPrice = c1.getFloat(0);
            }

            c1.close();

            double sum = userCurrentBalance+itemMaxPrice;
            double div = itemMaxPrice/20;

            ContentValues con = new ContentValues();
            con.put("balance", sum - div);

            long x = db.update("users", con, "user_id = ?", new String[]{String.valueOf(userId)});

            con.clear();

            con.put("is_paid", "yes");
            con.put("item_id", userId);
            db.insert("paid_users", null, con);

            return x>0;

        }catch (Exception e)
        {
            e.printStackTrace();
        }finally {
            db.close();
            if (c!=null)
            {
                c.close();
            }
        }

        return false;
    }

    //Check if the user is paid or not
    public boolean userIsPaid(String item_id) {
        Cursor c = null;
        try
        {
            boolean a = false;

            db = this.getReadableDatabase();

            c = db.rawQuery("select is_paid from paid_users where item_id = ?", new String[]{item_id});
            if (c.getCount()>0)
            {
                c.moveToFirst();

               a = c.getString(0).equals("yes");
            }

            Log.d("From the curosr", String.valueOf(a));
            return a;

        }catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }finally {
            db.close();
            if (c!=null)
            {
                c.close();
            }
        }

    }


    //ADMIN MODULE DB
    public boolean approveItem(int item_id) {

        try
        {
            ContentValues c = new ContentValues();
            c.put("state", "Approved");
            c.put("item_id", item_id);

            db = this.getReadableDatabase();

            return db.update("item", c, "item_id=?", new String[]{String.valueOf(item_id)})>0;

        }catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }finally {
            db.close();

        }
    }


    public ArrayList<DAO> getAllItemsPending() {
        ArrayList<DAO> uList = new ArrayList<>();

        Cursor c = null;
        try {
            db = this.getReadableDatabase();

            c = db.rawQuery("select item.item_id,item.item_name, item.start_date, item.end_date, item.item_img_path, item.category,  item.user_id , sold_item.sold_id from item  left join sold_item on item.item_id = sold_item.item_id where state = ?", new String[]{"Pending"});
            if (c.getCount() > 0)
                c.moveToFirst();
            while (!c.isAfterLast()) {
                DAO U = new DAO(c.getInt(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4), c.getString(5), c.getString(6), c.getInt(7));
                uList.add(U);
                c.moveToNext();
            }

        } catch (Exception e) {

            e.printStackTrace();
        } finally {

            db.close();
            assert c != null;
            c.close();

        }

        return uList;
    }


}