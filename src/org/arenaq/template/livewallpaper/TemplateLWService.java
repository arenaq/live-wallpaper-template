package org.arenaq.template.livewallpaper;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.view.SurfaceHolder;

public class TemplateLWService extends WallpaperService {

    @Override
    public Engine onCreateEngine() {
        return new TemplateLWEngine();
    }

    private class TemplateLWEngine extends Engine {
        private boolean mVisible = false;
        private final Handler mHandler = new Handler();
        private final Runnable mUpdateDisplay = new Runnable() {

            @Override
            public void run() {
                draw();
            }
        };

        private void draw() {
            SurfaceHolder holder = getSurfaceHolder();
            Canvas c = null;
            try {
                c = holder.lockCanvas();
                if (c != null) {
                    // sample drawing on canvas
                    /*
                    Paint p = new Paint();
                    p.setTextSize(20);
                    p.setAntiAlias(true);
                    String text = "system time: "+Long.toString(System.currentTimeMillis());
                    float w = p.measureText(text, 0, text.length());
                    int offset = (int) w / 2;
                    int x = c.getWidth()/2 - offset;
                    int y = c.getHeight()/2;
                    p.setColor(Color.BLACK);
                    c.drawRect(0, 0, c.getWidth(), c.getHeight(), p);
                    p.setColor(Color.WHITE);
                    c.drawText(text, x, y, p);
                    */
                }
            } finally {
                if (c != null)
                    holder.unlockCanvasAndPost(c);
            }
            mHandler.removeCallbacks(mUpdateDisplay);
            if (mVisible) {
                mHandler.postDelayed(mUpdateDisplay, 100);
            }
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            super.onVisibilityChanged(visible);
            mVisible = visible;

            if (visible) {
                draw();
            } else {
                mHandler.removeCallbacks(mUpdateDisplay);
            }
        }

        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            super.onSurfaceChanged(holder, format, width, height);
            draw();
        }

        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            super.onSurfaceDestroyed(holder);
            mVisible = false;
            mHandler.removeCallbacks(mUpdateDisplay);
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            mVisible = false;
            mHandler.removeCallbacks(mUpdateDisplay);
        }
    }
}
