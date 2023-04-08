package project;


import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import java.awt.event.*;
import java.util.*;
import javax.speech.*;
import javax.speech.synthesis.Synthesizer;
import javax.speech.synthesis.SynthesizerModeDesc;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javax.speech.Central;
import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.sql.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The QuizApp class represents a quiz game application.
 * It extends JFrame and implements ActionListener.
 */
public class QuizApp extends JFrame implements ActionListener {
    /** The amount of time per question, in seconds. */
    private static final int TIME_PER_QUESTION = 60;
    /** The amount of time left for the current question, in seconds. */
    private AtomicInteger timeLeft = new AtomicInteger(TIME_PER_QUESTION);
    /** The button used to start the quiz. */

    private JButton startButton;
    /** The label used to display the remaining time. */
    private JLabel countdownLabel;
    /** The combo box used to select the quiz category. */
    private JComboBox<String> sourceComboBox;
    /** The label used to prompt the user for the number of questions. */
    private JLabel numQuestionsLabel;
    /** The text field used to enter the number of questions. */
    private JTextField numQuestionsField;
    /** The panel containing the quiz settings. */
    private JPanel settingsPanel;
    /** The synthesizer used to generate speech. */
    private Synthesizer synthesizer;
    /** The button used to move to the next question. */
    private JButton nextButton;
    /** The label used to display the current question. */
    private JLabel promptLabel;

    private int counter = 60;
    /** The radio button used for choice A. */
    private JRadioButton choiceARadio;
    private String userName;
    /** The radio button used for choice B. */
    private JRadioButton choiceBRadio;
    /** The radio button used for choice C. */
    private JRadioButton choiceCRadio;
    /** The radio button used for choice D. */
    private JRadioButton choiceDRadio;
    /** The group of radio buttons for the answer choices. */
    private ButtonGroup choiceGroup;
    /** The panel containing the current question. */
    private JPanel questionPanel;
    /** The timer used to track the remaining time for the current question. */
    private Timer questionTimer;
    /** The database connection used to retrieve quiz questions. */
    private Connection connection;
    /** The list of questions in the current quiz. */
    private List<project.Question> questions;
    /** The index of the current question in the list of questions. */
    private int currentQuestionIndex;
    /** The user's score. */
    private int score;
    /** The list of answer buttons. */
    private List<JButton> answerButtons;
    /** The remaining time for the current question, in seconds. */
    private int remainingTime;
    /** The JFrame used for the application window. */
    private JFrame frame = new JFrame("Quiz Game");
    /** The current quiz question. */
    private project.Question question;
    /** The AWT key stroke for handling events. */
    private AWTKeyStroke evt;
    /** The timer used to track the remaining time for the quiz. */

