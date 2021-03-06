package in.restroin.restroin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.SearchEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import in.restroin.restroin.adapters.CusinesGridAdapter;
import in.restroin.restroin.adapters.HangoutPlacesAdapter;
import in.restroin.restroin.adapters.OffersAdapter;
import in.restroin.restroin.adapters.PopularLocationsAdapter;
import in.restroin.restroin.adapters.PopularRestaurantsAdapter;
import in.restroin.restroin.interfaces.CusinesClient;
import in.restroin.restroin.interfaces.OfferClient;
import in.restroin.restroin.interfaces.PopularLocationClient;
import in.restroin.restroin.interfaces.PopularRestaurantsClient;
import in.restroin.restroin.models.CusineGridModel;
import in.restroin.restroin.models.HangoutRestaurants;
import in.restroin.restroin.models.Offers;
import in.restroin.restroin.models.PopularLocations;
import in.restroin.restroin.models.PopularRestaurants;
import in.restroin.restroin.utils.SaveSharedPreferences;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import in.restroin.restroin.utils.MyGridView;

public class UserFeedActivity extends AppCompatActivity {

    private final static String RETROFIT_TAG = "RETROFIT_LOG";

    Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl("https://www.restroin.in/")
            .addConverterFactory(GsonConverterFactory.create());

