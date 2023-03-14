package com.manokero.underwaterworld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class BitmapUI {
    private BitmapFont scoreFont;
    private BitmapFont scoreText;
    private BitmapFont bonusFont;
    private BitmapFont bonusText;
    private GlyphLayout layout;
    private GlyphLayout bonusLayout;
    private int screenHeight;
    private float gameScore;
    private int bonusScore;

    public void setGameScore(float gameScore) {
        this.gameScore = gameScore;
    }
    public void setBonusScore(int bonusScore) {
        this.bonusScore = bonusScore;
    }

    public void create() {
        screenHeight = Gdx.graphics.getHeight();

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
    }

    public void drawUI(SpriteBatch batch) {
        scoreText.draw(batch,"Score:",25,screenHeight - 25);
        scoreFont.draw(batch,
                String.valueOf(Math.round(gameScore)),
                layout.width + 10,
                screenHeight - 35);
        bonusText.draw(batch,"Bonus:",25,screenHeight - bonusLayout.height - 80);
        bonusFont.draw(batch,
                String.valueOf(Math.round(bonusScore)),
                layout.width + 20,
                screenHeight - bonusLayout.height - 85);
    }
}