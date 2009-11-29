package dk.androbet.betex.model;

import java.util.Date;

/**
 * Represents a bet on a betting exchange.
 * 
 * @author daniel
 * 
 */
public class Bet implements Cloneable{

	private int userId;
	private long betId;
	private BetStatus betStatus;
	private BetType betType;

	private Date placedDate;
	private Date matchedDate;

	private double size;
	private double price;

	public long getBetId() {
		return betId;
	}

	public void setBetId(long betId) {
		this.betId = betId;
	}

	public BetStatus getBetStatus() {
		return betStatus;
	}

	public void setBetStatus(BetStatus betStatus) {
		this.betStatus = betStatus;
	}

	public BetType getBetType() {
		return betType;
	}

	public void setBetType(BetType betType) {
		this.betType = betType;
	}

	public Date getPlacedDate() {
		return placedDate;
	}

	public void setPlacedDate(Date placedDate) {
		this.placedDate = placedDate;
	}

	public Date getMatchedDate() {
		return matchedDate;
	}

	public void setMatchedDate(Date matchedDate) {
		this.matchedDate = matchedDate;
	}

	public double getSize() {
		return size;
	}

	public void setSize(double size) {
		this.size = size;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	@Override
	public Object clone() {
		try {
			Bet clone = (Bet) super.clone();
			clone.setPlacedDate(placedDate != null ? (Date) placedDate.clone() : null);
			clone.setMatchedDate(matchedDate != null ? (Date) matchedDate.clone() : null);

			return clone;
		} catch (CloneNotSupportedException e) {
			throw new UnsupportedOperationException(e);
		}
	}
}
