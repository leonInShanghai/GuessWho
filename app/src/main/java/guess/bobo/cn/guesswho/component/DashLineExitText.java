package guess.bobo.cn.guesswho.component;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Leon on 2018/6/3.
 * Functions: 自定义输入框组件
 */

@SuppressLint("AppCompatCustomView")
public class DashLineExitText extends EditText{

    private static final int SEGENT_CNT = 9;
    private static final int GAP = 2;

    public DashLineExitText(Context context) {
        this(context,null);
    }

    public DashLineExitText(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public DashLineExitText(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr,0);
    }

    @SuppressLint("NewApi")
    public DashLineExitText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.setFocusable(true);
        this.setFocusableInTouchMode(true);
        this.requestFocus();




        this.setOnEditorActionListener(new OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                return false;
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);
        int height = getHeight();
        int width = getWidth();
        int segWidth = (width + GAP) / SEGENT_CNT - GAP;
        for (int i = 0;i < SEGENT_CNT;i++){
            canvas.drawRect(i * (segWidth + GAP),height - 1,i * (segWidth + GAP) + segWidth,height,paint);
        }
    }

    //调起键盘的方法暂时没有用到
    public void showKeyboard() {

            //设置可获得焦点
            this.setFocusable(true);
            this.setFocusableInTouchMode(true);
            //请求获得焦点
            this.requestFocus();
            //调用系统输入法
            InputMethodManager inputManager = (InputMethodManager) this
                    .getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.showSoftInput(this, 0);

    }
}
