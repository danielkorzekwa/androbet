package dk.androbet.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import dk.androbet.betex.model.Bet;
import dk.androbet.betex.model.BetStatus;
import dk.androbet.betex.model.BetType;
import dk.androbet.betex.model.LadderPrices;
import dk.androbet.betex.model.RunnerPrice;

/**
 * Widget displays prices ladder for market runner.
 * 
 * @author korzekwad
 * 
 */
public class LadderPriceImpl extends TableLayout implements LadderPrice {

	private final double BET_SIZE = 2;
	private final int SCREEN_WIDTH = 320;

	private final String BACKGROUND_COLOR = "#ffffff";
	private final String TEXT_COLOR = "#000000";

	private final String BACK_PRICE_COLOR = "#C8D0E4";
	private final String LAY_PRICE_COLOR = "#E4B8D6";

	private static final String LAY_BET_COLOR = "#FFEFFA";
	private static final String BACK_BET_COLOR = "#EFF3FF";

	private OnClickUserBetListener listener;

	/** Rows from 0 to 2 are for back bets, rows from 3 to 5 are for lay bets. */
	private List<LadderPriceRow> priceRows;

	public LadderPriceImpl(Context context, AttributeSet attrs) {
		super(context, attrs);
		initLadderPrice(context);
	}

	public LadderPriceImpl(Context context) {
		super(context);
		initLadderPrice(context);
	}

	private void initLadderPrice(Context ctx) {

		if (priceRows == null) {
			priceRows = new ArrayList<LadderPriceRow>();
			for (int i = 0; i < 3; i++) {
				LadderPriceRow row = new LadderPriceRow(ctx, SCREEN_WIDTH, BACK_PRICE_COLOR);
				addView(row);
				priceRows.add(row);
			}
			for (int i = 0; i < 3; i++) {
				LadderPriceRow row = new LadderPriceRow(ctx, SCREEN_WIDTH, LAY_PRICE_COLOR);
				addView(row);
				priceRows.add(row);
			}
		}
	}

	@Override
	public void setOnClickUserBetListener(OnClickUserBetListener listener) {
		this.listener = listener;
	}

	/**
	 * Refreshes prices ladder with a new data.
	 * 
	 * @param ladderPrices
	 *            best three back and lay prices
	 * @param bets
	 *            user bets
	 */
	public void update(LadderPrices ladderPrices, List<Bet> bets) {

		/** User unmatched lay bets. Key - price, value - totalBetsSize */
		final Map<Double, Double> uLayBets = calculateTotalUBetSize(bets, BetType.L);
		/** User unmatched back bets. Key - price, value - totalBetsSize */
		final Map<Double, Double> uBackBets = calculateTotalUBetSize(bets, BetType.B);

		/** Update back bet rows */
		int backRowIndex = 2;
		for (int i = 0; i < ladderPrices.getPricesToLay().size(); i++) {
			RunnerPrice runnerPrice = ladderPrices.getPricesToLay().get(i);
			priceRows.get(backRowIndex).getPriceValueField().setText("" + runnerPrice.getPrice());
			priceRows.get(backRowIndex).getTotalToBetField().setText("" + runnerPrice.getTotalToLay());

			if (uBackBets.get(runnerPrice.getPrice()) != null) {
				priceRows.get(backRowIndex).getBackColumn().setText("" + uBackBets.get(runnerPrice.getPrice()));
			} else {
				priceRows.get(backRowIndex).getBackColumn().setText("");
			}
			if (uLayBets.get(runnerPrice.getPrice()) != null) {
				priceRows.get(backRowIndex).getLayColumn().setText("" + uLayBets.get(runnerPrice.getPrice()));
			} else {
				priceRows.get(backRowIndex).getLayColumn().setText("");
			}

			backRowIndex--;
		}

		/** Update lay back bet rows */
		int layRowIndex = 5;
		for (int i = 0; i < ladderPrices.getPricesToBack().size(); i++) {
			RunnerPrice runnerPrice = ladderPrices.getPricesToBack().get(i);
			priceRows.get(layRowIndex).getPriceValueField().setText("" + runnerPrice.getPrice());
			priceRows.get(layRowIndex).getTotalToBetField().setText("" + runnerPrice.getTotalToBack());

			if (uLayBets.get(runnerPrice.getPrice()) != null) {
				priceRows.get(layRowIndex).getLayColumn().setText("" + uLayBets.get(runnerPrice.getPrice()));
			} else {
				priceRows.get(layRowIndex).getLayColumn().setText("");
			}
			if (uBackBets.get(runnerPrice.getPrice()) != null) {
				priceRows.get(layRowIndex).getBackColumn().setText("" + uBackBets.get(runnerPrice.getPrice()));
			} else {
				priceRows.get(layRowIndex).getBackColumn().setText("");
			}
			layRowIndex--;
		}
	}

