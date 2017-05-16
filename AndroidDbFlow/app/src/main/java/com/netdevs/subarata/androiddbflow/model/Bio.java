package com.netdevs.subarata.androiddbflow.model;

import com.netdevs.subarata.androiddbflow.database.BioDb;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.annotation.Unique;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by student on 19/11/2016.
 */

@Table(database = BioDb.class)
public class Bio extends BaseModel {

    @PrimaryKey(autoincrement = true)
    private int id;

    @Column(length = 50)
    private String name;

    @Column(length = 250)
    private String education;

    @Column(length = 150)
    @Unique
    private String email;

    @Column(length = 15)
    @Unique
    private String cell;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCell() {
        return cell;
    }

    public void setCell(String cell) {
        this.cell = cell;
    }
}
