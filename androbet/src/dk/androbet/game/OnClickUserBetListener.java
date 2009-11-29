package dk.androbet.game;

import dk.androbet.betex.model.BetType;

/**Called when user clicks on the user bets area (either lay or back side)
 * 
 * @author korzekwad
 *
 */
public interface OnClickUserBetListener {

	/**What price and on which side user clicked.
	 * 
	 * @param betType
	 * @param price
	 * @param size
	 */
	public void onClick(BetType betType, double price, double size);
}
