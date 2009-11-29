package dk.androbet.game;

/**Header widget
 *  
 * @author korzekwad
 *
 */
public interface Header {

	/** Update the profit/loss information displayed by a header
	 * 
	 * @param winnerProfit
	 * @param looserProfit
	 */
	public void setProfitLoss(double winnerProfit,double looserProfit) ;
	
	/** Updates remaining time before market start time that is displayed in header.
	 * 
	 * @param time Remaining time before market start time.
	 */
	public void setCountDown(long time);
}
