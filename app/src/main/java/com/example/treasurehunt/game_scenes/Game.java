package com.example.treasurehunt.game_scenes;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.example.treasurehunt.GameStatus;
import com.example.treasurehunt.Quiz;
import com.example.treasurehunt.R;
import com.example.treasurehunt.Score;
import com.example.treasurehunt.cell.Cell;
import com.example.treasurehunt.game_controllers.NormalGameController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/*
 * The main game
 * @author group 8
 */

public class Game extends Activity implements Observer {

    /*
     * Properties
     */
    private TableLayout map;
    private int trapsRemain = 0;

    private int cellWidth = 34;
    private int cellPadding = 2;

    NormalGameController gameController;

    // Sound
    private MediaPlayer mp;

    // Popup
    Handler popupHandler;

    // Texts
    Typeface numberFont, textFont;
    TextView levelText, scoreText, timeText, livesText, finalScoreText,
            finalTimeText, trapText;

    // UI
    ImageView mImgViewResult;

    // Save Score
    private SharedPreferences gamePrefs;
    public static final String GAME_PREFS = "ArithmeticFile";

    /*
     * (non-Javadoc)
     *
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // @author 8C Pham Duy Hung
        try {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                gameController = new NormalGameController(
                        new GameStatus(
                                Integer.parseInt(extras.getString("Lives")),
                                Integer.parseInt(extras.getString("Total Score")),
                                Integer.parseInt(extras.getString("Level"))));
            } else {
                loadGameFailed();
            }
        } catch (Exception e) {
            loadGameFailed();
        }

        // Share preference of Game class
        gamePrefs = getSharedPreferences(GAME_PREFS, 0);
        initView();
        startNewGame();
    }

    private void loadGameFailed() {
        Toast.makeText(this, "Cannot load the game", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, MainMenu.class));
    }

    /*
     * (non-Javadoc)
     *
     * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.game, menu);
        return true;
    }

    /*
     * (non-Javadoc)
     *
     * @see android.app.Activity#onBackPressed()
     */
    @Override
    public void onBackPressed() {
        gameController.backBtnPressed(gamePrefs);
        finish();
        super.onBackPressed();
    }

    /*
     * Initial view
     *
     * @author 8B Pham Hung Cuong
     */
    @SuppressWarnings("deprecation")
    private void initView() {
        // @author 8A Tran Trong Viet
        mp = MediaPlayer.create(Game.this, R.raw.flag);
        map = (TableLayout) findViewById(R.id.Map);

        numberFont = Typeface.createFromAsset(getBaseContext().getAssets(),
                "fonts/FRANCHISE-BOLD.TTF");
        textFont = Typeface.createFromAsset(getBaseContext().getAssets(),
                "fonts/Sketch_Block.ttf");

        levelText = (TextView) findViewById(R.id.levelText);
        levelText.setTypeface(textFont);
        levelText.setText(String.format("LEVEL %d", gameController.getGameStatus().getLevel()));

        scoreText = (TextView) findViewById(R.id.scoreText);
        scoreText.setTypeface(textFont);
        scoreText.setText(gameController.getGameStatus().getScore());

        timeText = (TextView) findViewById(R.id.timeText);
        timeText.setTypeface(textFont);

        livesText = (TextView) findViewById(R.id.livesText);
        livesText.setTypeface(textFont);
        livesText.setText(gameController.getGameStatus().getLives());

        trapText = (TextView) findViewById(R.id.trapText);
        trapText.setTypeface(textFont);
        trapText.setText(trapsRemain);

        mImgViewResult = (ImageView) findViewById(R.id.img_result);
    }

    /*
     * Start the game
     *
     * @author 8C Pham Duy Hung
     */
    private void startNewGame() {
        gameController.createMap(this);
        showMap();
        timeText.setText(timer);
    }

    /*
     * Show map procedure
     *
     * @author 8C Pham Duy Hung
     */
    private void showMap() {
        int numberOfRows = gameController.getBoardSize().getRows();
        int numberOfColumns = gameController.getBoardSize().getCols();

        // remember we will not show 0th and last Row and Columns
        // they are used for calculation purposes only
        for (int row = 1; row < numberOfRows + 1; row++) {
            TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(new LayoutParams(
                    (cellWidth + 2 * cellPadding) * numberOfColumns, cellWidth
                    + 2 * cellPadding));

            for (int column = 1; column < numberOfColumns + 1; column++) {
                cells[row][column].setLayoutParams(new LayoutParams(cellWidth
                        + 2 * cellPadding, cellWidth + 2 * cellPadding));
                cells[row][column].setPadding(cellPadding, cellPadding,
                        cellPadding, cellPadding);
                tableRow.addView(cells[row][column]);
            }
            map.addView(tableRow, new TableLayout.LayoutParams(
                    (cellWidth + 2 * cellPadding) * numberOfColumns, cellWidth
                    + 2 * cellPadding));
        }
    }

