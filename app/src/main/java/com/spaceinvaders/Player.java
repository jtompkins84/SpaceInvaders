package com.spaceinvaders;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import com.spaceinvaders.game_entities.PlayerLaserShot;
import com.spaceinvaders.game_entities.PlayerShield;
import com.spaceinvaders.game_entities.PowerUp;

public class Player extends Sprite {
    PlayerShield playerShield;

    /**
     * The count of how many projectiles belonging to the player are currently on
     * the playfield. Used to limit the firing rate of the player.
     */
    private short projCount = 0;
    /**
     * The maximum amount of projectiles the player is allowed to have exist on the
     * playfield at a given time. Can be increased by a power-up.
     */
    private short maxProjCount = 1;

    // x-position of player movement boundaries relative to the screen's resolution
    private float lBoundary, rBoundary;

    private boolean isDead = false;
    private long deathTime = 0;
    private boolean doUpdate = true;
    private boolean doMove = true;

    /**
     * a time-since-epoch value representing the time the player last fired
     */
    private long lastFireTime = 0l;
    /**
     * the time in milliseconds that the player can't fire in between shots.
     */
    private long rapidFireDelay = 500l;
    /**
     * Keeps track of elapsed while certain power-ups are active.
     */
    private long rapidFireTimer = 0l;
    private long specialShotTimer = 0l;
    /**
     * the amount of time in milliseconds that the player can't move.
     */
    private long specialShotDelay = 1700l;

    private boolean hasShield = false;

    private boolean hasSpecial = false;
    private boolean isSpecialFired = false;

    private boolean hasRapid = false;
    private short rapidCounter = 0;
    private short rapidMaxSeconds = 8;

    /**
     * <code>Player</code> constructor.
     * @param screenWidth <code>float</code> - play field's current width
     * @param screenHeight <code>float</code> - play field's current height
     */
    public Player(float screenWidth, float screenHeight) {
        // sets the bitmap image and initializes the size of the hit-box.
        // hit-box values are derived by opening the original image in and image editor
        // and determining the dimension of the hit-box in pixels from there.
        super(new Bitmap[]{Resources.img_player, Resources.img_player_death01,
                        Resources.img_player_death02, Resources.img_player_death03,
                        Resources.img_player_death04},
                new RectF(9.0f * Resources.DPIRatio, 21.0f * Resources.DPIRatio,
                        101.0f * Resources.DPIRatio, 56.0f * Resources.DPIRatio));

        // set position relative to view of the play field
        setPosition(screenWidth / 2.0f, screenHeight - (screenHeight / 12.0f));

        playerShield = new PlayerShield(getX(), getY());

        // initialize player movement speed
        speed = 350;

        this.setSkipFrames((short)10);

        // set the boundaries for the player relative to screen resolution
        lBoundary = screenWidth / 24;
        rBoundary = screenWidth - (screenWidth / 24);
    }

    @Override
    public void update(long fps) {
        RectF hitBox = getHitBox();

        if(isDead) {
            long currTime = System.currentTimeMillis();
            if(deathTime == 0) deathTime = currTime; // set initial time of death.


            // check to see if death period is done.
            if(currTime - deathTime >= 2000) {
                isDead = false;
                this.setStartAndEndFrames(0, 0); // reset frame to living player
                deathTime = 0;
            }
        }
        else if(hasRapid || isSpecialFired) {
            long currTime = System.currentTimeMillis();

            if(hasRapid && currTime - rapidFireTimer >= 1000) {
                rapidCounter--;
                rapidFireTimer = currTime;
            }

            if(rapidCounter <= 0) {
                hasRapid = false;
            }

            if(isSpecialFired && currTime - specialShotTimer >= specialShotDelay) {
                doMove = true;
                isSpecialFired = false;
            }
        }

        if(doMove && doUpdate && hitBox != null) {
            if(movement == Movement.RIGHT && hitBox.right < rBoundary) {
                move(speed / fps, 0.0f);
                playerShield.move(speed / fps, 0.0f);
            }
            else if(movement == Movement.LEFT && hitBox.left > lBoundary) {
                move(-(speed / fps), 0.0f);
                playerShield.move(-(speed / fps), 0.0f);
            }
        }
    }

    @Override
    public void draw(Canvas canvas, Paint paint, boolean showHitBox) {
        if(hasShield) {
            playerShield.draw(canvas, paint);
        }

        super.draw(canvas, paint, showHitBox);
    }

    /**
     * @return When the projectile count of the player is less than the max projectile count,
     * <code>projCount</code> will increment by 1 and <code>true</code> is returned.
     * Otherwise, this returns <code>false</code>.
     */
    public boolean fire(ProjectileArray projArr) {
        if(!isDead) {
            long currTime = System.currentTimeMillis();

            if(hasSpecial) {
                hasSpecial = false;
                doMove = false;
                isSpecialFired = true;

                specialShotTimer = currTime;
                lastFireTime = currTime;

                float laserYOrigin =
                        Resources.img_player_laser.getHeight() / 2.1f;
                projArr.addProjectile(new PlayerLaserShot(getX(),
                        getY() - laserYOrigin - (getCurrentFrameSpriteBitmap().getHeight() * 1.2f)));

                return true;
            }
            else if(projCount < maxProjCount && !isSpecialFired) {
                projCount++;
                projArr.addProjectile(getX(), getY(), Projectile.Type.PLAYER);
                lastFireTime = currTime;

                return true;
            }
            else if(hasRapid && !isSpecialFired) {
                if(currTime - lastFireTime >= rapidFireDelay) {
                    projCount++;
                    projArr.addProjectile(getX(), getY(), Projectile.Type.PLAYER);
                    lastFireTime = currTime;

                    return true;
                }
            }
        }

        return false;
    }

