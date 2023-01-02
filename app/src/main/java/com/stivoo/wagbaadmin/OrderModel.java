package com.stivoo.wagbaadmin;

public class OrderModel {
    public OrderModel() {
    }

    String timestamp;
        String pic;
        String name;
        String phone;
        Order orderData;
        String uid;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Order getOrderData() {
        return orderData;
    }

    public void setOrderData(Order orderData) {
        this.orderData = orderData;
    }

    @Override
    public String toString() {
        return "OrderModel{" +
                "timestamp='" + timestamp + '\'' +
                ", pic='" + pic + '\'' +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", orderData=" + orderData +
                ", uid='" + uid + '\'' +
                '}';
    }

    public String getTimestamp() {
        return timestamp;
    }


    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public OrderModel(String timestamp, String pic, String name, String phone, Order orderData, String uid) {
        this.timestamp = timestamp;
        this.pic = pic;
        this.name = name;
        this.phone = phone;
        this.orderData = orderData;
        this.uid = uid;
    }
}
