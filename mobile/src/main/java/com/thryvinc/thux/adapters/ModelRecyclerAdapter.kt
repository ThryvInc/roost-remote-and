package com.thryvinc.thux.adapters

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.thryvinc.thux.models.OnBoundProvider

interface RecyclerItemViewModelInterface {
    fun configureHolder(holder: androidx.recyclerview.widget.RecyclerView.ViewHolder)
    fun newViewHolder(parent: ViewGroup): androidx.recyclerview.widget.RecyclerView.ViewHolder
    fun viewType(): Int
}

abstract class RecyclerItemViewModel<T>(val model: T): RecyclerItemViewModelInterface

abstract class ModelViewHolder<T>(itemView: View): androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
    abstract fun configure(model: T)
}

abstract class LayoutIdRecyclerItemViewModel<T>(model: T, val layoutId: Int): RecyclerItemViewModel<T>(model) {
    override fun viewType(): Int = layoutId

    override fun newViewHolder(parent: ViewGroup): androidx.recyclerview.widget.RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(layoutId, parent, false)
        return viewHolderWithView(view)
    }

    abstract fun viewHolderWithView(view: View): ModelViewHolder<T>
}

open class ModelRecyclerViewAdapter(var itemViewModels: List<RecyclerItemViewModelInterface>): androidx.recyclerview.widget.RecyclerView.Adapter<androidx.recyclerview.widget.RecyclerView.ViewHolder>() {
    override fun getItemCount(): Int {
        return itemViewModels.size
    }

    override fun getItemViewType(position: Int): Int {
        return itemViewModels[position].viewType()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): androidx.recyclerview.widget.RecyclerView.ViewHolder {
        return itemViewModels.first { it.viewType() == viewType }.newViewHolder(parent)
    }

    override fun onBindViewHolder(holder: androidx.recyclerview.widget.RecyclerView.ViewHolder, position: Int) {
        val itemViewModel = itemViewModels[position]
        itemViewModel.configureHolder(holder)
    }
}

abstract class OnBoundAdapter(itemViewModels: List<RecyclerItemViewModelInterface>): ModelRecyclerViewAdapter(itemViewModels), OnBoundProvider

open class OnBoundModelRecyclerViewAdapter(itemViewModels: List<RecyclerItemViewModelInterface>): OnBoundAdapter(itemViewModels) {
    var _onBound: ((Int, Int) -> Unit)? = null
    override var onBound: ((Int, Int) -> Unit)?
        get() = _onBound
        set(value) {
            _onBound = value
        }

    override fun onBindViewHolder(holder: androidx.recyclerview.widget.RecyclerView.ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        val onBound = _onBound
        if (onBound != null) {
            onBound(position, itemViewModels.size)
        }
    }

}
