package com.hhy.ui;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.hhy.R;
import com.hhy.bean.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import static android.content.ContentValues.TAG;

//    {
//        "code": 1,   //登录成功
//            "username": "zhangsan",  //登录名
//            "password": "123456",// 登录密码
//            "wordname": "工人",  //工种名称
//            "wordid": "1002"   //工种代码
//    }
//http://zhpmy1.blog.163.com/blog/static/58944295201692804128739/
//
//a17dchttp://yushigui.gotoip2.com/df7de

/**
 * 登录页
 * by hhy
 */
public class LoginActivity extends Activity implements View.OnClickListener {
    public static final String BLOG_ADDRESS="http://www.zhikesys.top";// "http://139.199.132.212:8030";//"http://zhpmy1.blog.163.com/blog/static/58944295201692804128739/";/*"http://139.199.132.212:8030/";*/
    public static final String a17dc = "a17dc";
    public static final String df7de = "df7de";
    public static final String USER_NAME = "name";
    public static final String IS_SERVICE_HOST = "is_service_Host";
    public static final String PASS_WORD = "pwd";
    public static final String SERVICE_HOST = "service_host";
    public static final String DJM_HOST = "djm_host";
    public static final String DUIJIEMA = "DUIJIEMA";
    public static final String SP_NAME = "sp_name";
    public static final String TAG_USER = "USER";
    public static final String TAG_C0DE = "gb2312";


    private EditText userNameView, passWordView, hostView;
    private Button login;
    private Boolean isServiceHost;
    private SharedPreferences sp;
    private String url, name, pwd, service_host, duijiema, djm_host;
    private int indexs=0;

