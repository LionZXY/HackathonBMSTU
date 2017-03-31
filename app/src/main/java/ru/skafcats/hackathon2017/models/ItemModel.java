package ru.skafcats.hackathon2017.models;

import java.util.ArrayList;

/**
 * Created by vasidmi on 31/03/2017.
 */

public class ItemModel {

    private String Title;
    private String Description;
    private String Icon;

    public ItemModel(String Title, String Description, String Icon) {
        this.Title = Title;
        this.Description = Description;
        this.Icon = Icon;
    }

    public String getTitle() {
        return this.Title;
    }

    public String getDescription() {
        return this.Description;
    }

    public String getIcon() {
        return this.Icon;
    }
}

