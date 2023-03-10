package com.stivoo.wagbaadmin;

public class CartItem {
    String meal_name;
    String restaurant_name;
    String price;
    String additional_info;
    int qty;
    String img;
    float delivery_fee;

    public CartItem() {
    }

    @Override
    public String toString() {
        return "CartItem{" +
                "meal_name='" + meal_name + '\'' +
                ", restaurant_name='" + restaurant_name + '\'' +
                ", price='" + price + '\'' +
                ", additional_info='" + additional_info + '\'' +
                ", qty=" + qty +
                ", img='" + img + '\'' +
                ", delivery_fee=" + delivery_fee +
                '}';
    }

    public float getDelivery_fee() {
        return delivery_fee;
    }

    public CartItem(String meal_name, String restaurant_name, String price, String additional_info, int qty, String img, float delivery_fee) {
        this.meal_name = meal_name;
        this.restaurant_name = restaurant_name;
        this.price = price;
        this.additional_info = additional_info;
        this.qty = qty;
        this.img = img;
        this.delivery_fee = delivery_fee;
    }

    public String getMeal_name() {
        return meal_name;
    }

    public String getRestaurant_name() {
        return restaurant_name;
    }

    public String getPrice() {
        return price;
    }

    public String getAdditional_info() {
        return additional_info;
    }

    public int getQty() {
        return qty;
    }

    public void setMeal_name(String meal_name) {
        this.meal_name = meal_name;
    }

    public void setRestaurant_name(String restaurant_name) {
        this.restaurant_name = restaurant_name;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setAdditional_info(String additional_info) {
        this.additional_info = additional_info;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public void setDelivery_fee(float delivery_fee) {
        this.delivery_fee = delivery_fee;
    }

    public String getImg() {
        return img;
    }
}
