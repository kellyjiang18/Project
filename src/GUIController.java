import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JButton;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
        setUpGui2();
        setupGui();
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
            if(api.getPlayerBust())
            {
                playerScore.setText("You got "+api.getScore()+" and went over 21, its the dealer's turn!");
            }
        }
        else if(text.equals("Stay")){
            api.dealerDrawCard();
            dealerScore.setText("Dealer Score: "+api.getDealerScore());
            if(api.getDealerBust())
            {
                dealerScore.setText("The dealer got "+api.getDealerScore()+", no one wins!");
            }
            else if(!api.getDealerBust()&&!api.getDealerBust())
            {
                if(api.getScore()>api.getDealerScore())
                {playerScore.setText("You won! You got a score of "+api.getScore()+" while the dealer got a score of "+api.getDealerScore());}
                else if(api.getScore()<api.getDealerScore())
                {dealerScore.setText("The dealer won! They got a score of "+api.getDealerScore()+" while you got a score of "+api.getScore());}
                else
                {playerScore.setText("Tie! Score of "+api.getScore());
                dealerScore.setText("Tie! Score of "+api.getDealerScore());}
            }
        }
    }
}
