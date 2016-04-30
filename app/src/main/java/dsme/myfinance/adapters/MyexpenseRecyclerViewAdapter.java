package dsme.myfinance.adapters;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

//import dsme.myfinance.fragments.ExpenseListFragment.OnListFragmentInteractionListener;
import dsme.myfinance.MainActivity;
import dsme.myfinance.R;
import dsme.myfinance.TransactionEditActivity;
import dsme.myfinance.models.Expense;
import dsme.myfinance.utils.DateUtils;

import java.util.List;

public class MyexpenseRecyclerViewAdapter extends RecyclerView.Adapter<MyexpenseRecyclerViewAdapter.ViewHolder> {

    private List<Expense> mValues;
//    private final OnListFragmentInteractionListener mListener;
//
//    public MyexpenseRecyclerViewAdapter(List<Expense> items, OnListFragmentInteractionListener listener) {
//        mValues = items;
//        mListener = listener;
//    }

    public MyexpenseRecyclerViewAdapter(List<Expense> items) {
        mValues = items;
    }

    public void setData(List<Expense> items) {
        this.mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.expense_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mWeekDay.setText(DateUtils.getNameOfDay(holder.mItem.getDate()));
        holder.mDescription.setText(holder.mItem.getExpenseName());
        holder.mDay.setText(Integer.toString(DateUtils.getNumOfDayInMonth(holder.mItem.getDate())));
        holder.mAmount.setText(Float.toString(mValues.get(position).getExpenseAmount()));
        holder.mCategory.setText(holder.mItem.getCategory());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), TransactionEditActivity.class);
                intent.putExtra(MainActivity.EXPENSE_ID, holder.mItem.getTimestamp());
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public Expense mItem;

        public final TextView mWeekDay;
        public final TextView mDay;
        public final TextView mDescription;
        public final TextView mCategory;
        public final TextView mAmount;


        public ViewHolder(View view) {
            super(view);
            mView = view;
            mWeekDay = (TextView) mView.findViewById(R.id.weekdayTextView);
            mDay = (TextView) mView.findViewById(R.id.dayTextView);
            mDescription = (TextView) mView.findViewById(R.id.titleTextView);
            mCategory = (TextView) mView.findViewById(R.id.subtitleTextView);
            mAmount= (TextView) mView.findViewById(R.id.amountTextView);
        }

//        @Override
//        public String toString() {
//            return super.toString() + " '" + mContentView.getText() + "'";
//        }
    }
}
