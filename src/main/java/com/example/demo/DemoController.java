package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.google.gson.*;

@Controller
public class DemoController {


    public static String apiKey = "44ab2e4a";
    public static String apiUrl = "http://www.omdbapi.com";


    /**
     * get Api data Method
     * @param type
     * @param searchKey
     * @return ArrayList
     */
    public ArrayList<Object> getApidata(String type, String searchKey) {

        // determine Url using Type value.
        String url = "";
        if("Main".equals(type)){
            url = apiUrl+"/?s="+searchKey+"&apikey="+apiKey;
        }else if("Detail".equals(type)){
            url = apiUrl+"/?i="+searchKey+"&apikey="+apiKey;
        }else {
            return new ArrayList<>();
        }
        // Get Movie List
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.exchange(url,
                HttpMethod.GET, null, String.class);

        // get body String
        String jsonStr = responseEntity.getBody();
        
        // if it is not succeed, display empty list.
        if(!"200 OK".equals(responseEntity.getStatusCode().toString())){
            return null;    
        }
        JsonObject convertedObject = new Gson().fromJson(jsonStr, JsonObject.class);

        ArrayList<Object> list = null;

        
        if("Main".equals(type)){
             // if it is Search List type
             list = ArrayUtil.convert(convertedObject.getAsJsonArray("Search"));
        }else if("Detail".equals(type)){
            // if it is Object type
            list = new ArrayList<Object>();
            list.add(ArrayUtil.convertMap(convertedObject));
            list.add(ArrayUtil.convert(convertedObject.getAsJsonArray("Ratings")));
        }

        return list;

    }
    

    /**
     * Call Movie List (Main)
     * @param model
     * @param searchKey
     * @return main.html
     */
    @GetMapping("/")
    public String main(Model model, @RequestParam(value="searchKey", defaultValue = "Home") String searchKey) {

        // Get Movie List
        ArrayList<Object> list = getApidata("Main",searchKey);

        // if it is Zero count of list, it will get defaul list.
        if(list != null && list.size() == 0){
            list = getApidata("Main","Home");
        }

        // set the list data
        model.addAttribute("dataList", list);

        return "main"; // show main.html
    }

    /**
     * Call Movie in detail
     * @param model
     * @param searchKey
     * @return detail.html
     */
    @GetMapping("/detail")
    public String detail( Model model,@RequestParam(value="searchKey", defaultValue = "Home") String searchKey) {

        // Get Movie List
        ArrayList<Object> list = getApidata("Detail",searchKey);

        // set the list data
        model.addAttribute("detail", list.get(0));
        model.addAttribute("ratings", list.get(1));

        return "detail"; // show detail.html
    }

}
