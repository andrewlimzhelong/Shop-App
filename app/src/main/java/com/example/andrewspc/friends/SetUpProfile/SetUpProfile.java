package com.example.andrewspc.friends.SetUpProfile;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.andrewspc.friends.Explore.ExploreHomePage;
import com.example.andrewspc.friends.R;
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
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class SetUpProfile extends AppCompatActivity {

    /////////// Image View For editing the image
    private ImageView setUpImg;
    private Uri imageUriFilePath;
    Uri filePath;
    private String imageLink;

    private static final int GALLERY_PICK = 1;

    private EditText regName;
    private EditText occupation;
    private EditText contactNumber;
    private Button createBtn;

    // For Back Button
    private long backTimePressed;

    //Firebase
    private DatabaseReference mStatusDatabase;
    private FirebaseUser mCurrentUser;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up_profile);

        setUpImg = findViewById(R.id.userSelectImage);
        regName = findViewById(R.id.regName);
        occupation = findViewById(R.id.occupation);
        contactNumber = findViewById(R.id.contactNumber);
        createBtn = findViewById(R.id.createBtn);

        //Firebase instances
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        String current_uid = mCurrentUser.getUid();
        storageReference = FirebaseStorage.getInstance().getReference("uploads");

        //Referencing to specific value in the firebase database
        mStatusDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child("Profile").child(current_uid);

        // For Checking if a particular data is present in firebase database in this case we are checking if the child "name" exists in the database.
        mStatusDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.hasChild("Username") && snapshot.hasChild("Occupation") && snapshot.hasChild("ContactNumber")) {
                    Intent intent = new Intent(SetUpProfile.this, ExploreHomePage.class);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!regName.getEditableText().toString().isEmpty() && !occupation.getEditableText().toString().isEmpty() && !contactNumber.getEditableText().toString().isEmpty()) {
                    uploadData(regName, mStatusDatabase, "Username");
                    uploadData(occupation, mStatusDatabase, "Occupation");
                    uploadData(contactNumber, mStatusDatabase, "ContactNumber");

                    uploadFile();

                    mStatusDatabase.child("UserID").setValue(FirebaseAuth.getInstance().getUid());

                    Intent mainIntent = new Intent(SetUpProfile.this, ExploreHomePage.class);
                    startActivity(mainIntent);
                    finish();
                }
            }
        });

        setUpImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showFileChooser();
            }
        });

    }

    public void uploadData(final EditText dataField, final DatabaseReference databaseReference, final String name) {
        String whatUserEnters = dataField.getEditableText().toString();
        databaseReference.child(name).setValue(whatUserEnters);
    }

    public void retrieveData(final String leftTitle, final EditText anotherEditText, final DatabaseReference databaseReference) {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.hasChild(leftTitle)) {
                    String Name = snapshot.child(leftTitle).getValue().toString();
                    anotherEditText.setText(Name);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select an Image"), GALLERY_PICK);
    }


    private void uploadFile() {

        if (filePath != null) {
            final StorageReference fileReference = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(filePath));
            fileReference.putFile(filePath).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    /// Suspect this line is not working
                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        UploadimageModelClass upload = new UploadimageModelClass(downloadUri.toString());
                        imageLink = upload.getmImageUrl();
                        mStatusDatabase.child("ProfilePicture").setValue(imageLink);
                    } else {
                        Toast.makeText(SetUpProfile.this, "upload failed here2: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            mStatusDatabase.child("ProfilePicture").setValue("https://firebasestorage.googleapis.com/v0/b/connectv6.appspot.com/o/uploads%2Fzzuserblackoutline.png?alt=media&token=ca15eb94-01b9-4e79-b327-79691a71aa0f");
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
                    .setAspectRatio(1, 1)
                    .setMinCropResultSize(200, 200)
                    .start(this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();

                setUpImg.setImageURI(resultUri);
                filePath = resultUri;

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
    /////////////////////////////////////////////////////////////////////// UPLOAD IMAGE ////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onBackPressed() {

        if (backTimePressed + 2000 > System.currentTimeMillis()) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            Toast.makeText(SetUpProfile.this, "Please Back Again To Exit The Application", Toast.LENGTH_SHORT).show();
        }
        backTimePressed = System.currentTimeMillis();
    }
}
