package com.udacity.sandwichclub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;

    Sandwich sandwich = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        final ImageView ingredientsIv = findViewById(R.id.image_iv);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }

        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
        String json = sandwiches[position];
        sandwich = JsonUtils.parseSandwichJson(json);
        if (sandwich == null) {
            // Sandwich data unavailable
            closeOnError();
            return;
        }

        /*Picasso.with(this)
                .load(sandwich.getImage())
                .into(ingredientsIv);*/
        populateUI();

        //Sawarma weblink was not working during testing so handled error case too.
        Picasso.with(this)
                .load(sandwich.getImage())
                .into(ingredientsIv,new Callback() {

                            @Override
                            public void onSuccess() {
                                //Success image already loaded into the view so do nothing
                            }

                            @Override
                            public void onError() {
                                //Error, do further handling of this situation here
                                ingredientsIv.setVisibility(View.GONE);
                                Toast.makeText(DetailActivity.this,"Error Loading Image link : " +sandwich.getImage(),Toast.LENGTH_LONG).show();

                            }
                        }
                );
        setTitle(sandwich.getMainName());
        // Support Up Navigation
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    /**
     * This method populates the UI with sandwich details
     */

    private void populateUI() {

        String tempAlsoKnownAs ="";
        String tempIngredients = "";

        List<String> alsoKnownAs = new ArrayList<String>();
        List<String> ingredients = new ArrayList<String>();

        /* Get Reference to all Views from the detail layout.*/
        TextView mAlsoKnownAs = findViewById(R.id.also_known_tv);
        TextView mPlaceOfOrigin = findViewById(R.id.origin_tv);
        TextView mDescription = findViewById(R.id.description_tv);
        TextView mIngredients = findViewById(R.id.ingredients_tv);

        /* get the values of alsoKnownas List */
        alsoKnownAs = sandwich.getAlsoKnownAs();
        for(int i=0;i<alsoKnownAs.size();i++){
            if (i==0)
                tempAlsoKnownAs = alsoKnownAs.get(i);
            else
                tempAlsoKnownAs = tempAlsoKnownAs + "\n" + alsoKnownAs.get(i);
        }

        /* get the values of ingredients List */
        ingredients = sandwich.getIngredients();
        for(int i=0;i<ingredients.size();i++){
            if (i==0)
                tempIngredients = ingredients.get(i);
            else
                tempIngredients = tempIngredients + "\n" + ingredients.get(i);
        }

        /*Populate the corresponding views with their values*/
        mAlsoKnownAs.setText(tempAlsoKnownAs);
        mIngredients.setText(tempIngredients);
        mPlaceOfOrigin.setText(sandwich.getPlaceOfOrigin().toString());
        mDescription.setText(sandwich.getDescription().toString());

    }
}
