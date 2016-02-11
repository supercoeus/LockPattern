package com.andriyantonov.lockpatternt.lock.view;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Vibrator;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.andriyantonov.lockpatternt.R;

import java.util.ArrayList;

/**
 * Created by pro100svitlo on 1/24/16.
 */
public class LockPatternView extends RelativeLayout{

    public final int SET_PATTERN = 100;
    public final int CONFIRM_PATTERN = 200;
    public final int CHECK_PATTERN = 300;

    private Context mContext;
    private LockPatternView mLPV;
    private MainPatternView mMainPatternView;
    private StatusTextView mStatusTitle;
    private ForgotPassTextView mForgotPassTitle;
    private LPV_Interface mInterface;
    private LPV_SharedPreferences mSharedPreferences;
    private BottomButtonsLayout mBottomButtonsLayout;
    private RelativeLayout mLogoView;
    private Button mBtnCancel;
    private Button mBtnConfirm;
    private Canvas mPatternCanvas;
    private Drawable mBgNormal;
    private Drawable mBgError;
    private Drawable mDrawableLogo;
    private Paint mDotLinePaint;
    private Paint mBgLinePaint;
    private Paint mPatternPaint;
    private Path mPatternPath;
    private Vibrator mVibrator;
    private Bitmap mPatternBitmap;
    private Bitmap mDotBitmapNormal;
    private Bitmap mDotBitmapTouched;
    private Bitmap mDotBitmapError;
    private ArrayList<ImageView> mAllDots = new ArrayList<>();
    private ArrayList<ImageView> mDotsTouched = new ArrayList<>();
    private int mButtonsViewId = -111;
    private int mForgetPassViewId = -222;
    private int mMainPatternViewId = -333;
    private int mStatusViewId = -444;
    private int mLogoViewId = -555;
    private int mButtonTextColor = R.color.textColor;
    private int mStatusColorNormal = R.color.lpv_white_100;
    private int mStatusColorError = R.color.lpv_white_100;
    private int mForgotPassColor = R.color.lpv_white_100;
    private int mDotColorNormal = R.color.lpv_white_100;
    private int mDotColorTouched = R.color.lpv_green;
    private int mDotColorError = R.color.lpv_red;
    private int mBgColorNormal = R.color.lpv_white_100;
    private int mBgColorError = R.color.lpv_red;
    private int mDotAnimationDuration = 100;
    private int mDotCountMin = 4;
    private int mDotRadius = 20;
    private int mDotLineWidth = 10;
    private int mBgRadius = 20;
    private int mBgLineWidth = 5;
    private int mDotVibrateDuration = 10;
    private int mErrorVibrateDuration = 200;
    private int mErrorTimeOut = 2000;
    private int mHorizontalDotsCount = 3;
    private int mVerticalDotsCount = 3;
    private Drawable mButtonBgResource;
    private int mCurrentLockStatus;
    private int mStatusBarHeight;
    private int mScreenWidth;
    private int mScreenHeight;
    private int mLastPatternElement;
    private int mItemStartPosition;
    private int mMainPatternViewSize;
    private int mMatrixSize;
    private int mDialogMinAnswerLength = 4;
    private int mDialogMaxAnswerLength = 20;
    private int mDialogTitleColor;
    private int mDialogMessageColor;
    private int mDialogTextColor;
    private int mDialogButtonsColor;
    private int mSecondPassRadioBtnColor;
    private float[] mDotsCoordinatesX;
    private float[] mDotsCoordinatesY;
    private float mDotAnimationScaleMax = 4f;
    private float mDotAnimationScaleNormal = 1f;
    private float mTextSize;
    private float mDotWidth;
    private float mDotHeight;
    private float mDisplayDensity;
    private float mDotTouchLock_prevX;
    private float mDotTouchLock_prevY;
    private float mDotTouchLock_currentY;
    private float mDotTouchLock_currentX;
    private String[] mQuestionsArray;
    private String mPassSetStr;
    private String mPassConfirmStr;
    private String mPatternToShortStr;
    private String mForgotPassFailedStr;
    private String mForgotPassSuccessStr;
    private String mTitleSetNewPatterStr;
    private String mTitlePatterErrorStr;
    private String mTitleConfirmPatternStr;
    private String mForgotPassTitleStr;
    private String mCancelBtnStr;
    private String mConfirmBtnStr;
    private String mRepeatBtnStr;
    private String mDialogSecondPassTitleStr;
    private String mDialogSecondPassMessageStr;
    private String mDialogSecondPassPositiveStr;
    private String mDialogSecondPassNegativeStr;
    private String mPassRestoreTitleStr;
    private String mPassRestoreMessageStr;
    private String mPassRestorePositiveStr;
    private String mPassRestoreNegativeStr;
    private boolean mDotVibrateEnable;
    private boolean mErrorVibrateEnable;
    private boolean mPatternEditEnable;
    private boolean mNeedSetDefItems;
    private boolean mSecretModeEnable;
    private boolean mSecondPassDialogEnable;

    public LockPatternView(Context context) {
        super(context);
        mContext = context;
        initDefaultItems();
    }

