package com.hhy.ui;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.hhy.bean.User;
import com.hhy.R;
import com.hhy.utils.Constant;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * 扫码结果页
 * by hhy
 */
public class ScanActivity extends Activity implements View.OnClickListener {
//    serverurl+ExecuteSqlSaotiaomaapp.aspx?mbtypeid=01&type=appExcuteStatus&tiaomabiaoshi=wordid&ordernumber=0117033100


    public static final String TAG_RESULT = "result";
    public static final int REQUEST_CODE = 110;
    public static final int RESULT_CODE = 119;
    private long firstTime = 0;

    private Button scan;
    private WebView web;
    private SharedPreferences sp;
    private String host, duijima, djm_host, service_host;
    private String  hosts="http://www.zhikesys.top";
    private User user;
    private boolean isHost;
    private ProgressBar loading;
    private Spinner spinner;
    private ArrayList<String> data_list;
    private ArrayAdapter<String> arr_adapter;
    private ArrayList<String> data_listId;
    int indextt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = getSharedPreferences(LoginActivity.SP_NAME, Context.MODE_PRIVATE);
        setContentView(R.layout.activity_scan);
        user = (User) getIntent().getExtras().getSerializable(LoginActivity.TAG_USER);
        scan = (Button) findViewById(R.id.scan);
        web = (WebView) findViewById(R.id.webview);
        loading = (ProgressBar) findViewById(R.id.scan_loading);
        spinner=findViewById(R.id.spinner);
        setData();

    }

    /**
     * 组件赋值
     */
    private void setData() {
        //数据
        data_list = new ArrayList<String>();
        data_listId=new ArrayList<String>();
        for (int i = 0; i <user.getWordname().size(); i++) {
            data_list.add(user.getWordname().get(i).getName());
            data_listId.add(user.getWordname().get(i).getId());

        }



        //适配器
        arr_adapter= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,data_list);
        //设置样式
        arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器
        spinner.setAdapter(arr_adapter);
        spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {//选择item的选择点击监听事件
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                indextt=arg2;
                // TODO Auto-generated method stub
                // 将所选mySpinner 的值带入myTextView 中
               Toast.makeText(ScanActivity.this,"您已切换到岗位：" + data_list.get(arg2),Toast.LENGTH_LONG).show();//文本说明
                LoadWebView(user.getWordname().get(arg2).getName(),user.getWordname().get(arg2).getId());
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });


        if (sp.getString(LoginActivity.SERVICE_HOST, null) != null) {
            service_host = sp.getString(LoginActivity.SERVICE_HOST, "");
        }
        if (sp.getBoolean(LoginActivity.IS_SERVICE_HOST, false)) {
            isHost = sp.getBoolean(LoginActivity.IS_SERVICE_HOST, false);
        }
        if (sp.getString(LoginActivity.DUIJIEMA, null) != null) {
            duijima = sp.getString(LoginActivity.DUIJIEMA, null);
        }
        if (sp.getString(LoginActivity.DJM_HOST, null) != null) {
            djm_host = sp.getString(LoginActivity.DJM_HOST, null);
        }
        host = isHost ? service_host : djm_host;
       //"http://139.199.132.212:8030";
        scan.setOnClickListener(this);
        if(data_list.size()>0) {
            LoadWebView(data_list.get(0), data_listId.get(0));
        }
        //***/ExecuteSqlSaotiaomaapp.aspx?duijiema=****&user=***&tiaomabiaoshi=***

    }
    void LoadWebView(String wordname,String wordId){
        try {
            web.loadUrl(hosts + "/ExecuteSqlSaotiaomaapp.aspx?"
                    + "duijiema=" + URLEncoder.encode(duijima, LoginActivity.TAG_C0DE)
                    +"&user="+URLEncoder.encode(user.getUsername(), LoginActivity.TAG_C0DE)
                    +"&tiaomabiaoshi="+URLEncoder.encode(wordId, LoginActivity.TAG_C0DE)
                    +"&wordname=" + URLEncoder.encode(wordname,LoginActivity.TAG_C0DE));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Log.e("hhy", "start weburl-------->" + web.getUrl());
        web.getSettings().setJavaScriptEnabled(true);
        web.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                // TODO Auto-generated method stub
                if (newProgress == 100) {
                    // 网页加载完成
                    loading.setVisibility(View.GONE);
                } else {
                    // 加载中

                }

            }
        });
    }
    @Override
    public void onClick(View v) {
        requestPermission();
    }


    private static final int MY_PERMISSIONS_REQUEST_CALL_CAMERA = 10000;//请求码，自己定义
//检查权限

   //TODO 这里webView
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//***/ExecuteSqlSaotiaomaapp.aspx&duijiema=****&user=***&tiaomabiaoshi=***&ordernumber=***
        if (requestCode == REQUEST_CODE && resultCode == RESULT_CODE) {
            loading.setVisibility(View.VISIBLE);
            try {
                web.loadUrl(hosts + "/ExecuteSqlSaotiaomaapp.aspx?" +
                        "mbtypeid=01" +
                        "&type=appExcuteStatus" +
                        "&tiaomabiaoshi=" + URLEncoder.encode(user.getWordname().get(indextt).getId(), LoginActivity.TAG_C0DE) +
                        "&ordernumber=" + URLEncoder.encode(data.getStringExtra(TAG_RESULT) , LoginActivity.TAG_C0DE)+
                        "&duijiema=" + URLEncoder.encode(duijima, LoginActivity.TAG_C0DE)+
                        "&user=" + URLEncoder.encode(user.getUsername()  , LoginActivity.TAG_C0DE)+
                        "&wordname=" + URLEncoder.encode(user.getWordname().get(indextt).getName(),LoginActivity.TAG_C0DE)
                );
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            Log.e("hhy", "now weburl-------->" + web.getUrl());
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            long secondTime = System.currentTimeMillis();
            if (secondTime - firstTime > 2000) {
                Toast.makeText(ScanActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                firstTime = secondTime;
                return true;
            } else {
                System.exit(0);
            }
        }
        return super.onKeyDown(keyCode, event);
    }
    /**
     * 调用扫码页面
     */
    private void requetScan() {
        Intent intent = new Intent(ScanActivity.this, CommonScanActivity.class);
        intent.putExtra(Constant.REQUEST_SCAN_MODE, Constant.REQUEST_SCAN_MODE_ALL_MODE);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //判断请求码
        if (requestCode == MY_PERMISSIONS_REQUEST_CALL_CAMERA) {
            //grantResults授权结果
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //成功，开启摄像头
                requetScan();
            } else {
                //授权失败
                Toast.makeText(this, "授权失败", Toast.LENGTH_SHORT).show();
            }
            return;
        }
    }

    /**
     * 请求权限
     */
    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            /**
             * 第一次请求权限时，用户如果拒绝，下一次请求调用shouldShowRequestPermissionRationale()
             * 方法返回true
             * 向用户解释为什么需要这个权限
             */
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                new AlertDialog.Builder(this)
                        .setMessage("申请相机权限")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //申请相机权限
                                ActivityCompat.requestPermissions(ScanActivity.this,
                                        new String[]{Manifest.permission.CAMERA,
                                                Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_CALL_CAMERA);
                            }
                        }).show();
            } else {
                //申请相机权限
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_CALL_CAMERA);
            }
        } else {
            requetScan();
        }
    }
}


