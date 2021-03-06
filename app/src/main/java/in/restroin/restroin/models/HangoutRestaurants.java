package in.restroin.restroin.models;

public class HangoutRestaurants {
    private String restaurant_name;
    private String restaurant_region;
    private String restaurant_rating;
    private String front_image;
    private String price_for_two;
    private String restaurant_id, restaurant_coupon_selected;

    public String getRestaurant_name() {
        return restaurant_name;
    }

    public void setRestaurant_name(String restaurant_name) {
        this.restaurant_name = restaurant_name;
    }

    public String getRestaurant_region() {
        return restaurant_region;
    }

    public void setRestaurant_region(String restaurant_region) {
        this.restaurant_region = restaurant_region;
    }

    public String getRestaurant_rating() {
        return restaurant_rating;
    }

    public void setRestaurant_rating(String restaurant_rating) {
        this.restaurant_rating = restaurant_rating;
    }

    public String getFront_image() {
        return front_image;
    }

    public void setFront_image(String front_image) {
        this.front_image = front_image;
    }

    public String getPrice_for_two() {
        return price_for_two;
    }

    public void setPrice_for_two(String price_for_two) {
        this.price_for_two = price_for_two;
    }

    public String getRestaurant_id() {
        return restaurant_id;
    }

    public void setRestaurant_id(String restaurant_id) {
        this.restaurant_id = restaurant_id;
    }

    public String getRestaurant_coupon_selected() {
        return restaurant_coupon_selected;
    }

    public void setRestaurant_coupon_selected(String restaurant_coupon_selected) {
        this.restaurant_coupon_selected = restaurant_coupon_selected;
    }

    public HangoutRestaurants(String restaurant_name, String restaurant_region, String restaurant_rating, String front_image, String price_for_two, String restaurant_id, String restaurant_coupon_selected) {

        this.restaurant_name = restaurant_name;
        this.restaurant_region = restaurant_region;
        this.restaurant_rating = restaurant_rating;
        this.front_image = front_image;
        this.price_for_two = price_for_two;
        this.restaurant_id = restaurant_id;
        this.restaurant_coupon_selected = restaurant_coupon_selected;
    }
}
