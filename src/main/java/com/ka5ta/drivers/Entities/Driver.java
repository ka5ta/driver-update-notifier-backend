package com.ka5ta.drivers.Entities;

import javax.persistence.*;

@Entity
@Table(name="students")
public class Driver {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;
    private String version;
    private String link;

    public Driver(long id, String name, String version, String link) {
        this.id = id;
        this.name = name;
        this.version = version;
        this.link = link;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public String getLink() {
        return link;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setLink(String link) {
        this.link = link;
    }
}



