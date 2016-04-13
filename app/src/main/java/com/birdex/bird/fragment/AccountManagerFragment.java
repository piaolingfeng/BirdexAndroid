package com.birdex.bird.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.birdex.bird.MyApplication;
import com.birdex.bird.R;
import com.birdex.bird.activity.LoginActivity;
import com.birdex.bird.api.BirdApi;
import com.birdex.bird.entity.Area;
import com.birdex.bird.entity.BusinessModel;
import com.birdex.bird.entity.City;
import com.birdex.bird.entity.CompanyInformation;
import com.birdex.bird.entity.Contact;
import com.birdex.bird.entity.Market;
import com.birdex.bird.entity.Markets;
import com.birdex.bird.entity.QgModel;
import com.birdex.bird.util.JsonHelper;
import com.birdex.bird.util.StringUtils;
import com.birdex.bird.util.T;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by hyj on 2016/3/29.
 */
public class AccountManagerFragment extends BaseFragment implements View.OnClickListener{

    @Bind(R.id.base_content)
    com.zhy.android.percent.support.PercentLinearLayout baseContent;

    @Bind(R.id.main_content)
    com.zhy.android.percent.support.PercentLinearLayout mainContent;

    @Bind(R.id.contacts_content)
    com.zhy.android.percent.support.PercentLinearLayout contactsContent;

    @Bind(R.id.pattern_content)
    com.zhy.android.percent.support.PercentLinearLayout patternContent;

    @Bind(R.id.base_jia)
    ImageView baseJia;

    @Bind(R.id.main_jia)
    ImageView mainJia;

    @Bind(R.id.contacts_jia)
    ImageView contactsJia;

    @Bind(R.id.pattern_jia)
    ImageView patternJia;

    @Bind(R.id.base_ll)
    com.zhy.android.percent.support.PercentRelativeLayout basell;

    @Bind(R.id.main_ll)
    com.zhy.android.percent.support.PercentRelativeLayout mainll;

    @Bind(R.id.contacts_ll)
    com.zhy.android.percent.support.PercentRelativeLayout contactsll;

    @Bind(R.id.pattern_ll)
    com.zhy.android.percent.support.PercentRelativeLayout patternll;


    // 签约公司名称
    @Bind(R.id.sign_company_name)
    TextView sign_company_name;

    // 公司简称
    @Bind(R.id.company_short_name)
    TextView company_short_name;

    // 详细地址
    @Bind(R.id.detail_address)
    TextView detail_address;

    // 总部所在地
    @Bind(R.id.headquarters_address)
    TextView headquarters_address;

    // 主营市场
    @Bind(R.id.main_market)
    TextView main_market;

    // 订单平均客单价
    @Bind(R.id.order_pri)
    TextView order_pri;

    // 商务总负责人
    @Bind(R.id.principal)
    TextView principal;
    @Bind(R.id.principal_phone_no)
    TextView principal_phone_no;
    @Bind(R.id.principal_email)
    TextView principal_email;

    // 出入库负责人
    @Bind(R.id.outin)
    TextView outin;
    @Bind(R.id.outin_phone_no)
    TextView outin_phone_no;
    @Bind(R.id.outin_email)
    TextView outin_email;

    // 异常件通知邮箱
    @Bind(R.id.exception_email)
    TextView exception_email;

    // 财务对接人
    @Bind(R.id.finance)
    TextView finance;
    @Bind(R.id.finance_phone_no)
    TextView finance_phone_no;
    @Bind(R.id.finance_email)
    TextView finance_email;

    // IT对接人
    @Bind(R.id.it)
    TextView it;
    @Bind(R.id.it_phone_no)
    TextView it_phone_no;
    @Bind(R.id.it_email)
    TextView it_email;


    // 业务模式
    @Bind(R.id.pattern)
    TextView pattern;

    // 清关服务
    @Bind(R.id.clear_server)
    TextView clear_server;


