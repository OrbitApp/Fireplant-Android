package com.example.dara.wikiplant;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddPlantActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    @BindView(R.id.imageView)
    ImageView imageView;
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
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_plant);
        ButterKnife.bind(this);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference("plants");

    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @OnClick(R.id.button_add)
    public void onViewClicked() {
        String name = editTextName.getText().toString();
        String genus = editTextGenus.getText().toString();
        String taxonomy = editTextTaxonomy.getText().toString();
        String description = editTextDescription.getText().toString();
        mDatabase.push().setValue(new Plant(name, "pending", genus, taxonomy, description));
    }
}