    private Handler handle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            index++;
            if (msg.what == 0&&indexs==0) {
                Intent intent = new Intent(LoginActivity.this, ScanActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                loading.setVisibility(View.GONE);
                finish();
                indexs=0;
                onlcickindex=0;
            } else {
               // new MainTask().execute(url);
            }

        }
    };
    private ProgressBar loading;
    private RadioButton duijiemaBtn, hostBtn;
    CheckBox checkBox;
    private int index;
    private LinearLayout softOnclick;
    private int onlcickindex=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sp = getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        login = (Button) findViewById(R.id.login);
        userNameView = (EditText) findViewById(R.id.name);
        passWordView = (EditText) findViewById(R.id.password);
        hostView = (EditText) findViewById(R.id.host);
        loading = (ProgressBar) findViewById(R.id.loading);
        duijiemaBtn = (RadioButton) findViewById(R.id.djmradio);
        hostBtn = (RadioButton) findViewById(R.id.hostradio);
        checkBox = findViewById(R.id.checkboxs);
        softOnclick = findViewById(R.id.softOnclick);
        softOnclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                // 隐藏软键盘
                imm.hideSoftInputFromWindow(
                        getWindow().getDecorView().getWindowToken(), 0);
            }
        });

        setData();

    }

    /**
     * 组件赋值
     */
    private void setData() {
        login.setOnClickListener(this);

        Log.e(TAG, "得到的数据----->" + sp.getAll().toString());
        isServiceHost = sp.getBoolean(IS_SERVICE_HOST, true);
        hostBtn.setChecked(isServiceHost);
        checkBox.setChecked(!isServiceHost);

        if (sp.getString(USER_NAME, null) != null) {
            name = sp.getString(USER_NAME, null);
            userNameView.setText(name);
        }

        if (sp.getString(PASS_WORD, null) != null) {
            pwd = sp.getString(PASS_WORD, null);
            passWordView.setText(pwd);
        }

        if (sp.getString(SERVICE_HOST, null) != null) {
            service_host = sp.getString(SERVICE_HOST, null);
        }

        if (sp.getString(DUIJIEMA, null) != null) {
            duijiema = sp.getString(DUIJIEMA,"");
        }

       // hostView.setHint(isServiceHost ? "请输服务器地址" : "请输入入对接码");
        hostView.setText(duijiema);

        hostBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                hostView.setHint(/*isChecked ? "请输服务器地址" : */"请输入对接码");
                hostView.setText(/*isChecked ? service_host :*/ duijiema);
            }
        });
    }

    @Override
    public void onClick(View v) {
        if(onlcickindex==0) {
            onlcickindex++;
            login.setEnabled(false);
            if (TextUtils.isEmpty(userNameView.getText().toString())) {
                Toast.makeText(this, "用户名不正确", Toast.LENGTH_LONG).show();
                loading.setVisibility(View.GONE);

                login.setEnabled(true);
                return;
            }
            if (TextUtils.isEmpty(passWordView.getText().toString())) {
                Toast.makeText(this, "密码不正确", Toast.LENGTH_LONG).show();
                loading.setVisibility(View.GONE);
                login.setEnabled(true);
                return;
            }
            //TODO 对接码了:
            if (TextUtils.isEmpty(hostView.getText().toString())) {
                loading.setVisibility(View.GONE);
                Toast.makeText(this, /*checkBox.isChecked() ?*/ "对接码不不能为空"/* : "地址不正确"*/, Toast.LENGTH_LONG).show();
                login.setEnabled(true);
                return;
            }
            loading.setVisibility(View.VISIBLE);
            //TODO 对接码了:
            // if (checkBox.isChecked()) {
            if (!TextUtils.isEmpty(hostView.getText().toString())) {
                new BlogTask().execute(BLOG_ADDRESS);
            } else {
                //TODO 对接码了:
           /* try {
                url = hostView.getText().toString() + "/applogin.aspx?user="
                        + URLEncoder.encode(userNameView.getText().toString(), TAG_C0DE) + "&pwd="
                        + URLEncoder.encode(passWordView.getText().toString(), TAG_C0DE);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            new MainTask().execute(url);*/
            }
        }
    }

    /**
     * 网络请求
     *
     * @param address
     * @return
     */
    private String requestGet(String address) {
        try {
           String urls = address + "/applogin.aspx?user="
                    + URLEncoder.encode(userNameView.getText().toString(), TAG_C0DE) + "&pwd="
                    + URLEncoder.encode(passWordView.getText().toString(), TAG_C0DE)
                    + "&duijiema=" + URLEncoder.encode(hostView.getText().toString(), TAG_C0DE);
            // 新建一个URL对象
            URL url = new URL(urls);
            // 打开一个HttpURLConnection连接
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            // 设置连接主机超时时间
            urlConn.setConnectTimeout(5 * 1000);
            //设置从主机读取数据超时
            urlConn.setReadTimeout(5 * 1000);
            // 设置是否使用缓存  默认是true
            urlConn.setUseCaches(true);
            urlConn.setRequestProperty("Charset", TAG_C0DE);
            // 设置为Post请求
            urlConn.setRequestMethod("GET");
            urlConn.setDoOutput(true);
            urlConn.setDoInput(true);
            urlConn.setUseCaches(false);
            //urlConn设置请求头信息
            //设置请求中的媒体类型信息。
//            urlConn.setRequestProperty("Content-Type", "application/json");
//            urlConn.setRequestProperty("Content-type", "application/x-www-form-urlencoded;charset=UTF-8");
            //设置客户端与服务连接类型
            urlConn.addRequestProperty("Connection", "Keep-Alive");
            // 开始连接
            urlConn.connect();
            // 判断请求是否成功
            if (urlConn.getResponseCode() == 200) {
                // 获取返回的数据
                return streamToString(urlConn.getInputStream());
            } else {
                Log.e(TAG, "Get方式请求失败");
            }
            // 关闭连接
            urlConn.disconnect();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return "";
    }

    /**
     * 将输入流转换成字符串
     *
     * @param is 从网络获取的输入流
     * @return
     */
    public String streamToString(InputStream is) {
        BufferedReader br = null;
        StringBuffer resultBuffer = null;
        try {
            br = new BufferedReader(new InputStreamReader(is, TAG_C0DE));
            resultBuffer = new StringBuffer();
            String tempLine = null;
            try {
                while ((tempLine = br.readLine()) != null) {
                    resultBuffer.append(tempLine);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return resultBuffer.toString();

    }

    private Bundle bundle;

    /**
     * 登录异步
     */
    class MainTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            Log.e(TAG, "main url--->" + params[0]);
            return requestGet(params[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            loading.setVisibility(View.GONE);
           // login.setEnabled(true);
            if (TextUtils.isEmpty(result) || !result.contains("###") || !result.contains("***")) {
                Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_LONG).show();
                onlcickindex=0;
                return;
            }

            String str = result.substring(result.indexOf("###") + 3, result.indexOf("***")).replace("^", "\"");
            Log.e(TAG, "main--->" + str);
            User user = JSON.parseObject(str, User.class);
            if (user.getCode() == 1) {
                bundle = new Bundle();
                bundle.putSerializable(TAG_USER, user);
                if (!checkBox.isChecked()) {
                    sp.edit().putString(USER_NAME, userNameView.getText().toString())
                            .putString(PASS_WORD, passWordView.getText().toString())
                            .putString(SERVICE_HOST, hostView.getText().toString())
                            .putBoolean(IS_SERVICE_HOST, true)
                            .commit();
                } else {
                    sp.edit().putString(USER_NAME, userNameView.getText().toString())
                            .putString(PASS_WORD, passWordView.getText().toString())
                            .putString(DJM_HOST, djm_host)
                            .putString(DUIJIEMA, hostView.getText().toString())
                            .putBoolean(IS_SERVICE_HOST, false)
                            .commit();
                }
                Log.e(TAG, "保存的数据----->" + sp.getAll().toString());
                handle.sendEmptyMessage(0);
                Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_LONG).show();
                onlcickindex=0;
            }else if(user.getCode()==2){
                Toast.makeText(LoginActivity.this, "对接码错误", Toast.LENGTH_LONG).show();
                onlcickindex=0;

            }else if(user.getCode()==2){
                Toast.makeText(LoginActivity.this, "用户名或密码错误", Toast.LENGTH_LONG).show();
                login.setEnabled(true);
                onlcickindex=0;

            }else {
                Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_LONG).show();
                onlcickindex=0;

            }

        }

    }

    /**
     * 博客异步
     */
    class BlogTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            Log.e(TAG, "blog url--->" + params[0]);
            return requestGet(params[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            Log.e("result",result.toString());
            //TODO 这里截取最终的字符串
            String str = result.substring(result.indexOf("###") + 3, result.indexOf("***")).replace("^", "\"");
            //TODO 这里去登录
            Log.e(TAG, "main--->" + str);
            User user = JSON.parseObject(str, User.class);
            if (user.getCode() == 1) {
                bundle = new Bundle();
                bundle.putSerializable(TAG_USER, user);
                if (!checkBox.isChecked()) {
                    sp.edit().putString(USER_NAME, userNameView.getText().toString())
                            .putString(PASS_WORD, passWordView.getText().toString())
                            .putString(SERVICE_HOST, hostView.getText().toString())
                            .putBoolean(IS_SERVICE_HOST, true)
                            .commit();
                } else {
                    sp.edit().clear();
                    sp.edit().putString(USER_NAME, userNameView.getText().toString())
                            .putString(PASS_WORD, passWordView.getText().toString())
                            .putString(DJM_HOST, djm_host)
                            .putString(DUIJIEMA, hostView.getText().toString())
                            .putBoolean(IS_SERVICE_HOST, false)
                            .commit();
                }
                sp.edit().putString(USER_NAME, userNameView.getText().toString())
                        .putString(PASS_WORD, passWordView.getText().toString())
                        .putString(DJM_HOST, djm_host)
                        .putString(DUIJIEMA, hostView.getText().toString())
                        .putBoolean(IS_SERVICE_HOST, false)
                        .putString("user",user.toString())
                        .commit();
                Log.e(TAG, "保存的数据----->" + sp.getAll().toString());
                handle.sendEmptyMessage(0);
                Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_LONG).show();
                //login.setEnabled(true);
                onlcickindex=0;

            }else if(user.getCode()==2){
                loading.setVisibility(View.GONE);
                Toast.makeText(LoginActivity.this, "对接码错误", Toast.LENGTH_LONG).show();
                //login.setEnabled(true);

            }else if(user.getCode()==3){
                loading.setVisibility(View.GONE);
                Toast.makeText(LoginActivity.this, "用户名或密码错误", Toast.LENGTH_LONG).show();
                login.setEnabled(true);
                onlcickindex=0;
            }else {
                loading.setVisibility(View.GONE);
                Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_LONG).show();
                //login.setEnabled(true);
                onlcickindex=0;
            }

            /*loading.setVisibility(View.GONE);
            login.setEnabled(true);
            if (TextUtils.isEmpty(result) ||
                    !result.contains(a17dc) || !result.contains(df7de)) {
                Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_LONG).show();
                return;
            }
            djm_host = result.substring(result.indexOf(a17dc) + 5, result.indexOf(df7de) - 1);
            Log.e(TAG, "blog--->" + djm_host);
            try {
                //http://139.199.132.212:8030/applogin.aspx?user=001&pwd=001&duijiema=123123123
                url = djm_host + "/applogin.aspx?user="
                        + URLEncoder.encode(userNameView.getText().toString(), TAG_C0DE) + "&pwd="
                        + URLEncoder.encode(passWordView.getText().toString(), TAG_C0DE)
                        + "&duijiema=" + URLEncoder.encode(hostView.getText().toString(), TAG_C0DE);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }*/

           // handle.sendEmptyMessage(1);
        }
    }
}

