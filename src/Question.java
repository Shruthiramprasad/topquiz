package project;

import javax.swing.*;
import javax.swing.ImageIcon;
/**
 * Represents a single question in a quiz game.
 */
public class Question {
    private final int questionNumber;
    private final String question;

    private String answer_a;
    private String answer_b;
    private String answer_c;
    private String answer_d;
    private String correct_answer;
    private ImageIcon imageIcon;

    /**
     * Constructs a new Question object with the specified parameters.
     *
     * @param questionNumber the number of the question in the quiz
     * @param question the text of the question
     * @param answer_a the text of choice A for the question
     * @param answer_b the text of choice B for the question
     * @param answer_c the text of choice C for the question
     * @param answer_d the text of choice D for the question
     * @param correct_answer the text of the correct answer for the question
     * @param imageIcon the ImageIcon object representing an image associated with the question
     */



    public Question(int questionNumber,String question, String answer_a, String answer_b, String answer_c, String answer_d, String correct_answer, ImageIcon imageIcon) {
        this.questionNumber = questionNumber;
        this.question = question;
        this.imageIcon = imageIcon;
        this.answer_a = answer_a;
        this.answer_b = answer_b;
        this.answer_c = answer_c;
        this.answer_d = answer_d;
        this.correct_answer = correct_answer;
    }
    /**
     * Returns the question number of this Question object.
     *
     * @return the question number
     */
    public int getQuestionNumber() {
        return questionNumber;
    }
    /**
     * Returns the text of the question of this Question object.
     *
     * @return the text of the question
     */
    public String getPrompt() {
        return question;
    }
    /**
     * Returns the text of choice A for this Question object.
     *
     * @return the text of choice A
     */
    public String getChoiceA() {
        return answer_a;
    }
    /**
     * Returns the text of choice B for this Question object.
     *
     * @return the text of choice B
     */
    public String getChoiceB() {
        return answer_b;
    }
    /**
     * Returns the text of choice C for this Question object.
     *
     * @return the text of choice C
     */
    public String getChoiceC() {
        return answer_c;
    }
    /**
     * Returns the text of choice D for this Question object.
     *
     * @return the text of choice D
     */
    public String getChoiceD() {
        return answer_d;
    }
    /**
     * Returns the text of the correct answer for this Question object.
     *
     * @return the text of the correct answer
     */
    public String getAnswer() {
        return correct_answer;
    }
    /**
     * Returns the ImageIcon object representing an image associated with this Question object.
     *
     * @return the ImageIcon object representing an image
     */
    public ImageIcon getImageIcon() {
        return imageIcon;
    }

}
