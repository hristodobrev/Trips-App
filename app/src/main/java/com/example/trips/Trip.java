package com.example.trips;

public class Trip {
    public String Title, Description, Picture, Location, Date;
    public int Id;

    public Trip(int id, String title, String description, String picture, String location, String date){
        this.Id = id;
        this.Title = title;
        this.Description = description;
        this.Picture = picture;
        this.Location = location;
        this.Date = date;
    }
}
