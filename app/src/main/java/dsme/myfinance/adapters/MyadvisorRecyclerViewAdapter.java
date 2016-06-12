package dsme.myfinance.adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;


import com.google.gson.Gson;

import java.util.List;

import dsme.myfinance.R;
import dsme.myfinance.activities.ActivityAdviserProfile;
import dsme.myfinance.activities.MainActivity;
import dsme.myfinance.models.Model;
import dsme.myfinance.models.User;

public class MyadvisorRecyclerViewAdapter extends RecyclerView.Adapter<MyadvisorRecyclerViewAdapter.ViewHolder> {

    private List<User.Adviser> mValues;

    public MyadvisorRecyclerViewAdapter(List items) {
        mValues = items;
    }

    public void setData(List advisers)
    {
        mValues = advisers;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_adviser, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mAdviserName.setText(mValues.get(position).getDisplayName());
        holder.mEmail.setText(mValues.get(position).getEmail());

        if(Model.instance().getCustomer().getAdviserId() != null &&
                Model.instance().getCustomer().getAdviserId().equals(holder.mItem.getId())){
            holder.isAssociated.setChecked(true);
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gson gson = new Gson();
                Intent intent = new Intent(v.getContext(), ActivityAdviserProfile.class);
                intent.putExtra(ActivityAdviserProfile.ADVISER_EXTRA, gson.toJson(holder.mItem));
                ((Activity)v.getContext()).startActivityForResult(intent, MainActivity.ACTIVITY_SUBSCRIBE);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(mValues == null){
            return 0;
        }else{
            return mValues.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public User.Adviser mItem;
        public final View mView;
        public final TextView mAdviserName;
        public final TextView mEmail;
        public CheckBox isAssociated;


        public ViewHolder(View view) {
            super(view);
            mView = view;
            mAdviserName = (TextView) view.findViewById(R.id.titleTextView);
            mEmail = (TextView) view.findViewById(R.id.subtitleTextView);
            isAssociated = (CheckBox)view.findViewById(R.id.checkbox_adviser);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mEmail.getText() + "'";
        }
    }
}
