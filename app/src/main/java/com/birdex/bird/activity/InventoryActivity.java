package com.birdex.bird.activity;

import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.birdex.bird.R;
import com.birdex.bird.adapter.CommonSimpleAdapter;
import com.birdex.bird.fragment.BaseFragment;
import com.birdex.bird.fragment.InventoryFragment;
import com.birdex.bird.interfaces.BackHandledInterface;
import com.birdex.bird.interfaces.OnRecyclerViewItemClickListener;
import com.birdex.bird.widget.HeaderView;

import org.simple.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.Bind;

/**
 * Created by huwei on 16/4/5.
 */
public class InventoryActivity extends BaseActivity implements BaseFragment.OnFragmentInteractionListener, BackHandledInterface, HeaderView.OnHeadViewClickLister {

    @Bind(R.id.hv_invenyory)
    public HeaderView hv_title;


    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void setSelectedFragment(BaseFragment selectedFragment) {
        this.baseFragment = selectedFragment;
    }


    //弹出框
    private PopupWindow mPopupWindow = null;
    private PopupWindow menuPop = null;

    @Override
    public int getContentLayoutResId() {
        return R.layout.inventory_all_layout;
    }

    private ArrayList<BaseFragment> fragments = null;
    //
    private FragmentManager manager = null;
    //
    private FragmentTransaction transaction = null;
    private BaseFragment baseFragment;//加入栈顶的fragment

    /**
     * 隐藏添加fragment
     */
    private void addFragment(int index) {
        BaseFragment fragment = fragments.get(index);
        transaction = getSupportFragmentManager().beginTransaction();
        hideFragment();
        if (!fragment.isAdded()) {
            transaction.add(R.id.ll_inventory_fragment, fragment);
        }
        transaction.show(fragment);
        transaction.commit();
    }

    //隐藏所有fragment
    private void hideFragment() {
        for (Fragment fragment : fragments) {
            transaction.hide(fragment);
        }
    }

    @Override
    public void initializeContentViews() {
        EventBus.getDefault().register(this);
        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();
        fragments = new ArrayList<>();
        InventoryFragment inventoryFragment = new InventoryFragment();
        fragments.add(inventoryFragment);
        addFragment(0);
        hv_title.setOnHeadViewClickLister(this);
    }


    @Override
    public void onLeftClick(View view) {
        finish();
    }

    @Override
    public void onRightClick(View view) {
        showMenuPopupWindow(view);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * 展示弹出框
     */
    public void showMenuPopupWindow(View viewID) {
//        LayoutInflater mLayoutInflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        final View popWindow = LayoutInflater.from(this).inflate(R.layout.common_recycleview_layout, null);
//        popWindow.setBackgroundColor(Color.TRANSPARENT);
        RecyclerView rcy = (RecyclerView) popWindow.findViewById(R.id.rcy);
        rcy.setLayoutManager(new LinearLayoutManager(this));
        ArrayList<String> list = new ArrayList<>();
        for (String s : getResources().getStringArray(R.array.manage_mode_items)) {
            list.add(s);
        }
        CommonSimpleAdapter adapter = new CommonSimpleAdapter(this, list);
        rcy.setAdapter(adapter);
        adapter.setOnRecyclerViewItemClickListener(new OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (menuPop.isShowing()) {
                    menuPop.dismiss();
                }
            }
        });
        int width = getWindowManager().getDefaultDisplay().getWidth();
        menuPop = new PopupWindow(popWindow, width / 2, LinearLayout.LayoutParams.WRAP_CONTENT);
        menuPop.setFocusable(true);
        menuPop.setOutsideTouchable(true);
        menuPop.setBackgroundDrawable(new BitmapDrawable());
        menuPop.update();
        menuPop.showAsDropDown(viewID, width / 2, 0);
    }

}
