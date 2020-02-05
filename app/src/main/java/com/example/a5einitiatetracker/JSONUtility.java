package com.example.a5einitiatetracker;

import android.content.Context;
import android.util.JsonReader;
import android.util.JsonWriter;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.List;

class JSONUtility {

    //Creates a new file with the specified context and filename if one does not already exist
    static void createFile(Context context, String filename){
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
    static void storeMonstersToJSON(List<MonsterName> list, File file){
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
                }
                jw.endObject();
            }
            jw.endArray();

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
            Log.e("JSON_CONVERTER", "Error converting MonsterName list to JSON Object: "
                    + e.getLocalizedMessage());
        }
        //endregion

        return arr;
    }

    static List<MonsterName> readMonsternamesFromJSONFile(Context context, String filename){
        File file;
        FileReader fr;
        JsonReader jr;

        try{
            file = new File(context.getFilesDir(), filename);
            fr = new FileReader(file);
            jr = new JsonReader(fr);

            while(jr.hasNext()){
                
            }
        }
        catch (Exception e){
            Log.e("JSON_CONVERTER", "Error converting JSON to MOnsterName list: " + e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

    private static List<MonsterName> convertJSONtoMonsterNames(JSONArray arr){

    }
}
