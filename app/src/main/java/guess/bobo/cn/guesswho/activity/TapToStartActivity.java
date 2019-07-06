package guess.bobo.cn.guesswho.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import guess.bobo.cn.guesswho.R;
import guess.bobo.cn.guesswho.service.MediaService;
import guess.bobo.cn.guesswho.utils.LogUtil;


/**
 * Created by Leon on 2018/6/9.
 * Functions:
 */
public class TapToStartActivity extends Activity {

    private TextView hiTitle;
    private TextView tapText;
    private TextView questionText;
    private TextView tipText;
    private Button hiTipButton;
    private ImageView ansButton;
    private ImageView skipButton;

    private static final int MSG_ID_TIP_ANIM = 1;
    private static final int MSG_ID_ANS_BUTTON_ANIM = 2;
    private static final int MSG_ID_SKIP_BUTTON_ANIM = 3;
    private static final int MSG_ID_TAP_TO_START_ANIM = 4;
    private static final int MSG_ID_START_GAME = 5;

    private boolean isStartedGame = false;
    private boolean isMoveBack = false;
    private String playName;

    private Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what){
                case MSG_ID_TIP_ANIM:
                    tipText.setVisibility(View.VISIBLE);
                    Animation tipAnim = AnimationUtils.loadAnimation(TapToStartActivity.this,R.anim.tip_animation);
                    tipAnim.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            tipText.setVisibility(View.VISIBLE);
                            questionText.setVisibility(View.GONE);
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            tipText.setVisibility(View.GONE);
                            questionText.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    tipText.startAnimation(tipAnim);
                    sendEmptyMessageDelayed(MSG_ID_TIP_ANIM,6000);
                    break;
                case MSG_ID_ANS_BUTTON_ANIM :

                    if (isMoveBack == false){
                        Animation anim = new TranslateAnimation(
                                TranslateAnimation.RELATIVE_TO_PARENT,0.0f,
                                TranslateAnimation.RELATIVE_TO_PARENT,0.828f,
                                TranslateAnimation.RELATIVE_TO_PARENT,0.0f,
                                TranslateAnimation.RELATIVE_TO_PARENT,0.0f
                        );
                        anim.setRepeatCount(0);
                        anim.setDuration(3000);
                        ansButton.startAnimation(anim);
                        isMoveBack = true;
                    }else {
                        Animation anim2 = new TranslateAnimation(
                                TranslateAnimation.RELATIVE_TO_PARENT,0.828f,
                                TranslateAnimation.RELATIVE_TO_PARENT,0.0f,
                                TranslateAnimation.RELATIVE_TO_PARENT,0.0f,
                                TranslateAnimation.RELATIVE_TO_PARENT,0.0f
                        );
                        anim2.setRepeatCount(0);
                        anim2.setDuration(3000);
                        ansButton.startAnimation(anim2);
                        isMoveBack = false;
                    }

                    sendEmptyMessageDelayed(MSG_ID_ANS_BUTTON_ANIM,3000);
                    break;
                case MSG_ID_SKIP_BUTTON_ANIM:
                    Animation skipAnim = new AlphaAnimation(0.3f,1.0f);
                    skipAnim.setDuration(2000);
                    skipAnim.setRepeatMode(AlphaAnimation.REVERSE);
                    skipButton.startAnimation(skipAnim);
                    sendEmptyMessageDelayed(MSG_ID_SKIP_BUTTON_ANIM,3000);
                    break;
                case MSG_ID_TAP_TO_START_ANIM:
                    Animation tapAnim = new AlphaAnimation(0.5f,1.0f);
                    tapAnim.setDuration(1600);
                    tapText.startAnimation(tapAnim);
                    sendEmptyMessageDelayed(MSG_ID_TAP_TO_START_ANIM,2000);
                    break;
                case MSG_ID_START_GAME:
                    startGame();
                    finish();
            }
        }
    };


    /**
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private void findViews() {
        hiTitle = (TextView)findViewById( R.id.hi_title );
        tapText = (TextView)findViewById( R.id.hi_tap_to_start );
        questionText = (TextView)findViewById( R.id.hi_sample_question );
        tipText = (TextView)findViewById( R.id.hi_tip_msg );
        hiTipButton = (Button)findViewById( R.id.hi_tip_button );
        ansButton = (ImageView)findViewById( R.id.hi_ans_button );
        skipButton = (ImageView)findViewById( R.id.hi_skip_button );
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tap_to_start);
        findViews();

        playName = getIntent().getStringExtra("PLAYER");
        if (playName.length() == 0 || playName.equals("")){
            playName = "没有名字的用户";
        }

        String hi = String.format(hiTitle.getText().toString(),playName);
        hiTitle.setText(hi);

        handler.sendEmptyMessageDelayed(MSG_ID_TIP_ANIM,3000);
        handler.sendEmptyMessage(MSG_ID_ANS_BUTTON_ANIM);
        handler.sendEmptyMessage(MSG_ID_SKIP_BUTTON_ANIM);
        handler.sendEmptyMessage(MSG_ID_TAP_TO_START_ANIM);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (isStartedGame == false){
            isStartedGame = true;
            MediaService.play(this,R.raw.slide);
            handler.sendEmptyMessage(MSG_ID_START_GAME);
        }

        return true;
    }

    //等着写这里可能会用到 playName 已经设置为全局变量请放心使用
    private void startGame() {
        Intent intent = new Intent(this,MainViewActivity.class);
        intent.putExtra("PLAYER_MAME",playName);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {

        if (handler != null){
            handler.removeMessages(MSG_ID_TIP_ANIM);
            handler.removeMessages(MSG_ID_ANS_BUTTON_ANIM);
            handler.removeMessages(MSG_ID_SKIP_BUTTON_ANIM);
            handler.removeMessages(MSG_ID_TAP_TO_START_ANIM);
            handler.removeMessages(MSG_ID_START_GAME);
            handler = null;
            LogUtil.e("Activity销毁的时候移除了Handler");
        }

        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MediaService.play(this,R.raw.button);
    }
}
