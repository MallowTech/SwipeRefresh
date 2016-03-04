package com.mallow.swiperefresh;

import android.graphics.Color;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    private SwipeRefreshLayout swipeView;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private ScrollView emptyView;
    private String[] originalValues = { "Great Wall of China", "Taj Mahal",
            "The Colosseum", "Petra", "Machu Picchu", "Christ the Redeemer" };
    private String[] loadMoreValues = { "Pyramid", "Sydney Opera House",
            "Grand Canyon", "Chichen Itza", "CN Tower", "Great Barrier Reef" };

    private List<String> cityList;
    boolean refreshToggle = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        swipeView = (SwipeRefreshLayout) findViewById(R.id.swipe_view);
        swipeView.setOnRefreshListener(this);
        swipeView.setColorSchemeColors(Color.GRAY, Color.GREEN, Color.BLUE,
                Color.RED, Color.CYAN);
        swipeView.setDistanceToTriggerSync(20);// in dips
        swipeView.setSize(SwipeRefreshLayout.DEFAULT);// LARGE also can be used
        emptyView = (ScrollView) findViewById(R.id.tv_empty);
        listView = (ListView) findViewById(R.id.listView);

        cityList = new ArrayList<>();
        cityList.addAll(Arrays.asList(originalValues));
        adapter = new ArrayAdapter<String>(getApplicationContext(),
                R.layout.adapter_list_item, cityList);
        listView.setAdapter(adapter);
        listView.setEmptyView(emptyView);
    }

    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (refreshToggle) {
                refreshToggle = false;
                cityList = Arrays.asList(loadMoreValues);
                adapter = new ArrayAdapter<String>(getApplicationContext(),
                        R.layout.adapter_list_item, cityList);
            } else {
                refreshToggle = true;
                cityList = Arrays.asList(originalValues);
                adapter = new ArrayAdapter<String>(getApplicationContext(),
                        R.layout.adapter_list_item, cityList);
            }
            listView.setAdapter(adapter);
            swipeView.postDelayed(new Runnable() {

                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(),
                            "city list refreshed", Toast.LENGTH_SHORT).show();
                    swipeView.setRefreshing(false);
                }
            }, 1000);
        };
    };

    @Override
    public void onRefresh() {
        swipeView.postDelayed(new Runnable() {

            @Override
            public void run() {
                swipeView.setRefreshing(true);
                handler.sendEmptyMessage(0);
            }
        }, 1000);
    }
}
