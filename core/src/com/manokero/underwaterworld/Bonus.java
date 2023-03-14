package com.manokero.underwaterworld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;

import java.util.Random;

public class Bonus {
    private  Random random;
    private  Sprite[] bonusesSpriteArray;
    private  Circle[] bonusCircleArray;

    private  int screenWidth;
    private  int screenHeight;
    private  float bonusGap;
    private  float bonusSpeed;
    private  boolean[] isCollidedWithBonus;

    public Circle[] getBonusCircleArray() {
        return bonusCircleArray;
    }
    public void setIsCollidedWithBonus(boolean[] isCollidedWithBonus) {
        this.isCollidedWithBonus = isCollidedWithBonus;
    }

    public void create() {
        UnderwaterWorld underwaterWorld = new UnderwaterWorld();
        random = new Random();
        screenWidth = Gdx.graphics.getWidth();
        screenHeight = Gdx.graphics.getHeight();
        bonusSpeed = 200;
        bonusGap = screenWidth/3.5f;
        int bonusNumber = underwaterWorld.getNumberOfStonesAndBonuses();

        Texture bonusTexture = new Texture("bonus.png");
        bonusCircleArray = new Circle[bonusNumber];
        bonusesSpriteArray = new Sprite[bonusNumber];
        isCollidedWithBonus = new boolean[bonusNumber];

        float bonusMaxY = screenHeight + bonusGap;
        for (int i = 0; i < bonusesSpriteArray.length; i++) {
            bonusesSpriteArray[i] = new Sprite(bonusTexture);
            bonusesSpriteArray[i].setPosition(random.nextInt(
                            screenWidth - (int) bonusesSpriteArray[i].getWidth() + 20),
                    bonusMaxY - i * bonusGap - 10 * bonusesSpriteArray[0].getWidth());
            bonusesSpriteArray[i].setBounds(
                    bonusesSpriteArray[i].getX(),
                    bonusesSpriteArray[i].getY(),
                    bonusTexture.getWidth(), bonusTexture.getHeight());
            bonusCircleArray[i] = new Circle();
        }
    }

    public void render(SpriteBatch bonusBatch) {
        for (Sprite bonus : bonusesSpriteArray) {
            bonus.translateY(bonusSpeed * Gdx.graphics.getDeltaTime());
            bonus.draw(bonusBatch);
            if (bonus.getY() > screenHeight) {
                bonus.setPosition(random.nextInt(screenWidth - (int) bonus.getWidth()),
                        bonus.getY() - bonusesSpriteArray.length * bonusGap);
            }
            for (int i = 0; i < bonusesSpriteArray.length; i++) {
                bonusCircleArray[i] = new Circle(
                        bonusesSpriteArray[i].getX() + bonusesSpriteArray[i].getWidth() / 2,
                        bonusesSpriteArray[i].getY() + bonusesSpriteArray[i].getHeight() / 2,
                        bonusesSpriteArray[i].getWidth() / 2);
                if (isCollidedWithBonus[i]) {
                    bonusesSpriteArray[i].setAlpha(0f);
                }
            }
        }
    }
}