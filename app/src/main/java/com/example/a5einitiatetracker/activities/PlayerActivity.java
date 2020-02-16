package com.example.a5einitiatetracker.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;

import com.example.a5einitiatetracker.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class PlayerActivity extends AppCompatActivity  {

    private LinearLayout parentLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        parentLinearLayout = findViewById(R.id.lnrLayoutPlayers);
    }

    public void onAddNewPlayer(View v) {

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.player_entry_layout, null);
        parentLinearLayout.addView(rowView, parentLinearLayout.getChildCount() - 1);

    }

    public void onDelete(View v) {
        parentLinearLayout.removeView((View) v.getParent());
    }
}
