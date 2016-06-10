package dsme.myfinance.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONObject;

import java.util.List;

import dsme.myfinance.R;
import dsme.myfinance.adapters.MyadvisorRecyclerViewAdapter;
import dsme.myfinance.models.ModelCloudDB;
import dsme.myfinance.models.User;

public class adviserListFragment extends Fragment {

    List<User.Adviser> advisers;
    ProgressDialog progressDialog;
    RecyclerView recyclerView;

    public adviserListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_advisor_list, container, false);
        progressDialog = new ProgressDialog(view.getContext(), R.style.AppTheme_Dark_Dialog);

        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Finding Advisers...");
        progressDialog.show();

        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(new MyadvisorRecyclerViewAdapter(advisers));
        }

        new ModelCloudDB(). new GetAdvisers() {
            @Override
            protected void onPostExecute(List<User.Adviser> allAdvisers) {
                if (allAdvisers != null) {
                    advisers = allAdvisers;
                    refreshList();
                }
            }
        }.execute();


        return view;
    }

    public void refreshList(){
        ((MyadvisorRecyclerViewAdapter)recyclerView.getAdapter()).setData(advisers);
        recyclerView.getAdapter().notifyDataSetChanged();
        progressDialog.dismiss();
    }
}
