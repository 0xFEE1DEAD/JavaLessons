package com.example.retrofit2stub;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TableLayout;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class MainActivity extends AppCompatActivity {

    String API_URL = "https://pixabay.com/";
    String q = "bad dog";
    String key = "8831692-b1c2e4ac049f22756c0be1b6c";
    String image_type = "photo";
    Picasso picasso;
    GridView gridView;
    GridViewAdapter adapter;
    ImageView errorImage;
    LinearLayout centerLayout;
    ProgressBar progressBar;
    EditText etSearch;


    interface PixabayAPI {
        @GET("/api")
        Call<Response> search(@Query("q") String q, @Query("key") String key, @Query("image_type") String image_type);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        picasso = new Picasso.Builder(this).build();
        gridView = findViewById(R.id.gridView);
        adapter = new GridViewAdapter(this, picasso);
        gridView.setAdapter(adapter);
        errorImage = findViewById(R.id.errorImage);
        centerLayout = findViewById(R.id.CenterLayout);
        progressBar = findViewById(R.id.loadingBar);
        etSearch = findViewById(R.id.text);
    }

    public void startSearch(String text) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PixabayAPI api = retrofit.create(PixabayAPI.class);

        Call<Response> call = api.search(text, key, image_type);

        Callback<Response> callback = new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                Response r = response.body();
                displayResults(r.hits);
                Log.d("mytag", "hits:" + r.hits.length);
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                visibleSwitcher(centerLayout,true);
                visibleSwitcher(errorImage, true);
                visibleSwitcher(progressBar, false);
                visibleSwitcher(gridView, false);
                Log.d("mytag", "Error: " + t.getLocalizedMessage());
            }
        };
        call.enqueue(callback);

    }

    public void displayResults(Hit[] hits) {
        visibleSwitcher(centerLayout,false);
        visibleSwitcher(progressBar, false);
        visibleSwitcher(errorImage, false);
        visibleSwitcher(gridView, true);
        adapter.images = new ArrayList<>(Arrays.asList(hits));
        adapter.notifyDataSetChanged();
    }

    public void onSearchClick(View v) {
        String text = etSearch.getText().toString();
        visibleSwitcher(centerLayout,true);
        visibleSwitcher(progressBar, true);
        visibleSwitcher(errorImage, false);
        visibleSwitcher(gridView, false);
        startSearch(text);
    }

    void visibleSwitcher(View el, boolean visible) {
        if(el == null) {
            return;
        }
        if(el.getVisibility() == View.VISIBLE && !visible) {
            el.setVisibility(View.GONE);
        }
        if(el.getVisibility() == View.GONE && visible) {
            el.setVisibility(View.VISIBLE);
        }
    }
}
