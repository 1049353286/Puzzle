package com.apricot.puzzle.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.apricot.puzzle.R;
import com.apricot.puzzle.adapter.GridPuzzleMainAdapter;
import com.apricot.puzzle.bean.ItemBean;
import com.apricot.puzzle.util.GameUtil;
import com.apricot.puzzle.util.ImageUtil;
import com.apricot.puzzle.util.ScreenUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Apricot on 2016/9/8.
 */
public class PuzzleMain extends AppCompatActivity implements View.OnClickListener{
    public static final String TAG = "PuzzleMain";
    public static Bitmap mLastBitmap;
    public static int TYPE=2;
    public static int COUNT_INDEX = 0;
    public static int TIMER_INDEX = 0;

    private Bitmap mPicSelected;
    private GridView mGridView;
    private int mResId;
    private String mPicPath;
    private Button mBtnImage;
    private Button mBtnBack;
    private Button mBtnRestart;

    private TextView mTvPuzzleMainCount;
    private TextView mTvTimer;
    private List<Bitmap> mBitmapItemList=new ArrayList<>();
    private GridPuzzleMainAdapter mAdapter;

    private Timer mTimer;

    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    TIMER_INDEX++;
                    mTvTimer.setText(""+TIMER_INDEX);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.puzzle_main);
        Bitmap picSelectedTemp;
        mResId=getIntent().getExtras().getInt("picSelectId");
        mPicPath=getIntent().getExtras().getString("picPath");
        if(mResId!=0){
            picSelectedTemp= BitmapFactory.decodeResource(getResources(),mResId);
        }else{
            picSelectedTemp=ImageUtil.getimage(mPicPath);
            Log.d(TAG,"compress width:"+picSelectedTemp.getWidth()+"compress height:"+picSelectedTemp.getHeight());
        }
        TYPE=getIntent().getExtras().getInt("mType",2);
        handleImage(picSelectedTemp);
        initViews();
        generateGame();
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(GameUtil.isMoveable(position)){
                    GameUtil.swapItems(GameUtil.mItemBeans.get(position),GameUtil.blankItemBean);
                    COUNT_INDEX++;
                    mTvPuzzleMainCount.setText(""+COUNT_INDEX);
                    reCreateData();
                    mAdapter.notifyDataSetChanged();
                }
                if(GameUtil.isSuccess()){
                    mBitmapItemList.remove(TYPE*TYPE-1);
                    mBitmapItemList.add(mLastBitmap);
                    mAdapter.notifyDataSetChanged();
                    Toast.makeText(PuzzleMain.this, "拼图成功!",
                            Toast.LENGTH_LONG).show();
                    mGridView.setEnabled(false);
                    mTimer.cancel();
                }
            }
        });

    }

    private void initViews(){
        mBtnBack= (Button) findViewById(R.id.btn_puzzle_back);
        mBtnImage= (Button) findViewById(R.id.btn_puzzle_img);
        mBtnRestart= (Button) findViewById(R.id.btn_puzzle_restart);
        mTvPuzzleMainCount= (TextView) findViewById(R.id.tv_puzzle_main_counts);
        mTvTimer= (TextView) findViewById(R.id.tv_puzzle_main_time);
        mGridView= (GridView) findViewById(R.id.gv_puzzle_main);
        mGridView.setNumColumns(TYPE);
        mGridView.setHorizontalSpacing(0);
        mGridView.setVerticalSpacing(0);
        RelativeLayout.LayoutParams GridParams=new RelativeLayout.LayoutParams(mPicSelected.getWidth(),mPicSelected.getHeight());
        GridParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        GridParams.addRule(RelativeLayout.BELOW,R.id.ll_puzzle_main_spinner);
        mGridView.setLayoutParams(GridParams);

        mBtnBack.setOnClickListener(this);
        mBtnRestart.setOnClickListener(this);
        mBtnImage.setOnClickListener(this);
        mTvPuzzleMainCount.setText(""+COUNT_INDEX);
        mTvTimer.setText("0秒");
    }

    private void handleImage(Bitmap bitmap){
        int screenWidth= ScreenUtil.getScreenSize(this).widthPixels;
        int screenHeight=ScreenUtil.getScreenSize(this).heightPixels;
        mPicSelected=ImageUtil.resizeBitmap(screenWidth*0.9f,screenHeight*0.7f,bitmap);
        Log.d(TAG,"handle width:"+mPicSelected.getWidth()+"handle height:"+mPicSelected.getHeight());
    }

    private void generateGame(){
        ImageUtil.createInitBitmaps(this,mPicSelected,TYPE);
        GameUtil.getPuzzleGenerator();
        for(ItemBean itemBean:GameUtil.mItemBeans){
            mBitmapItemList.add(itemBean.getmBitmap());
        }
        mAdapter=new GridPuzzleMainAdapter(mBitmapItemList,this);
        mGridView.setAdapter(mAdapter);

        mTimer=new Timer(true);
        TimerTask timerTask=new TimerTask() {
            @Override
            public void run() {
                Message message=new Message();
                message.what=1;
                mHandler.sendMessage(message);
            }
        };
        mTimer.schedule(timerTask,0,1000);
    }

    private void reCreateData(){
        mBitmapItemList.clear();
        for(ItemBean itemBean:GameUtil.mItemBeans){
            mBitmapItemList.add(itemBean.getmBitmap());
        }
    }


    private void cleanConfig(){
        GameUtil.mItemBeans.clear();
        mTimer.cancel();
        COUNT_INDEX=0;
        TIMER_INDEX=0;
        if(mPicPath!=null){
            File file=new File(MainActivity.TEMP_IMAGE_PATH);
            if(file.exists()){
                file.delete();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cleanConfig();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_puzzle_back:
                PuzzleMain.this.finish();
                break;
            case R.id.btn_puzzle_restart:
                cleanConfig();
                generateGame();
                reCreateData();
                mAdapter.notifyDataSetChanged();
                mTvPuzzleMainCount.setText(""+COUNT_INDEX);
                mGridView.setEnabled(true);
                break;
            case R.id.btn_puzzle_img:
                break;
            default:
                break;
        }
    }
}