	/**
	 * Calculate total size of unmatched bets grouped by price.
	 * 
	 * @param bets
	 * @param betType
	 * @return key - price, value - total size of unmatched bets for price
	 */
	private Map<Double, Double> calculateTotalUBetSize(List<Bet> bets, BetType betType) {
		/** key - price, value - totalBetsSize */
		Map<Double, Double> totalSizeMap = new HashMap<Double, Double>();

		for (Bet bet : bets) {
			if (bet.getBetStatus() == BetStatus.U && bet.getBetType() == betType) {
				Double totalBetsSize = totalSizeMap.get(bet.getPrice());
				if (totalBetsSize == null) {
					totalBetsSize = new Double(0);
				}
				totalBetsSize += bet.getSize();
				totalSizeMap.put(bet.getPrice(), totalBetsSize);
			}
		}

		return totalSizeMap;
	}

	/** Adapter from OnClickUseBetListener to OnClickListener */
	private class OnclickUserBetAdapter implements OnTouchListener {

		private final BetType betType;
		private final TextView priceValueField;
		private final String betTypeColor;

		public OnclickUserBetAdapter(String betTypeColor, BetType betType, TextView priceValueField) {
			this.betTypeColor = betTypeColor;
			this.betType = betType;
			this.priceValueField = priceValueField;
		}

		@Override
		public boolean onTouch(View v, MotionEvent event) {

			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				v.setBackgroundColor(Color.parseColor(betTypeColor));

			} else if (event.getAction() == MotionEvent.ACTION_UP) {
				v.setBackgroundColor(Color.parseColor(BACKGROUND_COLOR));
				listener.onClick(betType, Double.parseDouble(priceValueField.getText().toString()), BET_SIZE);

				double currentBetsSize = 0;
				String currentBetsSizeText = ((TextView) v).getText().toString();
				if (currentBetsSizeText.length() != 0) {
					currentBetsSize = Double.parseDouble(currentBetsSizeText);
				}
				((TextView) v).setText("" + (currentBetsSize + BET_SIZE));
			}
			return true;
		}

	}

	/**
	 * GUI component that represents one row of ladder price component.
	 * 
	 * @author korzekwad
	 * 
	 */
	public class LadderPriceRow extends TableRow {

		private TextView priceValueField;
		private TextView totalToBetField;

		private TextView layColumn;
		private TextView backColumn;

		public LadderPriceRow(Context context, int width, String rowColor) {
			super(context);

			setBackgroundColor(Color.parseColor(TEXT_COLOR));
			MarginLayoutParams layoutLeftRight = new TableRow.LayoutParams((width - (width / 4)) / 2,
					TableRow.LayoutParams.FILL_PARENT);
			layoutLeftRight.setMargins(1, 0, 0, 1);
			MarginLayoutParams layoutCentre = new TableRow.LayoutParams(width / 4, TableRow.LayoutParams.FILL_PARENT);
			layoutCentre.setMargins(1, 0, 0, 1);

			/** Create price column */
			LinearLayout priceColumn = new LinearLayout(context);
			priceColumn.setOrientation(LinearLayout.VERTICAL);
			priceColumn.setLayoutParams(layoutCentre);
			priceColumn.setBackgroundColor(Color.parseColor(rowColor));
			priceColumn.setPadding(0, 14, 0, 14);

			priceValueField = new TextView(context);
			priceValueField.setTextColor(Color.parseColor(TEXT_COLOR));
			priceValueField.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
			priceValueField.setGravity(Gravity.CENTER);
			priceColumn.addView(priceValueField);

			totalToBetField = new TextView(context);
			totalToBetField.setTextColor(Color.parseColor(TEXT_COLOR));
			totalToBetField.setGravity(Gravity.CENTER);
			priceColumn.addView(totalToBetField);

			/** Create user lay bets column. */
			layColumn = new TextView(context);
			layColumn.setTextColor(Color.parseColor(TEXT_COLOR));
			layColumn.setBackgroundColor(Color.parseColor(BACKGROUND_COLOR));
			layColumn.setGravity(Gravity.CENTER);
			layColumn.setLayoutParams(layoutLeftRight);
			layColumn.setOnTouchListener(new OnclickUserBetAdapter(LAY_BET_COLOR, BetType.L, priceValueField));

			/** Create user back bets column. */
			backColumn = new TextView(context);
			backColumn.setTextColor(Color.parseColor(TEXT_COLOR));
			backColumn.setBackgroundColor(Color.parseColor(BACKGROUND_COLOR));
			backColumn.setGravity(Gravity.CENTER);
			backColumn.setLayoutParams(layoutLeftRight);
			backColumn.setOnTouchListener(new OnclickUserBetAdapter(BACK_BET_COLOR, BetType.B, priceValueField));

			/** Add all columns to the row. */
			addView(backColumn);
			addView(priceColumn);
			addView(layColumn);

		}

		public TextView getPriceValueField() {
			return priceValueField;
		}

		public TextView getTotalToBetField() {
			return totalToBetField;
		}

		public TextView getLayColumn() {
			return layColumn;
		}

		public TextView getBackColumn() {
			return backColumn;
		}

	}

}
