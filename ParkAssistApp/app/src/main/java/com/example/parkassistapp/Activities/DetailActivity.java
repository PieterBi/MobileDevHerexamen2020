package com.example.parkassistapp.Activities;

import android.os.Bundle;

import android.app.FragmentManager;

import com.example.parkassistapp.Model.ParkingGarage;
import com.example.parkassistapp.R;
import com.example.parkassistapp.Fragments.FragmentDetail

public class DetailActivity extends BaseActivity {

    private FragmentManager manager;
    private ParkingGarage selectedGarage;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        manager = getFragmentManager();
        selectedGarage = getIntent().getExtras().getParcelable("selectedGarage");
        showGarageDetails();
    }

    private void showGarageDetails()
    {
        FragmentDetail fragmentDetail = (FragmentDetail) manager.findFragmentById(R.id.frag_container);
    }

    @Override
    public void update()
    {
        //do nothing, SearchActivity will update Favourite onResume()
    }
}
