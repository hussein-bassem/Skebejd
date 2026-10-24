
package com.hussein.truckhelper;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.graphics.Path;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.util.Log;

public class GameAccessibilityService extends AccessibilityService {

    private static GameAccessibilityService instance;
    private static final String TAG = "TruckHelper";

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        instance = this;
        Log.d(TAG, "Accessibility service connected");
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (event == null) return;

        AccessibilityNodeInfo root = getRootInActiveWindow();
        if (root == null) {
            Log.d(TAG, "No accessible window");
            return;
        }

        StringBuilder result = new StringBuilder();
        readNodes(root, result, 0);

        Log.d(TAG, "Screen elements:\n" + result);
        root.recycle();
    }

    private void readNodes(
            AccessibilityNodeInfo node,
            StringBuilder result,
            int depth) {

        if (node == null || depth > 20) return;

        CharSequence text = node.getText();
        CharSequence description = node.getContentDescription();

        if ((text != null && text.length() > 0) ||
            (description != null && description.length() > 0)) {

            result.append(" ".repeat(depth))
                  .append("Text: ").append(text)
                  .append(" | Description: ").append(description)
                  .append(" | Clickable: ").append(node.isClickable())
                  .append('\n');
        }

        for (int i = 0; i < node.getChildCount(); i++) {
            AccessibilityNodeInfo child = node.getChild(i);
            if (child != null) {
                readNodes(child, result, depth + 1);
            }
        }
    }

    @Override
    public void onInterrupt() {
    }

    public static boolean isConnected() {
        return instance != null;
    }

    public static void testTap() {
        if (instance == null) return;

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
    public void onDestroy() {
        instance = null;
        super.onDestroy();
    }
}
