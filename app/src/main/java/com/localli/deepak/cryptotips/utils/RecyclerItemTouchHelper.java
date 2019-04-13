package com.localli.deepak.cryptotips.utils;

import android.graphics.Canvas;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;
import android.view.View;

import com.localli.deepak.cryptotips.alerts.AlertItemAdapter;
import com.localli.deepak.cryptotips.portfolio.PortfolioItemAdapter;

/**
 * Created by Deepak Prasad on 21-01-2019.
 */

public class RecyclerItemTouchHelper extends ItemTouchHelper.SimpleCallback {

    public static int ITEM_PORTFOLIO =1,
            ITEM_ALERT = 2;

    private RecyclerItemTouchHelperListener listener;
    int ITEM_TYPE;

    public RecyclerItemTouchHelper(int dragDirs, int swipeDirs, RecyclerItemTouchHelperListener listener, int ITEM_TYPE){
        super(dragDirs,swipeDirs);
        this.listener = listener;
        this.ITEM_TYPE = ITEM_TYPE;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
        return true;
    }

    @Override
    public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
        if(viewHolder != null){
            View foregroundView;

            if(ITEM_TYPE==ITEM_PORTFOLIO)
                foregroundView = ( (PortfolioItemAdapter.ViewHolder) viewHolder).foregroundRl;
            else if(ITEM_TYPE == ITEM_ALERT)
                foregroundView = ((AlertItemAdapter.ViewHolder) viewHolder).foregroundRl;
            else
                foregroundView = ( (PortfolioItemAdapter.ViewHolder) viewHolder).foregroundRl;

            getDefaultUIUtil().onSelected(foregroundView);

        }
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        //super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        //final View foregroundView = ((PortfolioItemAdapter.ViewHolder)viewHolder).foregroundRl;

        View foregroundView;

        if(ITEM_TYPE==ITEM_PORTFOLIO)
            foregroundView = ( (PortfolioItemAdapter.ViewHolder) viewHolder).foregroundRl;
        else if(ITEM_TYPE == ITEM_ALERT)
            foregroundView = ((AlertItemAdapter.ViewHolder) viewHolder).foregroundRl;
        else
            foregroundView = ( (PortfolioItemAdapter.ViewHolder) viewHolder).foregroundRl;

        getDefaultUIUtil().onDraw(c,recyclerView,foregroundView,dX,dY,
                actionState,isCurrentlyActive);
    }

    @Override
    public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        //final View foregroundView = ((PortfolioItemAdapter.ViewHolder)viewHolder).foregroundRl;
        View foregroundView;

        if(ITEM_TYPE==ITEM_PORTFOLIO)
            foregroundView = ( (PortfolioItemAdapter.ViewHolder) viewHolder).foregroundRl;
        else if(ITEM_TYPE == ITEM_ALERT)
            foregroundView = ((AlertItemAdapter.ViewHolder) viewHolder).foregroundRl;
        else
            foregroundView = ( (PortfolioItemAdapter.ViewHolder) viewHolder).foregroundRl;

        getDefaultUIUtil().clearView(foregroundView);
    }

    @Override
    public void onChildDrawOver(@NonNull Canvas c, @NonNull RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        //final View foregroundView = ((PortfolioItemAdapter.ViewHolder)viewHolder).foregroundRl;
        View foregroundView;

        if(ITEM_TYPE==ITEM_PORTFOLIO)
            foregroundView = ( (PortfolioItemAdapter.ViewHolder) viewHolder).foregroundRl;
        else if(ITEM_TYPE == ITEM_ALERT)
            foregroundView = ((AlertItemAdapter.ViewHolder) viewHolder).foregroundRl;
        else
            foregroundView = ( (PortfolioItemAdapter.ViewHolder) viewHolder).foregroundRl;

        getDefaultUIUtil().onDrawOver(c,recyclerView,foregroundView,dX,dY,
                actionState,isCurrentlyActive);    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        listener.onSwiped(viewHolder,direction,viewHolder.getAdapterPosition());
    }

    @Override
    public int convertToAbsoluteDirection(int flags, int layoutDirection) {
        return super.convertToAbsoluteDirection(flags, layoutDirection);
    }

    public interface RecyclerItemTouchHelperListener{
        void onSwiped(RecyclerView.ViewHolder viewHolder,int direction, int position);

    }

}
