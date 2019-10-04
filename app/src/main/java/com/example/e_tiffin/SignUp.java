package com.example.e_tiffin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

public class SignUp extends AppCompatActivity {

    MaterialEditText editId,edtPhone,edtName,edtPassword;
    MaterialButton btnsignUp;

    public FirebaseDatabase database;
    public DatabaseReference table_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        editId=(MaterialEditText)findViewById(R.id.enterId);
        edtName     = (MaterialEditText)findViewById(R.id.enterName);
        edtPhone    = (MaterialEditText)findViewById(R.id.enterPhone);
        edtPassword = (MaterialEditText)findViewById(R.id.enterPassword);

        btnsignUp   = (MaterialButton)findViewById(R.id.material_button_sign_up);

         database = FirebaseDatabase.getInstance();
        table_user = database.getReference("User");

        btnsignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Common.CheckInternet(getBaseContext())) {


                    final ProgressDialog mDialog = new ProgressDialog(SignUp.this);
                    mDialog.setMessage("Please Wait ....");
                    mDialog.show();

                    table_user.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if (editId.getText().toString().isEmpty() || edtName.getText().toString().isEmpty() || edtPassword.getText().toString().isEmpty() || edtPhone.getText().toString().isEmpty()) {

                                mDialog.dismiss();

                                Toast.makeText(SignUp.this, "Please Fulfill The Empty Field", Toast.LENGTH_SHORT).show();
                            } else {


                                if (dataSnapshot.child(edtPhone.getText().toString()).exists()) {
                                    mDialog.dismiss();

                                    Toast.makeText(SignUp.this, "User Already Registered!Please try Another Phone Number", Toast.LENGTH_SHORT).show();
                                    // Log.d(SignUp.this,"Register");

                                } else {

                                    mDialog.dismiss();

                                    User user = new User(editId.getText().toString(), edtName.getText().toString(), edtPassword.getText().toString());
                                    table_user.child(edtPhone.getText().toString()).setValue(user);


                                    Toast.makeText(SignUp.this, "Sign Up Successful!", Toast.LENGTH_SHORT).show();

                                    Intent homeIntent = new Intent(SignUp.this, SignIn.class);
                                    startActivity(homeIntent);

                                }

                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
                else {

                    Toast.makeText(SignUp.this, "Please Check Your Internet Connection!", Toast.LENGTH_SHORT).show();
                    return;
                }

            }
        });
    }



}
