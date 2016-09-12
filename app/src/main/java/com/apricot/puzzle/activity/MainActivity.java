package com.apricot.puzzle.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.apricot.puzzle.R;
import com.apricot.puzzle.adapter.GridPicListAdapter;
import com.apricot.puzzle.util.ImageUtil;
import com.apricot.puzzle.util.ScreenUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    public static final String TAG="MainActivity";
    public static final int RESULT_IMAGE=100;
    public static final int RESULT_CAMERA=200;
    public static String TEMP_IMAGE_PATH;
    private int mType=2;
    private GridView mGridView;
    private List<Bitmap> mPicList;
    private int[] mResPicId;
    private TextView mTvTypeSelect;
    LayoutInflater mLayoutInflater;
    private PopupWindow mPopupWindow;
    private View mPopupView;
    private TextView mTvType2;
    private TextView mTvType3;
    private TextView mTvType4;
    private String[] mCustomItems = new String[]{"本地图册", "相机拍照"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPicList=new ArrayList<>();
        TEMP_IMAGE_PATH = Environment.getExternalStorageDirectory().getPath() + "/temp.jpg";
        initViews();
        mGridView.setAdapter(new GridPicListAdapter(MainActivity.this,mPicList));
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==mResPicId.length-1){
                    showCustomDialog();
                }else{
                    Intent intent=new Intent(MainActivity.this,PuzzleMain.class);
                    intent.putExtra("picSelectId",mResPicId[i]);
                    intent.putExtra("mType",mType);
                    startActivity(intent);
                }
            }
        });
        mTvTypeSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopup(view);
            }
        });
    }



    private void initViews(){
        mGridView= (GridView) findViewById(R.id.main_gv);
        mTvTypeSelect= (TextView) findViewById(R.id.type_select);

        mResPicId=new int[]{
                R.drawable.pic1, R.drawable.pic2, R.drawable.pic3,
                R.drawable.pic4, R.drawable.pic5, R.drawable.pic6,
                R.drawable.pic7, R.drawable.pic8, R.drawable.pic9,
                R.drawable.pic10, R.drawable.pic11, R.drawable.pic12,
                R.drawable.pic13, R.drawable.pic14,
                R.drawable.pic15, R.drawable.plus};

        Bitmap[] bitmaps=new Bitmap[mResPicId.length];
        for(int i=0;i<bitmaps.length;i++){
            bitmaps[i]= BitmapFactory.decodeResource(getResources(),mResPicId[i]);
            mPicList.add(bitmaps[i]);
        }
        mLayoutInflater= (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        mPopupView =mLayoutInflater.inflate(R.layout.main_type_select,null);
        mTvType2= (TextView) mPopupView.findViewById(R.id.tv_main_type2);
        mTvType3= (TextView) mPopupView.findViewById(R.id.tv_main_type3);
        mTvType4= (TextView) mPopupView.findViewById(R.id.tv_main_type4);

        mTvType2.setOnClickListener(this);
        mTvType3.setOnClickListener(this);
        mTvType4.setOnClickListener(this);
    }

    private void showCustomDialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("选择:");
        builder.setItems(mCustomItems, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(i==0){
                    Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
                    startActivityForResult(intent,RESULT_IMAGE);
                }else if(i==1){
                    Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File file=new File(TEMP_IMAGE_PATH);
                    if(file.exists()){
                        file.delete();
                    }
                    Uri photoUri=Uri.fromFile(file);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT,photoUri);
                    startActivityForResult(intent,RESULT_CAMERA);
                }
            }
        });
        builder.create().show();
    }

    private void showPopup(View view){
        int density= (int) ScreenUtil.getDeviceDensity(MainActivity.this);
        mPopupWindow=new PopupWindow(mPopupView,90*density,140*density);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(true);
        Drawable transparent=new ColorDrawable(Color.WHITE);
        mPopupWindow.setBackgroundDrawable(transparent);
        mPopupWindow.showAsDropDown(view);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if (resultCode == RESULT_OK) {
            if (requestCode == RESULT_IMAGE && data != null) {
                // 相册
                Uri imageUri=data.getData();
                String imagePath=imageUri.getPath();
                Intent intent = new Intent(
                        MainActivity.this,
                        PuzzleMain.class);
                intent.putExtra("picPath", imagePath);
                intent.putExtra("mType", mType);
                startActivity(intent);
            } else if (requestCode == RESULT_CAMERA) {
                // 相机
                Intent intent = new Intent(
                        MainActivity.this,
                        PuzzleMain.class);
                intent.putExtra("picPath", TEMP_IMAGE_PATH);
                intent.putExtra("mType", mType);
                startActivity(intent);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // Type
            case R.id.tv_main_type2:
                mType = 2;
                mTvTypeSelect.setText("2 X 2");
                break;
            case R.id.tv_main_type3:
                mType = 3;
                mTvTypeSelect.setText("3 X 3");
                break;
            case R.id.tv_main_type4:
                mType = 4;
                mTvTypeSelect.setText("4 X 4");
                break;
            default:
                break;
        }
        mPopupWindow.dismiss();

    }
}
