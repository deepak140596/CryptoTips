package com.localli.deepak.cryptotips.currencydetails.chartandtable;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.localli.deepak.cryptotips.DataBase.SharedPrefSimpleDB;
import com.localli.deepak.cryptotips.R;
import com.localli.deepak.cryptotips.alerts.AddAlertActivity;
import com.localli.deepak.cryptotips.formatters.PercentageFormatter;
import com.localli.deepak.cryptotips.formatters.PriceFormatter;
import com.localli.deepak.cryptotips.formatters.XAxisDayFormatter;
import com.localli.deepak.cryptotips.models.CoinItem;
import com.localli.deepak.cryptotips.models.MarketChart;
import com.localli.deepak.cryptotips.rest.CoinGeckoService;
import com.nex3z.togglebuttongroup.SingleSelectToggleGroup;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Deepak Prasad on 24-12-2018.
 */

public class GraphFragment extends Fragment {
    public static  final String CURRENCY_ID = "currency_id";
    public String TAG ="FRAG_GRAPH";

    public String TO_SYMBOL;

    CoinItem coinItem;
    MarketChart marketChartData;

    Context context;
    AppCompatActivity activity;

    TextView selectedPriceTv, selectedDateTv, change24hrPctTv, change24hrTv,
            high24hrTv, low24hrTv, mktCapTv, nameTv, rankTv,
            changeTDTv, highTDTv, lowTDTv;
    ProgressBar progressBar;
    SingleSelectToggleGroup toggleGroup;
    //GraphView graphView;
    LineChart lineChart;

    RequestQueue requestQueue;
    Gson gson;

    int DATE_FORMAT=1;

   // PointsGraphSeries<DataPoint> selectedSeries = new PointsGraphSeries<>();

    public static GraphFragment newInstance(CoinItem coinItem){
        GraphFragment graphFragment = new GraphFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(CURRENCY_ID,coinItem);
        graphFragment.setArguments(bundle);
        return graphFragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.context = getContext();
        this.activity = (AppCompatActivity)getActivity();

        //coinItem =(CoinItem) activity.getIntent().getSerializableExtra(CURRENCY_ID);

        coinItem = (CoinItem)this.getArguments().getSerializable(CURRENCY_ID);
        //Log.i(TAG,"COIN_ITEM: "+coinItem.getId());
        TO_SYMBOL = SharedPrefSimpleDB.getPreferredCurrency(context);

        return inflater.inflate(R.layout.fragment_graph,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);

        // get toolbar
       // Toolbar toolbar = view.findViewById(R.id.toolbar_fragment_graph);
        //activity.setSupportActionBar(toolbar);
        activity.setTitle("Price Chart");

        requestQueue = Volley.newRequestQueue(context);
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm: a");
        gson = gsonBuilder.create();


        intialiseViews(view);
        setUpCoinDetails();
        setUpToggleGroupListener();

        //fetchMarketData("usd","1");

    }

    public void intialiseViews(View view){
        selectedPriceTv = view.findViewById(R.id.frag_graph_selected_price_tv);
        selectedDateTv = view.findViewById(R.id.frag_graph_selected_day_tv);
        change24hrPctTv = view.findViewById(R.id.frag_graph_24hr_price_change_pct_tv);
        change24hrTv = view.findViewById(R.id.frag_graph_24hr_price_change_tv);
        mktCapTv = view.findViewById(R.id.frag_graph_market_cap_tv);
        high24hrTv = view.findViewById(R.id.frag_graph_24hr_high_tv);
        low24hrTv = view.findViewById(R.id.frag_graph_24hr_low_tv);
        nameTv = view.findViewById(R.id.frag_graph_name_tv);
        rankTv = view.findViewById(R.id.frag_graph_rank_tv);
        changeTDTv = view.findViewById(R.id.frag_graph_selected_tf_change_pct_value_tv);
        highTDTv = view.findViewById(R.id.frag_graph_selected_tf_high_value_tv);
        lowTDTv = view.findViewById(R.id.frag_graph_selected_tf_low_value_tv);

        toggleGroup = view.findViewById(R.id.frag_graph_toggle_group);
        progressBar = view.findViewById(R.id.frag_graph_loading_progress_bar);
        //graphView = view.findViewById(R.id.frag_graph_graphview);
        lineChart = view.findViewById(R.id.frag_graph_graph_view2);
    }

