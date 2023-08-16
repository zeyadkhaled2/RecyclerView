package com.example.voting.database;

public class VoteItem {
    private int id;
    private boolean checked = false;
    private String body = "";
    private int votes;

    public VoteItem(int id, String body, int votes) {
        this.id = id;
        this.body = body;
        this.votes = votes;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getVotes() {
        return votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }
}
