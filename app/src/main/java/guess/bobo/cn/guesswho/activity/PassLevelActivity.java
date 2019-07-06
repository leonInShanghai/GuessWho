package guess.bobo.cn.guesswho.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.widget.TextView;

import guess.bobo.cn.guesswho.R;
import guess.bobo.cn.guesswho.utils.Constants;

/**
 * Created by Leon on 2018/6/18.
 * Functions: 关卡完成跳转activity
 */
public class PassLevelActivity extends Activity {


    private TextView passLevelTitle;
    private TextView PassLevelPrompt;
    private TextView PassLevelClick;

    /**
     * Auto-created on 2018-06-18 14:07:18 by Android Layout Finder
     */
    private void findViews() {
        passLevelTitle = (TextView)findViewById( R.id.passLevelTitle );
        PassLevelPrompt = (TextView)findViewById( R.id.PassLevelPrompt );
        PassLevelClick = (TextView)findViewById( R.id.PassLevelClick );
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.passlevel);
        findViews();

        //传递过来的游戏等级
        int level = getIntent().getIntExtra("LEVEL",-1);
        String playName = getIntent().getStringExtra("PLAYER_NAME");

        //设置用户的姓名
        String name = String.format(passLevelTitle.getText().toString(),playName);
        passLevelTitle.setText(name);

        //通过关卡的文本
        switch (level) {
            case Constants.CardLimitNumber1:
                PassLevelPrompt.setText(R.string.PassLevelPrompt1);
                break;
            case Constants.CardLimitNumber2:
                PassLevelPrompt.setText(R.string.PassLevelPrompt2);
                break;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            finish();
            return true;
        }

        return super.onTouchEvent(event);
    }
}
