package com.shreyasbhandare.ruevents.Presenter;

public class Organization {

    private String name;

    private String id;

    private String photo;

    public Organization(String name, String id, String photo){
        setOrgId(id);
        setOrgName(name);
        setOrgPhoto(photo);
    }

    public String getOrgName() {
        return name;
    }

    private void setOrgName(String name) {
        this.name = name;
    }

    public String getOrgId() {
        return id;
    }

    private void setOrgId(String id) {
        this.id = id;
    }

    public String getOrgPhoto() {
        return photo;
    }

    private void setOrgPhoto(String id) {
        this.photo = id;
    }
}
