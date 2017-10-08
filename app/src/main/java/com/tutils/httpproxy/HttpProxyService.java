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
    private String m_agentAddr = "tvps.tutils.com:53129";
    private String m_proxyAddr = "127.0.0.1:56080";
    private Thread m_proxyThread;

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
                .setContentTitle("HTTP Proxy is running") // 设置下拉列表里的标题
                .setContentText(m_proxyAddr)  // 设置上下文内容
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
        if (m_proxyThread != null && m_proxyThread.isAlive()) {
            // TODO: stop thread in itself
            //m_proxyThread.stop();
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
        if (m_proxyThread == null) {
            m_proxyThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    Gobind.startProxy(m_agentAddr, m_proxyAddr);
                }
            });
        }

        if (!m_proxyThread.isAlive()) {
            m_proxyThread.start();
        }
    }
}
