package com.example.workoutlogger.data;

public class Exercise {
    private String id;
    private String name;

    public Exercise() {
    }

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
}
