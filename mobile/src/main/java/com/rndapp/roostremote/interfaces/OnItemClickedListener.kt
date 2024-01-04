package com.rndapp.roostremote.interfaces

import androidx.recyclerview.widget.RecyclerView

interface OnItemClickedListener {
    fun onViewHolderClicked(holder: androidx.recyclerview.widget.RecyclerView.ViewHolder, position: Int)
}

interface OnItemLongClickedListener {
    fun onViewHolderLongClicked(holder: androidx.recyclerview.widget.RecyclerView.ViewHolder, position: Int)
}
