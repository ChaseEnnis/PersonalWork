// Chase Ennis
//gitrepo
import java.awt.Color;
import java.awt.Font;
import javax.swing.border.Border;
import javax.swing.text.DefaultCaret;
import java.awt.event.*;
import java.awt.*;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.io.File; 
import java.io.IOException;

public class Gui extends JFrame{

    private ArrayList<String> list = new ArrayList<>();
    private String corWords = "";
    private int score=0;

    public static void main(String[] args) throws Exception {
        new Gui();   
    }
    
    public Gui(){
        File file = new File("words.txt");
        JFrame frame = new JFrame("Welcome to my Word Game!"); // JFrame is the Window
        Font myFont = new Font("MS Gothic", Font.BOLD, 24); // fun font for project
        
        // sound effects
        String sound_track;
        sound_track = "explosion.wav";

        String pacman;
        pacman= "pacman.wav";

        String correct;
        correct = "correct.wav";

        String wrong;
        wrong= "wrong.wav";

        String reset;
        reset = "reset.wav";

        String bye;
        bye = "bye.wav";
        
        Music music = new Music();

        ImageIcon propic = new ImageIcon("profilepic.png"); // will serve as the profile pic
        frame.setIconImage(propic.getImage()); // change icon of frame
        
        JPanel panel = new JPanel();
    
        ArrayList<String> correctGuesses= new ArrayList<String>();
        
        JFrame instructionFrame = new JFrame();
        instructionFrame.setIconImage(propic.getImage()); // change icon of frame
        instructionFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        JOptionPane Gamelabel = new JOptionPane();
        Gamelabel.showMessageDialog(instructionFrame, "The game instructions are simple. You will be given a 7 letter word with all different letters and need to make 4 or more lettered words from it! Scoring will start at 1 point for 4 letter words, 2 points for 5 letter words, and so on. To end the game type 'bye'");
        Gamelabel.setFont(new Font("Helvetica Neue", Font.PLAIN, 60));
        instructionFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        instructionFrame.setVisible(true);


        Gamelabel.setAlignmentX(CENTER_ALIGNMENT);

        JLabel printoutScrambleLetters = new JLabel();
        printoutScrambleLetters.setAlignmentX(CENTER_ALIGNMENT);
        printoutScrambleLetters.setFont(myFont);


        JButton scrambleButton = new JButton("Scramble Word: ");
        scrambleButton.setAlignmentX(CENTER_ALIGNMENT);
        scrambleButton.setFont(myFont);

        JTextArea correctWords = new JTextArea();
        correctWords.setBackground(Color.red);
        correctWords.setEditable(false);
        correctWords.setFont(myFont);
        correctWords.setLineWrap(true);
        correctWords.setWrapStyleWord(true);

        JLabel scoreLabel = new  JLabel("Score: ");
        scoreLabel.setAlignmentX(CENTER_ALIGNMENT);
        scoreLabel.setFont(myFont);

        try {
            list = readFile(file); // pass the arrayList "list" into readFile method which will return the words in wordstxt as arraylist
        }             catch(Exception ex){
            // should not even get here
        }

        scrambleButton.addActionListener(e -> {
            music.setFile(pacman);
            music.play();

            try{
                String chosenWord = checkWord(list); // this is the word chosen that will be shuffled
                ArrayList<Character> letters = new ArrayList<>();
                
                for (Character g : chosenWord.toCharArray()) {
                    letters.add(g);
                }
                    Collections.shuffle(letters);
                    chosenWord="";
                    String wordScrambled = " ";
                for (Character g : letters) {
                    chosenWord += g + "    ";
                    // prints out the scrambled word
                }
                printoutScrambleLetters.setText(chosenWord);
            }
            catch(Exception ex){
                // should not even get here
            }
        });

        Border border = BorderFactory.createLineBorder(Color.BLUE, 30);

        JTextField makewords = new JTextField(); // where the user will make the words
        DefaultCaret cursor = (DefaultCaret)makewords.getCaret();
        makewords.setHorizontalAlignment(makewords.CENTER); // puts cursor in the middle of box
        makewords.setCaret(cursor);
        makewords.setFont(myFont);
        makewords.setAlignmentX(CENTER_ALIGNMENT);
        makewords.setPreferredSize(new Dimension(500,200));
        makewords.setMaximumSize(new Dimension(10000,200));
        makewords.setBorder(border);

        JButton CheckButton = new JButton("Check Me!");
        CheckButton.setFont(myFont);
        CheckButton.setAlignmentX(CENTER_ALIGNMENT);
        CheckButton.setBounds(100,100,100,100);
        CheckButton.addActionListener(e -> {
            String userGuess = makewords.getText(); // parse the words formed in the textfield into a string
            ArrayList<Character> letters = new ArrayList<>();
                String chosenWord = printoutScrambleLetters.getText();
                
                for(int x=0;x<7;x++){
                    String letter=chosenWord.substring(0, chosenWord.indexOf(" "));
                    chosenWord=chosenWord.substring(chosenWord.indexOf(" ")+4);
                    letters.add(letter.toCharArray()[0]);
                    // creates a character list of letters for reference when user makes theirs to compare to
                    // makes an Array based upon the shuffled word at the top
                }
                if(!(userGuess.equals("bye"))) { // main function of program
                    String correctGuessString= corWords;
                    //ArrayList<String> correctGuesses= new ArrayList<String>();
                    while(correctGuessString.length()>1){
                        String word = correctGuessString.substring(0, correctGuessString.indexOf(" "));
                        correctGuessString=correctGuessString.substring(correctGuessString.indexOf(" ")+1);
                        // adds space to the words list in the correct words section. We want spaces so it us readable for user
                    }
                    int wordScore = scoreUpdate(userGuess, list, correctGuesses, letters);
                    if (wordScore !=0){ // if valid word
                        correctGuesses.add(userGuess);
                        music.setFile(correct);
                        music.play();
                        score += wordScore;
                        corWords += userGuess + " ";
                        correctWords.setText(correctWords.getText() + " " + userGuess);
                        correctWords.repaint();
                        scoreLabel.setText("Score: ");
                        scoreLabel.setFont(myFont);
                        scoreLabel.setText(scoreLabel.getText()+" "+score);
                    }
                    else{
                        music.setFile(wrong);
                        music.play();
                        
                    }
                
                }
                if (userGuess.equals("bye")){ // ends game effectively
                    makewords.setEnabled(false); // locks the user out of the game
                    scrambleButton.setEnabled(false);
                    CheckButton.setEnabled(false);
                    music.setFile(bye);
                    music.play();
                }
            makewords.setText("");
        });

        JButton clear = new JButton("Clear Text!");
        clear.setAlignmentX(RIGHT_ALIGNMENT);
        clear.setFont(myFont);
            clear.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                makewords.setText("");
                music.setFile(sound_track);
                music.play();
        }
        });

        JButton resetScore = new JButton("Reset All!");
        resetScore.setAlignmentX(LEFT_ALIGNMENT);
        resetScore.setFont(myFont);
        resetScore.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                score=0;
                makewords.setText("");
                correctWords.setText("");
                scoreLabel.setText("Score: " + score);
                correctGuesses.clear();
                music.setFile(reset);
                music.play();
            }
        });
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setSize(10, 10);
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.add(clear);
        buttonPanel.add(CheckButton);
        buttonPanel.add(resetScore);

        JPanel wordPanel = new JPanel();
        wordPanel.setLayout(new BoxLayout(wordPanel, BoxLayout.Y_AXIS));
        wordPanel.setMaximumSize(new Dimension(400, 400));
        JLabel correctWordsThusFar = new JLabel("Here are your correct words: ");
        correctWordsThusFar.setFont(myFont);
        correctWordsThusFar.setAlignmentX(CENTER_ALIGNMENT);

        
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.RED);
        panel.add(scrambleButton);
        panel.add(printoutScrambleLetters);
        panel.add(makewords);
        panel.add(buttonPanel);
        panel.add(correctWordsThusFar);
        panel.add(wordPanel);
        wordPanel.add(correctWords);
        panel.add(scoreLabel);
        frame.add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        
    }


    public class Music {
        // found online https://www.codespeedy.com/how-to-add-audio-on-jswing-in-java/
        // found the effects on https://mixkit.co/free-sound-effects
        Clip clip;
        AudioInputStream sound;
        public void setFile(String soundFileName) {
            try {
                File file = new File(soundFileName);
                sound = AudioSystem.getAudioInputStream(file);
                clip = AudioSystem.getClip();
                clip.open(sound);
            } catch (Exception e) {
            }
        }
        public void play() {
            clip.start();
        }
        public void stop() throws IOException {
            sound.close();
            clip.close();
            clip.stop();
        }
    }


    public static ArrayList<String> readFile(File f) throws Exception { // take in file, use scanner to read in file, return words
        ArrayList<String> listOfWords = new ArrayList<>();
        Scanner scanNewFileIJustMade = new Scanner(f); // this scans what is currently in the file to get the word count
        while (scanNewFileIJustMade.hasNext()) { // as long as it has next word
            listOfWords.add(scanNewFileIJustMade.next()); // keep adding next thing in file to list
        }
        scanNewFileIJustMade.close();
        return listOfWords;
    }


    public static int scoreUpdate(String word, ArrayList<String> list, ArrayList<String> correctGuesses, ArrayList<Character>letters) {
        for (int i=0; i<word.length(); i ++) { // only count words from the scrambled letters
            if (!(letters.contains(word.charAt(i)))) {
                return 0;
            }    
        }
        if (correctGuesses.contains(word)) {
            //System.out.println("checking for word"); 
            return 0; // do not take same word twice
        }
            if (word.length() == 4) { // make sure the list of correct guesses contains the word entered. 
                if (list.contains(word)) {
                    return 1;
            } // assigning the point values
                    return 0;
        }
            if (word.length() < 4) {
                return 0;
        }
            if (word.length() > 4) {
                if (list.contains(word)) {
                    return word.length();
            }
        }
            return 0;

    }


    public static String checkWord(ArrayList<String> words) throws Exception { 
        // so here I will be putting the word
        // generated to scramble to check to see if it will work
        ArrayList<String> validWords = new ArrayList<>();
        boolean flag = true; // check to see if the word meets all conditions
        for (String generatedWord : words) {
            flag = true;
            if (generatedWord.length() == 7) { // first check to see if the word is 7 letters
                for (int i = 0; i < 7; i++) { // now run through to check and see if the conditon are met by checking against each letter
                    char letter = generatedWord.charAt(i);
                    for (int j = 0; j < 7; j++) {
                        if (letter == generatedWord.charAt(j) && i != j) {
                            flag = false;
                        }
                    }
                }
            } else {
                flag = false; // if not 7 don't need to check anything else
            }

            if (flag == true) {
                validWords.add(generatedWord);
            }
        }
        Collections.shuffle(validWords);
        return (validWords.get(0));
    }

}
