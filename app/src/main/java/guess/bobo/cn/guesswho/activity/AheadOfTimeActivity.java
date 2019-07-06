package guess.bobo.cn.guesswho.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.widget.TextView;

import guess.bobo.cn.guesswho.R;
import guess.bobo.cn.guesswho.service.MediaService;

/**
 * Created by Leon on 2018/7/3.
 * Functions: 用户提前手动结束或者打错题被迫结束
 */
public class AheadOfTimeActivity extends Activity {

    private TextView aheadTitle;
    private boolean isTouchScreen = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aheadoftime);

        aheadTitle = (TextView)findViewById(R.id.aheadTitle);

        String playName =  getIntent().getStringExtra("PLAYER_MAME");
        String hi = String.format(aheadTitle.getText().toString(),playName);
        aheadTitle.setText(hi);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (isTouchScreen){
            //保证用户只能点击一次避免调用2次
            isTouchScreen = false;

            //播放音效
            MediaService.play(this, R.raw.button);

            //用户点击屏幕后进入排行榜
            Intent intent = new Intent(this,PlayerNameActivity.class);
            startActivity(intent);

            //关闭本页面
            finish();
        }


        return super.onTouchEvent(event);
    }


}
