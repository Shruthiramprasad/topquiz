/**

 The TopQuizWelcomePanel class implements a welcome screen panel for a student quiz application.
 This panel includes a background image, a welcome text label, a text field to enter the user name,
 and a start button to begin the quiz.
 */
package project;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.SoftBevelBorder;
/**
 * Constructs a TopQuizWelcomePanel object and sets up the welcome screen panel.
 */

public class TopQuizWelcomePanel implements ActionListener {
    /** Instance variables **/
    private JFrame quizFrame;
    private JTextField textField;
    private JButton startButton;
    /**
     * Initializes the TopQuizWelcomePanel object and sets up the JFrame.
     */
    public TopQuizWelcomePanel() {
        quizFrame = new JFrame();
        quizFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        quizFrame.getContentPane().setBackground(new Color(12, 22, 45));
        quizFrame.setLayout(null);
        quizFrame.setResizable(true);
        quizFrame.setTitle("Student Quiz Application");

        // Get screen size
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        BackgroundPanel panel = new BackgroundPanel(new ImageIcon("C:\\Users\\yamini\\Downloads\\WhatsApp Image 2023-03-21 at 10.42.20 PM.jpeg").getImage());
        panel.setBounds(165, 150, 1200, 650);
        quizFrame.add(panel);

        // Add Welcome Text Label
        JLabel welcomeLabel = new JLabel("Welcome to TopQuiz..!!!");
        welcomeLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 50));
        welcomeLabel.setForeground(new Color(60, 167, 180));
        welcomeLabel.setBounds(450, 0, 900, 150);
        quizFrame.add(welcomeLabel);

        // Add text field to enter the user name
        JLabel nameLabel = new JLabel("Enter your name: ");
        nameLabel.setFont(new Font("ComicSans", Font.BOLD, 24));
        nameLabel.setForeground(new Color(145, 10, 20));
        nameLabel.setBounds(220, 500, 250, 50);
        panel.add(nameLabel);

        textField = new JTextField();
        textField.setBounds(470, 500, 200, 50);
        textField.setBackground(new Color(163, 174, 120));
        textField.setForeground(new Color(20, 90, 220));
        textField.setFont(new Font("Arial", Font.BOLD, 20));
        textField.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
        textField.setHorizontalAlignment(JTextField.LEFT);
        panel.add(textField);

        // Add GIF to the panel
       /* JLabel gifLabel = new JLabel(new ImageIcon("C:\\Users\\iamba\\OneDrive\\Desktop\\file.gif"));
        //gifLabel.setBounds(screenSize.width / 4 - 100, screenSize.height / 4 - 200, 100, 100);
        gifLabel.setBounds(80, 30, 120, 40);
        panel.add(gifLabel);*/

        startButton = new JButton("Start");
        startButton.setFont(new Font("SansSerif", Font.BOLD, 20));
        startButton.addActionListener(this);
        startButton.setLayout(null);
        startButton.setBounds(820,500,250,50);
        startButton.setFocusable(true);
        //startButton.setBounds(screenSize.width, screenSize.height, 200, 50);
        startButton.setVisible(true);
        startButton.setBorder(BorderFactory.createBevelBorder(SoftBevelBorder.RAISED));
        startButton.setBackground(new Color(20, 90, 15));
        panel.add(startButton);
        panel.setLayout(null);

        quizFrame.pack();
        quizFrame.setLocationRelativeTo(null); // Center on screen
        quizFrame.setVisible(true);
    }
    /**

     This class represents an ActionListener that listens to the startButton
     and creates a new QuizApp JFrame when clicked. It also checks if a user
     name has been entered in the textField and displays an error message
     if it is blank or null.
     @param e the ActionEvent triggered by the startButton
     @return nothing, but either displays an error message or creates a new QuizApp JFrame
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == startButton) {
            String userName = textField.getText();
            if(userName == null || userName.isBlank()){
                JOptionPane.showMessageDialog(quizFrame, "Please enter your name to start TopQuiz!",
                        "Top Quiz", JOptionPane.ERROR_MESSAGE);
            }else {
                quizFrame.setVisible(false);
                JFrame f = new project.QuizApp(userName);
                f.setVisible(true);
            }
        }
    }
    /**

     This class represents a custom JPanel with a background image. The image
     is set in the constructor and is drawn on the panel when the paintComponent
     method is called.
     @return nothing, but the panel's background is set to the specified image
     */
    class BackgroundPanel extends JPanel {
        private Image backgroundImage;

        public BackgroundPanel(Image backgroundImage) {
            this.backgroundImage = backgroundImage;
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(backgroundImage, 0, 0, null);
        }
    }
}