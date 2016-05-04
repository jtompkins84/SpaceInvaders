package com.spaceinvaders.game_entities;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import com.spaceinvaders.gui.Movement;
import com.spaceinvaders.Resources;

import java.util.Random;
import java.util.Timer;

public class InvaderUFO extends Sprite {

    /**
     * Represents the states the UFO AI can be in when firing its weapon.
     */
    private enum AttackState {NOT_FIRING, PRE_FIRING, BEGIN_FIRING, CHARGE_FIRE, FIRE, FINISH_FIRE}
    /**
     * Represents the states of UFO AI movement
     */
    private enum AIState {DO_MOVE_LEFT, DO_MOVE_RIGHT, DELAY, DO_ATTACK, ATTACKING,
        POST_ATTACK_MOVE}

    private Timer delayTimer;

    private AttackState attackState = AttackState.NOT_FIRING;
    private AIState state = AIState.DO_ATTACK;

    private Player player;
    private ProjectileArray projectiles;

    private boolean isHit = false;
    private boolean isDead = false;
    private boolean isScoreTallied = false;

    private short health = 3;
    private short countdownToFire = 2;
    private short delayCounter = 0;

    private int maxSpeed = 350;

    private float width;
    private float height;
    private float playFieldWidth;

    private long lastFireTime = 0l;

    private InvaderLaser laser;
    private boolean resetMoveCount = false;

    public InvaderUFO (float yPos, Player player, ProjectileArray projectiles,float playFieldWidth) {
        super(new Bitmap[] {
                Resources.img_invader_UFO_hit01,
                Resources.img_invader_UFO01,
                Resources.img_invader_UFO_firing01,
                Resources.img_invader_UFO_firing02,
                Resources.img_invader_UFO_firing03,
                Resources.img_invader_UFO_firing02,
                Resources.img_invader_UFO_firing01,
                Resources.img_invader_UFO01}
                , new RectF[] {
                new RectF(7 * Resources.DPIRatio, 14 * Resources.DPIRatio, 126 * Resources.DPIRatio, 63 * Resources.DPIRatio),
                new RectF(7 * Resources.DPIRatio, 14 * Resources.DPIRatio, 126 * Resources.DPIRatio, 63 * Resources.DPIRatio),
                new RectF(7 * Resources.DPIRatio, 14 * Resources.DPIRatio, 126 * Resources.DPIRatio, 63 * Resources.DPIRatio),
                new RectF(7 * Resources.DPIRatio, 14 * Resources.DPIRatio, 126 * Resources.DPIRatio, 63 * Resources.DPIRatio),
                new RectF(7 * Resources.DPIRatio, 14 * Resources.DPIRatio, 126 * Resources.DPIRatio, 63 * Resources.DPIRatio),
                new RectF(7 * Resources.DPIRatio, 14 * Resources.DPIRatio, 126 * Resources.DPIRatio, 63 * Resources.DPIRatio),
                new RectF(7 * Resources.DPIRatio, 14 * Resources.DPIRatio, 126 * Resources.DPIRatio, 63 * Resources.DPIRatio),
                new RectF(7 * Resources.DPIRatio, 14 * Resources.DPIRatio, 126 * Resources.DPIRatio, 63 * Resources.DPIRatio)});

        this.player = player;
        this.projectiles = projectiles;

        if(Resources.difficulty == Resources.Difficulty.HARD) {
            maxSpeed = 450;
        }

        speed = maxSpeed;

        movement = Movement.LEFT;

        this.playFieldWidth = playFieldWidth;
        width = getCurrentFrameSpriteBitmap().getWidth();
        height = getCurrentFrameSpriteBitmap().getHeight();

        setPosition(playFieldWidth + width, yPos);

        doAnimationLoop = false;
        setStartAndEndFrames(1, 1);
    }

    @Override
    public void update(long fps) {
        if(isDead) return;

        doStateTransitions();

        float dx;
        switch (movement) {
            case LEFT:
                dx = -(speed) / fps;
                move(dx, 0.0f);

                if(laser != null && !laser.isDestroyed())
                    laser.move(dx, 0.0f);
                break;
            case RIGHT:
                dx = speed / fps;
                move(dx, 0.0f);

                if(laser != null && !laser.isDestroyed())
                    laser.move(dx, 0.0f);
                break;
        }
    }

