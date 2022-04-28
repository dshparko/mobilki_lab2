package by.bsuir.dshparko.mobilki_lab2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import by.bsuir.dshparko.mobilki_lab2.Parser.ParseAdapter;
import by.bsuir.dshparko.mobilki_lab2.Parser.ParseItem;
import by.bsuir.dshparko.mobilki_lab2.Utilities.NetworkChangeListener;

public class HomeActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener{

    boolean searchIsOpen;
    private AppCompatButton searchButton;
    private SearchView searchView;
    private ArrayList<ParseItem> parseItems = new ArrayList<>();
    private RecyclerView recyclerView;
    private ParseAdapter parseAdapter;
    private NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getSupportActionBar().hide();

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
        SwitchCompat switchh = findViewById(R.id.switch_button);
        if (switchh != null) {
            switchh.setOnCheckedChangeListener(this);
        }

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
        searchView = findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                newText = newText.toLowerCase();
                ArrayList<ParseItem> newList = new ArrayList<>();
                for (ParseItem parseItem : parseItems){
                    String name = parseItem.getName().toLowerCase();
                    if( name.contains(newText)){
                        newList.add(parseItem);
                    }
                }
                parseAdapter.setFilter(newList);
                return true;
            }
        });
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        Toast.makeText(this, "Отслеживание переключения: " + (isChecked ? "on" : "off"),
                Toast.LENGTH_SHORT).show();
        if(isChecked==true){
            System.out.println("DARKMODE");
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
            int theme = sp.getInt("THEME", R.style.AppTheme);
            setTheme(theme);
           // getApplication().setTheme(R.style.AppTheme);
        }else{
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
            int theme = sp.getInt("THEME", R.style.Theme_Mobilki_lab2);
            setTheme(theme);
        }
    }    public static boolean isNetworkAvailable() {
        return network_available;
    }

    public static double GLOBAL_CURRENCY_COEFF = 1.0;

    public static double GLOBAL_CURRENCY_BYN = 1.0;
    public static boolean network_available = true;
    public static double USD_IN_BYN = 0.0;
    private static HttpURLConnection connection;
    private static final String USD_IN_BYN_URL = "https://www.nbrb.by/api/exrates/rates/431";
    private StringBuffer responseContent;
    private void handleUsdInBynCurrency(){
        network_available = true;
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            try {
                URL url = new URL(USD_IN_BYN_URL);
                connection = (HttpURLConnection) url.openConnection();

                connection.setRequestMethod("GET");
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);

                int status = connection.getResponseCode();
                Log.e("CONNECTION STATUS", String.valueOf(status));

                BufferedReader reader;
                String line;
                responseContent = new StringBuffer();

                if (status > 299){
                    reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                    while ((line = reader.readLine()) != null){
                        responseContent.append(line);
                    }
                    reader.close();
                } else{
                    reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    while ((line = reader.readLine()) != null){
                        responseContent.append(line);
                    }
                    reader.close();
                }
                Log.e("JSON RESPONSE", responseContent.toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                connection.disconnect();
            }
        });

        try {
            executor.shutdown();
            executor.awaitTermination(7, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        try {
            JSONObject jsonObject = new JSONObject(responseContent.toString());
            USD_IN_BYN = jsonObject.getDouble("Cur_OfficialRate");
            GLOBAL_CURRENCY_BYN = USD_IN_BYN;
            Log.e("USDINBYN", String.valueOf(USD_IN_BYN));
        } catch (Exception e) {
            network_available = false;
        }
    }

    public void showPopupMenu(View v) {
        PopupMenu popupMenu = new PopupMenu(this, v);
        popupMenu.inflate(R.menu.menu_rate);

        popupMenu
                .setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu1:
                                Toast.makeText(getApplicationContext(),
                                        "Вы выбрали PopupMenu 1",
                                        Toast.LENGTH_SHORT).show();
                                return true;
                            case R.id.menu2:
                                Toast.makeText(getApplicationContext(),
                                        "Вы выбрали PopupMenu 2",
                                        Toast.LENGTH_SHORT).show();
                                return true;
                            case R.id.menu3:
                                Toast.makeText(getApplicationContext(),
                                        "Вы выбрали PopupMenu 3",
                                        Toast.LENGTH_SHORT).show();
                                return true;
                            default:
                                return false;
                        }
                    }
                });

        popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu menu) {
                Toast.makeText(getApplicationContext(), "onDismiss",
                        Toast.LENGTH_SHORT).show();
            }
        });
        popupMenu.show();
    }


    @Override
    protected void onStart() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener,filter);
        super.onStart();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(networkChangeListener);
        super.onStop();
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
                    String imgUrl = data.select("picture.b-product_tile-image").select("img")
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
