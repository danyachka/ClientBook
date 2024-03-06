package ru.etysoft.clientbook.ui.adapters

interface ScrollListener<DataHolder> {

    fun onFirstScrolled(dataHolder: DataHolder)

    fun onLastBottomScrolled(dataHolder: DataHolder)
}