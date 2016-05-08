package dsme.myfinance.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import java.util.Calendar;
import java.util.List;

import dsme.myfinance.R;
import dsme.myfinance.adapters.MymessageRecyclerViewAdapter;
import dsme.myfinance.models.Message;
import dsme.myfinance.models.Model;

public class ChatFragment extends Fragment {

    View mView;
    RecyclerView mRecyclerView;
    ImageButton buttonSend;
    EditText newMessageText;

    public ChatFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static ChatFragment newInstance(int columnCount) {
        ChatFragment fragment = new ChatFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_message_list, container, false);
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.chatList);
        buttonSend = (ImageButton) mView.findViewById(R.id.btSend);
        newMessageText = (EditText) mView.findViewById(R.id.etMessage);

        if (mRecyclerView instanceof RecyclerView) {
            Context context = mRecyclerView.getContext();
            mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
            int[] colors = context.getResources().getIntArray(R.array.preload_colors_small);
            mRecyclerView.setAdapter(new MymessageRecyclerViewAdapter(Model.instance().getMessages(), colors));
        }

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (newMessageText.getText().length() > 0) {
                    long timestamp = Calendar.getInstance().getTimeInMillis();
                    Model.instance().addMessage(new Message(timestamp, timestamp, newMessageText.getText().toString(), "me"));
                    newMessageText.setText("");
                    reloadChat();
                }
            }
        });
        return mView;
    }

    @Override
    public void onResume(){
        super.onResume();

        //reloadChat();
    }

    private void reloadChat(){
            List<Message> items = Model.instance().getMessages();
            ((MymessageRecyclerViewAdapter) mRecyclerView.getAdapter()).setData(items);
            mRecyclerView.getAdapter().notifyDataSetChanged();
    }
}
