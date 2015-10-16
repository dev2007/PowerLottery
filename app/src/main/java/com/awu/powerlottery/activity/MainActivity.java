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

import com.awu.powerlottery.R;
import com.awu.powerlottery.app.ActivityCollector;
import com.awu.powerlottery.util.LotteryType;
import com.awu.powerlottery.util.WebUtility;

public class MainActivity extends BaseActivity {
    private DisplayFragment displayFragment;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ListView menuListView;
    private ArrayAdapter<String> listAdapter;

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
        mDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,R.mipmap.ic_launcher,R.string.drawer_in,R.string.drawer_out){
            public void onDrawerClosed(View view){

            }

            public void onDrawerOpened(View drawerView){

            }
        };

        drawerLayout.setDrawerListener(mDrawerToggle);

        FragmentTransaction transaction =  getFragmentManager().beginTransaction();
        transaction.add(R.id.fragment_container, displayFragment);
        transaction.commit();
    }

    private void initMenuList(){
        menuListView = (ListView)findViewById(R.id.listview_menu);
        String[] data = getResources().getStringArray(R.array.lotterytype);
        listAdapter = new ArrayAdapter<String>(this,R.layout.lottery_menu_item,R.id.textview_menuname,data);
        menuListView.setAdapter(listAdapter);

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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_exit) {
            exitAlert();
            return true;
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
