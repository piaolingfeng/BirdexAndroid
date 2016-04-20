package com.birdex.bird.activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.birdex.bird.MyApplication;
import com.birdex.bird.R;
import com.birdex.bird.api.BirdApi;
import com.birdex.bird.entity.Area;
import com.birdex.bird.entity.City;
import com.birdex.bird.entity.ContactDetail;
import com.birdex.bird.greendao.DaoUtils;
import com.birdex.bird.greendao.city;
import com.birdex.bird.greendao.cityDao;
import com.birdex.bird.util.JsonHelper;
import com.birdex.bird.util.T;
import com.birdex.bird.widget.WheelView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.simple.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import de.greenrobot.dao.query.QueryBuilder;

/**
 * Created by hyj on 2016/4/5.
 */
public class ChangeAdressActivity extends BaseActivity implements View.OnClickListener {

    // 省份
    @Bind(R.id.select_view)
    WheelView select_view;
    // 城市
    @Bind(R.id.select_view2)
    WheelView select_view2;
    // 地区
    @Bind(R.id.select_view3)
    WheelView select_view3;

    // 所在地区
    @Bind(R.id.region)
    TextView region;

    // 收货人
    @Bind(R.id.consignee)
    EditText consignee;

    // 联系电话
    @Bind(R.id.phone)
    EditText phone;

    // 详细地址
    @Bind(R.id.detail_adress)
    EditText detail_adress;

    // 右上角文字
    @Bind(R.id.save)
    TextView save;

    // 地区选择控件
    @Bind(R.id.wheelview_ll)
    LinearLayout wheelview_ll;


    private cityDao cityDao;

    // 标记，如果设置过地区信息就可以点击  否则点击无效
    private boolean setRegionFlag = false;

    @Override
    public int getContentLayoutResId() {
        return R.layout.changeadress;
    }

    @Override
    public void initializeContentViews() {
        initData();
    }

    // 初始化数据
    private void initData() {
        // 获取省份列表
        setData(DaoUtils.getcity());

        save.setText(getString(R.string.change_adress));
    }

    private List<city> provinces = new ArrayList<city>();
    private List<city> cities = new ArrayList<city>();
    private List<city> areas = new ArrayList<city>();

    // 记录被选中的实体
    private city selectP;
    private city selectC;
    private city selectA;

    // 存在 显示城市的 List
    private List<city> citts = new ArrayList<city>();
    private List<city> areas1 = new ArrayList<city>();

    public static final int OFF_SET_DEFAULT = 2;

    private List<String> provincesStr;

    private void setData(City city) {
        if(city != null) {
            provinces = city.getProvinces();
            cities = city.getCities();
            areas = city.getAreas();
            provincesStr = getAreaStr(provinces);

            select_view.setOffset(OFF_SET_DEFAULT);
            select_view2.setOffset(OFF_SET_DEFAULT);
            select_view3.setOffset(OFF_SET_DEFAULT);

            select_view.setItems(provincesStr);

            select_view.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
                @Override
                public void onSelected(int selectedIndex, String item) {
                    // 获取到 省份的 父 id
                    city area = getArea(item, provinces);
                    String parentId = area.getAreaID();
                    // 获取该省份下所有的城市
                    HashMap map = getCitiesStr(parentId);
                    citts = (List<city>) map.get("area");
                    select_view2.setItems((List<String>) map.get("name"));
                    select_view2.setSeletion(0);
                    selectP = area;

                    contactDetail.setReceiver_province_id(area.getAreaID());
                    contactDetail.setReceiver_province(area.getAreaName());

                    if (selectP != null && selectC != null && selectA != null) {
                        region.setText(selectP.getAreaName() + selectC.getAreaName() + selectA.getAreaName());
                    }
                }
            });

