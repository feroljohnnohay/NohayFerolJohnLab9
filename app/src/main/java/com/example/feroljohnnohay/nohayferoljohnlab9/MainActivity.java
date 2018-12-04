package com.example.feroljohnnohay.nohayferoljohnlab9;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    FirebaseDatabase db;
    DatabaseReference DB;
    EditText eFName, eAge, eGender;
    TextView rName, rAge, rGender;
    ArrayList<Person> personL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = FirebaseDatabase.getInstance();
        DB = db.getReference("nohayferoljohnlab9");
        personL = new ArrayList<>();

        eFName = findViewById(R.id.etFname);
        eAge = findViewById(R.id.etAge);
        eGender = findViewById(R.id.etGender);

        rName = findViewById(R.id.rName);
        rAge = findViewById(R.id.rAge);
        rGender = findViewById(R.id.rGender);

    }

    protected void onStart() {
        super.onStart();

        DB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ss : dataSnapshot.getChildren()) {
                    Person person = ss.getValue(Person.class);
                    personL.add(person);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void saveRecord(View v) {

        try {
            String fullName = eFName.getText().toString();
            Integer age = Integer.parseInt(eAge.getText().toString().trim());
            String gender = eGender.getText().toString().trim();

            String key = DB.push().getKey();
            Person person = new Person(fullName, age, gender);
            DB.child(key).setValue(person);

            Toast.makeText(this, "Record inserted.", Toast.LENGTH_LONG).show();
        } catch (Exception e){
            Toast.makeText(this, "Error has occured.", Toast.LENGTH_LONG).show();
        }
    }

    public void retrieveData(View v){
        DB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String nameData = dataSnapshot.child("fullName").getValue(String.class);
                    String ageData = dataSnapshot.child("age").getValue(String.class);
                    String genderData = dataSnapshot.child("gender").getValue(String.class);

                    rName.setText(nameData);
                    rAge.setText(ageData);
                    rGender.setText(genderData);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
