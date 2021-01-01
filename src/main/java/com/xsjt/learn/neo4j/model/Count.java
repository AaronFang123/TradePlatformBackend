package com.xsjt.learn.neo4j.model;

public class Count {
    private int before;

    @Override
    public String toString() {
        return "Count{" +
                "before=" + before +
                ", after=" + after +
                '}';
    }

    private int after;

    public int getBefore() {
        return before;
    }

    public void setBefore(int before) {
        this.before = before;
    }

    public int getAfter() {
        return after;
    }

    public void setAfter(int after) {
        this.after = after;
    }
}