    @Override
    protected void onPostResume() {
        setProfileImage();
        super.onPostResume();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        setProfileImage();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_feed);
        setProfileImage();
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.ProgressBar);
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.main_layout);
        progressBar.setVisibility(View.VISIBLE);
        relativeLayout.setVisibility(View.GONE);
        final SearchView searchView = (SearchView) findViewById(R.id.searchView);
        ImageView searchViewIcon = (ImageView) searchView.findViewById(android.support.v7.appcompat.R.id.search_mag_icon);
        ViewGroup searchbarViewGroup = (ViewGroup) searchViewIcon.getParent();
        searchbarViewGroup.removeView(searchViewIcon);
        searchbarViewGroup.addView(searchViewIcon);
        TextView textView = (TextView) findViewById(R.id.footer);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://www.restroin.in/home/privacy_policy";
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            }
        });
        View v = searchView.findViewById(android.support.v7.appcompat.R.id.search_plate);
        v.setBackgroundColor(Color.TRANSPARENT);
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserFeedActivity.this, SearchActivity.class);
                intent.putExtra("filter_type", "Restaurant");
                intent.putExtra("filter_id", searchView.getQuery());
                startActivity(intent);
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent intent = new Intent(UserFeedActivity.this, SearchActivity.class);
                intent.putExtra("filter_type", "Restaurant");
                intent.putExtra("filter_id", query);
                startActivity(intent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Intent intent = new Intent(UserFeedActivity.this, SearchActivity.class);
                intent.putExtra("filter_type", "Restaurant");
                intent.putExtra("filter_id", newText);
                return false;
            }
        });
        ShowOffers(UserFeedActivity.this);
        setProfileImage();
        CheckForfirstRejection();
        ShowPopularRestaurants(UserFeedActivity.this);
        ShowCusines();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.popular_locations_recycler);
        ShowPopularLocations(recyclerView);
        ShowHangoutRestaurants(UserFeedActivity.this);
        CircleImageView profile_image__ = (CircleImageView) findViewById(R.id.profile_image);
        profile_image__.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(new SaveSharedPreferences().getAccess_token(UserFeedActivity.this) == null){
                    Intent goToProfile = new Intent(UserFeedActivity.this, ProfileActivity.class);
                    goToProfile.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(goToProfile);
                } else {
                    Intent goToProfile = new Intent(UserFeedActivity.this, ProfileActivity.class);
                    startActivity(goToProfile);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        SearchView searchView = (SearchView) findViewById(R.id.searchView);
        RelativeLayout rootView = (RelativeLayout) findViewById(R.id.toolbar);
        searchView.setQuery("", false);
        rootView.requestFocus();
    }

    public void setProfileImage(){
        SaveSharedPreferences saveSharedPreferences = new SaveSharedPreferences();
        final CircleImageView imageView = (CircleImageView) findViewById(R.id.profile_image);
        String image = saveSharedPreferences.getImage(UserFeedActivity.this);
        if(image == null){
            Uri image_path = Uri.parse("https://www.restroin.in/developers/api/images/blank.png");
            Picasso.get().load(image_path).into(imageView);
        } else {
            Uri image_path = Uri.parse(image);
            Picasso.get().load(image_path).into(imageView);
        }
    }

    public void CheckForfirstRejection(){
        SaveSharedPreferences s = new SaveSharedPreferences();
        if (!s.getFirstLoginRejected(UserFeedActivity.this)) {
            Intent goToLogin = new Intent(UserFeedActivity.this, LoginActivity.class);
            s.setFirstLoginRejected(true, UserFeedActivity.this);
            startActivity(goToLogin);
        }
    }

    @Override
    protected void onStart() {
        CheckForfirstRejection();
        super.onStart();
        setProfileImage();
    }
    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
    public void ShowPopularRestaurants(final Context context){
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.ProgressBar);
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.main_layout);
        progressBar.setVisibility(View.VISIBLE);
        relativeLayout.setVisibility(View.GONE);
        Retrofit retrofit_popular_restaurants = builder.build();
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.popular_restaurants_recycler);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        PopularRestaurantsClient popularRestaurantsClient = retrofit_popular_restaurants.create(PopularRestaurantsClient.class);
        Call<List<PopularRestaurants>> call = popularRestaurantsClient.getPopularRestaurants();
        call.enqueue(new Callback<List<PopularRestaurants>>() {
            @Override
            public void onResponse(@NonNull Call<List<PopularRestaurants>> call,@NonNull Response<List<PopularRestaurants>> response) {
                if(response.isSuccessful()){
                    List<PopularRestaurants> popularRestaurants = response.body();
                    PopularRestaurantsAdapter popularRestaurantsAdapter = new PopularRestaurantsAdapter(popularRestaurants, UserFeedActivity.this);
                    recyclerView.setAdapter(popularRestaurantsAdapter);
                    final ProgressBar progressBar = (ProgressBar) findViewById(R.id.ProgressBar);
                    RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.main_layout);
                    progressBar.setVisibility(View.GONE);
                    relativeLayout.setVisibility(View.VISIBLE);
                    scaleRestaurnats(recyclerView, popularRestaurantsAdapter, popularRestaurants);
                } else {
                    Toast.makeText(UserFeedActivity.this, "Something Went Wrong ", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<PopularRestaurants>> call,@NonNull Throwable t) {
                Toast.makeText(UserFeedActivity.this, "Oops!! Error : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void ShowHangoutRestaurants(final Context context){
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.ProgressBar);
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.main_layout);
        progressBar.setVisibility(View.VISIBLE);
        relativeLayout.setVisibility(View.GONE);
        Retrofit retrofit_popular_restaurants = builder.build();
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.hangout_places_recycler);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        PopularRestaurantsClient popularRestaurantsClient = retrofit_popular_restaurants.create(PopularRestaurantsClient.class);
        Call<List<HangoutRestaurants>> call = popularRestaurantsClient.getHangoutPlaces();
        call.enqueue(new Callback<List<HangoutRestaurants>>() {
            @Override
            public void onResponse(@NonNull Call<List<HangoutRestaurants>> call,@NonNull Response<List<HangoutRestaurants>> response) {
                if(response.isSuccessful()){
                    final List<HangoutRestaurants> popularRestaurants = response.body();
                    HangoutPlacesAdapter popularRestaurantsAdapter = new HangoutPlacesAdapter(popularRestaurants, UserFeedActivity.this);
                    recyclerView.setAdapter(popularRestaurantsAdapter);
                    final ProgressBar progressBar = (ProgressBar) findViewById(R.id.ProgressBar);
                    RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.main_layout);
                    progressBar.setVisibility(View.GONE);
                    relativeLayout.setVisibility(View.VISIBLE);
                    final GestureDetector gestureDetector = new GestureDetector(UserFeedActivity.this, new GestureDetector.OnGestureListener() {
                        @Override
                        public boolean onDown(MotionEvent e) {
                            return false;
                        }

                        @Override
                        public void onShowPress(MotionEvent e) {

                        }

                        @Override
                        public boolean onSingleTapUp(MotionEvent e) {
                            return true;
                        }

                        @Override
                        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                            return false;
                        }

                        @Override
                        public void onLongPress(MotionEvent e) {

                        }

                        @Override
                        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                            return false;
                        }
                    });
                    recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
                        @Override
                        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                            final View offerView = rv.findChildViewUnder(e.getX(), e.getY());
                            final int position = rv.getChildAdapterPosition(offerView);
                            if(offerView != null && gestureDetector.onTouchEvent(e)){
                                offerView.setScaleX((float)0.985);
                                offerView.setScaleY((float) 0.985);
                                Intent intent = new Intent(UserFeedActivity.this, RestaurantViewActivity.class);
                                intent.putExtra("restaurant_id", popularRestaurants.get(position).getRestaurant_id());
                                startActivity(intent);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        offerView.setScaleX((float)1.0);
                                        offerView.setScaleY((float)1.0);
                                    }
                                }, 100);
                            }
                            return false;
                        }

                        @Override
                        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
                        }

                        @Override
                        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

                        }
                    });
                } else {
                    Toast.makeText(UserFeedActivity.this, "Something Went Wrong ", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<HangoutRestaurants>> call,@NonNull Throwable t) {
                Toast.makeText(UserFeedActivity.this, "Oops!! Error : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void ShowOffers(final Context context){
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.ProgressBar);
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.main_layout);
        progressBar.setVisibility(View.VISIBLE);
        relativeLayout.setVisibility(View.GONE);
        Retrofit retrofit_offers = builder.build();
        final RecyclerView offersRecycler = (RecyclerView) findViewById(R.id.offers_recycler_view);
        PagerSnapHelper pagerSnapHelper = new PagerSnapHelper();
        pagerSnapHelper.attachToRecyclerView(offersRecycler);
        final LinearLayoutManager OffersLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        offersRecycler.setLayoutManager(OffersLayoutManager);
        OfferClient offerClient = retrofit_offers.create(OfferClient.class);
        Call<List<Offers>> offersCall = offerClient.getOffers();
        offersCall.enqueue(new Callback<List<in.restroin.restroin.models.Offers>>() {
            @Override
            public void onResponse(Call<List<Offers>> call, Response<List<Offers>> response) {
                if(response.isSuccessful()){
                    List<Offers> offers = response.body();
                    OffersAdapter offersAdapter = new OffersAdapter(offers, context);
                    offersRecycler.setAdapter(offersAdapter);
                    scaleItem(offersRecycler, offersAdapter, offers);
                    final ProgressBar progressBar = (ProgressBar) findViewById(R.id.ProgressBar);
                    RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.main_layout);
                    progressBar.setVisibility(View.GONE);
                    relativeLayout.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(context, "Something Went wrong : " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Offers>> call, Throwable t) {
                Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void scaleItem(RecyclerView recyclerView, final RecyclerView.Adapter adapter, final List<Offers> offers){
        final GestureDetector gestureDetector = new GestureDetector(UserFeedActivity.this, new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return false;
            }

            @Override
            public void onShowPress(MotionEvent e) {

            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                return false;
            }

            @Override
            public void onLongPress(MotionEvent e) {

            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                return false;
            }
        });
        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                final View offerView = rv.findChildViewUnder(e.getX(), e.getY());
                final int position = rv.getChildAdapterPosition(offerView);
                if(offerView != null && gestureDetector.onTouchEvent(e)){
                    offerView.setScaleX((float)0.985);
                    offerView.setScaleY((float) 0.985);
                    Intent intent = new Intent(UserFeedActivity.this, SearchActivity.class);
                    intent.putExtra("filter_type", "Coupon");
                    intent.putExtra("filter_id", offers.get(position).getCoupon_id());
                    startActivity(intent);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            offerView.setScaleX((float)1.0);
                            offerView.setScaleY((float)1.0);
                        }
                    }, 100);
                }
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });
    }

    public void scaleRestaurnats(RecyclerView recyclerView, final RecyclerView.Adapter adapter, final List<PopularRestaurants> offers){
        final GestureDetector gestureDetector = new GestureDetector(UserFeedActivity.this, new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return false;
            }

            @Override
            public void onShowPress(MotionEvent e) {

            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                return false;
            }

            @Override
            public void onLongPress(MotionEvent e) {

            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                return false;
            }
        });
        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                final View offerView = rv.findChildViewUnder(e.getX(), e.getY());
                final int position = rv.getChildAdapterPosition(offerView);
                if(offerView != null && gestureDetector.onTouchEvent(e)){
                    offerView.setScaleX((float)0.985);
                    offerView.setScaleY((float) 0.985);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            offerView.setScaleX((float)1.0);
                            offerView.setScaleY((float)1.0);
                            Intent goToView = new Intent(UserFeedActivity.this, RestaurantViewActivity.class);
                            goToView.putExtra("restaurant_id", offers.get(position).getId());
                            startActivity(goToView);
                        }
                    }, 300);
                }
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });
    }

    public void ShowCusines(){
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.ProgressBar);
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.main_layout);
        progressBar.setVisibility(View.VISIBLE);
        relativeLayout.setVisibility(View.GONE);
        Retrofit Cusines = builder.build();
        final MyGridView cusinesGridView = (MyGridView) findViewById(R.id.cusines_gridView);
        CusinesClient client = Cusines.create(CusinesClient.class);
        Call<List<CusineGridModel>> call = client.getCusines();
        call.enqueue(new Callback<List<CusineGridModel>>() {
            @Override
            public void onResponse(@NonNull Call<List<CusineGridModel>> call,@NonNull Response<List<CusineGridModel>> response) {
                if(response.isSuccessful()){
                    final List<CusineGridModel> cusines = response.body();
                    CusinesGridAdapter cusinesGridAdapter = new CusinesGridAdapter(cusines, UserFeedActivity.this);
                    cusinesGridView.setAdapter(cusinesGridAdapter);
                    final ProgressBar progressBar = (ProgressBar) findViewById(R.id.ProgressBar);
                    RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.main_layout);
                    progressBar.setVisibility(View.GONE);
                    relativeLayout.setVisibility(View.VISIBLE);
                    cusinesGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(UserFeedActivity.this, SearchActivity.class);
                            intent.putExtra("filter_type", "Cuisine");
                            intent.putExtra("filter_id", cusines.get(position).getCusine_id());
                            startActivity(intent);
                        }
                    });
                } else {
                    Toast.makeText(UserFeedActivity.this, "Something went wrong: ", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<CusineGridModel>> call, Throwable t) {
                Toast.makeText(UserFeedActivity.this, "Error :( + " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void ShowPopularLocations(final RecyclerView recyclerView){
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.ProgressBar);
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.main_layout);
        progressBar.setVisibility(View.VISIBLE);
        relativeLayout.setVisibility(View.GONE);
        Retrofit Locations = builder.build();
        LinearLayoutManager layoutManager = new LinearLayoutManager(UserFeedActivity.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        PagerSnapHelper pagerSnapHelper = new PagerSnapHelper();
        pagerSnapHelper.attachToRecyclerView(recyclerView);
        PopularLocationClient client = Locations.create(PopularLocationClient.class);
        Call<List<PopularLocations>> call = client.getPopularLocations();
        call.enqueue(new Callback<List<PopularLocations>>() {
            @Override
            public void onResponse(@NonNull Call<List<PopularLocations>> call,@NonNull Response<List<PopularLocations>> response) {
                if(response.isSuccessful()){
                    final List<PopularLocations> locations = response.body();
                    PopularLocationsAdapter adapter = new PopularLocationsAdapter(locations, UserFeedActivity.this);
                    recyclerView.setAdapter(adapter);
                    final GestureDetector gestureDetector = new GestureDetector(UserFeedActivity.this, new GestureDetector.OnGestureListener() {
                        @Override
                        public boolean onDown(MotionEvent e) {
                            return false;
                        }

                        @Override
                        public void onShowPress(MotionEvent e) {

                        }

                        @Override
                        public boolean onSingleTapUp(MotionEvent e) {
                            return true;
                        }

                        @Override
                        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                            return false;
                        }

                        @Override
                        public void onLongPress(MotionEvent e) {

                        }

                        @Override
                        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                            return false;
                        }
                    });
                    recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
                        @Override
                        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                            final View offerView = rv.findChildViewUnder(e.getX(), e.getY());
                            final int position = rv.getChildAdapterPosition(offerView);
                            if(offerView != null && gestureDetector.onTouchEvent(e)){
                                offerView.setScaleX((float)0.985);
                                offerView.setScaleY((float) 0.985);
                                Intent intent = new Intent(UserFeedActivity.this, SearchActivity.class);
                                intent.putExtra("filter_type", "Location");
                                intent.putExtra("filter_id", locations.get(position).getId());
                                startActivity(intent);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        offerView.setScaleX((float)1.0);
                                        offerView.setScaleY((float)1.0);
                                    }
                                }, 100);
                            }
                            return false;
                        }

                        @Override
                        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
                        }

                        @Override
                        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

                        }
                    });
                    final ProgressBar progressBar = (ProgressBar) findViewById(R.id.ProgressBar);
                    RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.main_layout);
                    progressBar.setVisibility(View.GONE);
                    relativeLayout.setVisibility(View.VISIBLE);
                } else {
                    Log.e(RETROFIT_TAG, "Something Went Wrong: " + response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<PopularLocations>> call,@NonNull Throwable t) {
                Log.e(RETROFIT_TAG, "Something Went Wrong: " + t.getMessage());
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }
}