    /**
     * For use when a player's projectile is destroyed.
     */
    public void decrementProjectileCount() {
        if(projCount > 0) projCount--;
    }

    /**
     * Does player death cycle.
     */
    public void doDeath() {
        removePowerups();
        this.setStartAndEndFrames(1, 4); // set animation frame loop to dead player frames
        doAnimationLoop = true;
        isDead = true;
        doUpdate = false;
        projCount = 0;
    }

    /**
     * Checks for collision between hitBoxes of sprites. <code>Projectile</code>s that originate
     * from the player do not trigger a collision event.
     * @param sprite the <code>Sprite</code> to check for collision.
     * @return <code>true</code> collision detected
     */
    public boolean checkCollision(Sprite sprite) {
        if(sprite != null && !isDead
                && sprite.getHitBox() != null
                && sprite.getHitBox().intersect(getHitBox())) {
            if(sprite instanceof PowerUp) {
                PowerUp p = (PowerUp) sprite;
                switch (p.getColor()) {
                    case BLUE:
                        hasShield = true;
                        break;
                    case YELLOW:
                        startRapidFire();
                        break;
                    case RED:
                        hasSpecial = true;
                        break;
                }

                p.isDestroyed(true);
                return true;
            }
            else if(sprite.getClass() == Projectile.class) {
                Projectile projectile = (Projectile)sprite;

                Projectile.Type projType = projectile.getProjectileType();
                if(projType == Projectile.Type.PLAYER || projType == Projectile.Type.PLAYER_SPECIAL)
                    return false; // if projectile is from the player, ignore collision and return false
                else if(!projectile.isFromPlayer() && !projectile.isDestroyed()) {
                    if (hasShield) {
                        {
                            hasShield = false;
                            projectile.isDestroyed(true);
                        }
                        return true;
                    }
                    else {
                        doDeath();

                        return true;
                    }
                }
            }
            else {
                doDeath();
            }
        }

        return false;
    }

/****************************************
 * ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^
 * Members, Constructors, update(), fire()
 *
 * Getters / Setters
 * v v v v v v v v v v v v v v v
 *****************************************/

    /**
     * Set the number of projectiles the player currently has on the play field.
     * @param count <code>short</code> - only values greater than equal to 0 accepted.
     */
    public void setProjectileCount(short count) {
        if(count >= 0) projCount = count;
    }

    /**
     * @return the number of projectiles the player currently has on the field.
     */
    public short getProjectileCount() {
        return  projCount;
    }

    /**
     * Set the maximum number of projectiles the player is allowed to have on the play field
     * at a given time.
     * @param maxCount <code>short</code> - only values greater than equal to 0 accepted.
     */
    public void setMaxProjectileCount(short maxCount) {
        if(maxCount >= 0) maxProjCount = maxCount;
    }

    /**
     * @return the number of projectiles the the player is allowed to have on the
     * play field at one time.
     */
    public short getMaxProjectileCount() {
        return maxProjCount;
    }

    public boolean isDead() {
        return isDead;
    }

    /**
     *
     * @return <code>true</code> if the player is allowed to move.
     */
    public boolean doUpdate() {
        return doUpdate;
    }

    /**
     * Set <code>false</code> to prevent player from moving.
     * @param b
     */
    public void doUpdate(boolean b) {
        doUpdate = b;
    }

    /**
     * The boolean value that determines whether or not the play has the shield powerup.
     * @return <code>true</code> of play has the shied power-up active
     */
    public boolean hasShield() {
        return this.hasShield;
    }

    /**
     * The boolean value that determines whether or not the play has the shield powerup.
     * @param hasShield
     */
    public void hasShield(boolean hasShield) {
        this.hasShield = hasShield;
    }

    /**
     * The boolean value that determines whether or not the play has the special shot powerup.
     * @return
     */
    public boolean hasSpecial() {
        return hasSpecial;
    }

    /**
     * The boolean value that determines whether or not the play has the special shot powerup.
     * @param hasSpecial
     */
    public void hasSpecial(boolean hasSpecial) {
        this.hasSpecial = hasSpecial;
    }

    /**
     * The boolean value that determines whether or not the play has the rapid fire powerup.
     * @return
     */
    public boolean hasRapid() {
        return hasRapid;
    }

    /**
     * Sets all the conditions for rapid fire.
     */
    public void startRapidFire() {

        hasRapid = true;
        rapidCounter = rapidMaxSeconds;
    }

    public void removePowerups() {
        hasRapid = false;
        hasShield = false;
        hasSpecial = false;
    }
}