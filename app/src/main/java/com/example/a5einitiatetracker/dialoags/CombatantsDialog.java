package com.example.a5einitiatetracker.dialoags;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;

import androidx.fragment.app.DialogFragment;

import com.example.a5einitiatetracker.R;
import com.example.a5einitiatetracker.activities.CombatActivity;
import com.example.a5einitiatetracker.combatant.NPC;

import java.util.ArrayList;
import java.util.Objects;

public class CombatantsDialog extends DialogFragment {
    private ArrayList<NPC> npcArrList = new ArrayList<>(CombatActivity.npcs);
    ListView npcListView;
    int damage;
    private CombatantsDialogListener listener;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.npc_combatant_dialog_layout, null);
        npcListView = view.findViewById(R.id.npcListView);
        NPCsAdapter adapter = new NPCsAdapter(Objects.requireNonNull(this.getContext()), npcArrList);
        npcListView.setAdapter(adapter);
        builder.setView(view)
                .setTitle("Select NPC(s) to Damage")
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.returnCombatantsList(getCheckedNPCs(), damage);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        return builder.create();
    }

    public interface CombatantsDialogListener {
        void returnCombatantsList(ArrayList<NPC> checkedNPCs, int damage);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (CombatantsDialogListener) context;
        } catch (Exception e) {
            throw new ClassCastException(context.toString() +
                    "must implement CombatantsDialogListener");
        }
    }

    private ArrayList<NPC> getCheckedNPCs() {
        ArrayList<NPC> checkedNPCs = new ArrayList<>();
        CheckBox cb;
        View v;
        for (int i = 0; i < npcArrList.size(); i++) {
            v = npcListView.getChildAt(i);
            cb = v.findViewById(R.id.npcListItemChkBox);
            if (cb.isChecked()) {
                checkedNPCs.add(npcArrList.get(i));
            }
        }
        return checkedNPCs;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }
}