    // 通过 省份 城市 地区 id ，得到 地址
    private String getAddressDetail(City city,String provinceId,String cityId,String areaId){
        String provstr = "";
        String citystr = "";
        String areastr = "";
        List<Area> pro = city.getProvinces();
        for(Area area:pro){
            if(area.getAreaID()!=null && area.getAreaID().equals(provinceId)){
                provstr = area.getAreaName();
            }
        }
        List<Area> cit = city.getCities();
        for(Area area:cit){
            if(area.getAreaID()!=null && area.getAreaID().equals(cityId)){
                citystr = area.getAreaName();
            }
        }
        List<Area> are = city.getAreas();
        for(Area area:are){
            if(area.getAreaID()!=null && area.getAreaID().equals(areaId)){
                areastr = area.getAreaName();
            }
        }

        String result = provstr + citystr + areastr;
        return result;
    }

    // 将接口获取到的值 set 进去
    private void setDetailData(CompanyInformation information, City cities,List<Market> markets, List<BusinessModel> businessModels,List<QgModel> qgModels){
        if(information != null) {
            sign_company_name.setText(information.getCompany_name());
            company_short_name.setText(information.getCompany_short_name());

            // 总部所在地   -- 缺少
            // 所在省份 id
            String provinceId = information.getProvince_id();
            // 所在地城市
            String cityId = information.getCity_id();
            // 所在地区县
            String areaId = information.getArea_id();
            if(cities != null) {
                headquarters_address.setText(getAddressDetail(cities, provinceId, cityId, areaId));
            }
            detail_address.setText(information.getAddress());

            // 主营市场   -- 缺少
            // 返回主营市场 list
            List<String> marTypes = StringUtils.spiltStringArray(information.getMarkets());
            if(markets != null) {
                main_market.setText(getMarketStr(markets, marTypes));
            }

            order_pri.setText(information.getOrder_avg_price());

            // 设置联系人信息相关
            setContactInf(information.getContacts());

            exception_email.setText(information.getNotice_email());

            // 业务模式 list
            List<String> businessType = StringUtils.spiltStringArray(information.getBusiness_models());
            if(businessModels != null) {
                pattern.setText(getBusinessStr(businessModels, businessType));
            }

            // 清关服务  -- 未知
            List<String> qgs = StringUtils.spiltStringArray(information.getQg_models());
            if(qgModels != null) {
                clear_server.setText(getQgStr(qgModels, qgs));
            }

        }
        hideLoading();
    }

    // 获取 主营市场 字符串
    private String getMarketStr(List<Market> markets,List<String> marTypes){
        List<String> result = new ArrayList<>();
        for(String no:marTypes){
            for(Market market:markets){
                if(market.getId().equals(no)){
                    result.add(market.getName());
                }
            }
        }
        return StringUtils.listToStr(result);
    }

    // 获取 业务模式 字符串
    private String getBusinessStr(List<BusinessModel> businessModels,List<String>businessType){
        List<String> result = new ArrayList<>();
        for(String no:businessType){
            for(BusinessModel model:businessModels){
                if(model.getId().equals(no)){
                    result.add(model.getName());
                }
            }
        }
        return StringUtils.listToStr(result);
    }

    // 获取 清关服务 字符串
    private String getQgStr(List<QgModel> qgModels,List<String> qgType){
        List<String> result = new ArrayList<>();
        for(String no:qgType){
            for(QgModel qgModel:qgModels){
                if(qgModel.getId().equals(no)){
                    result.add(qgModel.getName());
                }
            }
        }
        return StringUtils.listToStr(result);
    }

    // 设置联系人信息
    private void setContactInf(List<Contact> contacts){
        for(Contact contact : contacts){
            if("10".equals(contact.getContact_type())){
                principal.setText(contact.getName());
                principal_phone_no.setText(contact.getPhone());
                principal_email.setText(contact.getEmail());
            } else if("20".equals(contact.getContact_type())){
                outin.setText(contact.getName());
                outin_phone_no.setText(contact.getPhone());
                outin_email.setText(contact.getEmail());
            } else if("30".equals(contact.getContact_type())){
                finance.setText(contact.getName());
                finance_phone_no.setText(contact.getPhone());
                finance_email.setText(contact.getEmail());
            } else if("40".equals(contact.getContact_type())){
                it.setText(contact.getName());
                it_phone_no.setText(contact.getPhone());
                it_email.setText(contact.getEmail());
            }
        }
    }


