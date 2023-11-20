package com.example.workoutlogger.data;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.firebase.Timestamp;

public class Record implements Parcelable {
    private String exerciseID;
    private String documentID;
    private ExerciseSet set;
    private Timestamp timestamp;

    public Record() {}

    public Record(String exerciseID, ExerciseSet set) {
        this.exerciseID = exerciseID;
        this.set = set;
        this.timestamp = Timestamp.now();
    }

    public String getExerciseID() {
        return exerciseID;
    }

    public ExerciseSet getSet() {
        return set;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public boolean checkIfNewRecord(Record oldRecord) {
        if (!this.set.isCompleted()) {
            return false;
        }

        if (this.set.getReps() == oldRecord.getSet().getReps()) {
            return this.set.getWeight() > oldRecord.getSet().getWeight();
        }

        if (this.set.getWeight() == oldRecord.getSet().getWeight()) {
            return this.set.getReps() > oldRecord.getSet().getReps();
        }

        return false;
    }

    public void setDocumentID(String documentID) {
        this.documentID = documentID;
    }

    public String getDocumentID() {
        return documentID;
    }

    protected Record(Parcel in) {
        exerciseID = in.readString();
        documentID = in.readString();
        set = in.readParcelable(ExerciseSet.class.getClassLoader(), ExerciseSet.class);
        timestamp = in.readParcelable(Timestamp.class.getClassLoader(), Timestamp.class); // Read the timestamp
    }

    public static final Creator<Record> CREATOR = new Creator<Record>() {
        @Override
        public Record createFromParcel(Parcel in) {
            return new Record(in);
        }

        @Override
        public Record[] newArray(int size) {
            return new Record[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int flags) {
        parcel.writeString(exerciseID);
        parcel.writeString(documentID);
        parcel.writeParcelable(set, flags);
        parcel.writeParcelable(timestamp, flags);
    }
}
