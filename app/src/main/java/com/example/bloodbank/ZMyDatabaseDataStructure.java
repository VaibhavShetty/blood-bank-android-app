package com.example.bloodbank;

import android.os.Parcel;
import android.os.Parcelable;

public class ZMyDatabaseDataStructure implements Parcelable {
    String email;
    String address;
    String name;
    String bgroup;
    String imgloc;
    String phone;

    String  distance;
    String gender;

    public String getPhone() {
        return phone;
    }


    public String getDistance() {
        return distance;
    }



    public void setPhone(String phone) {
        this.phone = phone;
    }


    public void setDistance(String distance) {
        this.distance = distance;
    }

    protected ZMyDatabaseDataStructure(Parcel in) {
        name = in.readString();
        bgroup = in.readString();
        imgloc = in.readString();
        gender = in.readString();

    }
    public ZMyDatabaseDataStructure(){}



    public ZMyDatabaseDataStructure(String name, String bgroup, String imgloc) {
        this.name = name;
        this.bgroup = bgroup;
        this.imgloc=imgloc;
    }

    public ZMyDatabaseDataStructure(String email, String address, String name, String bgroup, String imgloc, String gender,String phone) {
        this.email = email;
        this.address = address;
        this.name = name;
        this.bgroup = bgroup;
        this.imgloc = imgloc;
        this.phone=phone;
        this.gender = gender;
    }


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBgroup() {
        return bgroup;
    }

    public void setBgroup(String bgroup) {
        this.bgroup = bgroup;
    }

    public String getImgloc() {
        return imgloc;
    }

    public void setImgloc(String imgloc) {
        this.imgloc = imgloc;
    }

    public String getGender() {
        return this.gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoto() {
        return this.imgloc;
    }
    public static final Creator<ZMyDatabaseDataStructure> CREATOR = new Creator<ZMyDatabaseDataStructure>() {
        @Override
        public ZMyDatabaseDataStructure createFromParcel(Parcel in) {
            return new ZMyDatabaseDataStructure(in);
        }

        @Override
        public ZMyDatabaseDataStructure[] newArray(int size) {
            return new ZMyDatabaseDataStructure[size];
        }
    };



        @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(name);
        parcel.writeString(bgroup);
        parcel.writeString(imgloc);
        parcel.writeString(gender);
    }
}
