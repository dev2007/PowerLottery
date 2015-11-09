package com.awu.powerlottery.activity;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.awu.powerlottery.R;
import com.awu.powerlottery.app.ActivityCollector;
import com.awu.powerlottery.util.DataUtil;

import java.util.List;
import java.util.Map;

public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";

    private DisplaySSQFragment displaySSQFragment;
    private Display3DFragment display3DFragment;
    private DipslayQLCFragment displayQLCFragment;
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
        displaySSQFragment = new DisplaySSQFragment();
        display3DFragment = new Display3DFragment();
        displayQLCFragment = new DipslayQLCFragment();

        drawerLayout = (DrawerLayout)findViewById(R.id.layout_drawer);
        mDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,R.mipmap.ic_list_white,R.string.drawer_in,R.string.drawer_out){
            public void onDrawerClosed(View view){
                getActionBar().setLogo(R.mipmap.ic_menu_white);
            }

            public void onDrawerOpened(View drawerView){
                getActionBar().setLogo(R.mipmap.ic_arrow_back_white);
            }
        };

        drawerLayout.setDrawerListener(mDrawerToggle);
        getActionBar().setHomeButtonEnabled(true);

        FragmentTransaction transaction =  getFragmentManager().beginTransaction();
        transaction.add(R.id.fragment_container, displaySSQFragment);
        transaction.commit();
    }

    private void initMenuList(){
        menuListView = (ListView)findViewById(R.id.listview_menu);
        menuData = DataUtil.getMenuData();
        listMenuAdapter = new SimpleAdapter(this,menuData,R.layout.lottery_menu_item,new String[]{"img","title"},
                new int[]{R.id.imgview_menuimg,R.id.textview_menuname});
        menuListView.setAdapter(listMenuAdapter);
        menuListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                Map<String, Object> rowData = menuData.get(position);
                switch ((int) rowData.get("id")) {
                    case R.string.lottery_name_ssq:
                        ft.replace(R.id.fragment_container, displaySSQFragment);
                        break;
                    case R.string.lottery_name_3d:
                        ft.replace(R.id.fragment_container,display3DFragment);
                        break;
                    case R.string.lottery_name_qlc:
                        ft.replace(R.id.fragment_container,displayQLCFragment);
                        break;
                    case R.string.lottery_name_dlt:
                        break;
                    case R.string.lottery_name_pl:
                        break;
                    case R.string.lottery_name_qxc:
                        break;
                    default:
                        break;
                }
                ft.commit();

                drawerLayout.closeDrawer(Gravity.LEFT);
            }
        });
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
                    drawerLayout.openDrawer(Gravity.LEFT);
                }
                else {
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
