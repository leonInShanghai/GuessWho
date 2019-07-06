package guess.bobo.cn.guesswho.modle;



import java.util.ArrayList;

import java.util.List;


import guess.bobo.cn.guesswho.utils.Constants;
import guess.bobo.cn.guesswho.utils.LogUtil;


public class OneRoundGame {

	private String playerName;
	private int score = 0;
	private int level = 0;
	private int wrongAnsCnt = 0;
	private int correctAnsCnt = 0;
	private int correctAnsCntForCurrentLevel = 0;
	// 用户已经使用的提示次数
	private int tipUsedCnt = 0;
	// 当前问题的提示序号
	private int tipUsedCntForCurrentQuestion = 0;
	private boolean isFinish = false;

	//是否进入下一关
	private boolean isLevelUp = false;
	private int timeUsedInSeconds = 0;
	private int currentQuestionId = 0;
	private List<Question> questions = new ArrayList<Question>();

	public static final int LEVEL_1_CNT = 10;
	public static final int LEVEL_2_CNT = 20;
	public static final int LEVEL_3_CNT = 30;


	/**第1关的问题个数*/
	public static final int LEVEL_1_COUNT = 10;

	/**第2关的问题个数*/
	public static final int LEVEL_2_COUNT = 10;

	/**第3关的问题个数*/
	public static final int LEVEL_3_COUNT = 10;


	// 游戏是否暂停
	private boolean isPaused;

	public boolean isPaused() {
		return isPaused;
	}

	public Question getCurrentQuestion() {
		return questions.get(currentQuestionId);
	}

	//问题的个数Leon新增加
	public int getQuestionCount(){
		return questions.size();
	}

	public void setPaused(boolean isPaused) {
		this.isPaused = isPaused;
	}

	public OneRoundGame(String player) {
		playerName = player;
	}

	public void fillQuestions(Question question){
		questions.add(question);
	}

	public void increaseTimeUsed() {
		timeUsedInSeconds++;
	}

	public int getTimeUsedInMin() {
		return timeUsedInSeconds / 60;
	}

	public int getTimeUsedInSec() {
		return timeUsedInSeconds % 60;
	}

	public String getPlayerName() {
		return playerName;
	}

	public int getScore() {
		return score;
	}

	public int getLevel() {
		return level;
	}

	public int getWrongAnsCnt() {
		return wrongAnsCnt;
	}

	public int getCorrectAnsCnt() {
		return correctAnsCnt;
	}

	//返回积分数
	public int getCorrectAnsCntForCurrentLevel() {
		return correctAnsCntForCurrentLevel;
	}

	//返回用户使用提示的次数
	public int getTipUsedCnt() {
		return tipUsedCnt;
	}

	public int getTipUsedCntForCurrentQuestion() {
		return tipUsedCntForCurrentQuestion;
	}

	//判断游戏是否结束用户打错几道题结束目前是答错2道题就让用户结束游戏
	public boolean isFinish() {

	    //LEVEL_1_CNT-1 调试阶段也就是2
	    if (wrongAnsCnt >= 2){
	        return true;
        }
		return isFinish;
	}

	public boolean isLevelUp() {

		//用戶第一關 并且 答對了LEVEL_1_CNT道題 升級
		if (level == (Constants.CardLimitNumber1 - 1) && correctAnsCntForCurrentLevel == LEVEL_1_CNT) {
			level++;
			return true;
		}
		//用戶第二關 并且 答對了LEVEL_1_CNT道題 升級
		if (level == (Constants.CardLimitNumber2 - 1) && correctAnsCntForCurrentLevel == LEVEL_2_CNT) {
			level++;
			return true;
		}
		return false;
		//return isLevelUp;
	}



	// 处理用户的答案是否正确-并计算分数
	public boolean userSelectAnswer(int useSelectedAnswerIndex) {
		if (useSelectedAnswerIndex == this.getCurrentQuestion()
				.getCorrectAnswerIndex()) {
			// 一个简单的积分规则
			correctAnsCntForCurrentLevel++;
			score += 72;
			return true;
		}else
		{
		    wrongAnsCnt++;
			score -= 80;
			if (score < 0)
			{
				score = 0;
			}
			return false;
		}
	}

	//下一题
	public boolean nextQuestion() {
		tipUsedCntForCurrentQuestion = 0;

		currentQuestionId++;

		//Leon避免数组越界
		if (currentQuestionId >= questions.size()){


			currentQuestionId = 0;
			return true;
		}
		return false;
	}

	//下一题的索引
	public int nextQuestionCount() {
		return currentQuestionId;
	}

	public String getCurrentTip() {
		if (tipUsedCnt > 2){
			return "提示次数用完了";
		}else if (tipUsedCntForCurrentQuestion <= 1) {
			return this.getCurrentQuestion().getTips().get(tipUsedCntForCurrentQuestion);
		} else {
			return "一个问题最多提示两次";
		}
	}

	public void addTipCnt() {

	    //一道题最多使用2次提示-每更新一道题这个变量会置零
		if (tipUsedCntForCurrentQuestion <= 1){

			    //用户最多有3次机会
				tipUsedCnt++;
		}

		//这里++当它等一的时候后面会处理
		tipUsedCntForCurrentQuestion++;
	}
	

}
