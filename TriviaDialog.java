import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;

public class TriviaDialog extends JDialog implements ActionListener {
    private ArrayList<Question> questions;
    private int currentQuestionIndex;
    private int score;
    private int totalQuestions;
    private JLabel questionLabel;
    private JLabel scoreLabel;
    private JLabel questionNumberLabel;
    private JRadioButton[] optionButtons;
    private ButtonGroup buttonGroup;
    private JButton submitButton;
    private JButton nextButton;
    private JButton restartButton;
    private JButton closeButton;
    private JPanel mainPanel;
    private JPanel gamePanel;
    private JPanel resultPanel;
    
    public TriviaDialog(JFrame parent) {
        super(parent, "Trivia Game", true); // Modal dialog
        initializeQuestions();
        setupGUI();
        startGame();
    }
    
    public void initializeQuestions() {
        questions = new ArrayList<>();
        
        questions.add(new Question("System.out.println(Hello +  World!)?", new String[]{"London", "Berlin", "Paris", "Madrid"}, 2));
        
        questions.add(new Question("Which planet is known as the Red Planet?",new String[]{"Venus", "Mars", "Jupiter", "Saturn"},1));
        
        questions.add(new Question("What is 2 + 2?",new String[]{"3", "4", "5", "6"},1));
        
        questions.add(new Question( "Who painted the Mona Lisa?",new String[]{"Van Gogh", "Picasso", "Da Vinci", "Monet"},2));
        Collections.shuffle(questions);
        totalQuestions = questions.size();
    }
    
    public void setupGUI() {
        setSize(650, 500);
        setLocationRelativeTo(getParent());
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setResizable(true);
        
        mainPanel = new JPanel(new CardLayout());
        
        gamePanel = new JPanel(new BorderLayout());
        gamePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JPanel topPanel = new JPanel(new FlowLayout());
        scoreLabel = new JLabel("Score: 0");
        questionNumberLabel = new JLabel("Question 1 of " + totalQuestions);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 16));
        questionNumberLabel.setFont(new Font("Arial", Font.BOLD, 16));
        topPanel.add(scoreLabel);
        topPanel.add(Box.createHorizontalStrut(50));
        topPanel.add(questionNumberLabel);
        
        JPanel questionPanel = new JPanel(new BorderLayout());
        questionLabel = new JLabel();
        questionLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        questionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        questionLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        questionPanel.add(questionLabel, BorderLayout.CENTER);
        
        JPanel optionsPanel = new JPanel(new GridLayout(4, 1, 5, 5));
        optionsPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        optionButtons = new JRadioButton[4];
        buttonGroup = new ButtonGroup();
        
        for (int i = 0; i < 4; i++) {
            optionButtons[i] = new JRadioButton();
            optionButtons[i].setFont(new Font("Arial", Font.PLAIN, 14));
            optionButtons[i].setOpaque(true);
            optionButtons[i].setBackground(Color.WHITE);
            buttonGroup.add(optionButtons[i]);
            optionsPanel.add(optionButtons[i]);
        }
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        submitButton = new JButton("Submit Answer");
        nextButton = new JButton("Next Question");
        closeButton = new JButton("Close Game");
        submitButton.addActionListener(this);
        nextButton.addActionListener(this);
        closeButton.addActionListener(this);
        nextButton.setEnabled(false);
        buttonPanel.add(submitButton);
        buttonPanel.add(nextButton);
        buttonPanel.add(closeButton);
        
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(questionPanel, BorderLayout.NORTH);
        centerPanel.add(optionsPanel, BorderLayout.CENTER);
        
        gamePanel.add(topPanel, BorderLayout.NORTH);
        gamePanel.add(centerPanel, BorderLayout.CENTER);
        gamePanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Result panel
        resultPanel = new JPanel(new BorderLayout());
        resultPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        
        mainPanel.add(gamePanel, "game");
        mainPanel.add(resultPanel, "result");
        
        add(mainPanel);
    }
    
    public void startGame() {
        currentQuestionIndex = 0;
        score = 0;
        updateScoreLabel();
        showQuestion();
    }
    
    public void showQuestion() {
        if (currentQuestionIndex >= totalQuestions) {
            showResults();
            return;
        }
        
        Question q = questions.get(currentQuestionIndex);
        questionLabel.setText("<html><center>" + q.getQuestion() + "</center></html>");
        
        String[] options = q.getOptions();
        for (int i = 0; i < 4; i++) {
            optionButtons[i].setText(options[i]);
            optionButtons[i].setSelected(false);
            optionButtons[i].setEnabled(true);
        }
        
        questionNumberLabel.setText("Question " + (currentQuestionIndex + 1) + " of " + totalQuestions);
        submitButton.setEnabled(true);
        nextButton.setEnabled(false);
    }
    
    public void checkAnswer() {
        int selectedOption = -1;
        for (int i = 0; i < 4; i++) {
            if (optionButtons[i].isSelected()) {
                selectedOption = i;
                break;
            }
        }
        
        if (selectedOption == -1) {
            JOptionPane.showMessageDialog(this, "Please select an answer!");
            return;
        }
        
        Question currentQuestion = questions.get(currentQuestionIndex);
        boolean correct = selectedOption == currentQuestion.getCorrectAnswer();
        
        if (correct) {
            score++;
            updateScoreLabel();
            JOptionPane.showMessageDialog(this, "Correct!", "Result", JOptionPane.INFORMATION_MESSAGE);
        } else {
            String correctAnswer = currentQuestion.getOptions()[currentQuestion.getCorrectAnswer()];
            JOptionPane.showMessageDialog(this, "Wrong! The correct answer is: " + correctAnswer, 
                                        "Result", JOptionPane.ERROR_MESSAGE);
        }
        
        // Disable option buttons and submit button
        for (JRadioButton button : optionButtons) {
            button.setEnabled(false);
        }
        submitButton.setEnabled(false);
        nextButton.setEnabled(true);
    }
    
    public void nextQuestion() {
        currentQuestionIndex++;
        showQuestion();
    }
    
    public void showResults() {
        resultPanel.removeAll();
        
        JLabel resultLabel = new JLabel("<html><center>" +
            "<h1>Game Over!</h1>" +
            "<h2>Your Final Score: " + score + " out of " + totalQuestions + "</h2>" +
            "<p>Percentage: " + Math.round((double) score / totalQuestions * 100) + "%</p>" +
            "</center></html>");
        resultLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        restartButton = new JButton("Play Again");
        JButton closeResultButton = new JButton("Close Game");
        restartButton.addActionListener(this);
        closeResultButton.addActionListener(e -> dispose());
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(restartButton);
        buttonPanel.add(closeResultButton);
        
        resultPanel.add(resultLabel, BorderLayout.CENTER);
        resultPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        CardLayout cl = (CardLayout) mainPanel.getLayout();
        cl.show(mainPanel, "result");
        
        resultPanel.revalidate();
        resultPanel.repaint();
    }
    
    public void restartGame() {
        Collections.shuffle(questions);
        CardLayout cl = (CardLayout) mainPanel.getLayout();
        cl.show(mainPanel, "game");
        startGame();
    }
    
    public void updateScoreLabel() {
        scoreLabel.setText("Score: " + score);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == submitButton) {
            checkAnswer();
        } else if (e.getSource() == nextButton) {
            nextQuestion();
        } else if (e.getSource() == restartButton) {
            restartGame();
        } else if (e.getSource() == closeButton) {
            dispose();
        }
    }
}


