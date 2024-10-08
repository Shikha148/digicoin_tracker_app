package com.example.digicointacker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private EditText searchEdt;
    private RecyclerView currenciesRV;
    private ProgressBar loadingPB;
    private ArrayList<CurrencyRVModel> currencyRVModelArrayList;
    private CurrencyRVAdapter currencyRVAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        searchEdt = findViewById(R.id.search);
        currenciesRV = findViewById(R.id.currencies);
        loadingPB = findViewById(R.id.loading);
        currencyRVModelArrayList = new ArrayList<>();
        currencyRVAdapter = new CurrencyRVAdapter(currencyRVModelArrayList,this);
        currenciesRV.setLayoutManager(new LinearLayoutManager(this));
        currenciesRV.setAdapter(currencyRVAdapter);
        getCurrencyData();
        searchEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                    filterCurrencies(s.toString());
            }
        });
    }
    private void filterCurrencies(String currency)
    {
        ArrayList<CurrencyRVModel> filteredList = new ArrayList<>();
        for(CurrencyRVModel item : currencyRVModelArrayList)
        {
            if(item.getName().toLowerCase().contains(currency.toLowerCase()))
            {
                filteredList.add(item);
            }
        }
        if(filteredList.isEmpty())
        {
            Toast.makeText(MainActivity.this,"No currency found for searched value",Toast.LENGTH_SHORT).show();
        }
        else
        {
            currencyRVAdapter.filterList(filteredList);
        }
    }
    private void getCurrencyData()
    {
        loadingPB.setVisibility(View.VISIBLE);
        String url = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                    loadingPB.setVisibility(View.GONE);
                    try {
                        JSONArray dataArray = response.getJSONArray("data");
                        for(int i=0;i<dataArray.length();i++)
                        {
                            JSONObject dataObj = dataArray.getJSONObject(i);
                            String name = dataObj.getString("name");
                            String symbol = dataObj.getString("symbol");
                            JSONObject quote = dataObj.getJSONObject("quote");
                            JSONObject USD = quote.getJSONObject("USD");
                            double price = USD.getDouble("price");
                            currencyRVModelArrayList.add(new CurrencyRVModel(name,symbol,price));
                        }
                        currencyRVAdapter.notifyDataSetChanged();
                    }catch(JSONException e)
                    {
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this,"Fail extract json data",Toast.LENGTH_SHORT).show();
                    }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingPB.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this,"Fail to get the data",Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String,String> headers = new HashMap<>();
                headers.put("X-CMC_PRO_API_KEY","f7915393-fe47-4f54-8797-d0772a84fb01");
                return headers;
            }
        };
                requestQueue.add(jsonObjectRequest);
    }
}