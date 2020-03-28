package com.example.sarthakj.home_automation_mark1;

/**
 * Created by SarthakJ on 8/12/2017.
 */

public class Entity {
    String name;
    boolean status;

    public Entity(String name, boolean status) {
        this.name = name;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
