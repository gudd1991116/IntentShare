package com.share.gudd.intentshare;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.share.gudd.intentshare.utils.Resource;
import com.share.gudd.intentshare.utils.ShareToolUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;

        ShareToolUtil.getPermission(this);

        final List<String> datas = new ArrayList<>();
        datas.add("微信好友-文本");
        datas.add("微信好友-图片");
        datas.add("微信好友-文件");
        datas.add("微信朋友圈-单图");
        datas.add("QQ好友-文本");
        datas.add("QQ好友-图片");
        datas.add("QQ好友-文件");
        datas.add("QQ空间");
        datas.add("新浪好友");
        datas.add("新浪微博");
        datas.add("直接添加QQ群号");



        final NativeShareTool nativeShareTool = NativeShareTool.getInstance(this);

        RecyclerView recyclerview = findViewById(R.id.recyclerview);
        recyclerview.setLayoutManager(new GridLayoutManager(this, 2));
        MyAdapter myAdapter = new MyAdapter(datas);
        recyclerview.setAdapter(myAdapter);
        myAdapter.notifyDataSetChanged();
        myAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                switch (datas.get(position)) {
                    case "微信好友-文本":
                        nativeShareTool.shareWechatFriend("Test Test test!!!");
                        break;
                    case "微信好友-图片":
                        nativeShareTool.shareWechatFriend(Resource.getInstance(mContext).getPicFile(),true);
                        break;
                    case "微信好友-文件":
                        nativeShareTool.shareWechatFriend(Resource.getInstance(mContext).getDocFile("contract.docx"),false);
                        break;
                    case "微信朋友圈-单图":
                        nativeShareTool.shareWechatMoment(Resource.getInstance(mContext).getPicFile());
                        break;
                    case "QQ好友-文本":
                        nativeShareTool.shareQQ("Test test test!!!");
                        break;
                    case "QQ好友-图片":
                        nativeShareTool.shareImageToQQ(getPicBit());
                        break;
                    case "QQ好友-文件":
                        nativeShareTool.shareImageToQQ(Resource.getInstance(mContext).getDocFile("newpdf.pdf"));
                        break;
                    case "QQ空间":
                        nativeShareTool.shareImageToQQZone(Resource.getInstance(mContext).getPicFile().getAbsolutePath());
                        break;
                    case "新浪好友":
                        nativeShareTool.shareToSinaFriends(MainActivity.this, true,Resource.getInstance(mContext).getPicFile().getAbsolutePath());
                        break;
                    case "新浪微博":
                        nativeShareTool.shareToSinaFriends(MainActivity.this, false,Resource.getInstance(mContext).getPicFile().getAbsolutePath());
                        break;
                    case "直接添加QQ群号":
                        AboutQQUtil.joinQQGroup(MainActivity.this, "CXaQmSGNixYtgpaRuUlxd0CwyMhQYkd_");
                        break;
                }
            }
        });


    }

    private Bitmap getPicBit() {
        AssetManager assets = this.getResources().getAssets();
        InputStream open = null;
        File mFile = null;
        Bitmap mBitmap = null;
        try {
            open = assets.open("share_pic.jpg");

            BitmapFactory.Options bo  = new BitmapFactory.Options();
            bo.inScaled = false;
            bo.inPreferredConfig = Bitmap.Config.RGB_565;
            bo.inJustDecodeBounds = false;
            bo.inDither = true;
            mBitmap = BitmapFactory.decodeStream(open, null, bo);

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (open != null){
                try {
                    open.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return mBitmap;
    }

    class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyHolder> {
        List<String> mDatas;
        private OnItemClickListener mOnItemClickListener;

        public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            this.mOnItemClickListener = onItemClickListener;
        }

        public MyAdapter(List<String> datas) {
            this.mDatas = datas;
        }

        @NonNull
        @Override
        public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return new MyHolder(LayoutInflater.from(MainActivity.this).inflate(R.layout.recyclerview_item, viewGroup, false));
        }

        @Override
        public void onBindViewHolder(@NonNull MyHolder myHolder, int i) {
            myHolder.item.setText(mDatas.get(i));
            String color = "#6896ff";
            if (i % 3 == 0) {
                color = "#71fec5";
            }else if (i % 3 == 1){
                color = "#ff9946";
            }
            myHolder.item.setBackgroundColor(Color.parseColor(color));
            myHolder.item.setTextColor(Color.parseColor("#ffffff"));

            myHolder.item.setTag(i);
        }

        @Override
        public int getItemCount() {
            return mDatas.size();
        }

        class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            TextView item;

            public MyHolder(@NonNull View itemView) {
                super(itemView);
                item = itemView.findViewById(R.id.list_item);

                item.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    int position = (Integer) v.getTag();
                    mOnItemClickListener.onItemClick(v, position);
                }
            }
        }


    }

    interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
}
