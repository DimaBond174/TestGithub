package com.bond.testgithub.ui.main;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.arch.lifecycle.ViewModelProviders;

import com.bond.testgithub.R;
import com.bond.testgithub.content.github.GitHubDataManager;
import com.bond.testgithub.i.IRecyclerDataManager;
import com.bond.testgithub.i.ISimpleObserver;
import com.bond.testgithub.objs.RecyclerDataItem;
import com.bond.testgithub.ui.SpecTheme;
import com.bond.testgithub.ui.widgets.WidGithub_for_list;


/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {

  private static final String ARG_SECTION_NUMBER = "section_number";

  private PageViewModel pageViewModel;
  public int index;
  RecyclerView recyclerView = null;
  RVAdapter rvAdapter = null;

  public static PlaceholderFragment newInstance(int index) {
    PlaceholderFragment fragment = new PlaceholderFragment();
    Bundle bundle = new Bundle();
    bundle.putInt(ARG_SECTION_NUMBER, index);
    fragment.setArguments(bundle);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    pageViewModel = ViewModelProviders.of(this).get(PageViewModel.class);
    int index = 1;
    if (getArguments() != null) {
      index = getArguments().getInt(ARG_SECTION_NUMBER);
    }
    pageViewModel.setIndex(index);
    setRetainInstance(true);
  }

  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
//    View root = inflater.inflate(R.layout.fragment_main, container, false);
//    final TextView textView = root.findViewById(R.id.section_label);
//    pageViewModel.getText().observe(this, new Observer<String>() {
//      @Override
//      public void onChanged(@Nullable String s) {
//        textView.setText(s);
//      }
//    });
    View root = inflater.inflate(R.layout.fragment_main, container, false);
    recyclerView = root.findViewById(R.id.section_label);
    recyclerView.setBackgroundColor(SpecTheme.SLightColor);
    LinearLayoutManager layoutManager = new LinearLayoutManager(SpecTheme.context);
    recyclerView.setLayoutManager(layoutManager);
    //View root = new WidRecyclerView(SpecTheme.context);
    iRecyclerDataManager = 1 == pageViewModel.getIndex()? new GitHubDataManager()
        : SpecTheme.iActivity.getLocalDB().getIRecyclerDataManager();
    rvAdapter = new RVAdapter(iRecyclerDataManager);
    recyclerView.setAdapter(rvAdapter);
    return root;
  }
  IRecyclerDataManager iRecyclerDataManager = null;

  @Override
  public void setUserVisibleHint(boolean visible)
  {
    super.setUserVisibleHint(visible);
    if (visible && isResumed())
    {
      //Only manually call onResume if fragment is already visible
      //Otherwise allow natural fragment lifecycle to call onResume
      onResume();
    }
  }

  @Override
  public void onResume() {
    super.onResume();
    if (!getUserVisibleHint())  {
      return;
    }
    if (null != SpecTheme.iActivity) {
      SpecTheme.iActivity.getIUserSettings().getISearchControl()
          .setRecyclerDataManager(iRecyclerDataManager);
    }
    if (null != recyclerView && null != rvAdapter) {
      recyclerView.setAdapter(rvAdapter);
    }

  }

  public class RVAdapter
      extends  RecyclerView.Adapter<RecyclerView.ViewHolder>
      implements ISimpleObserver {
    final IRecyclerDataManager iRecyclerDataManager;
    public RVAdapter(IRecyclerDataManager iRecyclerDataManager) {
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
              iRecyclerDataManager.onClickItem(item.getDataPtr());
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