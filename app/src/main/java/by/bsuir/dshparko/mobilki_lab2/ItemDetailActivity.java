package by.bsuir.dshparko.mobilki_lab2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Objects;

public class ItemDetailActivity extends AppCompatActivity {

    private ImageView imageView;
    private TextView  nameText, priceText, descriptionText;
    private String description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setContentView(R.layout.activity_item_detail);
        nameText = findViewById(R.id.item_name);

        imageView = findViewById(R.id.item_image_view);

        priceText = findViewById(R.id.item_price);
        descriptionText = findViewById(R.id.item_description);
        setTitle(getIntent().getStringExtra("name"));
      //  nameText.setText(getIntent().getStringExtra("name"));
        priceText.setText(getIntent().getStringExtra("price"));
System.out.println(getIntent().getStringExtra("image"));
      Glide.with(getApplicationContext())
           .load("https:"+ Uri.parse(getIntent().getStringExtra("image")))
             .into(imageView);
      /*  Picasso.with(getBaseContext())
                .load(getIntent().getStringExtra("image"))
               .placeholder(R.drawable.icon)
                .into(imageView);
*/
        Content content = new Content();
        content.execute();


    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                Intent intent1  =new Intent(this,HomeActivity.class);
                startActivity(intent1);
                return true;
            default:return super.onOptionsItemSelected(item);
        }
    }

    private class Content extends AsyncTask<Void, Void, Void> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @SuppressLint("NotifyDataSetChanged")
        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            descriptionText.setText(description);

        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try{
                String detailUrl = getIntent().getStringExtra("detailUrl");
                Document document = Jsoup.connect(detailUrl).get();

                Elements data = document.getElementsByClass("b-product_details-content");
                System.out.println(data);

                description = data.select("p")
                        .text();

            }catch (IOException e){
                e.printStackTrace();
            }
            return null;
        }
    }
}