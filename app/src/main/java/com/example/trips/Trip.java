package com.example.trips;

public class Trip {
    public String Id, Title, Description, Picture, Location, Date;

    public Trip(String id, String title, String description, String picture, String location, String date){
        this.Id = id;
        this.Title = title;
        this.Description = description;
        this.Picture = picture;
        this.Location = location;
        this.Date = date;
    }
}
