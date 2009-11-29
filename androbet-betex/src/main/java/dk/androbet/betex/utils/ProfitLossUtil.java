package dk.androbet.betex.utils;

import java.util.List;

import dk.androbet.betex.model.Bet;
import dk.androbet.betex.model.BetStatus;
import dk.androbet.betex.model.BetType;

/**
 * Calculates profit loss for runner when is a winner or a looser.
 * 
 * @author korzekwad
 * 
 */
public class ProfitLossUtil {

	/**
	 * Calculates profit loss for runner when is a winner or a looser.
	 * 
	 * @param bets
	 *            List of back/lay bets on runner.
	 * @param winner
	 *            If true then calculate profit for runner as awinner, otherwise calculate profit for runner as a
	 *            looser.
	 * @return
	 */
	public static double calculateProfit(List<Bet> bets, boolean winner) {

		double profit = 0;

		for (Bet bet : bets) {
			if (bet.getBetStatus() == BetStatus.M) {
				if (winner) {
					if (bet.getBetType() == BetType.B) {
						profit += bet.getSize() * (bet.getPrice() - 1);
					} else if (bet.getBetType() == BetType.L) {
						profit -= bet.getSize() * (bet.getPrice() - 1);
					}
				} else {
					if (bet.getBetType() == BetType.B) {
						profit -= bet.getSize();
					} else if (bet.getBetType() == BetType.L) {
						profit += bet.getSize();
					}
				}
			}
		}
		return profit;

	}
	
	/**Calculates the average price based on a runner profit for outcomes (winner and loser).
	 * 
	 * Formula
	 * - for (winP&L > 0 and loserP&L<0) or (winP&L < 0 and loserP&L) y = (w-l)/-l
	 * - other cases = -1 is returned
	 * 
	 * @param winProfitLoss What is the p&l if a runner is a winner.
	 * @param loserProfitLoss What is the p&l if a runner is a loser.
	 * @return
	 */
	public static double calculateAvgPrice(double winProfitLoss,double loserProfitLoss) {
		if((winProfitLoss>0 && loserProfitLoss<0) || (winProfitLoss<0 && loserProfitLoss>0)) {
			return (winProfitLoss - loserProfitLoss)/-loserProfitLoss;
		}
		else {
			return -1;
		}

	}
}
