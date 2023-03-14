package com.manokero.underwaterworld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;

public class Submarine {
    private MyInputAdapter inputAdapter;
    private Texture[] submarine;
    private Circle submarineCircle;

    private float speed;
    private int flag;
    private int screenWidth;
    private int screenHeight;
    private float submarineWidth;
    private float submarineHeight;
    private int submarineX;
    private int submarineY;

    public Circle getSubmarineCircle() {
        return submarineCircle;
    }

    public void create() {
        screenWidth = Gdx.graphics.getWidth();
        screenHeight = Gdx.graphics.getHeight();

        inputAdapter = new MyInputAdapter(screenWidth, screenHeight);
        Gdx.input.setInputProcessor(inputAdapter);

        submarine = new Texture[2];
        submarine[0] = new Texture("submarine_left.png");
        submarine[1] = new Texture("submarine_right.png");
        submarineCircle = new Circle();

        submarineWidth = submarine[0].getWidth();
        submarineHeight = submarine[0].getHeight();
        submarineX = (int)( screenWidth / 2 - submarineWidth / 2);
        submarineY = (int)(screenHeight * 2 / 3 - submarineHeight / 2);
        speed = 0;
    }

    public void render(SpriteBatch spriteBatchSubmarine){
        addControl();
        spriteBatchSubmarine.draw(submarine[flag], submarineX, submarineY);
        submarineCircle.set(submarineX + submarineWidth/2,
                screenHeight * 2 / 3,
                submarineWidth/2.5f);
    }

    private void addControl() {
        //Moving and inertia
        if(inputAdapter.isTouchedLeft()){
            flag = 0;
            speed = -15;
        } else if(inputAdapter.isTouchedRight()){
            flag = 1;
            speed = +15;
        }
        submarineX += speed;
        speed *= 0.9f;

        //Bounds
        if (submarineX < 0) {
            submarineX = 0;
        } else if (submarineX + submarineWidth > screenWidth) {
            submarineX = (int)(screenWidth - submarineWidth);
        }
    }
}