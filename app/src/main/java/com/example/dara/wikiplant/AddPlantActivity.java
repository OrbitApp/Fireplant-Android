package com.example.dara.wikiplant;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class AddPlantActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_TAKE_PHOTO = 1;
    private static final String TAG = AddPlantActivity.class.getSimpleName();
    private static final int WRITE_EXTERNAL_STORAGE_REQUEST = 100;
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
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            // Show the thumbnail on ImageView
            Uri imageUri = Uri.parse(mCurrentPhotoPath);
            File file = new File(imageUri.getPath());

            mGalleryRecyclerViewAdapter.addBitmaps(file);

            // ScanFile so it will be appeared on Gallery
            MediaScannerConnection.scanFile(this,
                    new String[]{imageUri.getPath()}, null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String path, Uri uri) {
                        }
                    });
        }
    }

    private void uploadFile() {
        if (mGalleryRecyclerViewAdapter.getUploadPlantImages() == null) {
            return;
        }
        File GFile = mGalleryRecyclerViewAdapter.getUploadPlantImages().get(0);
        Uri file = Uri.fromFile(GFile);
        Toast.makeText(this, GFile.getName(), Toast.LENGTH_SHORT).show();
        mStorageRef.child("plants/" + GFile.getName()).putFile(file)
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

    private void dispatchTakePictureIntent() throws IOException {
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
                Uri photoURI = FileProvider.getUriForFile(this,
                        BuildConfig.APPLICATION_ID + ".provider",
                        createImageFile());
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
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
                try {
                    startCamera();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.button_add:
                uploadFile();
                // uploadEntry();
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == WRITE_EXTERNAL_STORAGE_REQUEST && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            try {
                dispatchTakePictureIntent();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    void startCamera() throws IOException {
        //request for runtime permission
        if (ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{WRITE_EXTERNAL_STORAGE, CAMERA},
                    WRITE_EXTERNAL_STORAGE_REQUEST);
            return;
        }
        dispatchTakePictureIntent();

    }


}
