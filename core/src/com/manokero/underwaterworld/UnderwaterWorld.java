package com.manokero.underwaterworld;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;

import java.util.Random;

public class UnderwaterWorld extends Game {
	//ShapeRenderer shapeRenderer;
	MyInputAdapter inputAdapter;

	SpriteBatch batch;
	Texture background;
	Texture rockTexture;
	Texture submarine_life1;
	Texture submarine_life2;
	Texture submarine_life3;
	Texture bonusTexture;
	Texture gameOverT;
	Texture splashT;
	Texture victoryT;

	Texture[] submarine;
	Sprite[] bonuses;
	Sprite[] rocks;
	Circle[] stoneCircle;
	Circle[] bonusCircle;
	Circle submarineCircle;

	BitmapFont scoreFont;
	BitmapFont scoreText;
	BitmapFont bonusFont;
	BitmapFont bonusText;

	GlyphLayout layout;
	GlyphLayout bonusLayout;
	Random random;

	float gameScore = 0;
	float rockSpeed;
	float fallSide;
	float fallHeight;
	float speed = 0;
	int flag = 0;
	int stoneNumber = 100;
	int bonusNumber = 100;
	int gameState = 0;
	int bonusScore = 0;
	int lifes = 3;

	boolean[] isCollidedWithStone = new boolean[stoneNumber];
	boolean[] isCollidedWithBonus = new boolean[bonusNumber];

