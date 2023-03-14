package com.manokero.underwaterworld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;

public class Life {
    private Texture submarine_life1;
    private Texture submarine_life2;
    private Texture submarine_life3;
    private Circle submarineCircle;
    private Circle[] stoneCircleArray;
    private Circle[] bonusCircleArray;

    private int currentLife;
    private int bonusScoreLife;
    private int gameState;
    private boolean[] isCollidedWithStone;
    private boolean[] isCollidedWithBonus;

    public void setSubmarineCircle(Circle submarineCircle) {
        this.submarineCircle = submarineCircle;
    }
    public void setStoneCircleArray(Circle[] stoneCircleArray) {
        this.stoneCircleArray = stoneCircleArray;
    }
    public void setBonusCircleArray(Circle[] bonusCircleArray) {
        this.bonusCircleArray = bonusCircleArray;
    }
    public boolean[] getIsCollidedWithStone() {
        return isCollidedWithStone;
    }
    public boolean[] getIsCollidedWithBonus() {
        return isCollidedWithBonus;
    }
    public int getBonusScoreLife() {
        return bonusScoreLife;
    }
    public int getGameState() {
        return gameState;
    }

    public void create() {
        UnderwaterWorld underwaterWorld = new UnderwaterWorld();
        int number = underwaterWorld.getNumberOfStonesAndBonuses();
        gameState = 1;
        currentLife = 3;
        bonusScoreLife = 0;
        submarineCircle = new Circle();
        isCollidedWithStone = new boolean[number];
        isCollidedWithBonus = new boolean[number];
        stoneCircleArray = new Circle[number];
        bonusCircleArray = new Circle[number];

        submarine_life1 = new Texture("submarine_life1.png");
        submarine_life2 = new Texture("submarine_life2.png");
        submarine_life3 = new Texture("submarine_life3.png");
    }

    public void render(SpriteBatch spriteBatchLife) {
        //Submarine with Stones
        for (int i = 0; i < isCollidedWithStone.length; i++) {
            if (!isCollidedWithStone[i] &&
                    Intersector.overlaps(submarineCircle, stoneCircleArray[i])) {
                currentLife--;
                isCollidedWithStone[i] = true;
            }
        }

        //Submarine with Bonuses
        for (int i = 0; i < isCollidedWithBonus.length; i++) {
            if (!isCollidedWithBonus[i] &&
                    Intersector.overlaps(submarineCircle, bonusCircleArray[i]) && currentLife < 3) {
                bonusScoreLife++;
                isCollidedWithBonus[i] = true;
                if (bonusScoreLife % 3 == 0 && bonusScoreLife > 0) {
                    currentLife++;
                    bonusScoreLife = bonusScoreLife - 3;
                }
            }
        }
        drawLife(spriteBatchLife);
    }

    private void drawLife(SpriteBatch spriteBatchLife) {
        float padding = 10f;
        float totalWidth = submarine_life3.getWidth() * 3 / 1.5f + padding * 2;
        float xCoord = Gdx.graphics.getWidth() - totalWidth - 25;

        if (currentLife >= 3) {
            spriteBatchLife.draw(submarine_life1, xCoord + 2 * (submarine_life3.getWidth() / 1.5f + padding),
                    Gdx.graphics.getHeight() - 25 - submarine_life3.getHeight() / 1.5f,
                    submarine_life1.getWidth() / 1.5f,
                    submarine_life1.getHeight() / 1.5f);

            spriteBatchLife.draw(submarine_life2, xCoord + submarine_life3.getWidth() / 1.5f + padding,
                    Gdx.graphics.getHeight() - 25 - submarine_life3.getHeight() / 1.5f,
                    submarine_life2.getWidth() / 1.5f,
                    submarine_life2.getHeight() / 1.5f);

            spriteBatchLife.draw(submarine_life3, xCoord,
                    Gdx.graphics.getHeight() - 25 - submarine_life3.getHeight() / 1.5f,
                    submarine_life3.getWidth() / 1.5f,
                    submarine_life3.getHeight() / 1.5f);

        } else if(currentLife == 2) {
            spriteBatchLife.draw(submarine_life1, xCoord + submarine_life2.getWidth() / 1.5f + padding,
                    Gdx.graphics.getHeight() - 25 - submarine_life3.getHeight() / 1.5f,
                    submarine_life1.getWidth() / 1.5f,
                    submarine_life1.getHeight() / 1.5f);

            spriteBatchLife.draw(submarine_life2, xCoord,
                    Gdx.graphics.getHeight() - 25 - submarine_life3.getHeight() / 1.5f,
                    submarine_life2.getWidth() / 1.5f,
                    submarine_life2.getHeight() / 1.5f);

        } else if(currentLife == 1) {
            spriteBatchLife.draw(submarine_life1, xCoord,
                    Gdx.graphics.getHeight() - 25 - submarine_life3.getHeight() / 1.5f,
                    submarine_life1.getWidth() / 1.5f,
                    submarine_life1.getHeight() / 1.5f);
        } else {
            gameState = 2;
        }
    }
}