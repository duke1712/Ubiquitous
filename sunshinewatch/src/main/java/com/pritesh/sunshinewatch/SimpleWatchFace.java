package com.pritesh.sunshinewatch;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.text.format.Time;
import android.util.Log;

/**
 * Created by prittesh on 9/12/16.
 */

public class SimpleWatchFace {

    private static final String TIME_FORMAT_WITHOUT_SECONDS = "%02d.%02d";
    private static final String TIME_FORMAT_WITH_SECONDS = TIME_FORMAT_WITHOUT_SECONDS + ".%02d";
    private static final String DATE_FORMAT = "%02d.%02d.%d";

    private final Paint timePaint;
    private final Paint datePaint;
    private final Time time;
    private static Context context;
    private boolean shouldShowSeconds = true;

    public static SimpleWatchFace newInstance(Context context1) {
        Paint timePaint = new Paint();
        timePaint.setColor(Color.WHITE);
        timePaint.setTextSize(context1.getResources().getDimension(R.dimen.time_size));
        timePaint.setAntiAlias(true);
        context=context1;
        Paint datePaint = new Paint();
        datePaint.setColor(Color.rgb(238,238,238));
        datePaint.setTextSize(context.getResources().getDimension(R.dimen.date_size));
        datePaint.setAntiAlias(true);

        return new SimpleWatchFace(timePaint, datePaint, new Time());
    }

    SimpleWatchFace(Paint timePaint, Paint datePaint, Time time) {
        this.timePaint = timePaint;
        this.datePaint = datePaint;
        this.time = time;
    }

    public void draw(Canvas canvas, Rect bounds, double high, double low, String id) {
        time.setToNow();
        canvas.drawColor(Color.rgb(33,150,243));

       // String timeText = String.format(shouldShowSeconds ? TIME_FORMAT_WITH_SECONDS : TIME_FORMAT_WITHOUT_SECONDS, time.hour, time.minute, time.second);
        String timeText = String.format(TIME_FORMAT_WITHOUT_SECONDS, time.hour, time.minute);

        float timeXOffset = computeXOffset(timeText, timePaint, bounds);
        float timeYOffset = computeTimeYOffset(timeText, timePaint, bounds);
        canvas.drawText(timeText, timeXOffset, timeYOffset, timePaint);
//        String graphic;
//        if (id >= 200 && id <= 232) {
//            graphic= "storm";
//        } else if (id >= 300 && id <= 321) {
//            graphic= "light_rain";
//        } else if (id >= 500 && id <= 504) {
//            graphic= "rain";
//        } else if (id == 511) {
//            graphic= "snow";
//        } else if (id >= 520 && id <= 531) {
//            graphic= "rain";
//        } else if (id >= 600 && id <= 622) {
//            graphic= "snow";
//        } else if (id >= 701 && id <= 761) {
//            graphic= "fog";
//        } else if (id == 761 || id == 781) {
//            graphic= "storm";
//        } else if (id == 800) {
//            graphic= "clear";
//        } else if (id == 801) {
//            graphic= "light_clouds";
//        } else if (id >= 802 && id <= 804) {
//            graphic= "cloudy";
        //}

        String dateText = String.format(DATE_FORMAT, time.monthDay, (time.month + 1), time.year);
        float dateXOffset = computeXOffset(dateText, datePaint, bounds);
        float dateYOffset = computeDateYOffset(dateText, datePaint);
        canvas.drawText(dateText, dateXOffset, timeYOffset + dateYOffset, datePaint);
        String text=high+"|"+low;
        if (id!=null) {
            try {
                Bitmap artBitmap = null;
                switch (id) {
                    case "storm":
                        artBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.art_storm);
                        break;
                    case "light_rain":
                        artBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.art_light_rain);
                        break;
                    case "rain":
                        artBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.art_rain);
                        break;
                    case "snow":
                        artBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.art_snow);
                        break;
                    case "fog":
                        artBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.art_fog);
                        break;
                    case "clear":
                        artBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.art_clear);
                        break;
                    case "light_clouds":
                        artBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.art_light_clouds);
                        break;
                    case "clouds":
                        artBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.art_clouds);
                        break;
                }
                Log.d("Before creating mapcon", String.valueOf(context));
                artBitmap = Bitmap.createScaledBitmap(artBitmap, 70, 55, true);
                canvas.drawBitmap(artBitmap, timeXOffset,
                        timeYOffset+dateYOffset+55+ context.getResources().getDimension(R.dimen.extra_y_offset_icon), new Paint());
                Log.d("Bitmap", "Should be created");
            } catch (NullPointerException npe) {
                Log.e("Watchface ArtId", id);
            }
            canvas.drawText(text,computeXOffset(text, timePaint,bounds),(timeYOffset + dateYOffset)+computeDateYOffset(text, timePaint),timePaint);
    }
    }

    private float computeXOffset(String text, Paint paint, Rect watchBounds) {
        float centerX = watchBounds.exactCenterX();
        float timeLength = paint.measureText(text);
        return centerX - (timeLength / 2.0f);
    }

    private float computeTimeYOffset(String timeText, Paint timePaint, Rect watchBounds) {
        float centerY = watchBounds.exactCenterY()/2;
        Rect textBounds = new Rect();
        timePaint.getTextBounds(timeText, 0, timeText.length(), textBounds);
        int textHeight = textBounds.height();
        return centerY + (textHeight / 2.0f);
    }

    private float computeDateYOffset(String dateText, Paint datePaint) {
        Rect textBounds = new Rect();
        datePaint.getTextBounds(dateText, 0, dateText.length(), textBounds);
        return textBounds.height() + 10.0f;
    }

    public void setAntiAlias(boolean antiAlias) {
        timePaint.setAntiAlias(antiAlias);
        datePaint.setAntiAlias(antiAlias);

    }

    public void setColor(int color) {
        timePaint.setColor(color);
        datePaint.setColor(color);
    }

    public void setShowSeconds(boolean showSeconds) {
        shouldShowSeconds = showSeconds;
    }

}
