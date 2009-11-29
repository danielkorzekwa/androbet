package dk.androbet.game;

import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.Window;
import dk.androbet.R;
import dk.androbet.betex.BetexImpl;
import dk.androbet.betex.model.Bet;
import dk.androbet.betex.model.BetType;
import dk.androbet.betex.model.LadderPrices;
import dk.androbet.betex.model.RunnerPrice;
import dk.androbet.betex.puntersimulator.PunterManager;
import dk.androbet.betex.puntersimulator.PunterManagerImpl;
import dk.androbet.betex.puntersimulator.punter.FillEmptyPricesPunter;
import dk.androbet.betex.puntersimulator.punter.HappyLooserPunter;
import dk.androbet.betex.puntersimulator.punter.Punter;
import dk.androbet.betex.puntersimulator.punter.TradingPunter;
import dk.androbet.betex.utils.AvgPriceUtil;
import dk.androbet.betex.utils.LadderPricesUtil;
import dk.androbet.betex.utils.ProfitLossUtil;
import dk.androbet.betex.utils.RoundUtil;

/**
 * Main activity of a game. Provides ladder price panel and allows user to place bets. In the of a game results are
 * displayed.
 * 
 * @author korzekwad
 * 
 */
public class GameActivity extends Activity {

	private static final int DIALOG_CANT_PLACE_BET = 1;
	private static final int DIALOG_END_OF_GAME = 2;

	/** Length of game in milliseconds. */
	private static long GAME_LENGHT = 1000 * 180;
	
	/** Betting exchange user id that application user uses to place bets. */
	private static final int USER_ID = 1;
	
	private BetexImpl betex = new BetexImpl();

	/** Virtual punters that simulate liquidity. */
	private PunterManager punterManager = new PunterManagerImpl(betex);

	private Header header;
	private LadderPrice ladderPrice;

	private UpdateUITask updateUITask;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// turn off the window's title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.game);
	
		header = (Header) findViewById(R.id.gameHeader);
		ladderPrice = (LadderPrice) findViewById(R.id.gameLadderPrice);

		/** Add listener for user interaction with PriceLadder. */
		ladderPrice.setOnClickUserBetListener(new OnClickUserBetListener() {
			@Override
			public void onClick(BetType betType, double price,double size) {
				try {
					betex.placeBet(USER_ID, betType, size, price);
				} catch (Exception e) {
					runOnUiThread(new ShowDialogRunnable(DIALOG_CANT_PLACE_BET));
				}
			}
		});

		punterManager.addPunter(new Punter(101,0.05,new HappyLooserPunter()));	
		punterManager.addPunter(new Punter(102,0.05,new TradingPunter()));	
		punterManager.addPunter(new Punter(103,0.5,new FillEmptyPricesPunter()));	
		punterManager.process(100);
				
		doWork(GAME_LENGHT);

		updateUITask = new UpdateUITask();
		updateUITask.startGame();
	}

	/**
	 * Updates ladder price component with newest prices/bets and updates header component with winnings and remaining
	 * time of game. Calls virtual punters to generate virtual bets on a betting exchange.
	 */
	private void doWork(long remainingTime) {

		punterManager.process(1);
	
		List<RunnerPrice> runnerPrices = betex.getRunnerPrices();
		List<Bet> bets = betex.getMUBets(USER_ID);
		LadderPrices ladderPrices = LadderPricesUtil.getThreeBackLayRunnerPrices(runnerPrices);
		
		double winnerProfit = ProfitLossUtil.calculateProfit(bets, true);
		double looserProfit = ProfitLossUtil.calculateProfit(bets, false);
		runOnUiThread(new UpdateUIRunnable(ladderPrices, bets, winnerProfit, looserProfit, remainingTime));
	}

	@Override
	public void finish() {
		super.finish();
		updateUITask.stopGame();
	}

	protected Dialog onCreateDialog(int id) {
		Dialog dialog = null;
		switch (id) {
		case DIALOG_CANT_PLACE_BET:
			dialog = new AlertDialog.Builder(this).setTitle("Can't place a bet.").setPositiveButton("OK", null)
					.create();
			break;
		case DIALOG_END_OF_GAME:
			dialog = createGameResultDialog();
			break;
		default:
			dialog = null;
		}
		return dialog;
	}

	private AlertDialog createGameResultDialog() {
		OnClickListener goToMenuListener = new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				finish();
			}
		};

		double avgPrice = AvgPriceUtil.getAvgPrice(betex.getRunnerPrices());
		double avgPriceProb = 1 / avgPrice;
		Random rand = new Random(System.currentTimeMillis());
		boolean outcome = avgPriceProb > rand.nextFloat();
		String outcomeString = outcome ? "Winner" : "Looser";
		
		List<Bet> bets = betex.getMUBets(USER_ID);
		double outcomeProfit = outcome ? RoundUtil.round(ProfitLossUtil.calculateProfit(bets, true), 2) : RoundUtil.round(ProfitLossUtil.calculateProfit(bets, false), 2);
		String profitLoss = outcomeProfit >= 0 ? "You won " + outcomeProfit + "£" : "You lost " + outcomeProfit + "£";
		
		StringBuilder gameResults = new StringBuilder().append("Starting price: ").append(RoundUtil.round(avgPrice, 2)).append("\n").append(
		"Runner outcome: ").append(outcomeString).append("\n\n").append(profitLoss);
		
		AlertDialog dialog = new AlertDialog.Builder(this).setTitle("Game result").setMessage(gameResults.toString())
				.setPositiveButton("Continue", goToMenuListener).create();
		return dialog;
	}

	/**
	 * Background task that updates betting ui.
	 * 
	 * @author korzekwad
	 * 
	 */
	private class UpdateUITask extends Thread {

		/** Starting time of the game - allows to measure the remaining time of game. */
		private long gameStart;
		private boolean running = true;

		public void startGame() {
			gameStart = System.currentTimeMillis();
			start();
		}
		
		/** Stop and wait until thread is stopped. */
		public void stopGame() {
			running = false;
			try {
				join();
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}

		@Override
		public void run() {
			while (running) {
				try {
					Thread.sleep(100);

					/** Remaining time of game. */
					long remainingTime = GAME_LENGHT - (System.currentTimeMillis() - gameStart);
					/** If count down < 0 then it is the end of game */
					if (remainingTime >= 0) {
						doWork(remainingTime);
					} else {
						running = false;
						runOnUiThread(new ShowDialogRunnable(DIALOG_END_OF_GAME));
					}

				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
			}
		}

	}

	private class UpdateUIRunnable implements Runnable {

		private final List<Bet> bets;
		private final LadderPrices ladderPrices;
		private final long countDown;
		private final double profit;
		private final double loss;

		/**
		 * 
		 * @param ladderPrices
		 * @param bets
		 * @param countDown
		 *            Remaining time before market start time
		 */
		public UpdateUIRunnable(LadderPrices ladderPrices, List<Bet> bets, double profit, double loss, long countDown) {

			this.ladderPrices = ladderPrices;
			this.bets = bets;
			this.profit = profit;
			this.loss = loss;
			this.countDown = countDown;
		}

		@Override
		public void run() {

			header.setProfitLoss(profit, loss);
			header.setCountDown(countDown);

			/** Update ladder price with prices and bets. */
			ladderPrice.update(ladderPrices, bets);
		}
	}

	private class ShowDialogRunnable implements Runnable {

		private final int dialogId;

		public ShowDialogRunnable(int dialogId) {
			this.dialogId = dialogId;
		}

		@Override
		public void run() {
			showDialog(dialogId);
		}
	}

}
