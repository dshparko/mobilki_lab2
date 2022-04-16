package by.bsuir.dshparko.mobilki_lab2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

import by.bsuir.dshparko.mobilki_lab2.Parser.ParseAdapter;
import by.bsuir.dshparko.mobilki_lab2.Parser.ParseItem;
import by.bsuir.dshparko.mobilki_lab2.Utilities.NetworkChangeListener;

public class HomeActivity extends AppCompatActivity {

    private ArrayList<ParseItem> parseItems = new ArrayList<>();
    private RecyclerView recyclerView;
    private ParseAdapter parseAdapter;
    private NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setTitle("Home");

        recyclerView = findViewById(R.id.recycler_view);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        parseAdapter = new ParseAdapter(parseItems, this);
        recyclerView.setAdapter(parseAdapter);

        Content content = new Content("https://www.nastygal.com/eu/womens/dresses");
        content.execute();
        @SuppressLint("ResourceType") TabLayout toolbar;
        toolbar = findViewById(R.id.toolbar);
        toolbar.selectTab(toolbar.getTabAt(0),true);
        toolbar.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:

                        Intent intent2 = new Intent(HomeActivity.this, HomeActivity.class);
                        startActivity(intent2);
                        break;
                    case 1:

                        Intent intent1 = new Intent(HomeActivity.this, UserActivity.class);
                        startActivity(intent1);
                        break;
                    case 2:

                        Intent intent3 = new Intent(HomeActivity.this, SearchActivity.class);
                        startActivity(intent3);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private class Content extends AsyncTask<Void, Void, ArrayList<ParseItem>> {

        String url = "";


        private Content(String url){
            this.url = url;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @SuppressLint("NotifyDataSetChanged")
        @Override
        protected void onPostExecute(ArrayList<ParseItem> unused) {
            super.onPostExecute(unused);
            parseAdapter.notifyDataSetChanged();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected ArrayList<ParseItem> doInBackground(Void... voids) {
            try{
                Document document = Jsoup.connect(url).get();

                Elements data = document.getElementsByClass("l-plp_grid").select("section");
                System.out.println(data);

                int size = data.size();
                int k = 1;
                for (int i = 0; i < size; i++){
                    String imgUrl = data.select("a.b-product_tile-image_link").select("img")
                            .eq(i+k).attr("src").trim();

                    String name = data.select("a.b-product_tile-link")
                            .eq(i).text();
                    String price = data
                            .select("span.b-price-item.m-new")
                           // .select("span.b-price-item m-new")
                            .eq(i).text();

                   // price = price.substring(1, price.length()-1);
                    String detailUrl = data.select("h3.b-product_tile-title")
                            .select("a")
                            .eq(i).attr("href");

                    System.out.println(imgUrl+"  "+"  "+name+"  "+price+"  "+detailUrl);
                    parseItems.add(new ParseItem(imgUrl,  name, price, detailUrl));

                    k++;
                }
            }catch (IOException e){
                e.printStackTrace();
            }
            return null;
        }
    }
}
