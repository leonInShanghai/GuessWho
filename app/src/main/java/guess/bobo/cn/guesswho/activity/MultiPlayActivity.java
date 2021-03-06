package guess.bobo.cn.guesswho.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import guess.bobo.cn.guesswho.R;
import guess.bobo.cn.guesswho.service.MediaService;

/**
 * Created by Leon on 2018/7/4.
 */
public class MultiPlayActivity extends Activity implements View.OnClickListener {


    private Button multiplayMultiMenu;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.multiplay);

        multiplayMultiMenu = (Button)findViewById( R.id.multiplay_multi_menu );
        multiplayMultiMenu.setOnClickListener( this );
    }

    @Override
    public void onClick(View v) {
        if ( v == multiplayMultiMenu ) {
            //用户点击了菜单返回菜单
            MediaService.play(this,R.raw.button);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MediaService.play(this,R.raw.button);
    }

}
