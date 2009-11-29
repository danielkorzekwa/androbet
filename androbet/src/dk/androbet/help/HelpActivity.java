package dk.androbet.help;

import dk.androbet.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

public class HelpActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// turn off the window's title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.help);
	}
}
