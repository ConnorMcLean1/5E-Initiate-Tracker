package com.example.a5einitiatetracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class MonstersActivity extends AppCompatActivity {

    private LinearLayout parentLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monsters);

        parentLinearLayout = findViewById(R.id.parent_linear_layout);

        Button addMonsterButton = findViewById(R.id.btnAddMonster);
        addMonsterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View rowView = inflater.inflate(R.layout.monster_entry_layout, null);
                parentLinearLayout.addView(rowView, parentLinearLayout.getChildCount() - 1);
            }
        });
    }

    public void onDelete(View v) {
        parentLinearLayout.removeView((View) v.getParent());
    }
}
