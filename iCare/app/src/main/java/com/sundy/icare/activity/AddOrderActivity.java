package com.sundy.icare.activity;

import android.os.Bundle;
import android.view.View;

import com.androidquery.AQuery;
import com.sundy.icare.R;

/**
 * Created by sundy on 15/12/23.
 */
public class AddOrderActivity extends BaseActivity {

    private final String TAG = "AddOrderActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_order);

        aq = new AQuery(this);

        init();

    }

    private void init() {
        aq.id(R.id.txtTitle).text(R.string.add_order);
        aq.id(R.id.btnCancel).clicked(onClick);
        aq.id(R.id.rel_User).clicked(onClick);

    }

    private View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btnCancel:
                    finish();
                    break;
                case R.id.rel_User:
                    //选择服务对象
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}