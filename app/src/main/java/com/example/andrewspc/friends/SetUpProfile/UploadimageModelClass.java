package com.example.andrewspc.friends.SetUpProfile;

public class UploadimageModelClass {

    private String mImageUrl;

    public UploadimageModelClass(){

    }

    public UploadimageModelClass(String imageUrl){

        mImageUrl = imageUrl;

    }

    public String getmImageUrl() {
        return mImageUrl;

    }

    public void setmImageUrl(String mImageUrl) {
        this.mImageUrl = mImageUrl;
    }
}