	@Override
	public void create() {
		//shapeRenderer = new ShapeRenderer();
		inputAdapter = new MyInputAdapter(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.input.setInputProcessor(inputAdapter);
		batch = new SpriteBatch();

		background = new Texture("background.jpeg");
		submarine = new Texture[2];
		submarine_life1 = new Texture("submarine_life1.png");
		submarine_life2 = new Texture("submarine_life2.png");
		submarine_life3 = new Texture("submarine_life3.png");
		submarine[0] = new Texture("submarine_left.png");
		submarine[1] = new Texture("submarine_right.png");
		rockTexture = new Texture("stone.png");
		bonusTexture = new Texture("bonus.png");
		gameOverT = new Texture("gameover.png");
		splashT = new Texture("underwaterworld.png");
		victoryT = new Texture("victory.png");

		bonusFont = new BitmapFont();
		bonusFont.setColor(Color.WHITE);
		bonusFont.getData().setScale(8);

		scoreFont = new BitmapFont();
		scoreFont.setColor(Color.WHITE);
		scoreFont.getData().setScale(8);

		scoreText = new BitmapFont();
		scoreText.setColor(Color.WHITE);
		scoreText.getData().setScale(8);
		layout = new GlyphLayout(scoreText, "Score:  ");

		bonusText = new BitmapFont();
		bonusText.setColor(Color.WHITE);
		bonusText.getData().setScale(8);
		bonusLayout = new GlyphLayout(bonusText, "Bonus:  ");

		submarineCircle = new Circle();
		stoneCircle = new Circle[stoneNumber];
		bonusCircle = new Circle[bonusNumber];

		init();
	}

	public void init() {
		fallSide = Gdx.graphics.getWidth() / 2 - submarine[0].getWidth() / 2;
		fallHeight = Gdx.graphics.getHeight() * 2 / 3 - submarine[flag].getHeight() / 2;

		bonuses = new Sprite[bonusNumber];
		rocks = new Sprite[stoneNumber];
		random = new Random();

		int rockGap = submarine[0].getWidth() * 2;
		int rockMaxY = Gdx.graphics.getHeight() + rockGap;
		for (int i = 0; i < rocks.length; i++) {
			rocks[i] = new Sprite(rockTexture);
			rocks[i].setPosition(random.nextInt(
							Gdx.graphics.getWidth() - (int) rocks[i].getWidth()),
					rockMaxY - i * rockGap - 10 * submarine[0].getWidth());
			rocks[i].setBounds(
					rocks[i].getX(),
					rocks[i].getY(),
					rockTexture.getWidth(), rockTexture.getHeight());
			stoneCircle[i] = new Circle();
		}
		rockSpeed = 200;

		int bonusGap = submarine[0].getWidth() * 2;
		int bonusMaxY = Gdx.graphics.getHeight() + bonusGap;
		for (int i = 0; i < bonuses.length; i++) {
			bonuses[i] = new Sprite(bonusTexture);
			bonuses[i].setPosition(random.nextInt(
							Gdx.graphics.getWidth() - (int) bonuses[i].getWidth() + 20),
					bonusMaxY - i * bonusGap - 10 * rocks[0].getWidth());
			bonuses[i].setBounds(
					bonuses[i].getX(),
					bonuses[i].getY(),
					bonusTexture.getWidth(), bonusTexture.getHeight());
			bonusCircle[i] = new Circle();
		}
		rockSpeed = 200;
	}

	@Override
	public void render() {
		batch.begin();

		batch.draw(splashT, 0, 0,
				Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight());

		if (gameState == 1) {
			batch.draw(background, 0, 0,
					Gdx.graphics.getWidth(),
					Gdx.graphics.getHeight());
			controlAdd();
			stoneRender();
			bonusRender();
			updateLife();

			if (fallSide < 0) {
				fallSide = 0;
			} else if (fallSide + submarine[0].getWidth() > Gdx.graphics.getWidth()) {
				fallSide = Gdx.graphics.getWidth() - submarine[0].getWidth();
			}
			if (fallHeight < 0) {
				fallHeight = 0;
			} else if (fallHeight + submarine[0].getHeight() > Gdx.graphics.getHeight()) {
				fallHeight = Gdx.graphics.getHeight() - submarine[0].getHeight();
			}
			batch.draw(submarine[flag],
						fallSide,
						fallHeight);
			//UI
			scoreText.draw(batch,
					"Score:",
					25,
					Gdx.graphics.getHeight() - 25);

			scoreFont.draw(batch,
					String.valueOf((int) Math.round(gameScore)),
					layout.width + 10,
					Gdx.graphics.getHeight() - 35);

			bonusText.draw(batch,
					"Bonus:",
					25,
					Gdx.graphics.getHeight() - bonusLayout.height - 80);

			bonusFont.draw(batch,
					String.valueOf((int) Math.round(bonusScore)),
					layout.width + 20,
					Gdx.graphics.getHeight() - bonusLayout.height - 85);
			//UI
			gameScore += Gdx.graphics.getDeltaTime();
			if(gameScore >= 100f) {
				gameState = 3;
			}

		} else if (gameState == 0) {
			if (Gdx.input.justTouched()) {
				batch.draw(splashT, 0, 0,
						Gdx.graphics.getWidth(),
						Gdx.graphics.getHeight());
				gameState = 1;
			}
		} else if (gameState == 2) {
			batch.draw(gameOverT, 0, 0,
					Gdx.graphics.getWidth(),
					Gdx.graphics.getHeight());

			if (Gdx.input.justTouched()) {
				for (int i = 0; i < rocks.length; i++) {
					isCollidedWithStone[i] = false;
				}
				init();
				gameState = 1;
				lifes = 3;
				gameScore = 0;
				bonusScore = 0;
			}
		} else if (gameState == 3) {
			batch.draw(victoryT, 0, 0,
					Gdx.graphics.getWidth(),
					Gdx.graphics.getHeight());

			if (Gdx.input.justTouched()) {
				for (int i = 0; i < rocks.length; i++) {
					isCollidedWithStone[i] = false;
				}
				init();
				gameState = 1;
				lifes = 3;
				gameScore = 0;
				bonusScore = 0;
			}
		}
		batch.end();
		buildShapes();
	}

	@Override
	public void dispose () {
	}

	public void buildShapes() {
		for (int i = 0; i < rocks.length; i++) {
			stoneCircle[i] = new Circle(rocks[i].getX() + rocks[i].getWidth() / 2,
					rocks[i].getY() + rocks[i].getHeight() / 2,
					rocks[i].getWidth() / 3.5f);

			submarineCircle.set(fallSide + submarine[flag].getWidth()/2,
					Gdx.graphics.getHeight() * 2 / 3,
					submarine[flag].getWidth()/2.5f);
//			shapeRenderer.setColor(Color.WHITE);
//			shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
//			shapeRenderer.circle(stoneCircle[i].x+5,
//					stoneCircle[i].y+7, (float) (stoneCircle[i].radius));
//			shapeRenderer.end();

//			shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
//			shapeRenderer.setColor(Color.WHITE);
//			shapeRenderer.circle(submarineCircle.x, submarineCircle.y, submarineCircle.radius);
//			shapeRenderer.end();
			for (int x = 0; x < rocks.length; x++) {
				if (!isCollidedWithStone[x] && Intersector.overlaps(submarineCircle, stoneCircle[x])) {
					lifes--;
					Gdx.app.log("BOOM", String.valueOf(lifes));
					isCollidedWithStone[x] = true;
				} else if (isCollidedWithStone[x] && Intersector.overlaps(submarineCircle, stoneCircle[x])) {
					rocks[x].setAlpha(0f);
				}
			}
		}
		for (int i = 0; i < bonuses.length; i++) {
			bonusCircle[i] = new Circle(bonuses[i].getX() + bonuses[i].getWidth() / 2,
					bonuses[i].getY() + bonuses[i].getHeight() / 2,
					bonuses[i].getWidth() / 2);
//			shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
//			shapeRenderer.circle(bonusCircle[i].x+5,
//					bonusCircle[i].y, (float) (bonusCircle[i].radius));
//			shapeRenderer.end();

			for (int x = 0; x < bonusCircle.length; x++) {
				if (!isCollidedWithBonus[x] && Intersector.overlaps(submarineCircle, bonusCircle[x])) {
					bonusScore++;
					Gdx.app.log("Bonus", String.valueOf(bonusScore));
					isCollidedWithBonus[x] = true;
				} else if (isCollidedWithBonus[x] && Intersector.overlaps(submarineCircle, bonusCircle[x])) {
					bonuses[x].setAlpha(0f);
				}
			}
		}


	}

	public void controlAdd() {
		flag = inputAdapter.getFlag();
		if(inputAdapter.isTouchedLeft()){
			speed = -15;
		}
		if(inputAdapter.isTouchedRight()){
			speed = +15;
		}
		fallSide += speed;
		speed *= 0.9f;
	}

	public void stoneRender() {
		for (Sprite rock : rocks) {
			rock.translateY(rockSpeed * Gdx.graphics.getDeltaTime());
			rock.draw(batch);
			if (rock.getY() > Gdx.graphics.getHeight()) {
				rock.setPosition(random.nextInt(Gdx.graphics.getWidth() - (int) rock.getWidth()),
						rock.getY() - rocks.length * submarine[0].getWidth() * 2);
			}
		}
	}

	public void bonusRender() {
		for (Sprite bonus : bonuses) {
			bonus.translateY(rockSpeed * Gdx.graphics.getDeltaTime());
			bonus.draw(batch);
			if (bonus.getY() > Gdx.graphics.getHeight()) {
				bonus.setPosition(random.nextInt(Gdx.graphics.getWidth() - (int) bonus.getWidth()),
						bonus.getY() - bonuses.length * rocks[0].getWidth() * 10);
			}
		}
	}

	public void updateLife(){
		float padding = 10f;
		float totalWidth = submarine_life3.getWidth() * 3 / 1.5f + padding * 2;
		float xCoord = Gdx.graphics.getWidth() - totalWidth - 25;

		if (bonusScore % 3 == 0 && bonusScore > 0 && lifes <= 3) {
			lifes++;
			int bonusUsed = bonusScore / 3;
			bonusScore -= bonusUsed * 3;
		}

		if (lifes >= 3) {
			batch.draw(submarine_life1, xCoord + 2 * (submarine_life3.getWidth() / 1.5f + padding),
					Gdx.graphics.getHeight() - 25 - submarine_life3.getHeight() / 1.5f,
					submarine_life1.getWidth() / 1.5f,
					submarine_life1.getHeight() / 1.5f);

			batch.draw(submarine_life2, xCoord + submarine_life3.getWidth() / 1.5f + padding,
					Gdx.graphics.getHeight() - 25 - submarine_life3.getHeight() / 1.5f,
					submarine_life2.getWidth() / 1.5f,
					submarine_life2.getHeight() / 1.5f);

			batch.draw(submarine_life3, xCoord,
					Gdx.graphics.getHeight() - 25 - submarine_life3.getHeight() / 1.5f,
					submarine_life3.getWidth() / 1.5f,
					submarine_life3.getHeight() / 1.5f);

		} else if(lifes == 2) {
			batch.draw(submarine_life1, xCoord + submarine_life2.getWidth() / 1.5f + padding,
					Gdx.graphics.getHeight() - 25 - submarine_life3.getHeight() / 1.5f,
					submarine_life1.getWidth() / 1.5f,
					submarine_life1.getHeight() / 1.5f);

			batch.draw(submarine_life2, xCoord,
					Gdx.graphics.getHeight() - 25 - submarine_life3.getHeight() / 1.5f,
					submarine_life2.getWidth() / 1.5f,
					submarine_life2.getHeight() / 1.5f);

		} else if(lifes == 1) {
			batch.draw(submarine_life1, xCoord,
					Gdx.graphics.getHeight() - 25 - submarine_life3.getHeight() / 1.5f,
					submarine_life1.getWidth() / 1.5f,
					submarine_life1.getHeight() / 1.5f);
		} else {
			gameState = 2;
		}
	}
}
