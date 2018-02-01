package com.puxiang.mall.module.emotion.view.fragment;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;

import com.orhanobut.logger.Logger;
import com.puxiang.mall.MyApplication;
import com.puxiang.mall.R;
import com.puxiang.mall.databinding.FragmentMainEmotionBinding;
import com.puxiang.mall.fragment.BaseBindFragment;
import com.puxiang.mall.model.data.RxPostInfo;
import com.puxiang.mall.module.emotion.model.ImageModel;
import com.puxiang.mall.module.emotion.utils.EmotionUtils;
import com.puxiang.mall.module.emotion.utils.GlobalOnItemClickManagerUtils;
import com.puxiang.mall.module.emotion.utils.SharedPreferencedUtils;
import com.puxiang.mall.module.emotion.view.emotionkeyboardview.EmotionKeyboard;
import com.puxiang.mall.module.emotion.viewmodel.EmotionViewModer;
import com.puxiang.mall.module.emotion.viewmodel.adapter.HorizontalRecyclerviewAdapter;
import com.puxiang.mall.module.emotion.viewmodel.adapter.NoHorizontalScrollerVPAdapter;
import com.puxiang.mall.module.login.view.LoginFragment;
import com.puxiang.mall.module.main.view.MainActivity;
import com.puxiang.mall.module.post.view.PostDetailActivity;
import com.puxiang.mall.utils.ActivityUtil;
import com.puxiang.mall.utils.AppUtil;
import com.puxiang.mall.utils.MyTextUtils;
import com.puxiang.mall.utils.StringUtil;
import com.puxiang.mall.utils.ToastUtil;
import com.gyf.barlibrary.ImmersionBar;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:表情主界面
 */
public class EmotionMainFragment extends BaseBindFragment implements View.OnClickListener {

    //是否绑定当前Bar的编辑框的flag
    public static final String BIND_TO_EDITTEXT = "bind_to_edittext";
    //是否隐藏bar上的编辑框和发生按钮
    public static final String HIDE_BAR_EDITTEXT_AND_BTN = "hide bar's editText and btn";

    //当前被选中底部tab
    private static final String CURRENT_POSITION_FLAG = "CURRENT_POSITION_FLAG";
    private int CurrentPosition = 0;
    //底部水平tab
    // private RecyclerView recyclerview_horizontal;
    private HorizontalRecyclerviewAdapter horizontalRecyclerviewAdapter;
    //表情面板
    private EmotionKeyboard mEmotionKeyboard;


    //需要绑定的内容view
    private View contentView;

    //不可横向滚动的ViewPager
    // private NoHorizontalScrollerViewPager viewPager;

    List<Fragment> fragments = new ArrayList<>();
    private PostDetailActivity postDetail;
    private FragmentMainEmotionBinding binding;
    private EmotionViewModer viewModer;
    private boolean isHidenBarEditTextAndBtn;


    @Override
    public View initBinding(LayoutInflater inflater, ViewGroup container) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main_emotion, container, false);
        initView();
        initDatas();
        ImmersionBar.with(getActivity(), this).keyboardEnable(true).keyboardMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE).init();
        return binding.getRoot();
    }

    public void setViewModel(ObservableField<RxPostInfo> postInfoBean) {
        viewModer = new EmotionViewModer(this, postInfoBean);
        binding.setViewModel(viewModer);
        binding.emotionBar.setViewModel(viewModer);
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
        Bundle args = getArguments();
        isHidenBarEditTextAndBtn = args.getBoolean(EmotionMainFragment.HIDE_BAR_EDITTEXT_AND_BTN);
        //获取判断绑定对象的参数
        boolean isBindToBarEditText = args.getBoolean(EmotionMainFragment.BIND_TO_EDITTEXT);
        mEmotionKeyboard = EmotionKeyboard.with(getActivity())
                .setEmotionView(binding.llEmotionLayout)//绑定表情面板
                .bindToContent(contentView)//绑定内容view
                .bindToEditText(!isBindToBarEditText ? ((EditText) contentView) : binding
                        .emotionBar.etEmotion)//判断绑定那种EditView
                .bindToEmotionButton(binding.emotionBar.ivEmotionBtn)//绑定表情按钮
                .build();
        postDetail = (PostDetailActivity) getActivity();

        binding.emotionBar.vZan.setOnClickListener(this);
        binding.emotionBar.btnSend.setOnClickListener(this);
        binding.emotionBar.ivShareBtn.setOnClickListener(this);

    }

    @Override
    public void onResume() {
        super.onResume();
        GlobalOnItemClickManagerUtils.getInstance().attachToEditText(binding.emotionBar.etEmotion);
    }


    @Override
    protected void viewModelDestroy() {
        if (viewModer != null) viewModer.destroy();

    }


    public void setBarState(boolean b) {
        if (viewModer == null) return;
        if (b) {
            viewModer.bigBar.set(true);
        } else {
            MainActivity.handler.postDelayed(() -> {
                if (!mEmotionKeyboard.isSoftInputShown()) {
                    viewModer.bigBar.set(false);
                }
            }, 200);
        }
    }

    /**
     * 数据操作,这里是测试数据，请自行更换数据
     */
    int[] imgRes = {R.mipmap.a00, R.mipmap.b00, R.mipmap.c00, R.mipmap.d00, R.mipmap.e00, R.mipmap.f00, R.mipmap.g00};

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
        EmotionComplateFragment f1 = (EmotionComplateFragment) factory.getFragment(EmotionUtils.EMOTION_A_TYPE);
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
            case R.id.v_zan:
                if (StringUtil.isEmpty(MyApplication.TOKEN)) {
                    ActivityUtil.startLoginActivity(getActivity());
                } else {
                    postDetail.postViewModel.like();
                }
                break;
            case R.id.iv_share_btn:
                postDetail.share();
                break;
            case R.id.btn_send:
                if (StringUtil.isEmpty(MyApplication.TOKEN)) {
                    startActivity(new Intent(getContext(), LoginFragment.class));
                } else {

                    EditText etEmotion = binding.emotionBar.etEmotion;
                    if (MyTextUtils.isEmpty(etEmotion)) {
                        ToastUtil.toast("请输入评论内容");
                        return;
                    }
                    String context = etEmotion.getText().toString();
                    viewModer.comment(postDetail.getPostId(), context);
                    etEmotion.setText("");
                    isInterceptBackPress();
                }
                break;
        }
    }

    public void loadPostComments() {
        postDetail.postViewModel.loadPostComments(true);
    }
}


