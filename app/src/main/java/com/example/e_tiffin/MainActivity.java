package com.example.e_tiffin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.e_tiffin.Common.Common;
import com.example.e_tiffin.Model.User;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    private MaterialButton sign_Up,sign_In;
    private TextView txtslogan;


    public  FirebaseDatabase database;
    public DatabaseReference table_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = FirebaseDatabase.getInstance();
        table_user = database.getReference("User");

        sign_Up=(MaterialButton)findViewById(R.id.btnSignUp);
        sign_In=(MaterialButton)findViewById(R.id.btnSignIn);

        txtslogan=(TextView)findViewById(R.id.textSlogan);


        //init paper

        Paper.init(this);


        Typeface face=Typeface.createFromAsset(getAssets(),"fonts/fonts.TTF");
        txtslogan.setTypeface(face);


        sign_Up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(MainActivity.this,SignUp.class);
                startActivity(intent);

            }
        });


        sign_In.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(MainActivity.this,SignIn.class);
                startActivity(intent);

            }
        });

        //check remember

        String user=Paper.book().read(Common.USER_KEY);
        String password=Paper.book().read(Common.PWD_KEY);

        if (user != null && password != null){

            if (!user.isEmpty() && !password.isEmpty()){
                login(user,password);
            }

        }

    }

    private void login(final String phone, final String password) {
        if (Common.CheckInternet(getBaseContext())) {

            //save user && password

            final ProgressDialog mDialog = new ProgressDialog(MainActivity.this);
            mDialog.setMessage("Please Wait ....");
            mDialog.show();


            table_user.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.child(phone).exists()) {
                        mDialog.dismiss();
                        //Get User Information

                        User user = dataSnapshot.child(phone).getValue(User.class);
                        user.setPhone(phone); //set phone

                        if (user.getPassword().equals(password)) {
                            Intent homeIntent = new Intent(MainActivity.this, Home.class);
                            Common.currentUser = user;
                            startActivity(homeIntent);
                            finish();
                        } else {
                            Toast.makeText(MainActivity.this, "Wrong Password!!!", Toast.LENGTH_SHORT).show();
                        }


                    } else {
                        mDialog.dismiss();
                        Toast.makeText(MainActivity.this, "User Does Not Exist", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
        else {

            Toast.makeText(MainActivity.this, "Please Check your Internet Connection", Toast.LENGTH_SHORT).show();

            // return;
        }
    }
}
