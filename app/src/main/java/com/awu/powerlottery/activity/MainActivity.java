package com.awu.powerlottery.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.util.Log;

import com.awu.powerlottery.R;
import com.awu.powerlottery.app.ActivityCollector;
import com.awu.powerlottery.util.DataUtil;
import com.awu.powerlottery.util.LotteryType;
import com.awu.powerlottery.util.WebUtility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";

    private DisplayFragment displayFragment;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ListView menuListView;
    private SimpleAdapter listMenuAdapter;
    private List<Map<String,Object>> menuData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initMenuList();
    }

    private void initView(){
        displayFragment = new DisplayFragment();
        drawerLayout = (DrawerLayout)findViewById(R.id.layout_drawer);
        mDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,R.mipmap.ic_list_white,R.string.drawer_in,R.string.drawer_out){
            public void onDrawerClosed(View view){
                getActionBar().setIcon(R.mipmap.ic_menu_white);
            }

            public void onDrawerOpened(View drawerView){
                getActionBar().setIcon(R.mipmap.ic_arrow_back_white);
            }
        };

        drawerLayout.setDrawerListener(mDrawerToggle);
        getActionBar().setHomeButtonEnabled(true);
//        getActionBar().setIcon(R.mipmap.ic_menu_white);

        FragmentTransaction transaction =  getFragmentManager().beginTransaction();
        transaction.add(R.id.fragment_container, displayFragment);
        transaction.commit();
    }

    private void initMenuList(){
        menuListView = (ListView)findViewById(R.id.listview_menu);
        menuData = DataUtil.getMenuData();
        listMenuAdapter = new SimpleAdapter(this,menuData,R.layout.lottery_menu_item,new String[]{"img","title"},
                new int[]{R.id.imgview_menuimg,R.id.textview_menuname});
        menuListView.setAdapter(listMenuAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id){
            case R.id.action_exit:
                exitAlert();
                return true;
            case R.id.action_about:
                break;
            case android.R.id.home:
                if(!drawerLayout.isDrawerOpen(Gravity.LEFT)) {
                    Log.i(TAG, "onOptionsItemSelected home drawer open");
                    drawerLayout.openDrawer(Gravity.LEFT);
                }
                else {
                    Log.i(TAG, "onOptionsItemSelected home drawer close.");
                    drawerLayout.closeDrawer(Gravity.LEFT);
                }
                return true;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * alert dialog for click exit menu item.
     */
    private void exitAlert(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("确认要退出？")
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCollector.finishAll();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        builder.show();
    }
}
