package ipower.hongbaohelper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

/**
 * 红包助手主界面。
 * @author young(jeason1914@qq.com)
 * @since 2016-01-12
 */
public class MainActivity extends Activity implements View.OnClickListener{
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "加载界面布局文件...");
        setContentView(R.layout.activity_main);
        //加载按钮
        final View btn = this.findViewById(R.id.btnStart);
        btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Log.d(TAG, "开启微信红包助手");
        try {
            startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
            Toast.makeText(this,"找到["+getString(R.string.app_name)+"],然后开启服务即可", Toast.LENGTH_LONG).show();
        }catch(Exception e){
            Log.e(TAG,"异常:" + e.getMessage(), e);
            Toast.makeText(this,"发生异常:" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
