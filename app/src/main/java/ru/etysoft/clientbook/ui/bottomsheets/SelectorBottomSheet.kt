package ru.etysoft.clientbook.ui.bottomsheets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.etysoft.clientbook.R

class SelectorBottomSheet: BottomSheetDialogFragment() {

    private lateinit var view: View;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v: View = inflater!!.inflate(R.layout.selector_bottom_sheet, container, true)
        view = v
        dialog!!.window!!.attributes.windowAnimations = R.style.DialogAnimation

        initButtons()

        return view
    }

    private fun initButtons() {
        view.findViewById<LinearLayout>(R.id.create_client).setOnClickListener {
            //Start activity for result
            dismiss()
        }

        view.findViewById<LinearLayout>(R.id.create_appointment).setOnClickListener {
            //Start activity for result
            dismiss()
        }
    }

}