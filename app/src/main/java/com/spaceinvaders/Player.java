package com.spaceinvaders;

import android.graphics.Bitmap;
import android.graphics.RectF;

public class Player extends Sprite {

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

    private long lastTime = 0l;

    private boolean hasShield;
    private boolean hasSpecial;
    private boolean hasRapid;
    private short rapidCounter = 0;
    private short rapidMaxSeconds = 15;

    /**
     * <code><b><i>Player</i></b></code><p>
     * <code>Player</code> contructor.
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
        if(isDead == true) {
            this.setStartAndEndFrames(1, 4); // set animation frame loop to dead player frames
            long currTime = System.currentTimeMillis();
            if(deathTime == 0) deathTime = currTime; // set initial time of death.

            // check to see if death period is done.
            if(currTime - deathTime >= 2000) {
                isDead = false;
                this.setStartAndEndFrames(0, 0); // reset frame to living player
                deathTime = 0;
            }
        }
        if(hitBox != null && doUpdate == true) {
            if(movement == Movement.RIGHT && hitBox.right < rBoundary) {
                move(speed / fps, 0.0f);
            }
            if(movement == Movement.LEFT && hitBox.left > lBoundary) {
                move(-(speed / fps), 0.0f);
            }
        }
    }

    /**
     * @return When the projectile count of the player is less than the max projectile count,
     * <code>projCount</code> will increment by 1 and <code>true</code> is returned.
     * Otherwise, this returns <code>false</code>.
     */
    public boolean fire(ProjectileArray projArr) {
        if( projCount < maxProjCount && isDead != true) {
            projCount++;
            projArr.addProjectile(getX(), getY(), true);
            return true;
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
        isDead = true;
        doUpdate = false;
    }

    /**
     * Checks for collision between hitBoxes of sprites. <code>Projectile</code>s that originate
     * from the player do not trigger a collision event.
     * @param sprite the <code>Sprite</code> to check for collision.
     * @return <code>true</code> collision detected
     */
    public boolean checkCollision(Sprite sprite) {
        if(sprite != null && !isDead &&
                sprite.getHitBox().intersect(getHitBox())) {
            if(sprite.getClass() == Projectile.class) {
                Projectile projectile = (Projectile)sprite;

                if(!projectile.isFromPlayer()) doDeath();
                else return false; // if projectile isn't from the player, ignore collision and return false

                return true;
            }
            else {
                sprite = null;
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
     * @param bool
     */
    public void doUpdate(boolean bool) {
        doUpdate = bool;
    }

    /**
     * The boolean value that determines whether or not the play has the shield powerup.
     * @return
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
     * The boolean value that determines whether or not the play has the special shot powerup.
     * @return
     */
    public boolean hasRapid() {
        return hasRapid;
    }

    public void hasRapid(boolean hasRapid) {
        this.hasRapid = hasRapid;
        rapidCounter = 0;
    }
}
