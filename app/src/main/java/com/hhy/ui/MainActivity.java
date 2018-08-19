package com.hhy.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.hhy.R;
import com.hhy.utils.Constant;

import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        int mode = getIntent().getIntExtra(Constant.REQUEST_SCAN_MODE, Constant.REQUEST_SCAN_MODE_ALL_MODE);
        if (Build.VERSION.SDK_INT>22){
            if (ContextCompat.checkSelfPermission(MainActivity.this,
                    android.Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
                //先判断有没有权限 ，没有就在这里进行权限的申请
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{android.Manifest.permission.CAMERA},1);

            }else {
                //说明已经获取到摄像头权限了 想干嘛干嘛
            }
        }else {
//这个说明系统版本在6.0之下，不需要动态获取权限。

        }

    }

    /**
     * 按钮监听事件，这里我使用Butterknife，不喜欢的也可以直接写监听
     * @param view
     */
    @OnClick({R.id.create_code,R.id.scan_2code,R.id.scan_bar_code,R.id.scan_code})
    public void clickListener(View view){
        Intent intent;
        switch (view.getId()){
            case  R.id.create_code: //生成码
                intent=new Intent(this,CreateCodeActivity.class);
                startActivity(intent);
                break;
            case  R.id.scan_2code: //扫描二维码
                intent=new Intent(this,CommonScanActivity.class);
                intent.putExtra(Constant.REQUEST_SCAN_MODE,Constant.REQUEST_SCAN_MODE_QRCODE_MODE);
                startActivity(intent);
                break;
            case  R.id.scan_bar_code://扫描条形码
                intent=new Intent(this,CommonScanActivity.class);
                intent.putExtra(Constant.REQUEST_SCAN_MODE,Constant.REQUEST_SCAN_MODE_BARCODE_MODE);
                startActivity(intent);
                break;
            case  R.id.scan_code://扫描条形码或者二维码
                intent=new Intent(this,CommonScanActivity.class);
                intent.putExtra(Constant.REQUEST_SCAN_MODE,Constant.REQUEST_SCAN_MODE_ALL_MODE);
                startActivity(intent);
                break;
        }
    }
}
