/**
 * Copyright (C) 2016 Marco Aurélio Prado dos Santos Vidoca.
 */

package com.example.marcoaurelio.popularmovies;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;

/**
 * Construct the elements of the GridView using the URL of each poster image.
 */
public class PosterImageAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<String> mAllPostersUrl;

    public PosterImageAdapter(Context c, ArrayList<String> allImagesUrl) {
        mContext = c;
        mAllPostersUrl = allImagesUrl;
    }

    @Override
    public int getCount() {
        return mAllPostersUrl.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            LayoutInflater inflater =
                    (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = inflater.inflate(R.layout.movie_poster, null);
            imageView = (ImageView) v.findViewById(R.id.image_view);
        } else {
            imageView = (ImageView) convertView;
        }

        Picasso.with(mContext)
                .load(mAllPostersUrl.get(position))
                .transform(new HalfOfScreenWidthTransformation())
                .into(imageView);

        return imageView;
    }

    /**
     * Image width is half of the device width and height is obtained respecting the original
     * aspect ratio of the image.
     */
    public class HalfOfScreenWidthTransformation implements Transformation {
        private static final String sKey = "half_screen_width";

        @Override
        public Bitmap transform(Bitmap source) {
            WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int screenWidth = size.x;
            int halfScreenWidth = screenWidth/2;

            Bitmap result = Bitmap.createScaledBitmap(source,
                    halfScreenWidth,
                    halfScreenWidth / source.getWidth() * source.getHeight(),
                    false);

            if (result != source) {
                source.recycle();
            }

            return result;
        }

        @Override
        public String key() {
            return sKey;
        }
    }
}
