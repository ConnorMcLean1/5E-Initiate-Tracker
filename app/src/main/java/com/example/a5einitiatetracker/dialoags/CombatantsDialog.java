package com.example.a5einitiatetracker.dialoags;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.fragment.app.DialogFragment;

import com.example.a5einitiatetracker.R;
import com.example.a5einitiatetracker.activities.CombatActivity;
import com.example.a5einitiatetracker.combatant.NPC;

import java.util.ArrayList;
import java.util.Objects;

public class CombatantsDialog extends DialogFragment {
    private ArrayList<NPC> npcArrList = new ArrayList<>(CombatActivity.npcs);
    int damageAmount;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.npc_combatant_dialog_layout, null);
        ListView npcListView = view.findViewById(R.id.npcListView);
        NPCsAdapter adapter = new NPCsAdapter(Objects.requireNonNull(this.getContext()), npcArrList);
        npcListView.setAdapter(adapter);
        builder.setView(view)
                .setTitle("Select NPC(s) to Damage")
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        return builder.create();
    }

    public void setDamageAmount(int damageAmount) {
        this.damageAmount = damageAmount;
    }
}
