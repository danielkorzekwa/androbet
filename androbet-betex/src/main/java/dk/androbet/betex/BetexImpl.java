package dk.androbet.betex;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;

import dk.androbet.betex.model.Bet;
import dk.androbet.betex.model.BetStatus;
import dk.androbet.betex.model.BetType;
import dk.androbet.betex.model.RunnerPrice;
import dk.androbet.betex.utils.PriceUtil;
import dk.androbet.betex.utils.PriceUtil.PriceRange;

public class BetexImpl implements Betex {

	/**
	 * Unmatched back bets, map is sorted by price. key - price, value - queue of bets
	 */
	private SortedMap<Double, LinkedBlockingQueue<Bet>> unmatchedBackBetsMap = new TreeMap<Double, LinkedBlockingQueue<Bet>>();

	/**
	 * Unmatched lay bets, map is sorted by price. key - price, value - queue of bets
	 */
	private SortedMap<Double, LinkedBlockingQueue<Bet>> unmatchedLayBetsMap = new TreeMap<Double, LinkedBlockingQueue<Bet>>();

	/**  Matched lay and back bets.*/
	private List<Bet> matchedBets = new ArrayList<Bet>();
	private AtomicLong betIdsequence = new AtomicLong();
	
	/**Only bet with a valid price can be placed*/
	private List<PriceRange> priceRanges = PriceUtil.getPriceRanges();
	
	private ReentrantLock lock = new ReentrantLock();
	
	public BetexImpl() {
	}

	public void initWithSampleData() {
		
		lock.lock();
		try {
			 placeBet(100, BetType.L,3, 2.3);
			 placeBet(100, BetType.L,5, 2.2);
			 
			 placeBet(100, BetType.B,8, 2.4);
			 placeBet(100, BetType.B,25, 2.5);
		}
		finally {
			lock.unlock();
		} 
	}
	
	@Override
	public double getTotalMatched() {
		lock.lock();
		try {
			double totalMatched=0;
			for(Bet bet: matchedBets) {
				totalMatched+=bet.getSize();
			}
			return totalMatched;
		}
		finally {
			lock.unlock();
		}
	}

	@Override
	public List<RunnerPrice> getRunnerPrices() {
		lock.lock();
		try {
			SortedMap<Double,RunnerPrice> runnerPricesMap = new TreeMap<Double, RunnerPrice>();
			
			for(Double price: unmatchedBackBetsMap.keySet()) {
				double totalToLay=0;
				
				LinkedBlockingQueue<Bet> bets = unmatchedBackBetsMap.get(price);
				for(Bet bet: bets) {
					totalToLay += bet.getSize();
				}
				RunnerPrice runnerPrice = runnerPricesMap.get(price);
				if(runnerPrice==null) {
					runnerPrice = new RunnerPrice();
					runnerPrice.setPrice(price);
					runnerPricesMap.put(price,runnerPrice);
				}
				runnerPrice.setTotalToLay(runnerPrice.getTotalToLay() + totalToLay);
			}
			
			for(Double price: unmatchedLayBetsMap.keySet()) {
				double totalToBack=0;
				
				LinkedBlockingQueue<Bet> bets = unmatchedLayBetsMap.get(price);
				for(Bet bet: bets) {
					totalToBack += bet.getSize();
				}
				RunnerPrice runnerPrice = runnerPricesMap.get(price);
				if(runnerPrice==null) {
					runnerPrice = new RunnerPrice();
					runnerPrice.setPrice(price);
					runnerPricesMap.put(price,runnerPrice);
				}
				runnerPrice.setTotalToBack(runnerPrice.getTotalToBack() + totalToBack);
			}
			
		
			return new ArrayList<RunnerPrice>(runnerPricesMap.values());		
		}
		finally {
			lock.unlock();
		}
	}
	
	@Override
	public List<Bet> getMUBets(int userId) {
		lock.lock();
		try {
			List<Bet> muBets = new ArrayList<Bet>();

			for (LinkedBlockingQueue<Bet> backBets : unmatchedBackBetsMap.values()) {
				for (Bet bet : backBets.toArray(new Bet[0])) {
					if (bet.getUserId() == userId) {
						muBets.add(bet);
					}
				}
			}
			for (LinkedBlockingQueue<Bet> layBets : unmatchedLayBetsMap.values()) {
				for (Bet bet : layBets.toArray(new Bet[0])) {
					if (bet.getUserId() == userId) {
						muBets.add(bet);
					}
				}
			}
			
			/**Add matched bets.*/
			for(Bet bet: matchedBets) {
				if (bet.getUserId() == userId) {
					muBets.add(bet);
				}
			}

			return muBets;
		}
		finally {
			lock.unlock();
		}
	}

	@Override
	public long placeBet(int userId, BetType betType, double size, double price) {
		lock.lock();
		try {
			double validatePrice = PriceUtil.validatePrice(priceRanges, price, PriceUtil.ROUND_DOWN);
			if(price!=validatePrice) {
				throw new IllegalArgumentException("Price is not valid:" + price + ":" + validatePrice);
			}
			
			if(size<2) {
				throw new IllegalArgumentException("Min bet size is 2");
			}
			if(price<1.05) {
				throw new IllegalArgumentException("Min price size is 1.05");
			}
			if(price>950) {
				throw new IllegalArgumentException("Max price size is 950");
			}
			
			Bet bet = new Bet();
			bet.setUserId(userId);
			bet.setBetId(betIdsequence.incrementAndGet());
			bet.setBetStatus(BetStatus.U);
			bet.setBetType(betType);
			bet.setPlacedDate(new Date(System.currentTimeMillis()));
			bet.setSize(size);
			bet.setPrice(price);

			matchBet(bet);
			
			return bet.getBetId();
		}
		finally {
			lock.unlock();
		}
	}
	
