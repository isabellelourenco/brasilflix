package com.halil.ozel.moviedb.dagger.modules;

import android.app.Application;

import com.halil.ozel.moviedb.dagger.AppScope;
import com.halil.ozel.moviedb.data.Api.TMDbAPI;
import com.squareup.moshi.Moshi;

import java.io.File;
import java.util.concurrent.TimeUnit;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.moshi.MoshiConverterFactory;

@Module
public class HttpClientModule {

    private static final long DISK_CACHE_SIZE = 50 * 1024 * 1024;
    public static final String TMDb_API_URL = "https://api.themoviedb.org/3/";
    public static final String NOW_ON_PLAYING = "https://api.themoviedb.org/3/discover/movie/?with_original_language=pt&vote_average.gte=8";
    public static final String POPULAR = "discover/movie/?with_original_language=pt";
    public static final String MOVIE_DETAILS = "movie/";
    public static final String NEW_MOVIES = "discover/movie/?with_original_language=pt&primary_release_year=2022";

    @Provides
    @AppScope
    public OkHttpClient provideOkHttpClient(Application app) {
        File cacheDir = new File(app.getCacheDir(), "https");
        return new OkHttpClient.Builder()
                .readTimeout(1, TimeUnit.MINUTES)
                .connectTimeout(1, TimeUnit.MINUTES)
                .writeTimeout(1, TimeUnit.MINUTES)
                .cache(new okhttp3.Cache(cacheDir, DISK_CACHE_SIZE))
                .build();


    }

    @Provides
    @AppScope
    public Retrofit provideRestAdapter(MoshiConverterFactory moshiConverterFactory, OkHttpClient okHttpClient) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        okHttpClient = okHttpClient.newBuilder()
                .addInterceptor(interceptor)
                .build();

        return new Retrofit.Builder()
                .baseUrl(TMDb_API_URL)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(moshiConverterFactory)
                .build();
    }


    @Provides
    public TMDbAPI provideApi(Retrofit restAdapter) {
        return restAdapter.create(TMDbAPI.class);
    }

    @Provides
    @AppScope
    public MoshiConverterFactory provideMoshiConverterFactory() {

        Moshi moshi = new Moshi.Builder().build();

        return MoshiConverterFactory.create(moshi);
    }
}
