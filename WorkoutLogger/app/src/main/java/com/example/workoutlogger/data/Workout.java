package com.example.workoutlogger.data;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.firebase.Timestamp;

import java.util.Date;
import java.util.List;

public class Workout implements Parcelable {
    private int id;
    private String name;
    private List<Exercise> exercises;
    private Timestamp date = Timestamp.now();

    public Workout(int id, String name, List<Exercise> exercises) {
        this.id = id;
        this.name = name;
        this.exercises = exercises;
    }

    protected Workout(Parcel in) {
        id = in.readInt();
        name = in.readString();
        exercises = in.createTypedArrayList(Exercise.CREATOR);
        date = new Timestamp(in.readSerializable(Date.class.getClassLoader(), Date.class));
    }

    public static final Creator<Workout> CREATOR = new Creator<Workout>() {
        @Override
        public Workout createFromParcel(Parcel in) {
            return new Workout(in);
        }

        @Override
        public Workout[] newArray(int size) {
            return new Workout[size];
        }
    };

    public Workout() {
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Exercise> getExercises() {
        return exercises;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setExercises(List<Exercise> exercises) {
        this.exercises = exercises;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeTypedList(exercises);
        parcel.writeSerializable(date.toDate());
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }
}
