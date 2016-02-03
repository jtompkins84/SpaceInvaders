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
 * Created by Joseph on 1/31/2016.
 */
public class SpaceInvadersView extends SurfaceView implements Runnable {
    Context context;

    private Thread gameThread = null;

    private SurfaceHolder ourHolder;

    private volatile boolean playing;

    // game starts out paused
    private boolean paused = true;

    // need a canvas and paint object
    private Canvas canvas;
    private Paint paint;

    // tracks the games frame rate
    private long fps;

    private long timeThisFrame;

    private int screenX;
    private int screenY;

    private Player player;

    private Projectile projectile;

    private Projectile[] invadersProjectiles = new Projectile[200];
    private int nextBullet;
    private int maxInvaderBullets = 10;

    private Invader[] invaders = new Invader[60];
    private int numInvaders = 0;

    private DefenseBrick[] bricks = new DefenseBrick[400];
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

    public SpaceInvadersView(Context context, int x, int y) {
        super(context);

        this.context = context;

        ourHolder = getHolder();
        paint = new Paint();

        screenX = x;
        screenY = y;

        // TODO this is deprecated and we will most likely change it
        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC,0);

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
        }
        catch(IOException e) {
            Log.e("error", "failed to load sound files");
        }

        initializePlayField();
    }

    private void initializePlayField() {
        // Here we will initialize all the game objects

        // Make a new player
        player = new Player(context, screenX, screenY);

        // Prepare the players projectile

        // Initialize the invadersProjectiles array

        // Build an army of invaders

        // Build the shelters
    }

    @Override
    public void run() {
        while(playing) {
            long startFrameTime = System.currentTimeMillis();

            if(!paused) {
                update();
            }

            draw();

            timeThisFrame = System.currentTimeMillis() - startFrameTime;
            if(timeThisFrame >= 1) {
                fps = 1000 / timeThisFrame;
            }

            // We will do something here towards the end of the project;
        }
    }

    private void update() {
        // Did an invader bump into the side of the screen
        boolean bumped = false;


        // Has the player lost
        boolean lost = false;

        // Move the player
        player.update(fps);

        // Update the invaders if visible

        // Update all the invaders bullets if active

        // Did and invader bump into the edge of the screen

        if(lost) {
            initializePlayField();
        }

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
        if(ourHolder.getSurface().isValid()) {
            // Lock the canvas ready to draw
            canvas = ourHolder.lockCanvas();

            //Draw the background color
            canvas.drawColor(Color.argb(255, 0, 0, 0));

            // Choose the brush color for the drawing
            paint.setColor(Color.argb(255, 0, 255, 0));

            // Draw the player spaceship
            canvas.drawBitmap(player.getBitmap(), player.getX(), player.getY(), paint);

            // Draw the invaders

            // Draw the bricks if visible

            // Draw the players projectile if active

            // Draw the score and remaining lives
            // Change brush color
            paint.setColor(Color.argb(255, 0, 255, 0));
            paint.setTextSize(40);
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
        }
        catch (InterruptedException e) {
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
        switch(motionEvent.getAction() & MotionEvent.ACTION_MASK) {

            //Player has touched the screen
            case MotionEvent.ACTION_DOWN:

                break;
            // Player has removed finger from screen
            case MotionEvent.ACTION_UP:

                break;
        }

        return true;
    }
}
