package com.birdex.bird.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.birdex.bird.MyApplication;
import com.birdex.bird.R;
import com.birdex.bird.activity.AboutActivity;
import com.birdex.bird.activity.LoginActivity;
import com.birdex.bird.activity.MyAccountInfoActivity;
import com.birdex.bird.adapter.MineIndexAdapter;
import com.birdex.bird.entity.MineEntity;
import com.birdex.bird.util.decoration.FullyLinearLayoutManager;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by chuming.zhuang on 2016/3/25.
 */
public class MineFragment extends BaseFragment implements View.OnClickListener{
    //设置列表
    @Bind(R.id.rv_mine_list)
    public RecyclerView rv_list;
    private MineIndexAdapter adapter=null;
    @Override
    protected void key(int keyCode, KeyEvent event) {

    }

    @Override
    public int getContentLayoutResId() {
        return R.layout.fragment_mine_layout;
    }

    @Override
    public void initializeContentViews() {
        ArrayList<MineEntity> list=new ArrayList<>();
        String [] titles= getActivity().getResources().getStringArray(R.array.mine_item_title);
        for(int i=0;i<titles.length;i++){
            MineEntity entity=new MineEntity();
            entity.setTitle(titles[i]);
            if(i==1||i==3||i==6){
                entity.setShowType(MineEntity.Type.Bottom);
            }else if(i==0||i==2||i==4){
                entity.setShowType(MineEntity.Type.Top);
            }else{
                entity.setShowType(MineEntity.Type.Middle);
            }
            list.add(entity);
        }
        adapter=new MineIndexAdapter(getActivity(),list);
        rv_list.setLayoutManager(new FullyLinearLayoutManager(getActivity()));
        rv_list.setAdapter(adapter);
    }

    @Override
    protected void lazyLoad() {

    }

    @OnClick({R.id.out_account})
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.out_account:
                // 清除掉登录的相关信息
                SharedPreferences sp = getActivity().getSharedPreferences("login", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();

                editor.putString("company_code", "");
                editor.putString("company_name", "");
                editor.putString("company_short_name", "");
                editor.putString("user_code", "");
                editor.putString("token", "");
                editor.commit();

                Intent intent = new Intent(getActivity(), LoginActivity.class);
                MyApplication.getInstans().clearActivities();
                startActivity(intent);
                break;
        }
    }
}
