package com.asus.escortBot;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.asus.robotframework.API.*;
import com.asus.robotframework.API.results.RoomInfo;
import com.robot.asus.robotactivity.RobotActivity;

import org.json.JSONObject;

import java.util.ArrayList;


public class MainActivity extends RobotActivity {
    public final static String TAG = "escortBot";
    public final static String DOMAIN = "2823932198314769A8B19ADE57B38153";

    // GUI components
    // Main Layout
    private static ConstraintLayout mainLayout;
    // Welcome layout
    private static ConstraintLayout welcomeLayout;
    private static ImageView giveIntroBtn;
    private static ImageView escortMeBtn;
    private static ImageView leaveMeBtn;
    // Event intro layout
    private static ConstraintLayout eventIntroLayout;
    private static Button eventIntroRepeatBtn;
    private static Button eventIntroNextBtn;
    // Escort layout
    private static ConstraintLayout escortLayout;
    private static ImageView mobileBtn;
    private static ImageView desktopBtn;
    private static ImageView crossBtn;
    // WantKnowPromo layout
    private static ConstraintLayout wantKnowPromoLayout;
    private static ImageView wantKnowPromoYesBtn;
    private static ImageView wantKnowPromoNoBtn;
    // Promo layout
    private static ConstraintLayout promoMobileLayout;
    private static Button promoMobileBtnRepeat;
    private static Button  promoMobileBtnDesktop;
    private static Button  promoMobileBtnNext;
    private static ConstraintLayout promoDesktopLayout;
    private static Button  promoDesktopBtnRepeat;
    private static Button  promoDesktopBtnMobile;
    private static Button  promoDesktopBtnNext;

    // Navigation
    // request code for READ_CONTACTS. It can be any number > 0.
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    static String section = "";
    static ArrayList<RoomInfo> arrayListRooms;

    // Used to control robot current state
    static boolean greet = false;
    static boolean introOrEscort = false;
    static boolean afterFirstIntro = false;
    static boolean escortAndAskPromo = false;
    static boolean explainPromo = false;
    static boolean afterPromoShown = false;
    static boolean mobile = false;
    static boolean desktop = false;
    static boolean escorted = false;

    // Other essential declarations
    static SpeakConfig speakConfig = new SpeakConfig();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainLayout = findViewById(R.id.MainLayout);
        welcomeLayout = findViewById(R.id.WelcomeLayout);
        giveIntroBtn = findViewById(R.id.img_intro);
        escortMeBtn = findViewById(R.id.img_escort);
        leaveMeBtn = findViewById(R.id.img_leaveMe);
        eventIntroLayout = findViewById(R.id.EventIntroLayout);
        eventIntroRepeatBtn= findViewById(R.id.eventIntro_btn1);
        eventIntroNextBtn = findViewById(R.id.eventIntro_btn2);
        escortLayout = findViewById(R.id.EscortLayout);
        mobileBtn = findViewById(R.id.img_mobile);
        desktopBtn = findViewById(R.id.img_desktop);
        crossBtn = findViewById(R.id.img_cross);
        wantKnowPromoLayout = findViewById(R.id.wantKnowPromoLayout);
        wantKnowPromoYesBtn = findViewById(R.id.img_yes);
        wantKnowPromoNoBtn = findViewById(R.id.img_no);
        promoMobileLayout = findViewById(R.id.promoMobileLayout);
        promoMobileBtnRepeat = findViewById(R.id.promoMobileBtn1);
        promoMobileBtnDesktop = findViewById(R.id.promoMobileBtn2);
        promoMobileBtnNext = findViewById(R.id.promoMobileBtn3);
        promoDesktopLayout = findViewById(R.id.promoDesktopLayout);
        promoDesktopBtnRepeat = findViewById(R.id.promoDesktopBtn1);
        promoDesktopBtnMobile = findViewById(R.id.promoDesktopBtn2);
        promoDesktopBtnNext = findViewById(R.id.promoDesktopBtn3);


        giveIntroBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                robotAPI.robot.stopSpeakAndListen();
                welcomeLayout.setVisibility(View.INVISIBLE);
                eventIntroLayout.setVisibility(View.VISIBLE);
                robotAPI.robot.speakAndListen(" This is an annual IT fair held by the Faculty of Information " +
                        "Science of Technology of UKM. This fair is divided into two sections, namely mobile " +
                        "and desktop. Products such as headphones, mouse and keyboard are also sold here.", speakConfig.timeout(4));
            }
        });

        escortMeBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                robotAPI.robot.stopSpeakAndListen();
                introOrEscort = false;
                welcomeLayout.setVisibility(View.INVISIBLE);
                escortLayout.setVisibility(View.VISIBLE);
                robotAPI.robot.speakAndListen("May I know which section would you like to go to?", speakConfig.timeout(4));
                escortAndAskPromo = true;
            }
        });

        leaveMeBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                robotAPI.robot.stopSpeakAndListen();
                introOrEscort = false;
                welcomeLayout.setVisibility(View.INVISIBLE);
                mainLayout.setVisibility(View.INVISIBLE);
                robotAPI.robot.setExpression(RobotFace.DEFAULT,"OK, hope you enjoy the fair.");
                greet = true;
            }
        });

        eventIntroRepeatBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                robotAPI.robot.stopSpeakAndListen();
                welcomeLayout.setVisibility(View.INVISIBLE);
                eventIntroLayout.setVisibility(View.VISIBLE);
                robotAPI.robot.speakAndListen(" This is an annual IT fair held by the Faculty of Information " +
                        "Science of Technology of UKM. This fair is divided into two sections, namely mobile " +
                        "and desktop. Products such as headphones, mouse and keyboard are also sold here.", speakConfig.timeout(4));
            }
        });

        eventIntroNextBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                robotAPI.robot.stopSpeakAndListen();
                introOrEscort = false;
                eventIntroLayout.setVisibility(View.INVISIBLE);
                escortLayout.setVisibility(View.VISIBLE);
                robotAPI.robot.speakAndListen("May I know which section would you like to go to?", speakConfig.timeout(4));
                escortAndAskPromo = true;
            }
        });

        mobileBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                robotAPI.robot.stopSpeakAndListen();
                escortAndAskPromo = false;
                mobile = true;
                escortLayout.setVisibility(View.INVISIBLE);
                mainLayout.setVisibility(View.INVISIBLE);
                robotAPI.robot.setExpression(RobotFace.CONFIDENT,"Mobile section? Got it, follow me.");
                //requestPermissionForNavigation();
                arrayListRooms = robotAPI.contacts.room.getAllRoomInfo();
                Log.d("ZenboGoToLocation", "arrayListRooms = " + arrayListRooms);
                section = arrayListRooms.get(1).keyword;
                robotAPI.robot.setExpression(RobotFace.SINGING);
                robotAPI.motion.goTo(section);
                mainLayout.setVisibility(View.VISIBLE);
                wantKnowPromoLayout.setVisibility(View.VISIBLE);
                robotAPI.robot.speakAndListen("OK. We are here. Before I go, would you like to know" +
                        "about the promotions held in this section?", speakConfig.timeout(4));
                escorted = true;
                explainPromo = true;
            }
        });

        desktopBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                robotAPI.robot.stopSpeakAndListen();
                escortAndAskPromo = false;
                desktop = true;
                escortLayout.setVisibility(View.INVISIBLE);
                mainLayout.setVisibility(View.INVISIBLE);
                robotAPI.robot.setExpression(RobotFace.CONFIDENT,"The desktop section? Got it, follow me.");
                //requestPermissionForNavigation();
                arrayListRooms = robotAPI.contacts.room.getAllRoomInfo();
                Log.d("ZenboGoToLocation", "arrayListRooms = " + arrayListRooms);
                section = arrayListRooms.get(2).keyword;
                robotAPI.robot.setExpression(RobotFace.SINGING);
                robotAPI.motion.goTo(section);
                mainLayout.setVisibility(View.VISIBLE);
                wantKnowPromoLayout.setVisibility(View.VISIBLE);
                robotAPI.robot.speakAndListen("OK. We are here. Before I go, would you like to know" +
                        "about the promotions held in this section?", speakConfig.timeout(4));
                escorted = true;
                explainPromo = true;
            }
        });

        crossBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                robotAPI.robot.stopSpeakAndListen();
                escortAndAskPromo = false;
                escortLayout.setVisibility(View.INVISIBLE);
                wantKnowPromoLayout.setVisibility(View.VISIBLE);
                robotAPI.robot.speakAndListen("OK. Would you like to know the promotion details for this " +
                        "event?",speakConfig.timeout(4));
                mobile = true;
                explainPromo = true;
            }
        });

        wantKnowPromoYesBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                robotAPI.robot.stopSpeakAndListen();
                wantKnowPromoLayout.setVisibility(View.INVISIBLE);
                if (mobile) {
                    promoMobileLayout.setVisibility(View.VISIBLE);
                    robotAPI.robot.speakAndListen("Currently, all Samsung and Huawei smartphones have discounts " +
                            "up to fifty percent. Headphones and chargers of all brands are also on sale.", speakConfig.timeout(4));
                }
                else if (desktop) {
                    promoDesktopLayout.setVisibility(View.VISIBLE);
                    robotAPI.robot.speakAndListen("Currently, customers can enjoy up to thirty percent discounts for laptops " +
                            "of all brands. Other products such as mouse and keyboards are also enjoying discounts.", speakConfig.timeout(4));
                }
            }
        } );

        wantKnowPromoNoBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                robotAPI.robot.stopSpeakAndListen();
                explainPromo = false;
                mobile = false;
                wantKnowPromoLayout.setVisibility(View.INVISIBLE);
                mainLayout.setVisibility(View.INVISIBLE);
                robotAPI.robot.setExpression(RobotFace.PLEASED, "OK, hope you enjoy your time here....Bye");
                if (escorted) {
                    // Navigate back to main entrance and ready to greet next visitor
                    section = arrayListRooms.get(0).keyword;
                    robotAPI.robot.setExpression(RobotFace.SINGING);
                    robotAPI.motion.goTo(section);
                    robotAPI.robot.setExpression(RobotFace.DEFAULT);
                    escorted = false;
                }
                greet = true;
            }
        });

        promoMobileBtnRepeat.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                robotAPI.robot.stopSpeakAndListen();
                robotAPI.robot.speakAndListen("Currently, all Samsung and Huawei smartphones have discounts " +
                        "up to fifty percent. Headphones and chargers of all brands are also on sale.", speakConfig.timeout(4));
            }
        });

        promoMobileBtnDesktop.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                robotAPI.robot.stopSpeakAndListen();
                mobile = false;
                desktop = true;
                robotAPI.robot.speak("Switching to desktop promotion details.");
                promoMobileLayout.setVisibility(View.INVISIBLE);
                promoDesktopLayout.setVisibility(View.VISIBLE);
                robotAPI.robot.speakAndListen("Currently, customers can enjoy up to thirty percent discounts for laptops " +
                        "of all brands. Other products such as mouse and keyboards are also enjoying discounts.", speakConfig.timeout(4));
            }
        });

        promoMobileBtnNext.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                robotAPI.robot.stopSpeakAndListen();
                explainPromo = false;
                mobile = false;
                promoMobileLayout.setVisibility(View.INVISIBLE);
                mainLayout.setVisibility(View.INVISIBLE);
                robotAPI.robot.setExpression(RobotFace.PLEASED,"That is all. Have fun....Bye");
                if (escorted) {
                    // Navigate back to main entrance and ready to greet next visitor
                    section = arrayListRooms.get(0).keyword;
                    robotAPI.robot.setExpression(RobotFace.SINGING);
                    robotAPI.motion.goTo(section);
                    robotAPI.robot.setExpression(RobotFace.DEFAULT);
                    escorted = false;
                }
                greet = true;
            }
        });

        promoDesktopBtnRepeat.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                robotAPI.robot.stopSpeakAndListen();
                robotAPI.robot.speakAndListen("Currently, customers can enjoy up to thirty percent discounts for laptops " +
                        "of all brands. Other products such as mouse and keyboards are also enjoying discounts.",speakConfig.timeout(4));
            }
        });

        promoDesktopBtnMobile.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                robotAPI.robot.stopSpeakAndListen();
                desktop = false;
                mobile = true;
                robotAPI.robot.speak("Switching to mobile promotion details.");
                promoDesktopLayout.setVisibility(View.INVISIBLE);
                promoMobileLayout.setVisibility(View.VISIBLE);
                robotAPI.robot.speakAndListen("Currently, all Samsung and Huawei smartphones have discounts " +
                        "up to fifty percent. Headphones and chargers of all brands are also on sale.",speakConfig.timeout(4));
            }
        });

        promoDesktopBtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                robotAPI.robot.stopSpeakAndListen();
                explainPromo = false;
                desktop = false;
                promoDesktopLayout.setVisibility(View.INVISIBLE);
                mainLayout.setVisibility(View.INVISIBLE);
                robotAPI.robot.setExpression(RobotFace.PLEASED,"That is all. Have fun....Bye");
                if (escorted) {
                    // Navigate back to main entrance and ready to greet next visitor
                    section = arrayListRooms.get(0).keyword;
                    robotAPI.robot.setExpression(RobotFace.SINGING);
                    robotAPI.motion.goTo(section);
                    robotAPI.robot.setExpression(RobotFace.DEFAULT);
                    escorted = false;
                }
                greet = true;
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();

        // close faical
        robotAPI.robot.setExpression(RobotFace.HIDEFACE);

        robotAPI.robot.setVoiceTrigger(false);

        // jump dialog domain
        robotAPI.robot.jumpToPlan(DOMAIN, "launchEscortBotPlan");

        // listen user utterance
        robotAPI.robot.speakAndListen("Hi there, I am escort bot and I am a helper for this IT fair. " +
                "Would you like to know more about this event or let me escort you?", speakConfig.timeout(4));
        introOrEscort = true;
    }


    public static RobotCallback robotCallback = new RobotCallback() {
        @Override
        public void onResult(int cmd, int serial, RobotErrorCode err_code, Bundle result) {
            super.onResult(cmd, serial, err_code, result);
        }

        @Override
        public void onStateChange(int cmd, int serial, RobotErrorCode err_code, RobotCmdState state) {
            super.onStateChange(cmd, serial, err_code, state);
        }

        @Override
        public void initComplete() {
            super.initComplete();

        }

    };


    public static RobotCallback.Listen robotListenCallback = new RobotCallback.Listen() {

        @Override
        public void onFinishRegister() {

        }

        @Override
        public void onVoiceDetect(JSONObject jsonObject) {

        }

        @Override
        public void onSpeakComplete(String s, String s1) {

        }

        @Override
        public void onEventUserUtterance(JSONObject jsonObject) {
            String text;
            text = "onEventUserUtterance: " + jsonObject.toString();
            Log.d(TAG, text);
        }

        @Override
        public void onResult(JSONObject jsonObject) {
            String text;
            text = "onResult: " + jsonObject.toString();
            Log.d(TAG, text);

            String sIntentionID = RobotUtil.queryListenResultJson(jsonObject, "IntentionId");
            Log.d(TAG, "Intention Id = " + sIntentionID);

            if(sIntentionID.equals("obeyUser")) {
                // Get belief value from user utterance
                String SLUresponse = RobotUtil.queryListenResultJson(jsonObject, "myResponse", null);
                String SLUinitiate = RobotUtil.queryListenResultJson(jsonObject, "myInitiate", null);

                Log.d(TAG, "Result Response = " + SLUresponse);
                Log.d(TAG, "Result Response = " + SLUinitiate);


                if (greet) {
                    if (SLUinitiate.equals("initiate")) {
                        greet = false;
                        robotAPI.robot.setExpression(RobotFace.HIDEFACE);
                        mainLayout.setVisibility(View.VISIBLE);
                        welcomeLayout.setVisibility(View.VISIBLE);
                        robotAPI.robot.speakAndListen("Hi there, I am escort bot and I am a helper for this IT fair. " +
                                "Would you like to know more about this event or let me escort you?", speakConfig.timeout(4));
                        introOrEscort = true;
                    }
                }

                else if (introOrEscort) {
                    if (afterFirstIntro) {
                        if (SLUresponse.equals("repeat")) {
                            robotAPI.robot.speakAndListen(" This is an annual IT fair held by the Faculty of Information " +
                                    "Science of Technology of UKM. This fair is divided into two sections, namely mobile " +
                                    "and desktop. Products such as headphones, mouse and keyboard are also sold here.", speakConfig.timeout(4));
                        }
                        else if (SLUresponse.equals("next") || SLUresponse.equals("escort")) {
                            introOrEscort = false;
                            afterFirstIntro = false;
                            eventIntroLayout.setVisibility(View.INVISIBLE);
                            escortLayout.setVisibility(View.VISIBLE);
                            robotAPI.robot.speakAndListen("May I know which section would you like to go to?", speakConfig.timeout(4));
                            escortAndAskPromo = true;
                        }
                    }
                    else {
                        if (SLUresponse.equals("intro")) {
                            welcomeLayout.setVisibility(View.INVISIBLE);
                            eventIntroLayout.setVisibility(View.VISIBLE);
                            robotAPI.robot.speakAndListen(" This is an annual IT fair held by the Faculty of Information " +
                                    "Science of Technology of UKM. This fair is divided into two sections, namely mobile " +
                                    "and desktop. Products such as headphones, mouse and keyboard are also sold here.", speakConfig.timeout(4));
                            afterFirstIntro = true;
                        }
                        else if (SLUresponse.equals("no")) {
                            introOrEscort = false;
                            welcomeLayout.setVisibility(View.INVISIBLE);
                            mainLayout.setVisibility(View.INVISIBLE);
                            robotAPI.robot.setExpression(RobotFace.DEFAULT, "OK, hope you enjoy the fair.");
                            greet = true;
                        }
                        else if (SLUresponse.equals("escort")) {
                            introOrEscort = false;
                            welcomeLayout.setVisibility(View.INVISIBLE);
                            escortLayout.setVisibility(View.VISIBLE);
                            robotAPI.robot.speakAndListen("Which section would you like to go to?", speakConfig.timeout(4));
                            escortAndAskPromo = true;
                        }
                    }
                }

                else if (escortAndAskPromo) {
                    escortAndAskPromo = false;
                    if (SLUresponse.equals("no")) {
                        escortLayout.setVisibility(View.INVISIBLE);
                        wantKnowPromoLayout.setVisibility(View.VISIBLE);
                        robotAPI.robot.speakAndListen("OK. Would you like to know the promotion details for this event?", speakConfig.timeout(4));
                        mobile = true;
                        explainPromo = true;
                    }
                    else {
                        escortLayout.setVisibility(View.INVISIBLE);
                        mainLayout.setVisibility(View.INVISIBLE);
                        //use robotAPI to get all room info:
                        arrayListRooms = robotAPI.contacts.room.getAllRoomInfo();
                        Log.d("ZenboGoToLocation", "arrayListRooms = " + arrayListRooms);

                        if (SLUresponse.equals("mobile")) {
                            try {
                                mobile = true;
                                section = arrayListRooms.get(1).keyword;
                                Log.d("ZenboGoToLocation", "arrayListRooms(0) = " + section);
                                robotAPI.robot.setExpression(RobotFace.CONFIDENT, "Mobile section? Got it, follow me.");
                                robotAPI.robot.setExpression(RobotFace.SINGING);
                                robotAPI.motion.goTo(section);
                            }
                            catch (Exception e) {
                                Log.d("ZenboGoToLocation", "get room info result exception = " + e);
                            }
                        }
                        else if (SLUresponse.equals("desktop")) {
                            try {
                                desktop = true;
                                section = arrayListRooms.get(2).keyword;
                                Log.d("ZenboGoToLocation", "arrayListRooms(1) = " + section);
                                robotAPI.robot.setExpression(RobotFace.CONFIDENT, "The desktop section? Got it, follow me.");
                                robotAPI.robot.setExpression(RobotFace.SINGING);
                                robotAPI.motion.goTo(section);
                            }
                            catch (Exception e) {
                                Log.d("ZenboGoToLocation", "get room info result exception = " + e);
                            }
                        }
                        // After arrived, ask user want to know promo or not
                        mainLayout.setVisibility(View.VISIBLE);
                        wantKnowPromoLayout.setVisibility(View.VISIBLE);
                        robotAPI.robot.speakAndListen("OK. We are here. Before I go, would you like to know" +
                                "about the promotional details of this section?", speakConfig.timeout(4));
                        escorted = true;
                        explainPromo = true;
                    }
                }

                else if (explainPromo) {
                    if (afterPromoShown) {
                        if (SLUresponse.equals("repeat")) {
                            if (mobile) {
                                robotAPI.robot.speakAndListen("Currently, all Samsung and Huawei smartphones have discounts " +
                                        "up to fifty percent. Headphones and chargers of all brands are also on sale.", speakConfig.timeout(4));
                            }
                            else if (desktop) {
                                robotAPI.robot.speakAndListen("Currently, customers can enjoy up to thirty percent discounts for laptops " +
                                        "of all brands. Other products such as mouse and keyboards are also enjoying discounts.", speakConfig.timeout(4));
                            }
                        }
                        else if (SLUresponse.equals("next") || SLUresponse.equals("finish")) {
                            explainPromo = false;
                            afterPromoShown = false;
                            mobile = false;
                            desktop = false;
                            promoMobileLayout.setVisibility(View.INVISIBLE);
                            mainLayout.setVisibility(View.INVISIBLE);
                            robotAPI.robot.setExpression(RobotFace.PLEASED, "That is all. Have fun....Bye");
                            if (escorted) {
                                // Navigate back to main entrance and ready to greet next visitor
                                section = arrayListRooms.get(0).keyword;
                                robotAPI.robot.setExpression(RobotFace.SINGING);
                                robotAPI.motion.goTo(section);
                                robotAPI.robot.setExpression(RobotFace.DEFAULT);
                                escorted = false;
                            }
                            greet = true;
                        }
                        else if (SLUresponse.equals("mobile")) {
                            if (desktop) {
                                desktop = false;
                                mobile = true;
                                robotAPI.robot.speakAndListen("Switching to mobile promotion details. Currently, all Samsung " +
                                        "and Huawei smartphones have discounts up to fifty percent. Headphones and chargers of all " +
                                        "brands are also on sale.", speakConfig.timeout(4));
                                promoDesktopLayout.setVisibility((View.INVISIBLE));
                                promoMobileLayout.setVisibility(View.VISIBLE);
                            }
                        }
                        else if (SLUresponse.equals("desktop")) {
                            if (mobile) {
                                mobile = false;
                                desktop = true;
                                robotAPI.robot.speak("Switching to desktop promotion details. Currently, customers can enjoy " +
                                        "up to thirty percent discounts for laptops of all brands. Other products such as mouse and " +
                                        "keyboards are also enjoying discounts.", speakConfig.timeout(4));
                                promoMobileLayout.setVisibility((View.INVISIBLE));
                                promoDesktopLayout.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                    else {
                        if (SLUresponse.equals("yes")) {
                            if (mobile) {
                                // Explain mobile promo
                                wantKnowPromoLayout.setVisibility(View.INVISIBLE);
                                promoMobileLayout.setVisibility(View.VISIBLE);
                                robotAPI.robot.speakAndListen("Currently, all Samsung and Huawei smartphones have discounts " +
                                        "up to fifty percent. Headphones and chargers of all brands are also on sale.", speakConfig.timeout(4));
                            }
                            else if (desktop) {
                                // Explain desktop promo
                                wantKnowPromoLayout.setVisibility(View.INVISIBLE);
                                promoDesktopLayout.setVisibility(View.VISIBLE);
                                robotAPI.robot.speakAndListen("Currently, customers can enjoy up to thirty percent discounts for laptops " +
                                        "of all brands. Other products such as mouse and keyboards are also enjoying discounts.", speakConfig.timeout(4));
                            }
                            afterPromoShown = true;
                        }
                        else if (SLUresponse.equals("no")) {
                            explainPromo = false;
                            mobile = false;
                            desktop = false;
                            promoMobileLayout.setVisibility(View.INVISIBLE);
                            mainLayout.setVisibility(View.INVISIBLE);
                            robotAPI.robot.setExpression(RobotFace.PLEASED, "OK, hope you enjoy your time here....Bye");
                            if (escorted) {
                                // Navigate back to main entrance and ready to greet next visitor
                                section = arrayListRooms.get(0).keyword;
                                robotAPI.robot.setExpression(RobotFace.SINGING);
                                robotAPI.motion.goTo(section);
                                robotAPI.robot.setExpression(RobotFace.DEFAULT);
                                escorted = false;
                            }
                            greet = true;
                        }
                    }
                }
            }
        }


        @Override
        public void onRetry(JSONObject jsonObject) {

        }
    };


    // Used to request permission to access mapped location info for navigation
    private void requestPermissionForNavigation() {
        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
                this.checkSelfPermission(Manifest.permission.READ_CONTACTS) ==
                        PackageManager.PERMISSION_GRANTED) {
            // Android version is lesser than 6.0 or the permission is already granted.
            Log.d("ZenboGoToLocation", "permission is already granted");
            return;
        }

        if (shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS)) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS},
                    PERMISSIONS_REQUEST_READ_CONTACTS);
        } else {
            //showMessageOKCancel("You need to allow access to Contacts",
            //        new DialogInterface.OnClickListener() {
            //            @Override
            //            public void onClick(DialogInterface dialog, int which) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS},
                    PERMISSIONS_REQUEST_READ_CONTACTS);
            //            }
            //        });
        }
    }


    public MainActivity() {
        super(robotCallback, robotListenCallback);
    }
}
