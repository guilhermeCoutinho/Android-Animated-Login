package coutinho.guilherme.animatedloginform;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Scene;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FullscreenActivity extends AppCompatActivity implements  View.OnClickListener  {
    enum Scenes {
        BOTH ,
        LOGIN,
        SIGN_UP
    }
    private static int TARGET_WEIGHT = 20;
    Scenes showingScene;
    View decorView;
    View login;
    View signup;
    View collapsed;
    View expanded;
    TextView signUptv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        makeFullScreen();
        UiChangeListener();
        setContentView(R.layout.activity_fullscreen);
        showingScene = Scenes.BOTH;
        login =  findViewById(R.id.login_fragment);
        signup = findViewById(R.id.signup_fragment);
        signUptv = (TextView) findViewById(R.id.login_activity_signup_tv);
        login.setOnClickListener(this);
        signup.setOnClickListener(this);
        decorView = getWindow().getDecorView();
    }
    /*
        Must be the first called method inside onCreate;
     */
    void makeFullScreen () {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    public void onClick(View v) {
        switchScenes(v);
    }

    void switchScenes (View v) {
        if (showingScene == getViewType(v))
            return;
        collapsed = v;
        expanded = v.getId()==signup.getId()? login : signup;
        showingScene = getViewType(v);
        LinearLayout.LayoutParams params;
        params =(LinearLayout.LayoutParams)expanded.getLayoutParams();
        params.weight = 1;
        expanded.setLayoutParams(params);
        params =(LinearLayout.LayoutParams)collapsed.getLayoutParams();
        params.weight = TARGET_WEIGHT;
        collapsed.setLayoutParams(params);

    }

    Scenes getViewType (View v) {
        if (v.getId()==login.getId())
            return Scenes.LOGIN;
        return Scenes.SIGN_UP;
    }


    public void UiChangeListener() {
        final View decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener (new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                    decorView.setSystemUiVisibility(
                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
                }
            }
        });
    }

    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

}
