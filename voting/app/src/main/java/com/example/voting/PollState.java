package com.example.voting;

enum EDITABLE_STATE {
    EDITABLE, NOT_EDITABLE
}

public class PollState {

    private String title, desc;
    private int id;
    private final EDITABLE_STATE state;

    public PollState(String title, String desc, int id, EDITABLE_STATE state) {
        this.title = title;
        this.desc = desc;
        this.id = id;
        this.state = state;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public EDITABLE_STATE getState() {
        return state;
    }
}
