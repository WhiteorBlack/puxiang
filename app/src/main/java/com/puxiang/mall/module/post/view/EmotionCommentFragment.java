package com.puxiang.mall.module.post.view;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.puxiang.mall.MyApplication;
import com.puxiang.mall.R;
import com.puxiang.mall.databinding.FragmentCommentEmotionBinding;
import com.puxiang.mall.fragment.BaseBindFragment;
import com.puxiang.mall.model.data.RxCommentReply;
import com.puxiang.mall.module.emotion.model.ImageModel;
import com.puxiang.mall.module.emotion.utils.EmotionUtils;
import com.puxiang.mall.module.emotion.utils.GlobalOnItemClickManagerUtils;
import com.puxiang.mall.module.emotion.utils.SharedPreferencedUtils;
import com.puxiang.mall.module.emotion.view.emotionkeyboardview.EmotionKeyboard;
import com.puxiang.mall.module.emotion.view.fragment.BEmotiomFragment;
import com.puxiang.mall.module.emotion.view.fragment.EmotionComplateFragment;
import com.puxiang.mall.module.emotion.view.fragment.FragmentFactory;
import com.puxiang.mall.module.emotion.viewmodel.adapter.HorizontalRecyclerviewAdapter;
import com.puxiang.mall.module.emotion.viewmodel.adapter.NoHorizontalScrollerVPAdapter;
import com.puxiang.mall.module.login.view.LoginFragment;
import com.puxiang.mall.network.NetworkSubscriber;
import com.puxiang.mall.network.retrofit.ApiWrapper;
import com.puxiang.mall.utils.AppUtil;
import com.puxiang.mall.utils.MyTextUtils;
import com.puxiang.mall.utils.StringUtil;
import com.puxiang.mall.utils.ToastUtil;
import com.gyf.barlibrary.ImmersionBar;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zejian
 * Time  16/1/6 下午5:26
 * Email shinezejian@163.com
 * Description:表情主界面
 */
public class EmotionCommentFragment extends BaseBindFragment implements View.OnClickListener {

    //是否绑定当前Bar的编辑框的flag
    public static final String BIND_TO_EDITTEXT = "bind_to_edittext";
    //是否隐藏bar上的编辑框和发生按钮
    public static final String HIDE_BAR_EDITTEXT_AND_BTN = "hide bar's editText and btn";

    //当前被选中底部tab
    private static final String CURRENT_POSITION_FLAG = "CURRENT_POSITION_FLAG";
    private int CurrentPosition = 0;
    //底部水平tab
    private HorizontalRecyclerviewAdapter horizontalRecyclerviewAdapter;
    //表情面板
    private EmotionKeyboard mEmotionKeyboard;


    //需要绑定的内容view
    private View contentView;

    //不可横向滚动的ViewPager
    // private NoHorizontalScrollerViewPager viewPager;

    //是否绑定当前Bar的编辑框,默认true,即绑定。
    //false,则表示绑定contentView,此时外部提供的contentView必定也是EditText
    private boolean isBindToBarEditText = true;


    List<Fragment> fragments = new ArrayList<>();
    private CommentActivity commentActivity;
    private FragmentCommentEmotionBinding binding;


