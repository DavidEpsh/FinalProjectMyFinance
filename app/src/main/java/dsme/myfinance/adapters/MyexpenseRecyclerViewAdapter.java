package dsme.myfinance.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

//import dsme.myfinance.fragments.ExpenseListFragment.OnListFragmentInteractionListener;
import dsme.myfinance.R;
import dsme.myfinance.models.Expense;

import java.util.List;

public class MyexpenseRecyclerViewAdapter extends RecyclerView.Adapter<MyexpenseRecyclerViewAdapter.ViewHolder> {

    private final List<Expense> mValues;
//    private final OnListFragmentInteractionListener mListener;
//
//    public MyexpenseRecyclerViewAdapter(List<Expense> items, OnListFragmentInteractionListener listener) {
//        mValues = items;
//        mListener = listener;
//    }

    public MyexpenseRecyclerViewAdapter(List<Expense> items) {
        mValues = items;
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
        holder.mWeekDay.setText("wed");
        holder.mDescription.setText("testtest");
        holder.mDay.setText("1");
        holder.mAmount.setText(Float.toString(mValues.get(position).getExpenseAmount()));
        holder.mCategory.setText("test");

//        holder.mView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (null != mListener) {
//                    // Notify the active callbacks interface (the activity, if the
//                    // fragment is attached to one) that an item has been selected.
//                    //mListener.onListFragmentInteraction(holder.mItem);
//                }
//            }
//        });
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
