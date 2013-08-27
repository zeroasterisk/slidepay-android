package com.slidepay.coresdk;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * Created by Alex on 8/26/13.
 */
public class OrderListFragment extends ListFragment {

    @Override
    public ListAdapter getListAdapter() {
        return super.getListAdapter();
    }

    public OrderListFragment() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
    }

    @Override
    public void setListAdapter(ListAdapter adapter) {
        super.setListAdapter(adapter);
    }

    @Override
    public void setSelection(int position) {
        super.setSelection(position);
    }

    @Override
    public int getSelectedItemPosition() {
        return super.getSelectedItemPosition();
    }

    @Override
    public long getSelectedItemId() {
        return super.getSelectedItemId();
    }

    @Override
    public ListView getListView() {
        return super.getListView();
    }

    @Override
    public void setEmptyText(CharSequence text) {
        super.setEmptyText(text);
    }

    @Override
    public void setListShown(boolean shown) {
        super.setListShown(shown);
    }

    @Override
    public void setListShownNoAnimation(boolean shown) {
        super.setListShownNoAnimation(shown);
    }
}
