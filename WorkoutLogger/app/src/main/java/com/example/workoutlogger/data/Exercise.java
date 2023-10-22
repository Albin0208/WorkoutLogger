package com.example.workoutlogger.data;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Exercise implements Parcelable {
    private String id;
    private String name;

    public Exercise() {
    }

    protected Exercise(Parcel in) {
        id = in.readString();
        name = in.readString();
    }

    public static final Creator<Exercise> CREATOR = new Creator<Exercise>() {
        @Override
        public Exercise createFromParcel(Parcel in) {
            return new Exercise(in);
        }

        @Override
        public Exercise[] newArray(int size) {
            return new Exercise[size];
        }
    };

    public Exercise(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean equals(Exercise exercise) {
        return this.name.equals(exercise.getName());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
    }
}
