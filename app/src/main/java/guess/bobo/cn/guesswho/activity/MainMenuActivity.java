package guess.bobo.cn.guesswho.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Map;

import guess.bobo.cn.guesswho.R;
import guess.bobo.cn.guesswho.service.MediaService;
import guess.bobo.cn.guesswho.service.PrefService;
import guess.bobo.cn.guesswho.service.SoundService;
import guess.bobo.cn.guesswho.utils.Constants;
import guess.bobo.cn.guesswho.utils.HighScoreDB;


/**
 * Created by Leon on 2018/6/2.
 * Functions: 首页-主菜单页面
 */
public class MainMenuActivity extends Activity implements View.OnClickListener{

    private SoundService sound;
    private Button mainMenuPlay;
    private Button mainMenuMultiPlay;
    private Button mainMenuOptions;
    private Button mainMenuScore;
    private Button mainMenuFacebook;
    private Button mainMenuMultiExit;
    private ImageView mainmenu_information;
    private TextView high_run;
    private PrefService pref;
    private TextView MainMenuPlayerName;
    private TextView MainMenuPayerScore;
    private TextView mainmenuPoints;
    private boolean isExit;


    /**
     * Auto-created on 2018-06-02 15:35:55 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private void findViews() {
        mainMenuPlay = (Button)findViewById( R.id.main_menu_play );
        mainMenuMultiPlay = (Button)findViewById( R.id.main_menu_multi_play );
        mainMenuOptions = (Button)findViewById( R.id.main_menu_options );
        mainMenuScore = (Button)findViewById( R.id.main_menu_score );
        mainMenuFacebook = (Button) findViewById( R.id.main_menu_facebook );
        mainMenuMultiExit = (Button)findViewById( R.id.main_menu_multi_exit );
        mainmenu_information = (ImageView)findViewById(R.id.mainmenu_information);
        high_run = (TextView)findViewById(R.id.high_run);

        MainMenuPlayerName = (TextView)findViewById( R.id.MainMenu_player_name );
        MainMenuPayerScore = (TextView)findViewById( R.id.MainMenu_payer_score );
        mainmenuPoints = (TextView)findViewById( R.id.mainmenu_points );

        mainMenuPlay.setOnClickListener( this );
        mainMenuMultiPlay.setOnClickListener( this );
        mainMenuOptions.setOnClickListener( this );
        mainMenuScore.setOnClickListener( this );
        mainMenuFacebook.setOnClickListener( this );
        mainMenuMultiExit.setOnClickListener( this );
        mainmenu_information.setOnClickListener(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);
        pref = new PrefService(this);
        //在app启动的第一个activity取出上次存储的语言设置一下不然会是中文（默认）
        pref.changeLocaleAccordingOptions(this,pref.LANGUAGE_OPTION[ pref.getLanguage(this)]);
        findViews();
    }

    private void recreateLanguage() {

        if (Constants.ISUPDATE){
            //刷新button的text适配国际化
            mainMenuMultiPlay.setText(R.string.mainmenu_multi_play);
            mainMenuFacebook.setText(R.string.mainmenu_facebook_fan_us);
            mainMenuScore.setText(R.string.mainmenu_score);
            mainMenuOptions.setText(R.string.mainmenu_options);
            mainMenuMultiExit.setText(R.string.mainmenu_exit);
            mainMenuPlay.setText(R.string.mainmenu_play);
            high_run.setText(R.string.mainmenu_best_score);
            mainmenuPoints.setText(R.string.mainmenu_points);
            Constants.ISUPDATE = false;
        }
    }

    //onPause：与onResume配对，表示Activity正在暂停，正常情况下，onStop接着就会被调用
    @Override
    protected void onPause() {
        super.onPause();
        //释放音响资源
        sound.release();
    }

    //onResume：与onPause配对，表示Activity已经创建完成
    @Override
    protected void onResume() {

        Map map = HighScoreDB.getHighestPoint(this);
        if (map != null && !map.isEmpty() && map.size() > 0){
            MainMenuPlayerName.setText((String)map.get("PLAYERNAME"));
            MainMenuPayerScore.setText(String.valueOf(map.get("POINTS")));
        }

        //创建音响服务类
        sound = new SoundService(this);
        recreateLanguage();
        super.onResume();
    }

    //点击事件的处理
    @Override
    public void onClick(View v) {

        //只要是用户点击了按钮就播放按钮的声音 不用每次都设置
        if (v instanceof Button || v instanceof ImageView){
            MediaService.play(this,R.raw.button);
        }

        //用户点击了开始
        if ( v == mainMenuPlay ) {
            Intent intent = new Intent(MainMenuActivity.this,PlayerNameActivity.class);
            startActivity(intent);

            // 用户点击了多人游戏
        } else if ( v == mainMenuMultiPlay ) {
            Intent intent = new Intent(MainMenuActivity.this,MultiPlayActivity.class);
            startActivity(intent);

            // 用户点击了选项
        } else if ( v == mainMenuOptions ) {
            Intent intent = new Intent(this,OptionsActivity.class);
            startActivity(intent);

            // 用户点击了分数
        } else if ( v == mainMenuScore ) {
            Intent intent = new Intent(MainMenuActivity.this,RankingListActivity.class);
            startActivity(intent);

            // 用户点击了微博
        } else if ( v == mainMenuFacebook ) {
            Intent intent = new Intent(MainMenuActivity.this,WeiboActivity.class);
            startActivity(intent);

            // 用户点击了退出
        } else if ( v == mainMenuMultiExit ) {
            finish();
        }else if ( v == mainmenu_information){
            Intent intent = new Intent(this,InformationActivity.class);
            startActivity(intent);
        }
    }

    //像其他软件一样连续点击2次退出
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (!isExit){
            //播放音效
            MediaService.play(this,R.raw.button);
                isExit = true;
                Toast.makeText(MainMenuActivity.this,"再按一次退出",Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isExit = false;
                    }
                },2000);
                return true;
            }
        return super.onKeyDown(keyCode, event);
    }


}




