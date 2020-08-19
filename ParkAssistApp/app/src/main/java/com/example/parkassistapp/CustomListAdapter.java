package com.example.parkassistapp;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.parkassistapp.Model.ParkingGarage;

import java.util.ArrayList;
import java.util.Arrays;

public class CustomListAdapter extends ArrayAdapter implements Filterable {

    private final Activity context;
    private final ParkingGarage[] originalGarageArray;
    private ParkingGarage[] filteredGarageArray;

    public CustomListAdapter(Activity context, ParkingGarage[] garageArray)
    {
        super(context, R.layout.rowlayout, garageArray);

        this.context = context;
        this.originalGarageArray = garageArray;
        this.filteredGarageArray = garageArray;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent)
    {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.rowlayout, null, true);

        TextView txtName = (TextView) rowView.findViewById(R.id.tv_name);

        txtName.setText(filteredGarageArray[position].toString());

        return rowView;
    }

    public int getCount() { return filteredGarageArray.length; }

    @Override
    public Filter getFilter()
    {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence)
            {
                FilterResults results = new FilterResults();
                ArrayList<ParkingGarage> filterResultsData = new ArrayList<ParkingGarage>();

                if (charSequence == null || charSequence.length() == 0)
                {
                    results.values = new ArrayList<>(Arrays.asList(originalGarageArray));
                    results.count = originalGarageArray.length;
                }
                else
                {
                    for (ParkingGarage parkingGarage : originalGarageArray)
                    {
                        if(parkingGarage.getName().toLowerCase().contains(charSequence.toString().toLowerCase()))
                            filterResultsData.add(parkingGarage);
                    }
                    results.values = filterResultsData;
                    results.count = filterResultsData.size();
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults)
            {
                ArrayList<ParkingGarage> resultList = (ArrayList<ParkingGarage>) filterResults.values;
                filteredGarageArray = resultList.toArray(new ParkingGarage[resultList.size()]);
                notifyDataSetChanged();
            }
        };
    }
}
