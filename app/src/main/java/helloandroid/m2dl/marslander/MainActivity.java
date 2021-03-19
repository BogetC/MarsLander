package helloandroid.m2dl.marslander;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {
    private Handler handler;
    private int counter_time;
    TextView counterTV;
    View menuLayout;

    private Runnable count = () -> {
        this.counter_time--;
        this.counterTV.setText(String.valueOf(this.counter_time));

        if (this.counter_time == 0) {
            this.fadeMenu();
        } else {
            this.handler.postDelayed(this.count, 1000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.handler = new Handler();
        this.counterTV = (TextView) findViewById(R.id.counter_text_view);
        this.menuLayout = (View) findViewById(R.id.menu_layout);
        this.startCounter();
    }

    public void startCounter() {
        this.counter_time = getResources().getInteger(R.integer.counter_max);
        this.counterTV.setText(String.valueOf(this.counter_time));
        this.handler.postDelayed(this.count, 1000);
    }

    public void startGame() {

    }

    private void fadeMenu() {
        this.menuLayout.animate()
                .alpha(0.0f)
                .setDuration(getResources().getInteger(R.integer.fade_out_duration))
                .setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                menuLayout.setVisibility(View.GONE);
                startGame();
            }
        });
    }
}