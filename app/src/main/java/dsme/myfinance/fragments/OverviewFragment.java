package dsme.myfinance.fragments;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import dsme.myfinance.MainActivity;
import dsme.myfinance.R;
import dsme.myfinance.models.Expense;
import dsme.myfinance.models.Model;
import dsme.myfinance.models.ModelCloudDB;
import dsme.myfinance.utils.DateUtils;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class OverviewFragment extends Fragment{

    PieChart mPieChart;
    LineChart mLineChart;
    View mView;
    ArrayList<Entry> entries1;
    ArrayList<String> xVals;
    List<String> categories;
    ArrayList<Entry> e1;
    ArrayList<String> months;
    boolean shouldUpdateDataSets = false;
    TextView monthSum;

    public OverviewFragment() {
    }

    public static OverviewFragment newInstance(String param1, String param2) {
        OverviewFragment fragment = new OverviewFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_overview, container, false);

        TextView monthName = (TextView) mView.findViewById(R.id.overviewCurrentMonthName);
        monthSum = (TextView) mView.findViewById(R.id.overviewCurrentMonthSum);

        monthName.setText(DateUtils.getMonthName(GregorianCalendar.getInstance().get(Calendar.MONTH)+1));
        monthSum.setText(Float.toString(Model.instance().getSumByMonth(DateUtils.getStartOfMonth(),0))+ "$");

        createPieChart();

        mPieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
                Toast.makeText(getActivity().getBaseContext(), Float.toString(e.getVal()) + "$", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected() {

            }
        });

//        new ModelCloudDB().new
//                GetAllData(){
//            @Override
//            protected void onPostExecute(String result){
//                int temp = 1;
//            }
//        }.execute();

        mLineChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
                int dif = 12 - e.getXIndex(); // there are 13 entries in the linechart
                int selectedMonth, currMonth = Calendar.getInstance().get(Calendar.MONTH);
                int currYear = Calendar.getInstance().get(Calendar.YEAR);
                if(dif > currMonth){
                    currYear--;
                    selectedMonth = 12 - Math.abs(dif - currMonth);
                }else{
                    selectedMonth = currMonth - dif;
                }

                Fragment frag = ExpenseListFragment.newInstance(selectedMonth, currYear);
                openFragment(frag);
            }

            @Override
            public void onNothingSelected() {

            }
        });

        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();

        if(shouldUpdateDataSets) {
            refreshCharts();
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        shouldUpdateDataSets = true;
    }

    public void createPieChart(){
        mPieChart = (PieChart) mView.findViewById(R.id.pieChart);
        mPieChart.setDescription("");

        mLineChart = (LineChart) mView.findViewById(R.id.lineChart);
        mLineChart.setDescription("");

        mLineChart.setData(generateDataLine());
        mLineChart.getAxisRight().setEnabled(false);
        mLineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        Legend l1 = mLineChart.getLegend();
        l1.setEnabled(false);

//        Typeface tf = Typeface.createFromAsset(getAssets(), "OpenSans-Light.ttf");
//
//        mPieChart.setCenterTextTypeface(tf);
//        mPieChart.setCenterText(generateCenterText());
//        mPieChart.setCenterTextSize(10f);
//        mPieChart.setCenterTextTypeface(tf);
        // radius of the center hole in percent of maximum radius
        mPieChart.setHoleRadius(3f);
        mPieChart.setTransparentCircleRadius(0f);

        Legend l = mPieChart.getLegend();
        l.setEnabled(false);
        //mPieChart.value
        //l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);

        mPieChart.setData(generatePieData());

    }

    protected PieData generatePieData() {
        entries1 = new ArrayList<>();
        xVals = new ArrayList<String>();

        categories = Model.instance().getCategories();

        generatePieDataSet();

        PieDataSet ds1 = new PieDataSet(entries1, "Expenses");
        ds1.setColors(ColorTemplate.VORDIPLOM_COLORS);
        ds1.setSliceSpace(2f);
        ds1.setValueTextColor(Color.BLACK);
        ds1.setDrawValues(false);
        ds1.setValueTextSize(12f);


        PieData d = new PieData(xVals, ds1);
        //d.setValueTypeface(tf);

        return d;
    }

    private LineData generateDataLine() {
        e1 = new ArrayList<>();
        months = new ArrayList<>();

        generateLineDataSet();

        LineDataSet d1 = new LineDataSet(e1, "New DataSet " + ", (1)");
        d1.setLineWidth(2.5f);
        d1.setCircleRadius(4.5f);
        d1.setHighLightColor(Color.rgb(244, 117, 117));
        d1.setDrawValues(false);

        LineData cd = new LineData(months, d1);
        return cd;
    }

    public void generatePieDataSet(){
        xVals.clear();
        entries1.clear();
        categories = Model.instance().getCategories();
        long startOfMonth = DateUtils.getStartOfMonth();

        for(int i = 0; i < categories.size(); i++) {
            xVals.add(categories.get(i));
            float sum = Model.instance().getSumByCategory(categories.get(i), startOfMonth,0);
            if (sum > 0) {
                entries1.add(new Entry(sum, i));
            }
        }
    }

    public void generateLineDataSet(){
        int currentMonth = GregorianCalendar.getInstance().get(Calendar.MONTH);
        int currentYear = GregorianCalendar.getInstance().get(Calendar.YEAR);
        long[] monthRange;
        months.clear();
        e1.clear();

        for (int i = currentMonth; i <= 12 + currentMonth; i++) {
            if(i < 12){
                months.add(DateUtils.getMonthNameShort(i+1));
                monthRange = DateUtils.getMonthDateRange(i, currentYear - 1);
                e1.add(new Entry(Model.instance().getSumByMonth(monthRange[0], monthRange[1]), i-currentMonth));
            }else{
                months.add(DateUtils.getMonthNameShort(i - 12 + 1));
                monthRange = DateUtils.getMonthDateRange(i - 12, currentYear);
                e1.add(new Entry(Model.instance().getSumByMonth(monthRange[0], monthRange[1]), i-currentMonth));
            }
        }
    }

    private void openFragment(final Fragment fragment){
                getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

    private void refreshCharts(){
        generatePieDataSet();
        mPieChart.getData().notifyDataChanged();
        mPieChart.notifyDataSetChanged();
        mPieChart.invalidate();
        //mPieChart.animateY(1200);
        shouldUpdateDataSets = false;
        monthSum.setText(Float.toString(Model.instance().getSumByMonth(DateUtils.getStartOfMonth(),0))+ "$");
        generateLineDataSet();
        mLineChart.getData().notifyDataChanged();
        mLineChart.notifyDataSetChanged();
        mLineChart.invalidate();
    }

}
