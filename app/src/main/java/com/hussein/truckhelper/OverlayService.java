package com.hussein.truckhelper;

import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.GradientDrawable;
import android.os.IBinder;
import android.provider.Settings;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class OverlayService extends Service {
    private WindowManager wm;
    private View bubble;
    private View panel;
    private WindowManager.LayoutParams bubbleParams;
    private float downX, downY;
    private int startX, startY;
    private boolean moved;
    private TextView scanResult;

    @Override
    public void onCreate() {
        super.onCreate();

        if (!Settings.canDrawOverlays(this)) {
            stopSelf();
            return;
        }

        wm = (WindowManager) getSystemService(WINDOW_SERVICE);

        TextView icon = new TextView(this);
        icon.setText("🚛");
        icon.setTextSize(26);
        icon.setGravity(Gravity.CENTER);
        icon.setTextColor(Color.WHITE);

        GradientDrawable circle = new GradientDrawable();
        circle.setShape(GradientDrawable.OVAL);
        circle.setColor(Color.rgb(57, 73, 171));
        circle.setStroke(3, Color.rgb(255, 193, 7));
        icon.setBackground(circle);

        bubble = icon;
        bubbleParams = new WindowManager.LayoutParams(
            dp(62), dp(62),
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        );
        bubbleParams.gravity = Gravity.TOP | Gravity.START;
        bubbleParams.x = dp(16);
        bubbleParams.y = dp(220);

        bubble.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    downX = event.getRawX();
                    downY = event.getRawY();
                    startX = bubbleParams.x;
                    startY = bubbleParams.y;
                    moved = false;
                    return true;

                case MotionEvent.ACTION_MOVE:
                    int dx = (int) (event.getRawX() - downX);
                    int dy = (int) (event.getRawY() - downY);
                    if (Math.abs(dx) > dp(5) || Math.abs(dy) > dp(5)) {
                        moved = true;
                    }
                    bubbleParams.x = startX + dx;
                    bubbleParams.y = startY + dy;
                    wm.updateViewLayout(bubble, bubbleParams);
                    return true;

                case MotionEvent.ACTION_UP:
                    if (!moved) togglePanel();
                    return true;
            }
            return false;
        });

        wm.addView(bubble, bubbleParams);
    }

    private void togglePanel() {
        if (panel != null) {
            wm.removeView(panel);
            panel = null;
            scanResult = null;
            return;
        }

        LinearLayout box = new LinearLayout(this);
        box.setOrientation(LinearLayout.VERTICAL);
        box.setPadding(dp(12), dp(10), dp(12), dp(10));
        box.setBackgroundColor(Color.rgb(18, 24, 38));

        TextView title = new TextView(this);
        title.setText("Truck Helper 🚛");
        title.setTextSize(20);
        title.setTextColor(Color.WHITE);
        box.addView(title);

        TextView status = new TextView(this);
        status.setText("مساعد الرحلات — فحص تجريبي");
        status.setTextColor(Color.LTGRAY);
        status.setPadding(0, dp(8), 0, dp(8));
        box.addView(status);

        Button scan = new Button(this);
        scan.setText("فحص الشاشة");
        scan.setOnClickListener(v -> {
            if (!GameAccessibilityService.isConnected()) {
                scanResult.setText("فعّل خدمة إمكانية الوصول أولاً");
                return;
            }
            scanResult.setText(GameAccessibilityService.getScreenText());
        });
        box.addView(scan);

        ScrollView scroll = new ScrollView(this);
        scanResult = new TextView(this);
        scanResult.setText("افتح شاشة الرحلات ثم اضغط فحص الشاشة");
        scanResult.setTextColor(Color.WHITE);
        scanResult.setTextSize(14);
        scroll.addView(scanResult);
        box.addView(scroll, new LinearLayout.LayoutParams(
            -1, dp(150)
        ));

        Button test = new Button(this);
        test.setText("اختبار اللمس");
        test.setOnClickListener(v -> {
            if (!GameAccessibilityService.isConnected()) {
                Toast.makeText(this,
                    "فعّل خدمة إمكانية الوصول أولاً",
                    Toast.LENGTH_LONG).show();
                return;
            }
            GameAccessibilityService.testTap();
        });
        box.addView(test);

        Button close = new Button(this);
        close.setText("إغلاق اللوحة");
        close.setOnClickListener(v -> togglePanel());
        box.addView(close);

        Button hide = new Button(this);
        hide.setText("إخفاء الدائرة");
        hide.setOnClickListener(v -> stopSelf());
        box.addView(hide);

        WindowManager.LayoutParams p = new WindowManager.LayoutParams(
            dp(270), WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        );
        p.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
        p.y = dp(80);

        panel = box;
        wm.addView(panel, p);
    }

    private int dp(int value) {
        return (int) (value *
            getResources().getDisplayMetrics().density + 0.5f);
    }

    @Override
    public void onDestroy() {
        if (wm != null) {
            if (panel != null) wm.removeView(panel);
            if (bubble != null) wm.removeView(bubble);
        }
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
