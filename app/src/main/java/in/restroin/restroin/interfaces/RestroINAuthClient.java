package in.restroin.restroin.interfaces;

import java.util.List;

import in.restroin.restroin.models.BookingStepModel;
import in.restroin.restroin.models.DiningModel;
import in.restroin.restroin.models.HangoutRestaurants;
import in.restroin.restroin.models.ImageModel;
import in.restroin.restroin.models.LoginModel;
import in.restroin.restroin.models.MessageModel;
import in.restroin.restroin.models.ProfileModel;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RestroINAuthClient {

    @FormUrlEncoded
    @POST("v1/authorization/userCreator")
    Call<MessageModel> registerUser(
            @Field("name") String name,
            @Field("email") String email,
            @Field("password") String password,
            @Field("dev_uid") String device_uid,
            @Field("mob_no") String mobile_number,
            @Field("action") String action
    );

    @GET("getRestaurantImages.php")
    Call<ImageModel> getImages(
            @Query("id") String id
    );

    @FormUrlEncoded
    @POST("v1/authorization/resetPassword")
    Call<MessageModel> resetPassword(
            @Field("email") String email
    );

    @FormUrlEncoded
    @POST("v1/authorization/authenticator")
    Call<LoginModel> authenticateUser(
            @Field("device_uid") String device_uid
    );

    @FormUrlEncoded
    @POST("v1/authorization/bookingsManager")
    Call<MessageModel> ManageReservation(
            @Header("Authorization") String access_token,
            @Field("actionStep") String actionStep,
            @Field("restaurant_id") String restaurant_id,
            @Field("user_id") String user_id,
            @Field("visiting_date") String visiting_date,
            @Field("visiting_time") String visiting_time,
            @Field("number_of_male") String number_of_male,
            @Field("guest_name") String guest_name,
            @Field("guest_phone") String guest_phone,
            @Field("guest_email") String guest_email,
            @Field("couponSelected") String couponSelected
    );

    @FormUrlEncoded
    @POST("v1/authorization/bookingsManager")
    Call<MessageModel> cancelBooking(
            @Header("Authorization") String access_token,
            @Field("actionStep") String actionStep,
            @Field("booking_id") String booking_id,
            @Field("restaurant_name") String restaurant_name,
            @Field("guest_name") String guest_name,
            @Field("guest_phone") String guest_phone,
            @Field("restaurant_phone") String restaurant_phone
    );

    @FormUrlEncoded
    @POST("v1/authorization/OtpManager")
    Call<MessageModel> otpGenerator(
            @Header("Authorization") String access_token,
            @Field("action") String action,
            @Field("mobile_no") String mobile
    );

    @FormUrlEncoded
    @POST("v1/authorization/OtpManager")
    Call<MessageModel> otpVerifier(
            @Header("Authorization") String access_token,
            @Field("action") String action,
            @Field("mobile_no") String mobile,
            @Field("otp") String otp
    );

    @GET("v1/authorization/getProfile")
    Call<ProfileModel> getUserProfile();

    @FormUrlEncoded
    @POST("v1/authorization/searchApi")
    Call<List<HangoutRestaurants>> getRestaurants(
            @Field("filter_name") String filter_name,
            @Field("filter_type") String filter_type
    );

    @FormUrlEncoded
    @POST("developers/api/restroin/v1/authorization/submitReview")
    Call<MessageModel> submitReview(
            @Header("Authorization") String access_token,
            @Field("restaurant_id") String restaurant_id,
            @Field("user_id") String user_id,
            @Field("rating") String rating,
            @Field("review") String review
    );

    @FormUrlEncoded
    @POST("v1/authorization/getDineHistory")
    Call<DiningModel> getDiningHistory (
      @Header("Authorization") String access_token,
      @Field("user_id") String user_id
    );

    @FormUrlEncoded
    @POST("v1/authorization/editProfile")
    Call<MessageModel> updateProfile(
            @Header("Authorization") String access_token,
            @Field("action") String action,
            @Field("first_name") String first_name,
            @Field("last_name") String last_name,
            @Field("email") String email
    );

    @FormUrlEncoded
    @POST("v1/authorization/changePassword")
    Call<MessageModel> changePassword(
        @Header("Authorization") String access_token,
        @Field("old_password") String old_password,
        @Field("new_password") String new_password
    );

    @FormUrlEncoded
    @POST("v1/authorization/getBookingProfile")
    Call<BookingStepModel> getStepBookingData(
            @Header("Authorization") String access_token,
            @Field("booking_id") String booking_id
    );

    @Multipart
    @POST("v1/authorization/editProfile")
    Call<MessageModel> updateImage(
            @Header("Authorization") String access_token,
            @Part MultipartBody.Part file,
            @Part("name") RequestBody image_name,
            @Part("action") RequestBody action
            );
}
