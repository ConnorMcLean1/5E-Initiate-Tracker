package com.example.a5einitiatetracker;

import android.util.JsonWriter;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.util.List;

public class JSONUtility {

    static void storeMonstersToJSON(List<MonsterName> list, String directory, String filename){
        File file = new File(directory, filename);
        FileWriter fw;
        JsonWriter jw;
        JSONArray arr = MonsterNamesToJSONArray(list);

        //If the file does not exist, create a new one
        if(!file.exists()){
            try{
                file.createNewFile();
            }
            catch (Exception e){
                Log.e("FILE_CREATION", "Error: " + e.getLocalizedMessage());
            }
        }
        try{
            fw = new FileWriter(file.getAbsoluteFile());
            jw = new JsonWriter(fw);
            jw.beginArray();
            for(int i = 0; i < arr.length(); i++){
                jw.beginObject();
                jw.value(arr.getJSONObject(i).toString());
                jw.endObject();
            }
            jw.endArray();

        }
        catch (Exception e){
            Log.e("FILE_WRITING", "Error: " + e.getLocalizedMessage());
        }
    }

    private static JSONArray MonsterNamesToJSONArray(List<MonsterName> list){
        //region VARIABLES
        JSONArray arr = new JSONArray();
        JSONObject obj;
        //endregion
        try {
            //region LOOP
            for (int i = 0; i < list.size(); i++) {
                obj = new JSONObject();
                obj.put("Index", list.get(i).getIndex());
                obj.put("Name", list.get(i).getIndex());
                arr.put(obj);
            }
        }
        catch (Exception e){
            Log.e("JSON_CONVERTER", "Error converting MonsterNames list to JSON Object: "
                    + e.getLocalizedMessage());
        }
        //endregion

        //region TESTING
        try {
            JSONObject tempOBJ;
            for (int i = 0; i < arr.length(); i++) {
                tempOBJ = arr.getJSONObject(i);
                Log.d("JSON_TEST", "Name: " + tempOBJ.getString("Name") + ", Index: "
                        + tempOBJ.getString("Index"));
            }
        }
        catch (Exception e){
            Log.e("JSON_TEST", "Error: " + e.getLocalizedMessage());
        }
        //endregion

        return arr;
    }
}
