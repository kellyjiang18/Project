import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JButton;
import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class GUIController implements ActionListener
{
    private JTextField ansEntryField;
    private BlackJackAPI api;
    private JTextArea playerScore;
    private JTextArea dealerScore;

    public GUIController()
    {
        playerScore = new JTextArea(5,5);
        dealerScore = new JTextArea(5,5);
        ansEntryField = new JTextField();
        setupGui();
        setUpGui2();
    }


public void setupGui()
{
    JFrame frame = new JFrame("Blackjack Start");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    JPanel entryPanel = new JPanel();
    JLabel questionLabel = new JLabel("How many decks?");
    ansEntryField = new JTextField(10);
    JButton enterButton = new JButton("Enter");
    entryPanel.add(questionLabel);
    entryPanel.add(ansEntryField);
    entryPanel.add(enterButton);
    frame.add(entryPanel,BorderLayout.SOUTH);

    enterButton.addActionListener(this);
    enterButton.addActionListener(e -> {frame.dispose();});

    frame.pack();
    frame.setVisible(true);
}

public void setUpGui2()
{
    JFrame game = new JFrame("Blackjack Game!");
    game.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    JPanel midPanel = new JPanel();
    playerScore.setText("Player score: ");
    playerScore.setFont(new Font("Helvetica", Font.PLAIN, 16));
    playerScore.setWrapStyleWord(true);
    playerScore.setLineWrap(true);
    midPanel.add(playerScore);
    dealerScore.setText("Dealer score: ");
    dealerScore.setFont(new Font("Helvetica", Font.PLAIN, 16));
    dealerScore.setWrapStyleWord(true);
    dealerScore.setLineWrap(true);
    midPanel.add(dealerScore);
    JButton hitButton = new JButton("Hit");
    JButton stayButton = new JButton("Stay");
    midPanel.add(hitButton);
    midPanel.add(stayButton);
    game.add(midPanel,BorderLayout.SOUTH);

    hitButton.addActionListener(this);
    stayButton.addActionListener(this);

    game.pack();
    game.setVisible(true);
}

    public void actionPerformed(ActionEvent e) {
        JButton button = (JButton) (e.getSource());
        String text = button.getText();
        if (text.equals("Enter")) {
            String number = ansEntryField.getText();
            int numDecks = Integer.parseInt(number);
            api = new BlackJackAPI(numDecks);
        }
        else if(text.equals("Hit")){
            api.drawCard();
            playerScore.setText("Player Score: "+api.getScore());
        }
        else if(text.equals("Stay")){
            api.dealerDrawCard();
            dealerScore.setText("Dealer Score: "+api.getDealerScore());
        }
    }
}

