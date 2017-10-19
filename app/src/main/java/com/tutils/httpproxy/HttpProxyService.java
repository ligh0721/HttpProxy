package com.tutils.httpproxy;


import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import gobind.Gobind;

/**
 * Created by t5w0rd on 2017/10/8.
 */

public class HttpProxyService extends Service {
    private static final String TAG = HttpProxyService.class.getSimpleName();
    //private String mAgentAddr = "tvps.tutils.com:53129";
    private String mAgentAddr = "tvps.tutils.com:51081";
    private String mProxyAddr = "0.0.0.0:56080";
    private Thread mProxyThread;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate()");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand()");

        startProxyThread();

        // 在API11之后构建Notification的方式
        Notification.Builder builder = new Notification.Builder(this.getApplicationContext()); //获取一个Notification构造器
        Intent nfIntent = new Intent(this, MainActivity.class);
        nfIntent.setAction("android.intent.action.MAIN")
                .addCategory("android.intent.category.LAUNCHER");

        builder.setContentIntent(PendingIntent.getActivity(this, 0, nfIntent, 0)) // 设置PendingIntent
                //.setCategory("android.intent.category.LAUNCHER")
                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(),
                        R.mipmap.ic_launcher)) // 设置下拉列表中的图标(大图标)
                .setSmallIcon(R.drawable.ic_stat_noti) // 设置状态栏内的小图标
                .setContentTitle(getString(R.string.noti_proxy_service_is_running)) // 设置下拉列表里的标题
                .setContentText(mProxyAddr)  // 设置上下文内容
                .setWhen(System.currentTimeMillis()); // 设置该通知发生的时间

        Notification notification = builder.getNotification(); // 获取构建好的Notification
        //notification.defaults = Notification.DEFAULT_SOUND; //设置为默认的声音

        // 参数一：唯一的通知标识；参数二：通知消息。
        startForeground(110, notification);// 开始前台服务

        // toast
        Toast.makeText(getApplicationContext(),
                getString(R.string.toast_proxy_is_started),
                Toast.LENGTH_SHORT).show();

        return Service.START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy()");
        if (mProxyThread != null && mProxyThread.isAlive()) {
            // TODO: stop thread in itself
            //mProxyThread.stop();
        }
        stopForeground(true);  // 停止前台服务--参数：表示是否移除之前的通知

        // toast
        Toast.makeText(getApplicationContext(),
                getString(R.string.toast_proxy_is_stopped),
                Toast.LENGTH_SHORT).show();

        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind()");
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void startProxyThread() {
        if (mProxyThread == null) {
            mProxyThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    Gobind.startProxy(mAgentAddr, mProxyAddr);
                }
            });
        }

        if (!mProxyThread.isAlive()) {
            mProxyThread.start();
        }
    }
}
