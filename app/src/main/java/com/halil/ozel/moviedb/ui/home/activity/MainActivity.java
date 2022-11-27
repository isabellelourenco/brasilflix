package com.halil.ozel.moviedb.ui.home.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.halil.ozel.moviedb.App;
import com.halil.ozel.moviedb.R;
import com.halil.ozel.moviedb.data.Api.TMDbAPI;
import com.halil.ozel.moviedb.data.models.Results;
import com.halil.ozel.moviedb.ui.home.adapters.BannerAdapter;
import com.halil.ozel.moviedb.ui.home.adapters.MovieAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

import static com.halil.ozel.moviedb.data.Api.TMDbAPI.TMDb_API_KEY;


public class MainActivity extends AppCompatActivity {

    @Inject
    TMDbAPI tmDbAPI;

    public RecyclerView rvPopularMovie;
    public RecyclerView.Adapter popularMovieAdapter;
    public RecyclerView.LayoutManager popularMovieLayoutManager;
    public List<Results> popularMovieDataList;

    public RecyclerView rvNowPlaying;
    public RecyclerView.Adapter nowPlayingMovieAdapter;
    public RecyclerView.LayoutManager nowPlayingLayoutManager;
    public List<Results> nowPlayingDataList;

    public RecyclerView rvNewMovie;
    public RecyclerView.Adapter newMovieAdapter;
    public RecyclerView.LayoutManager newLayoutManager;
    public List<Results> newMovieDataList;

    public RecyclerView rvNewMovieCarousel;
    public RecyclerView.Adapter newMovieAdapterCarousel;
    public RecyclerView.LayoutManager newLayoutManagerCarousel;
    public List<Results> newMovieDataListCarousel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.instance().appComponent().inject(this);
        setContentView(R.layout.activity_main);

        popularMovieDataList = new ArrayList<>();
        popularMovieAdapter = new MovieAdapter(popularMovieDataList, this);
        popularMovieLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        rvPopularMovie = findViewById(R.id.rvPopularMovie);
        rvPopularMovie.setHasFixedSize(true);
        rvPopularMovie.setLayoutManager(popularMovieLayoutManager);
        rvPopularMovie.setAdapter(popularMovieAdapter);

        nowPlayingDataList = new ArrayList<>();
        nowPlayingMovieAdapter = new MovieAdapter(nowPlayingDataList, this);
        nowPlayingLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        rvNowPlaying = findViewById(R.id.rvNowPlaying);
        rvNowPlaying.setHasFixedSize(true);
        rvNowPlaying.setLayoutManager(nowPlayingLayoutManager);
        rvNowPlaying.setAdapter(nowPlayingMovieAdapter);

        newMovieDataList = new ArrayList<>();
        newMovieAdapter = new MovieAdapter(newMovieDataList, this);
        newLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        rvNewMovie = findViewById(R.id.rvNewMovie);
        rvNewMovie.setHasFixedSize(true);
        rvNewMovie.setLayoutManager(newLayoutManager);
        rvNewMovie.setAdapter(newMovieAdapter);

        newMovieDataListCarousel = new ArrayList<>();
        newMovieAdapterCarousel = new BannerAdapter(newMovieDataList, this);
        newLayoutManagerCarousel = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        rvNewMovieCarousel = findViewById(R.id.rvNewMovieCarousel);
        rvNewMovieCarousel.setHasFixedSize(true);
        rvNewMovieCarousel.setLayoutManager(newLayoutManagerCarousel);
        rvNewMovieCarousel.setAdapter(newMovieAdapterCarousel);

        getPopularMovies();
        getNowPlaying();
        getNewMovies();
        getNewMoviesCarousel();
    }


    @SuppressLint("NotifyDataSetChanged")
    public void getNowPlaying() {
        tmDbAPI.getNowPlaying(TMDb_API_KEY, 1).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(response -> {

            popularMovieDataList.addAll(response.getResults());

            popularMovieAdapter.notifyDataSetChanged();

        }, e -> Timber.e(e, "Error fetching now popular movies: %s", e.getMessage()));
    }

    @SuppressLint("NotifyDataSetChanged")
    public void getPopularMovies() {
        tmDbAPI.getPopularMovie(TMDb_API_KEY, 1).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(response -> {

            nowPlayingDataList.addAll(response.getResults());

            nowPlayingMovieAdapter.notifyDataSetChanged();

        }, e -> Timber.e(e, "Error fetching now popular movies: %s", e.getMessage()));
    }

    @SuppressLint("NotifyDataSetChanged")
    public void getNewMovies() {
        tmDbAPI.getNewMovie(TMDb_API_KEY, 1).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(response -> {

            newMovieDataList.addAll(response.getResults());

            newMovieAdapter.notifyDataSetChanged();

        }, e -> Timber.e(e, "Error fetching now popular movies: %s", e.getMessage()));
    }

    @SuppressLint("NotifyDataSetChanged")
    public void getNewMoviesCarousel() {
        tmDbAPI.getNewMovie(TMDb_API_KEY, 1).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(response -> {

            newMovieDataListCarousel.addAll(response.getResults());

            newMovieAdapterCarousel.notifyDataSetChanged();

        }, e -> Timber.e(e, "Error fetching now popular movies: %s", e.getMessage()));
    }

}