package com.udacity.sandwichclub.utils;

import android.util.Log;

import com.udacity.sandwichclub.MainActivity;
import com.udacity.sandwichclub.model.Sandwich;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JsonUtils {

    /**
     * This method parses JSON response and returns a Sandwich object,
     * response is parsed and details about selected sandwich is populated into sandwich object.
     * <p/>
     *
     * @param json JSON response
     *
     * @return Sandwich object describing sandwich details
     *
     * @throws JSONException If JSON data cannot be properly parsed
     */
    public static Sandwich parseSandwichJson(String json) {

        String TAG = Sandwich.class.getSimpleName();

        /*ArrayList to hold list of other names*/
        ArrayList<String> alsoKnownAs = new ArrayList<String>();
        /*ArrayList to hold list of ingredients*/
        ArrayList<String> ingredients = new ArrayList<String>();

        if (json != null) {
            try {
                JSONObject jsonResponse = new JSONObject(json);

                /*Extract Names*/
                JSONObject sandwichName = jsonResponse.getJSONObject("name");
                String sandwichMainName = sandwichName.getString("mainName");

                /*Extract OtherNames*/
                JSONArray sandwichAlsoKnownAs = sandwichName.getJSONArray("alsoKnownAs");
                if (sandwichAlsoKnownAs.length() > 0) {
                    for (int i = 0; i < sandwichAlsoKnownAs.length(); i++) {
                        alsoKnownAs.add(sandwichAlsoKnownAs.getString(i));
                    }
                }else {
                    alsoKnownAs.add("Not Available");
                }

                /*Extract placeOforigin*/
                String sandwichPlaceOfOrigin = jsonResponse.getString("placeOfOrigin");
                if (sandwichPlaceOfOrigin.isEmpty())
                    sandwichPlaceOfOrigin = "Not Available";

                /*Extract Description*/
                String sandwichDescription = jsonResponse.getString("description");
                if (sandwichDescription.isEmpty())
                    sandwichDescription = "Not Available";
                String sandwichImage = jsonResponse.getString("image");

                /*Extract Ingredients*/
                JSONArray sandwichIngredients = jsonResponse.getJSONArray("ingredients");
                if (sandwichIngredients.length() > 0) {
                    for (int i = 0; i < sandwichIngredients.length(); i++) {
                        ingredients.add(sandwichIngredients.getString(i));
                    }
                }else {
                    ingredients.add("Not Available");
                }

                /*Populate the details into sandwich Object*/
                Sandwich selectedSandwich = new Sandwich(sandwichMainName, alsoKnownAs, sandwichPlaceOfOrigin, sandwichDescription, sandwichImage, ingredients);

                return selectedSandwich;

            } catch (JSONException e) {
                Log.e(TAG,"JSON Parsing Error : " + e.getMessage());
                e.printStackTrace();
            }
        }else{
            Log.e(TAG,"JSON response is Null.");
        }

        return null;
    }
}
