import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Game {
	
	String[] suits = new String[] {" of Hearts", " of Diamonds", " of Spades", " of Clubs"};
	ArrayList<Card> deck;
	ArrayList<ArrayList<Card>> hands;
	int[] finalScores;
	int deckCounter;
	int players;
	
	public Game() {
		deck = new ArrayList<Card>();
		players = 0;
		for(int i = 0; i < 4; i++) {
			for(int j = 0; j < 13; j++) {
				if(j == 0)
					deck.add(new Card(11, "Ace" + suits[i]));
				else if(j == 10)
					deck.add(new Card(10, "Jack" + suits[i]));
				else if(j == 11)
					deck.add(new Card(10, "Queen" + suits[i]));
				else if(j == 12)
					deck.add(new Card(10, "King" + suits[i]));
				else
					deck.add(new Card((j + 1), (j + 1) + suits[i]));
			}
		}
	}
	
	public static void main(String[] args) {
		Game game = new Game();
		Scanner userIn = new Scanner(System.in);
		game.gameplay(userIn);
	}
	
	public void gameplay(Scanner userInputScanner) {
		System.out.println("How many Players are there? (There can be up to 6! There will be "
				+ "one additional player that is not controllable and will act as the Dealer)");
		numOfPlayers(userInputScanner);
		boolean gamePlay = true;
		while(gamePlay) {
			hands = new ArrayList<ArrayList<Card>>();
			deckCounter = 0;
			finalScores = new int[7];
			shuffleDeck();
			dealTheCards();
			System.out.println();
			standOrHit(userInputScanner);
			System.out.println();
			System.out.println("Dealer's turn");
			System.out.println("Dealer's 1st card is the " + hands.get(players).get(0).getSuitandName());
			System.out.println("Dealer's 2nd card is the " + hands.get(players).get(1).getSuitandName());
			int dealerScore = (hands.get(players).get(0).getRank() + hands.get(players).get(1).getRank());
			if(dealerScore > 17) {
				finalScores[players] = dealerScore;
				System.out.println("Dealer's final score is " + dealerScore);
			}
			else {
				while(true) {
					hands.get(players).add(deck.get(deckCounter));
					System.out.println("Dealer's next card is the " + deck.get(deckCounter).getSuitandName());
					dealerScore += deck.get(deckCounter).getRank();
					dealerScore = checkScore(dealerScore, hands.get(players));
					if(dealerScore > 21) {
						System.out.println("At a score of " + dealerScore + " the Dealer Busts");
						dealerScore = 0;
						System.out.println();
						break;
					}
					else if(dealerScore >= 17) {
						System.out.println("Dealer's final score is " + dealerScore);
						break;
					}
					System.out.println("Dealer's current score is " + dealerScore);
					deckCounter++;
				}
				finalScores[players] = dealerScore;
				System.out.println();
			}
			winners();
			while(true) {
				System.out.println("Would you like to play another game with the same amount of players? (yes/no)");
				String userCommand = userInputScanner.nextLine();
				if(userCommand.equals("yes"))
					break;
				else if(userCommand.equals("no")) {
					gamePlay = false;
					break;
				}
			}
		}
	}
	//Shuffle the deck
	public void shuffleDeck() {
		Collections.shuffle(deck);
	}
	//Find out who beat the dealer
	public void winners() {
		int winners = 0;
		System.out.println();
		for(int i = 0; i < players; i++) {
			if(finalScores[i] > finalScores[players]) {
				System.out.println("Player " + (i + 1) + " Won!");
				winners++;
			}
		}
		if(winners == 0)
			System.out.println("The Dealer Won");
		System.out.println();
	}
	//Game situation where the player can get another card, pass or see their hand
	public void standOrHit(Scanner userInputScanner) {
		int currScore = 0;
		for(int i = 0; i < players; i++) {
			System.out.println("Player " + (i + 1) + "'s turn");
			System.out.println("Your 1st card is the " + hands.get(i).get(0).getSuitandName());
			System.out.println("Your current score is " + (hands.get(i).get(0).getRank() + hands.get(i).get(1).getRank()));
			currScore = (hands.get(i).get(0).getRank() + hands.get(i).get(1).getRank());
			System.out.println();
			while(true) {
				System.out.println("Would you like to stand or hit? Or type hand to see your hand");
				String userCommand = userInputScanner.nextLine();
				if(userCommand.toLowerCase().equals("stand"))
					break;
				else if(userCommand.toLowerCase().equals("hit")) {
					hands.get(i).add(deck.get(deckCounter));
					System.out.println("Player " + (i + 1) + "'s next card is the " + deck.get(deckCounter).getSuitandName());
					currScore += deck.get(deckCounter).getRank();
					currScore = checkScore(currScore, hands.get(i));
					if(currScore > 21) {
						System.out.println("At a score of " + currScore + " Player " + (i + 1) + " Busts");
						currScore = 0;
						System.out.println();
						break;
					}
					System.out.println("Your current score is " + currScore);
					deckCounter++;
				}
				else if(userCommand.toLowerCase().equals("hand"))
					checkHand(hands.get(i), currScore);
				System.out.println();
			}
			finalScores[i] = currScore;
		}
	}
	//Dealing the Cards
	public void dealTheCards() {
		for(int i = 0; i < (players + 1); i++) {
			ArrayList<Card> newHand = new ArrayList<Card>();
			newHand.add(deck.get(deckCounter));
			deckCounter++;
			newHand.add(deck.get(deckCounter));
			if(i != players)
				System.out.println("Player " + (i + 1) + "'s 2nd card is the " + deck.get(deckCounter).getSuitandName());
			else
				System.out.println("Dealer's 2nd card is the " + deck.get(deckCounter).getSuitandName());
			deckCounter++;
			hands.add(newHand);
			System.out.println();
		}
	}
	//Asking for the amount of players
	public void numOfPlayers(Scanner userInputScanner) {
		while(true) {
			try {
				String userCommand = userInputScanner.nextLine();
				players = Integer.parseInt(userCommand);
				if(players > 0 && players < 7)
					break;
				else 
					System.out.println("Number needs to be 1-6!");
			} catch(NumberFormatException e) {
				System.out.println("Invalid command! Please enter a number between 1-6");
			}
		}
	}
	//This method checks for an Ace to lower the players score so they don't bust
	public int checkScore(int currScore, ArrayList<Card> currPlayer) {
		for(int i = 0; i < currPlayer.size(); i++) {
			if((currPlayer.get(i).getSuitandName().contains("Ace")) && (currPlayer.get(i).getRank() == 11)) {
				currPlayer.get(i).setRank(1);
				currScore -= 10;
				if(currScore <= 21)
					return currScore;
				
			}
		}
		return currScore;
	}
	//Displays the Player's hand
	public void checkHand(ArrayList<Card> currPlayer, int currScore) {
		for(int i = 0; i < currPlayer.size(); i++)
			System.out.println(currPlayer.get(i).getSuitandName());
		System.out.println("Your current score is " + currScore);
		System.out.println();
	}
}