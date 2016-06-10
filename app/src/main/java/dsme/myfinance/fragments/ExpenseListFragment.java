package dsme.myfinance.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;

import dsme.myfinance.R;
import dsme.myfinance.adapters.MyexpenseRecyclerViewAdapter;
import dsme.myfinance.models.Expense;
import dsme.myfinance.models.Model;
import dsme.myfinance.utils.DateUtils;

public class ExpenseListFragment extends Fragment {

    private static final String CURRENT_MONTH = "month";
    private static final String CURRENT_YEAR = "year";
    private int month = -1;
    private int year = 0;
    RecyclerView recyclerView;
    View mView;
    ImageButton buttonPreviousMonth;
    ImageButton buttonNextMonth;
    TextView monthShownTextView;


    public ExpenseListFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static ExpenseListFragment newInstance(int currMonth, int currYear) {
        ExpenseListFragment fragment = new ExpenseListFragment();
        Bundle args = new Bundle();
        args.putInt(CURRENT_MONTH, currMonth);
        args.putInt(CURRENT_YEAR, currYear);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            month = getArguments().getInt(CURRENT_MONTH);
            year = getArguments().getInt(CURRENT_YEAR);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_expense_list, container, false);
        View mRecyclerView = mView.findViewById(R.id.list);

        buttonPreviousMonth = (ImageButton) mView.findViewById(R.id.buttonPreviousMonth);
        buttonNextMonth = (ImageButton) mView.findViewById(R.id.buttonNextMonth);
        monthShownTextView = (TextView) mView.findViewById(R.id.textViewMonthShown);

        buttonPreviousMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(month >= 1) {
                    month--;
                }else{
                    month = 11;
                    year--;
                }
                if (buttonNextMonth.getVisibility() == View.INVISIBLE){
                    buttonNextMonth.setVisibility(View.VISIBLE);
                    buttonNextMonth.setEnabled(true);
                }
                monthShownTextView.setText(DateUtils.getMonthName(month + 1));
                reloadListDate();
            }
        });

        buttonNextMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(month == 11){
                    month = 0;
                    year++;
                }else{
                    month++;
                }

                if (month == Calendar.getInstance().get(Calendar.MONTH) && year == Calendar.getInstance().get(Calendar.YEAR)){
                    buttonNextMonth.setVisibility(View.INVISIBLE);
                    buttonNextMonth.setEnabled(false);
                }

                monthShownTextView.setText(DateUtils.getMonthName(month + 1));
                reloadListDate();
            }
        });
        // Set the adapter
        if (mRecyclerView instanceof RecyclerView) {
            Context context = mView.getContext();
            recyclerView = (RecyclerView) mRecyclerView;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));

            List<Expense> items;

            if(month != -1) {
                long[] dateRange = DateUtils.getMonthDateRange(month, year);
                items = Model.instance().getExpensesByMonth(dateRange[0], dateRange[1]);
                monthShownTextView.setText(DateUtils.getMonthName(month+ 1));
            }else{
                buttonNextMonth.setVisibility(View.INVISIBLE);
                buttonNextMonth.setEnabled(false);
                month = Calendar.getInstance().get(Calendar.MONTH);
                year = Calendar.getInstance().get(Calendar.YEAR);
                monthShownTextView.setText(DateUtils.getMonthName(month + 1));
                long[] dateRange = DateUtils.getMonthDateRange(month, year);
                items = Model.instance().getExpensesByMonth(dateRange[0], dateRange[1]);
            }
             int[] colors = context.getResources().getIntArray(R.array.preloaded_colors);
            recyclerView.setAdapter(new MyexpenseRecyclerViewAdapter(colors ,items)); //, mListener));
        }
        return mView;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //mListener = null;
    }

    @Override
    public void onResume(){
        super.onResume();

        reloadListDate();
    }

    private void reloadListDate(){
        if(month != -1) {
            long[] dateRange = DateUtils.getMonthDateRange(month, year);
            List<Expense> items = Model.instance().getExpensesByMonth(dateRange[0], dateRange[1]);
            ((MyexpenseRecyclerViewAdapter) recyclerView.getAdapter()).setData(items);
            recyclerView.getAdapter().notifyDataSetChanged();
        }
    }
}
