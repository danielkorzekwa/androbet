package dk.androbet.game;

import java.text.DecimalFormat;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import dk.androbet.betex.utils.RoundUtil;

/**
 * Header widget
 * 
 * @author korzekwad
 * 
 */
public class HeaderImpl extends LinearLayout implements Header {

	private TextView profitLoss;
	private TextView countDown;

	private DecimalFormat decimalFormat = new DecimalFormat("00");

	public HeaderImpl(Context context, AttributeSet attrs) {
		super(context, attrs);
		initHeader(context);
	}

	public HeaderImpl(Context context) {
		super(context);
		initHeader(context);
	}

	private void initHeader(Context context) {
		setBackgroundColor(Color.parseColor("#BFBFBF"));

		/** Add profit loss field */
		profitLoss = new TextView(context);
		profitLoss.setPadding(2, 2, 2, 2);
		profitLoss.setGravity(Gravity.LEFT);
		profitLoss.setTextColor(Color.parseColor("#000000"));
		profitLoss.setTextSize(29);
		setProfitLoss(0, 0);
		addView(profitLoss, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT, 1));

		/** Add count down field. */
		countDown = new TextView(context);
		countDown.setPadding(2, 2, 2, 2);
		countDown.setGravity(Gravity.RIGHT);
		countDown.setTextColor(Color.parseColor("#000000"));
		countDown.setTextSize(29);
		setCountDown(0);
		addView(countDown, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT, 2));
	}

	public void setProfitLoss(double winnerProfit, double looserProfit) {

		profitLoss.setText(RoundUtil.round(winnerProfit, 2) + "/" + RoundUtil.round(looserProfit, 2));
		if (winnerProfit < 0 || looserProfit < 0) {
			profitLoss.setTextColor(Color.parseColor("#960000"));
		} else if (winnerProfit > 0 || looserProfit > 0) {
			profitLoss.setTextColor(Color.parseColor("#009600"));
		} else if (winnerProfit == 0 && looserProfit == 0) {
			profitLoss.setTextColor(Color.parseColor("#000000"));
		}
	}

	@Override
	public void setCountDown(long time) {

		if (time < 0) {
			time = 0;
		}
		long minutes = time / 1000 / 60;
		long seconds = time / 1000 % 60;

		countDown.setText(decimalFormat.format(minutes) + ":" + decimalFormat.format(seconds));
	}
}
