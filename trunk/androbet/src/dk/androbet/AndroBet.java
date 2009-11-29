package dk.androbet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import dk.androbet.game.GameActivity;
import dk.androbet.help.HelpActivity;

/**Start activity of a game with buttons to start/exit game, etc.
 * 
 * @author korzekwad
 *
 */
public class AndroBet extends Activity {
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// turn off the window's title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);

		Button startGameButton = (Button)findViewById(R.id.StartGameButton);
		startGameButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	Intent intent = new Intent(AndroBet.this,GameActivity.class);
        		startActivity(intent);
            }		
        });

		Button helpGameButton = (Button)findViewById(R.id.HelpButton);	
		helpGameButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
              Intent intent = new Intent(AndroBet.this,HelpActivity.class);
              startActivity(intent);
            }		
        });
		
		Button exitGameButton = (Button)findViewById(R.id.ExitGameButton);	
		exitGameButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
               finish();
            }		
        });
	}
}