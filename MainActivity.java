package com.carrompool.bp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.miniclip.carrom.R;

public class MainActivity extends AppCompatActivity {

    private static View floatingButtonView = null;
    private static Activity currentActivity = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentActivity = this;
        //setContentView(R.layout.activity_main);

        // Invocare din MainActivity
        floatingbtn(this);
    }

    /**
     * Creează și afișează butonul floating draggable cu logo Telegram.
     * Poate fi invocat din orice altă clasă: MainActivity.floatingbtn(activity);
     *
     * @param activity Activity that hosts the draggable control.
     */
    public static void floatingbtn(Activity activity) {

        // Evită duplicate dacă metoda e apelată de mai multe ori
        if (floatingButtonView != null) return;

        ViewGroup content = activity.findViewById(android.R.id.content);
        if (content == null) return;

        // Keep the control inside this Activity so it needs no overlay token or permission.
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                Gravity.TOP | Gravity.START
        );
        params.leftMargin = dp(activity, 50);
        params.topMargin = dp(activity, 200);

        // ── Container principal ───────────────────────────────────────────────
        LinearLayout container = new LinearLayout(activity);
        container.setOrientation(LinearLayout.HORIZONTAL);
        container.setGravity(Gravity.CENTER_VERTICAL);
        container.setPadding(dp(activity, 24), dp(activity, 16), dp(activity, 28), dp(activity, 16));
        container.setElevation(dp(activity, 12));

        // Fundal rotunjit albastru Telegram
        container.setBackground(createRoundedBackground());

        // ── Offline heart icon ───────────────────────────────────────────────
        ImageView icon = new ImageView(activity);
        icon.setLayoutParams(new LinearLayout.LayoutParams(dp(activity, 72), dp(activity, 72)));
        icon.setScaleType(ImageView.ScaleType.FIT_CENTER);

        try {
            Bitmap bitmap = BitmapFactory.decodeResource(
                    activity.getResources(),
                    R.drawable.offline_heart
            );
            if (bitmap != null) {
                icon.setImageBitmap(bitmap);
            } else {
                icon.setBackgroundColor(0xFFFFFFFF);
            }
        } catch (Exception e) {
            icon.setBackgroundColor(0xFFFFFFFF);
        }

        // ── Text buton ────────────────────────────────────────────────────────
        TextView text = new TextView(activity);
        LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        textParams.setMargins(dp(activity, 16), 0, 0, 0);
        text.setLayoutParams(textParams);
        text.setText("DARK OWNER ADMIN SERVER");
        text.setTextColor(0xFFFFFFFF);
        text.setTextSize(14f);
        text.setTypeface(android.graphics.Typeface.DEFAULT_BOLD);

        container.addView(icon);
        container.addView(text);

        // ── Click → deschide link Telegram ───────────────────────────────────
        container.setOnClickListener(v -> {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://t.me/DARK_OWNER_VIP"));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // ── Drag logic (mișcare buton) ────────────────────────────────────────
        final int dragThresholdPx = dp(activity, 10);
        container.setOnTouchListener(new View.OnTouchListener() {
            private int initialX, initialY;
            private float initialTouchX, initialTouchY;
            private boolean isDragging = false;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        initialX = params.leftMargin;
                        initialY = params.topMargin;
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        isDragging = false;
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        float deltaX = event.getRawX() - initialTouchX;
                        float deltaY = event.getRawY() - initialTouchY;

                        if (!isDragging &&
                                (Math.abs(deltaX) > dragThresholdPx ||
                                        Math.abs(deltaY) > dragThresholdPx)) {
                            isDragging = true;
                        }

                        if (isDragging) {
                            int maxX = Math.max(0, content.getWidth() - container.getWidth());
                            int maxY = Math.max(0, content.getHeight() - container.getHeight());
                            params.leftMargin = Math.max(0, Math.min(maxX, initialX + (int) deltaX));
                            params.topMargin = Math.max(0, Math.min(maxY, initialY + (int) deltaY));
                            container.setLayoutParams(params);
                        }
                        return true;

                    case MotionEvent.ACTION_UP:
                        if (!isDragging) {
                            v.performClick(); // declanșează onClick
                        }
                        return true;
                }
                return false;
            }
        });

        // ── Adaugă butonul peste conținutul Activity-ului ─────────────────────
        content.addView(container, params);
        floatingButtonView = container;
    }



    /**
     * Enables/disables Android's secure-window flag so screenshots and
     * screen recordings cannot capture this Activity while enabled.
     */
    public static void setHideRecording(boolean enabled) {
        final Activity activity = currentActivity;
        if (activity == null) return;

        activity.runOnUiThread(() -> {
            try {
                if (enabled) {
                    activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
                } else {
                    activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_SECURE);
                }
            } catch (Throwable ignored) {}
        });
    }

    /**
     * Removes the draggable control from its Activity.
     */
    public static void removeFloatingBtn(Activity activity) {
        View view = floatingButtonView;
        if (view == null) return;

        if (view.getParent() instanceof ViewGroup) {
            ((ViewGroup) view.getParent()).removeView(view);
        }
        floatingButtonView = null;
    }

    @Override
    protected void onDestroy() {
        if (currentActivity == this) currentActivity = null;
        super.onDestroy();
        removeFloatingBtn(this);
    }

    private static int dp(Activity activity, int value) {
        return (int) (value * activity.getResources().getDisplayMetrics().density + 0.5f);
    }

    // ── Helper: fundal rotunjit albastru Telegram ─────────────────────────────
    private static android.graphics.drawable.Drawable createRoundedBackground() {
        android.graphics.drawable.GradientDrawable shape = new android.graphics.drawable.GradientDrawable();
        shape.setShape(android.graphics.drawable.GradientDrawable.RECTANGLE);
        shape.setCornerRadius(60f);
        shape.setColor(0xFFB00000); // login red
        return shape;
    }
}
