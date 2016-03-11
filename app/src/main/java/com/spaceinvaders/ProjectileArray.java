package com.spaceinvaders;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

/**
 * This class is designed to handle the projectiles of the play field as a iterable
 * collective.
 */
public class ProjectileArray {

    private Projectile[] projectiles;
    private float playFieldHeight;
    private Player player;
    private int activeProjectileCount = 0;

    /**
     * Creates a new instance of <code>ProjectileArray</code> with a <code>Projectile[]</code>
     * of size 64.
     */
    public ProjectileArray(Player p, float playFieldHeight) {
        this.playFieldHeight = playFieldHeight;
        projectiles = new Projectile[64];

        for(int i = 0; i < projectiles.length; i++) projectiles[i] = null;

        player = p;
    }

    /**
     * Creates an instance of <code>ProjectileArray</code> with a <code>Projectile[]</code>
     * with length specified by parameter <code>size</code>. Will set the length of the
     * array to be of 64 if <code>size</code> is outside of the bounds allowed.
     * @param size <code>int</code> that is between 1 and 300
     */
    public ProjectileArray(int size, Player p, float playFieldHeight) {
        this.playFieldHeight = playFieldHeight;

        if(size > 0 && size < 256) projectiles = new Projectile[size];
        else {
            projectiles = new Projectile[64];
            Log.e("ProjectileArray", "Constructor arg \"size\" must be less than 256 or greater than 0.");
        }

        for(int i = 0; i < projectiles.length; i++) projectiles[i] = null;

        player = p;
    }

    /**
     * Performs the <code>update()</code> method for all of the <code>Projectile</code> objects
     * in the array.
     * @param fps <code>short</code>
     */
    public void update(long fps) {
        if(projectiles != null) {
            int count = 0;  // to check against activeProjectileCount. Method will terminate once
                            // all active projectiles are checked to prevent extra, unnecessary
                            // checks. PURPOSE: Optimization
            for(int i = 0; i < projectiles.length; i++) {
                if(projectiles[i] != null) {
                    count++;    // increment number of active projectiles updated.
                    projectiles[i].update(fps);
                    if(projectiles[i].getY() > playFieldHeight || projectiles[i].getY() < 20) {

                        projectiles[i].isDestroyed(true);
                    }
                    else if(projectiles[i].isCollisionDetected() == true) projectiles[i].isDestroyed(true);

                    if(projectiles[i].isDestroyed() == true) {
                        if (projectiles[i].isFromPlayer() == true)
                            player.decrementProjectileCount();
                        projectiles[i] = null;
                        activeProjectileCount--;
                    }
                }
            // method terminates once all active projectiles have been updated.
                if(count >= activeProjectileCount) return;
            }
        }
    }

    public void draw(Canvas canvas, Paint paint, boolean doDrawHitbox) {
        if(projectiles != null) {
            for(int i = 0; i < projectiles.length; i++) {
                if(projectiles[i] != null) {
                    projectiles[i].draw(canvas, paint, doDrawHitbox);
                }
            }
        }
    }

    /**
     * Adds a <code>Projectile</code> to the array.
     */
    public void addProjectile(float posX, float posY, boolean isFromPlayer) {
        if(projectiles != null) {
            for(int i = 0; i < projectiles.length; i++) {
                if(projectiles[i] == null) {
                    projectiles[i] = new Projectile(posX, posY, isFromPlayer);
                    activeProjectileCount++;
                    return;
                }
            }
        }
    }

    /**
     * Get the array of <code>Projectile</code>s
     * @return <code>Projectile[]</code>
     */
    public Projectile[] getProjectiles() {
        return projectiles;
    }
}
