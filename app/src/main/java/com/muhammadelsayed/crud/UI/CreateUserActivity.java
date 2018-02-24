package com.muhammadelsayed.crud.UI;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.muhammadelsayed.crud.API.model.User;
import com.muhammadelsayed.crud.API.service.UserClient;
import com.muhammadelsayed.crud.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CreateUserActivity extends AppCompatActivity {

    private static final String LOG_TAG = CreateUserActivity.class.getName();
    private static final String SERVER_URL = "http://18.194.70.123";

    EditText name;
    EditText email;
    EditText password;
    Button create;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);
        Log.i(LOG_TAG, "TEST: onCreate has been triggered");


        //final ListView usersListView = findViewById(R.id.list);
        //adapter = new UsersAdapter(getApplicationContext(), new ArrayList<User>());
        //usersListView.setAdapter(adapter);

        name = findViewById(R.id.create_name_edit_text);
        email = findViewById(R.id.create_email_edit_text);
        password = findViewById(R.id.create_password_edit_text);
        create = findViewById(R.id.create_button);


        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = new User(
                        name.getText().toString(),
                        email.getText().toString(),
                        password.getText().toString()
                );

                sendNetworkRequest(user);
            }
        });
    }

    private void sendNetworkRequest(User user) {
        Log.i(LOG_TAG, "TEST: sendNetworkRequest has been triggered");

        Log.i(LOG_TAG, "TEST: configuring Retrofit.Builder.");
        // Create Retrofit instance.
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create());

        Log.i(LOG_TAG, "TEST: Creating Retrofit instance.");
        Retrofit retrofit = builder.build();

        // Get client & call retrofit object for the request.
        UserClient client = retrofit.create(UserClient.class);
        Call<Void> call = client.createAccount(user);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.i(LOG_TAG, "New user account created successfully on the server.");
                Toast.makeText(CreateUserActivity.this, "Done, Yeah! :)", Toast.LENGTH_SHORT).show();
                finish();
            }


            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.wtf(LOG_TAG, "Error while Creating an new account.");
                Toast.makeText(CreateUserActivity.this, "something went wrong :(", Toast.LENGTH_SHORT).show();
            }
        });

    }

}
