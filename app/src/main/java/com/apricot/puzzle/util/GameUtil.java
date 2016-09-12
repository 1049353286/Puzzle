package com.apricot.puzzle.util;

import com.apricot.puzzle.activity.PuzzleMain;
import com.apricot.puzzle.bean.ItemBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Apricot on 2016/9/10.
 */
public class GameUtil {
    public static List<ItemBean> mItemBeans=new ArrayList<>();
    public static ItemBean blankItemBean=new ItemBean();


    public static void getPuzzleGenerator(){
        int index=0;
        for(int i=0;i<mItemBeans.size();i++){
            index=(int)(Math.random()*PuzzleMain.TYPE*PuzzleMain.TYPE);
            swapItems(mItemBeans.get(index),blankItemBean);
        }
        List<Integer> data=new ArrayList<>();
        for(int i=0;i<mItemBeans.size();i++){
            data.add(mItemBeans.get(i).getmBitmapId());
        }
        if(canSolve(data)){
            return;
        }else{
            getPuzzleGenerator();
        }
    }

    public static boolean isMoveable(int position){
        int type= PuzzleMain.TYPE;
        int blankId=blankItemBean.getmItemId()-1;
        if(Math.abs(blankId-position)==type){
            return true;
        }
        if((blankId/type==position/type)&&
                Math.abs(blankId-position)==1){
            return true;
        }
        return false;
    }


    public static void swapItems(ItemBean from,ItemBean blank){
        ItemBean temp=new ItemBean();
        temp.setmBitmap(from.getmBitmap());
        from.setmBitmap(blank.getmBitmap());
        blank.setmBitmap(temp.getmBitmap());

        temp.setmBitmapId(from.getmBitmapId());
        from.setmBitmapId(blank.getmBitmapId());
        blank.setmBitmapId(temp.getmBitmapId());

        GameUtil.blankItemBean=from;
    }

    public static boolean isSuccess(){
        for(ItemBean itemBean:mItemBeans){
            if(itemBean.getmBitmapId()!=0&&itemBean.getmBitmapId()==itemBean.getmItemId()){
                continue;
            }else if(itemBean.getmBitmapId()==0&&itemBean.getmItemId()==PuzzleMain.TYPE*PuzzleMain.TYPE){
                continue;
            }else{
                return false;
            }
        }
        return true;
    }

    public static boolean canSolve(List<Integer> data){
        int blankId=blankItemBean.getmItemId()-1;
        if(data.size()%2==1){
            return getInversions(data)%2==0;
        }else{
            if((blankId/PuzzleMain.TYPE)%2==1){
                return getInversions(data)%2==0;
            }else{
                return  getInversions(data)%2==1;
            }
        }

    }

    public static int getInversions(List<Integer> data){
        int inversions=0;
        int inversionCount=0;
        for(int i=0;i<data.size();i++){
            for(int j=i+1;j<data.size();j++){
                int index=data.get(i);
                if(data.get(j)!=0&&data.get(j)<index){
                    inversionCount++;
                }
            }
            inversions+=inversionCount;
            inversionCount=0;
        }
        return inversions;
    }
}