    /*
     * Show Winning pop-up
     *
     * @author Pham Hung Cuong
     */
    private void showWinPopUp() {
        if (!isGameOver) {
            isGameOver = true; // mark game as over
            final Dialog popup = new Dialog(Game.this);
            popup.requestWindowFeature(Window.FEATURE_NO_TITLE);
            popup.getWindow().setBackgroundDrawable(
                    new ColorDrawable(android.graphics.Color.TRANSPARENT));
            popup.setContentView(R.layout.win_popup);
            // Set dialog title

            popup.setCancelable(false);

            finalScoreText = (TextView) popup.findViewById(R.id.finalScore);
            finalScoreText.setTypeface(numberFont);
            finalScoreText.setText(gameController.getGameStatus().getScore());

            finalTimeText = (TextView) popup.findViewById(R.id.finalTime);
            finalTimeText.setTypeface(numberFont);
            finalTimeText.setText(timer);

            popup.dismiss();
            popup.show();

            Button saveRecordBtn = (Button) popup
                    .findViewById(R.id.save_record);
            saveRecordBtn.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(
                            Game.this);

                    alert.setTitle("Enter your name");

                    // Set an EditText view to get user
                    final EditText input = new EditText(Game.this);
                    input.setText("Playername");
                    alert.setView(input);

                    alert.setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int whichButton) {
                                    String value = input.getText().toString();
                                    setHighScore(value, gameController.getGameStatus().getScore(), gameController.getGameStatus().getLevel());
                                }
                            });

                    alert.setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int whichButton) {
                                    // Canceled.
                                }
                            });
                    alert.show();
                }
            });

            Button quitToMenuBtn = (Button) popup
                    .findViewById(R.id.quit_to_menu);
            quitToMenuBtn.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    popup.dismiss();
                    Intent backToMenu = new Intent(Game.this, MainMenu.class);
                    startActivity(backToMenu);
                    finish();
                }
            });

            Button nextLevelBtn = (Button) popup.findViewById(R.id.next_level);
            nextLevelBtn.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    gameController.getGameStatus().nextLevel();
                    int level = gameController.getGameStatus().getLevel();
                    int totalScore = gameController.getGameStatus().getScore();
                    int lives = gameController.getGameStatus().getLives();

                    if (level == 5 || level == 10) {
                        startActivity(createIntent(Game.this, Quiz.class, level, totalScore, lives));
                        finish();
                    } else if (level < 16) {
                        startActivity(createIntent(Game.this, Game.class, level, totalScore, lives));
                        finish();
                    } else {
                        Toast.makeText(Game.this, "Congratulation, you win!!",
                                Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Game.this,
                                MainMenu.class));
                        finish();
                    }
                }
            });

            Button postToFbBtn = (Button) popup.findViewById(R.id.post_to_fb);
            postToFbBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }

    private Intent createIntent(Context packageContext, Class<?> cls, int level, int totalScore, int lives) {
        Intent nextLevel = new Intent(packageContext, cls);
        nextLevel.putExtra("Level", level);
        nextLevel.putExtra("Total Score", totalScore);
        nextLevel.putExtra("Lives", lives);
        return nextLevel;
    }

    /*
     * Finish the game
     *
     * @author 8B Pham Hung Cuong
     */
    private void finishGame(int currentRow, int currentColumn) {
        stopTimer(); // stop timer
        isGameStart = false;

        // show all traps
        // disable all traps
        for (int row = 1; row < numberOfRows + 1; row++) {
            for (int column = 1; column < numberOfColumns + 1; column++) {
                // disable block
                cells[row][column].disable();

                // block has trap and is not flagged
                if (cells[row][column].hasTrap()
                        && !cells[row][column].isFlagged()) {
                    // set trap icon
                    cells[row][column].setTrapIcon(false);
                }

                // block is flagged and doesn't not have trap
                if (!cells[row][column].hasTrap()
                        && cells[row][column].isFlagged()) {
                    // set flag icon
                    cells[row][column].setFlagIcon();
                }

                // block is flagged
                if (cells[row][column].isFlagged()) {
                    // disable the block
                    cells[row][column].setClickable(false);
                }

                // set treasure icon
                if (cells[row][column].hasTreasure()) {
                    // set treasure icon
                    cells[row][column].setTreasure();
                }
            }
        }

        // trigger trap
        cells[currentRow][currentColumn].triggerTrap();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                if (timer <= 0) {
                    mImgViewResult.setBackgroundResource(R.drawable.timeout);
                }
                mImgViewResult.setVisibility(View.VISIBLE);
                mImgViewResult.bringToFront();
                mImgViewResult.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        mImgViewResult.setVisibility(View.GONE);

                        if (!isGameOver) {
                            isGameOver = true; // mark game as over
                            final Dialog popup = new Dialog(Game.this);
                            popup.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            popup.getWindow()
                                    .setBackgroundDrawable(
                                            new ColorDrawable(
                                                    android.graphics.Color.TRANSPARENT));
                            popup.setContentView(R.layout.finish_popup);
                            // Set dialog title
                            popup.setCancelable(false);

                            finalScoreText = (TextView) popup
                                    .findViewById(R.id.finalScore);
                            finalScoreText.setTypeface(numberFont);
                            finalScoreText.setText("" + totalScore);

                            finalTimeText = (TextView) popup
                                    .findViewById(R.id.finalTime);
                            finalTimeText.setTypeface(numberFont);
                            finalTimeText.setText("" + timer);

                            popup.show();

                            Button saveRecordBtn = (Button) popup
                                    .findViewById(R.id.save_record);
                            saveRecordBtn
                                    .setOnClickListener(new OnClickListener() {

                                        @Override
                                        public void onClick(View v) {
                                            AlertDialog.Builder alert = new AlertDialog.Builder(
                                                    Game.this);

                                            alert.setTitle("Enter your name");

                                            // Set an EditText view to get user
                                            // input
                                            final EditText input = new EditText(
                                                    Game.this);
                                            input.setText("Playername");
                                            alert.setView(input);

                                            alert.setPositiveButton(
                                                    "Ok",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(
                                                                DialogInterface dialog,
                                                                int whichButton) {
                                                            String value = input
                                                                    .getText()
                                                                    .toString();
                                                            // Do something with
                                                            // value!
                                                            setHighScore(value,
                                                                    totalScore,
                                                                    level);

                                                        }
                                                    });

                                            alert.setNegativeButton(
                                                    "Cancel",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(
                                                                DialogInterface dialog,
                                                                int whichButton) {
                                                            // Canceled.
                                                        }
                                                    });

                                            alert.show();
                                        }
                                    });

                            Button quitToMenuBtn = (Button) popup
                                    .findViewById(R.id.quit_to_menu);
                            quitToMenuBtn
                                    .setOnClickListener(new OnClickListener() {

                                        @Override
                                        public void onClick(View v) {
                                            popup.dismiss();
                                            Intent backToMenu = new Intent(
                                                    Game.this, MainMenu.class);
                                            startActivity(backToMenu);
                                            finish();
                                        }
                                    });

                            Button postToFbBtn = (Button) popup
                                    .findViewById(R.id.post_to_fb);
                            postToFbBtn
                                    .setOnClickListener(new OnClickListener() {

                                        @Override
                                        public void onClick(View v) {
                                            popup.dismiss();
                                        }
                                    });
                        }
                    }
                }, 2000);
            }
        }, 500);
    }

    /*
     * Set high score
     *
     * @author 8A Tran Trong Viet
     *
     * @param sc: savedInstanceState: the state of previous game
     */
    public void setHighScore(String playerName, int score, int level) {
        try {
            if (score > 0) {

                SharedPreferences.Editor scoreEdit = gamePrefs.edit();
                // get existing scores
                String scores = gamePrefs.getString("highScores", "");

                // check for scores
                if (scores.length() > 0) {

                    List<Score> scoreStrings = new ArrayList<Score>();
                    String[] exScores = scores.split("\\|");

                    // add score object for each
                    for (String eSc : exScores) {
                        String[] parts = eSc.split(" - ");
                        scoreStrings
                                .add(new Score(parts[0], Integer
                                        .parseInt(parts[1]), Integer
                                        .parseInt(parts[2])));
                    }

                    // new score
                    Score newScore = new Score(playerName, score, level);
                    scoreStrings.add(newScore);
                    Collections.sort(scoreStrings);

                    // get top ten
                    StringBuilder scoreBuild = new StringBuilder();
                    for (int s = 0; s < scoreStrings.size(); s++) {
                        if (s >= 10)
                            break;
                        if (s > 0)
                            scoreBuild.append("|");
                        scoreBuild.append(scoreStrings.get(s).getScoreText());
                    }
                    // write to prefs
                    scoreEdit.putString("highScores", scoreBuild.toString());
                    scoreEdit.commit();

                } else {
                    // no existing scores
                    scoreEdit.putString("highScores", playerName + " - " + score + " - " + level);
                    scoreEdit.commit();
                }

            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Observable observable, Object data) {
        finishGame(0, 0);
    }
}
