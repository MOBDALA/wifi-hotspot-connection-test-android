package com.example.mobdala.wifihotspotconnection.modules.wifi;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.mobdala.wifihotspotconnection.R;
import com.example.mobdala.wifihotspotconnection.modules.wifi.ifaces.IListWifiPresenter;
import com.example.mobdala.wifihotspotconnection.modules.wifi.ifaces.IListWifiView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListWifiViewActivity extends AppCompatActivity implements IListWifiView {

    private static final int REQUEST_ENABLE_WIFI = 10;

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

    @Override
    protected void onPause() {
        presenter.unregisterReceiver();
        super.onPause();
    }

    // ---------------------------------------------------------------------------------------------

    // IListWifiView

    // ---------------------------------------------------------------------------------------------


    @Override
    public Activity getContextActivity() {
        return this;
    }

    @Override
    public void showLoading() {

        if (loading == null) {
            loading = new ProgressDialog(ListWifiViewActivity.this);
            loading.setMessage("Loading...");
            loading.setCancelable(false);
            loading.setCanceledOnTouchOutside(true);
        }

        if (!loading.isShowing()) {
            loading.show();
        }
    }

    @Override
    public void hideLoading() {

        if (loading != null) {
            loading.dismiss();
        }
    }

    @Override
    public void showList(final List<ScanResult> items) {
        tvEmpty.setVisibility(View.GONE);
        listWifis.setVisibility(View.VISIBLE);
        adapter.updateItems(items);
    }

    @Override
    public void showEmpty() {

        tvEmpty.setVisibility(View.VISIBLE);
        listWifis.setVisibility(View.GONE);
    }

    @Override
    public void showWifiDisabledDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
        builder.setTitle(getString(R.string.wifi_disabled_title));
        builder.setMessage(getString(R.string.wifi_disabled_message));
        builder.setCancelable(false);

        String positiveText = getString(R.string.settings);
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                        startActivity(intent);
                    }
                });

        String negativeText = getString(R.string.cancel);
        builder.setNegativeButton(negativeText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    // ---------------------------------------------------------------------------------------------

    // Helper Methods

    // ---------------------------------------------------------------------------------------------

    private void setupViews() {

        adapter = new ListWifiAdapter(new ListWifiAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ScanResult item, int position) {
                presenter.connectToWifi(item);
            }
        });

        listWifis.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        listWifis.setAdapter(adapter);
        listWifis.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }
}
