package com.ozh.bunchdecorator.lib.decorators.sample

import android.graphics.Canvas
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.ozh.bunchdecorator.lib.decorators.UNDEFINE_VIEW_HOLDER
import com.ozh.bunchdecorator.lib.decorators.layers.LayerDrawer
import com.ozh.bunchdecorator.lib.decorators.round.RoundMode
import com.ozh.bunchdecorator.lib.decorators.round.RoundOutlineProvider

class RoundViewHoldersGroupDrawer(private val cornerRadius: Float) : LayerDrawer() {

    override fun draw(
        canvas: Canvas,
        recyclerView: RecyclerView,
        state: RecyclerView.State,
        view: View,
        viewHolder: RecyclerView.ViewHolder,
        nextViewHolder: RecyclerView.ViewHolder?
    ) {

        val previousChildViewHolder = recyclerView.findViewHolderForAdapterPosition(viewHolder.adapterPosition - 1)

        if (cornerRadius.compareTo(0f) != 0) {
            val roundMode = getRoundMode(previousChildViewHolder, viewHolder, nextViewHolder)
            val outlineProvider = view.outlineProvider
            if(outlineProvider is RoundOutlineProvider) {
                outlineProvider.roundMode = roundMode
                view.invalidateOutline()
            } else {
                view.outlineProvider = RoundOutlineProvider(cornerRadius, roundMode)
                view.clipToOutline = true
            }
        }
    }


    private fun getRoundMode(
        previousChildViewHolder: RecyclerView.ViewHolder?,
        currentViewHolder: RecyclerView.ViewHolder?,
        nextChildViewHolder: RecyclerView.ViewHolder?
    ): RoundMode {

        val previousHolderItemType = previousChildViewHolder?.itemViewType ?: UNDEFINE_VIEW_HOLDER
        val currentHolderItemType = currentViewHolder?.itemViewType ?: UNDEFINE_VIEW_HOLDER
        val nextHolderItemType = nextChildViewHolder?.itemViewType ?: UNDEFINE_VIEW_HOLDER

        return when {
            previousHolderItemType != currentHolderItemType && currentHolderItemType != nextHolderItemType -> RoundMode.ALL
            previousHolderItemType != currentHolderItemType && currentHolderItemType == nextHolderItemType -> RoundMode.TOP
            previousHolderItemType == currentHolderItemType && currentHolderItemType != nextHolderItemType -> RoundMode.BOTTOM
            previousHolderItemType == currentHolderItemType && currentHolderItemType == nextHolderItemType -> RoundMode.NONE
            else -> RoundMode.NONE
        }
    }
}