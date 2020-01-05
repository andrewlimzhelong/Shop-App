package com.example.andrewspc.friends.Chats;

public class ChatObject {

    private String Title;
    private String Occupation;
    private String Username;
    private String ProfilePicture;
    private String UserID;
    private String BioDesc;
    private String UniqueChatID;

    public ChatObject() {
    }

    public String getBioDesc() {
        return BioDesc;
    }

    public void setBioDesc(String bioDesc) {
        BioDesc = bioDesc;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getOccupation() {
        return Occupation;
    }

    public void setOccupation(String occupation) {
        Occupation = occupation;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getProfilePicture() {
        return ProfilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        ProfilePicture = profilePicture;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getUniqueChatID() {
        return UniqueChatID;
    }

    public void setUniqueChatID(String uniqueChatID) {
        UniqueChatID = uniqueChatID;
    }
}