    @Override
    protected void key(int keyCode, KeyEvent event) {

    }

    @Override
    public int getContentLayoutResId() {
        return R.layout.fragment_accountmanager;
    }

    @Override
    public void initializeContentViews() {
        initData();
    }

    // 获取的公司信息
    private CompanyInformation companyInf;

    // 数据就绪标记
    private boolean dataReady = false;

    // 数据类型就绪标记
    private boolean typeReady = false;

    // 获取的城市信息
    private City myCities;

    private List<Market> myMarkest;

    private List<BusinessModel> businessModels;

    private  List<QgModel> qgModels;

    // 初始化数据
    private void initData(){
        showLoading();
        RequestParams params = new RequestParams();
        BirdApi.getCompanyMes(MyApplication.getInstans(), params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    if (response.get("error") == 0) {
                        companyInf = JsonHelper.parseObject((JSONObject) response.get("data"), CompanyInformation.class);

                        if (companyInf != null) {

                            dataReady = true;

                            if(typeReady){
                                setDetailData(companyInf, myCities, myMarkest, businessModels, qgModels);
                            }

                        }


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                hideLoading();
                T.showShort(getActivity(), getString(R.string.tip_myaccount_getdatawrong));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                hideLoading();
                T.showShort(getActivity(), getString(R.string.tip_myaccount_getdatawrong));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                hideLoading();
                T.showShort(getActivity(), getString(R.string.tip_myaccount_getdatawrong));
            }
        });


