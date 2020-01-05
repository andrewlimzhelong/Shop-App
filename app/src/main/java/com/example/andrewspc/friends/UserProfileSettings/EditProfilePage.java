package com.example.andrewspc.friends.UserProfileSettings;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.andrewspc.friends.R;
import com.example.andrewspc.friends.SetUpProfile.UploadimageModelClass;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfilePage extends AppCompatActivity {

    private CircleImageView imageView;
    private EditText mSettingsName;
    private EditText occupation;
    private EditText settingsContactHP;
    private TextView mSavebtn;

    //Firebase
    private DatabaseReference mStatusDatabase;
    private FirebaseUser mCurrentUser;
    private static final int GALLERY_PICK = 1;
    private StorageReference storageReference;
    private Uri filePath;
    //Image Uri Storage variable
    private String imageLink;
    private ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_page);

        // User Profile Set Up
        imageView = (CircleImageView) findViewById(R.id.profileImage2);
        //Name Edit Text Field
        mSettingsName = (EditText) findViewById(R.id.settingsName);
        occupation = findViewById(R.id.occupation);
        settingsContactHP = findViewById(R.id.handphoneNum);
        mSavebtn = (TextView) findViewById(R.id.SaveSettingsBtn);

        ////////////// Firebase Image uploading database reference
        storageReference = FirebaseStorage.getInstance().getReference("uploads");
        //Firebase instances
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        String current_uid = mCurrentUser.getUid();

        backButton = findViewById(R.id.backArrow);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(EditProfilePage.this, ProfilePage.class);
                startActivity(i);
            }
        });

        // Launch Image Gallery on clicking image
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFileChooser();
            }
        });

        //Referencing to specific value in the firebase database
        mStatusDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child("Profile").child(current_uid);

        retrivingDataFromFirebaseET("Username", mSettingsName, mStatusDatabase);
        retrivingDataFromFirebaseET("Occupation", occupation, mStatusDatabase);
        retrivingDataFromFirebaseET("ContactNumber", settingsContactHP, mStatusDatabase);
        retrievingPicture("ProfilePicture",imageView, mStatusDatabase);

        // Changing data in Firebase (Editting the database real time)
        //Tutorial 11 Entire tutorial
        // YOUTUBE : https://www.youtube.com/watch?v=60nMpGRjcns&list=PLGCjwl1RrtcQ3o2jmZtwu2wXEA4OIIq53&index=11
        mSavebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                saveData(mSettingsName, mStatusDatabase, "Username");
                saveData(occupation, mStatusDatabase, "Occupation");
                saveData(settingsContactHP, mStatusDatabase, "ContactNumber");

                uploadFile();

                Intent settingsIntent = new Intent(EditProfilePage.this, ProfilePage.class);
                startActivity(settingsIntent);
            }
        });
    }

    public void saveData(final EditText dataField, final DatabaseReference databaseReference, final String name){
        String whatUserEnters = dataField.getEditableText().toString();
        databaseReference.child(name).setValue(whatUserEnters);
    }

    public void retrievingPicture(final String databaseLeftHeader, final ImageView imageView, final DatabaseReference imageDatabaseRef){

        imageDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.hasChild(databaseLeftHeader)) {

                    imageDatabaseRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                String Image = dataSnapshot.child(databaseLeftHeader).getValue().toString();
                                Picasso.get().load(Image).fit().into(imageView);
                            }

                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void retrivingDataFromFirebaseET(final String leftTitle, final EditText anotherEditText, final DatabaseReference databseReference) {

        databseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.hasChild(leftTitle)) {

                    databseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String Name = dataSnapshot.child(leftTitle).getValue().toString();
                            anotherEditText.setText(Name);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void retrivingDataFromFirebaseTV(final String leftTitle, final TextView anotherTextView, final DatabaseReference databseReference) {

        databseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.hasChild(leftTitle)) {

                    databseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String Name = dataSnapshot.child(leftTitle).getValue().toString();
                            anotherTextView.setText(Name);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    ///////////////////////////////////// UPLOAD IMAGE ////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////// Uploading of Image to Firebase ///////////////////////////////////////////////////////////
    // Link to youtube tutorial for image uploading
    // https://www.youtube.com/watch?v=ZmgncLHk_s4&t=831s

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select an Image"), GALLERY_PICK);
    }

    private void uploadFile(){

        if (filePath != null)
        {
            final StorageReference fileReference = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(filePath));
            fileReference.putFile(filePath).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>()
            {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception
                {
                    if (!task.isSuccessful())
                    {
                        throw task.getException();
                    }

                    /// Suspect this line is not working
                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>()
            {
                @Override
                public void onComplete(@NonNull Task<Uri> task)
                {
                    if (task.isSuccessful())
                    {

                        Uri downloadUri = task.getResult();
                        UploadimageModelClass upload = new UploadimageModelClass(downloadUri.toString());
                        imageLink = upload.getmImageUrl();
                        mStatusDatabase.child("ProfilePicture").setValue(imageLink);

                    } else
                    {
                        Toast.makeText(EditProfilePage.this, "upload failed here2: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    ////////////////////////////////////////////////// The below method is just to get the file extension for the image //////////////////////////////////////////////////
    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
    ////////////////////////////////////////////////// The above method is just to get the file extension for the image //////////////////////////////////////////////////

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_PICK && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            Uri imageUri = data.getData();

            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .setMinCropResultSize(200,200)
                    .start(this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();

                imageView.setImageURI(resultUri);
                filePath = resultUri;

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
    /////////////////////////////////////////////////////////////////////// UPLOAD IMAGE ////////////////////////////////////////////////////////////////////////////////////
}