            select_view2.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
                @Override
                public void onSelected(int selectedIndex, String item) {
                    // 获取 城市的 id   再查找 地区 id
                    city area = getArea(item, cities);
                    String parentId = area.getAreaID();
                    // 获取该城市下所有的地区
                    HashMap map = getAreasStr(parentId);
                    areas1 = (List<city>) map.get("area");
                    select_view3.setItems((List<String>) map.get("name"));

                    select_view3.setSeletion(0);
                    selectC = area;
                    contactDetail.setReceiver_city_id(area.getAreaID());
                    contactDetail.setReceiver_city(area.getAreaName());

                    if (selectP != null && selectC != null && selectA != null) {
                        region.setText(selectP.getAreaName() + selectC.getAreaName() + selectA.getAreaName());
                    }
                }
            });

            select_view3.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
                @Override
                public void onSelected(int selectedIndex, String item) {
                    city area = getArea(item, areas);
                    selectA = area;

                    contactDetail.setReceiver_area_id(area.getAreaID());
                    contactDetail.setReceiver_area(area.getAreaName());

                    if (selectP != null && selectC != null && selectA != null) {
                        region.setText(selectP.getAreaName() + selectC.getAreaName() + selectA.getAreaName());
                    }
                }
            });

            setRegionFlag = true;
        }
        // 将传递过来的 值 设置上去
        setOriginalData();

    }

    // 一开始传递过来的
    private ContactDetail contactDetail;

    // 传递过来的 order_code
    private String orderNo;

    private void setOriginalData() {
        Intent intent = getIntent();
//        contactDetail = (ContactDetail) intent.getExtras().get("ContactDetail");
//        if (contactDetail != null){
//            consignee.setText(contactDetail.getReceiver_name());
//            phone.setText(contactDetail.getReceiver_mobile());
//            region.setText(contactDetail.getReceiver_province()+contactDetail.getReceiver_city()+contactDetail.getReceiver_area());
//            detail_adress.setText(contactDetail.getReceiver_address());
//
//        }

        orderNo = intent.getExtras().getString("order_code");
        // 通过订单号，拿到订单信息
        getOrderDetail();
    }

    private void getOrderDetail() {
        showLoading();
        RequestParams params = new RequestParams();
        params.put("order_code", orderNo);
//        params.add("order_code","c708fecf8f8e3b39622c35ece3371772");
        BirdApi.getOrderDetail(MyApplication.getInstans(), params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                hideLoading();
                try {
                    if ("0".equals(response.getString("error"))) {
                        contactDetail = JsonHelper.parseObject((JSONObject) response.get("data"), ContactDetail.class);
                        if (contactDetail != null) {
                            consignee.setText(contactDetail.getReceiver_name());
                            phone.setText(contactDetail.getReceiver_mobile());
                            region.setText(contactDetail.getReceiver_province() + contactDetail.getReceiver_city() + contactDetail.getReceiver_area());
                            detail_adress.setText(contactDetail.getReceiver_address());
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                T.showShort(MyApplication.getInstans(), getString(R.string.tip_myaccount_getdatawrong));
                hideLoading();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                T.showShort(MyApplication.getInstans(), getString(R.string.tip_myaccount_getdatawrong));
                hideLoading();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                T.showShort(MyApplication.getInstans(), getString(R.string.tip_myaccount_getdatawrong));
                hideLoading();
            }
        });
    }

    // 将传过来的 省市区 设置上 选择器上
    private void setRegionData() {
        String provinceId = contactDetail.getReceiver_province_id();
        String cityId = contactDetail.getReceiver_city_id();
        String areaId = contactDetail.getReceiver_area_id();

        // 查找 provinceId 所在的位置 并设置上去
        findAndSetAdress(provinces, provinceId, 1);
        findAndSetAdress(citts, cityId, 2);
        findAndSetAdress(areas1, areaId, 3);
    }

    // 通过id 查找所在的位置，并设置到 选择器上
    private void findAndSetAdress(List<city> list, String id, int index) {
        for (int i = 0; i < list.size(); i++) {
            city area = list.get(i);
            if (area.getAreaID().equals(id)) {
                switch (index) {
                    // 省份
                    case 1:
                        select_view.setSeletion(i);
                        break;
                    // 城市
                    case 2:
                        select_view2.setSeletion(i);
                        break;
                    // 地区
                    case 3:
                        select_view3.setSeletion(i);
                        break;
                }
                break;
            }
        }
    }

    // 通过 item 判断选中的
    private city getArea(String cityName, List<city> areas) {
        for (city area : areas) {
            if (area.getAreaName().equals(cityName)) {
                return area;
            }
        }
        return null;
    }

    // 获取所有的省份 城市 或者 地区 的 name 返回 list
    private List<String> getAreaStr(List<city> areas) {
        List<String> provinces = new ArrayList<String>();
        for (city area : areas) {
            provinces.add(area.getAreaName());
        }

        return provinces;
    }

    // 通过 省份  获取到该省份所有的城市
    private HashMap<String, List> getCitiesStr(String parentId) {
        HashMap<String, List> map = new HashMap();

        List<city> list = new ArrayList<city>();
        List<String> strs = new ArrayList<String>();
        for (city area : cities) {
            if (area.getParentID().equals(parentId)) {
                list.add(area);
                strs.add(area.getAreaName());
            }
        }
        map.put("name", strs);
        map.put("area", list);
        return map;
    }

    // 通过 城市 获取到该城市下所有的地区
    private HashMap<String, List> getAreasStr(String parentId) {
        HashMap<String, List> map = new HashMap();

        List<city> list = new ArrayList<city>();
        List<String> strs = new ArrayList<String>();
        for (city area : areas) {
            if (area.getParentID().equals(parentId)) {
                list.add(area);
                strs.add(area.getAreaName());
            }
        }
        map.put("name", strs);
        map.put("area", list);
        return map;
    }

    public static final int SELECT_CONTACT = 1;

    private boolean isFirst = true;

    @OnClick({R.id.contacts, R.id.region_ll, R.id.confirm, R.id.back})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.contacts:
                // 点击选择已有联系人
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, SELECT_CONTACT);
                break;
            // 点击 所在地区
            case R.id.region_ll:
                if (setRegionFlag) {
                    // 如果隐藏 就显示， 反之亦然
                    if (wheelview_ll.getVisibility() == View.VISIBLE) {
                        wheelview_ll.setVisibility(View.GONE);
                    } else {
                        wheelview_ll.setVisibility(View.VISIBLE);
                        if (isFirst) {
                            // 第一次点击 将传过来的 省市区 设置上 选择器上
                            setRegionData();
                            isFirst = false;
                        }
                    }
                }
                break;

            // 点击确定键
            case R.id.confirm:
                if (TextUtils.isEmpty(consignee.getText())) {
                    T.showShort(MyApplication.getInstans(), getString(R.string.consignee_empty));
                    return;
                }
                if (TextUtils.isEmpty(phone.getText())) {
                    T.showShort(MyApplication.getInstans(), getString(R.string.phone_empty));
                    return;
                }
                if (TextUtils.isEmpty(detail_adress.getText())) {
                    T.showShort(MyApplication.getInstans(), getString(R.string.adress_empty));
                    return;
                }

                contactDetail.setReceiver_name(consignee.getText().toString());
                contactDetail.setReceiver_mobile(phone.getText().toString());
                contactDetail.setReceiver_address(detail_adress.getText().toString());

                modOrder();

                break;

            //点击返回键
            case R.id.back:
                finish();
                break;
        }
    }

    private void modOrder() {
        showLoading();
        RequestParams params = new RequestParams();
        params.put("order_code", orderNo);
        params.put("receiver_name", contactDetail.getReceiver_name());
        params.put("receiver_mobile", contactDetail.getReceiver_mobile());
        params.put("receiver_province_id", contactDetail.getReceiver_province_id());
        params.put("receiver_province", contactDetail.getReceiver_province());
        params.put("receiver_city_id", contactDetail.getReceiver_city_id());
        params.put("receiver_city", contactDetail.getReceiver_city());
        params.put("receiver_area_id", contactDetail.getReceiver_area_id());
        params.put("receiver_area", contactDetail.getReceiver_area());
        params.put("receiver_address", contactDetail.getReceiver_address());

        BirdApi.modOrder(MyApplication.getInstans(), params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    hideLoading();
                    if ("0".equals(response.getString("error"))) {
                        T.showShort(MyApplication.getInstans(), getString(R.string.change_suc));
                        String post = region.getText().toString() + detail_adress.getText().toString();
                        EventBus.getDefault().post(post, "changeAddr");
                    } else {
                        T.showShort(MyApplication.getInstans(), response.getString("data"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                hideLoading();
                T.showShort(MyApplication.getInstans(), getString(R.string.change_fail));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                hideLoading();
                T.showShort(MyApplication.getInstans(), getString(R.string.change_fail));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                hideLoading();
                T.showShort(MyApplication.getInstans(), getString(R.string.change_fail));
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SELECT_CONTACT:
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    // 得到ContentResolver对象
                    ContentResolver cr = getContentResolver();
                    // 取得电话本中开始一项的光标
                    Cursor cursor = cr.query(uri, null, null, null, null);
                    // 向下移动光标
                    if (cursor.moveToNext()) {
                        // 取得联系人名字
                        int nameFieldColumnIndex = cursor
                                .getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
                        String contactName = cursor.getString(nameFieldColumnIndex);
                        String contactNumber = "";

                        String contactId = cursor.getString(cursor
                                .getColumnIndex(ContactsContract.Contacts._ID));

                        Cursor phones = getContentResolver().query(
                                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                                        + "=" + contactId, null, null);
                        if (phones.moveToFirst()) {
                            // 取出电话号码
                            contactNumber = phones
                                    .getString(phones
                                            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        }
                        // 将获取的联系人信息 设置上去
                        consignee.setText(contactName);
                        phone.setText(contactNumber);

                        //关闭游标
                        phones.close();
                    }
                    cursor.close();
                }
                break;
        }
    }
}
