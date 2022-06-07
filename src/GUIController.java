import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class GUIController implements ActionListener
{
    private JTextField ansEntryField;
    private BlackJackAPI api;
    private JTextArea playerScore;
    private JTextArea dealerScore;
    private JFrame playerCards = new JFrame("Player Cards");
    private JFrame dealerCards = new JFrame("Dealer Cards");
    private Container playerPane = playerCards.getContentPane();
    private Container dealerPane = dealerCards.getContentPane();

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
        playerScore.setText("Player Score: ");
        playerScore.setFont(new Font("Helvetica", Font.PLAIN, 16));
        playerScore.setWrapStyleWord(true);
        playerScore.setLineWrap(true);
        midPanel.add(playerScore);
        dealerScore.setText("Dealer Score: ");
        dealerScore.setFont(new Font("Helvetica", Font.PLAIN, 16));
        dealerScore.setWrapStyleWord(true);
        dealerScore.setLineWrap(true);
        midPanel.add(dealerScore);
        JButton hitButton = new JButton("Hit");
        JButton stayButton = new JButton("Stay");
        JButton resetButton = new JButton("Reset");
        midPanel.add(hitButton);
        midPanel.add(stayButton);
        midPanel.add(resetButton);
        game.add(midPanel,BorderLayout.SOUTH);

        hitButton.addActionListener(this);
        stayButton.addActionListener(this);
        resetButton.addActionListener(this);

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


    public void setPlayerCards()
    {
            try {
                URL imageURL = new URL(api.getImageURL());
                BufferedImage image = ImageIO.read(imageURL);
                playerCards.repaint();
                playerCards.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                JLabel cardImage = new JLabel(new ImageIcon(image));
                playerPane.setLayout(new BoxLayout(playerPane, BoxLayout.X_AXIS));
                playerPane.add(cardImage);
                playerPane.setVisible(true);
                playerCards.setVisible(true);
            } catch (IOException k) {
                System.out.println(k.getMessage());
            }
    }

    public void setDealerCards()
    {
        ArrayList<String> dealerCardsArray = api.getDealerCards();
        for(String card:dealerCardsArray)
        try {
            URL imageURL = new URL(card);
            BufferedImage image = ImageIO.read(imageURL);
            dealerCards.repaint();
            dealerCards.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            JLabel cardImage = new JLabel(new ImageIcon(image));
            dealerPane.setLayout(new BoxLayout(dealerPane, BoxLayout.X_AXIS));
            dealerPane.add(cardImage);
            dealerPane.setVisible(true);
            dealerCards.setVisible(true);
        } catch (IOException k) {
            System.out.println(k.getMessage());
        }
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
            setPlayerCards();
            playerScore.setText("Player Score: "+api.getScore());
            if(api.getPlayerBust())
            {playerScore.setText("You got "+api.getScore()+" and went over 21, its the dealer's turn!");}
        }
        else if(text.equals("Stay")){
            api.dealerDrawCard();
            dealerScore.setText("Dealer Score: "+api.getDealerScore());
            setDealerCards();
            if(api.getPlayerBust())
            {dealerScore.setText("The dealer won since you went over 21!");}
            else if(api.getDealerBust()&&!api.getPlayerBust())
            {playerScore.setText("You won! You got a score of "+api.getScore()+" while the dealer got a score of "+api.getDealerScore());}
            else if(api.getDealerBust())
            {dealerScore.setText("The dealer got "+api.getDealerScore()+", no one wins!");}
            else if(!api.getDealerBust()&&!api.getDealerBust())
            {
                if(api.getScore()>api.getDealerScore())
                {playerScore.setText("You won! You got a score of "+api.getScore()+" while the dealer got a score of "+api.getDealerScore());}
                else if(api.getScore()<api.getDealerScore()||api.getDealerScore()==21)
                {dealerScore.setText("The dealer won! They got a score of "+api.getDealerScore()+" while you got a score of "+api.getScore());}
                else
                {playerScore.setText("Tie! Score of "+api.getScore());
                    dealerScore.setText("Tie! Score of "+api.getDealerScore());}
            }
        }
        else if(text.equals("Reset")){
            api.reset();
            playerScore.setText("Player Score: "+api.getScore());
            dealerScore.setText("Dealer Score: "+api.getDealerScore());
            playerPane.removeAll();
            playerCards.repaint();
            dealerPane.removeAll();
            dealerPane.repaint();
            api.clearArray();
        }
    }
}
