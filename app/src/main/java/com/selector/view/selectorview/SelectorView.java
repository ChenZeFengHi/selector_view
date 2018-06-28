package com.selector.view.selectorview;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * 多级选择器
 *
 * @author Ambit
 * @date 2018/6/22
 */
public class SelectorView extends LinearLayout {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter mAdapter;

    /**
     * TabLayout 高度
     */
    private float tabLayout_height;
    /**
     * 线 选中的颜色
     */
    private int indicator_color;
    /**
     * 文字选中颜色
     */
    private int text_select_color;
    /**
     * 文字未选中颜色
     */
    private int text_unSelect_color;

    private ArrayList<View> views = new ArrayList<>();
    private ArrayList<String> tabs = new ArrayList<>();

    public SelectorView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public SelectorView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public SelectorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SelectorView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        @SuppressLint({"Recycle", "CustomViewStyleable"}) final TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.selector_view, defStyleAttr, 0);
        tabLayout_height = typedArray.getDimension(R.styleable.selector_view_sv_tabLayout_height, 120);
        indicator_color = typedArray.getColor(R.styleable.selector_view_sv_tabLayout_height, 0xFF09C04D);
        text_select_color = typedArray.getColor(R.styleable.selector_view_sv_text_select_color, 0xFF09C04D);
        text_unSelect_color = typedArray.getColor(R.styleable.selector_view_sv_text_unSelect_color, 0xFF333333);

        views.clear();
        tabs.clear();

        tabLayout = new TabLayout(getContext());
        tabLayout.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) tabLayout_height));
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setTabTextColors(text_unSelect_color, text_select_color);
        tabLayout.setSelectedTabIndicatorColor(indicator_color);
        View lineView = new View(getContext());
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 2);
//        layoutParams.setMargins(0, 0, 0, 50);
        lineView.setLayoutParams(layoutParams);
        lineView.setBackgroundColor(0xFFE5E5E5);

        viewPager = new ViewPager(getContext());
        mAdapter = new ViewPagerAdapter(views, tabs);
        viewPager.setAdapter(mAdapter);

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabsFromPagerAdapter(mAdapter);

        setOrientation(VERTICAL);
        addView(tabLayout);
        addView(lineView);
        addView(viewPager);
    }

    private ArrayList<ISelectAble> selectAbles = new ArrayList<>();

    private void addView(final ISelectAble data, final int position) {
        if (dataProvider == null) return;

        dataProvider.provideData(position, new DataProvider.DataReceiver() {
            @Override
            public void send(List<ISelectAble> iSelectAbles) {
                if (iSelectAbles != null && iSelectAbles.size() > 0) {
                    //去除多余的TabView

                    if (tabLayout.getTabCount() > 0 && data.getName().equals(tabLayout.getTabAt(position - 1).getText())) {
                        viewPager.setCurrentItem(position);
                        return;
                    }

                    while (position < tabLayout.getTabCount()) {
                        tabLayout.removeTabAt(tabLayout.getTabCount() - 1);
                    }
                    //去除多余的view和tab
                    while (position < views.size()) {
                        views.remove(views.size() - 1);
                    }
                    while (position < tabs.size()) {
                        tabs.remove(tabs.size() - 1);
                    }
                    while (position < selectAbles.size()) {
                        selectAbles.remove(selectAbles.size() - 1);
                    }
                    if (position > 0) {
                        selectAbles.add(data);
                        //添加选中 有bug！！1
                    }

                    RecyclerView view = new RecyclerView(getContext());
                    LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                    view.setHasFixedSize(true);
                    view.setItemAnimator(null);
                    view.setLayoutManager(manager);
                    MyAdapter adapter;
                    view.setAdapter(adapter = new MyAdapter((Activity) getContext()));
                    adapter.replaceAll(iSelectAbles);
                    adapter.setOnItemClickListener(new MyAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(ISelectAble iSelectAble, int psi) {
                            addView(iSelectAble, position + 1);
                        }
                    });
                    views.add(view);
                    tabs.add("请选择");
                    if (position != 0) {
                        tabs.set(position - 1, data.getName());
                        tabLayout.getTabAt(position - 1).setText(data.getName());
                    }
                    mAdapter.notifyDataSetChanged();
                    tabLayout.addTab(tabLayout.newTab().setText(tabs.get(tabs.size() - 1)));
                    viewPager.setCurrentItem(tabs.size() - 1);
                } else {
                    //次级没有内容，直接回调
                    if (selectedListener != null) {
                        selectedListener.onAddressSelected(selectAbles);
                    }
                }
            }
        });
    }

    private DataProvider dataProvider;

    public void setDataProvider(DataProvider dataProvider) {
        this.dataProvider = dataProvider;
        addView(new SelectModel("请选择"), 0);
    }

    private SelectedListener selectedListener;

    public void setSelectedListener(SelectedListener selectedListener) {
        this.selectedListener = selectedListener;
    }

    public class ViewPagerAdapter extends PagerAdapter {
        private List<View> views;
        private List<String> tabStr;

        ViewPagerAdapter(List<View> views, List<String> tabStr) {
            this.views = views;
            this.tabStr = tabStr;
        }

        @Override
        public int getCount() {
            return views.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            container.addView(views.get(position));
            return views.get(position);
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView(views.get(position));
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabStr.get(position);
        }
    }

    public static class MyAdapter extends RecyclerView.Adapter<MyAdapter.BaseViewHolder> {
        private List<ISelectAble> dataList = new ArrayList<>();
        private Activity activity;

        public MyAdapter(Activity activity) {
            this.activity = activity;
        }

        public void replaceAll(@Nullable List<ISelectAble> list) {
            dataList.clear();
            if (list != null && list.size() > 0) {
                dataList.addAll(list);
            }
            notifyDataSetChanged();
        }


        @NonNull
        @Override
        public MyAdapter.BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new MyAdapter.BaseViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
            holder.setData(dataList.get(position), position);
        }

        @Override
        public int getItemCount() {
            return dataList == null ? 0 : dataList.size();
        }


        class BaseViewHolder extends RecyclerView.ViewHolder {
            private TextView tvName;

            BaseViewHolder(View itemView) {
                super(itemView);
                tvName = itemView.findViewById(R.id.tvName);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (onItemClickListener != null) {
                            onItemClickListener.onItemClick(iSelectAble, position);
                        }
                    }
                });
            }

            private ISelectAble iSelectAble;
            private int position;

            public void setData(Object data, int position) {
                this.iSelectAble = (ISelectAble) data;
                this.position = position;
                tvName.setText(iSelectAble.getName());
            }
        }

        private MyAdapter.OnItemClickListener onItemClickListener;

        public void setOnItemClickListener(MyAdapter.OnItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }

        public interface OnItemClickListener {
            void onItemClick(ISelectAble iSelectAble, int position);
        }
    }
}