    public void setUpCoinDetails(){
        if(coinItem.getName() ==null)
            nameTv.setText("N/A");
        else
            nameTv.setText(coinItem.getName());

        if(coinItem.getMarketCapRank() == null)
            rankTv.setText("N/A");
        else
            rankTv.setText("#".concat(coinItem.getMarketCapRank().toString()));

        if(coinItem.getCurrentPrice() == null)
            selectedPriceTv.setText("N/A");
        else
            PriceFormatter.setPriceFormatTextView(context,selectedPriceTv,coinItem.getCurrentPrice());
        //currentPriceTv.setText(coinItem.getCurrentPrice()+"");

        if(coinItem.getPriceChange24h() == null)
            change24hrTv.setText("N/A");
        else
            PriceFormatter.setPriceFormatTextView(context,change24hrTv,coinItem.getPriceChange24h());
        //change24hrTv.setText(coinItem.getPriceChange24h()+"");

        if(coinItem.getPriceChangePercentage24h() == null)
            change24hrPctTv.setText("N/A");
        else
            PercentageFormatter.setPercentChangeTextView(context,change24hrPctTv,coinItem.getPriceChangePercentage24h());
        //change24hrPctTv.setText(coinItem.getPriceChangePercentage24h()+"");

        if(coinItem.getMarketCap() == null)
            mktCapTv.setText("N/A");
        else
            PriceFormatter.setPriceFormatTextView(context,mktCapTv,coinItem.getMarketCap());
        //mktCapTv.setText(coinItem.getMarketCap()+"");

        if(coinItem.getHigh24h() == null)
            high24hrTv.setText("N/A");
        else
            PriceFormatter.setPriceFormatTextView(context,high24hrTv,coinItem.getHigh24h());
        //high24hrTv.setText(coinItem.getHigh24h()+"");

        if(coinItem.getLow24h() == null)
            low24hrTv.setText("N/A");
        else
            PriceFormatter.setPriceFormatTextView(context,low24hrTv,coinItem.getLow24h());
        //low24hrTv.setText(coinItem.getLow24h()+"");
    }

