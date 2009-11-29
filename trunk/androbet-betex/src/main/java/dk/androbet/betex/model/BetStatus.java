package dk.androbet.betex.model;


/** Represents bet status, e.g. matched, unmatched, for details see BetFair betStatus.
 * 
 * @author daniel
 *
 */
public enum BetStatus {

	M, U;

	public String value() {
		return name();
	}

	public static BetStatus fromValue(String v) {
		return valueOf(v);
	}

}