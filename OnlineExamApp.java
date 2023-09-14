import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class OnlineExamApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setSize(400, 150);
            loginFrame.setVisible(true);
        });
    }
}

class LoginFrame extends JFrame implements ActionListener {
    private JButton submitButton;
    private JPanel inputPanel;
    private JLabel userLabel, passLabel;
    private JTextField usernameField, passwordField;

    public LoginFrame() {
        setTitle("LOGIN");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        userLabel = new JLabel("Username:");
        passLabel = new JLabel("Password:");

        usernameField = new JTextField(15);
        passwordField = new JPasswordField(8);

        submitButton = new JButton("SUBMIT");
        submitButton.addActionListener(this);

        inputPanel = new JPanel(new GridLayout(3, 2));
        inputPanel.add(userLabel);
        inputPanel.add(usernameField);
        inputPanel.add(passLabel);
        inputPanel.add(passwordField);
        inputPanel.add(submitButton);

        add(inputPanel, BorderLayout.CENTER);
        pack();
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (!password.isEmpty()) {
            OnlineTestFrame testFrame = new OnlineTestFrame(username);
            testFrame.setVisible(true);
            dispose(); // Close the login frame
        } else {
            passwordField.setText("Enter Password");
        }
    }
}

class OnlineTestFrame extends JFrame implements ActionListener {
    private JLabel questionLabel, timerLabel;
    private JRadioButton[] answerOptions;
    private JButton saveAndNextButton, saveForLaterButton, resultButton;
    private ButtonGroup answerGroup;
    private Timer timer;
    private int count, current, questionNumber;
    private int[] savedQuestions;

    public OnlineTestFrame(String username) {
        setTitle("Online Test - " + username);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 350);

        questionLabel = new JLabel();
        timerLabel = new JLabel("Time left: 600");

        answerOptions = new JRadioButton[4];
        for (int i = 0; i < 4; i++) {
            answerOptions[i] = new JRadioButton();
        }

        answerGroup = new ButtonGroup();
        for (JRadioButton option : answerOptions) {
            answerGroup.add(option);
        }

        saveAndNextButton = new JButton("Save and Next");
        saveForLaterButton = new JButton("Save for Later");
        resultButton = new JButton("Result");

        saveAndNextButton.addActionListener(this);
        saveForLaterButton.addActionListener(this);
        resultButton.addActionListener(this);

        setLayout(null);

        add(questionLabel);
        add(timerLabel);
        for (JRadioButton option : answerOptions) {
            add(option);
        }
        add(saveAndNextButton);
        add(saveForLaterButton);
        add(resultButton);

        setBounds();
        initializeQuestions();

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            int remainingTime = 100;

            @Override
            public void run() {
                timerLabel.setText("Time left: " + remainingTime);
                remainingTime--;
                if (remainingTime < 0) {
                    timer.cancel();
                    timerLabel.setText("Time Out");
                    enableButtons(false);
                }
            }
        }, 0, 1000);
    }

    private void setBounds() {
        questionLabel.setBounds(30, 40, 450, 20);
        timerLabel.setBounds(20, 20, 450, 20);

        for (int i = 0, y = 80; i < 4; i++, y += 30) {
            answerOptions[i].setBounds(50, y, 200, 20);
        }

        saveAndNextButton.setBounds(95, 240, 140, 30);
        saveForLaterButton.setBounds(270, 240, 150, 30);
        resultButton.setBounds(270, 280, 150, 30);
    }

    private void initializeQuestions() {
        questionNumber = 0;
        current = 0;
        count = 0;
        savedQuestions = new int[10];

        // Define your questions and answer choices here
        // Example:
        String[] questions = {
            "What is the size of float and double in java?",
            "Number of primitive data types in Java are?",
            // Add more questions here...
        };

        String[][] answerChoices = {
            {"32 and 64", "32 and 32", "64 and 64", "64 and 32"},
            {"6", "7", "8", "9"},
            // Add more answer choices here...
        };

        // Set the first question and answer choices
        setQuestion(questions[0], answerChoices[0]);
    }

    private void setQuestion(String question, String[] choices) {
        questionLabel.setText("Q" + (questionNumber + 1) + ": " + question);
        for (int i = 0; i < 4; i++) {
            answerOptions[i].setText(choices[i]);
            answerOptions[i].setSelected(false);
        }
        answerOptions[0].setSelected(true);
    }

    private void enableButtons(boolean enable) {
        saveAndNextButton.setEnabled(enable);
        saveForLaterButton.setEnabled(enable);
        resultButton.setEnabled(enable);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == saveAndNextButton) {
            if (checkAnswer()) {
                count++;
            }
            questionNumber++;
            if (questionNumber < 10) {
                setQuestion("Next Question", new String[]{" A", "B", "C", "D"});
            }
        } else if (e.getSource() == saveForLaterButton) {
            savedQuestions[current] = questionNumber;
            current++;
            questionNumber++;
            if (questionNumber < 10) {
                setQuestion("Next Question", new String[]{" A", "B", "C", "D"});
            }
        } else if (e.getSource() == resultButton) {
            if (checkAnswer()) {
                count++;
            }
            JOptionPane.showMessageDialog(this, "Score = " + count);
            System.exit(0);
        }
    }

    private boolean checkAnswer() {
        // Implement your answer checking logic here
        // Example: Check if the correct option is selected
        int correctAnswerIndex = 1; // Change this to the correct answer index (0, 1, 2, or 3)
        return answerOptions[correctAnswerIndex].isSelected();
    }
}
