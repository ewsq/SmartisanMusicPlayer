package com.yibao.music.view.music;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.yibao.music.R;
import com.yibao.music.base.listener.OnGlideLoadListener;
import com.yibao.music.util.Constants;
import com.yibao.music.util.LogUtil;

import java.util.Objects;

/**
 * Des：${将音乐列表(RecyclerView) ,StickyView, SlideBar导航栏 封装到一个Viwe里面，方便多个页面使用}
 * Time:2017/9/10 00:43
 *
 * @author Stran
 */

public class MusicView
        extends RelativeLayout {

    private RecyclerView mRecyclerView;
    private MusicSlidBar mSlidebar;

    public MusicView(Context context) {
        super(context);
        initView();
        initListener(context);
    }

    public MusicView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
        initListener(context);
    }

    private void initView() {
        //自定义组合控件将第三个参数设置为true  解析之后直接添加到当前view中
        LayoutInflater.from(getContext())
                .inflate(R.layout.music_view, this, true);
        mRecyclerView = findViewById(R.id.rv);
        mSlidebar = findViewById(R.id.music_slidbar);


    }

    private void initListener(Context context) {
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (context instanceof OnGlideLoadListener) {
                    switch (newState) {
                        case RecyclerView.SCROLL_STATE_IDLE:
                            ((OnGlideLoadListener) context).resumeRequests();
                            break;
                        // 加载图片
                        case RecyclerView.SCROLL_STATE_DRAGGING:
                        case RecyclerView.SCROLL_STATE_SETTLING:
                            ((OnGlideLoadListener) context).pauseRequests();
                            break;

                        default:
                            break;
                    }
                }
            }
        });
    }

    /**
     * //设置列表的适配器
     *
     * @param context        c
     * @param adapterType    1 : SongListAdapter  、 2  ：ArtistAdapter  、
     *                       3  ： AlbumAdapter  普通视图  、 4  ： AlbumAdapter  平铺视图 GridView 3列
     * @param isShowSlideBar 只有按歌曲名排列时，Slidebar才显示 。
     */
    public void setAdapter(Context context, int adapterType, boolean isShowSlideBar, RecyclerView.Adapter adapter) {
        mSlidebar.setAdapterType(adapterType);
        if (adapterType == Constants.NUMBER_FOUR) {
            GridLayoutManager manager = new GridLayoutManager(context, Constants.NUMBER_THRRE);
            manager.setOrientation(GridLayoutManager.VERTICAL);
            mRecyclerView.setLayoutManager(manager);
        } else {
            LinearLayoutManager manager = new LinearLayoutManager(context);
            manager.setOrientation(LinearLayoutManager.VERTICAL);
            mRecyclerView.setLayoutManager(manager);
            DividerItemDecoration divider = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
            divider.setDrawable(Objects.requireNonNull(ContextCompat.getDrawable(context, R.drawable.shape_item_decoration)));
            mRecyclerView.addItemDecoration(divider);
        }

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        mSlidebar.setBarVisibility(isShowSlideBar ? VISIBLE : GONE);

    }


}







