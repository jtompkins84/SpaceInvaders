package com.spaceinvaders;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.tutorials.joseph.spaceinvaders.R;

import java.io.IOException;

/**
 * Created by Joseph on 2/5/2016.
 */
public class PlayFieldView extends SurfaceView implements Runnable {
    private Thread gameThread = null;
    private UserControllerView userControllerView;
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

    private int playFieldWidth;
    private int playFieldHeight;

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

    /*
    *
    * RESOURCES
    * v v v v v v v v v
    */
    SpriteImage bmp_player;
    SpriteImage bmp_invader_a01;
    SpriteImage bmp_invader_a02;
    SpriteImage bmp_projectile_a;
    SpriteImage bmp_brick_top_left01;
    SpriteImage bmp_brick_top_left02;
    SpriteImage bmp_brick_top_left03;
    SpriteImage bmp_brick_top_left04;

    public PlayFieldView(Context context, UserControllerView userControllerView, int width, int height) {
        super(context);

        this.userControllerView = userControllerView;
        playFieldWidth = width;
        playFieldHeight = height;
        ourHolder = getHolder();
        paint = new Paint();

        // TODO initialize all resources. Perhaps create seperate method or class for this.
        initResources();

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
        player = new Player(bmp_player, playFieldWidth, playFieldHeight);

        // Prepare the players projectile

        // Initialize the invadersProjectiles array

        // Build an army of invaders

        // Build the shelters
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

        if (lost) {
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
        if (ourHolder.getSurface().isValid()) {
            // Lock the canvas ready to draw
            canvas = ourHolder.lockCanvas();
//            canvas.setDensity(100);

            //Draw the background color
            canvas.drawColor(Color.argb(255, 0, 0, 0));

            // Choose the brush color for the drawing
            paint.setColor(Color.argb(255, 0, 255, 0));

            // Draw the player spaceship
            player.draw(canvas, paint, true);

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

                break;
            // Player has removed finger from screen
            case MotionEvent.ACTION_UP:

                break;
        }

        return true;
    }

    private boolean initResources() {
        int dpi = getResources().getDisplayMetrics().densityDpi;
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inDensity = 480 * (640 / dpi);
        opt.inTargetDensity = 640;
        opt.inScreenDensity = dpi;

        float DPIRatio = (float)dpi / (float)opt.inDensity;
        if(DPIRatio < 1.0f) DPIRatio = 1.0f - DPIRatio;
        Log.v("DPI Ratio", opt.inDensity + " / " + dpi + " = " + ((float)opt.inDensity / (float)dpi));

        bmp_player = new SpriteImage(BitmapFactory.decodeResource(getContext().getResources(), R.drawable.player, opt), DPIRatio);
        Log.v("Width Ratio", bmp_player.getBitmap().getWidth() + " / " + getWidth() + " = " + ((float)bmp_player.getBitmap().getWidth() / (float)playFieldWidth));
        bmp_invader_a01 = new SpriteImage(BitmapFactory.decodeResource(getContext().getResources(), R.drawable.invader_a01, opt), 100 / opt.inDensity);
        bmp_invader_a02 = new SpriteImage(BitmapFactory.decodeResource(getContext().getResources(), R.drawable.invader_a02, opt), 100 / opt.inDensity);
        bmp_projectile_a = new SpriteImage(BitmapFactory.decodeResource(getContext().getResources(), R.drawable.projectile_a, opt), 100 / opt.inDensity);

        return true;
    }
}
