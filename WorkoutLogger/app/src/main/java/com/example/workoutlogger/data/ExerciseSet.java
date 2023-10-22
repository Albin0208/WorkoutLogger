package com.example.workoutlogger.data;

public class ExerciseSet {
    private int id;
    private int reps;
    private int weight;

    public ExerciseSet(int id, int reps, int weight) {
        this.id = id;
        this.reps = reps;
        this.weight = weight;
    }
    public ExerciseSet() {
    }

    public int getId() {
        return id;
    }

    public int getReps() {
        return reps;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public boolean equals(ExerciseSet exerciseSet) {
        return this.id == exerciseSet.getId() && this.reps == exerciseSet.getReps() && this.weight == exerciseSet.getWeight();
    }
}
