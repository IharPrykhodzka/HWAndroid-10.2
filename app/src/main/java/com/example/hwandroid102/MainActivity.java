package com.example.hwandroid102;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity  {

    private List<Map<String, String>> contentList;
    private SharedPreferences sharedPref;
    private static String LARGE_TEXT = "large_text";
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initSharedPreferences();

        initList();

        onRefresh();

    }

    private void initList() {
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        ListView list = findViewById(R.id.list);

        String[] values = prepareContent();

        final BaseAdapter listContentAdapter = createAdapter(values);

        list.setAdapter(listContentAdapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                contentList.get(position).remove("length");
                contentList.get(position).remove("text");

                listContentAdapter.notifyDataSetChanged();
            }
        });

    }

    @NonNull
    private SimpleAdapter createAdapter(String[] stringsTxt) {
        contentList = new ArrayList<>();
        prepareAdapterContetn(stringsTxt);

        return new SimpleAdapter(this, contentList,
                R.layout.list_with_content,
                new String[]{"text", "length"},
                new int[]{R.id.firstText, R.id.secondText});
    }

    @NonNull
    private List<Map<String, String>> prepareAdapterContetn(String[] stringsTxt) {
        Map<String, String> mapForList;
        for (String value : stringsTxt) {
            mapForList = new HashMap<>();
            mapForList.put("text", value);
            mapForList.put("length", Integer.toString(value.length()));
            contentList.add(mapForList);
        }
        return contentList;
    }

    @NonNull
    private String[] prepareContent() {

        return sharedPref.getString(LARGE_TEXT, "").split("\n\n");
    }



    private void initSharedPreferences() {
        sharedPref =getSharedPreferences("MyText", MODE_PRIVATE);
        SharedPreferences.Editor myEditor = sharedPref.edit();
        myEditor.putString(LARGE_TEXT, getString(R.string.large_text));
        myEditor.apply();
    }

    public void onRefresh()  {
        swipeRefreshLayout = findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {


                swipeRefreshLayout.setOnRefreshListener(this);
                swipeRefreshLayout.setRefreshing(false);

                initList();
            }
        });
    }

}
