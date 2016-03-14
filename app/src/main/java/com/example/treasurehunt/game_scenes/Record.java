package com.example.treasurehunt.game_scenes;

/*
 * Class Score
 * 
 * @author 8A Tran Trong Viet
 * 
 */

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

import com.example.treasurehunt.R;

public class Record extends Activity {
	private Typeface font2;
	private TextView intro, playerName, scoreRecord, levelRecord;
	
	private SharedPreferences gamePrefs;

	/*
	 * Show high score
	 * 
	 * @author 8A Tran Trong Viet
	 * 
	 * @param sc: savedInstanceState: the state of previous game
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_record);
		font2 = Typeface.createFromAsset(getBaseContext().getAssets(),
				"fonts/Sketch_Block.ttf");
		initView();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main_menu, menu);
		return true;
	}

	/*
	 * Initial view
	 * 
	 * @author 8A Tran Trong Viet
	 */
	private void initView() {
		intro  = (TextView) findViewById(R.id.intro);
		intro.setTypeface(font2);
		playerName  = (TextView) findViewById(R.id.player_name);
		playerName.setTypeface(font2);
		scoreRecord  = (TextView) findViewById(R.id.score_record);
		scoreRecord.setTypeface(font2);
		levelRecord  = (TextView) findViewById(R.id.level_record);
		levelRecord.setTypeface(font2);

		TextView scoreView = (TextView) findViewById(R.id.high_scores_list);
		scoreView.setTypeface(font2); 

		// get shared prefs
		SharedPreferences scorePrefs = getSharedPreferences(Game.GAME_PREFS, 0);
		String[] savedScores = scorePrefs.getString("highScores", "").split(
				"\\|");

		// build string
		StringBuilder scoreBuild = new StringBuilder();
		for (String score : savedScores) {
			scoreBuild.append(score).append("\n");
		}

		scoreView.setText(scoreBuild.toString());
	}
}
