package com.spaceinvaders;

public class DefenseBrick extends Sprite {
    // NOTE: Defense block will be able to be hit 4 times before being destroyed
    private short health = 4;

    // TODO this is just a placeholder constructor
    public DefenseBrick(SpriteImage image) {
        super(image);
    }

    @Override
    public void update(long fps) {
        // TODO implement update
    }
}
