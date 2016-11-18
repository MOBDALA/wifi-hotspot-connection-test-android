package com.example.mobdala.wifihotspotconnection.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.mobdala.wifihotspotconnection.R;
import com.example.mobdala.wifihotspotconnection.ui.ifaces.IListWifiPresenter;
import com.example.mobdala.wifihotspotconnection.ui.ifaces.IListWifiView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.mobdala.wifihotspotconnection.utils.Constants.LOG_TAG;

public class ListWifiViewActivity extends AppCompatActivity implements IListWifiView {

    /*
    Views
     */
    @BindView(R.id.listWifis)
    protected RecyclerView listWifis;
    @BindView(R.id.tvEmpty)
    protected TextView tvEmpty;
    private ProgressDialog loading = null;

    /*
    Aux
     */
    private ListWifiAdapter adapter = null;
    private IListWifiPresenter presenter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_wifi);
        ButterKnife.bind(this);

        presenter = new ListWifiPresenter(this);

        setupViews();
    }

    @Override
    protected void onResume() {
        super.onResume();

        presenter.getWifis();
    }

    // ---------------------------------------------------------------------------------------------

    // IListWifiView

    // ---------------------------------------------------------------------------------------------


    @Override
    public Context getContext() {
        return getContext();
    }

    @Override
    public void showLoading() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (loading == null) {
                    loading = new ProgressDialog(ListWifiViewActivity.this);
                    loading.setMessage("Loading...");
                    loading.setCancelable(false);
                    loading.setCanceledOnTouchOutside(false);
                }

                if (!loading.isShowing()) {
                    loading.show();
                }
            }
        });
    }

    @Override
    public void hideLoading() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (loading != null) {
                    loading.dismiss();
                }
            }
        });
    }

    @Override
    public void showList(final List<ScanResult> items) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvEmpty.setVisibility(View.GONE);
                listWifis.setVisibility(View.VISIBLE);
                adapter.updateItems(items);
            }
        });
    }

    @Override
    public void showEmpty() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvEmpty.setVisibility(View.VISIBLE);
                listWifis.setVisibility(View.GONE);
            }
        });
    }

    // ---------------------------------------------------------------------------------------------

    // Helper Methods

    // ---------------------------------------------------------------------------------------------

    private void setupViews() {

        adapter = new ListWifiAdapter(new ListWifiAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ScanResult item, int position) {
                Log.i(LOG_TAG, "position: " + position);
            }
        });

        listWifis.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        listWifis.setAdapter(adapter);
        listWifis.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }
}
