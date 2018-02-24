package com.muhammadelsayed.crud.UI.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.muhammadelsayed.crud.API.model.User;
import com.muhammadelsayed.crud.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Muhammad Elsayed on 11/26/2017.
 */

public class UsersAdapter extends ArrayAdapter<User> {

    private static final String LOG_TAG = UsersAdapter.class.getName();

    private Context context;
    private List<User> users;

    public UsersAdapter(@NonNull Context context, List<User> users) {
        super(context, R.layout.list_item, users);

        this.context = context;
        this.users = users;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View usersListItems = convertView;

        if (usersListItems == null) {
            //usersListItems = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
            LayoutInflater inflater =
                    (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            usersListItems = inflater.inflate(R.layout.list_item, parent, false);
        }

        User currentUser = getItem(position);

        TextView nameView = usersListItems.findViewById(R.id.name_text_view);
        nameView.setText(currentUser.getName());

        TextView emailView = usersListItems.findViewById(R.id.email_text_view);
        emailView.setText(currentUser.getEmail());

        String[] dateArray = handleDate(currentUser.getCreated_at());

        TextView dateView = usersListItems.findViewById(R.id.date_text_view);
        String date = formateDate(dateArray[0]);
        dateView.setText(date);


        TextView timeView = usersListItems.findViewById(R.id.time_text_view);
        String time = formatTime(dateArray[1]);
        timeView.setText(time);


        return usersListItems;
    }

    private String[] handleDate(String date) {
        // Date format: 2017-11-19T20:00:56Z
        String uDate = date.substring(0, 10);
        String uTime = date.substring(11, 16);
        String[] formattedDate = {uDate, uTime};
        return formattedDate;
    }

    private String formateDate(String date) {
        DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        DateFormat targetFormat = new SimpleDateFormat("LLL dd, yyyy");
        Date uDate = null;
        try {
            uDate = originalFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String formattedDate = targetFormat.format(uDate);
        return formattedDate;
    }

    private String formatTime(String time) {
        DateFormat originalFormat = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
        DateFormat targetFormat = new SimpleDateFormat("h:mm a");
        Date uTime = null;
        try {
            uTime = originalFormat.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String formattedTime = targetFormat.format(uTime);
        return formattedTime;
    }

}
