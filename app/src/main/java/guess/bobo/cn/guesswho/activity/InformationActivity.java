package guess.bobo.cn.guesswho.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import guess.bobo.cn.guesswho.R;
import guess.bobo.cn.guesswho.service.MediaService;

/**
 * Created by Leon on 2018/6/8.
 * Functions:
 */
public class InformationActivity extends Activity implements View.OnClickListener{

    private Button information_mainmenu;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.information);
        information_mainmenu = (Button)findViewById(R.id.information_mainmenu);
        information_mainmenu.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == information_mainmenu){
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