    public void setUpToggleGroupListener(){
        toggleGroup.setOnCheckedChangeListener(new SingleSelectToggleGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SingleSelectToggleGroup group, int checkedId) {
                switch (checkedId){

                    case R.id.frag_graph_choice_24hr:
                        DATE_FORMAT = 1;
                        fetchMarketData(TO_SYMBOL,"1");
                        break;
                    case R.id.frag_graph_choice_7day:
                        DATE_FORMAT = 2;
                        fetchMarketData(TO_SYMBOL,"7");
                        break;
                    case R.id.frag_graph_choice_14day:
                        DATE_FORMAT = 2;
                        fetchMarketData(TO_SYMBOL,"14");
                        break;
                    case R.id.frag_graph_choice_1M:
                        DATE_FORMAT = 2;
                        fetchMarketData(TO_SYMBOL,"30");
                        break;
                    case R.id.frag_graph_choice_2M:
                        DATE_FORMAT = 2;
                        fetchMarketData(TO_SYMBOL,"60");
                        break;
                    case R.id.frag_graph_choice_3M:
                        DATE_FORMAT = 2;
                        fetchMarketData(TO_SYMBOL,"90");
                        break;
                    case R.id.frag_graph_choice_6M:
                        DATE_FORMAT = 2;
                        fetchMarketData(TO_SYMBOL,"180");
                        break;
                    case R.id.frag_graph_choice_1yr:
                        DATE_FORMAT = 2;
                        fetchMarketData(TO_SYMBOL,"365");
                        break;
                    case R.id.frag_graph_choice_all:
                        DATE_FORMAT = 2;
                        fetchMarketData(TO_SYMBOL,"max");
                        break;
                    default:
                        break;
                }
            }
        });
    }

    public void fetchMarketData(String toSymbol, String duration){

        String fetchUrl = String.format(CoinGeckoService.MARKET_DATA_URL,coinItem.getId(),toSymbol,duration);
        StringRequest request = new StringRequest(Request.Method.GET,fetchUrl, onMarketDataLoaded, onMarketDataError);
        requestQueue.add(request);
        progressBar.setVisibility(View.VISIBLE);
    }

    private final Response.Listener<String> onMarketDataLoaded = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {

            Log.i(TAG,"Success! Response: ");
            //progressBar.setVisibility(View.GONE);

            marketChartData = gson.fromJson(response,MarketChart.class);
            Log.i(TAG,"price: "+marketChartData.getPrices().get(0));

            //plotChartForPrice(marketChartData);
            plotChart2(marketChartData);

        }
    };

    private final Response.ErrorListener onMarketDataError = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.i(TAG,"Failed!");
        }
    };


    public void plotChart2(MarketChart marketChartData){
        double highTD=Integer.MIN_VALUE, lowTD = Integer.MAX_VALUE, changePctTD;
        List<Entry> entries = new ArrayList<Entry>();
        List<BarEntry> barEntries = new ArrayList<>();


        lineChart.invalidate();

        long prevTimeStamp = 0;

        for(List<Double> point : marketChartData.getPrices()) {
            //Log.i(TAG, "Point: " + point);
            Long time = Double.valueOf(point.get(0)).longValue();

            // check to see if timestamps are in increasing fashion
            if(time<= prevTimeStamp){
                Log.e(TAG,"Wrong timestamp. Curr: "+time+" Last: "+prevTimeStamp);
                //prevTimeStamp= time;
                continue;
            }
            prevTimeStamp= time;
            Timestamp timestamp = new Timestamp(time);
            Date d = new Date(timestamp.getTime());

            entries.add(new Entry(d.getTime(),point.get(1).floatValue()));

            // choose the max and min price during the selected time frame
            if(point.get(1) >= highTD){
                highTD=point.get(1);
            }else if(point.get(1) <= lowTD){
                lowTD = point.get(1);
            }

        }

        try{
            LineDataSet lineDataSet = new LineDataSet(entries,"Price");
            lineDataSet.setColor(Color.GREEN);
            //lineDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);


            LineData lineData = new LineData(lineDataSet);

            XAxis xAxis = lineChart.getXAxis();
            xAxis.setLabelCount(8,true);
            xAxis.setValueFormatter(new XAxisDayFormatter(DATE_FORMAT));
            lineChart.animateX(1000);
            lineChart.setData(lineData);

            // set on value selected listener
            lineChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                @Override
                public void onValueSelected(Entry e, Highlight h) {

                    long timestamp = Double.valueOf(e.getX()).longValue();
                    double price = e.getY();

                    String selectedDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(timestamp);
                    selectedDateTv.setText(selectedDate);
                    //selectedPriceTv.setText(price+"");
                    PriceFormatter.setPriceFormatTextView(context,selectedPriceTv,price);

                }

                @Override
                public void onNothingSelected() {

                }
            });

        }catch (Exception e){

        }

        // set the high , low and change pct for selected time frame

        if(highTD == Integer.MIN_VALUE)
            highTDTv.setText("N/A");
        else
            PriceFormatter.setPriceFormatTextView(context,highTDTv,highTD);


        if(lowTD == Integer.MAX_VALUE)
            lowTDTv.setText("N/A");
        else
            PriceFormatter.setPriceFormatTextView(context,lowTDTv,lowTD);


        double change = (entries.get(entries.size()-1).getY() - entries.get(0).getY());
        changePctTD = (change/(entries.get(0).getY()))*100.00;
        //PercentageFormatter.setPercentChangeTextView(context,changeTDTv,changePctTD);
        PercentageFormatter.setPriceAndPercentChangeTextViewTwoLines(context,changeTDTv,changePctTD,change);

        progressBar.setVisibility(View.GONE);

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_coin_details,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_add_alert:
                Log.i(TAG,"Add Alert clicked!");
                Intent intent = new Intent(getActivity(), AddAlertActivity.class);
                intent.putExtra(getString(R.string.coin_id),coinItem);
                startActivity(intent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
