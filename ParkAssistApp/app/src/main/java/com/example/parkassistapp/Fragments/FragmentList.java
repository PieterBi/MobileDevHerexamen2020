package com.example.parkassistapp.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.example.parkassistapp.CustomListAdapter;
import com.example.parkassistapp.DAL.ParkingGarageDbHelper;
import com.example.parkassistapp.Model.ParkingGarage;
import com.example.parkassistapp.MyListener;
import com.example.parkassistapp.R;

import java.util.List;

public class FragmentList extends Fragment {
    private Context context;

    private ParkingGarage[] garages;
    private EditText inputSearch;
    private ListView listView;
    private CustomListAdapter adapter;

    private Handler searchHandler = new Handler();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favourite, container, false);

        context = getActivity().getApplicationContext();

        inputSearch = view.findViewById(R.id.et_inputSearch);
        inputSearch.addTextChangedListener(searchWatcher);
        listView = view.findViewById(R.id.lv_listOfGarages);

        getSavedParkingSpot();

        return view;
    }

    public void getSavedParkingSpot(){
        ParkingGarageDbHelper dbHelper = new ParkingGarageDbHelper(getActivity().getApplicationContext());

        List<ParkingGarage> list = dbHelper.getAllParkingGarages();

        garages = new ParkingGarage[list.size()];
        list.toArray(garages);

        adapter = new CustomListAdapter(getActivity(), garages);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                String item = (String) listView.getAdapter().getItem(position).toString();
//                Toast.makeText(context, item + " selected", Toast.LENGTH_LONG).show();
                sendPosition(position);
            }
        });
    }

    private void sendPosition(int position) {
        MyListener myListener = (MyListener) getActivity();
        myListener.sendParkingGarage(position);
    }

    private void searchParkingGarages(String s){ adapter.getFilter().filter(s); }

    TextWatcher searchWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            searchHandler.postDelayed(new Runnable() {
                public void run() {
                    searchParkingGarages(inputSearch.getText().toString());
                }
            }, 1200);
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
        }
    };
}
