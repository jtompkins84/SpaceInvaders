package com.spaceinvaders;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

/**
 * Created by Joseph on 2/5/2016.
 */
public class PlayFieldView extends SurfaceView implements Runnable {
    private Thread gameThread = null;
    private SurfaceHolder ourHolder;
    /**
     * Set to 1.0f by default. DPIRatio is adjusted during resource initialization and is used to
     * scale dimensions & coordinates to the correct proportions so that
     * the play field looks the same no matter what device it is displayed on.
     */
    float DPIRatio = 1.0f;

    private volatile boolean playing;

    // game starts out paused
    private boolean paused = false;

    // need a canvas and paint object
    private Canvas canvas;
    private Paint paint;

    // tracks the games frame rate
    private long fps;

    private long timeThisFrame;

    private float playFieldWidth;
    private float playFieldHeight;

    private Player player;

    private ProjectileArray projectiles;
    private int maxPlayerBullets = 10;

    private Invader[] invaders = new Invader[60]; // TODO replace with InvaderArmy class
    private int numInvaders = 0;

    private DefenseBrick[][] bricks = new DefenseBrick[3][4];
    private DefenseWall[] walls = new DefenseWall[4];
    private int numBricks;

    private SoundPool soundPool;
    private int playerExplodeID = -1;
    private int invaderExplodeID = -1;
    private int shootID = -1;
    private int damageShelterID = -1;
    private int uhID = -1;
    private int ohID = -1;

    // The score
    private int score = 0;

    // Lives
    private int lives = 3;

    private long menaceInterval = 1000;

    private boolean uhOrOh;

    private long lastMenaceTime = System.currentTimeMillis();

/*******************************************************************************
 * Constructor
 *
 * @param context super constructor needs reference to this
 * @param width <code>int</code> - this view's width in pixels
 * @param height <code>int</code> - this view's height in pixels
 ********************************************************************************/
    public PlayFieldView(Context context, int width, int height) {
        super(context);

        playFieldWidth = width;
        playFieldHeight = height;
        ourHolder = getHolder();
        paint = new Paint();

        // TODO this is deprecated and we will most likely change it
        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);

        try {
            AssetManager assetManager = context.getAssets();
            AssetFileDescriptor descriptor;

            descriptor = assetManager.openFd("invaderexplode.ogg");
            invaderExplodeID = soundPool.load(descriptor, 0);

            descriptor = assetManager.openFd("damageshelter.ogg");
            damageShelterID = soundPool.load(descriptor, 0);

            descriptor = assetManager.openFd("playerexplode.ogg");
            playerExplodeID = soundPool.load(descriptor, 0);

            descriptor = assetManager.openFd("damageshelter.ogg");
            damageShelterID = soundPool.load(descriptor, 0);

            descriptor = assetManager.openFd("uh.ogg");
            uhID = soundPool.load(descriptor, 0);

            descriptor = assetManager.openFd("oh.ogg");
            ohID = soundPool.load(descriptor, 0);
        } catch (IOException e) {
            Log.e("error", "failed to load sound files");
        }

        initializePlayField();
    }

    private void initializePlayField() {
    // Here we will initialize all the game objects

    // Make a new player
        player = new Player(playFieldWidth, playFieldHeight);

    // Prepare the projectiles
        projectiles = new ProjectileArray(player, playFieldHeight);

    // Build an army of invaders

    // Build the defense walls
        playFieldWidth = this.getResources().getDisplayMetrics().widthPixels;

        walls[1] = new DefenseWall((playFieldWidth / 2) + (Resources.img_brick_01.getWidth() / 2), playFieldHeight - (playFieldHeight / 5));
        walls[0] = new DefenseWall((playFieldWidth / 2) - (walls[1].getDimensions().x * 2.0f) + (Resources.img_brick_01.getWidth() / 2), playFieldHeight - (playFieldHeight / 5));
        walls[2] = new DefenseWall((playFieldWidth / 2) + (walls[1].getDimensions().x * 2.0f) + (Resources.img_brick_01.getWidth() / 2), playFieldHeight - (playFieldHeight / 5));
    }

    @Override
    public void run() {
        while (playing) {
            long startFrameTime = System.currentTimeMillis();
            if (!paused) {
                update();
            }

            draw();
            timeThisFrame = System.currentTimeMillis() - startFrameTime;
            if (timeThisFrame >= 1) {
                fps = 1000 / timeThisFrame;
            }

            // We will do something here towards the end of the project
        }
    }

    private void update() {
        // Has the player lost
        boolean lost = false;

        // update walls and calculate collisions with walls
        for(DefenseWall w : walls) {
            if(w != null) {
                for (Projectile p : projectiles.getProjectiles()) {
                    if (p != null)
                        w.doCollisions(p);
                }

                w.update(fps);
            }
        }



        // Move the player
        player.update(fps);

//        player.fire(projectiles); // TODO REMOVE THIS LINE. Only for testing functionality of player firing.

        // Update the invaders if visible

        // Update all the invaders bullets if active

        // Did and invader bump into the edge of the screen

        if (lost) {
            initializePlayField();
        }


        projectiles.update(fps);
//        projectiles.getProjectiles()[0].update(fps);

        // Update the players projectile

        // Has the player's projectile hit the top of the screen

        // Has an invaders' projectile hit the bottom of the screen

        // Has the player's projectile hit and invader

        // Has an alien projectile hit a shelter brick

        // Has the player projectile hit a shelter brick

        // Has an invader projectile hit the player ship
    }

    private void draw() {
        //Make sure our drawing surface is valid or we crash
        if (ourHolder.getSurface().isValid()) {
            // Lock the canvas ready to draw
            canvas = ourHolder.lockCanvas();
//            canvas.setDensity(100);

            //Draw the background color
            canvas.drawColor(Color.argb(255, 0, 0, 0));

            // Choose the brush color for the drawing
            paint.setColor(Color.argb(255, 0, 255, 0));

            // Draw the  projectiles if active
            projectiles.draw(canvas, paint, false);

            // Draw the player spaceship
            player.draw(canvas, paint, false);

            // Draw the invaders

            // Draw the bricks if visible
            for(DefenseWall wall : walls) {
                if(wall != null) {
                    wall.draw(canvas, paint, false);
                }
            }

            // Draw the score and remaining lives
            // Change brush color
            paint.setColor(Color.argb(255, 0, 255, 0));
            paint.setTextSize(40 * DPIRatio);
            canvas.drawText("Score: " + score + "  Lives: " + lives, 10, 50, paint);

            // Draw everything to the screen
            ourHolder.unlockCanvasAndPost(canvas);
        }
    }

    // IF SpaceInvadersActivity is paused/stopped
    public void pause() {
        playing = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            Log.e("Error:", "joining thread");
        }
    }

    // If SpaceInvadersActivity is started then
    // start out thread.
    public void resume() {
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    // The SurfaceView class implements onTouchListner
    // So we can override this method and detect screen touches
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {

            //Player has touched the screen
            case MotionEvent.ACTION_DOWN:
                player.fire(projectiles);
                break;
            // Player has removed finger from screen
            case MotionEvent.ACTION_UP:

                break;
        }

        return true;
    }

    public void playerFire() {
        player.fire(projectiles);
    }

    public void playerMovement(Movement direction) {
        player.setMovementState(direction);
    }
}
