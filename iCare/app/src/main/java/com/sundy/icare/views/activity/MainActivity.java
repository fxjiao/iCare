package com.sundy.icare.views.activity;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.androidquery.AQuery;
import com.easemob.EMCallBack;
import com.easemob.EMConnectionListener;
import com.easemob.EMError;
import com.easemob.chat.EMChatManager;
import com.easemob.util.NetUtils;
import com.sundy.huanxin.receiver.AckMessageBroadcastReceiver;
import com.sundy.huanxin.receiver.NewMessageBroadcastReceiver;
import com.sundy.icare.R;
import com.sundy.icare.utils.MyToast;
import com.sundy.icare.utils.MyUtils;
import com.sundy.icare.views.fragment.family.MarketFragment;
import com.sundy.icare.views.fragment.family.MeFragment;
import com.sundy.icare.views.fragment.family.MsgFragment;
import com.sundy.icare.views.fragment.family.ServiceFragment;
import com.sundy.icare.views.fragment.family.TabMenuFragment;

/**
 * Created by sundy on 15/12/6.
 */
public class MainActivity extends BaseActivity implements View.OnClickListener {

    private final String TAG = "MainActivity";
    private Fragment mContent;
    private TabMenuFragment frameMenu;
    private ViewPager pager;
    private FragmentPagerAdapter pagerAdapter;
    private int current_Position = 0;
    private boolean is_Ack_flag = true; //如果用到已读的回执需要把这个flag设置成true

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MyUtils.rtLog(TAG, "------------->onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        aq = new AQuery(this);

        //初始化环信方法
        initHuanXin();

        //初始化View
        initBottomMenu();
        initViewPager();
    }

    private void initHuanXin() {
        EMChatManager.getInstance().addConnectionListener(new MyConnectionListener());

        //注册接收新消息的监听广播
        registerNewMessageBroadcastReceiver();
        //注册接收ack回执消息的监听广播
        registerAckMessageBroadcastReceiver();

    }

    //注册接收新消息的监听广播
    private void registerNewMessageBroadcastReceiver() {
        //只有注册了广播才能接收到新消息，目前离线消息，在线消息都是走接收消息的广播（离线消息目前无法监听，在登录以后，接收消息广播会执行一次拿到所有的离线消息）
        NewMessageBroadcastReceiver msgReceiver = new NewMessageBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter(EMChatManager.getInstance().getNewMessageBroadcastAction());
        intentFilter.setPriority(3);
        registerReceiver(msgReceiver, intentFilter);
    }

    //注册接收ack回执消息的监听广播
    private void registerAckMessageBroadcastReceiver() {
        //如果用到已读的回执需要把这个flag设置成true
        EMChatManager.getInstance().getChatOptions().setRequireAck(is_Ack_flag);
        //如果用到已读的回执需要把这个flag设置成true
        AckMessageBroadcastReceiver ackMessageReceiver = new AckMessageBroadcastReceiver();
        IntentFilter ackMessageIntentFilter = new IntentFilter(EMChatManager.getInstance().getAckMessageBroadcastAction());
        ackMessageIntentFilter.setPriority(3);
        registerReceiver(ackMessageReceiver, ackMessageIntentFilter);
    }

    private class MyConnectionListener implements EMConnectionListener {

        @Override
        public void onConnected() {
            //已连接到服务器
        }

        @Override
        public void onDisconnected(final int error) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (error == EMError.USER_REMOVED) {
                        MyUtils.rtLog(TAG, "---->显示帐号已经被移除");
                    } else if (error == EMError.CONNECTION_CONFLICT) {
                        MyUtils.rtLog(TAG, "---->显示帐号在其他设备登陆");
                        MyToast.rtToast(MainActivity.this, getString(R.string.login_at_another_device));
                        EMChatManager.getInstance().logout(new EMCallBack() {
                            @Override
                            public void onSuccess() {
                                //登出成功
                                //跳转到登陆页
                                goLogin();
                            }

                            @Override
                            public void onError(int i, String s) {
                                //登出异常
                                //跳转到登陆页
                                goLogin();
                            }

                            @Override
                            public void onProgress(int i, String s) {

                            }
                        });

                    } else {
                        if (NetUtils.hasNetwork(MainActivity.this)) {
                            MyUtils.rtLog(TAG, "---->连接不到聊天服务器");
                        } else {
                            MyUtils.rtLog(TAG, "---->当前网络不可用，请检查网络设置");
                        }
                    }
                }
            });
        }
    }

    //跳转登陆
    private void goLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void initBottomMenu() {
        frameMenu = (TabMenuFragment) getSupportFragmentManager().findFragmentById(R.id.frameMenu);
        current_Position = 0;
        frameMenu.setPosition(current_Position);
    }

    private void initViewPager() {
        pager = (ViewPager) aq.id(R.id.pager).getView();
        pagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public android.support.v4.app.Fragment getItem(int position) {
                android.support.v4.app.Fragment fragment = null;
                switch (position) {
                    case 0:
                        fragment = new MsgFragment();
                        break;
                    case 1:
                        fragment = new ServiceFragment();
                        break;
                    case 2:
                        fragment = new MarketFragment();
                        break;
                    case 3:
                        fragment = new MeFragment();
                        break;
                }
                return fragment;
            }

            @Override
            public int getCount() {
                return 4;
            }
        };

        pager.setAdapter(pagerAdapter);

        pager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                frameMenu.setPosition(position);
                current_Position = position;
                super.onPageSelected(position);
            }
        });
    }

    /**
     * 切换Fragment
     *
     * @param id
     */
    public void switchFragment(int id) {
        switch (id) {
            case R.id.btnMsg:
                current_Position = 0;
                break;
            case R.id.btnService:
                current_Position = 1;
                break;
            case R.id.btnMarket:
                current_Position = 2;
                break;
            case R.id.btnMe:
                current_Position = 3;
                break;
        }
        pager.setCurrentItem(current_Position);
    }

    /**
     * 手机返回键控制
     */
    @Override
    public void onBackPressed() {
        MyUtils.rtLog(TAG, "------------------->onBackPressed");
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        MyUtils.rtLog(TAG, "------------------->onDestroy");
        super.onDestroy();
        try {
            if (mContent != null)
                mContent = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

        }
    }

}

