package coutinho.guilherme.animatedloginform;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.TransitionManager;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;

public class FullscreenActivity extends AppCompatActivity implements  View.OnClickListener,
        ValueAnimator.AnimatorUpdateListener , Animator.AnimatorListener{
    enum Scenes {
        BOTH ,
        LOGIN,
        SIGN_UP
    }

    public static long TRANSITION_DURATION = 500;
    Scenes showingScene;
    ViewGroup rootLayout;
    View decorView;
    View loginContainer;
    View signupContainer;
    View collapsed;
    View expanded;
    LinearLayout.LayoutParams collapsed_params;
    LinearLayout.LayoutParams expanded_params;
    Button switchLoginBtn;
    Button switchSignupBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        makeFullScreen();
        UiChangeListener();
        setContentView(R.layout.activity_fullscreen);
        showingScene = Scenes.BOTH;
        loginContainer =  findViewById(R.id.login_fragment);
        signupContainer = findViewById(R.id.signup_fragment);
        rootLayout = (ViewGroup) findViewById(R.id.rootLayout);
        switchLoginBtn = (Button) findViewById(R.id.activity_fullscreen_switch_login);
        switchSignupBtn = (Button) findViewById(R.id.activity_fullscreen_switch_signup);

        switchLoginBtn.setOnClickListener(this);
        switchSignupBtn.setOnClickListener(this);
        loginContainer.setOnClickListener(this);
        signupContainer.setOnClickListener(this);
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
        if (showingScene != Scenes.BOTH && (v.getId()== signupContainer.getId() || v.getId()== loginContainer.getId()) )
            return;
        switchScenes(v);
        updateButtonVisibility();
    }

    void updateButtonVisibility () {
       // TransitionManager.beginDelayedTransition(rootLayout);
        switchSignupBtn.setVisibility(View.GONE);
        switchLoginBtn.setVisibility(View.GONE);
        if (showingScene == Scenes.LOGIN)
            switchSignupBtn.setVisibility(View.VISIBLE);
        else if (showingScene == Scenes.SIGN_UP)
            switchLoginBtn.setVisibility(View.VISIBLE);
    }

    void setContainerReferences (View v) {
        if (showingScene == Scenes.BOTH) {
            collapsed = v;
            expanded = (v.getId()== signupContainer.getId()) ? loginContainer : signupContainer;
        }else if (showingScene == Scenes.LOGIN){
            collapsed = signupContainer;
            expanded = loginContainer;
        }else {
            collapsed = loginContainer;
            expanded = signupContainer;
        }
    }

    void switchScenes (View v) {
        if (showingScene == getViewType(v))
            return;
        setContainerReferences(v);
        updateLayoutParams();
        animateWeight();
        showingScene = getViewType(v);
    }

    void updateLayoutParams () {
        collapsed_params =  (LinearLayout.LayoutParams) collapsed.getLayoutParams();
        expanded_params =  (LinearLayout.LayoutParams) expanded.getLayoutParams();
        collapsed.setLayoutParams(collapsed_params);
        expanded.setLayoutParams(expanded_params);
    }

    boolean wasShowingBoth ;
     void animateWeight(){
        ValueAnimator valueAnimator ;
        valueAnimator = ValueAnimator.ofFloat(0, 1);
        valueAnimator.setDuration(TRANSITION_DURATION);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(this);
        valueAnimator.addListener(this);
        wasShowingBoth = showingScene == Scenes.BOTH;
        valueAnimator.start();
    }

    @Override
    public void onAnimationUpdate(ValueAnimator valueAnimator) {
        float value = (float) valueAnimator.getAnimatedValue();
        collapsed_params.weight = value;
        collapsed.setLayoutParams(collapsed_params);
        expanded_params.weight = 1-value;
        expanded.setLayoutParams(expanded_params);
    }

    @Override
    public void onAnimationStart(Animator animator) {

    }

    @Override
    public void onAnimationEnd(Animator animator) {

    }

    @Override
    public void onAnimationCancel(Animator animator) {

    }

    @Override
    public void onAnimationRepeat(Animator animator) {

    }

    Scenes getViewType (View v) {
        if (v.getId()== switchLoginBtn.getId() || v.getId() == loginContainer.getId())
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
