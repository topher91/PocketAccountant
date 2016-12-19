package cst4701.game.pocketaccountant;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class GameActivity extends AppCompatActivity {

    //initial hunger and happy values
    private int hungerValue;
    private int happyValue;
    private int hygieneValue;
    private int energyValue;
    private int ageCounter;



    /*
        - Values that need to be saved into the DB - status values... happy, hunger, fun, energy.
        - What is his current age?
            - Kid
            - Young Adult
            - Adult
        - How to calculate his age?
            - Maybe have a counter that adds 1 every cycle and then at set numbers he evolves
            - Between 0 and 17 he is kid
            - Between 18 and 24 is a young adult
            - Between 25 and 64 is an adult
            - 65+ is an old fart
            Something like that
        TO DO:
           x - Fix progress bars not turning red
            - Add icons for fun and happy
           x - Add accountant image
            - Make accountant pace back and forth on screen
           x - Set up database
                - This would be nice to do but if we run out of time I think he will
                  accept the game with everything else
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //access the shared preferences file. If the values do not exist it will default to 100 or 0
        //if the values exist it assigns the value in the file to the global variable
        SharedPreferences values = getApplicationContext().getSharedPreferences("values", 0);
        happyValue = values.getInt("happy", 100);
        hungerValue = values.getInt("hunger", 100);
        hygieneValue = values.getInt("hygiene", 100);
        energyValue = values.getInt("energy", 100);
        ageCounter = values.getInt("age", 0);

        //assign variable to TextView objects
        final TextView timer = (TextView)findViewById(R.id.countdownTimer);

        final ImageView hungerIcon = (ImageView)findViewById(R.id.hungerIcon);
        final ImageView happyIcon = (ImageView)findViewById(R.id.happyIcon);
        final ImageView funIcon = (ImageView) findViewById(R.id.hygieneIcon);
        final ImageView energyIcon = (ImageView) findViewById(R.id.energyIcon);

        //assign variable to Happy progress bar
        final ProgressBar happyBar = (ProgressBar) findViewById(R.id.happyBar);

        //set max progress bar value
        happyBar.setMax(100);
        //set starting progress bar value
        happyBar.setProgress(happyValue);
        //set progress bar height
        happyBar.setScaleY(3f);
        //set color of progress bar using hex value
        happyBar.getProgressDrawable().setColorFilter(Color.parseColor("#1e9626"),
                android.graphics.PorterDuff.Mode.SRC_IN);

        //assign variable to hunger bar
        final ProgressBar hungerBar = (ProgressBar) findViewById(R.id.hungerBar);

        //set max progress bar value
        hungerBar.setMax(100);
        //set starting progress bar value
        hungerBar.setProgress(hungerValue);
        //set progress bar height
        hungerBar.setScaleY(3f);
        //set color of progress bar using hex value
        hungerBar.getProgressDrawable().setColorFilter(Color.parseColor("#1e9626"),
                android.graphics.PorterDuff.Mode.SRC_IN);

        final ProgressBar hygieneBar = (ProgressBar) findViewById(R.id.hygieneBar);
        hygieneBar.setMax(100);
        hygieneBar.setProgress(hygieneValue);
        hygieneBar.setScaleY(3f);
        hygieneBar.getProgressDrawable().setColorFilter(Color.parseColor("#1e9626"),
                android.graphics.PorterDuff.Mode.SRC_IN);

        final ProgressBar energyBar = (ProgressBar) findViewById(R.id.energyBar);
        energyBar.setMax(100);
        energyBar.setProgress(energyValue);
        energyBar.setScaleY(3f);
        energyBar.getProgressDrawable().setColorFilter(Color.parseColor("#1e9626"),
                android.graphics.PorterDuff.Mode.SRC_IN);

        //assign variable to image view to hold accountant image
        final ImageView accountantImage = (ImageView) findViewById(R.id.accountantImage);
        //set initial accountant image
        accountantImage.setBackgroundResource(R.drawable.baby_accountant_happy);
        GameEngine engine = new GameEngine(ageCounter, happyValue, hungerValue, energyValue,
                hygieneValue, happyBar, hungerBar, energyBar, hygieneBar, accountantImage);

        new CountDownTimer(5000, 1000) {

            //what to during each tick of the timer
            public void onTick(long millisUntilFinished) {
                GameEngine engine = new GameEngine(ageCounter, happyValue, hungerValue, energyValue,
                        hygieneValue, happyBar, hungerBar, energyBar, hygieneBar, accountantImage);
                timer.setText(Long.toString(millisUntilFinished / 1000));
                engine.setAge();

                ageCounter++;
            }

            //what to do when count down finishes
            public void onFinish() {
                //check if happiness or hunger go below 0
                //if not then continue as normal and restart countdown
                if ((happyValue-10)>0 && (hungerValue-10)>0 && (hygieneValue -10)>0 && (energyValue-10)>0){
                    //decrease hunger value and happy value by 10
                    hungerValue -= 10;
                    happyValue -= 10;
                    hygieneValue -= 10;
                    energyValue -= 10;

                    //set progress bars to current hunger, happy, fun, and energy value
                    happyBar.setProgress(happyValue);
                    hungerBar.setProgress(hungerValue);
                    hygieneBar.setProgress(hygieneValue);
                    energyBar.setProgress(energyValue);

                    //change text to show current hunger, happy, fun and energy value
                    //restart countdown
                    this.start();
                }
                //if happiness is <= 0 set happiness values to 0 and end countdown
                if ((happyValue-10) <= 0){
                    timer.setText("0");
                    happyBar.setProgress(0);

                    hungerBar.setProgress(hungerValue-10);
                    hygieneBar.setProgress(hygieneValue -10);
                    energyBar.setProgress(energyValue-10);

                    accountantImage.setBackgroundResource(R.drawable.tombstone);
                    accountantImage.requestLayout();
                    accountantImage.getLayoutParams().height = Math.round(dpToPx());
                }
                //if hunger <= 0 set hunger values to 0 and end countdown
                else if ((hungerValue-10) <= 0) {
                    timer.setText("0");
                    hungerBar.setProgress(0);

                    happyBar.setProgress(happyValue-10);
                    hygieneBar.setProgress(hygieneValue -10);
                    energyBar.setProgress(energyValue-10);

                    //game over, show dead accountant
                    //fix this to show correct accountant
                    accountantImage.setBackgroundResource(R.drawable.tombstone);
                    accountantImage.requestLayout();
                    accountantImage.getLayoutParams().height = Math.round(dpToPx());
                }

                //if fun <=0 set fun values to 0 and end countdown
                else if ((hygieneValue -10) <= 0){
                    timer.setText("0");
                    hygieneBar.setProgress(0);

                    happyBar.setProgress(happyValue-10);
                    hungerBar.setProgress(hungerValue-10);
                    energyBar.setProgress(energyValue-10);

                    accountantImage.setBackgroundResource(R.drawable.tombstone);
                    accountantImage.requestLayout();
                    accountantImage.getLayoutParams().height = Math.round(dpToPx());

                }

                //if energy <=0 set fun values to 0 and end countdown
                else if ((energyValue-10) <= 0){
                    timer.setText("0");
                    energyBar.setProgress(0);

                    happyBar.setProgress(happyValue-10);
                    hungerBar.setProgress(hungerValue-10);
                    hygieneBar.setProgress(hygieneValue -10);

                    accountantImage.setBackgroundResource(R.drawable.tombstone);
                    accountantImage.requestLayout();
                    accountantImage.getLayoutParams().height = Math.round(dpToPx());
                }
            }
        }.start();

        //create listener for happy bar
        happyIcon.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                Animation shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
                findViewById(R.id.accountantImage).startAnimation(shake);

                //happy and hunger value can't exceed 100
                //check what value will be when adding 20, if > 100
                //reset happy value to 100 then update text and progress
                //bar to reflect new value
                if ((happyValue+20)<=100){
                    happyValue += 20;
                    happyBar.setProgress(happyValue);
                    //happy.setText(Integer.toString(happyValue));
                }
                else {
                    happyValue = 100;
                    happyBar.setProgress(happyValue);
                    //happy.setText(Integer.toString(happyValue));
                }

                GameEngine engine = new GameEngine(ageCounter, happyValue, hungerValue, energyValue,
                        hygieneValue, happyBar, hungerBar, energyBar, hygieneBar, accountantImage);

                engine.setAge();

            }
        });

        //create listener for hunger bar
        hungerIcon.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                //showPopupWindow(v, hungryBar);

                Animation shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
                findViewById(R.id.accountantImage).startAnimation(shake);
                //happy and hunger value can't exceed 100
                //check what value will be when adding 20, if > 100
                //reset happy value to 100 then update text and progress
                //bar to reflect new value
                if ((hungerValue+20)<=100){
                    hungerValue += 20;
                    hungerBar.setProgress(hungerValue);
                    //hunger.setText(Integer.toString(hungerValue));
                }
                else {
                    hungerValue = 100;
                    hungerBar.setProgress(hungerValue);
                    //hunger.setText(Integer.toString(hungerValue));
                }
                GameEngine engine = new GameEngine(ageCounter, happyValue, hungerValue, energyValue,
                        hygieneValue, happyBar, hungerBar, energyBar, hygieneBar, accountantImage);

                engine.setAge();


            }
        });

        funIcon.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Animation shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
                findViewById(R.id.accountantImage).startAnimation(shake);

                if ((hygieneValue +20)<=100){
                    hygieneValue += 20;
                    hygieneBar.setProgress(hygieneValue);
                }
                else {
                    hygieneValue = 100;
                    hygieneBar.setProgress(hygieneValue);
                }
                GameEngine engine = new GameEngine(ageCounter, happyValue, hungerValue, energyValue,
                        hygieneValue, happyBar, hungerBar, energyBar, hygieneBar, accountantImage);

                engine.setAge();

            }
        });

        energyIcon.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Animation shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
                findViewById(R.id.accountantImage).startAnimation(shake);

                if ((energyValue+20)<=100){
                    energyValue += 20;
                    energyBar.setProgress(energyValue);
                }
                else {
                    energyValue = 100;
                    energyBar.setProgress(energyValue);
                }
                GameEngine engine = new GameEngine(ageCounter, happyValue, hungerValue, energyValue,
                        hygieneValue, happyBar, hungerBar, energyBar, hygieneBar, accountantImage);

                engine.setAge();

            }
        });
    }

    @Override
    protected void onPause(){
        super.onPause();
        //Any time the app looses focus it opens the SharedPreferences file and stores the
        //values to the appropriate key then saves the file
        SharedPreferences settings = getApplicationContext().getSharedPreferences("values", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("happy", happyValue);
        editor.putInt("hunger", hungerValue);
        editor.putInt("hygiene", hygieneValue);
        editor.putInt("energy", energyValue);
        editor.putInt("age", ageCounter);

        editor.apply();
    }

    @Override
    protected void onResume(){
        super.onResume();
        //When the app resumes focus it assigns the key values to the global variables.
        //If the values do not exist in the file it defaults to 100 or 0
        SharedPreferences values = getApplicationContext().getSharedPreferences("values", 0);
        happyValue = values.getInt("happy", 100);
        hungerValue = values.getInt("hunger", 100);
        hygieneValue = values.getInt("hygiene", 100);
        energyValue = values.getInt("energy", 100);
        ageCounter = values.getInt("age", 0);

    }

    protected float dpToPx(){
        Resources r = getResources();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 300, r.getDisplayMetrics());
    }
    /*

    Popup Menu code

    //https://readyandroid.wordpress.com/popup-menu-with-icon/
    int showPopupWindow(View view, ProgressBar hungryBar){
        PopupMenu popup = new PopupMenu(this, view);
        try {
            Field[] fields = popup.getClass().getDeclaredFields();
            for (Field field : fields) {
                if ("mPopup".equals(field.getName())) {
                    field.setAccessible(true);
                    Object menuPopupHelper = field.get(popup);
                    Class<?> classPopupHelper = Class.forName(menuPopupHelper.getClass().getName());
                    Method setForceIcons = classPopupHelper.getMethod("setForceShowIcon", boolean.class);
                    setForceIcons.invoke(menuPopupHelper, true);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                //setHungerMenuChoice(item.getTitle().toString());
                //Toast.makeText(getApplicationContext(), "You Clicked : " + item.getTitle(),  Toast.LENGTH_SHORT).show();


                return true;
            }
        });
        popup.show();
        return 0;
    }
    */
}
