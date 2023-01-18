package com.example.kedaikopi.Model;

public class Reservasi {
    private String pid,kname,price,quantity,discount,menu;

    public Reservasi (){

        }
    public Reservasi (String pid, String kname, String price, String quantity, String discount,String menu) {
        this.pid = pid;
        this.kname = kname;
        this.price = price;
        this.quantity = quantity;
        this.menu = menu;
        this.discount = discount;

    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getKname() {
        return kname;
    }

    public void setKname(String kname) {
        this.kname = kname;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getMenu() {
        return menu;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }


}
