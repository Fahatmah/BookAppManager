package com.activity.bookappmanager.service;

import com.activity.bookappmanager.BookResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GoogleBooksService {
    @GET("volumes")
    Call<BookResponse> getBooks(@Query("q") String query);
}
