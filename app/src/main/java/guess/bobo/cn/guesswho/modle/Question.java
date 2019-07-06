package guess.bobo.cn.guesswho.modle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;


public class Question {


	private int questionId;
	private String question = "Who is this super star? (id = " + (questionId+=1) + ")";
	private String description = "He is famous for film \"kill bill\".";
	private List<String> tips = new ArrayList();
	private int level;
	private int personType;
	private List<String> wrongAnswers = new ArrayList();
	private String correctAnswer;
	private int correctAnswerIndex;

	public void setCorrectAnswerIndex(int correctAnswerIndex) {
		this.correctAnswerIndex = correctAnswerIndex;
	}

	public int getCorrectAnswerIndex() {
		return correctAnswerIndex;
	}

	public String getCorrectAnswer() {
		return correctAnswer;
	}

	public void setCorrectAnswer(String correctAnswer) {

		this.correctAnswer = correctAnswer;
	}

	public void setWrongAnswers(List<String> wrongAnswers) {
		this.wrongAnswers = wrongAnswers;
	}

	public List<String> getWrongAnswers() {
		return wrongAnswers;
	}

	public void setPersonType(int personType) {
		this.personType = personType;
	}

	public int getPersonType() {
		return personType;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}


	public List<String> getTips() {
		return tips;
	}


	public int getQuestionId() {
		return questionId;
	}

	//这个方法只能被调用一次，需改进。
	public List<String> getAllAnswers() {
		List<String> result = new ArrayList<String>(wrongAnswers);
		result.add(correctAnswer);
		Collections.shuffle(result);
		correctAnswerIndex = result.indexOf(correctAnswer);
		return result;
	}

	/**Leon增加退出MainViewActivity后设置为零重新开始游戏*/
	public void setQuestionId(int questionId) {
		this.questionId = questionId;
	}
}
