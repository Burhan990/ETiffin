package com.example.e_tiffin;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.e_tiffin.Common.Common;
import com.example.e_tiffin.Database.Database;
import com.example.e_tiffin.Model.Food;
import com.example.e_tiffin.Model.Order;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class FoodDetail extends AppCompatActivity {

    private LottieAnimationView Homeicon;

    TextView food_name,food_price,food_description;
    ImageView food_image;

    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton btnCart;
    ElegantNumberButton numberButton;

    String foodId="";

    FirebaseDatabase database;
    DatabaseReference foods;
    Food currentFood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);

        Homeicon=(LottieAnimationView)findViewById(R.id.homeicon);

        Homeicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(FoodDetail.this,Home.class);
                startActivity(i);
            }
        });


        //FireBase
        database = FirebaseDatabase.getInstance();
        foods = database.getReference("Foods");

        //Init View
        numberButton = (ElegantNumberButton)findViewById(R.id.number_button);
        btnCart = (FloatingActionButton)findViewById(R.id.btnCart);

        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

             new Database(FoodDetail.this).addToCart(new Order(foodId,
                     currentFood.getName(),
                     numberButton.getNumber(),
                     currentFood.getPrice(),
                     currentFood.getDiscount()));

                Toast.makeText(FoodDetail.this,"Added To Cart",Toast.LENGTH_SHORT).show();


            }
        });



        food_name = (TextView)findViewById(R.id.food_name1);
        food_image =(ImageView)findViewById(R.id.img_food);
        food_price = (TextView)findViewById(R.id.food_price);
        food_description=(TextView)findViewById(R.id.food_description);

        collapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.collapsing);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);

        if(getIntent() != null)
            foodId = getIntent().getStringExtra("FoodId");
        if(!foodId.isEmpty())
        {
            if (Common.CheckInternet(getBaseContext())) {
                getDetailFood(foodId);
            }else {
                Toast.makeText(this, "Please check Internet Connection!", Toast.LENGTH_SHORT).show();
                return;
            }
        }
    }

    private void getDetailFood(String foodId) {


        foods.child(foodId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentFood = dataSnapshot.getValue(Food.class);

                Picasso.get().load(currentFood.getImage()).into(food_image);
                collapsingToolbarLayout.setTitle(currentFood.getName());

                food_price.setText(currentFood.getPrice());
                food_name.setText(currentFood.getName());
                food_description.setText(currentFood.getDescription());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


}
