package com.example.parkassistapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class ParkingGarage implements Parcelable {

    private int id;
    private String company;
    private String name;
    private String coordinateX;
    private String coordinateY;


    public ParkingGarage(String company, String name, String coordinateX, String coordinateY)
    {
        this.company = company;
        this.name = name;
        this.coordinateX = coordinateX;
        this.coordinateY = coordinateY;
    }

    public ParkingGarage(int id, String company, String name, String coordinateX, String coordinateY)
    {
        this(company, name, coordinateX, coordinateY);
        this.id = id;
    }

    public ParkingGarage(int id, ParkingGarage p)
    {
        this(id, p.getCompany(), p.getName(), p.getCoordinateX(), p.getCoordinateY());
    }

    public int getId() {
        return id;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCoordinateX() {
        return coordinateX;
    }

    public void setCoordinateX(String coordinateX) {
        this.coordinateX = coordinateX;
    }

    public String getCoordinateY() {
        return coordinateY;
    }

    public void setCoordinateY(String coordinateY) {
        this.coordinateY = coordinateY;
    }

    protected ParkingGarage(Parcel in) {
        id = in.readInt();
        company = in.readString();
        name = in.readString();
        coordinateX = in.readString();
        coordinateY = in.readString();
    }

    @Override
    public String toString() {
        return this.name.toString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(company);
        dest.writeString(name);
        dest.writeString(coordinateX);
        dest.writeString(coordinateY);
    }

    public static final Parcelable.Creator<ParkingGarage> CREATOR = new Parcelable.Creator<ParkingGarage>() {
        @Override
        public ParkingGarage createFromParcel(Parcel in) {
            return new ParkingGarage(in);
        }

        @Override
        public ParkingGarage[] newArray(int size) {
            return new ParkingGarage[size];
        }
    };

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!ParkingGarage.class.isAssignableFrom(obj.getClass())) {
            return false;
        }

        final ParkingGarage other = (ParkingGarage) obj;
        if (this.getName() == other.getName() || this.getName() == other.getName()) {
            return true;
        }

        return false;
    }
}
