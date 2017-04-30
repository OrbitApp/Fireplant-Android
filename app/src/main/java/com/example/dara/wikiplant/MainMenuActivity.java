package com.example.dara.wikiplant;

import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MainMenuActivity extends AppCompatActivity {

    private static final int WRITE_EXTERNAL_STORAGE_REQUEST = 101;
    private static final int REQUEST_TAKE_PHOTO = 102;
    private static final String TAG = MainMenuActivity.class.getSimpleName();
    @BindView(R.id.main_menu_toolbar)
    Toolbar mMainMenuToolbar;

    String mCurrentPhotoPath;
    PlantService mPlantService;
    private StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        ButterKnife.bind(this);
        setSupportActionBar(mMainMenuToolbar);
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://us-central1-fireplant-wiki.cloudfunctions.net/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        mPlantService = retrofit.create(PlantService.class);

        mStorageRef = FirebaseStorage.getInstance().getReference();

    }

    @OnClick({R.id.list_button, R.id.scan_button, R.id.button_add})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.list_button:
                startActivity(new Intent(this, ListOfPlantsActivity.class));
                break;
            case R.id.scan_button:
                try {
                    startCamera();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.button_add:
                startActivity(new Intent(this, AddPlantActivity.class));
                break;
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            // Show the thumbnail on ImageView
            Uri imageUri = Uri.parse(mCurrentPhotoPath);
            File file = new File(imageUri.getPath());

            //upload to cloud
            uploadFile(file);
            // ScanFile so it will be appeared on Gallery
            MediaScannerConnection.scanFile(this,
                    new String[]{imageUri.getPath()}, null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String path, Uri uri) {
                        }
                    });
        }
    }

    private void uploadFile(File GFile) {

        Uri file = Uri.fromFile(GFile);
        mStorageRef.child("plants/" + GFile.getName()).putFile(file)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        Toast.makeText(MainMenuActivity.this, downloadUrl.toString(), Toast.LENGTH_SHORT).show();
                        getIdentifier(downloadUrl);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                        exception.printStackTrace();
                        Toast.makeText(MainMenuActivity.this, exception.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


    }

    private void getIdentifier(Uri downloadUrl) {
        //retrofit
        mPlantService.getPlantKey(new ImageUrlPayload(downloadUrl.toString())).enqueue(new Callback<ImageUriDownload>() {
            @Override
            public void onResponse(Call<ImageUriDownload> call, Response<ImageUriDownload> response) {
                Log.d(TAG, response.body().toString());
                startDetailActivity(response.body().getKey());
            }

            @Override
            public void onFailure(Call<ImageUriDownload> call, Throwable t) {
                Log.e(TAG, t.getMessage());
            }
        });
    }

    private void startDetailActivity(String key) {
        if (key == null) {
            Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(this, SinglePlantActivity.class);
        intent.putExtra("key", key);
        startActivity(intent);
    }

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

    @OnClick(R.id.test_button)
    public void onViewClicked() {
        Intent intent = new Intent(this, SinglePlantActivity.class);
        intent.putExtra("key", "-KivtvLC0lTlMERFMBQA");
        startActivity(intent);
    }
}
