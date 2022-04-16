package by.bsuir.dshparko.mobilki_lab2.Parser;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import by.bsuir.dshparko.mobilki_lab2.ItemDetailActivity;
import by.bsuir.dshparko.mobilki_lab2.R;

public class ParseAdapter extends RecyclerView.Adapter<ParseAdapter.ViewHolder> {

    public ArrayList<ParseItem> parseItems;
    private Context context;

    public ParseAdapter(ArrayList<ParseItem> parseItems, Context context){
        this.parseItems = parseItems;
        this.context = context;
    }

    @NonNull
    @Override
    public ParseAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ParseItem parseItem = parseItems.get(position);
        holder.nameTxt.setText(parseItem.getName());
        holder.priceTxt.setText(parseItem.getPrice());
       // Picasso.with(this.context).load(parseItem.getImgUrl()).into(holder.imageView);
        System.out.println(parseItem.getImgUrl());
        Glide.with(this.context)
                .load("https:"+parseItem.getImgUrl())
                .into(holder.imageView);
       // Glide.with(this.context)
      //          .load(parseItem.getImgUrl())
         //       .into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return parseItems.size();
    }

    public void clearItemsList () {
        parseItems.clear();
        notifyDataSetChanged();
    }

    public String getMaxPrice(){
        int maxPrice = 0;
        for(ParseItem parseItem : parseItems){
            if(maxPrice < Integer.parseInt(parseItem.getPrice())){
                maxPrice = Integer.parseInt(parseItem.getPrice());
            }
        }
        return Integer.toString(maxPrice);
    }

    public void setFilter(ArrayList<ParseItem> newList){
        parseItems = new ArrayList<>();
        parseItems.addAll(newList);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageView;
        TextView brandTxt, nameTxt, priceTxt;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.item_image_view);
            nameTxt = itemView.findViewById(R.id.item_name);
            priceTxt = itemView.findViewById(R.id.item_price);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            ParseItem parseItem = parseItems.get(position);

            Intent intent = new Intent(context, ItemDetailActivity.class);
            intent.putExtra("name", parseItem.getName());
            intent.putExtra("price",parseItem.getPrice());
            intent.putExtra("image",parseItem.getImgUrl());
            intent.putExtra("detailUrl",parseItem.getDetailUrl());
            context.startActivity(intent);
        }
    }
}