    public LockPatternView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initDefaultItems();
        getItemsFromTheme(attrs);
    }

    public LockPatternView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initDefaultItems();
        getItemsFromTheme(attrs);
    }

    public LockPatternView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mContext = context;
        initDefaultItems();
        getItemsFromTheme(attrs);
    }

    private void initDefaultItems(){
        mDisplayDensity = mContext.getResources().getDisplayMetrics().density;

        initDefaultInteger();
        initDefaultColors();
        initDefaultStrings();
        initDefaultBooleans();
    }
    private void initDefaultInteger(){
        mTextSize = mContext.getResources().getDimension(R.dimen.lpv_tv_textSize)/mDisplayDensity;
    }
    private void initDefaultColors(){
        mStatusColorNormal = ContextCompat.getColor(mContext, mStatusColorNormal);
        mStatusColorError = ContextCompat.getColor(mContext, mStatusColorError);
        mForgotPassColor = ContextCompat.getColor(mContext, mForgotPassColor);

        mDotColorNormal = ContextCompat.getColor(mContext, mDotColorNormal);
        mDotColorTouched = ContextCompat.getColor(mContext, mDotColorTouched);
        mDotColorError = ContextCompat.getColor(mContext, mDotColorError);

        mBgColorNormal = ContextCompat.getColor(mContext, mBgColorNormal);
        mBgColorError = ContextCompat.getColor(mContext, mBgColorError);

        mButtonTextColor = ContextCompat.getColor(mContext, mButtonTextColor);
    }
    private void initDefaultStrings(){
        mForgotPassTitleStr = mContext.getString(R.string.lpv_tv_forgotPass_title);
        mForgotPassFailedStr = mContext.getString(R.string.lpv_snack_forgotPassFailed);
        mForgotPassSuccessStr = mContext.getString(R.string.lpv_snack_forgotPassSuccess);
        mPatternToShortStr = mContext.getString(R.string.lpv_tv_shortPattern);

        mTitleSetNewPatterStr = mContext.getString(R.string.lpv_tv_statusTitle_setNewPattern);
        mTitleConfirmPatternStr = mContext.getString(R.string.lpv_tv_statusTitle_confirmPattern);
        mTitlePatterErrorStr = mContext.getString(R.string.lpv_tv_statusTitle_patternError);

        mCancelBtnStr = mContext.getString(R.string.lpv_btn_actionCancel);
        mConfirmBtnStr = mContext.getString(R.string.lpv_btn_actionConfirm);
        mRepeatBtnStr = mContext.getString(R.string.lpv_btn_actionRepeat);
    }
    private void initDefaultBooleans(){
        mDotVibrateEnable = true;
        mErrorVibrateEnable = true;
        mPatternEditEnable = true;
        mSecretModeEnable = false;
        mSecondPassDialogEnable = true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getRawX();
        float y = event.getRawY();
        if (mPatternEditEnable){
            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    mNeedSetDefItems = true;
                    prepareDefaultView(0);
                    mDotTouchLock_currentX = x;
                    mDotTouchLock_currentY = y;
                    checkItemInPosition(x, y);
                    return true;
                case MotionEvent.ACTION_MOVE:
                    checkItemInPosition(x, y);
                    mDotTouchLock_currentX = x;
                    mDotTouchLock_currentY = y;
                    invalidate();
                    return true;
                case MotionEvent.ACTION_UP:
                    checkEnteredPattern();
                    mPatternPath.reset();
                    setItemCoordinatesToDefault();
                    invalidate();
                    return true;
            }
        }
        return false;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh){
            super.onSizeChanged(w, h, oldw, oldh);
        mScreenWidth = w;
        mScreenHeight = h;

        if (w>h){
            ImageView iv = new ImageView(mContext);
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(200, 200);
            iv.setLayoutParams(lp);
            iv.setImageResource(R.drawable.bla);

            Toast t = new Toast(mContext);
            t.setDuration(Toast.LENGTH_SHORT);
            t.setView(iv);
            t.show();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (!isSecretModeEnable() && mDotTouchLock_prevX != 0 && mDotTouchLock_prevY != 0){
            canvas.drawBitmap(mPatternBitmap, 0, 0, mPatternPaint);
            canvas.drawLine(mDotTouchLock_prevX, mDotTouchLock_prevY,
                    mDotTouchLock_currentX, mDotTouchLock_currentY, mDotLinePaint);
        }
    }

    public void initLockPatternView(Activity a, LPV_Interface lpv_interface){
        a.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mInterface = lpv_interface;
        mContext = a;

        onCreateLockPatternView();
    }

    public void resetPatternSuccessful(){
        mCurrentLockStatus = SET_PATTERN;
        mStatusTitle.setText(mTitleSetNewPatterStr);
        mForgotPassTitle.setVisibility(INVISIBLE);
        showSnackBar(mForgotPassSuccessStr);
    }

    public void resetPatternFailed(){
        showSnackBar(mForgotPassFailedStr);
    }

    public void patternSetSuccessful(String secondPass, String question){
        mInterface.patternConfirmedSuccess(true, mPassConfirmStr, secondPass);
        savePass(mPassConfirmStr, secondPass, question);
        setCurrentLockStatus(CHECK_PATTERN);
        mBottomButtonsLayout.setBottomButtonsVisibility(false);
    }

    public void secondPassDismissed(){
        mBottomButtonsLayout.onCancelPatternListener.onClick(mBtnCancel);
    }

    private void getItemsFromTheme(AttributeSet attrs){
        setCustomGeneralStyle(attrs);
        setCustomStatusStyle(attrs);
        setCustomMainPatternStyle(attrs);
        setCustomForgotPassStyle(attrs);
        setCustomButtonsStyle(attrs);
        setCustomDialogStyle(attrs);
        setCustomLogoStyle(attrs);
    }

    private void setCustomGeneralStyle(AttributeSet attrs){
        TypedArray lpv = mContext.getTheme().obtainStyledAttributes(attrs,
                R.styleable.lpv_general, 0, 0);
        try {
            mErrorTimeOut = lpv.getInteger(R.styleable.lpv_general_errorTimeOutDur, mErrorTimeOut);
            mSecretModeEnable = lpv.getBoolean(R.styleable.lpv_general_secretModeEnable, mSecretModeEnable);
            mSecondPassDialogEnable = lpv.getBoolean(R.styleable.lpv_general_secondPassDialogEnable, mSecondPassDialogEnable);
        } finally {
            lpv.recycle();
        }
    }
    private void setCustomButtonsStyle(AttributeSet attrs){
        TypedArray ta = mContext.getTheme().obtainStyledAttributes(attrs,
                R.styleable.lpv_btn, 0, 0);
        try {
            mButtonBgResource = ta.getDrawable(R.styleable.lpv_btn_btnBgResource);
            mButtonTextColor = ta.getInteger(R.styleable.lpv_btn_btnTextColor, mButtonTextColor);

            String cancelStr = ta.getString(R.styleable.lpv_btn_btnCancelStr);
            String confirmStr = ta.getString(R.styleable.lpv_btn_btnConfirmStr);
            String repeatStr = ta.getString(R.styleable.lpv_btn_btnRepeatStr);
            mCancelBtnStr = cancelStr != null ? cancelStr : mCancelBtnStr;
            mConfirmBtnStr = confirmStr != null ? confirmStr : mConfirmBtnStr;
            mRepeatBtnStr = repeatStr != null ? repeatStr : mRepeatBtnStr;
        } finally {
            ta.recycle();
        }
    }
    private void setCustomForgotPassStyle(AttributeSet attrs){
        TypedArray ta = mContext.getTheme().obtainStyledAttributes(attrs,
                R.styleable.lpv_forgotPass, 0, 0);
        try {
            mForgotPassColor = ta.getInteger(R.styleable.lpv_forgotPass_forgotPassColor, mForgotPassColor);
            String forgotPass = ta.getString(R.styleable.lpv_forgotPass_forgotPassTitleStr);
            String forgotPassFailed = ta.getString(R.styleable.lpv_forgotPass_forgotPassFailedStr);
            String forgotPassSuccess = ta.getString(R.styleable.lpv_forgotPass_forgotPassSuccessStr);
            mForgotPassTitleStr = forgotPass != null ? forgotPass : mForgotPassTitleStr;
            mForgotPassFailedStr = forgotPassFailed != null ? forgotPassFailed : mForgotPassFailedStr;
            mForgotPassSuccessStr = forgotPassSuccess != null ? forgotPassSuccess : mForgotPassSuccessStr;
        } finally {
            ta.recycle();
        }
    }
    private void setCustomMainPatternStyle(AttributeSet attrs){
        TypedArray ta = mContext.getTheme().obtainStyledAttributes(attrs,
                R.styleable.lpv_main, 0, 0);
        try {
            mDotAnimationScaleMax = ta.getFloat(R.styleable.lpv_main_dotAnimScaleMax, mDotAnimationScaleMax);
            mDotRadius = ta.getInteger(R.styleable.lpv_main_dotRadius, mDotRadius);
            mDotLineWidth = ta.getInteger(R.styleable.lpv_main_dotLineWidth, mDotLineWidth);
            mBgRadius = ta.getInteger(R.styleable.lpv_main_mainBgRadius, mBgRadius);
            mBgLineWidth = ta.getInteger(R.styleable.lpv_main_mainBgLineWidth, mBgLineWidth);
            int minDotsCount = ta.getInteger(R.styleable.lpv_main_dotMinCount, mDotCountMin);
            if (minDotsCount > mDotCountMin){
                mDotCountMin = minDotsCount;
            }
            mDotVibrateDuration = ta.getInteger(R.styleable.lpv_main_dotVibrateDur, mDotVibrateDuration);
            mErrorVibrateDuration = ta.getInteger(R.styleable.lpv_main_errorVibrateDur, mErrorVibrateDuration);

            mDotColorNormal = ta.getInteger(R.styleable.lpv_main_dotNormalColor, mDotColorNormal);
            mDotColorTouched = ta.getInteger(R.styleable.lpv_main_dotTouchedColor, mDotColorTouched);
            mDotColorError = ta.getInteger(R.styleable.lpv_main_dotErrorColor, mDotColorError);
            mBgColorNormal = ta.getInteger(R.styleable.lpv_main_dotErrorColor, mBgColorNormal);
            mBgColorError = ta.getInteger(R.styleable.lpv_main_dotErrorColor, mBgColorError);

            String patternToShortStr = ta.getString(R.styleable.lpv_main_toShortPatternStr);
            if (patternToShortStr != null){
                mPatternToShortStr = patternToShortStr;
            }
            mPatternToShortStr = String.format(mPatternToShortStr, mDotCountMin);

            mDotVibrateEnable = ta.getBoolean(R.styleable.lpv_main_dotVibrateEnable, mDotVibrateEnable);
            mErrorVibrateEnable = ta.getBoolean(R.styleable.lpv_main_errorVibrateEnable, mErrorVibrateEnable);
        } finally {
            ta.recycle();
        }
    }
    private void setCustomStatusStyle(AttributeSet attrs){
        TypedArray ta = mContext.getTheme().obtainStyledAttributes(attrs,
                R.styleable.lpv_status, 0, 0);
        try {
            mStatusColorNormal = ta.getInteger(R.styleable.lpv_status_statusNormalColor, mStatusColorNormal);
            mStatusColorError = ta.getInteger(R.styleable.lpv_status_statusErrorColor, mStatusColorError);

            String titleSetNewPattern = ta.getString(R.styleable.lpv_status_statusSetNewPatternStr);
            String titleConfirmPatternStr = ta.getString(R.styleable.lpv_status_statusConfirmPatternStr);
            String titleErrorPatternStr = ta.getString(R.styleable.lpv_status_statusErrorPatternStr);
            mTitleSetNewPatterStr = titleSetNewPattern != null ? titleSetNewPattern : mTitleSetNewPatterStr;
            mTitleConfirmPatternStr = titleConfirmPatternStr != null ? titleConfirmPatternStr : mTitleConfirmPatternStr;
            mTitlePatterErrorStr = titleErrorPatternStr != null ? titleErrorPatternStr : mTitlePatterErrorStr;
        } finally {
            ta.recycle();
        }
    }
    private void setCustomDialogStyle(AttributeSet attrs){
        TypedArray ta = mContext.getTheme().obtainStyledAttributes(attrs,
                R.styleable.lpv_dialog, 0, 0);
        try {
            //dialog general
            mDialogTitleColor = ta.getInteger(R.styleable.lpv_dialog_dialogTitleColor, 0);
            mDialogMessageColor = ta.getInteger(R.styleable.lpv_dialog_dialogMessageColor, 0);
            mDialogTextColor = ta.getInteger(R.styleable.lpv_dialog_dialogTextColor, 0);
            mDialogButtonsColor = ta.getInteger(R.styleable.lpv_dialog_dialogButtonsColor, 0);
            mDialogMinAnswerLength = ta.getInteger(R.styleable.lpv_dialog_dialogMinAnswerLength, mDialogMinAnswerLength);
            mDialogMaxAnswerLength = ta.getInteger(R.styleable.lpv_dialog_dialogMaxAnswerLength, mDialogMaxAnswerLength);

            //dialog setSecondPass
            mSecondPassRadioBtnColor = ta.getInteger(R.styleable.lpv_dialog_dialogRadioBtnColor, 0);
            mDialogSecondPassTitleStr = ta.getString(R.styleable.lpv_dialog_dialogSecondPassTitleStr);
            mDialogSecondPassMessageStr = ta.getString(R.styleable.lpv_dialog_dialogSecondPassMessageStr);
            mDialogSecondPassPositiveStr = ta.getString(R.styleable.lpv_dialog_dialogSecondPassPositiveStr);
            mDialogSecondPassNegativeStr = ta.getString(R.styleable.lpv_dialog_dialogSecondPassNegativeStr);
            //string-arrays
            CharSequence[] questions = ta.getTextArray(R.styleable.lpv_dialog_dialogQuestionsArray);
            if (questions != null){
                mQuestionsArray = new String[questions.length];
                for (int i = 0; i < questions.length; i++) {
                    mQuestionsArray[i] = questions[i].toString();
                }
            }

            //dialog passRestore
            mPassRestoreTitleStr = ta.getString(R.styleable.lpv_dialog_dialogPassRestoreTitleStr);
            mPassRestoreMessageStr = ta.getString(R.styleable.lpv_dialog_dialogPassRestoreMessageStr);
            mPassRestorePositiveStr = ta.getString(R.styleable.lpv_dialog_dialogPassRestorePositiveStr);
            mPassRestoreNegativeStr = ta.getString(R.styleable.lpv_dialog_dialogPassRestoreNegativeStr);
        } finally {
            ta.recycle();
        }
    }
    private void setCustomLogoStyle(AttributeSet attrs){
        TypedArray ta = mContext.getTheme().obtainStyledAttributes(attrs,
                R.styleable.lpv_logo, 0, 0);
        try {
            //dialog general
            mDrawableLogo = ta.getDrawable(R.styleable.lpv_logo_lpv_logo);
        } finally {
            ta.recycle();
        }
    }

    private void onCreateLockPatternView() {
        initItems();

        createAndAddBottomButtons();
        createForgotPassView();
        createAndAddMainPatternView();
        createAndAddTitleStatusTextView();
        createAndAddLogoView();

        checkSavedPass();
    }

    private void initItems(){
        mLPV = this;

        mVibrator = (Vibrator)mContext.getSystemService(Context.VIBRATOR_SERVICE);

        getStatusBarHeight();

        mCurrentLockStatus = SET_PATTERN;

        mDotLinePaint = new Paint();
        mPatternPath = new Path();
        mDotLinePaint.setColor(mDotColorTouched);
        mDotLinePaint.setAntiAlias(true);
        mDotLinePaint.setStrokeWidth(mDotLineWidth);
        mDotLinePaint.setStyle(Paint.Style.STROKE);
        mDotLinePaint.setStrokeCap(Paint.Cap.ROUND);
        mPatternPaint = new Paint(Paint.DITHER_FLAG);

        mBgLinePaint = new Paint();
        mBgLinePaint.setAntiAlias(true);
        mBgLinePaint.setStrokeWidth(mBgLineWidth);
        mBgLinePaint.setStyle(Paint.Style.STROKE);

        setWillNotDraw(false);
        clearPathBitmap();

        DisplayMetrics dm = new DisplayMetrics();
        ((Activity)getContext()).getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        mMainPatternViewSize = width - width/5;
    }

    private void getStatusBarHeight(){
        mStatusBarHeight = 0;
        int resId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resId > 0){
            mStatusBarHeight = getResources().getDimensionPixelSize(resId);
        }
    }

    private void checkSavedPass(){
        mPassSetStr = getSavedPass();
        boolean patternExist = false;
        if (mPassSetStr.length() >= mDotCountMin){
            mCurrentLockStatus = CHECK_PATTERN;
            patternExist = true;
            mForgotPassTitle.setVisibility(VISIBLE);
        } else {
            mStatusTitle.setText(mTitleSetNewPatterStr);
        }
        sendInterfacePassExist(patternExist);
    }

    private void sendInterfacePassExist(final boolean patternExist) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mInterface != null) {
                    mInterface.patternExist(patternExist);
                } else {
                    sendInterfacePassExist(patternExist);
                }
            }
        }, 50);
    }

    private void createAndAddBottomButtons(){
        mBottomButtonsLayout = new BottomButtonsLayout(mContext);
        addView(mBottomButtonsLayout);
    }
    private void createForgotPassView(){
        mForgotPassTitle = new ForgotPassTextView(mContext);
        if (mSecondPassDialogEnable){
            addView(mForgotPassTitle);
        }
    }
    private void createAndAddMainPatternView(){
        mMainPatternView = new MainPatternView(mContext);
        addView(mMainPatternView);
    }
    private void createAndAddTitleStatusTextView(){
        mStatusTitle = new StatusTextView(mContext);
        addView(mStatusTitle);
    }
    private void createAndAddLogoView(){
        if (mDrawableLogo != null){
            mLogoView = new CustomLogo(mContext);
            addView(mLogoView);
        }
    }

    private void savePass(String confirmPass, String secondPass, String question){
        getLPV_SharedPreferences();
        mSharedPreferences.saveMainPass(confirmPass);
        mSharedPreferences.saveSecondPass(secondPass);
        mSharedPreferences.saveSecondQuestion(question);
        clearPassStrings();
    }

    private boolean isSecretModeEnable(){
        return mCurrentLockStatus == CHECK_PATTERN && mSecretModeEnable;
    }

    private void setCurrentLockStatus(int currentLockStatus){
        mCurrentLockStatus = currentLockStatus;
    }

    private  void clearPassStrings(){
        mPassSetStr = mPassConfirmStr = "";
    }

    private String getSetPass(){
        String pass = "";
        for (int i = 0; i < mDotsTouched.size(); i++) {
            pass += mDotsTouched.get(i).getTag();
        }
        return pass;
    }

    private void clearPathBitmap(){
        if (mScreenWidth == 0 || mScreenHeight == 0){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    clearPathBitmap();
                }
            },20);
        } else {
            mPatternBitmap = Bitmap.createBitmap(mScreenWidth, mScreenHeight, Bitmap.Config.ARGB_8888);
            mPatternCanvas = new Canvas(mPatternBitmap);
        }
    }

    // TODO: 1/25/16   START - onTouchEven action
    private void checkItemInPosition( float x, float y){
        for (int i = 0; i < mAllDots.size(); i++) {
            if (mAllDots.get(i).isEnabled()){
                float currentX_min = mDotsCoordinatesX[i] - mDotWidth /2;
                float currentX_max = mDotsCoordinatesX[i] + mDotWidth /2;
                float currentY_min = mDotsCoordinatesY[i] - mDotHeight /2;
                float currentY_max = mDotsCoordinatesY[i] + mDotHeight /2;

                if (x > currentX_min && x < currentX_max &&
                        y > currentY_min && y < currentY_max){

                    mLastPatternElement = i;
                    int xx = (int) mDotsCoordinatesX[i];
                    int yy = (int) mDotsCoordinatesY[i];

                    if (mDotsTouched.size() == 0){
                        mPatternPath.moveTo(xx, yy - mStatusBarHeight);
                    } else {
                        mPatternPath.lineTo(xx, yy - mStatusBarHeight);
                    }
                    mPatternCanvas.drawPath(mPatternPath, mDotLinePaint);

                    mDotTouchLock_prevX = xx;
                    mDotTouchLock_prevY = yy - mStatusBarHeight;

                    vibrate(mDotVibrateEnable, mDotVibrateDuration);
                    setItemIsActive(i);
                }
            }
        }
    }

    private void setItemIsActive(int position){
        ImageView iv = mAllDots.get(position);
        iv.setEnabled(false);
        if (!isSecretModeEnable()){
            makeItemBig(iv);
            iv.setImageBitmap(mDotBitmapTouched);
        }
        mDotsTouched.add(iv);
    }

    private void makeItemBig(final ImageView v){
        v.animate()
                .scaleX(mDotAnimationScaleMax).scaleY(mDotAnimationScaleMax)
                .setDuration(mDotAnimationDuration)
                .start();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                v.animate()
                        .scaleX(mDotAnimationScaleNormal).scaleY(mDotAnimationScaleNormal)
                        .setDuration(mDotAnimationDuration)
                        .start();
            }
        }, mDotAnimationDuration);
    }

    private void checkEnteredPattern(){
        int size = mDotsTouched.size();
        if (size == 1){
            mNeedSetDefItems = true;
            prepareDefaultView(mErrorTimeOut);
        } else if (size > 1 && size < mDotCountMin){
            patternToShort();
        } else if (size >= mDotCountMin){
            patternIsSet();
        }
    }

    private void showSnackBar(String mess){
        Snackbar.make(mMainPatternView, mess, Snackbar.LENGTH_LONG).show();
    }

    private void prepareDefaultView(int timeOut) {
        for (int i = 0; i < mDotsTouched.size(); i++) {
            ImageView iv = mDotsTouched.get(i);
            iv.setEnabled(true);
        }

        if (mNeedSetDefItems && timeOut == 0){
            setDefaultView();
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mNeedSetDefItems){
                        setDefaultView();
                    }
                }
            }, timeOut);
        }
        mDotsTouched.clear();
    }

    private void setDefaultView(){
        mStatusTitle.setTextColor(mStatusColorNormal);
        mDotLinePaint.setColor(mDotColorTouched);
        mNeedSetDefItems = false;
        for (int i = 0; i < mAllDots.size(); i++) {
            ImageView iv = mAllDots.get(i);
            iv.animate()
                    .scaleX(mDotAnimationScaleNormal).scaleY(mDotAnimationScaleNormal)
                    .setDuration(mDotAnimationDuration)
                    .start();
            iv.setImageBitmap(mDotBitmapNormal);
        }
        mMainPatternView.setBackground(mBgNormal);
        clearPathBitmap();
        setDefaultTitle();
        invalidate();
    }

    private void setDefaultTitle(){
        if (mCurrentLockStatus == SET_PATTERN){
            mStatusTitle.setText(mTitleSetNewPatterStr);
        } else if (mCurrentLockStatus == CONFIRM_PATTERN){
            mStatusTitle.setText(mTitleConfirmPatternStr);
        } else if (mCurrentLockStatus == CHECK_PATTERN){
            mStatusTitle.setText("");
        }
    }

    private void patternToShort(){
        showSnackBar(mPatternToShortStr);
        patternError();
        if (mCurrentLockStatus != CHECK_PATTERN){
            mBtnConfirm.setText(mRepeatBtnStr);
        }
    }

    private void patternError(){
        mStatusTitle.setTextColor(mStatusColorError);
         mStatusTitle.setText(mTitlePatterErrorStr);
        mMainPatternView.setBackground(mBgError);
        vibrate(mErrorVibrateEnable, mErrorVibrateDuration);
        mNeedSetDefItems = true;
        if (!isSecretModeEnable()){
            for (int i = 0; i < mDotsTouched.size(); i++) {
                ImageView iv = mDotsTouched.get(i);
                iv.setImageBitmap(mDotBitmapError);
            }
        }
        prepareDefaultView(mErrorTimeOut);
    }

    private void vibrate(boolean needToVibrate, int duration){
        if (needToVibrate){
            mVibrator.vibrate(duration);
        }
    }

    private void patternIsSet(){
        if (mCurrentLockStatus == SET_PATTERN){
            doIfStatusPatternSet();
        } else if (mCurrentLockStatus == CONFIRM_PATTERN){
            doIfStatusPatternConfirm();
        } else if(mCurrentLockStatus == CHECK_PATTERN){
            doIfStatusPatternCheck();
        }
    }

    private void doIfStatusPatternSet(){
        mPassSetStr = getSetPass();
        mBottomButtonsLayout.setBottomButtonsVisibility(true);
    }

    private void doIfStatusPatternConfirm(){
        mPassConfirmStr = getSetPass();
        if (mPassConfirmStr.equals(mPassSetStr)){
            mBottomButtonsLayout.setBottomButtonsVisibility(true);
        } else {
            mInterface.patternConfirmFailed(mPassConfirmStr);
            patternError();
            mCurrentLockStatus = SET_PATTERN;
            mStatusTitle.setText(mTitleSetNewPatterStr);
            clearPassStrings();
        }
    }

    private void doIfStatusPatternCheck(){
        mPassConfirmStr = getSetPass();
        if (mPassConfirmStr.equals(mPassSetStr)){
            mInterface.patternConfirmedSuccess(false, mPassConfirmStr, "");
        }  else {
            mInterface.patternConfirmFailed(mPassConfirmStr);
            patternError();
        }
        mPassConfirmStr = "";
    }

    private String getSavedPass(){
        getLPV_SharedPreferences();

        return mSharedPreferences.getMainSavedPass();
    }

    private void getLPV_SharedPreferences(){
        if (mSharedPreferences == null){
            mSharedPreferences = new LPV_SharedPreferences(mContext);
        }
    }

    private void setItemCoordinatesToDefault(){
        mDotTouchLock_currentX = mDotsCoordinatesX[mLastPatternElement];
        mDotTouchLock_currentY = mDotsCoordinatesY[mLastPatternElement] - mStatusBarHeight;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mDotTouchLock_prevX = 0;
                mDotTouchLock_prevY = 0;
            }
        }, 100);

        // TODO: 2/2/16 fix this shit
    }

    // TODO: 1/25/16   END - onTouchEvent action

    private class MainPatternView extends LinearLayout {

        private LinearLayout.LayoutParams mPatternRowParams, mPatternItemParams;

        private Paint mItemPaint;
        private Bitmap.Config mItemBitmapConfig;
        private int mPatternItemMargin = 16;
        private int mPatternItemPadding = 30;

        public MainPatternView(Context context) {
            super(context);
            onCreateMainPatternView();
        }

        public MainPatternView(Context context, AttributeSet attrs) {
            super(context, attrs);
            onCreateMainPatternView();
        }

        public MainPatternView(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            onCreateMainPatternView();
        }

        public void setPatternEditEnable(boolean enable){
            mPatternEditEnable = enable;
            for (int i = 0; i < mAllDots.size(); i++) {
                mAllDots.get(i).setEnabled(enable);
            }
        }

        private void onCreateMainPatternView(){
            calculateItemsData();

            setId(mMainPatternViewId);
            setOrientation(LinearLayout.VERTICAL);

            for (int i = 0; i < mHorizontalDotsCount; i++) {
                addView(addNewPatternRow());
            }
            setMainPatternViewLayoutParams();

            createPatternBg();

            calculateDotsCoordinates();
        }

        private void createPatternBg(){
            RectF r = new RectF();
            float div = Math.round(mMainPatternViewSize * 0.5 /100);
            r.set(div, div, mMainPatternViewSize - div, mMainPatternViewSize - div);

            Bitmap normal = Bitmap.createBitmap(mMainPatternViewSize, mMainPatternViewSize, Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(normal);
            mBgLinePaint.setColor(mBgColorNormal);
            c.drawRoundRect(r, mBgRadius, mBgRadius, mBgLinePaint);
            mBgNormal = new BitmapDrawable(mContext.getResources(), normal);

            Bitmap error = Bitmap.createBitmap(mMainPatternViewSize, mMainPatternViewSize, Bitmap.Config.ARGB_8888);
            c = new Canvas(error);
            mBgLinePaint.setColor(mBgColorError);
            c.drawRoundRect(r, mBgRadius, mBgRadius, mBgLinePaint);
            mBgError = new BitmapDrawable(mContext.getResources(), error);

            setBackground(mBgNormal);
        }

        private LinearLayout addNewPatternRow(){
            LinearLayout row = new LinearLayout(mContext);
            row.setOrientation(LinearLayout.HORIZONTAL);
            if (mPatternRowParams == null){
                mPatternRowParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
            }
            mPatternRowParams.weight = 1f;
            row.setLayoutParams(mPatternRowParams);
            for (int i = 0; i < mVerticalDotsCount; i++) {
                row.addView(addPatternNewItem());
            }
            return row;
        }

        private ImageView addPatternNewItem(){
            ImageView item = new ImageView(mContext);
            item.setImageBitmap(mDotBitmapNormal);
            int scale = 2;
            if (mPatternItemParams == null){
                mPatternItemParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                mPatternItemParams.gravity = Gravity.CENTER_VERTICAL;
                mPatternItemMargin = Math.round(mPatternItemMargin * mDisplayDensity);
                mPatternItemParams.setMargins(mPatternItemMargin, mPatternItemMargin, mPatternItemMargin, mPatternItemMargin);
            }

            mPatternItemParams.weight = 1f;

            item.setPadding(mPatternItemPadding, mPatternItemPadding * scale,
                    mPatternItemPadding, mPatternItemPadding * scale);
            item.setLayoutParams(mPatternItemParams);

            mAllDots.add(item);
            item.setTag(String.valueOf(mItemStartPosition));
            mItemStartPosition++;

            return item;
        }

        private void setMainPatternViewLayoutParams(){
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(mMainPatternViewSize, mMainPatternViewSize);
            lp.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
            lp.addRule(ABOVE, mForgetPassViewId);
            setLayoutParams(lp);
        }

        private void calculateItemsData(){
            mItemPaint = new Paint();
            mItemPaint.setAntiAlias(true);
            mItemBitmapConfig = Bitmap.Config.ARGB_8888;
            int size = mDotRadius * 2;
            mDotBitmapNormal = Bitmap.createBitmap(size, size, mItemBitmapConfig);
            mDotBitmapTouched = Bitmap.createBitmap(size, size, mItemBitmapConfig);
            mDotBitmapError = Bitmap.createBitmap(size, size, mItemBitmapConfig);

            drawItemNormal();
            drawItemTouched();
            drawItemError();
            //////////////

            mMatrixSize = mHorizontalDotsCount * mVerticalDotsCount;
            mDotsCoordinatesX = new float[mMatrixSize];
            mDotsCoordinatesY = new float[mMatrixSize];
        }

        private void drawItemNormal(){
            mItemPaint.setColor(mDotColorNormal);
            Canvas canvas = new Canvas(mDotBitmapNormal);
            canvas.drawCircle(mDotRadius, mDotRadius, mDotRadius, mItemPaint);
        }

        private void drawItemTouched(){
            mItemPaint.setColor(mDotColorTouched);
            Canvas canvas = new Canvas(mDotBitmapTouched);
            canvas.drawCircle(mDotRadius, mDotRadius, mDotRadius, mItemPaint);
        }

        private void drawItemError(){
            mItemPaint.setColor(mDotColorError);
            Canvas canvas = new Canvas(mDotBitmapError);
            canvas.drawCircle(mDotRadius, mDotRadius, mDotRadius, mItemPaint);
        }

        private void calculateDotsCoordinates(){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    calculateItemSize();
                    for (int i = 0; i < mMatrixSize; i++) {
                        int[] loc = new int[2];
                        mAllDots.get(i).getLocationInWindow(loc);

                        mDotsCoordinatesX[i] = loc[0] + mDotWidth / 2;
                        mDotsCoordinatesY[i] = loc[1] + mDotHeight / 2;
                    }

                    if (mDotsCoordinatesX[0] == 0) {
                        calculateDotsCoordinates();
                    }
                }
            }, 200);
        }

        private void calculateItemSize(){
            if (mDotWidth == 0 || mDotHeight == 0){
                mDotWidth = mAllDots.get(0).getWidth();
                mDotHeight = mAllDots.get(0).getHeight();
            }
        }
    }

    private class StatusTextView extends TextView {

        public StatusTextView(Context context) {
            super(context);
            onCreateStatusTextView();
        }

        public StatusTextView(Context context, AttributeSet attrs) {
            super(context, attrs);
            onCreateStatusTextView();
        }

        public StatusTextView(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            onCreateStatusTextView();
        }

        private void onCreateStatusTextView(){
            setId(mStatusViewId);
            setGravity(Gravity.CENTER_HORIZONTAL);
            setTextSize(mTextSize);
            setTextColor(mStatusColorNormal);

            setLayoutParams();
        }

        private void setLayoutParams(){
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            int m = (int) mContext.getResources().getDimension(R.dimen.activity_vertical_margin);

            lp.setMargins(m, m/2, m, m/2);
            lp.addRule(ABOVE, mMainPatternViewId);
            lp.addRule(CENTER_HORIZONTAL, RelativeLayout.TRUE);
            setLayoutParams(lp);
        }
    }

    private class ForgotPassTextView extends TextView implements View.OnClickListener {

        private LPV_Dialog mPassResetDialog;

        public ForgotPassTextView(Context context) {
            super(context);
            onCreateForgotPassTextView();
        }

        public ForgotPassTextView(Context context, AttributeSet attrs) {
            super(context, attrs);
            onCreateForgotPassTextView();
        }

        public ForgotPassTextView(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            onCreateForgotPassTextView();
        }

        private void onCreateForgotPassTextView(){
            setId(mForgetPassViewId);
            setGravity(Gravity.CENTER_HORIZONTAL);
            setTextSize(mTextSize);
            setTextColor(mForgotPassColor);
            setText(mForgotPassTitleStr);
            setVisibility(INVISIBLE);
            setOnClickListener(this);

            int[] attrs = new int[]{R.attr.selectableItemBackground};
            TypedArray ta = mContext.obtainStyledAttributes(attrs);
            int bg = ta.getResourceId(0,0);
            setBackgroundResource(bg);
            ta.recycle();

            setLayoutParams();
        }

        private void setLayoutParams(){
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                    LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            int m = (int) mContext.getResources().getDimension(R.dimen.activity_vertical_margin);

            lp.setMargins(m / 2, m / 2, m / 2, 0);
            lp.addRule(CENTER_HORIZONTAL, TRUE);
            lp.addRule(ABOVE, mButtonsViewId);
            setLayoutParams(lp);
            setPadding(m, m, m, 0);
        }

        @Override
        public void onClick(View v) {
            if (mPassResetDialog == null){
                mPassResetDialog = new LPV_Dialog
                        .Builder(mContext, mLPV, mDisplayDensity, LPV_Dialog.DIALOG_RESTORE_PATTERN)
                        .setTitleColor(mDialogTitleColor)
                        .setMessageColor(mDialogMessageColor)
                        .setTextColor(mDialogTextColor)
                        .setButtonsColor(mDialogButtonsColor)
                        .setTitleStr(mPassRestoreTitleStr)
                        .setMessageStr(mPassRestoreMessageStr)
                        .setPositiveStr(mPassRestorePositiveStr)
                        .setNegativeStr(mPassRestoreNegativeStr)
                        .setMinAnswerLength(mDialogMinAnswerLength)
                        .setMaxAnswerLength(mDialogMaxAnswerLength)
                        .build();
            }
            mPassResetDialog.show();
        }
    }

    private class BottomButtonsLayout extends LinearLayout {

        private LPV_Dialog mSecondPassDialog;
        private int mMargin =  16;

        public BottomButtonsLayout(Context context) {
            super(context);
            onCreateBottomButtonsLayout();
        }

        public BottomButtonsLayout(Context context, AttributeSet attrs) {
            super(context, attrs);
            onCreateBottomButtonsLayout();
        }

        public BottomButtonsLayout(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            onCreateBottomButtonsLayout();
        }

        public void setBottomButtonsVisibility(boolean visible){
            if (visible){
                mBtnCancel.setVisibility(VISIBLE);
                mBtnConfirm.setVisibility(VISIBLE);
                mMainPatternView.setPatternEditEnable(false);
            } else {
                mBtnCancel.setVisibility(INVISIBLE);
                mBtnConfirm.setVisibility(INVISIBLE);
                mMainPatternView.setPatternEditEnable(true);
            }
        }

        private void onCreateBottomButtonsLayout(){
            setId(mButtonsViewId);
            setLayoutParams();
            setOrientation(HORIZONTAL);
            addView(addCancelButton());
            addView(addConfirmButton());
        }

        private void setLayoutParams(){
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(mMainPatternViewSize,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            lp.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
            lp.setMargins(mMargin, mMargin, mMargin, mMargin);
            setLayoutParams(lp);
        }

        private Button addCancelButton(){
            mBtnCancel = new Button(mContext);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.weight = 1;
            lp.setMargins(0, 0, mMargin, 0);
            mBtnCancel.setLayoutParams(lp);

            mBtnCancel.setText(mCancelBtnStr);
            mBtnCancel.setTextColor(mButtonTextColor);
            mBtnCancel.setOnClickListener(onCancelPatternListener);
            if (mButtonBgResource != null){
                mBtnCancel.setBackground(mButtonBgResource);
            }
            mBtnCancel.setVisibility(INVISIBLE);
            return mBtnCancel;
        }

        private Button addConfirmButton(){
            mBtnConfirm = new Button(mContext);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.weight = 1;
            lp.setMargins(mMargin, 0, 0, 0);
            mBtnConfirm.setLayoutParams(lp);
            mBtnConfirm.setText(mRepeatBtnStr);
            mBtnConfirm.setTextColor(mButtonTextColor);
            mBtnConfirm.setOnClickListener(onConfirmPatternListener);
            if (mButtonBgResource != null){
                mBtnConfirm.setBackground(mButtonBgResource);
            }
            mBtnConfirm.setVisibility(INVISIBLE);
            return mBtnConfirm;
        }

        private OnClickListener onCancelPatternListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                mInterface.patternSubmitCanceled();
                setCurrentLockStatus(mLPV.SET_PATTERN);
                mStatusTitle.setText(mTitleSetNewPatterStr);
                setBottomButtonsVisibility(false);
                mNeedSetDefItems = true;
                prepareDefaultView(0);
                mBtnConfirm.setText(mRepeatBtnStr);
            }
        };

        private OnClickListener onConfirmPatternListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentLockStatus == SET_PATTERN){
                    setCurrentLockStatus(CONFIRM_PATTERN);
                    mPassSetStr = mLPV.getSetPass();
                    mPassSetStr = mLPV.getSetPass();
                    mInterface.patternSet(mPassSetStr);
                    mBtnConfirm.setText(mConfirmBtnStr);
                    mStatusTitle.setText(mTitleConfirmPatternStr);
                } else if (mCurrentLockStatus == CONFIRM_PATTERN){
                    if (mSecondPassDialogEnable){
                        if (mSecondPassDialog == null){
                            mSecondPassDialog = new LPV_Dialog
                                    .Builder(mContext, mLPV, mDisplayDensity, LPV_Dialog.DIALOG_SET_SECOND_PASS)
                                    .setTitleColor(mDialogTitleColor)
                                    .setMessageColor(mDialogMessageColor)
                                    .setTextColor(mDialogTextColor)
                                    .setButtonsColor(mDialogButtonsColor)
                                    .setRadioButtonsColor(mSecondPassRadioBtnColor)
                                    .setTitleStr(mDialogSecondPassTitleStr)
                                    .setMessageStr(mDialogSecondPassMessageStr)
                                    .setPositiveStr(mDialogSecondPassPositiveStr)
                                    .setNegativeStr(mDialogSecondPassNegativeStr)
                                    .setQuestionsArray(mQuestionsArray)
                                    .setMinAnswerLength(mDialogMinAnswerLength)
                                    .setMaxAnswerLength(mDialogMaxAnswerLength)
                                    .build();
                        }
                        mSecondPassDialog.show();
                    } else {
                        patternSetSuccessful("", "");
                    }
                }
                setBottomButtonsVisibility(false);
                mNeedSetDefItems = true;
                prepareDefaultView(0);
            }
        };

    }

    private class CustomLogo extends RelativeLayout{

        private int mMargin =  16;

        public CustomLogo(Context context) {
            super(context);
            createLogoView();
        }

        public CustomLogo(Context context, AttributeSet attrs) {
            super(context, attrs);
            createLogoView();
        }

        public CustomLogo(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            createLogoView();
        }

        private void createLogoView(){
            setId(mLogoViewId);
            setLogo();
            setLayoutParams();
            invalidate();
        }

        private void setLogo(){
            ImageView logo = new ImageView(mContext);
            logo.setImageDrawable(mDrawableLogo);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            lp.addRule(CENTER_IN_PARENT, RelativeLayout.TRUE);
            logo.setLayoutParams(lp);
            addView(logo);
        }

        private void setLayoutParams(){
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            lp.addRule(ABOVE, mStatusViewId);
            mMargin = Math.round(mMargin * mDisplayDensity);
            lp.setMargins(mMargin, mMargin, mMargin, mMargin);
            setLayoutParams(lp);
        }
    }
}