    @Override
    public void draw(Canvas canvas, Paint paint, boolean drawHitBox) {

        super.draw(canvas, paint, drawHitBox);
    }

    public void updateTimeOnResume() {
        lastFireTime += System.currentTimeMillis() - lastFireTime;
    }

    /**
     * handles
     */
    private void doStateTransitions() {

        switch (state) {
// Handle ship movement to the left.
            case DO_MOVE_LEFT:
                if(pos.x <= -(1.5f * width)) {
                    movement = Movement.STOPPED;
                    state = AIState.DELAY;
                    delayCounter = 0;
                    countdownToFire--;
                }
                return;
// Handle ship movement to the right.
            case DO_MOVE_RIGHT:
                if(pos.x >= playFieldWidth + (1.5f * width)){
                    movement = Movement.STOPPED;
                    state = AIState.DELAY;
                    delayCounter = 0;
                    countdownToFire--;
                }
                return;
// Handle the delay at the edge of the screen
            case DELAY:
                Random rand = new Random();
                int result = rand.nextInt(40) + 1;
                if(result % 10 == 0 && delayCounter >= 300) {
                    if (countdownToFire <= 0) {
                        state = AIState.DO_ATTACK;
                        return;
                    }
                    else if(getX() >= playFieldWidth) {
                        state = AIState.DO_MOVE_LEFT;
                        movement = Movement.LEFT;
                        return;
                    }
                    else if(getX() <= 0) {
                        state = AIState.DO_MOVE_RIGHT;
                        movement = Movement.RIGHT;
                        return;
                    }
                }
                delayCounter++;
                return;
            case DO_ATTACK:
                if(Math.abs(getX() - player.getX()) < width / 3) {
                    state = AIState.ATTACKING;
                    doAttack();
                }
                else if(getX() > player.getX()) movement = Movement.LEFT;
                else if(getX() < player.getX()) movement = Movement.RIGHT;
                return;
            case ATTACKING:
                if(attackState == AttackState.NOT_FIRING) {
                    state = AIState.POST_ATTACK_MOVE;
                    return;
                }
                doAttack();
                return;
            case POST_ATTACK_MOVE:
                if(getX() >= playFieldWidth + width
                        || getX() <= -width)
                {
                    movement = Movement.STOPPED;
                    state = AIState.DELAY;
                    delayCounter = 0;
                    countdownToFire = 2;
                }
                return;
        }
    }

    /**
     * Provides the functionality and performs all the necessary stage transitions of the
     * attack phase of the AI.
     */
    private void doAttack() {

        switch (attackState) {
// Initial conditions of the attack phase.
            case NOT_FIRING:
                attackState = AttackState.BEGIN_FIRING;
                movement = Movement.STOPPED;
                setStartAndEndFrames(1, 4);
                doAnimationLoop = false;
                setSkipFrames((short) 8);
                speed = maxSpeed - (maxSpeed / 4);
                return;
// Display animation of UFO attack engaging
            case BEGIN_FIRING:
                if(getCurrFrameIndex() == 4) {
                    attackState = AttackState.CHARGE_FIRE;
                    laser = projectiles.addInvaderLaser(getX(), getY() + (height * 0.33f));
                    return;
                }
                return;
// Display laser charging animation
            case CHARGE_FIRE:
                if(laser.isCharged()) attackState = AttackState.FIRE;
                return;
// The laser is firing and the UFO can move to try and position itself on top of the player.
            case FIRE:
                if(laser.isDestroyed() || player.isDead()) {
                    attackState = AttackState.FINISH_FIRE;
                    movement = Movement.STOPPED;
                    setStartAndEndFrames(4, 7);
                    speed = maxSpeed;
                    return;
                }

                if(player.getX() > laser.getX()) movement = Movement.RIGHT;
                else if(player.getX() < laser.getX()) movement = Movement.LEFT;
                else movement = Movement.STOPPED;

                return;
// Laser has depleted and UFO weapon has "retracted"
            case FINISH_FIRE:
                if(getCurrFrameIndex() == 7) {
                    setStartAndEndFrames(1, 1);
                    attackState = AttackState.NOT_FIRING;
                    // If the UFO is closer to the right side of the screen.
                    if(playFieldWidth - pos.x < pos.x) movement = Movement.RIGHT;
                    else movement = Movement.LEFT;
                    return;
                }
                return;
        }
    }
}
