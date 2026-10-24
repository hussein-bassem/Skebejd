package com.hussein.truckhelper;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.graphics.Path;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

public class GameAccessibilityService extends AccessibilityService {

    private static GameAccessibilityService instance;
    private static String screenText = "ما تم فحص الشاشة بعد";

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        instance = this;
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (instance != null) {
            scanScreen();
        }
    }

    private void scanScreen() {
        AccessibilityNodeInfo root = getRootInActiveWindow();

        if (root == null) {
            screenText = "ما گدرنا نقرا عناصر الشاشة";
            return;
        }

        StringBuilder result = new StringBuilder();
        readNodes(root, result, 0);
        root.recycle();

        screenText = result.length() == 0
                ? "ما لكينا نصوص قابلة للقراءة"
                : result.toString();
    }

    private void readNodes(
            AccessibilityNodeInfo node,
            StringBuilder result,
            int depth) {

        if (node == null || depth > 15) {
            return;
        }

        CharSequence text = node.getText();
        CharSequence description = node.getContentDescription();

        if (text != null && text.length() > 0) {
            result.append(text).append('\n');
        }

        if (description != null && description.length() > 0) {
            result.append(description).append('\n');
        }

        for (int i = 0; i < node.getChildCount(); i++) {
            AccessibilityNodeInfo child = node.getChild(i);
            if (child != null) {
                readNodes(child, result, depth + 1);
            }
        }
    }

    public static String getScreenText() {
        if (instance != null) {
            instance.scanScreen();
        }
        return screenText;
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
                                        path, 0, 100))
                        .build();

        instance.dispatchGesture(gesture, null, null);
    }

    @Override
    public void onInterrupt() {
    }

    @Override
    public void onDestroy() {
        instance = null;
        super.onDestroy();
    }
}
