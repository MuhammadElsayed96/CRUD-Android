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

public class EditUserActivity extends AppCompatActivity {

    private static final String LOG_TAG = EditUserActivity.class.getName();
    private static final String SERVER_URL = "http://18.194.70.123";

    EditText name;
    EditText email;
    EditText password;
    Button update;

    int userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);
        Log.i(LOG_TAG, "TEST: onCreate has been triggered");

        name = findViewById(R.id.name_edit_text);
        email = findViewById(R.id.email_edit_text);
        password = findViewById(R.id.password_edit_text);
        update = findViewById(R.id.update_button);

        name.setText(getIntent().getExtras().getString("userName"));
        email.setText(getIntent().getExtras().getString("userEmail"));
        userID = getIntent().getExtras().getInt("userID");

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendNetworkRequestToUpdate(
                        new User(
                                userID,
                                name.getText().toString(),
                                email.getText().toString(),
                                password.getText().toString())
                );

            }
        });
    }


    private void sendNetworkRequestToUpdate(User user) {
        Log.i(LOG_TAG, "TEST: sendNetworkRequestToUpdate has been triggered");

        Log.i(LOG_TAG, "TEST: configuring Retrofit.Builder.");
        // Create Retrofit instance.
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create());

        Log.i(LOG_TAG, "TEST: Creating Retrofit instance.");
        Retrofit retrofit = builder.build();

        // Get client & call retrofit object for the request.
        UserClient client = retrofit.create(UserClient.class);
        Call<Void> call = client.updateUser(String.valueOf(user.getId()), user);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.i(LOG_TAG, "User account updated successfully on the server." + response.toString());
                Toast.makeText(EditUserActivity.this, "Updated!", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e(LOG_TAG, "Error updating user account on the server.");
                Toast.makeText(EditUserActivity.this, "Error :(", Toast.LENGTH_SHORT).show();
            }
        });


    }
}
