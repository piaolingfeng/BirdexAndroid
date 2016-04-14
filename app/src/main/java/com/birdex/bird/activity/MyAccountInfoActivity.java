package com.birdex.bird.activity;

import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.birdex.bird.R;
import com.birdex.bird.fragment.AccountManagerFragment;
import com.birdex.bird.fragment.BaseFragment;
import com.birdex.bird.fragment.BillDetailFragment;
import com.birdex.bird.fragment.RechargeFragment;
import com.birdex.bird.interfaces.BackHandledInterface;

import java.util.ArrayList;

import butterknife.Bind;

/**
 * Created by huwei on 16/3/25.
 */
public class MyAccountInfoActivity extends BaseActivity implements BaseFragment.OnFragmentInteractionListener,BackHandledInterface,View.OnClickListener{
    private ArrayList<BaseFragment> fragments=null;
    //
    private FragmentManager manager=null;
    //
    private FragmentTransaction transaction=null;

    @Bind(R.id.rb_myaccount_recharge)
    public TextView tv_recharge;
    @Bind(R.id.rb_myaccount_bill)
    public TextView tv_bill;
    @Bind(R.id.rb_myaccount_manage)
    public TextView tv_manage;
    @Bind(R.id.tv_myaccount_title)
    public TextView tv_title;
    @Bind(R.id.iv_myaccount_back)
    public ImageView iv_back;
    //设置选中的index
    private int selectIndex=-1;
    //
    private RechargeFragment rechargefragment=null;
    private BillDetailFragment billfragment=null;
    private AccountManagerFragment managefragment=null;
    @Override
    public int getContentLayoutResId() {
        return R.layout.myaccount_info_layout;
    }
    private BaseFragment baseFragment;//加入栈顶的fragment
    //获取进入显示的第几页
    private int enterIndex=0;
    @Override
    public void initializeContentViews() {
        //获取列表
        enterIndex=getIntent().getIntExtra("enterindex",0);
//        initSystemBar(R.color.blue_head_1);
        manager=getSupportFragmentManager();
        transaction=manager.beginTransaction();
        fragments=new ArrayList<>();
        rechargefragment=new RechargeFragment();
        fragments.add(rechargefragment);
        billfragment=new BillDetailFragment();
        fragments.add(billfragment);
        managefragment=new AccountManagerFragment();
        fragments.add(managefragment);
        //判断索引显示不同的页面
        switch (enterIndex){
            case 0:
                clickChangeFragment(R.id.rb_myaccount_recharge);
                break;
            case 1:
                clickChangeFragment(R.id.rb_myaccount_bill);
                break;
            case 2:
                clickChangeFragment(R.id.rb_myaccount_manage);
                break;
            default:
                clickChangeFragment(R.id.rb_myaccount_recharge);
                break;
        }
        //设置点击事件
        tv_recharge.setOnClickListener(this);
        tv_bill.setOnClickListener(this);
        tv_manage.setOnClickListener(this);
        //回退
        iv_back.setOnClickListener(this);
    }
    /*
     *
     */
    private void changeFragment(int index,String tag){
        if(index==selectIndex){
            return;
        }
        selectIndex=index;
        FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
        //设置明细列表
        if(index<fragments.size()&&index>=0){
            transaction.replace(R.id.fragment_myaccount,fragments.get(index),tag);
        }else{
            transaction.replace(R.id.fragment_myaccount,fragments.get(0),tag);
        }
        transaction.commit();
    }
    private void changeFragment(int index){
        if(index==selectIndex){
            return;
        }
        selectIndex=index;
        FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
        //设置明细列表
        if(index<fragments.size()&&index>=0){
            transaction.replace(R.id.fragment_myaccount,fragments.get(index));
        }else{
            transaction.replace(R.id.fragment_myaccount,fragments.get(0));
        }
        transaction.commit();
    }
    @Override
    public void onFragmentInteraction(Uri uri) {
        return ;
    }



    private void httpRequest() {

    }


    @Override
    public void onClick(View v) {
        tv_recharge.setTextAppearance(this,R.style.myaccount_style_unselect);
        tv_bill.setTextAppearance(this,R.style.myaccount_style_unselect);
        tv_manage.setTextAppearance(this,R.style.myaccount_style_unselect);
        switch (v.getId()){
            case R.id.rb_myaccount_recharge:
                clickChangeFragment(v.getId());
                break;
            case R.id.rb_myaccount_bill:
                clickChangeFragment(v.getId());
                break;
            case R.id.rb_myaccount_manage:
                clickChangeFragment(v.getId());
                break;
            case R.id.iv_myaccount_back:
                finish();
                break;
        }
    }
    private void clickChangeFragment(int id){
        switch (id){
            case R.id.rb_myaccount_recharge:
                tv_recharge.setTextAppearance(this, R.style.myaccount_style_select);
//                changeFragment(0);
                addFragment(0);
                tv_title.setText(R.string.myaccount_title_1);
                break;
            case R.id.rb_myaccount_bill:
                tv_bill.setTextAppearance(this, R.style.myaccount_style_select);
//                changeFragment(1);
                addFragment(1);
                tv_title.setText(R.string.myaccount_title_2);
                break;
            case R.id.rb_myaccount_manage:
                tv_manage.setTextAppearance(this,R.style.myaccount_style_select);
                tv_title.setText(R.string.myaccount_title_3);
                addFragment(2);
                break;
        }
    }
    /**
     * 隐藏添加fragment
     * */
    private void addFragment(int index) {
        BaseFragment fragment=fragments.get(index);
        transaction=getSupportFragmentManager().beginTransaction();
        hideFragment();
        if (!fragment.isAdded()){
            transaction.add(R.id.fragment_myaccount, fragment);
        }
        transaction.show(fragment);
        transaction.commit();
    }
    //隐藏所有fragment
    private void hideFragment() {
        for(Fragment fragment:fragments){
            transaction.hide(fragment);
        }
    }
    @Override
    public void setSelectedFragment(BaseFragment selectedFragment) {
        this.baseFragment = selectedFragment;
    }
}
