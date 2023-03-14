package com.manokero.underwaterworld;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class UnderwaterWorld extends Game {
	private Submarine submarineClass;
	private Bonus bonusClass;
	private Stones stonesClass;
	private Life lifeClass;
	private BitmapUI bitmapUI;
	private SpriteBatch batch;

	private Texture background;
	private Texture gameOverT;
	private Texture splashT;
	private Texture victoryT;

	private float gameScore;
	private int gameState;
	private int screenWidth;
	private int screenHeight;

	public int getNumberOfStonesAndBonuses() {
		return 100;
	}

	@Override
	public void create() {
		batch = new SpriteBatch();
		submarineClass = new Submarine();
		bonusClass = new Bonus();
		stonesClass = new Stones();
		lifeClass = new Life();
		bitmapUI = new BitmapUI();
		screenWidth = Gdx.graphics.getWidth();
		screenHeight = Gdx.graphics.getHeight();

		background = new Texture("background.jpeg");
		gameOverT = new Texture("gameover.png");
		splashT = new Texture("underwaterworld.png");
		victoryT = new Texture("victory.png");

		submarineClass.create();
		stonesClass.create();
		bonusClass.create();
		lifeClass.create();
		bitmapUI.create();
	}

	@Override
	public void render() {
		batch.begin();
		if (gameState == 0) {
			startWelcomeSplash();
		} else if (gameState == 1) {
			startNewGame(batch);
		} else if (gameState == 2) {
			startGameOverScreen();
		} else if (gameState == 3) {
			startVictoryScreen();
		}
		batch.end();
	}

	@Override
	public void dispose() {
	}

	private void startWelcomeSplash() {
		batch.draw(splashT, 0, 0, screenWidth, screenHeight);
		if (Gdx.input.justTouched()) {
			gameState = 1;
		}
	}

	private void startGameOverScreen() {
		batch.draw(gameOverT, 0, 0, screenWidth, screenHeight);
		if (Gdx.input.justTouched()) {
			recreate();
		}
	}

	private void startVictoryScreen() {
		batch.draw(victoryT, 0, 0, screenWidth, screenHeight);
		if (Gdx.input.justTouched()) {
			recreate();
		}
	}

	private void startNewGame(SpriteBatch batch) {
		setParamToClasses(batch);
		gameScore += Gdx.graphics.getDeltaTime();
		bitmapUI.drawUI(batch);
		gameState = lifeClass.getGameState();
		if (gameScore >= 100f) {
			gameState = 3;
		}
	}

	private void setParamToClasses(SpriteBatch batch){
		batch.draw(background, 0, 0, screenWidth, screenHeight);
		bitmapUI.setBonusScore(lifeClass.getBonusScoreLife());
		bitmapUI.setGameScore(gameScore);

		lifeClass.setStoneCircleArray(stonesClass.getStoneCircleArray());
		lifeClass.setBonusCircleArray(bonusClass.getBonusCircleArray());
		lifeClass.setSubmarineCircle(submarineClass.getSubmarineCircle());

		stonesClass.setIsCollidedWithStone(lifeClass.getIsCollidedWithStone());
		bonusClass.setIsCollidedWithBonus(lifeClass.getIsCollidedWithBonus());

		submarineClass.render(batch);
		stonesClass.render(batch);
		bonusClass.render(batch);
		lifeClass.render(batch);
	}

	private void recreate() {
		bonusClass.create();
		submarineClass.create();
		lifeClass.create();
		stonesClass.create();
		gameState = 1;
		gameScore = 0;
	}
}