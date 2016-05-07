package dsme.myfinance.adapters;

import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import dsme.myfinance.R;
import dsme.myfinance.models.Message;

import java.util.List;

public class MymessageRecyclerViewAdapter extends RecyclerView.Adapter<MymessageRecyclerViewAdapter.ViewHolder> {

    private List<Message> mValues;
    private final int[] colors;

    public MymessageRecyclerViewAdapter(List<Message> items, int[] colors) {
        mValues = items;
        this.colors = colors;
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
        public Message mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            myProfilePicture = (ImageView) view.findViewById(R.id.myProfileImage);
            otherProfilePicture = (ImageView) view.findViewById(R.id.otherProfileImage);
            mMessageContent = (TextView) view.findViewById(R.id.messageContentTextView);
        }
    }

    public void setData(List<Message> messages){
        this.mValues = messages;
    }
}
