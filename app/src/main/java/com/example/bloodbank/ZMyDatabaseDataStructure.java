package com.example.bloodbank;

import android.os.Parcel;
import android.os.Parcelable;

public class ZMyDatabaseDataStructure implements Parcelable {
    String email,address,name,bgroup,imgloc ,gender,phone;
    String imgurl;

    protected ZMyDatabaseDataStructure(Parcel in) {
        name = in.readString();
        bgroup = in.readString();
        imgloc = in.readString();

    }
    public ZMyDatabaseDataStructure(){}



    public ZMyDatabaseDataStructure(String name, String bgroup, String imgurl) {
        this.name = name;
        this.bgroup = bgroup;
        this.imgurl=imgurl;
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
        return gender;
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
    }
}
