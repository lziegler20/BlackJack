
public class Card {
	
	public int rank;
	public String suitAndName;
	
	public Card(int rank, String suitAndName) {
		this.rank = rank;
		this.suitAndName = suitAndName;
	}
	
	public int getRank() {
		return rank;
	}
	
	public void setRank(int rank) {
		this.rank = rank;
	}
	
	public String getSuitandName() {
		return suitAndName;
	}
}