package dk.androbet.betex.puntersimulator;

import dk.androbet.betex.puntersimulator.punter.Punter;

/**Allows to add create punters and call them.
 * 
 * @author korzekwad
 *
 */
public interface PunterManager {

	/**Add new punter*/
	public void addPunter(Punter punter);
	
	/**Process all punters.
	 * 
	 * @param numOfTimes How many times call all punters
	 * 
	 */
	public void process(int numOfTimes); 
}
