package com.muhammadelsayed.crud.API.service;

import com.muhammadelsayed.crud.API.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Muhammad Elsayed on 11/30/2017.
 */

public interface UserClient {
    @GET("/api/users")
    Call<List<User>> getDataOfUser();

    @POST("/api/users")
    Call<Void> createAccount(@Body User user);

    @DELETE("/api/users/{id}")
    Call<Void> deleteUser(@Path("id") int itemId);


    @PATCH("/api/users/{id}")
    Call<Void> updateUser(
            @Path("id") String id,
            @Body User user);
}
