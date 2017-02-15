package com.example.rxmvp.ui.common.lists;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

public class RecyclerViewHolder<T, IV extends ItemView<T>> extends RecyclerView.ViewHolder {

  protected final IV itemView;

  public RecyclerViewHolder(IV itemView) {
    super(itemView);
    this.itemView = itemView;
  }

  public void bind(T item) {
    itemView.setItem(item);
  }

  public void bindSectionTitle(@Nullable String sectionTitle) {
    itemView.setSectionTitle(sectionTitle);
  }

  public IV getItemView() {
    return itemView;
  }
}