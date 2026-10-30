package com.gathin.tvhome;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;

import java.io.File;

/** The profile_pill photo: its centre square, scaled down and cut to a circle. */
final class ProfilePhoto {
    private ProfilePhoto() {}

    /** A size x size circle of the photo's centre square; null when there is no file or it does not decode. */
    static Bitmap circle(File file, int size) {
        if (file == null || size <= 0) return null;
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(file.getPath(), options);
            int side = Math.min(options.outWidth, options.outHeight);
            if (side <= 0) return null;
            options.inJustDecodeBounds = false;
            options.inSampleSize = sampleSize(side, size);
            Bitmap source = BitmapFactory.decodeFile(file.getPath(), options);
            if (source == null) return null;
            int w = source.getWidth(), h = source.getHeight(), s = Math.min(w, h);
            Bitmap out = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(out);
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
            canvas.drawCircle(size / 2f, size / 2f, size / 2f, paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(source, new Rect((w - s) / 2, (h - s) / 2, (w + s) / 2, (h + s) / 2),
                    new Rect(0, 0, size, size), paint);
            source.recycle();
            return out;
        } catch (Exception | OutOfMemoryError failure) {
            return null;
        }
    }

    /** The largest power-of-two sample that keeps the photo's short side at least size pixels. */
    static int sampleSize(int side, int size) {
        int sample = 1;
        while (side / (sample * 2) >= size) sample *= 2;
        return sample;
    }
}
