package com.apricot.puzzle.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.apricot.puzzle.util.ScreenUtil;

import java.util.List;

/**
 * Created by Apricot on 2016/9/10.
 */
public class GridPuzzleMainAdapter extends BaseAdapter{
    List<Bitmap> bitmaps;
    Context mContext;
    public GridPuzzleMainAdapter(List<Bitmap> bitmaps,Context mContext){
        this.bitmaps=bitmaps;
        this.mContext=mContext;
    }
    @Override
    public int getCount() {
        return bitmaps.size();
    }

    @Override
    public Object getItem(int position) {
        return bitmaps.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView iv_pic_item=null;
        if(convertView==null){
            iv_pic_item=new ImageView(mContext);
            iv_pic_item.setLayoutParams(new GridView.LayoutParams(
                    bitmaps.get(position).getWidth(),
                    bitmaps.get(position).getHeight()
            ));
            iv_pic_item.setScaleType(ImageView.ScaleType.FIT_CENTER);
        }else{
            iv_pic_item= (ImageView) convertView;
        }
        iv_pic_item.setImageBitmap(bitmaps.get(position));
        return iv_pic_item;
    }
}