        // 请求获取相关的 type 数据，成功获取后再展现数据
        RequestParams requestParams = new RequestParams();
        requestParams.put("city", "北京");
        requestParams.put("markets", "all");
        requestParams.put("business_models", "all");
        requestParams.put("qg_models", "all");
        BirdApi.getConfig(MyApplication.getInstans(), requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                JSONObject all = null;
                try {

                    all = (JSONObject) response.get("data");
                    JSONObject city = (JSONObject) all.get("city");
                    myCities = JsonHelper.parseObject(city, City.class);
                    JSONObject markets = (JSONObject) all.get("markets");

                    myMarkest = new ArrayList<Market>();

                    Iterator<String> marketIter = markets.keys();
                    while (marketIter.hasNext()) {
                        String ma = marketIter.next();
                        JSONObject mm = (JSONObject) markets.get(ma);
                        Market market1 = JsonHelper.parseObject(mm, Market.class);
                        myMarkest.add(market1);
                    }

                    JSONObject business = (JSONObject) all.get("business_models");
                    businessModels = new ArrayList<BusinessModel>();
                    Iterator<String> businessIt = business.keys();
                    while (businessIt.hasNext()) {
                        String bb = businessIt.next();
                        JSONObject bm = (JSONObject) business.get(bb);
                        businessModels.add(JsonHelper.parseObject(bm, BusinessModel.class));
                    }

                    JSONObject qgModel = (JSONObject) all.get("qg_models");
                    qgModels = new ArrayList<QgModel>();
                    Iterator<String> models = qgModel.keys();
                    while (models.hasNext()) {
                        String mo = models.next();
                        JSONObject moo = (JSONObject) qgModel.get(mo);
                        qgModels.add(JsonHelper.parseObject(moo, QgModel.class));
                    }

                    typeReady = true;

                    if(dataReady) {
                        setDetailData(companyInf, myCities, myMarkest, businessModels, qgModels);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                hideLoading();
                T.showShort(getActivity(), getString(R.string.tip_myaccount_getdatawrong));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                hideLoading();
                T.showShort(getActivity(), getString(R.string.tip_myaccount_getdatawrong));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                hideLoading();
                T.showShort(getActivity(), getString(R.string.tip_myaccount_getdatawrong));
            }
        });
    }



    @Override
    protected void lazyLoad() {

    }


    // 控制 打开的信息
    private void control(int index){
        if( index == 0){
            // 打开了 base_ll
            baseContent.setVisibility(View.VISIBLE);
            baseJia.setBackgroundResource(R.drawable.arrow_down_select);

            mainContent.setVisibility(View.GONE);
            mainJia.setBackgroundResource(R.drawable.arrow_up_select);
            contactsContent.setVisibility(View.GONE);
            contactsJia.setBackgroundResource(R.drawable.arrow_up_select);
            patternContent.setVisibility(View.GONE);
            patternJia.setBackgroundResource(R.drawable.arrow_up_select);
        } else if( index == 1){
            // 打开了 main_ll
            mainContent.setVisibility(View.VISIBLE);
            mainJia.setBackgroundResource(R.drawable.arrow_down_select);

            baseContent.setVisibility(View.GONE);
            baseJia.setBackgroundResource(R.drawable.arrow_up_select);
            contactsContent.setVisibility(View.GONE);
            contactsJia.setBackgroundResource(R.drawable.arrow_up_select);
            patternContent.setVisibility(View.GONE);
            patternJia.setBackgroundResource(R.drawable.arrow_up_select);
        } else if( index == 2){
            // 打开了 contacts_ll
            contactsContent.setVisibility(View.VISIBLE);
            contactsJia.setBackgroundResource(R.drawable.arrow_down_select);

            baseContent.setVisibility(View.GONE);
            baseJia.setBackgroundResource(R.drawable.arrow_up_select);
            mainContent.setVisibility(View.GONE);
            mainJia.setBackgroundResource(R.drawable.arrow_up_select);
            patternContent.setVisibility(View.GONE);
            patternJia.setBackgroundResource(R.drawable.arrow_up_select);
        } else if( index == 3){
            // 打开了 pattern_ll
            patternContent.setVisibility(View.VISIBLE);
            patternJia.setBackgroundResource(R.drawable.arrow_down_select);

            baseContent.setVisibility(View.GONE);
            baseJia.setBackgroundResource(R.drawable.arrow_up_select);
            mainContent.setVisibility(View.GONE);
            mainJia.setBackgroundResource(R.drawable.arrow_up_select);
            contactsContent.setVisibility(View.GONE);
            contactsJia.setBackgroundResource(R.drawable.arrow_up_select);
        }
    }


    @OnClick({R.id.base_ll, R.id.main_ll, R.id.contacts_ll, R.id.pattern_ll, R.id.out_account})
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.base_ll:
                if(baseContent.getVisibility() != View.VISIBLE){
                    baseContent.setVisibility(View.VISIBLE);
                    baseJia.setBackgroundResource(R.drawable.arrow_down_select);
                    control(0);
                }
                break;

            case R.id.main_ll:
                if(mainContent.getVisibility() != View.VISIBLE){
                    mainContent.setVisibility(View.VISIBLE);
                    mainJia.setBackgroundResource(R.drawable.arrow_down_select);
                    control(1);
                }
                break;

            case R.id.contacts_ll:
                if(contactsContent.getVisibility() != View.VISIBLE){
                    contactsContent.setVisibility(View.VISIBLE);
                    contactsJia.setBackgroundResource(R.drawable.arrow_down_select);
                    control(2);
                }
                break;
//
            case R.id.pattern_ll:
                if(patternContent.getVisibility() != View.VISIBLE){
                    patternContent.setVisibility(View.VISIBLE);
                    patternJia.setBackgroundResource(R.drawable.arrow_down_select);
                    control(3);
                }
                break;

            // 退出账户按钮
            case R.id.out_account:
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                MyApplication.getInstans().clearActivities();
                startActivity(intent);
                break;
        }
    }
}