    /**
     * 创建与Fragment对象关联的View视图时调用
     *
     * @param inflater
     * @param container
     */
    @Override
    public View initBinding(LayoutInflater inflater, ViewGroup container) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_comment_emotion, container, false);
        commentActivity = ((CommentActivity) getActivity());
        return binding.getRoot();
    }

    /**
     * 绑定内容view
     *
     * @param contentView
     */
    public void bindToContentView(View contentView) {
        this.contentView = contentView;
    }

    /**
     * 初始化view控件
     */
    public void initView() {
        mEmotionKeyboard = EmotionKeyboard.with(getActivity())
                .setEmotionView(binding.llEmotionLayout)//绑定表情面板
                .bindToContent(contentView)//绑定内容view
                .bindToEditText(binding.emotionBar.etEmotion)//判断绑定那种EditView
                .bindToEmotionButton(binding.emotionBar.ivEmotionBtn)//绑定表情按钮
                .build();
        GlobalOnItemClickManagerUtils.getInstance().attachToEditText(binding.emotionBar.etEmotion);
        initDatas();
        binding.emotionBar.btnSend.setOnClickListener(this);
        //创建全局监听
        ImmersionBar.with(this).statusBarDarkFont(true).statusBarDarkFont(true).flymeOSStatusBarFontColor(R.color.text_black).flymeOSStatusBarFontColor(R.color.text_black).keyboardEnable(true).keyboardMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE).init();
    }

    @Override
    protected void viewModelDestroy() {
        //  GlobalOnItemClickManagerUtils.getInstance().detachToEditText();
    }

    /**
     * 数据操作,这里是测试数据，请自行更换数据
     */
    int[] imgRes = {R.mipmap.a00, R.mipmap.b00, R.mipmap.c00, R.mipmap.d00, R.mipmap.f00, R.mipmap.g00};

    protected void initDatas() {
        replaceFragment();
        List<ImageModel> list = new ArrayList<>();
        for (int i = 0; i < fragments.size(); i++) {
            ImageModel model1 = new ImageModel();
            model1.icon = AppUtil.getDrawable(imgRes[i]);
            if (i == 0) {
                model1.flag = "经典笑脸";
                model1.isSelected = true;
                list.add(model1);
            } else {
                model1.flag = "其他笑脸" + i;
                model1.isSelected = false;
                list.add(model1);
            }
        }

        //记录底部默认选中第一个
        CurrentPosition = 0;
        SharedPreferencedUtils.setInteger(getActivity(), CURRENT_POSITION_FLAG, CurrentPosition);

        //底部tab
        horizontalRecyclerviewAdapter = new HorizontalRecyclerviewAdapter(getActivity(), list);
        binding.rv.setHasFixedSize(true);//使RecyclerView保持固定的大小,这样会提高RecyclerView的性能
        binding.rv.setAdapter(horizontalRecyclerviewAdapter);
        binding.rv.setLayoutManager(new GridLayoutManager(getActivity(), 1, GridLayoutManager
                .HORIZONTAL, false));
        //初始化recyclerview_horizontal监听器
        horizontalRecyclerviewAdapter.setOnClickItemListener(new HorizontalRecyclerviewAdapter
                .OnClickItemListener() {
            @Override
            public void onItemClick(View view, int position, List<ImageModel> datas) {
                //获取先前被点击tab
                int oldPosition = SharedPreferencedUtils.getInteger(getActivity(),
                        CURRENT_POSITION_FLAG, 0);
                //修改背景颜色的标记
                datas.get(oldPosition).isSelected = false;
                //记录当前被选中tab下标
                CurrentPosition = position;
                datas.get(CurrentPosition).isSelected = true;
                SharedPreferencedUtils.setInteger(getActivity(), CURRENT_POSITION_FLAG,
                        CurrentPosition);
                //通知更新，这里我们选择性更新就行了
                horizontalRecyclerviewAdapter.notifyItemChanged(oldPosition);
                horizontalRecyclerviewAdapter.notifyItemChanged(CurrentPosition);
                //viewpager界面切换
                binding.vp.setCurrentItem(position, false);
            }

            @Override
            public void onItemLongClick(View view, int position, List<ImageModel> datas) {
            }
        });
    }

    private void replaceFragment() {
        //创建fragment的工厂类
        FragmentFactory factory = FragmentFactory.getSingleFactoryInstance();
        //创建修改实例
        EmotionComplateFragment f1 = (EmotionComplateFragment) factory.getFragment(EmotionUtils
                .EMOTION_A_TYPE);
        BEmotiomFragment f2 = (BEmotiomFragment) factory.getFragment(EmotionUtils.EMOTION_B_TYPE);
        BEmotiomFragment f3 = (BEmotiomFragment) factory.getFragment(EmotionUtils.EMOTION_C_TYPE);
        BEmotiomFragment f4 = (BEmotiomFragment) factory.getFragment(EmotionUtils.EMOTION_D_TYPE);
        BEmotiomFragment f5 = (BEmotiomFragment) factory.getFragment(EmotionUtils.EMOTION_E_TYPE);
        BEmotiomFragment f6 = (BEmotiomFragment) factory.getFragment(EmotionUtils.EMOTION_F_TYPE);
        BEmotiomFragment f7 = (BEmotiomFragment) factory.getFragment(EmotionUtils.EMOTION_G_TYPE);
        fragments.add(f1);
        fragments.add(f2);
        fragments.add(f3);
        fragments.add(f4);
        fragments.add(f5);
        fragments.add(f6);
        fragments.add(f7);
        NoHorizontalScrollerVPAdapter adapter = new NoHorizontalScrollerVPAdapter(getActivity()
                .getSupportFragmentManager(), fragments);
        binding.vp.setAdapter(adapter);
    }


    /**
     * 是否拦截返回键操作，如果此时表情布局未隐藏，先隐藏表情布局
     *
     * @return true则隐藏表情布局，拦截返回键操作
     * false 则不拦截返回键操作
     */
    public boolean isInterceptBackPress() {
        boolean b = false;
        if (mEmotionKeyboard != null) {
            b = mEmotionKeyboard.interceptBackPress();
        }
        return b;
    }


    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_send:
                if (StringUtil.isEmpty(MyApplication.TOKEN)) {
                    startActivity(new Intent(getContext(), LoginFragment.class));
                } else {
                    if (MyTextUtils.isEmpty(binding.emotionBar.etEmotion)) {
                        ToastUtil.toast("请输入评论内容");
                        return;
                    }
                    String context = binding.emotionBar.etEmotion.getText().toString();
                    reply(context);
                    binding.emotionBar.etEmotion.setText("");
                    isInterceptBackPress();
                }
                break;
        }
    }

    private void reply(String context) {
        ApiWrapper.getInstance().reply(commentActivity.viewModel.commentId, commentActivity
                .viewModel.byReplyUserId, context)
                .subscribe(new NetworkSubscriber<RxCommentReply>() {
                    @Override
                    public void onSuccess(RxCommentReply bean) {
                        commentActivity.viewModel.refresh();
                    }
                });
    }
}


