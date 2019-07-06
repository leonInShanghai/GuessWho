package guess.bobo.cn.guesswho.component;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import guess.bobo.cn.guesswho.R;
import guess.bobo.cn.guesswho.service.MediaService;

/**
 * Created by Leon on 2018/6/30.
 * Functions:
 */
@SuppressLint("AppCompatCustomView")
public class SlidingButton extends ImageView {

    float offset;
    int maxX = 637;
    int minX = 150;
    int selectLimit = 130;
    private boolean isTouched;


    public boolean isTouched() {
        return isTouched;
    }

    public SlidingButton(Context context) {
        super(context);
    }

    public SlidingButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SlidingButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @SuppressLint("NewApi")
    public SlidingButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    //----------------------------------------------------------------------------------------------

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            isTouched = true;
            offset = event.getX() + minX;
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            isTouched = false;
            offset = minX;
        }
//        return false;
        return super.onTouchEvent(event);
    }



    public boolean handleParentTouchEvent(Context context, MotionEvent parentEvent) {
        if (!isTouched) {
            return false;
        }
        boolean isHandled = false;
        if (isTouched) {

            int newX = (int) (parentEvent.getX() - offset);

            if (newX > maxX) {
                newX = maxX;
            }
            if (newX < 0) {
                newX = 0;
            }

            if ((parentEvent.getAction() == MotionEvent.ACTION_UP)) {

                if (newX > selectLimit) {
                    if (newX > selectLimit) {
                        isHandled = true;
                        //MediaService.play(context,R.raw.slide);
                        isTouched = false;
                        rePosition();

                    }
                }else
                {
                    // 手势抬起 MotionEvent.ACTION_UP
                    LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) getLayoutParams();
                    TranslateAnimation trans = new TranslateAnimation(
                            Animation.RELATIVE_TO_SELF, 0, Animation.ABSOLUTE,
                            -lp.leftMargin , Animation.RELATIVE_TO_SELF, 0,
                            Animation.RELATIVE_TO_SELF, 0);
                    trans.setStartOffset(0);
                    trans.setDuration(500);
                    trans.setFillBefore(true);
                    isTouched = false;
                    //rePosition();
                    trans.setAnimationListener(new
                            SlidingAnimationListener(this));
                    startAnimation(trans);
                    // setSoundPlayed(false);
                }


            } else {
                //在拖动
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams)
                        this
                                .getLayoutParams();
                lp.leftMargin = (int) (newX);
                this.setLayoutParams(lp);

            }
        }
        return isHandled;
    }

    public void rePosition() {
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) getLayoutParams();
        lp.leftMargin = 0;
        setLayoutParams(lp);
    }
    private class SlidingAnimationListener implements Animation.AnimationListener {

        SlidingButton button;
        public SlidingAnimationListener(SlidingButton slidingButton) {
            button = slidingButton;
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            button.rePosition();

        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }

        @Override
        public void onAnimationStart(Animation animation) {

        };

    }

}
