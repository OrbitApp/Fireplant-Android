package com.example.dara.wikiplant;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

public class AddPlantActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_TAKE_PHOTO = 1;
    private static final String TAG = AddPlantActivity.class.getSimpleName();
    @BindView(R.id.editText_name)
    EditText editTextName;
    @BindView(R.id.editText_genus)
    EditText editTextGenus;
    @BindView(R.id.editText_taxonomy)
    EditText editTextTaxonomy;
    @BindView(R.id.button_add)
    Button buttonAdd;
    @BindView(R.id.editText_description)
    EditText editTextDescription;
    @BindView(R.id.toolbar3)
    Toolbar mToolbar3;
    @BindView(R.id.editTextLatitude)
    EditText mEditTextLatitude;
    @BindView(R.id.editTextLongitude)
    EditText mEditTextLongitude;
    @BindView(R.id.button_add_images)
    Button mButtonAddImages;
    @BindView(R.id.plant_upload_thumb_recyclerview)
    RecyclerView mPlantUploadThumbRecyclerView;
    @BindView(R.id.test_image)
    ImageView mTestImage;
    String mCurrentPhotoPath;
    GalleryRecyclerViewAdapter mGalleryRecyclerViewAdapter;
    private DatabaseReference mDatabase;
    private StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_plant);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar3);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        inflateRecyclerView();
        mDatabase = database.getReference("plants");
        mStorageRef = FirebaseStorage.getInstance().getReference();
    }

    private void inflateRecyclerView() {
        mGalleryRecyclerViewAdapter = new GalleryRecyclerViewAdapter();
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mPlantUploadThumbRecyclerView.setLayoutManager(manager);
        mPlantUploadThumbRecyclerView.setAdapter(mGalleryRecyclerViewAdapter);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            if (data == null) {
                Toast.makeText(this, "Error capturing image", Toast.LENGTH_SHORT).show();
                return;
            }
            Bundle extras = data.getExtras();
            //     try {
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            mTestImage.setImageBitmap(imageBitmap);
            // mGalleryRecyclerViewAdapter.addBitmaps(new UploadPlantImage(imageFile, imageBitmap));
            //uploadFile();
            //      } catch (IOException e) {
            //       e.printStackTrace();
            //      Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            //   }
        }
    }

    private void uploadFile() {
        File GFile = mGalleryRecyclerViewAdapter.getUploadPlantImages().get(0).getPlantImage();
        Uri file = Uri.fromFile(GFile);
        StorageReference riversRef = mStorageRef.child("plants");


        riversRef.putFile(file)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        Toast.makeText(AddPlantActivity.this, downloadUrl.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                        exception.printStackTrace();
                        Toast.makeText(AddPlantActivity.this, exception.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

   /* private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    } */

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.d(TAG, ex.getMessage());
                return;
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = null;
                try {
                    photoURI = FileProvider.getUriForFile(this,
                            BuildConfig.APPLICATION_ID + ".provider",
                            createImageFile());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    /*  private void dispatchTakePictureIntent() {
          Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
          // Ensure that there's a camera activity to handle the intent
          if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
              // Create the File where the photo should go
              File photoFile = null;
              try {
                  photoFile = createImageFile();
              } catch (IOException ex) {
                  // Error occurred while creating the File
                  return;
              }
              // Continue only if the File was successfully created
              if (photoFile != null) {
                  Uri photoURI = null;
                  try {

                      takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                      startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                  } catch (IOException e) {
                      e.printStackTrace();
                  }

              }
          }
      }
  */
    private void confirmAddEntry() {

    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), "Camera");
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    public void uploadEntry() {
        String name = editTextName.getText().toString();
        String genus = editTextGenus.getText().toString();
        String taxonomy = editTextTaxonomy.getText().toString();
        String description = editTextDescription.getText().toString();
        //upload image first
        mDatabase.push().setValue(new Plant(name, "pending", genus, taxonomy, description));
    }

    @OnClick({R.id.button_add_images, R.id.button_add})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.button_add_images:
                dispatchTakePictureIntent();
                break;
            case R.id.button_add:
                uploadEntry();
                break;
        }
    }
}
