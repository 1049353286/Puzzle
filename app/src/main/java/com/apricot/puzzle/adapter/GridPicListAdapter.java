package com.apricot.puzzle.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.apricot.puzzle.util.ImageUtil;
import com.apricot.puzzle.util.ScreenUtil;

import java.util.List;

/**
 * Created by Apricot on 2016/9/8.
 */
public class GridPicListAdapter extends BaseAdapter{
    private List<Bitmap> picList;
    private Context mContext;

    public GridPicListAdapter(Context mContext,List<Bitmap> picList){
        this.picList=picList;
        this.mContext=mContext;
    }
    @Override
    public int getCount() {
        return picList.size();
    }

    @Override
    public Object getItem(int i) {
        return picList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ImageView iv_pic_item=null;
        int density= (int) ScreenUtil.getDeviceDensity(mContext);
        if(view==null){
            iv_pic_item=new ImageView(mContext);
            iv_pic_item.setLayoutParams(new GridView.LayoutParams(115*density,160*density));
            iv_pic_item.setScaleType(ImageView.ScaleType.FIT_XY);
        }else{
            iv_pic_item= (ImageView) view;
        }
        iv_pic_item.setImageBitmap(picList.get(i));
        return iv_pic_item;
    }
}
