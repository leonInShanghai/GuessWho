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
 * Functions:用户答题结束跳转页面
 */
public class UselessActivity extends Activity{

    private TextView gameOverTitle;
    private boolean isTouchScreen = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.useless);
        //tipText = (TextView) findViewById(R.id.mainTipText);
        gameOverTitle = (TextView)findViewById(R.id.gameOverTitle);

        String playName =  getIntent().getStringExtra("PLAYER_MAME");
        String hi = String.format(gameOverTitle.getText().toString(),playName);
        gameOverTitle.setText(hi);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (isTouchScreen){
            //保证用户只能点击一次避免调用2次
            isTouchScreen = false;

            //播放音效
            MediaService.play(this, R.raw.button);

            //用户点击屏幕后进入排行榜
            Intent intent = new Intent(this,RankingListActivity.class);
            startActivity(intent);

            //关闭本页面
            finish();
        }

        return super.onTouchEvent(event);
    }
}
