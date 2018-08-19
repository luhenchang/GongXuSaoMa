package com.hhy.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hhy.R;


public class Welcome extends Activity {
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        imageView=findViewById(R.id.ac_well_iv);
        //加载资源图片
        Glide.with(this).load("http://www.zhikesys.top/image/adminpic.jpg").skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE).into(imageView);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(Welcome.this, LoginActivity.class); // 从启动动画ui跳转到主ui
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                Welcome.this.finish(); // 结束启动动画界面

            }
        }, 2000); // 启动动画持续3秒钟

    }
}
