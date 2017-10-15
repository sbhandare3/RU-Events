package com.shreyasbhandare.ruevents.Presenter;

public class Event {
    private String name;
    private String organization;
    private String photo;
    private String date;
    private String id;

    public Event(String id, String name, String organization, String date, String photo){
        setEventName(name);
        setOrganization(organization);
        setPhoto(photo);
        setDate(date);
        setEventId(id);
    }

    public String getEventName() {
        return name;
    }

    private void setEventName(String name) {
        this.name = name;
    }

    public String getOrganization() {
        return organization;
    }

    private void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getEventId() {
        return id;
    }

    private void setEventId(String id) {
        this.id = id;
    }
}
