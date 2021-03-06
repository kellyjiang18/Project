import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;
import java.util.ArrayList;

public class BlackJackAPI
{
    private String deck_id;
    private int score=0;
    private int dealerScore=0;
    private boolean playerBust=false;
    private boolean dealerBust=false;
    private String imageURL="";
    private ArrayList<String> dealerCards = new ArrayList<>();

    public BlackJackAPI(int deck_count) //creates a new deck of cards
    {
        String url="https://deckofcardsapi.com/api/deck/new/shuffle/?deck_count="+deck_count;
        try {
            URI myUri = URI.create(url);
            HttpRequest request = HttpRequest.newBuilder().uri(myUri).build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println(response.body());
            String deck_id=response.body().substring(response.body().indexOf("deck_id")+11,response.body().indexOf("deck_id")+23);
            System.out.println("deck_id: "+deck_id);
            this.deck_id=deck_id;
        }
        catch (Exception e) {System.out.println(e.getMessage());}
    }

    public void drawCard() //draws a card for the player
    {
        if(playerBust==false)
        {
            String url ="https://deckofcardsapi.com/api/deck/"+deck_id+"/draw/?count=1";
            try{
                URI myUri = URI.create(url);
                HttpRequest request = HttpRequest.newBuilder().uri(myUri).build();

                HttpClient client = HttpClient.newHttpClient();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                System.out.println(response.body());
                int value=0;
                System.out.println(response.body().substring(response.body().indexOf("code")+8,response.body().indexOf("image")-5));
                if(response.body().substring(response.body().indexOf("code")+8,response.body().indexOf("image")-5).equals("Q")||
                        response.body().substring(response.body().indexOf("code")+8,response.body().indexOf("image")-5).equals("J")||
                        response.body().substring(response.body().indexOf("code")+8,response.body().indexOf("image")-5).equals("K"))
                {value=10;}
                else if(response.body().substring(response.body().indexOf("code")+8,response.body().indexOf("image")-5).equals("A"))
                {
                    System.out.println("You drew an ace! What would you like its value to be? (11 or 1): ");
                    Scanner s = new Scanner(System.in);
                    value = s.nextInt();
                }
                else {value = Integer.parseInt(response.body().substring(response.body().indexOf("value")+9,response.body().indexOf("suit")-4));}
                addScore(value);
                System.out.println(score);
                setPlayerBust();
                imageURL=response.body().substring(response.body().indexOf("image")+9,response.body().indexOf("images")-4);
                System.out.println("Image URL: "+imageURL);
            }
            catch (Exception e) {System.out.println(e.getMessage());}
        }
        else {System.out.println("Game over, you went over 21!");}
    }

    public void dealerDrawCard() //draws all the cards for the dealer
    {
        if(score==21)
        {
            while(dealerScore<21)
            {
                String url ="https://deckofcardsapi.com/api/deck/"+deck_id+"/draw/?count=1";
                try{
                    URI myUri = URI.create(url);
                    HttpRequest request = HttpRequest.newBuilder().uri(myUri).build();

                    HttpClient client = HttpClient.newHttpClient();
                    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                    System.out.println(response.body());
                    dealerCards.add(response.body().substring(response.body().indexOf("image")+9,response.body().indexOf("images")-4));
                    int value=0;
                    System.out.println(response.body().substring(response.body().indexOf("code")+8,response.body().indexOf("image")-5));
                    if(response.body().substring(response.body().indexOf("code")+8,response.body().indexOf("image")-5).equals("Q")||
                            response.body().substring(response.body().indexOf("code")+8,response.body().indexOf("image")-5).equals("J")||
                            response.body().substring(response.body().indexOf("code")+8,response.body().indexOf("image")-5).equals("K"))
                    {value=10;}
                    else if(response.body().substring(response.body().indexOf("code")+8,response.body().indexOf("image")-5).equals("A"))
                    {
                        if(dealerScore==10) {dealerAddScore(11);}
                        else {dealerAddScore(1);}
                    }
                    else {value = Integer.parseInt(response.body().substring(response.body().indexOf("value")+9,response.body().indexOf("suit")-4));}
                    dealerAddScore(value);
                    System.out.println(dealerScore);
                    setDealerBust();
                }
                catch (Exception e) {System.out.println(e.getMessage());}
            }
        }
        else
        {
            while(dealerScore<21&&dealerScore<score)
            {
                String url ="https://deckofcardsapi.com/api/deck/"+deck_id+"/draw/?count=1";
                try{
                    URI myUri = URI.create(url);
                    HttpRequest request = HttpRequest.newBuilder().uri(myUri).build();

                    HttpClient client = HttpClient.newHttpClient();
                    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                    System.out.println(response.body());
                    dealerCards.add(response.body().substring(response.body().indexOf("image")+9,response.body().indexOf("images")-4));
                    int value=0;
                    System.out.println(response.body().substring(response.body().indexOf("code")+8,response.body().indexOf("image")-5));
                    if(response.body().substring(response.body().indexOf("code")+8,response.body().indexOf("image")-5).equals("Q")||
                            response.body().substring(response.body().indexOf("code")+8,response.body().indexOf("image")-5).equals("J")||
                            response.body().substring(response.body().indexOf("code")+8,response.body().indexOf("image")-5).equals("K"))
                    {value=10;}
                    else if(response.body().substring(response.body().indexOf("code")+8,response.body().indexOf("image")-5).equals("A"))
                    {
                        if(dealerScore==10) {dealerAddScore(11);}
                        else {dealerAddScore(1);}
                    }
                    else
                    {value = Integer.parseInt(response.body().substring(response.body().indexOf("value")+9,response.body().indexOf("suit")-4));}
                    dealerAddScore(value);
                    System.out.println(dealerScore);
                    setDealerBust();
                }
                catch (Exception e) {System.out.println(e.getMessage());}
            }
        }
    }

    public void addScore(int points) {score+=points;}
    public void dealerAddScore(int points) {dealerScore+=points;}
    private void setPlayerBust() {if(score>21) {playerBust=true;}}
    private void setDealerBust() {if(dealerScore>21) {dealerBust=true;}}

    public void reset()
    {
        score=0;
        dealerScore=0;
        playerBust=false;
        dealerBust=false;
        dealerCards.clear();
    }

    public int getScore() {return score;}
    public int getDealerScore() {return dealerScore;}
    public boolean getPlayerBust() {return playerBust;}
    public boolean getDealerBust() {return dealerBust;}
    public String getImageURL() {return imageURL;}
    public ArrayList<String> getDealerCards() {return dealerCards;}
}
