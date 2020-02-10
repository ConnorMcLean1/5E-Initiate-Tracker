package com.example.a5einitiatetracker;

import android.content.Context;
import android.util.JsonReader;
import android.util.JsonWriter;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class JSONUtility {

    static final String JSON_FILE_NAME = "MonsterListJSON";

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

    static HashMap<String, String> readMonsternamesFromJSONFile(Context context, String filename){
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
}
