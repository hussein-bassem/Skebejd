package com.hussein.truckhelper;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.graphics.Path;
import android.view.accessibility.AccessibilityEvent;

public class GameAccessibilityService extends AccessibilityService {

    private static GameAccessibilityService instance;

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        instance = this;
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        // لا ينفذ أي إجراء تلقائي عند استقبال الأحداث.
    }

    @Override
    public void onInterrupt() {
    }

    public static boolean isConnected() {
        return instance != null;
    }

    public static void testTap() {
        if (instance == null) {
            return;
        }

        float x = instance.getResources()
            .getDisplayMetrics().widthPixels / 2f;
        float y = instance.getResources()
            .getDisplayMetrics().heightPixels / 2f;

        Path path = new Path();
        path.moveTo(x, y);

        GestureDescription gesture =
            new GestureDescription.Builder()
                .addStroke(
                    new GestureDescription.StrokeDescription(
                        path, 0, 100
                    )
                )
                .build();

        instance.dispatchGesture(gesture, null, null);
    }

    @Override
    public void onDestroy() {
        instance = null;
        super.onDestroy();
    }
}
