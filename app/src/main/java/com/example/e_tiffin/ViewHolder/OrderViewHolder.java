package com.example.e_tiffin.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.e_tiffin.Interface.ItemClickListener;
import com.example.e_tiffin.R;

public class OrderViewHolder  extends RecyclerView.ViewHolder implements View.OnClickListener{


    public TextView txtOrderId,txtOrderStatus,txtOrderPhone,txtOrderAddress;

    private ItemClickListener itemClickListener;


    public OrderViewHolder(View itemView) {
        super(itemView);
        txtOrderAddress=(TextView)itemView.findViewById(R.id.order_address);
        txtOrderPhone=(TextView)itemView.findViewById(R.id.order_phone);
        txtOrderStatus=(TextView)itemView.findViewById(R.id.order_status);
        txtOrderId=(TextView)itemView.findViewById(R.id.order_id);

        itemView.setOnClickListener(this);

    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view,getAdapterPosition(),false);
    }
}
