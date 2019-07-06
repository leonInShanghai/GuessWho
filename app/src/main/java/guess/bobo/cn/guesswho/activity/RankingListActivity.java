package guess.bobo.cn.guesswho.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import java.util.Map;

import guess.bobo.cn.guesswho.R;
import guess.bobo.cn.guesswho.modle.RankingListModle;
import guess.bobo.cn.guesswho.service.MediaService;
import guess.bobo.cn.guesswho.utils.HighScoreDB;
import guess.bobo.cn.guesswho.utils.LogUtil;


/**
 * Created by Leon on 2018/7/3.
 * Functions: 分数排行榜
 */
public class RankingListActivity extends Activity {


    private TextView lineOneName;
    private TextView lineOneScore;
    private TextView lineOnePosition;
    private TextView lineTwoName;
    private TextView lineTwoScore;
    private TextView lineTwoPosition;
    private TextView lineThreeName;
    private TextView lineThreeScore;
    private TextView lineThreePosition;
    private TextView nodataye;

    /**
     * Find the Views in the layout<br />
     * <br />
     * Auto-created on 2018-07-03 15:31:11 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private void findViews() {
        lineOneName = (TextView)findViewById( R.id.line_one_name );
        lineOneScore = (TextView)findViewById( R.id.line_one_score );
        lineOnePosition = (TextView)findViewById( R.id.line_one_position );
        lineTwoName = (TextView)findViewById( R.id.line_two_name );
        lineTwoScore = (TextView)findViewById( R.id.line_two_score );
        lineTwoPosition = (TextView)findViewById( R.id.line_two_position );
        lineThreeName = (TextView)findViewById( R.id.line_three_name );
        lineThreeScore = (TextView)findViewById( R.id.line_three_score );
        lineThreePosition = (TextView)findViewById( R.id.line_three_position );
        nodataye = (TextView)findViewById( R.id.nodataye );
    }




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ranking_list);

        findViews();
    }


    //onResume：与onPause配对，表示Activity已经创建完成
    @Override
    protected void onResume() {

        RankingListModle rankingModle = HighScoreDB.getHighestAll(this);


        if (rankingModle.getPlayerNames() != null && rankingModle.getPlayerNames().size() > 0) {
            //数据库中有数据隐藏提示
            nodataye.setVisibility(View.GONE);

            //第一名处理
            String playName1 = rankingModle.getPlayerNames().get(0);
            if (playName1 != null && playName1.length() != 0) {
                lineOneName.setText(playName1);
                lineOneScore.setText(String.valueOf(rankingModle.getPlayerScores().get(0)));
                lineOnePosition.setText("1");
            }

            if ( rankingModle.getPlayerNames().size() > 1) {
                //第2名处理
                String playName2 = rankingModle.getPlayerNames().get(1);
                if (playName2 != null && playName2.length() != 0) {
                    lineTwoName.setText(playName2);
                    lineTwoScore.setText(String.valueOf(rankingModle.getPlayerScores().get(1)));
                    lineTwoPosition.setText("2");
                }
            }

            if ( rankingModle.getPlayerNames().size() > 2) {
                //第3名处理
                String playName3 = rankingModle.getPlayerNames().get(2);
                if (playName3 != null && playName3.length() != 0) {
                    lineThreeName.setText(playName3);
                    lineThreeScore.setText(String.valueOf(rankingModle.getPlayerScores().get(2)));
                    lineThreePosition.setText("3");
                }
            }

        }else {
            //数据库中没有数据提示用户
            nodataye.setVisibility(View.VISIBLE);
        }

        super.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MediaService.play(this,R.raw.button);
    }


}
