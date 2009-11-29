package dk.androbet.game;

import java.util.List;

import dk.androbet.betex.model.Bet;
import dk.androbet.betex.model.LadderPrices;

/**Widget displays prices ladder for market runner.
 * 
 * @author korzekwad
 *
 */
public interface LadderPrice {

	/**
	 * Refreshes prices ladder with a new data.
	 * 
	 * @param ladderPrices
	 *            best three back and lay prices
	 * @param bets
	 *            user bets
	 */
	public void update(LadderPrices ladderPrices, List<Bet> bets);
	
	/**Called when user clicks on the user bets area (either lay or back side)
	 * 
	 * @param listener
	 */
	public void setOnClickUserBetListener(OnClickUserBetListener listener);
}
