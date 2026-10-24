
package com.hussein.truckhelper;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CENTER);
        layout.setPadding(28, 28, 28, 28);
        layout.setBackgroundColor(Color.rgb(18, 24, 38));

        TextView title = new TextView(this);
        title.setText("Truck Helper 🚛");
        title.setTextSize(28);
        title.setTextColor(Color.WHITE);
        layout.addView(title);

        TextView info = new TextView(this);
        info.setText("فعّل الدائرة العائمة لتظهر فوق اللعبة.");
        info.setTextColor(Color.LTGRAY);
        info.setPadding(0, 24, 0, 24);
        layout.addView(info);

        Button start = new Button(this);
        start.setText("تفعيل الدائرة العائمة");
        layout.addView(start);

        start.setOnClickListener(v -> {
            if (!Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName())
                );
                startActivity(intent);
            } else {
                startService(new Intent(this, OverlayService.class));
                info.setText("الدائرة العائمة مفعّلة.");
            }
        });

        setContentView(layout);
    }
}
  