	/**Match bet, then put matched portions to matched bets and put unmatched portion to unmatched bets.*/
	private void matchBet(Bet bet) {
		
		/** Put bet to the queue. */
		if (bet.getBetType() == BetType.B) {
			LinkedBlockingQueue<Bet> backBetsQueue = unmatchedBackBetsMap.get(bet.getPrice());
			if (backBetsQueue == null) {
				backBetsQueue = new LinkedBlockingQueue<Bet>();
				unmatchedBackBetsMap.put(bet.getPrice(), backBetsQueue);
			}
			/**try to match*/
			Date matchedDate = new Date(System.currentTimeMillis());
			while(bet.getSize()>0 && unmatchedLayBetsMap.size()>0 && bet.getPrice()<=unmatchedLayBetsMap.lastKey()) {
				LinkedBlockingQueue<Bet> unmatchedLayBets = unmatchedLayBetsMap.get(unmatchedLayBetsMap.lastKey());
				
				while(bet.getSize()>0 && unmatchedLayBets.size()>0) {
					Bet unmatchedLayBet = unmatchedLayBets.peek();
					double matchedSize = Math.min(bet.getSize(), unmatchedLayBet.getSize());
					
					Bet matchedBackBet = (Bet)bet.clone();
					matchedBackBet.setBetStatus(BetStatus.M);
					matchedBackBet.setSize(matchedSize);
					matchedBackBet.setPrice(unmatchedLayBet.getPrice());
					matchedBackBet.setMatchedDate(matchedDate);
					matchedBets.add(matchedBackBet);
					
					Bet matchedLayBet = (Bet)unmatchedLayBet.clone();
					matchedLayBet.setBetStatus(BetStatus.M);
					matchedLayBet.setSize(matchedSize);
					matchedLayBet.setMatchedDate(matchedDate);
					matchedBets.add(matchedLayBet);
				
					/**Update unmatched bets.*/
					bet.setSize(bet.getSize()-matchedSize);
					if(unmatchedLayBet.getSize()==matchedSize) {
						unmatchedLayBets.remove();
					}
					else {
						unmatchedLayBet.setSize(unmatchedLayBet.getSize()-matchedSize);
					}	
				}	
				/**Remove empty unmatchedBets queue from unmatched bets map.*/
				if(unmatchedLayBets.size()==0) {
					unmatchedLayBetsMap.remove(unmatchedLayBetsMap.lastKey());
				}
			}
			
			/**If bet is not fully matched then add it to unmatched bets.*/
			if(bet.getSize()>0) {
				backBetsQueue.add(bet);
			}

		} else if (bet.getBetType() == BetType.L) {
			LinkedBlockingQueue<Bet> layBetsQueue = unmatchedLayBetsMap.get(bet.getPrice());
			if (layBetsQueue == null) {
				layBetsQueue = new LinkedBlockingQueue<Bet>();
				unmatchedLayBetsMap.put(bet.getPrice(), layBetsQueue);
			}
			/**try to match*/
			Date matchedDate = new Date(System.currentTimeMillis());
			while(bet.getSize()>0 && unmatchedBackBetsMap.size()>0 && bet.getPrice()>=unmatchedBackBetsMap.firstKey()) {
				LinkedBlockingQueue<Bet> unmatchedBackBets = unmatchedBackBetsMap.get(unmatchedBackBetsMap.firstKey());
				
				while(bet.getSize()>0 && unmatchedBackBets.size()>0) {
					Bet unmatchedBackBet = unmatchedBackBets.peek();
					double matchedSize = Math.min(bet.getSize(), unmatchedBackBet.getSize());
					
					Bet matchedLayBet = (Bet)bet.clone();
					matchedLayBet.setBetStatus(BetStatus.M);
					matchedLayBet.setSize(matchedSize);
					matchedLayBet.setPrice(unmatchedBackBet.getPrice());
					matchedLayBet.setMatchedDate(matchedDate);
					matchedBets.add(matchedLayBet);
					
					Bet matchedBackBet = (Bet)unmatchedBackBet.clone();
					matchedBackBet.setBetStatus(BetStatus.M);
					matchedBackBet.setSize(matchedSize);
					matchedBackBet.setMatchedDate(matchedDate);
					matchedBets.add(matchedBackBet);
				
					/**Update unmatched bets.*/
					bet.setSize(bet.getSize()-matchedSize);
					if(unmatchedBackBet.getSize()==matchedSize) {
						unmatchedBackBets.remove();
					}
					else {
						unmatchedBackBet.setSize(unmatchedBackBet.getSize()-matchedSize);
					}	
				}	
				
				/**Remove empty unmatchedBets queue from unmatched bets map.*/
				if(unmatchedBackBets.size()==0) {
					unmatchedBackBetsMap.remove(unmatchedBackBetsMap.firstKey());
				}
			}
			
			/**If bet is not fully matched then add it to unmatched bets.*/
			if(bet.getSize()>0) {
				layBetsQueue.add(bet);
			}
		} else {
			throw new IllegalArgumentException("Not supported bet type: " + bet.getBetType());
		}

	}

}
