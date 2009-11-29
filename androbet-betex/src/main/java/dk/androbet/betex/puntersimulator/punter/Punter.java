package dk.androbet.betex.puntersimulator.punter;

/**
 * Represents punter that places bets on a betting exchange.
 * 
 * @author korzekwad
 * 
 */
public class Punter {

	private final int userId;
	private final double punterCallProb;
	private final PunterStrategy punterStrategy;

	/**
	 * 
	 * @param userId
	 * @param punterCallProb Probability of calling a punter: from 0 .. 1
	 * @param punterStrategy
	 */
	public Punter(int userId,  double punterCallProb, PunterStrategy punterStrategy) {
		if (punterCallProb < 0 || punterCallProb > 1) {
			throw new IllegalArgumentException("PunterCallProb must be between 0 and 1");
		}
		
		this.userId = userId;
		this.punterCallProb = punterCallProb;
		this.punterStrategy = punterStrategy;	
	}

	public int getUserId() {
		return userId;
	}

	public double getPunterCallProb() {
		return punterCallProb;
	}

	public PunterStrategy getPunterStrategy() {
		return punterStrategy;
	}
}
