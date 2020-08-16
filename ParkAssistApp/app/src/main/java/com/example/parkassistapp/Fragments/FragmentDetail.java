package com.example.parkassistapp.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.parkassistapp.DAL.ParkingGarageDbHelper;
import com.example.parkassistapp.Model.ParkingGarage;
import com.example.parkassistapp.R;

public class FragmentDetail extends Fragment implements View.OnClickListener {

    private ParkingGarage selectedGarage;
    private TextView tvCompany, tvName, tvCoordinatesX, tvCoordinatesY;
    private Button btnSaveGarage;
    private boolean isSavedGarage;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        tvCompany = (TextView) view.findViewById(R.id.tv_company);
        tvName = (TextView) view.findViewById(R.id.tv_name);
        tvCoordinatesX = (TextView) view.findViewById(R.id.tv_coordinatesx);
        tvCoordinatesY = (TextView) view.findViewById(R.id.tv_coordinatesy);
        btnSaveGarage = (Button) view.findViewById((R.id.btn_savegarage));
        btnSaveGarage.setOnClickListener(this);
        return view;
    }

    public void setGarageDetail(ParkingGarage parkingGarage)
    {
        selectedGarage = parkingGarage;

        tvCompany.setText(selectedGarage.getCompany());
        tvName.setText(selectedGarage.getName());
        tvCoordinatesX.setText(String.valueOf(selectedGarage.getCoordinateX()));
        tvCoordinatesY.setText(String.valueOf(selectedGarage.getCoordinateY()));

        setIsAlreadySaved(selectedGarage.getId());
    }

    private void setIsAlreadySaved(int id)
    {
        //if the ID is assigned, it comes from the local db
        if(id != 0)
        {
            btnSaveGarage.setText("I am leaving the garage");
            isSavedGarage = true;
        }
        else
        {
            btnSaveGarage.setText("I am parking here");
            isSavedGarage = false;
        }
    }

    private void switchBooleanIsSavedGarage()
    {
        isSavedGarage = !isSavedGarage;

        if(isSavedGarage)
        {
            btnSaveGarage.setText("I am leaving the garage");
        }
        else
        {
            btnSaveGarage.setText("I am parking here");
        }
    }

    @Override
    public void onClick(View view)
    {
        ParkingGarageDbHelper dbHelper = new ParkingGarageDbHelper(getActivity().getApplicationContext());

        if(isSavedGarage)
        {
            dbHelper.deleteGarage(selectedGarage, getActivity());
        }
        else
        {
            dbHelper.insertGarage(selectedGarage, getActivity());
        }

        switchBooleanIsSavedGarage();
    }
}
