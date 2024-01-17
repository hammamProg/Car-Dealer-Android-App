package com.main.project.UI;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.main.project.API.HttpManager;
import com.main.project.API.MainJsonParser;
import com.main.project.Database.DataBaseHelper;
import com.main.project.Objects.User;
import com.main.project.R;
import com.main.project.Screens.Auth.Login;

import java.util.List;
import java.util.stream.IntStream;

public class AdminDashboard extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dashboard_admin, container, false);

        return root;
    }

}