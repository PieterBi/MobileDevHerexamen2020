package com.example.parkassistapp.Activities;

import android.app.FragmentManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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
    private Button searchButton;
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
