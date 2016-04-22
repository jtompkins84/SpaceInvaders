package com.spaceinvaders;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.io.IOException;

public class PlayFieldView extends SurfaceView implements Runnable, View.OnTouchListener {
    private Thread gameThread = null;
    private SurfaceHolder ourHolder;

    private volatile boolean playing;
    RectF controlPanel;

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

    private InvaderArmy invaderArmy;

    private DefenseWall[] walls = new DefenseWall[4];

    private SoundPool soundPool;
    private int playerExplodeID = -1;
    private int invaderExplodeID = -1;
    private int shootID = -1;
    private int damageShelterID = -1;
    private int uhID = -1;
    private int ohID = -1;


    private int score = 0;

    private int lives = 3;

    private boolean uhOrOh;

    private short countdownNumber = -1;

/*******************************************************************************
 * Constructor
 *
 * @param context super constructor needs reference to this
 * @param width <code>int</code> - this view's width in pixels
 * @param height <code>int</code> - this view's height in pixels
 ********************************************************************************/
    public PlayFieldView(Context context, float width, float height) {
        super(context);

        playFieldWidth = width;
        playFieldHeight = height + (height * 0.25f);
        ourHolder = getHolder();
        paint = new Paint();

        controlPanel = new RectF(0, playFieldHeight,
                playFieldWidth, playFieldHeight *2);


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
    // Make a new player
        player = new Player(playFieldWidth, playFieldHeight);
        player.startRapidFire();

    // Prepare the projectiles
        projectiles = new ProjectileArray(player, playFieldHeight);

    // Build an army of invaders
        invaderArmy = new InvaderArmy(playFieldWidth, playFieldHeight, projectiles, this);

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
                    invaderArmy.doCollision(p);
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

        // Update the invaders
        invaderArmy.update(fps);

        // Update all the invaders bullets if active

        // Did and invader bump into the edge of the screen

        if (lost) {
            initializePlayField();
        }


        projectiles.update(fps);
//        projectiles.getProjectiles()[0].update(fps);
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
            invaderArmy.draw(canvas, paint, false);

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
            paint.setTextSize(40 * Resources.DPIRatio);
            canvas.drawText("Score: " + score + "  Lives: " + lives, 10, 50, paint);

            // Draw Player controls
            paint.setAntiAlias(true);

            paint.setColor(Color.argb(255, 10, 10, 10));
            canvas.drawRect(controlPanel, paint);

            paint.setColor(Color.argb(255, 0, 180, 0));
            canvas.drawCircle(playFieldWidth / 5, this.getBottom() - playFieldHeight / 4, playFieldWidth / 11, paint);

            canvas.drawCircle(playFieldWidth - (playFieldWidth/2 - playFieldWidth/15), this.getBottom() - playFieldHeight/4, playFieldWidth/11, paint);
            canvas.drawCircle(playFieldWidth - playFieldWidth/5, this.getBottom() - playFieldHeight/4, playFieldWidth/11, paint);


            //Slider
            /*
            canvas.drawCircle((playFieldWidth - (playFieldWidth/2 - playFieldWidth/15)) - playFieldWidth/100, (this.getBottom() - playFieldHeight/4) - playFieldHeight/100, playFieldWidth/11, paint);
            canvas.drawCircle(playFieldWidth - (playFieldWidth/5 - playFieldWidth/100), (this.getBottom() - playFieldHeight/4) - playFieldHeight/100, playFieldWidth/11, paint);
            canvas.drawRect((playFieldWidth - (playFieldWidth / 2 - playFieldWidth / 15)) - playFieldWidth / 100, ((this.getBottom() - playFieldHeight / 4) - playFieldHeight / 100) - playFieldWidth / 11,
                    playFieldWidth - (playFieldWidth / 5 - playFieldWidth / 100), ((this.getBottom() - playFieldHeight / 4) - playFieldHeight / 100) + playFieldWidth / 11, paint);
            */

            paint.setColor(Color.argb(255, 0, 255, 0));

            canvas.drawCircle(playFieldWidth / 5, (this.getBottom() - playFieldHeight / 4) - playFieldHeight / 100, playFieldWidth / 11, paint);

            canvas.drawCircle(playFieldWidth - (playFieldWidth/2 - playFieldWidth/15), (this.getBottom() - playFieldHeight/4) - playFieldHeight/100, playFieldWidth/11, paint);
            canvas.drawCircle(playFieldWidth - playFieldWidth/5, (this.getBottom() - playFieldHeight/4) - playFieldHeight/100, playFieldWidth/11, paint);


            //Joystick Circle
            /*
            canvas.drawCircle((((playFieldWidth - (playFieldWidth / 2 - playFieldWidth / 15)) - playFieldWidth / 100) + (playFieldWidth - (playFieldWidth / 5 - playFieldWidth / 100)))/2,
                    (this.getBottom() - playFieldHeight/4) - playFieldHeight/100, playFieldWidth/11 - playFieldWidth/100, paint);
            */

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

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {

            case MotionEvent.ACTION_DOWN:
                if (motionEvent.getX() > playFieldWidth/5 - playFieldWidth/11
                        && motionEvent.getX() < playFieldWidth/5 + playFieldWidth/11
                        && motionEvent.getY() > ((this.getBottom() - playFieldHeight/4) - playFieldHeight/100) - playFieldWidth/11
                        && motionEvent.getY() < ((this.getBottom() - playFieldHeight/4) - playFieldHeight/100) + playFieldWidth/11)
                {
                    player.fire(projectiles); // TODO REMOVE LINE. for testing purposes.
 //                   projectiles.addProjectile(playFieldWidth / 2, playFieldHeight / 2, false); // TODO REMOVE LINE. for testing purposes.
 //                   if(!resuming) resuming = true;
                }

                if (motionEvent.getX() > (playFieldWidth - (playFieldWidth/2 - playFieldWidth/15)) - playFieldWidth/11
                        && motionEvent.getX() < (playFieldWidth - (playFieldWidth/2 - playFieldWidth/15)) + playFieldWidth/11
                        && motionEvent.getY() > ((this.getBottom() - playFieldHeight/4) - playFieldHeight/100) - playFieldWidth/11
                        && motionEvent.getY() < ((this.getBottom() - playFieldHeight/4) - playFieldHeight/100) + playFieldWidth/11)
                {
                    player.setMovementState(Movement.LEFT);
                }

                if (motionEvent.getX() > (playFieldWidth - playFieldWidth/5) - playFieldWidth/11
                        && motionEvent.getX() < (playFieldWidth - playFieldWidth/5) + playFieldWidth/11
                        && motionEvent.getY() > ((this.getBottom() - playFieldHeight/4) - playFieldHeight/100) - playFieldWidth/11
                        && motionEvent.getY() < ((this.getBottom() - playFieldHeight/4) - playFieldHeight/100) + playFieldWidth/11)
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

    // Need to implement the ability to drag the drawn circle.

    /*
    public boolean onDragEvent(DragEvent dragEvent) {

        return true;
    }
    */

    public void playerFire() {
        player.fire(projectiles);
    }

    public void playerMovement(Movement direction) {
        player.setMovementState(direction);
    }

    public void addToPlayerScore(int plusScore) {
        score += plusScore;
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
        if(lives == 0) {
        }

        long currTime = System.currentTimeMillis();
        if(player.isDead() && startTime == -1) {
            startTime = currTime;
            player.doUpdate(false);
            lives--;
            projectiles.removeAllProjectiles();
        }

        long timeDif = currTime - startTime;
        if(timeDif >= 2000 && timeDif < 3000) countdownNumber =3;
        else if(timeDif >= 3000 && timeDif < 4000) countdownNumber = 2;
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

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }
}
