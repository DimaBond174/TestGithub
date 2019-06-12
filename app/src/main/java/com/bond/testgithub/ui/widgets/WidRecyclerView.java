package com.bond.testgithub.ui.widgets;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.bond.testgithub.i.IRecyclerDataManager;
import com.bond.testgithub.i.ISimpleObserver;
import com.bond.testgithub.objs.RecyclerDataItem;
import com.bond.testgithub.ui.SpecTheme;


public class WidRecyclerView extends FrameLayout {
    RecyclerView recyclerView;
    RVAdapter rvAdapter;

    public void setIRecyclerDataManager(IRecyclerDataManager iRecyclerDataManager) {
        rvAdapter.setIRecyclerDataManager(iRecyclerDataManager);
    }

    public WidRecyclerView(Context context) {
        super(context);
        createViews(context);
    }

    public WidRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        createViews(context);
    }

    public WidRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        createViews(context);
    }

    void createViews(Context  context) {
        rvAdapter = new RVAdapter();
        recyclerView = new RecyclerView(context);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setAdapter(rvAdapter);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setVerticalScrollBarEnabled(true);
        addView(recyclerView,
            new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width     = MeasureSpec.getSize(widthMeasureSpec);
        int height     = MeasureSpec.getSize(heightMeasureSpec);
        measureChildWithMargins(recyclerView,
            widthMeasureSpec, 0,
            heightMeasureSpec, 0);
        /* Скажем наверх насколько мы большие */
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        //super.onLayout(changed, left, top, right, bottom);
        recyclerView.layout(0, 0,
            right - left, bottom - top      );
    }

    public class RVAdapter
            extends  RecyclerView.Adapter<RecyclerView.ViewHolder>
            implements  ISimpleObserver {

        IRecyclerDataManager iRecyclerDataManager = null;
        public void setIRecyclerDataManager(IRecyclerDataManager iRecyclerDataManager) {
            this.iRecyclerDataManager = iRecyclerDataManager;
            iRecyclerDataManager.registerSimpleObserver(this);
        }

        @Override
        public void onSimpleChange() {
            notifyDataSetChanged();
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            //Log.d(TAG, "Element " + position + " set.");
            ViewHolder hldr = ((ViewHolder) holder);

            if (null==hldr.chatMsg) { return;}
            WidGithub_for_list item = hldr.chatMsg;
            item.setHighlighted(0);
            try {
                RecyclerDataItem data = null;
                if (null != iRecyclerDataManager) {
                    data = iRecyclerDataManager.getItemAtPos(position);
                }
                item.setData(data);
            } catch (Exception e) {}
        }


        /**
         * Provide a reference to the type of views that you are using (custom ViewHolder)
         */
        public class ViewHolder extends RecyclerView.ViewHolder {
            public WidGithub_for_list chatMsg;

            public ViewHolder(WidGithub_for_list v) {
                super(v);
                chatMsg = v;
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (null  !=  iRecyclerDataManager) {
                            WidGithub_for_list item = ((WidGithub_for_list)v);
                            iRecyclerDataManager.onClickItem(item.dataPtr);
                        }
                    }
                });
            }
        }//viewHolder


        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            WidGithub_for_list view =
                new WidGithub_for_list(SpecTheme.context);
            RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(
                RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT);
            view.setLayoutParams(params);
            return new ViewHolder(view);
        }


        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            int  re  =  0;
            if (null  !=  iRecyclerDataManager) {
                re  =  iRecyclerDataManager.getItemCount();
            }
            return re;
        }

    }

}
