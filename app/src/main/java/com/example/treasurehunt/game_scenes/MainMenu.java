package com.example.treasurehunt.game_scenes;

import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.example.treasurehunt.R;

/*
 * This is a main menu of this game
 * @author group 8
 */
public class MainMenu extends Activity implements OnClickListener {

    /*
     * Properties
     */
    private MediaPlayer mp;
    private Button btn;

    /*
     * (non-Javadoc)
     *
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        btn = (Button) findViewById(R.id.settingBtn);

        mp = MediaPlayer.create(MainMenu.this, R.raw.sound1);
        mp.setLooping(true);
        mp.start();
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
     * This code handle the activity
     *
     * @author 8C Pham Duy Hung
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.newGameBtn:
                newGame();
                break;
            case R.id.continueBtn:
                continueGame();
                break;
            case R.id.settingBtn:
                settingGame();
                break;
            case R.id.recordBtn:
                startActivity(new Intent(this, Record.class));
                break;
            case R.id.instructionBtn:
                startActivity(new Intent(this, Instruction.class));
                break;
        }
    }

    private void settingGame() {
        if (mp.isPlaying()) {
            btn.setBackgroundResource(R.drawable.mute);
            mp.pause();
            try {
                mp.prepare();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mp.seekTo(0);
        } else {
            btn.setBackgroundResource(R.drawable.sound);
            mp.start();
        }
    }

    private void continueGame() {
        if (mp.isPlaying() && mp.isLooping()) {
            btn.setBackgroundResource(R.drawable.mute);
            mp.pause();
        }

        SharedPreferences gameSavePrefs = getSharedPreferences(
                Game.GAME_PREFS, 0);

        String savedGameStr = gameSavePrefs.getString("saveGame", "");

        if (savedGameStr.length() > 0) {
            String[] parts = savedGameStr.split(" - ");
            int level, score, lives;

            level = Integer.parseInt(parts[0]);
            score = Integer.parseInt(parts[1]);
            lives = Integer.parseInt(parts[2]);

            // clear the saved game state
            gameSavePrefs.edit().putString("saveGame", "").commit();

            // load saved state to game play
            Intent openContinueGame = new Intent(MainMenu.this, Game.class);
            openContinueGame.putExtra("Level", level);
            openContinueGame.putExtra("Total Score", score);
            openContinueGame.putExtra("Lives", lives);
            startActivity(openContinueGame);
        } else {
            Toast dialog = Toast.makeText(MainMenu.this,
                    "There 's no game to continue !!!", Toast.LENGTH_SHORT);
            dialog.setGravity(Gravity.CENTER, 0, 0);
            dialog.setDuration(Toast.LENGTH_SHORT);
            dialog.show();
        }
    }

    private void newGame() {
        if (mp.isPlaying()) {
            btn.setBackgroundResource(R.drawable.mute);
            mp.pause();
        }

        Intent openNewGame = new Intent(MainMenu.this, Game.class);
        openNewGame.putExtra("Level", "1");
        openNewGame.putExtra("Total Score", "0");
        openNewGame.putExtra("Lives", "3");
        startActivity(openNewGame);
    }

    /*
     * (non-Javadoc)
     *
     * @see android.app.Activity#onBackPressed()
     */
    @Override
    public void onBackPressed() {
        System.exit(0);
    }

}
