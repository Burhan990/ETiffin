package com.example.e_tiffin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.e_tiffin.Common.Common;
import com.example.e_tiffin.Interface.ItemClickListener;
import com.example.e_tiffin.Model.Food;
import com.example.e_tiffin.ViewHolder.FoodViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class FoodList extends AppCompatActivity {

    LottieAnimationView Homeicon;

    RecyclerView recycler_food;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference foodlist;

    FirebaseRecyclerAdapter<Food, FoodViewHolder> adapter;

    FirebaseRecyclerAdapter<Food,FoodViewHolder> searchAdapter;
    List<String> suggestList = new ArrayList<>();
    MaterialSearchBar materialSearchBar;

    String categoryid="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);

        Homeicon=(LottieAnimationView)findViewById(R.id.homeicon);
        Homeicon.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
                Intent i=new Intent(FoodList.this,Home.class);
                startActivity(i);
            }
        });


        //Init Firebase
        database = FirebaseDatabase.getInstance();
        foodlist = database.getReference("Foods");
        foodlist.keepSynced(true);


        //Load Menu
        recycler_food = (RecyclerView)findViewById(R.id.recycler_food);
        recycler_food.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recycler_food.setLayoutManager(layoutManager);


        //Get intent Here
        if(getIntent()!=null)
        {
            categoryid=getIntent().getStringExtra("CategoryId");
        }
        if(!categoryid.isEmpty() && categoryid !=null)
        {

            if (Common.CheckInternet(getBaseContext())) {
                loadlistFood(categoryid);
            }else {
                Toast.makeText(this, "Please check Internet Connection!", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        //Search


        materialSearchBar = (MaterialSearchBar)findViewById(R.id.search_bar);
        materialSearchBar.setHint("Search For Food");
        loadSuggest();
        materialSearchBar.setLastSuggestions(suggestList);
        materialSearchBar.setCardViewElevation(10);

        materialSearchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                List<String> suggest = new ArrayList<String>();

                for(String search:suggestList)
                {
                    if(search.toLowerCase().contains(materialSearchBar.getText().toLowerCase()))
                        suggest.add(search);
                }
                materialSearchBar.setLastSuggestions(suggest );



            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        materialSearchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                //When Search Bar is Close
                //Restore original suggest adapter
                if(!enabled)
                    recycler_food.setAdapter(adapter);
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {

                startSearch(text);

            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });

    }



    private void startSearch(CharSequence text) {


        FirebaseRecyclerOptions<Food> op = new FirebaseRecyclerOptions.Builder<Food>().setQuery(foodlist.orderByChild("name").equalTo(text.toString()),Food.class).build();
        searchAdapter=new FirebaseRecyclerAdapter<Food, FoodViewHolder>(op) {
            @NonNull
            @Override
            public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_item,parent,false);
                return new FoodViewHolder(view);
            }
            @Override
            protected void onBindViewHolder(@NonNull FoodViewHolder holder, int position, @NonNull Food model) {
                holder.food_name.setText(model.getName());

                Picasso.get().load(model.getImage()).into(holder.foodimage);

                final Food local = model;

                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent fd = new Intent(FoodList.this,FoodDetail.class);
                        fd.putExtra("FoodId",searchAdapter.getRef(position).getKey());
                        startActivity(fd);
                    }
                });
            }
        };
        recycler_food.setAdapter(searchAdapter);
        searchAdapter.startListening();

    }

    private void loadSuggest() {

        foodlist.orderByChild("menuId").equalTo(categoryid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot:dataSnapshot.getChildren()){
                    Food item = postSnapshot.getValue(Food.class);
                    suggestList.add(item.getName());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void loadlistFood(String categoryid) {

        FirebaseRecyclerOptions<Food> options =
                new FirebaseRecyclerOptions.Builder<Food>()
                        .setQuery(foodlist.orderByChild("menuId").equalTo(categoryid), Food.class)
                        .build();
        adapter= new FirebaseRecyclerAdapter<Food,FoodViewHolder>(options)
        {
            @NonNull
            @Override
            public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.food_item, parent, false);

                return new FoodViewHolder(view);
            }
            @Override
            protected void onBindViewHolder(@NonNull FoodViewHolder viewHolder, int position, @NonNull Food model) {
                viewHolder.food_name.setText(model.getName());
                Picasso.get().load(model.getImage()).into(viewHolder.foodimage);
                final Food local = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent foodDetail = new Intent(FoodList.this,FoodDetail.class);
                        foodDetail.putExtra("FoodId",adapter.getRef(position).getKey());
                        startActivity(foodDetail);
                    }
                });
            }
        };

        adapter.startListening();
        recycler_food.setAdapter(adapter);



    }


    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();

    }



}
