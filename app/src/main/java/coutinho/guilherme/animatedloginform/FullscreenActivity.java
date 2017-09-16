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

public class FullscreenActivity extends AppCompatActivity implements  View.OnClickListener,
        ValueAnimator.AnimatorUpdateListener , Animator.AnimatorListener{
    enum Scenes {
        BOTH ,
        FREE,
        PREMIUM
    }
    private final static int FINAL_WEIGHT = 25;
    private final static int DURATION = 100;
    Scenes showingScene;
    View decorView;
    LinearLayout free;
    LinearLayout premium;
    LinearLayout collapsed;
    LinearLayout expanded;
    LinearLayout.LayoutParams collapsed_params;
    LinearLayout.LayoutParams expanded_params;
    TextView signUptv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        makeFullScreen();
        setContentView(R.layout.activity_fullscreen);
        showingScene = Scenes.BOTH;
        free = (LinearLayout) findViewById(R.id.login_activity_free_Ll);
        premium = (LinearLayout) findViewById(R.id.login_activity_premium_Ll);
        signUptv = (TextView) findViewById(R.id.login_activity_signup_tv);
        free.setOnClickListener(this);
        premium.setOnClickListener(this);
        decorView = getWindow().getDecorView();
    }
    /*
        Must be the first called method inside onCreate;
     */
    void makeFullScreen () {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    public void onClick(View v) {
        switchScenes((LinearLayout) v);
    }

    void switchScenes (LinearLayout v) {
        if (showingScene == getViewType(v))
            return;
        collapsed = v;
        expanded = v.getId()==free.getId()? premium : free;
        updateLayoutParams ();
        animateWeight();
        showingScene = getViewType(v);
    }

    boolean wasShowingBoth ;
    void animateWeight(){
        ValueAnimator valueAnimator ;
        valueAnimator = ValueAnimator.ofFloat(expanded_params.weight, FINAL_WEIGHT);
        valueAnimator.setDuration(DURATION);
        valueAnimator.setInterpolator(new DecelerateInterpolator(2f));
        valueAnimator.addUpdateListener(this);
        valueAnimator.addListener(this);
        wasShowingBoth = showingScene == Scenes.BOTH;
        valueAnimator.start();
    }

    void updateLayoutParams () {
        collapsed_params =  (LinearLayout.LayoutParams) collapsed.getLayoutParams();
        expanded_params =  (LinearLayout.LayoutParams) expanded.getLayoutParams();

        float aux = collapsed_params.weight;
        collapsed_params.weight = expanded_params.weight;
        expanded_params.weight = aux;

        collapsed.setLayoutParams(collapsed_params);
        expanded.setLayoutParams(expanded_params);
    }

    @Override
    public void onAnimationUpdate(ValueAnimator valueAnimator) {
        float value = (float) valueAnimator.getAnimatedValue();
        collapsed_params.weight = value;
        collapsed.setLayoutParams(collapsed_params);
        if (!wasShowingBoth){
            expanded_params.weight = FINAL_WEIGHT+1-value;
            expanded.setLayoutParams(expanded_params);
        }
    }

    @Override
    public void onAnimationStart(Animator animator) {

    }

    @Override
    public void onAnimationEnd(Animator animator) {
        if (showingScene == Scenes.FREE)
            signUptv.setTextColor( ContextCompat.getColor(this, R.color.dark_text_color));
        else
            signUptv.setTextColor( ContextCompat.getColor(this, R.color.light_text_color));
    }

    @Override
    public void onAnimationCancel(Animator animator) {

    }

    @Override
    public void onAnimationRepeat(Animator animator) {

    }

    Scenes getViewType (View v) {
        if (v.getId()==free.getId())
            return Scenes.FREE;
        return Scenes.PREMIUM;
    }

    @Override
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
