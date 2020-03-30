package com.example.a5einitiatetracker.api.json;

import android.content.Context;
import android.util.JsonReader;
import android.util.JsonWriter;
import android.util.Log;

import com.example.a5einitiatetracker.api.MonsterName;
import com.example.a5einitiatetracker.combatant.Combatant;
import com.example.a5einitiatetracker.combatant.Player;
import com.example.a5einitiatetracker.combatant.NPC;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JSONUtility {

    public static final String JSON_FILE_NAME = "MonsterListJSON";
    public static final String JSON_COMBAT_SAVED_FILE_NAME = "SavedCombatJSON";
    public static final String JSON_COMBAT_CURRENT_FILE_NAME = "CurrentCombatJSON";

    //Creates a new file with the specified context and filename if one does not already exist
    public static void createFile(Context context, String filename){
        File file = new File(context.getFilesDir(), filename);

        if(!file.exists()){
            try {
                file.createNewFile();
            }
            catch (Exception e){
                Log.e("FILE_CREATION", "Error: " + e.getLocalizedMessage());
                e.printStackTrace();
            }
        }
    }

    //Stores a list of MonsterName objects to a JSON File
    public static void storeMonstersToJSON(List<MonsterName> list, File file){
        //region VARIABLES
        FileWriter fw;
        JsonWriter jw;
        JSONArray arr = MonsterNamesToJSONArray(list);
        //endregion

        //region TRY
        try{
            //region LOCAL_VARIABLES
            fw = new FileWriter(file.getAbsoluteFile(), false);
            jw = new JsonWriter(fw);

            String name;
            JSONArray tempArr;
            JSONObject tempOBJ;
            //endregion

            //write the JSONArray of JSONObjects to the file
            jw.beginArray();
            //Outer loops writes the objects
            for(int i = 0; i < arr.length(); i++){
                jw.beginObject();
                tempOBJ = arr.getJSONObject(i);


                tempArr = tempOBJ.names();
                //Inner loop writes the objects names/values
                for(int j = 0; j < tempArr.length(); j++) {
                    name = tempArr.getString(j);
                    jw.name(name).value(tempOBJ.getString(name));
                    //Log.d("JSON_TEST", "" + tempOBJ.getString("Index"));
                    //Log.d("JSON_TEST", "" + tempOBJ.getString("Name"));
                }
                jw.endObject();
            }
            jw.endArray();
            jw.flush();

            //Log.d("JSON_TEST", "The information stored to the file is: " + arr.toString());
        }
        //endregion

        //region CATCH
        catch (Exception e){
            Log.e("FILE_WRITING", "Error: " + e.getLocalizedMessage());
            e.printStackTrace();
        }
        //endregion
    }

    //converts a list of MonsterNames to a JSONArray to make it easier to write to a JSONFile
    static JSONArray MonsterNamesToJSONArray(List<MonsterName> list){
        //region VARIABLES
        JSONArray arr = new JSONArray();
        JSONObject obj;
        //endregion

        try {
            //region LOOP
            for (int i = 0; i < list.size(); i++) {
                obj = new JSONObject();
                obj.put("Index", list.get(i).getIndex());
                obj.put("Name", list.get(i).getName());
                arr.put(obj);
            }
        }
        catch (Exception e){
            Log.e("JSON_CONVERTER", "Error converting MonsterName list to JSON Object: "
                    + e.getLocalizedMessage());
        }
        //endregion

        return arr;
    }

    public static HashMap<String, String> readMonsternamesFromJSONFile(Context context, String filename){
        File file;
        FileReader fr;
        JsonReader jr;

        JSONArray tempArr = new JSONArray();
        JSONObject tempObj;
        String name, Index, monstName;

        try{
            file = new File(context.getFilesDir(), filename);
            fr = new FileReader(file);
            jr = new JsonReader(fr);

            jr.beginArray();
            while(jr.hasNext()){
                jr.beginObject();
                tempObj = new JSONObject();
                for(int i = 0; i < 2; i++){
                    name = jr.nextName();
                    //Log.d("JSON_TEST", "Out of loop Index = " + name);
                    if(name.equals("Index")){
                        Index = jr.nextString();
                        tempObj.put(name, Index);
                        //Log.d("JSON_TEST", "In loop Index = " + Index);
                    }
                    else if(name.equals("Name")){
                        monstName = jr.nextString();
                        tempObj.put(name, monstName);
                        //Log.d("JSON_TEST", "In loop Name = " + monstName);
                    }
                }
                jr.endObject();
                tempArr.put(tempObj);
            }
            jr.endArray();
            jr.close();

            Log.d("JSON_TEST", "The information read from the file is: " + tempArr.toString());

            return convertJSONtoHashMap(tempArr);
        }
        catch (Exception e){
            Log.e("JSON_CONVERTER", "Error reading JSON File to convert to list: " + e.getLocalizedMessage());
            e.printStackTrace();

            return null;
        }
    }

    private static HashMap<String, String> convertJSONtoHashMap(JSONArray arr) {
        HashMap<String, String> map = new HashMap<>();
        JSONObject jsonObject;

        try {
            for (int i = 0; i < arr.length(); i++) {
                jsonObject = arr.getJSONObject(i);
                map.put(jsonObject.getString("Name"), jsonObject.getString("Index"));
            }
            Log.d("JSON_TEST", "The information converted from JSON is: " + map.toString());
            return map;
        }
        catch (Exception e){
            Log.e("JSON_CONVERTER", "Error converting JSON to MonsterName list: " + e.getLocalizedMessage());
            e.printStackTrace();
            return null;
        }
    }

    public static boolean saveCombatToJSON(List<Combatant> combatantList, int position, String filename, Context context){
        //File and writer variables
        FileWriter fw;
        File file;

        Combatant currCombatant;
        NPC npc;

        Boolean isNPC;

        try{
            //Creates the file if it does not already exist
            createFile(context, filename);

            file = new File(context.getFilesDir(), filename);
            fw = new FileWriter(file.getAbsoluteFile(), false);

            JSONObject positionObj = new JSONObject();
            JSONArray JSONCombatantsArr = new JSONArray();
            positionObj.put("position", position);
            JSONCombatantsArr.put(positionObj);

            JSONObject combatantObj = null;
            JSONArray deathSavesJSONArr = null;
            NPC.deathSaveResult[] deathSavesArr = new NPC.deathSaveResult[6];
            for(int i = 0; i < combatantList.size(); i++){
                combatantObj = new JSONObject();
                deathSavesJSONArr = new JSONArray();
                currCombatant = combatantList.get(i);
                combatantObj.put("name", currCombatant.getName());
                combatantObj.put("initiativeModifier", currCombatant.getInitiativeModifier());
                combatantObj.put("initiative", currCombatant.getInitiative());
                combatantObj.put("status", Combatant.getStringFromCombatantState(currCombatant.getStatus()));

                if(currCombatant instanceof NPC) {
                    npc = (NPC) currCombatant;
                    isNPC = true;
                    combatantObj.put("health", npc.getHealth());
                    combatantObj.put("maxHealth", npc.getMaxHealth());
                    combatantObj.put("armourClass", npc.getArmourClass());
                    deathSavesArr = npc.getDeathSaves();

                    for(int j = 0; j < 6; j++){
                        deathSavesJSONArr.put(j, deathSavesArr[j].toString());
                    }
                }
                else{
                    isNPC = false;
                }
                combatantObj.put("isNPC", isNPC);
                JSONCombatantsArr.put(combatantObj);
                JSONCombatantsArr.put(deathSavesJSONArr);
            }

            fw.write(JSONCombatantsArr.toString());
            fw.flush();
            fw.close();
        }
        catch (Exception e){
            Log.e("COMBAT_SAVING", "Error saving the combat to JSON: " + e.getLocalizedMessage());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static List<Combatant> loadCombatFromJSON(Context context, String filename){
        FileReader fr;
        File file;

        try{
            file = new File(context.getFilesDir(), filename);
            fr = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fr);

            int initiative, initiativeModifier, health, maxHealth,armourClass;
            Combatant.combatantStates status;
            NPC.deathSaveResult[] deathSaves = new NPC.deathSaveResult[6];
            String name;
            List<Combatant> combatantsList = new ArrayList<>();
            Player tempPC;
            NPC tempNPC;
            boolean isNPC;

            String JSONData = bufferedReader.readLine();
            JSONArray data = new JSONArray(), saves;

            try {

                data = new JSONArray(JSONData);
                Log.d("TESTING","" + data.toString());
            }
            catch (ClassCastException e){
                Log.e("COMBAT_LOADING", "Error loading the combatant data. " + e.getLocalizedMessage());
                e.printStackTrace();
            }

            JSONObject tempJsonObj;
            for(int i = 1; i < data.length(); i = i + 2){
                tempJsonObj = (JSONObject) data.get(i);
                isNPC = tempJsonObj.getBoolean("isNPC");
                name = tempJsonObj.getString("name");
                status = Combatant.getCombatantStateFromString(tempJsonObj.getString("status"));
                initiative = tempJsonObj.getInt("initiative");
                initiativeModifier = tempJsonObj.getInt("initiativeModifier");

                if(isNPC){
                    health = tempJsonObj.getInt("health");
                    maxHealth = tempJsonObj.getInt("maxHealth");
                    armourClass = tempJsonObj.getInt("armourClass");
                    saves = data.getJSONArray(i+1);
                    for(int j = 0; j < saves.length(); j++){
                        deathSaves[j] = NPC.convertStringToDeathSaveResult(saves.getString(j));
                    }
                    tempNPC = new NPC(name, initiative, initiativeModifier, status, armourClass, health, maxHealth, deathSaves) ;
                    combatantsList.add(tempNPC);
                }
                else{
                    tempPC = new Player(initiative, initiativeModifier, status, name);
                    combatantsList.add(tempPC);
                }
            }

            return combatantsList;

        }
        catch (Exception e){
            Log.e("JSON_CONVERTER", "Error converting JSON to Combatants list: " + e.getLocalizedMessage());
            e.printStackTrace();
            return null;
        }
    }

    public static int loadCombatPositionFromJSON(Context context, String filename) {
        FileReader fr;
        File file;
        int position = 0;

        try {
            file = new File(context.getFilesDir(), filename);
            fr = new FileReader(file);
            JsonParser jsonParser = new JsonParser();

            Object tempObj = jsonParser.parse(fr);
            JSONArray data = null;

            data = (JSONArray) tempObj;

            position = data.getJSONObject(0).getInt("position");

        } catch (Exception e) {
            Log.e("COMBAT_LOAD", "Error loading the combat, could not retrieve position data. " + e.getLocalizedMessage());
            e.printStackTrace();
        }

        return position;
    }
}
