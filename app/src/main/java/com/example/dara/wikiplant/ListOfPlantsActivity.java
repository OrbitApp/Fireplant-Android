package com.example.dara.wikiplant;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListOfPlantsActivity extends AppCompatActivity {

    private static final String TAG = ListOfPlantsActivity.class.getSimpleName();
    @BindView(R.id.RecyclerView)
    android.support.v7.widget.RecyclerView RecyclerView;
    FirebaseDatabase database;
    DatabaseReference myRef;
    List<Plant> listData;
    private ItemPlantAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_plants);
        ButterKnife.bind(this);
        initRecyclerview();

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("plants");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                listData.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Plant value = dataSnapshot1.getValue(Plant.class);
                    String name = value.getName();
                    String description = value.getDescription();

               //     String imageURI = value.getImageUrl();
                  System.out.println("ImageURL"+  value.getImageUrl());
                    Log.d(TAG, "Values in snapshot"+value.toString());
                    listData.add(value);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        System.out.println("List data"+listData);


    }

    private void initRecyclerview() {
        listData = new ArrayList<>();
        adapter = new ItemPlantAdapter(listData, this);
        RecyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        RecyclerView.setLayoutManager(llm);
        RecyclerView.setAdapter(adapter);
    }
}