import java.util.Scanner;
import java.util.ArrayList;
import java.util.Random;

class card {
    private String type, suit;
    private int value, serial;

    public card (String type, String suit, int value, int serial) {
        this.type = type;
        this.suit = suit;
        this.value = value;
        this.serial = serial;
    }

    public String getName () {
        return type + " of " + suit;
    }

    public int getValue () {
        return value;
    }
}

class player {
    private int runningTotal, dealerRunningTotal, numCards;
    boolean busted;
    private String name, cards, dealerCards;
    //private card[] hand = new card[52];

    public player (int runningTotal, int numCards, int dealerRunningTotal, boolean busted, String name, String cards, String dealerCards) {
        this.runningTotal = runningTotal;
        this.dealerRunningTotal = dealerRunningTotal;
        this.numCards = numCards;
        this.busted = busted;
        this.name = name;
        this.cards = cards;
        this.dealerCards = dealerCards;
        //this.hand = hand;
    }

    public int getScore () {
        return runningTotal;
    }

    public int getNumCards () {
        return numCards;
    }

    public boolean isBusted () {
        return busted;
    }

    public String getName () {
        return name;
    }

    public void print (boolean isDealer) {
        System.out.println(name + " has the following cards: ");
        if (isDealer) System.out.println("???\n" + dealerCards);
        else System.out.println(cards);
        if (isDealer) System.out.println("Value: " + (dealerRunningTotal) + " + ???");
        else System.out.println("Value: " + runningTotal + "\n");
    }

    public void increment (card newCard) {
        runningTotal += newCard.getValue();
        if (numCards != 0) dealerRunningTotal += newCard.getValue();
        cards = cards + newCard.getName() + "\n";
        if (numCards != 0) dealerCards = dealerCards + newCard.getName() + "\n";
        numCards++;

    }

    public void bust () {
        busted = true;
        runningTotal = 0;
    }
}

public class Workshop4ProjectBlackjack {

    public static void main (String[] args) {
        Scanner in = new Scanner(System.in);

        Random rand = new Random();
        System.out.println("How many people will be playing? ");
        int numPlayers = in.nextInt(), deckNum = 52;
        String[] nameNames = {"Ace", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten", "Jack", "Queen", "King"}, suitNames = {"Hearts", "Spades", "Clubs", "Diamonds"};
        player[] players = new player[numPlayers + 1];
        ArrayList<card> deck = new ArrayList<>(), blank = new ArrayList<>(), blank1 = new ArrayList<>();
        card[][] hand = new card[numPlayers][52];

        //set-up
        for (int i = 1; i <= numPlayers; i++) {
            System.out.println("Player " + i + ", what is your name? ");
            String name = in.nextLine();
            players[i] = new player(0, 0, 0, false, name, "", "");
        }
        players[0] = new player(0, 0, 0, false, "The dealer", "", "");
        for (int i = 0; i < 52; i++) {
            int value = (i % 13) + 1;
            if (value > 10) value = 10;
            deck.add(new card(nameNames[i%13], suitNames[i/13], value, i));
        }
        for (int i = 0; i <= numPlayers; i++) {
            for (int j = 0; j < 2; j++) {
                int ind = rand.nextInt(deckNum);
                //hand[i][players[i].getNumCards()] = deck.get(ind);
                players[i].increment(deck.get(ind));
                deck.remove(ind);
                deckNum--;
            }
        }

        //actual gameplay
        for (int i = 1; i <= numPlayers; i++) {
            boolean stand = false;

            players[0].print(true);
            for (int j = 1; j <= numPlayers; j++) {
                players[j].print(false);
            }
            while (!stand) {
                System.out.println(players[i].getName() + ", would you like to hit or stand? ");
                String option = in.nextLine();
                if (option.equals("hit")) {
                    int ind = rand.nextInt(deckNum);
                    //hand[i][players[i].getNumCards()] = deck.get(ind);
                    players[i].increment(deck.get(ind));
                    System.out.println(players[i].getName() + " chose to hit and recieved a " + deck.get(ind).getName());
                    deck.remove(ind);
                    deckNum--;
                    players[i].print(false);
                    if (players[i].getScore() > 21) {
                        System.out.println(players[i].getName() + " busted (went over 21)! ");
                        players[i].bust();
                        break;
                    }
                }
                else if (option.equals("stand")) {
                    System.out.println(players[i].getName() + " stood with a score of " + players[i].getScore() + "! ");
                    break;
                }
                else {
                    System.out.println("Please input a valid move. ");
                }
            }
        }
        for (int j = 0; j <= numPlayers; j++) {
            players[j].print(false);
        }
        while (true) {
            //System.out.println(players[i].getName() + ", would you like to hit or stand? ");
            //String option = in.nextLine();
            if (players[0].getScore() < 17) {
                int ind = rand.nextInt(deckNum);
                //hand[0][players[0].getNumCards()] = deck.get(ind);
                players[0].increment(deck.get(ind));
                System.out.println(players[0].getName() + " chose to hit and recieved a " + deck.get(ind).getName());
                deck.remove(ind);
                deckNum--;
                players[0].print(false);
                if (players[0].getScore() > 21) {
                    System.out.println(players[0].getName() + " busted (went over 21)! ");
                    players[0].bust();
                    break;
                }
            }
            else {
                System.out.println(players[0].getName() + " stood with a score of " + players[0].getScore() + "! ");
                break;
            }
        }

        //wrapping up, declaring winners
        for (int i = 1; i <= numPlayers; i++) {
            if (players[i].getScore() > players[0].getScore()){
                System.out.println(players[i].getName() + " stood with a higher score than the dealer at " + players[i].getScore() + ". Congratulations! ");
            }
            if (players[i].getScore() == players[0].getScore()) {
                System.out.println(players[i].getName() + " tied with the dealer at a score of " + players[i].getScore() + ". Good job! ");
            }
        }
    }
}