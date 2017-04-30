package com.example.dara.wikiplant;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SinglePlantActivity extends AppCompatActivity {

    private static final String TAG = SinglePlantActivity.class.getSimpleName();
    String key;
    @BindView(R.id.imageView)
    ImageView mImageView;
    @BindView(R.id.textView_name_of_flower)
    EditText mTextViewNameOfFlower;
    @BindView(R.id.textView_desc_of_flower)
    EditText mTextViewDescOfFlower;
    @BindView(R.id.textView_genus_of_flower)
    EditText mTextViewGenusOfFlower;
    @BindView(R.id.textView_status_of_flower)
    EditText mTextViewStatusOfFlower;
    @BindView(R.id.textView_taxonomy_of_flower)
    EditText mTextViewTaxonomyOfFlower;
    @BindView(R.id.textView_user)
    EditText mTextViewUser;
    @BindView(R.id.button_edit_flower)
    Button mButtonEditFlower;
    @BindView(R.id.text_view_latitude)
    EditText mTextViewLatitude;
    @BindView(R.id.text_view_longitude)
    EditText mTextViewLongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_plant);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        key = intent.getStringExtra("key");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("plants/" + key);

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Plant value = dataSnapshot.getValue(Plant.class);
                mTextViewNameOfFlower.setText(value.getName());
                mTextViewGenusOfFlower.setText(value.getGenus());
                if (value.getUnits() != null && value.getUnits().get(0) != null) {
                    mTextViewLatitude.setText(value.getUnits().get(0).getLatitude());
                    mTextViewLongitude.setText(value.getUnits().get(0).getLongitude());

                }
                mTextViewDescOfFlower.setText(value.getDescription());
                Picasso.with(SinglePlantActivity.this)
                        .load(value.getImages().get(0).getUrl())
                        .into(mImageView);
                Log.d(TAG, "Value is: " + value.toString());
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }
}
