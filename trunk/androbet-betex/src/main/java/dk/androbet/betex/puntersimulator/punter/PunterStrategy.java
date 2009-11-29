package dk.androbet.betex.puntersimulator.punter;

import java.util.Random;

import dk.androbet.betex.Betex;

/**
 * Represents punter strategy that places bets on a betting exchange.
 * 
 * @author korzekwad
 * 
 */
public interface PunterStrategy {

	/**
	 * This method should contain an intelligence that places bets.
	 * 
	 * @param userId
	 *            User id assigned to this runner by PunterManager. User id is required to getBets, placeBet on a
	 *            betting exchange.
	 * @param betex
	 *            interface to betting exchange.
	 * @param realRunnerPrice
	 *            Represents the real runner probability, allows to simulate the trend on a betting exchange.
	 * @param runnerPriceTrend 0 - going down, 1 - drifting, 2 - going up.
	 */
	public void process(int userId, double realRunnerPrice, byte runnerPriceTrend, Betex betex);
}
