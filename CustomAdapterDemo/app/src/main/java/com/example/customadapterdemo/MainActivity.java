package com.example.customadapterdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import static com.example.customadapterdemo.Sex.MAN;


public class MainActivity extends AppCompatActivity {

    UserListAdapter adapter;
    ListView listView;
    Button sortSexBtn;
    Button sortNameBtn;
    Button sortPhoneNumberBtn;
    boolean sortBySex;
    boolean sortByName;
    boolean sortByPhoneNumber;
    Comparator<User> sexComparator;
    Comparator<User> nameComparator;
    Comparator<User> phoneNumberComparator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.list);
        sortBySex = false;
        sortByName = false;
        sortByPhoneNumber = false;

        sortSexBtn = findViewById(R.id.sortSexBtn);
        sortNameBtn = findViewById(R.id.sortNameBtn);
        sortPhoneNumberBtn = findViewById(R.id.sortPhoneNumberBtn);

        sexComparator = new Comparator<User>() {
            public int compare(User one, User two) {
                int oneI = 0;
                int twoI = 0;
                switch (one.sex) {
                    case MAN: oneI = 0; break;
                    case WOMAN: oneI = 1; break;
                    case UNKNOWN: oneI = 2; break;
                }
                switch (two.sex) {
                    case MAN: twoI = 0; break;
                    case WOMAN: twoI = 1; break;
                    case UNKNOWN: twoI = 2; break;
                }
                return oneI - twoI;
            }
        };

        nameComparator = new Comparator<User>() {
            public int compare(User one, User two) {
                return one.name.compareTo(two.name);
            }
        };

        phoneNumberComparator = new Comparator<User>() {
            public int compare(User one, User two) {
                return one.phoneNumber.compareTo(two.phoneNumber);
            }
        };

        String jsonString = getStringFromFile("json.json");

        Gson gson = new Gson();
        User[] usersRaw = gson.fromJson(jsonString, User[].class);
        ArrayList<User> users = new ArrayList<>(Arrays.asList(usersRaw));

        adapter = new UserListAdapter(this, users);
        listView.setAdapter(adapter);

        sortPhoneNumberBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Collections.sort(adapter.users, phoneNumberComparator);
                if(sortBySex) {
                    Collections.reverse(adapter.users);
                }
                adapter.notifyDataSetChanged();

                sortBySex = !sortBySex;
                sortByName = false;
                sortByPhoneNumber = false;
            }
        });

        sortNameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Collections.sort(adapter.users, nameComparator);
                if(sortByName) {
                    Collections.reverse(adapter.users);
                }
                adapter.notifyDataSetChanged();
                sortBySex = false;
                sortByName = !sortByName;
                sortByPhoneNumber = false;
            }
        });

        sortSexBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Collections.sort(adapter.users, sexComparator);
                if(sortByPhoneNumber) {
                    Collections.reverse(adapter.users);
                }
                adapter.notifyDataSetChanged();
                sortBySex = false;
                sortByName = false;
                sortByPhoneNumber = !sortByPhoneNumber;
            }
        });
    }

    private String getStringFromFile(String filename) {
        byte[] buffer = null;
        InputStream is;
        try {
            is = getAssets().open(filename);
            int size = is.available();
            buffer = new byte[size];
            is.read(buffer);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String str_data = new String(buffer);
        return str_data;
    }
}
