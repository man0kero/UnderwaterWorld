package com.manokero.underwaterworld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import java.util.Random;

public class Stones{
    private Random random;
    private Sprite[] stonesSpriteArray;
    private Circle[] stoneCircleArray;

    private int screenWidth;
    private int screenHeight;
    private float rockGap;
    private float stonesSpeed;
    private boolean[] isCollidedWithStone;

    public void setIsCollidedWithStone(boolean[] isCollidedWithStone) {
        this.isCollidedWithStone = isCollidedWithStone;
    }
    public Circle[] getStoneCircleArray() {
        return stoneCircleArray;
    }

    public void create() {
        UnderwaterWorld underwaterWorld = new UnderwaterWorld();
        random = new Random();
        screenWidth = Gdx.graphics.getWidth();
        screenHeight = Gdx.graphics.getHeight();
        stonesSpeed = 200;
        rockGap = screenWidth/3.5f;
        int stoneNumber = underwaterWorld.getNumberOfStonesAndBonuses();

        Texture stoneTexture = new Texture("stone.png");
        stoneCircleArray = new Circle[stoneNumber];
        stonesSpriteArray = new Sprite[stoneNumber];
        isCollidedWithStone = new boolean[stoneNumber];

        float rockMaxY = screenHeight + rockGap;
        for (int i = 0; i < stonesSpriteArray.length; i++) {
            stonesSpriteArray[i] = new Sprite(stoneTexture);
            stonesSpriteArray[i].setPosition(random.nextInt(
                            screenWidth - (int) stonesSpriteArray[i].getWidth()),
                    rockMaxY - i * rockGap - 5 * rockGap);
            stonesSpriteArray[i].setBounds(
                    stonesSpriteArray[i].getX(),
                    stonesSpriteArray[i].getY(),
                    stoneTexture.getWidth(),
                    stoneTexture.getHeight());
            stoneCircleArray[i] = new Circle();
        }
    }

    public void render(SpriteBatch spriteBatchStones) {
        for (Sprite rock : stonesSpriteArray) {
            rock.translateY(stonesSpeed * Gdx.graphics.getDeltaTime());
            rock.draw(spriteBatchStones);
            if (rock.getY() > screenHeight) {
                rock.setPosition(random.nextInt(screenWidth - (int) rock.getWidth()),
                        rock.getY() - stonesSpriteArray.length * rockGap);
            }
            for (int i = 0; i < stonesSpriteArray.length; i++) {
                stoneCircleArray[i] = new Circle(
                        stonesSpriteArray[i].getX() + stonesSpriteArray[i].getWidth() / 2,
                        stonesSpriteArray[i].getY() + stonesSpriteArray[i].getHeight() / 2,
                        stonesSpriteArray[i].getWidth() / 3.5f);
                if (isCollidedWithStone[i]) {
                    stonesSpriteArray[i].setAlpha(0f);
                }
            }
        }
    }
}