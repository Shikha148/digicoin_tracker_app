package com.example.digicointacker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class CurrencyRVAdapter extends RecyclerView.Adapter<CurrencyRVAdapter.ViewHolder> {
    private ArrayList<CurrencyRVModel> currencyRVModelArrayList;
    private Context context;
    private static DecimalFormat df2 = new DecimalFormat("#.##");

    public CurrencyRVAdapter(ArrayList<CurrencyRVModel> currencyRVModelArrayList, Context context) {
        this.currencyRVModelArrayList = currencyRVModelArrayList;
        this.context = context;
    }

    public void filterList(ArrayList<CurrencyRVModel> filteredList)
    {
        currencyRVModelArrayList = filteredList;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public CurrencyRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.currency_rv_item,parent, false);
        return new  CurrencyRVAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CurrencyRVAdapter.ViewHolder holder, int position) {
        CurrencyRVModel currencyRVModel = currencyRVModelArrayList.get(position);
        holder.currencynameTV.setText(currencyRVModel.getName());
        holder.symbolTV.setText(currencyRVModel.getSymbol());
        holder.rateTV.setText("$"+df2.format(currencyRVModel.getPrice()));
    }

    @Override
    public int getItemCount() {
        return currencyRVModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView currencynameTV,symbolTV,rateTV;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            currencynameTV= itemView.findViewById(R.id.idTVCurrencyName);
            symbolTV= itemView.findViewById(R.id.idTVSymbol);
            rateTV=itemView.findViewById(R.id.idTVCurrencyRate);
        }
    }
}
