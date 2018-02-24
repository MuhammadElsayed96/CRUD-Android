package com.muhammadelsayed.crud.API.model;

/**
 * Created by Muhammad Elsayed on 11/26/2017.
 */

public class User {

    private int id;
    private String name;
    private String email;
    private String password;
    private String created_at;
    private String updated_at;


    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public User(int id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public User(int id, String name, String email, String timeCreated, String timeUpdated) {
        this.id = id;
        this.name = name;
        this.email = email;
        created_at = timeCreated;
        updated_at = timeUpdated;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }
}
