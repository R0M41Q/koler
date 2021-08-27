package com.chooloo.www.koler.adapter

import PhoneAccount
import com.chooloo.www.koler.R
import com.chooloo.www.koler.di.boundcomponent.BoundComponentRoot
import com.chooloo.www.koler.ui.widgets.ListItem

class PhonesAdapter(boundComponent: BoundComponentRoot) :
    ListAdapter<PhoneAccount>(boundComponent) {

    private val _blockedDrawable by lazy {
        boundComponent.drawableInteractor.getDrawable(R.drawable.round_block_black_24)
    }


    override fun onBindListItem(listItem: ListItem, item: PhoneAccount) {
        listItem.apply {
            titleText = item.number
            imageVisibility = false

            boundComponent.permissionInteractor.runWithDefaultDialer {
                if (boundComponent.numbersInteractor.isNumberBlocked(item.number)) {
                    imageDrawable = _blockedDrawable
                }
            }
        }
    }
}