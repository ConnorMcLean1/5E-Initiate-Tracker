package com.example.a5einitiatetracker.dialoags;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.a5einitiatetracker.R;
import com.example.a5einitiatetracker.combatant.NPC;

import java.util.List;
import java.util.Locale;

public class NPCsAdapter extends ArrayAdapter {

    NPCsAdapter(@NonNull Context context, @NonNull List<NPC> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        NPC npc = (NPC) getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.npc_damage_list_item, parent, false);
        }

        TextView monsterName = convertView.findViewById(R.id.npcListItemName);
        TextView monsterHealth = convertView.findViewById(R.id.npcListItemHealth);
        TextView monsterAC = convertView.findViewById(R.id.npcListItemAC);

        assert npc != null;
        monsterName.setText(npc.getName());
        monsterHealth.setText(String.format(Locale.CANADA,"%d/%d", npc.getHealth(), npc.getMaxHealth()));
        monsterAC.setText(String.valueOf(npc.getArmourClass()));

        return convertView;
    }
}
