package guess.bobo.cn.guesswho.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import guess.bobo.cn.guesswho.R;
import guess.bobo.cn.guesswho.component.DashLineExitText;
import guess.bobo.cn.guesswho.service.MediaService;


/**
 * Created by Leon on 2018/6/2.
 * Functions: 用户填写名字的页面
 */
public class PlayerNameActivity extends Activity implements View.OnClickListener{

    private Button playerNameMenu;
    private Button playerNameOk;
    private Button playerNameSkip;
    private DashLineExitText playerName;

    /**
     * Find the Views in the layout<br />
     * <br />
     * Auto-created on 2018-06-03 13:11:44 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private void findViews() {
        playerNameMenu = (Button)findViewById( R.id.player_name_menu );
        playerNameOk = (Button)findViewById( R.id.player_name_ok );
        playerNameSkip = (Button)findViewById( R.id.player_name_skip );
        playerName = (DashLineExitText)findViewById(R.id.player_name);

        playerNameMenu.setOnClickListener( this );
        playerNameOk.setOnClickListener( this );
        playerNameSkip.setOnClickListener( this );
        playerName.setOnClickListener( this );
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player_name);
        findViews();
    }

    /**
     * 用户点击事件的处理
     */
    @Override
    public void onClick(View v) {

        //只要是用户点击了按钮就播放按钮的声音 不用每次都设置
        if (v instanceof Button){
            MediaService.play(this,R.raw.button);
        }

        if ( v == playerNameMenu ) {
            // 用户点击了菜单返回菜单页
            finish();

        } else if ( v == playerNameOk ) {
            // 用户点击了ok
            if (playerName == null || playerName.getText().toString().equals("")){
                Toast.makeText(this,"姓名不可为空",Toast.LENGTH_SHORT).show();
            }else if (playerName.getText().toString().length() > 7){
                Toast.makeText(this,"姓名长度不能超过7个字节",Toast.LENGTH_SHORT).show();
            }else {
                Intent intent = new Intent(this, TapToStartActivity.class);
                intent.putExtra("PLAYER", playerName.getText().toString());
                startActivity(intent);
                finish();
            }
        } else if ( v == playerNameSkip ) {
            // 用户点击了跳过
            if ( playerName.getText().toString().length() > 7){
                Toast.makeText(this,"姓名长度不能超过7个字节",Toast.LENGTH_SHORT).show();
            }else {
                Intent intent = new Intent(this, TapToStartActivity.class);
                intent.putExtra("PLAYER", playerName.getText().toString());
                startActivity(intent);
                finish();
            }

        }else if ( v == playerName){
            //用户点击了输入框这里先不做任何事在输出框类DashLineExitText中已经处理
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MediaService.play(this,R.raw.button);
    }
}
