
package com.hussein.truckhelper;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    private TextView status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CENTER);
        layout.setPadding(28, 28, 28, 28);
        layout.setBackgroundColor(Color.rgb(18, 24, 38));

        TextView title = new TextView(this);
        title.setText("🚛 Truck Helper");
        title.setTextSize(30);
        title.setTextColor(Color.WHITE);
        title.setGravity(Gravity.CENTER);
        layout.addView(title);

        status = new TextView(this);
        status.setText("المساعد جاهز");
        status.setTextSize(18);
        status.setTextColor(Color.LTGRAY);
        status.setGravity(Gravity.CENTER);
        status.setPadding(10, 30, 10, 30);
        layout.addView(status);

        Button start = new Button(this);
        start.setText("بدء جلسة");
        layout.addView(start);

        start.setOnClickListener(v -> {
            status.setText("الجلسة بدأت — وضع الاختبار");
            Toast.makeText(this,
                "Truck Helper يعمل",
                Toast.LENGTH_SHORT).show();
        });

        Button info = new Button(this);
        info.setText("معلومات المساعد");
        layout.addView(info);

        info.setOnClickListener(v ->
            status.setText(
                "الإصدار 1.0\n" +
                "قراءة الشاشة والتحكم لم يُفعّلا بعد."
            )
        );

        setContentView(layout);
    }
}
