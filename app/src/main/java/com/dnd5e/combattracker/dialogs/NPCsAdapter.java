package com.dnd5e.combattracker.dialogs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.dnd5e.combattracker.R;
import com.dnd5e.combattracker.combatant.NPC;

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
        monsterHealth.setText(String.format(Locale.CANADA,"%s: %d/%d", "HP", npc.getHealth(), npc.getMaxHealth()));
        monsterAC.setText(String.format(Locale.CANADA,"%s: %d", "AC", npc.getArmourClass()));

        return convertView;
    }
}
