package com.example.parkassistapp.Activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import com.example.parkassistapp.Fragments.FragmentDetail;
import com.example.parkassistapp.Fragments.FragmentList;
import com.example.parkassistapp.Model.ParkingGarage;
import com.example.parkassistapp.MyListener;
import com.example.parkassistapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends BaseActivity implements MyListener {

    private ListView listView;
    private FragmentManager manager;
    private ParkingGarage selectedGarage;
    private Boolean wideMode;
    private Button searchButton, googleButton;
    private String mJSONURLString;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        listView = (ListView) findViewById(R.id.lv_listOfGarages);

        manager = getFragmentManager();
        checkLayoutMode(getResources().getConfiguration());

        if ((listView.getCount() > 0) && wideMode)
            sendGarage(0);

        searchButton = (Button) findViewById(R.id.btn_search);
        googleButton = (Button) findViewById(R.id.btn_goToMaps);
        mJSONURLString = "https://opendata.brussel.be/api/records/1.0/search/?dataset=public-parkings&q=";

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText searchEditText = (EditText) findViewById(R.id.et_inputSearch);
                String searchText = searchEditText.getText().toString();
                searchText = searchText.replaceAll(" ","_");
                mJSONURLString += searchText;

                // Initialize a new RequestQueue instance
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

                // Initialize a new JsonArrayRequest instance
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                        Request.Method.GET,
                        mJSONURLString,
                        null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                // Do something with response
                                //mTextView.setText(response.toString());

                                // Process the JSON
                                List<ParkingGarage> garageList = new ArrayList<ParkingGarage>();
                                try {
                                    // Loop through the array elements
                                    JSONArray responseArray = response.getJSONArray("Data");
                                    JSONArray parkingGaragesArray = responseArray.getJSONArray(1);
                                    for(int i=0;i<parkingGaragesArray.length();i++)
                                    {
                                        JSONObject garage = responseArray.getJSONObject(i);

                                        JSONObject fields = garage.getJSONObject("fields");

                                        String company = fields.getString("proprietaire_beheersmaatschappij");
                                        String name = fields.getString("nom_naam");

                                        JSONArray coordinatesArray = fields.getJSONArray("coordonnes_coordinaten");
                                        String coordinatesX = coordinatesArray.getString(0);
                                        String coordinatesY = coordinatesArray.getString(1);

                                        ParkingGarage parkingGarage = new ParkingGarage(company, name, coordinatesX, coordinatesY);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // Do something when error occurred
                                Toast toast = Toast.makeText(getApplicationContext(), "An error occurred", Toast.LENGTH_LONG);
                                toast.show();
                            }
                        });

            }
        });
        googleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView searchEditText = (TextView) findViewById(R.id.tv_coordinatesx);
                String coordinatesX = searchEditText.getText().toString();
                searchEditText = (TextView) findViewById(R.id.tv_coordinatesy);
                String coordinatesY = searchEditText.getText().toString();

                Uri gmmIntentUri = Uri.parse("google.navigation:q=" + coordinatesX + ", " + coordinatesY);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);

            }});
    }

    @Override
    public void onConfigurationChanged(Configuration _newConfig) {
        super.onConfigurationChanged(_newConfig);

        checkLayoutMode(getResources().getConfiguration());
    }

    @Override
    protected void onResume(){
        super.onResume();
        update();
    }

    @Override
    public void sendGarage(int position)
    {
        selectedGarage = (ParkingGarage) listView.getItemAtPosition(position);
        sendDataToFragmentDetail();
    }

    private void checkLayoutMode(Configuration config) {
        int orientation = config.orientation;

        if (orientation == config.ORIENTATION_PORTRAIT) {
            wideMode = false;
        } else if (orientation == config.ORIENTATION_LANDSCAPE) {
            wideMode = true;
        }
    }

    private void sendDataToFragmentDetail() {
        if (wideMode) {
            //if phone is in landscape, use detail fragment to show data
            FragmentDetail fragmentDetail = (FragmentDetail) manager.findFragmentById(R.id.frag_detail);
            fragmentDetail.setGarageDetail(selectedGarage);
        }
        else {
            //if phone is in portrait, create new activity with intent
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra("selectedGarage", selectedGarage);
            startActivity(intent);
        }
    }

    @Override
    public void update() {

        if (wideMode) {
            FragmentList fragmentList = (FragmentList) manager.findFragmentById(R.id.frag_list);
            fragmentList.getSavedParkingSpot();
        }
        else {
            FragmentList fragmentList = (FragmentList) manager.findFragmentById(R.id.frag_container);
            fragmentList.getSavedParkingSpot();
        }
    }
}