    private Timer timer;
    /**
     * Constructs a QuizApp object with the given user name.
     *
     * @param userName the name of the user playing the quiz
     */
    public QuizApp(String userName) {
        JFrame frame = new JFrame();
        JPanel panel = new JPanel();
this.userName=userName;


        JLabel timerLabel = new JLabel("01:00");

        JLabel imageLabel = new JLabel();
        // Initialize the UI components
        startButton = new JButton("Start");
        startButton.addActionListener(this);

        String[] sourceOptions = {"mathematics", "animals", "technology", "geography"};
        sourceComboBox = new JComboBox<>(sourceOptions);

        numQuestionsLabel = new JLabel("Number of Questions:");
        numQuestionsField = new JTextField("20");

        settingsPanel = new JPanel(new GridLayout(0, 2));
        settingsPanel.add(new JLabel("Select Quiz Category:"));
        settingsPanel.add(sourceComboBox);
        settingsPanel.add(numQuestionsLabel);
        settingsPanel.add(numQuestionsField);
        settingsPanel.add(startButton);

        nextButton = new JButton("Next");
        nextButton.addActionListener(this);

        promptLabel = new JLabel();
        choiceARadio = new JRadioButton();
        choiceBRadio = new JRadioButton();
        choiceCRadio = new JRadioButton();
        choiceDRadio = new JRadioButton();

       choiceGroup = new ButtonGroup();

      choiceGroup.add(choiceARadio);
      choiceGroup.add(choiceBRadio);
      choiceGroup.add(choiceCRadio);
       choiceGroup.add(choiceDRadio);

     questionPanel = new JPanel(new GridLayout(0, 1));

       //promptLabel.setBackground(new Color(96, 191, 183));
        promptLabel.setOpaque(true);
        choiceARadio.setBackground(new Color(176, 226, 255));
        choiceARadio.setOpaque(true);

      choiceBRadio.setBackground(new Color(176, 226, 255));

       choiceBRadio.setOpaque(true);

     choiceCRadio.setBackground(new Color(176, 226, 255));
        choiceCRadio.setOpaque(true);

      choiceDRadio.setBackground(new Color(176, 226, 255));
        choiceDRadio.setOpaque(true);

        questionPanel.add(promptLabel);

       questionPanel.add(choiceARadio);
        questionPanel.add(choiceBRadio);
        questionPanel.add(choiceCRadio);
        questionPanel.add(choiceDRadio);


        panel.add(questionPanel);
        // Initialize the database connection
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost/quiz", "root", "admin#");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Set up the window
        setTitle("Quiz App");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        add(settingsPanel, BorderLayout.NORTH);
        add(questionPanel, BorderLayout.CENTER);
        add(nextButton, BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(null);
    }



    /**

     This method handles the action events for the start and next buttons.

     @param e the action event generated by the user clicking on a button
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == startButton) {
            // Add animation for Start button
            Timer timer = new Timer(50, new ActionListener() {
                private int frameCount = 0;
                private final int maxFrameCount = 10;
                private final int[] frameDelay = {200, 180, 160, 140, 120, 100, 80, 60, 40, 20};
                /**
                 * This method handles the action events for the timer used to animate the start button.
                 *
                 * @param e the action event generated by the timer
                 */
                public void actionPerformed(ActionEvent e) {
                    startButton.setBackground(Color.getHSBColor(0.6f, 1.0f, 0.8f));
                    startButton.setForeground(Color.WHITE);
                    startButton.setFont(new Font("SansSerif", Font.BOLD, 16));
                    startButton.setBounds(startButton.getX() - 1, startButton.getY(), startButton.getWidth(), startButton.getHeight());

                    frameCount++;
                    if (frameCount >= maxFrameCount) {
                        frameCount = 0;
                        ((Timer)e.getSource()).stop();
                    } else {
                        ((Timer)e.getSource()).setDelay(frameDelay[frameCount]);
                    }
                }
            });
            timer.start();
            startQuiz();
        } else if (e.getSource() == nextButton) {
            // Add animation for Next button
            Timer timer = new Timer(50, new ActionListener() {
                private int frameCount = 0;
                private final int maxFrameCount = 10;
                private final int[] frameDelay = {200, 180, 160, 140, 120, 100, 80, 60, 40, 20};
                /**

                 ActionListener implementation that handles the functionality of the "Next" button in a quiz application.

                 This method sets the background color, font, and position of the button, increments the frame count,

                 and stops the timer if the maximum frame count has been reached. Otherwise, it sets the delay for the timer

                 to the value at the current frame count index in the frameDelay array.

                 @param e The ActionEvent that triggered this method call.
                 */
                public void actionPerformed(ActionEvent e) {
                    nextButton.setBackground(Color.getHSBColor(0.3f, 1.0f, 0.8f));
                    nextButton.setForeground(Color.WHITE);
                    nextButton.setFont(new Font("SansSerif", Font.BOLD, 16));
                    nextButton.setBounds(nextButton.getX() + 1, nextButton.getY(), nextButton.getWidth(), nextButton.getHeight());

                    frameCount++;
                    if (frameCount >= maxFrameCount) {
                        frameCount = 0;
                        ((Timer)e.getSource()).stop();
                    } else {
                        ((Timer)e.getSource()).setDelay(frameDelay[frameCount]);
                    }
                }
            });
            timer.start();
            showNextQuestion();
        }
    }





    /**

     Retrieves the quiz questions from a database table based on the selected quiz source and number of questions,

     and initializes the quiz state by setting the current question index and score to 0, disabling the start button,

     and showing the first question on the quiz panel.
     */
    private void startQuiz() {
// Determine the quiz source and number of questions
        String source = (String) sourceComboBox.getSelectedItem();
        int numQuestions = Integer.parseInt(numQuestionsField.getText());
        // Determine the database table name based on the selected quiz source
        String tableName = "";
        if (source.equals("mathematics")) {
            tableName = "quiz_questions";
        } else if (source.equals("animals")) {
            tableName = "animal_questions";
        } else if (source.equals("technology")) {
            tableName = "tech_questions";
        } else if (source.equals("geography")) {
            tableName = "geo_questions";
        }

        // Retrieve the questions from the database
        questions = new ArrayList<project.Question>();
        int questionNumber = 1;
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM " + tableName + " ORDER BY RAND() LIMIT " + numQuestions);

            while (resultSet.next()) {
                String imagePath = resultSet.getString("image_path");
                ImageIcon imageIcon = null;
                if (imagePath != null && !imagePath.isEmpty()) {
                    File file = new File(imagePath);
                    if (file.exists()) {
                        try {
                            Image image = ImageIO.read(file);
                            imageIcon = new ImageIcon(image);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                }

                project.Question question = new project.Question(
                        questionNumber,
                        resultSet.getString("question"),
                        resultSet.getString("answer_a"),
                        resultSet.getString("answer_b"),
                        resultSet.getString("answer_c"),
                        resultSet.getString("answer_d"),
                        resultSet.getString("correct_answer"),
                        imageIcon
                );

                questions.add(question);
                questionNumber++;
            }

            currentQuestionIndex = 0;
            score = 0;
            showQuestion(questions.get(currentQuestionIndex), questionPanel);
            startButton.setEnabled(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /**

     Displays the next question in the quiz and handles user input for answering the current question.
     If the user has not selected an answer, prompts the user to do so and returns without processing the question.
     After answering all questions, shows the quiz results including the score, percentage score, and a pie chart breakdown of correct vs. incorrect answers.
     Stores the user's score in a MySQL database and displays a message if the storage fails.
     Enables the start button, allows editing of the number of questions and source selection, and disables the next button.
     Clears the question and answer choices and resets the selection.
     */
        private void showNextQuestion() {


        questionPanel.requestFocusInWindow(); // Make sure the question panel has focus
        questionPanel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                char c = e.getKeyChar();
                if (c == 'a' || c == 'A') {
                    choiceARadio.setSelected(true);
                } else if (c == 'b' || c == 'B') {
                    choiceBRadio.setSelected(true);
                } else if (c == 'c' || c == 'C') {
                    choiceCRadio.setSelected(true);
                } else if (c == 'd' || c == 'D') {
                    choiceDRadio.setSelected(true);
                }
            }
        });

        final String[] userAnswer = {""};

        if (choiceARadio.isSelected()) {
            userAnswer[0] = "A";
        } else if (choiceBRadio.isSelected()) {
            userAnswer[0] = "B";
        } else if (choiceCRadio.isSelected()) {
            userAnswer[0] = "C";
        } else if (choiceDRadio.isSelected()) {
            userAnswer[0] = "D";
        }

        if (userAnswer[0].equals("")) {
            JOptionPane.showMessageDialog(this, "Please select an answer or press A, B, C, or D.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        project.Question currentQuestion = questions.get(currentQuestionIndex);
        if (currentQuestion.getAnswer().equals(userAnswer[0])) {
            score++;
        }

        currentQuestionIndex++;
        if (currentQuestionIndex >= questions.size()) {
            // Quiz is over
            JOptionPane.showMessageDialog(this, "Quiz is over, " + userName + ". You scored " + score + " out of " + questions.size() + " questions.");
            startButton.setEnabled(true);
            nextButton.setEnabled(false);
            try {
                // Connect to the scores database
                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/scores","root","admin#");

                // Insert the score into the scores table
                PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO score (username, quiz_topic, num_of_questions, score ) VALUES (?,?,?,?)");
                preparedStatement.setString(1, userName);
                //preparedStatement.setInt(1, score);

                preparedStatement.setString(1, userName);
                preparedStatement.setString(2, (String) sourceComboBox.getSelectedItem());
                preparedStatement.setInt(3, questions.size());
                preparedStatement.setInt(4, score);
                preparedStatement.executeUpdate();

                // Close the database connection
                connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Failed to store score in the database.", "Error", JOptionPane.ERROR_MESSAGE);
            }
            double percentageScore = ((double) score / (double) questions.size()) * 100.0;

            // Create a pie chart to show the score breakdown
            DefaultPieDataset dataset = new DefaultPieDataset();
            dataset.setValue("Correct", score);
            dataset.setValue("Incorrect", questions.size() - score);
            JFreeChart chart = ChartFactory.createPieChart("Quiz Results for " + userName, dataset, true, true, false);
            PiePlot plot = (PiePlot) chart.getPlot();
            plot.setSectionPaint("Correct", new Color(0, 0, 255)); // blue
            plot.setSectionPaint("Incorrect", new Color(255, 255, 0)); // yellow
            ChartPanel chartPanel = new ChartPanel(chart);
            JFrame chartFrame = new JFrame();
            chartFrame.setContentPane(chartPanel);
            chartFrame.pack();
            chartFrame.setVisible(true);

            // Show the result dialog
            JOptionPane.showMessageDialog(this, "Your score: " + score + "/" + questions.size() + " (" + String.format("%.2f", percentageScore) + "%)", "Quiz Result for " + userName, JOptionPane.INFORMATION_MESSAGE);
            startButton.setEnabled(true);
            numQuestionsField.setEditable(true);
            sourceComboBox.setEnabled(true);
            nextButton.setEnabled(false);
            promptLabel.setText("");
            choiceARadio.setText("");
            choiceBRadio.setText("");
            choiceCRadio.setText("");
            choiceDRadio.setText("");
            choiceGroup.clearSelection();
            clearQuestion();
        } else {
            // Show the next question
            showQuestion(questions.get(currentQuestionIndex), questionPanel);
        }
    }
    /**

     Displays the provided question and related choices on the provided JPanel, along with a timer of 60 seconds.

     Allows the user to select a choice by clicking on the corresponding radio button or pressing the corresponding

     keyboard key (A, B, C, D), and move on to the next question by clicking the "Next" button or pressing any of the

     four arrow keys. Also allows the user to listen to the question and choices by pressing the "E" key on the keyboard.

     @param question the question to be displayed

     @param questionPanel the JPanel on which to display the question and choices
     */
    private void showQuestion(project.Question question, JPanel questionPanel) {
        String userAnswer ="";

        promptLabel.setText(""); // clear promptLabel

        choiceARadio.setText(question.getChoiceA());
        choiceBRadio.setText(question.getChoiceB());
        choiceCRadio.setText(question.getChoiceC());
        choiceDRadio.setText(question.getChoiceD());

        // Remove any existing components
        questionPanel.removeAll();

        // Add image label if the question has an image
        ImageIcon imageIcon = question.getImageIcon();
        if (imageIcon != null) {
            JLabel imageLabel = new JLabel(imageIcon);
            questionPanel.add(imageLabel, BorderLayout.NORTH);
        }

        // Add question label
        JLabel questionLabel = new JLabel(question.getPrompt());
        questionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        questionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        questionPanel.add(questionLabel, BorderLayout.CENTER);
        JLabel numberLabel = new JLabel("Question " + question.getQuestionNumber() + ":");
        numberLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        questionPanel.add(numberLabel, BorderLayout.NORTH);
        // Add vertical spacing
        questionPanel.add(Box.createVerticalStrut(20));

        // Add radio buttons
        JPanel radioPanel = new JPanel();
        radioPanel.setLayout(new BoxLayout(radioPanel, BoxLayout.Y_AXIS));
        radioPanel.setBackground(new Color(176, 226, 255));
        radioPanel.add(choiceARadio);
        radioPanel.add(choiceBRadio);
        radioPanel.add(choiceCRadio);
        radioPanel.add(choiceDRadio);
        questionPanel.add(radioPanel, BorderLayout.SOUTH);
        questionPanel.setBackground(new Color(176, 226, 255));
        this.questionPanel.revalidate();
        this.questionPanel.repaint();

// Add a timer to count down from 60 seconds
       final JLabel timerLabel = new JLabel("60");
        timerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        timerLabel.setForeground(Color.RED);
        this.questionPanel.add(timerLabel, BorderLayout.SOUTH);

        final int[] remainingTime = {60};

        timer = new Timer(1000, e -> {
            remainingTime[0]--;
            timerLabel.setText(String.valueOf(remainingTime[0]));
            if (remainingTime[0] == 0 )  {
                // Stop the timer when it reaches zero and select the "no response" option
                timer.stop();

                    clearQuestion();
                    showNextQuestion();
                }

        });

        nextButton.addActionListener(e -> {
            // Stop the timer when the next button is clicked
            timer.stop();
            this.questionPanel.remove(timerLabel);


            if (userAnswer.equals("A") || userAnswer.equals("B") || userAnswer.equals("C") || userAnswer.equals("D")) {

                clearQuestion();
                showNextQuestion();
            }
        });
        timer.start();


// Add a key listener to handle keyboard input
        this.questionPanel.requestFocusInWindow();
        this.questionPanel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                char c = e.getKeyChar();
                if (c == 'a' || c == 'A') {
                    choiceARadio.setSelected(true);

                } else if (c == 'b' || c == 'B') {
                    choiceBRadio.setSelected(true);

                } else if (c == 'c' || c == 'C') {
                    choiceCRadio.setSelected(true);

                } else if (c == 'd' || c == 'D') {
                    choiceDRadio.setSelected(true);

                }
            }
        });


        // Add speech synthesis for the question when "E" is pressed
        this.questionPanel.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_E, 0), "speakQuestion");
        this.questionPanel.getRootPane().getActionMap().put("speakQuestion", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Thread(() -> {
                    try {
                        // setting properties as Kevin Dictionary
                        System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us" + ".cmu_us_kal.KevinVoiceDirectory");
                        // registering speech engine
                        Central.registerEngineCentral("com.sun.speech.freetts" + ".jsapi.FreeTTSEngineCentral");
                        // create a Synthesizer that generates voice
                        SynthesizerModeDesc desc = new SynthesizerModeDesc(Locale.US);
                        Synthesizer synthesizer = Central.createSynthesizer(desc);

                        // allocate the synthesizer if it's not already allocated
                        if (synthesizer.getEngineState() != Synthesizer.ALLOCATED) {
                            synthesizer.allocate();
                        }

                        // speak the specified text until the QUEUE become empty
                        synthesizer.speakPlainText(question.getPrompt(), null);
                        synthesizer.speakPlainText(question.getChoiceA(), null);
                        synthesizer.speakPlainText(question.getChoiceB(), null);
                        synthesizer.speakPlainText(question.getChoiceC(), null);
                        synthesizer.speakPlainText(question.getChoiceD(), null);

                        // wait for the synthesizer to finish speaking
                        synthesizer.waitEngineState(Synthesizer.QUEUE_EMPTY);

                    } catch (Throwable t) {
                        System.err.println("Error while speaking question:");
                        t.printStackTrace();
                    } finally {
                        // deallocating the Synthesizer
                        if (synthesizer != null) {
                            try {
                                synthesizer.deallocate();
                            } catch (EngineException ex) {
                                throw new RuntimeException(ex);
                            }
                        }
                    }
                }).start();
            }

        });

        clearQuestion();
    }






    /**

     Clears the selected choice in the ChoiceGroup associated with this question.
     */
    private void clearQuestion() {
        choiceGroup.clearSelection();
    }



   // public static void main(String[] args) {
      //  QuizApp quizApp = new QuizApp();
       // quizApp.setVisible(true);
   // }
}