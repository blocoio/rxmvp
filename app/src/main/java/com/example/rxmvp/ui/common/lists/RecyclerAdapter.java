package com.example.rxmvp.ui.common.lists;

import android.content.Context;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import com.jakewharton.rxrelay.PublishRelay;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import rx.Observable;

public class RecyclerAdapter<T, IV extends ItemView<T>>
    extends RecyclerView.Adapter<RecyclerViewHolder<T, IV>> {

  private final Class<IV> itemViewClass;
  private final PublishRelay<T> itemClicks;
  private List<T> adapterList;

  public RecyclerAdapter(Class<IV> itemViewClass) {
    this.itemViewClass = itemViewClass;
    this.adapterList = Collections.emptyList();
    this.itemClicks = PublishRelay.create();
  }

  @Override public RecyclerViewHolder<T, IV> onCreateViewHolder(ViewGroup parent, int viewType) {
    IV itemView;
    try {
      itemView = itemViewClass.getConstructor(Context.class).newInstance(parent.getContext());
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

    final RecyclerViewHolder<T, IV> viewHolder = new RecyclerViewHolder<>(itemView);

    itemView.getClickTarget().setOnClickListener(view -> {
      itemClicks.call(adapterList.get(viewHolder.getAdapterPosition()));
    });
    return viewHolder;
  }

  @Override public void onBindViewHolder(RecyclerViewHolder<T, IV> holder, int position) {
    holder.bind(getItem(position));
    holder.bindSectionTitle(getItemSectionTitle(position));
  }

  @Override public int getItemCount() {
    return adapterList.size();
  }

  public Observable<T> getItemClicks() {
    return itemClicks;
  }

  public void updateItems(final List<T> newItems) {
    if (isDiffUtilEnabled()) {
      RecyclerAdapterDiffUtilCallback callback = new RecyclerAdapterDiffUtilCallback(newItems);
      DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(callback);
      adapterList = new ArrayList<>(newItems);
      diffResult.dispatchUpdatesTo(this);
    } else {
      adapterList = new ArrayList<>(newItems);
      notifyDataSetChanged();
    }
  }

  protected T getItem(int position) {
    return adapterList.get(position);
  }

  protected String getItemSectionTitle(int position) {
    return null;
  }

  protected boolean isDiffUtilEnabled() {
    return true;
  }

  private class RecyclerAdapterDiffUtilCallback extends DiffUtil.Callback {

    private List<T> newItems;

    private RecyclerAdapterDiffUtilCallback(List<T> newItems) {
      this.newItems = newItems;
    }

    @Override public int getOldListSize() {
      return adapterList.size();
    }

    @Override public int getNewListSize() {
      return newItems.size();
    }

    @Override public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
      return adapterList.get(oldItemPosition).equals(newItems.get(newItemPosition));
    }

    @Override public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
      return areItemsTheSame(oldItemPosition, newItemPosition);
    }
  }
}