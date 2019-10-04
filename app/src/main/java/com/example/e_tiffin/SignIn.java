package com.example.e_tiffin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.e_tiffin.Common.Common;
import com.example.e_tiffin.Model.User;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import io.paperdb.Paper;

public class SignIn extends AppCompatActivity {

    MaterialEditText editPhone,editPassword;
    MaterialButton btnsignIn;
    //CheckBox ckbRemember1;

    com.rey.material.widget.CheckBox ckbRemember1;

    public  FirebaseDatabase database;
    public DatabaseReference table_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);



        editPassword = (MaterialEditText)findViewById(R.id.editPassword);
        editPhone    = (MaterialEditText)findViewById(R.id.editPhone);

        btnsignIn    = (MaterialButton)findViewById(R.id.material_button_sign_in);

        ckbRemember1= (com.rey.material.widget.CheckBox) findViewById(R.id.ckbRemember);

        Paper.init(this);


         database = FirebaseDatabase.getInstance();
         table_user = database.getReference("User");

         btnsignIn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {

                 if (Common.CheckInternet(getBaseContext())) {

                     //save user && password

                     if (ckbRemember1.isChecked()) {

                         Paper.book().write(Common.USER_KEY, editPhone.getText().toString());
                         Paper.book().write(Common.PWD_KEY, editPassword.getText().toString());
                     }

                     final ProgressDialog mDialog = new ProgressDialog(SignIn.this);
                     mDialog.setMessage("Please Wait ....");
                     mDialog.show();

                     if (editPhone.getText().toString().isEmpty() || editPassword.getText().toString().isEmpty()) {

                         mDialog.dismiss();

                         Toast.makeText(SignIn.this, "Please Fulfill The Empty Field", Toast.LENGTH_SHORT).show();


                     }else{


                     table_user.addValueEventListener(new ValueEventListener() {
                         @Override
                         public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                             if (dataSnapshot.child(editPhone.getText().toString()).exists()) {
                                 mDialog.dismiss();
                                 //Get User Information

                                 User user = dataSnapshot.child(editPhone.getText().toString()).getValue(User.class);
                                 user.setPhone(editPhone.getText().toString()); //set phone

                                 if (user.getPassword().equals(editPassword.getText().toString())) {
                                     Intent homeIntent = new Intent(SignIn.this, Home.class);
                                     Common.currentUser = user;
                                     startActivity(homeIntent);
                                     finish();
                                 } else {
                                     Toast.makeText(SignIn.this, "Wrong Password!!!", Toast.LENGTH_SHORT).show();
                                 }


                             } else {
                                 mDialog.dismiss();
                                 Toast.makeText(SignIn.this, "User Does Not Exist", Toast.LENGTH_SHORT).show();
                             }
                         }

                         @Override
                         public void onCancelled(@NonNull DatabaseError databaseError) {

                         }
                     });
                 }

                 }
                 else {

                     Toast.makeText(SignIn.this, "Please Check your Internet Connection", Toast.LENGTH_SHORT).show();

                    // return;
                 }
             }

         });
    }






}
