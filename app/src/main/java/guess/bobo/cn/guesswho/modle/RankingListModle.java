package guess.bobo.cn.guesswho.modle;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Leon on 2018/7/3.
 * Functions: 分数排行榜模型
 */
public class RankingListModle {

    private List<String> playerNames;
    private List<Integer> playerScores;


    public RankingListModle() {
        this.playerNames = new ArrayList();
        this.playerScores = new ArrayList();
    }

    public List<String> getPlayerNames() {
        return playerNames;
    }

    public List<Integer> getPlayerScores() {
        return playerScores;
    }




}
