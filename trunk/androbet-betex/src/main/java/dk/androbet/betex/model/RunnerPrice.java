package dk.androbet.betex.model;

/**
 * Data model for runner price.
 * 
 * @author korzekwad
 * 
 */
public class RunnerPrice implements Comparable<RunnerPrice> {
	private double price;

	private double totalToBack;

	private double totalToLay;

	public double getPrice() {
		return price;
	}

	public RunnerPrice() {
	}
	public RunnerPrice(double price,double totalToBack,double totalToLay) {
		this.price = price;
		this.totalToBack = totalToBack;
		this.totalToLay = totalToLay;
	}
	
	public void setPrice(double price) {
		this.price = price;
	}




	public double getTotalToBack() {
		return totalToBack;
	}




	public void setTotalToBack(double totalToBack) {
		this.totalToBack = totalToBack;
	}




	public double getTotalToLay() {
		return totalToLay;
	}




	public void setTotalToLay(double totalToLay) {
		this.totalToLay = totalToLay;
	}




	@Override
	public int compareTo(RunnerPrice another) {
		return Double.compare(price,another.getPrice());
	}
}
