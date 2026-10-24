
package com.hussein.truckhelper;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.graphics.Path;
import android.view.accessibility.AccessibilityEvent;

public class GameAccessibilityService extends AccessibilityService {

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        // يستقبل أحداث إمكانية الوصول عند تفعيل الخدمة.
    }

    @Override
    public void onInterrupt() {
        // إيقاف أي إجراء عند مقاطعة الخدمة.
    }

    public void tap(float x, float y) {
        Path path = new Path();
        path.moveTo(x, y);

        GestureDescription.StrokeDescription stroke =
            new GestureDescription.StrokeDescription(path, 0, 100);

        GestureDescription gesture =
            new GestureDescription.Builder()
                .addStroke(stroke)
                .build();

        dispatchGesture(gesture, null, null);
    }
}
