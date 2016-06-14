package dsme.myfinance.adapters;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import dsme.myfinance.R;
import dsme.myfinance.models.MessageLocal;

import java.util.List;

public class MymessageRecyclerViewAdapter extends RecyclerView.Adapter<MymessageRecyclerViewAdapter.ViewHolder> {

    private List<MessageLocal> mValues;
    private final int[] colors;
    private String customerId;

    public MymessageRecyclerViewAdapter(List<MessageLocal> items, int[] colors, String customerId) {
        mValues = items;
        this.colors = colors;
        this.customerId = customerId;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_message, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        GradientDrawable drawable = (GradientDrawable) holder.myProfilePicture.getDrawable();
        drawable.setColor(colors[0]);
        holder.mMessageContent.setText(mValues.get(position).getMessageContent());

        if (holder.mItem.getSenderId().equals(customerId)){
            if(holder.otherProfilePicture.getVisibility() == View.VISIBLE) {
                holder.otherProfilePicture.setVisibility(View.INVISIBLE);
                holder.mMessageContent.setGravity(Gravity.RIGHT | Gravity.CENTER);
                holder.mMessageContent.setPadding(0, 0, 28, 0);
            }
        }else {
            if(holder.myProfilePicture.getVisibility() == View.VISIBLE) {
                holder.myProfilePicture.setVisibility(View.INVISIBLE);
                holder.mMessageContent.setGravity(Gravity.LEFT | Gravity.CENTER);
                holder.mMessageContent.setPadding(28, 0, 0, 0);
                GradientDrawable drawableOther = (GradientDrawable) holder.otherProfilePicture.getDrawable();
                drawableOther.setColor(colors[1]);
            }
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mMessageContent;
        public final ImageView myProfilePicture;
        public final ImageView otherProfilePicture;
        public MessageLocal mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            myProfilePicture = (ImageView) view.findViewById(R.id.myProfileImage);
            otherProfilePicture = (ImageView) view.findViewById(R.id.otherProfileImage);
            mMessageContent = (TextView) view.findViewById(R.id.messageContentTextView);
        }
    }

    public void setData(List<MessageLocal> messages){
        this.mValues = messages;
    }
}
