package com.puxiang.mall.module.emotion.view.fragment;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.gyf.barlibrary.ImmersionBar;
import com.puxiang.mall.R;
import com.puxiang.mall.module.emotion.utils.DisplayUtils;
import com.puxiang.mall.module.emotion.utils.EmotionUtils;
import com.puxiang.mall.module.emotion.utils.GlobalOnItemClickManagerUtils;
import com.puxiang.mall.module.emotion.view.emotionkeyboardview.EmojiIndicatorView;
import com.puxiang.mall.module.emotion.viewmodel.adapter.EmotionPagerAdapter;
import com.puxiang.mall.module.emotion.viewmodel.adapter.EmotionViewsAdapter;
import com.puxiang.mall.utils.AutoUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:可替换的模板表情，gridview实现
 */
public class EmotionComplateFragment extends BaseFragment {
    private ViewPager vp;
    private EmojiIndicatorView ll_point_group;//表情面板对应的点列表
    private int emotionType;

    /**
     * 创建与Fragment对象关联的View视图时调用
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_complate_emotion, container, false);
        AutoUtils.auto(rootView);
        initView(rootView);
        initListener();
        return rootView;
    }

    /**
     * 初始化view控件
     */
    protected void initView(View rootView) {
        vp = (ViewPager) rootView.findViewById(R.id.vp_complate_emotion_layout);
        ll_point_group = (EmojiIndicatorView) rootView.findViewById(R.id.ll_point_group);
        //获取map的类型
        emotionType = args.getInt(FragmentFactory.EMOTION_MAP_TYPE);
        initEmotion();
    }

    /**
     * 初始化监听器
     */
    protected void initListener() {

        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            int oldPagerPos = 0;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                ll_point_group.playByStartPointToNext(oldPagerPos, position);
                oldPagerPos = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * 初始化表情面板
     * 思路：获取表情的总数，按每行存放7个表情，动态计算出每个表情所占的宽度大小（包含间距），
     * 而每个表情的高与宽应该是相等的，这里我们约定只存放3行
     * 每个面板最多存放7*3=21个表情，再减去一个删除键，即每个面板包含20个表情
     * 根据表情总数，循环创建多个容量为20的List，存放表情，对于大小不满20进行特殊
     * 处理即可。
     */
    private void initEmotion() {
        // 获取屏幕宽度
        int screenWidth = DisplayUtils.getScreenWidthPixels(getActivity());
        // item的间距
        int spacing = DisplayUtils.dp2px(getActivity(), 12);
        // 动态计算item的宽度和高度
        int itemWidth = (screenWidth - spacing * 8) / 7;
        //动态计算gridview的总高度
        int gvHeight = itemWidth * 3 + spacing * 6;

        List<RecyclerView> emotionViews = new ArrayList<>();
        List<String> emotionNames = new ArrayList<>();
        // 遍历所有的表情的key
        for (String emojiName : EmotionUtils.getEmojiMap(emotionType).keySet()) {
            emotionNames.add(emojiName);
            // 每20个表情作为一组,同时添加到ViewPager对应的view集合中
            if (emotionNames.size() == 21) {
                RecyclerView rv = createEmotionGridView(emotionNames, screenWidth, spacing, itemWidth, gvHeight);
                emotionViews.add(rv);
                // 添加完一组表情,重新创建一个表情名字集合
                emotionNames = new ArrayList<>();
            }
        }
        // 判断最后是否有不足20个表情的剩余情况
        if (emotionNames.size() > 0) {
            RecyclerView rv = createEmotionGridView(emotionNames, screenWidth, spacing, itemWidth, gvHeight);
            emotionViews.add(rv);
        }
        //初始化指示器
        ll_point_group.initIndicator(emotionViews.size());
        // 将多个GridView添加显示到ViewPager中
        EmotionPagerAdapter emotionPagerGvAdapter = new EmotionPagerAdapter(emotionViews);
        vp.setAdapter(emotionPagerGvAdapter);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(screenWidth, gvHeight);
        vp.setLayoutParams(params);
        ImmersionBar.with(this).statusBarDarkFont(true).flymeOSStatusBarFontColor(R.color.text_black).keyboardEnable(true).keyboardMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE).init();
    }

    /**
     * 创建显示表情的GridView
     */
    private RecyclerView createEmotionGridView(List<String> emotionNames, int gvWidth, int padding, int itemWidth,
                                               int gvHeight) {
        // 创建GridView
        RecyclerView rv = new RecyclerView(getActivity());
        rv.setLayoutManager(new GridLayoutManager(getActivity(), 7, GridLayoutManager.VERTICAL, false));
        rv.setPadding(padding, padding, padding, padding);
        //设置GridView的宽高
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(gvWidth, gvHeight);
        rv.setLayoutParams(params);
        rv.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.bottom = padding;
                // Add top margin only for the first item to avoid double space between items
//                if (parent.getChildPosition(view) == 0)
//                    outRect.top = padding * 2;
            }
        });
        // 给GridView设置表情图片
//        EmotionGridViewAdapter adapter = new EmotionGridViewAdapter(getActivity(), emotionNames, itemWidth,
//                emotionType);

        EmotionViewsAdapter adapter = new EmotionViewsAdapter(emotionNames, emotionType, itemWidth);
        rv.setAdapter(adapter);
        //设置全局点击事件
        rv.addOnItemTouchListener(GlobalOnItemClickManagerUtils.getInstance().getOnItemClickListener(emotionType));
        //   AutoUtils.auto(gv);
        return rv;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
