package com.digipodium.bakerylogin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.digipodium.bakerylogin.model.Cake;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

public class AddCakeActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {
    static final int REQUEST_IMAGE_OPEN = 1;

    private EditText editName;
    private EditText editPrice;
    private ImageView imgCake;
    private Button btnAdd;
    private StorageReference mStorageRef;
    private boolean permissionProvided;
    private ProgressBar pb;
    String[] permissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private boolean isImageSelected = false;
    private Uri mPhotoUri = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cake);
        editName = findViewById(R.id.editName);
        editPrice = findViewById(R.id.editPrice);
        imgCake = findViewById(R.id.imgCake);
        btnAdd = findViewById(R.id.btnAdd);
        pb = findViewById(R.id.pb);
        mStorageRef = FirebaseStorage.getInstance().getReference();

        permissionHandler();
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isImageSelected) {
                    pb.setVisibility(View.VISIBLE);
                    uploadImage(mPhotoUri);
                }
            }
        });
    }

    private void permissionHandler() {
        if (!EasyPermissions.hasPermissions(this, permissions)) {
            EasyPermissions.requestPermissions(this, "storage permission", 32, permissions);
        } else {
            permissionProvided = true;
        }
    }

    public void selectImage(View v) {
        if (permissionProvided) {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.setType("image/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            // Only the system receives the ACTION_OPEN_DOCUMENT, so no need to test.
            startActivityForResult(intent, REQUEST_IMAGE_OPEN);
        } else {
            Snackbar.make(editName, "no permission", Snackbar.LENGTH_INDEFINITE).setAction("try again", new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            }).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_OPEN && resultCode == RESULT_OK) {
            try {
                Uri fullPhotoUri = data.getData();

                ImageView imgCake = findViewById(R.id.imgCake);
                Glide.with(this).load(fullPhotoUri).into(imgCake);
                if (fullPhotoUri != null) {
                    mPhotoUri = fullPhotoUri;
                    isImageSelected = true;
                } else {
                    Snackbar.make(editName, "the uri could not be found", Snackbar.LENGTH_INDEFINITE).show();
                    isImageSelected = false;
                }
            } catch (Exception e) {
                Toast.makeText(this, "omg" + e, Toast.LENGTH_LONG).show();
            }

        }
    }

    private void uploadImage(Uri fullPhotoUri) {
        final StorageReference imageRef = mStorageRef.child("cakes/" + fullPhotoUri.getLastPathSegment());
        imageRef.putFile(fullPhotoUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        pb.setVisibility(View.GONE);
                        uploadCakeDetails(uri);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                pb.setVisibility(View.GONE);
                Snackbar.make(editName, "Image upload failed" + exception.getMessage(), Snackbar.LENGTH_LONG).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                long count = taskSnapshot.getTotalByteCount();
                long bytesTransferred = taskSnapshot.getBytesTransferred();
                Log.d("upload_stat", String.valueOf((bytesTransferred / count) * 100));
            }
        });
    }

    private void uploadCakeDetails(Uri uri) {
        FirebaseFirestore store = FirebaseFirestore.getInstance();
        try {
            FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                    .setTimestampsInSnapshotsEnabled(true)
                    .build();
            store.setFirestoreSettings(settings);
        } catch (Exception e) {

        }
        String name = editName.getText().toString();
        String price = editPrice.getText().toString();
        Cake cake = new Cake(name, price, uri.toString());

        store.collection("cakes").add(cake).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Snackbar.make(editName, "Data uploaded", Snackbar.LENGTH_LONG).show();
                updateUI();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Snackbar.make(editName, "Data Could not be uploaded, Retry" + e.getMessage(), Snackbar.LENGTH_INDEFINITE).show();
            }
        });
    }

    private void updateUI() {
        editPrice.setText("");
        editName.setText("");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        permissionProvided = true;
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        permissionProvided = false;
    }
}
