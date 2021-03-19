package helloandroid.m2dl.marslander;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;

public class ImageTestActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(new GameView(this));
    }
}
