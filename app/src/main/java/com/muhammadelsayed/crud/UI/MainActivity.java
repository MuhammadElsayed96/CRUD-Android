package com.muhammadelsayed.crud.UI;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.muhammadelsayed.crud.API.model.User;
import com.muhammadelsayed.crud.API.service.UserClient;
import com.muhammadelsayed.crud.R;
import com.muhammadelsayed.crud.UI.adapter.UsersAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getName();
    private static final String SERVER_URL = "http://52.224.66.22/abdullah/crud";
    private UsersAdapter adapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private TextView noInternetConnectionTextView;
    private Call<List<User>> call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i(LOG_TAG, "TEST: onCreate has been triggered.");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent createUserIntent = new Intent(MainActivity.this, CreateUserActivity.class);
                startActivity(createUserIntent);
            }
        });


        final ListView usersListView = findViewById(R.id.list);
        noInternetConnectionTextView = findViewById(R.id.no_internet_state);
        noInternetConnectionTextView.setText(R.string.empty_string);
        adapter = new UsersAdapter(getApplicationContext(), new ArrayList<User>());
        usersListView.setEmptyView(noInternetConnectionTextView);
        usersListView.setAdapter(adapter);

        usersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User currentUser = adapter.getItem(position);
                Intent editViewIntent = new Intent(getApplicationContext(), EditUserActivity.class);
                editViewIntent.putExtra("userID", currentUser.getId());
                editViewIntent.putExtra("userName", currentUser.getName());
                editViewIntent.putExtra("userEmail", currentUser.getEmail());

                startActivity(editViewIntent);
            }
        });


        usersListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                AlertDialog.Builder alert = new AlertDialog.Builder(
                        MainActivity.this);
                alert.setTitle("Alert!!");
                alert.setMessage("Are you sure to delete record?");

                alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        User currentUser = adapter.getItem(position);
                        int currentUserID = currentUser.getId();
                        sendNetworkRequestToDelete(currentUserID);
                    }
                });

                alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                });

                alert.show();

                return true;
            }
        });

        mSwipeRefreshLayout = findViewById(R.id.swiperefresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                sendNetworkRequestToFetch();
            }
        });


        // Checking the network connectivity.
        ConnectivityManager cm =
                (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        if (isConnected) {
            sendNetworkRequestToFetch();

        } else {
            Log.i(LOG_TAG, "No Internet connection.");
            noInternetConnectionTextView.setText(R.string.no_internet_connection);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void sendNetworkRequestToFetch() {
        Log.i(LOG_TAG, "TEST: sendNetworkRequestToFetch has been triggered");

        Log.i(LOG_TAG, "TEST: configuring Retrofit.Builder.");
        // To configure Retrofit easily I used Retrofit.Builder.
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create());

        Log.i(LOG_TAG, "TEST: Creating Retrofit object.");
        // Now it's time to create the actual Retrofit object.
        Retrofit retrofit = builder.build();


        Log.i(LOG_TAG, "TEST: Making a Retrofit request.");
        // It's time to do the actual request.
        UserClient client = retrofit.create(UserClient.class);
        call = client.getDataOfUser();

        // The final step here is to utilize this call object. either sync ot async.
        // Make an async call to avoid the UI thread freezing, so call it async in the background.
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                Log.i(LOG_TAG, "Data retrieved successfully from the server.");
                List<User> usersList = response.body();
                adapter.clear();
                mSwipeRefreshLayout.setRefreshing(false);
                adapter.addAll(usersList);

            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.wtf(LOG_TAG, "Error while retrieving Data from the server.");
                Toast.makeText(MainActivity.this, "Error :(", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendNetworkRequestToDelete(int id) {
        Log.i(LOG_TAG, "TEST: sendNetworkRequestToDelete has been triggered");

        Log.i(LOG_TAG, "TEST: configuring Retrofit.Builder.");
        // Create Retrofit instance.
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create());

        Log.i(LOG_TAG, "TEST: Creating Retrofit instance.");
        Retrofit retrofit = builder.build();

        // Get client & call retrofit object for the request.
        UserClient client = retrofit.create(UserClient.class);
        Call<Void> call = client.deleteUser(id);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.i(LOG_TAG, "user account deleted successfully from the server.");
                Toast.makeText(MainActivity.this, "Deleted!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e(LOG_TAG, "Error deleting user account from the server.");
                Toast.makeText(MainActivity.this, "Error :(", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
