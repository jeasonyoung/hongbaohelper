package ipower.hongbaohelper;

import android.accessibilityservice.AccessibilityService;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.PendingIntent;
import android.os.Build;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import java.util.List;

/**
 * 红包服务实现。
 * Created by jeasonyoung on 16/1/12.
 */
public class HongbaoServie extends AccessibilityService {
    private static final String TAG = "HongbaoServie";

    //领取红包
    private static final String WECHAT_UI_GET_PACKET = "com.tencent.mm.ui.LauncherUI";

    //拆红包
    private static final String WECHAT_UI_OPEN_PACKET = "com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyReceiveUI";

    //通知栏关键字
    private static final String NOTIFY_TEXT_KEY = "[微信红包]";
    //领取红包关键字
    private static final String PACKET_GET_KEY = "领取红包";
    //拆红包关键字
    private static final String PACKET_OPEN_KEY = "拆红包";

    private boolean isFirstChecked;


    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        Log.d(TAG,"事件=>" + event);
        switch (event.getEventType()) {
            //通知栏事件
            case AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED:{
                final List<CharSequence> texts = event.getText();
                if(!texts.isEmpty()){
                    for (CharSequence text : texts){
                        final String msg = String.valueOf(text);
                        if(msg.contains(NOTIFY_TEXT_KEY)){
                            //打开通知栏
                            this.openNotify(event);
                            break;
                        }
                    }
                }
                break;
            }
            //打开红包
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:{
                this.openPacket(event);
                break;
            }
        }
    }

    //打开通知栏
    private void openNotify(AccessibilityEvent e){
        if(e == null || e.getParcelableData() == null) return;
        if(!(e.getParcelableData() instanceof Notification)) return;
        //通知栏消息打开
        final Notification notify = (Notification)e.getParcelableData();
        PendingIntent pendingIntent = notify.contentIntent;
        try{
            Log.d(TAG,"通知栏消息打开=>" + e);
            isFirstChecked = true;
            pendingIntent.send();
        }catch(Exception ex) {
            Log.e(TAG, "通知栏消息打开异常:" + ex.getMessage(), ex);
        }
    }

    //打开红包
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void openPacket(AccessibilityEvent e){
        if(e == null) return;
        switch (String.valueOf(e.getClassName())){
            //领取红包
            case WECHAT_UI_GET_PACKET:{
                Log.d(TAG,"准备领取红包=>" + e);
                final AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
                if(nodeInfo == null){
                    Log.w(TAG,"ROOT WINDOW为空!");
                    return;
                }
                final List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByText(PACKET_GET_KEY);
                if(!list.isEmpty()){
                    //最新的红包领取
                    final AccessibilityNodeInfo parent = list.get(list.size() - 1).getParent();
                    Log.i(TAG,"领取红包＝>" + parent);
                    if(parent != null && isFirstChecked){
                        isFirstChecked = false;
                        parent.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    }
                }
                break;
            }
            //拆红包
            case WECHAT_UI_OPEN_PACKET:{
                Log.d(TAG,"准备拆红包=>" + e);
                final AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
                if(nodeInfo == null){
                    Log.w(TAG,"ROOT WINDOW为空!");
                    return;
                }
                final List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByText(PACKET_OPEN_KEY);
                if(!list.isEmpty()){
                    for(AccessibilityNodeInfo info : list){
                        info.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    }
                }
                break;
            }
        }
    }

    @Override
    public void onInterrupt() {
        Toast.makeText(this,"中断抢红包服务!", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        Toast.makeText(this,"连接抢红包服务", Toast.LENGTH_SHORT).show();
    }
}
