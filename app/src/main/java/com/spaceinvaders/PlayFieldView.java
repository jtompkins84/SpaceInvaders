package com.spaceinvaders;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
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
    // changes state of run() to do the resume countdown.
    private boolean resuming = false;

    // need a canvas and paint object
    private Canvas canvas;
    private Paint paint;

    // tracks the games frame rate
    private long fps;

    private long timeThisFrame;

    private float playFieldWidth;
    private float playFieldHeight;

    private Player player;
    private long startTime = -1;

    private ProjectileArray projectiles;
    private int maxPlayerBullets = 10; // TODO not used, should eventually remove

    private Invader[][] invaders = new Invader[6][5]; // TODO replace with InvaderArmy class

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
    private short countdownNumber = -1;

    private PointF playerFireButton;

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
        playFieldHeight = height + (height * 0.25f);
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
        playerFireButton = new PointF();

    // Make a new player
        player = new Player(playFieldWidth, playFieldHeight);

    // Prepare the projectiles
        projectiles = new ProjectileArray(player, playFieldHeight);

    // Build an army of invaders
        float horzSpacing = Resources.img_invader_a01.getWidth()* 1.33f; // the amount to horizontally space invaders apart from each other
        float vertSpacing = Resources.img_invader_a01.getHeight(); // the amount to vertical space invaders apart from each other
        float xCoord = playFieldWidth - (horzSpacing * 5.0f); // left most coordinate of the invader army
        float yCoord = vertSpacing * 3.0f; // top most coordinate of the invader army

    // initialize invader army from top to bottom
    // TODO temporary code. InvaderArmy class needs to be implemented.
    for(int i = 0; i < invaders.length; i++) {
        for(int j = 0; j < invaders[0].length; j++) {
            if(i < 2) {
                invaders[i][j] = new Invader(xCoord + (horzSpacing * j), yCoord + (vertSpacing * i), 'c');
            }
            else if(i >= 2 && i < 4) {
                invaders[i][j] = new Invader(xCoord + (horzSpacing * j), yCoord + (vertSpacing * i), 'b');
            }
            else {
                invaders[i][j] = new Invader(xCoord + (horzSpacing * j), yCoord + (vertSpacing * i), 'a');
            }
        }
    }

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
            if(resuming) doResumeCountdown();
            else if(!paused && player != null && player.doUpdate()) {
                update();
            }
            else if (!paused && player != null && !player.doUpdate()) {
                // while player
                player.update(fps);
                doPlayerDeath();
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
        // calculate projectile collisions
        for (Projectile p : projectiles.getProjectiles()) {
            if (p != null) {
                if (p.isFromPlayer() == false && player.checkCollision(p))
                    return; // skip the rest of the update process for player death.

                else if(p.isFromPlayer() == true) {
                    // TODO do InvaderArmy collisions
                    /* Invader class has checkCollision(Sprite) method that returns a boolean.
                     * If it returns true use "continue" keyword to skip to the next iteration of
                     * this for-loop so that collision check on the defense walls can be skipped
                     * and save a little bit of processing time.
                     *
                     * Also, the InvaderArmy class should implement a doCollisions method similar
                     * to the DefenseWall doCollision method, where it first checks to see
                     * if the Sprite object passed as a parameter is even close enough to the
                     * InvaderArmy object to do a collision check. If it isn't within a certain
                     * distance, there the collision calculations can be skipped to avoid
                     * additional processing.
                     */
                }

                for (DefenseWall w : walls) {
                    if(w != null) w.doCollisions(p);
                }
            }
        }

        // update walls
        for (DefenseWall w : walls) {
            if(w != null) w.update(fps);
        }


        // Move the player
        player.update(fps);

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
            for(int i = 0; i < invaders.length; i++) {
                for(int j = 0; j < invaders[0].length; j++) {
                    invaders[i][j].draw(canvas, paint, false);
                }
            }

            // Draw the bricks if visible
            for(DefenseWall wall : walls) {
                if(wall != null) {
                    wall.draw(canvas, paint, false);
                }
            }

            //Draw countdown numbers, if countdown is active
            switch (countdownNumber) {
                case 3:
                    canvas.drawColor(Color.argb(127, 0, 0, 0));
                    canvas.drawBitmap(Resources.img_countdown_3,
                            (playFieldWidth / 2) - (Resources.img_countdown_3.getWidth() / 2),
                            (playFieldHeight / 2) - (Resources.img_countdown_3.getHeight() / 2),
                            paint);
                    break;
                case 2:
                    canvas.drawColor(Color.argb(127, 0, 0, 0));
                    canvas.drawBitmap(Resources.img_countdown_2,
                            (playFieldWidth / 2) - (Resources.img_countdown_2.getWidth() / 2),
                            (playFieldHeight / 2) - (Resources.img_countdown_2.getHeight() / 2),
                            paint);
                    break;
                case 1:
                    canvas.drawColor(Color.argb(127, 0, 0, 0));
                    canvas.drawBitmap(Resources.img_countdown_1,
                            (playFieldWidth / 2) - (Resources.img_countdown_1.getWidth() / 2),
                            (playFieldHeight / 2) - (Resources.img_countdown_1.getHeight() / 2),
                            paint);
                    break;
                default:
                    break;
            }

            // Draw the score and remaining lives
            // Change brush color
            paint.setColor(Color.argb(255, 0, 255, 0));
            paint.setTextSize(40 * DPIRatio);
            canvas.drawText("Score: " + score + "  Lives: " + lives, 10, 50, paint);

            // Draw Player controls
            paint.setAntiAlias(true);
            paint.setColor(Color.argb(255, 0, 180, 0));
            canvas.drawCircle(315, this.getBottom() - 485, 120, paint);
            canvas.drawCircle(playFieldWidth - 315, this.getBottom() - 485, 120, paint);
            canvas.drawCircle(playFieldWidth - 620, this.getBottom() - 485, 120, paint);

            paint.setColor(Color.argb(255, 0, 255, 0));
            canvas.drawCircle(300, this.getBottom() - 500, 120, paint);
            canvas.drawCircle(playFieldWidth - 300, this.getBottom() - 500, 120, paint);
            canvas.drawCircle(playFieldWidth - 635, this.getBottom() - 500, 120, paint);

            // Draw everything to the screen
            ourHolder.unlockCanvasAndPost(canvas);
        }
    }

    // IF SpaceInvadersActivity is paused/stopped
    public void pause() {
        playing = false;

        // Resets timer to allow doResumeCountdown() to start from top of countdown.
        // This insures that if the device is put into a "sleep" state, the resume countdown
        // will always provide a 3 second countdown, even if the game is paused during the countdown.
        startTime = -1;

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

        resuming = true; // sets the state to resume
    }

    public void resumeNoCountdown() {
        playing = true;
        countdownNumber = -1;
        startTime = -1;
        resuming = false;

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
                if (motionEvent.getX() > 180 && motionEvent.getX() < 420 && motionEvent.getY() >
                        (this.getBottom() - 500) - 120 && motionEvent.getY() < (this.getBottom() - 500) + 120)
                {
                    player.fire(projectiles); // TODO REMOVE LINE. for testing purposes.
 //                   projectiles.addProjectile(playFieldWidth / 2, playFieldHeight / 2, false); // TODO REMOVE LINE. for testing purposes.
 //                   if(!resuming) resuming = true;
                }

                if (motionEvent.getX() > (playFieldWidth - 635) - 120 && motionEvent.getX() < (playFieldWidth - 635) + 120
                        && motionEvent.getY() > (this.getBottom() - 500) - 120 && motionEvent.getY() < (this.getBottom() - 500) + 120)
                {
                    player.setMovementState(Movement.LEFT);
                }

                if (motionEvent.getX() > (playFieldWidth - 300) - 120 && motionEvent.getX() < (playFieldWidth - 300) + 120
                        && motionEvent.getY() > (this.getBottom() - 500) - 120 && motionEvent.getY() < (this.getBottom() - 500) + 120)
                {
                    player.setMovementState(Movement.RIGHT);
                }

                break;

            // Player has removed finger from screen
            case MotionEvent.ACTION_UP:
                player.setMovementState(Movement.STOPPED);
                
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

    /**
     * The first call to this method decrements <code>lives</code> by 1,
     * initializes a death cycle timer, removes all projectiles from the play field, and
     * stops the player from being able to fire or move as well as prevents the rest of
     * the <code>Sprite</code> objects from updating.
     * <div></div>
     * Each consecutive call after displays the countdown numbers and once the timer reaches
     * 5 seconds, player movement is returned and the playfield resumes normal updates.
     */
    private void doPlayerDeath() {

        long currTime = System.currentTimeMillis();
        if(player.isDead() && startTime == -1) {
            startTime = currTime;
            player.doUpdate(false);
            lives--;
            projectiles.removeAllProjectiles();
            countdownNumber = 3;
        }

        long timeDif = currTime - startTime;
        if(timeDif >= 3000 && timeDif < 4000) countdownNumber = 2;
        else if(timeDif >= 4000 && timeDif < 5000) countdownNumber = 1;
        else if(timeDif >= 5000) {
            countdownNumber = -1;
            startTime = -1;
            player.doUpdate(true);
        }
    }

    /**
     * Does a 3 second countdown before allowing the game thread to resume updating.
     */
    private void doResumeCountdown() {
        long currTime = System.currentTimeMillis();
        long timeDif = 0;
        timeDif = currTime - startTime;
        if(startTime == -1) {
            startTime = currTime;
            countdownNumber = 3;
            return;
        }

        if(timeDif >= 1000 && timeDif < 2000) countdownNumber = 2;
        else if(timeDif >= 2000 && timeDif < 3000) countdownNumber = 1;
        else if(timeDif >= 3000) {
            countdownNumber = -1;
            startTime = -1;
            resuming = false;
        }
    }
}
