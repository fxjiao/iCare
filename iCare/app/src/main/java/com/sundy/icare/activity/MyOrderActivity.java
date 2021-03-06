package com.sundy.icare.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.androidquery.AQuery;
import com.sundy.icare.R;
import com.sundy.icare.adapters.MyOrderListAdapter;

/**
 * Created by sundy on 15/12/24.
 */
public class MyOrderActivity extends BaseActivity {

    private final String TAG = "MyOrderActivity";
    private ListView lv_Data;
    private MyOrderListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_order);

        aq = new AQuery(this);
        init();

    }

    private void init() {
        aq.id(R.id.txtTitle).text(R.string.my_order);
        aq.id(R.id.btnBack).clicked(onClick);
        aq.id(R.id.btnRight).clicked(onClick);

        lv_Data = aq.id(R.id.lv_Data).getListView();
        adapter = new MyOrderListAdapter(this);
        lv_Data.setAdapter(adapter);

        lv_Data.setOnItemClickListener(onItemClickListener);

    }

    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            if (i == 2) {
                goChooseServantList(); //跳转至选择服务者列表
            } else {
                goOrderDetail(); //跳转至订单详情页
            }
        }
    };

    private void goChooseServantList() {
        Intent intent = new Intent(this, ChooseServantActivity.class);
        startActivity(intent);
    }

    private void goOrderDetail() {
        Intent intent = new Intent(this, MyOrderDetailActivity.class);
        startActivity(intent);
    }

    private View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btnBack:
                    finish();
                    break;
                case R.id.btnRight:
                    goAddOrder();
                    break;
            }
        }
    };

    private void goAddOrder() {
        Intent intent = new Intent(this, AddOrderActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
