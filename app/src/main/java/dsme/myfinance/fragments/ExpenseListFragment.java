package dsme.myfinance.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.Calendar;
import java.util.List;

import dsme.myfinance.R;
import dsme.myfinance.adapters.MyexpenseRecyclerViewAdapter;
import dsme.myfinance.models.Expense;
import dsme.myfinance.models.Model;
import dsme.myfinance.utils.DateUtils;

public class ExpenseListFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String CURRENT_MONTH = "month";
    private static final String CURRENT_YEAR = "year";
    // TODO: Customize parameters
    private int month = -1;
    private int year = 0;
    RecyclerView recyclerView;
    View mView;


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

        setHasOptionsMenu(true);

        if (getArguments() != null) {
            month = getArguments().getInt(CURRENT_MONTH);
            year = getArguments().getInt(CURRENT_YEAR);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_expense_list, container, false);


        // Set the adapter
        if (mView instanceof RecyclerView) {
            Context context = mView.getContext();
            recyclerView = (RecyclerView) mView;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));

            List<Expense> items;

            if(month != -1) {
                long[] currMonth = DateUtils.getMonthDateRange(month, year);
                items = Model.instance().getExpensesByMonth(currMonth[0], currMonth[1]);
            }else{
                long[] currMonth = DateUtils.getMonthDateRange(Calendar.getInstance().get(Calendar.MONTH),Calendar.getInstance().get(Calendar.YEAR));
                items = Model.instance().getExpensesByMonth(currMonth[0], currMonth[1]);
            }
            recyclerView.setAdapter(new MyexpenseRecyclerViewAdapter(items)); //, mListener));
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

        List<Expense> items = Model.instance().getExpenses();
        ((MyexpenseRecyclerViewAdapter) recyclerView.getAdapter()).setData(items);
        recyclerView.getAdapter().notifyDataSetChanged();

    }
}
