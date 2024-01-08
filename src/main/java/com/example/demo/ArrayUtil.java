package com.example.demo;
import java.util.ArrayList;
import java.util.HashMap;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;


public class ArrayUtil
{

    /**
     * JsonArray to ArrayList
     * @param JsonArray
     * @return ArrayList
     */
    public static ArrayList<Object> convert(JsonArray jArr)
    {
        if(jArr == null){
            return new ArrayList<>();
        }
        ArrayList<Object> list = new ArrayList<Object>();
        try {
            for (int i=0, l=jArr.size(); i<l; i++){
                    list.add(convertMap((JsonObject)jArr.get(i)));
            }
        } catch (Exception e) {System.out.println("1111");}

        return list;
    }

    /**
     * JsonObject to Map
     * @param JsonObject
     * @return Map
     */
    public static HashMap<String,Object> convertMap(JsonObject jOb){
        HashMap<String,Object> tmp = new  HashMap<String,Object>();
        for(String key : jOb.keySet()){
            JsonElement je = jOb.get(key);
            try {
                System.out.println(je.getAsString());
                tmp.put(key, je.getAsString());
            } catch (Exception e) {
                // it is not a String type.
            }
        }
        return tmp;
    }
}
