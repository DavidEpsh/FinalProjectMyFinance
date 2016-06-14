package dsme.myfinance.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import dsme.myfinance.activities.MainActivity;
import dsme.myfinance.adapters.MymessageRecyclerViewAdapter;
import dsme.myfinance.models.Message;
import dsme.myfinance.models.MessageLocal;
import dsme.myfinance.models.Model;
import dsme.myfinance.models.ModelCloudDB;

public class ChatFragment extends Fragment {

    View mView;
    RecyclerView mRecyclerView;
    ImageButton buttonSend;
    EditText newMessageText;
    String userId;
    String adviserId;
    String adviserName;
    private Handler handler = new Handler();

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {

            new ModelCloudDB(). new GetAllMessages() {
                @Override
                protected void onPostExecute(String result) {
                    if (result != null && result.equals("OK")) {
                        reloadChat();
                    }else{
                    }
                }
            }.execute();
            handler.postDelayed(this, 10000);
        }
    };

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
        userId = Model.instance().getCustomer().getId();
        adviserId = Model.instance().getCustomer().getAdviserId();
        adviserName = Model.instance().getCustomer().getAdviserName().split(" ")[0];

        handler.postDelayed(runnable, 100);

        if (mRecyclerView instanceof RecyclerView) {
            Context context = mRecyclerView.getContext();
            mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
            int[] colors = context.getResources().getIntArray(R.array.preload_colors_small);
            mRecyclerView.setAdapter(new MymessageRecyclerViewAdapter(Model.instance().getMessages(), colors, Model.instance().getCustomer().getId(), adviserName));

        }

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (newMessageText.getText().length() > 0) {
                    MessageLocal message = new MessageLocal(null, userId, adviserId, newMessageText.getText().toString(), null);
                    new ModelCloudDB(). new SendMessage() {
                        @Override
                        protected void onPostExecute(String result) {
                            if (result != null && result.equals("OK")) {
                                reloadChat();
                                newMessageText.getText().clear();
                            }else{

                            }
                        }
                    }.execute(message);
                }
            }
        });
        return mView;
    }

    @Override
    public void onResume(){
        super.onResume();
    }
    public void onPause() {
        super.onPause();

        handler.removeCallbacks(runnable);
    }

    private void reloadChat(){
            List<MessageLocal> items = Model.instance().getMessages();
            ((MymessageRecyclerViewAdapter) mRecyclerView.getAdapter()).setData(items);
            mRecyclerView.getAdapter().notifyDataSetChanged();
    }
}
