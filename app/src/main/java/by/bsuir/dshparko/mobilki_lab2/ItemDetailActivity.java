package by.bsuir.dshparko.mobilki_lab2;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

public class ItemDetailActivity extends AppCompatActivity {

    private ImageView imageView;
    private TextView  nameText, priceText, descriptionText;
    private String description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        imageView = findViewById(R.id.item_image_view);
        nameText = findViewById(R.id.item_name);
        priceText = findViewById(R.id.item_price);
        descriptionText = findViewById(R.id.item_description);

        nameText.setText(getIntent().getStringExtra("name"));
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

                Elements data = document.getElementsByClass("woocommerce-Tabs-panel woocommerce-Tabs-panel--custom_tab_s panel entry-content wc-tab");

                description = data.select("p")
                        .text();

            }catch (IOException e){
                e.printStackTrace();
            }
            return null;
        }
    }
}