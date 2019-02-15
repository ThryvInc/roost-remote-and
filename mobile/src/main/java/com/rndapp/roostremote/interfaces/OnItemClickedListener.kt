package com.rndapp.roostremote.interfaces

import android.support.v7.widget.RecyclerView

interface OnItemClickedListener {
    fun onViewHolderClicked(holder: RecyclerView.ViewHolder, position: Int)
}

interface OnItemLongClickedListener {
    fun onViewHolderLongClicked(holder: RecyclerView.ViewHolder, position: Int)
}
