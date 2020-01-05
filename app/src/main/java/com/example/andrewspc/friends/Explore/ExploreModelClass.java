package com.example.andrewspc.friends.Explore;

public class ExploreModelClass {

    private String Username;
    private String ProfilePicture;
    private String UserID;
    private String ContactNumber;
    private String Occupation;
    private String Title;
    private String Description;
    private String Price;
    private String Decimal;
    private String PostedImage;
    private String ImageUniqueKey;

    public ExploreModelClass() {
    }

    public ExploreModelClass(String Username, String ProfilePicture, String UserID, String ContactNumber, String Occupation,
                             String Title, String Description, String Price, String Decimal, String PostedImage, String ImageUniqueKey) {

        this.Username = Username;
        this.ProfilePicture = ProfilePicture;
        this.ProfilePicture = UserID;
        this.ContactNumber = ContactNumber;
        this.Occupation = Occupation;
        this.Title = Title;
        this.Description = Description;
        this.Price = Price;
        this.Decimal = Decimal;
        this.PostedImage = PostedImage;
        this.ImageUniqueKey = ImageUniqueKey;
    }

    public String getProfilePicture() {
        return ProfilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        ProfilePicture = profilePicture;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getContactNumber() {
        return ContactNumber;
    }

    public void setContactNumber(String contactNumber) {
        ContactNumber = contactNumber;
    }

    public String getOccupation() {
        return Occupation;
    }

    public void setOccupation(String occupation) {
        Occupation = occupation;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getDecimal() {
        return Decimal;
    }

    public void setDecimal(String decimal) {
        Decimal = decimal;
    }

    public String getPostedImage() {
        return PostedImage;
    }

    public void setPostedImage(String postedImage) {
        PostedImage = postedImage;
    }

    public String getImageUniqueKey() {
        return ImageUniqueKey;
    }

    public void setImageUniqueKey(String imageUniqueKey) {
        ImageUniqueKey = imageUniqueKey;
    }
}