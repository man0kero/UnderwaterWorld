package com.manokero.underwaterworld;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;

public class MyInputAdapter extends InputAdapter {

    private int screenWidth;
    private int screenHeight;
    private boolean touchedLeft = false;
    private boolean touchedRight = false;
    int flag;
    public int getFlag() {
        return flag;
    }

    public MyInputAdapter(int screenWidth, int screenHeight) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (screenX < screenWidth / 2) {
            // Нажатие в левой части экрана
            flag = 0;
            touchedLeft = true;
            touchedRight = false;
        } else {
            // Нажатие в правой части экрана
            flag = 1;
            touchedLeft = false;
            touchedRight = true;
        }
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        touchedLeft = false;
        touchedRight = false;
        return true;
    }

    public boolean isTouchedLeft() {
        return touchedLeft;
    }

    public boolean isTouchedRight() {
        return touchedRight;
    }
}

