package me.gauravbordoloi.expensereport.tag

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.dialog_tag.*
import me.gauravbordoloi.expensereport.R

class TagDialog(ctx: Context, private val defaultTag: String?, val callback: (String?) -> Unit) :
    Dialog(ctx) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_tag)
        window?.setBackgroundDrawableResource(R.color.light_transparent)
        window?.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        setCancelable(false)
        setCanceledOnTouchOutside(false)

        defaultTag?.let {
            inputTag.editText?.setText(it)
            btnRemove.visibility = View.VISIBLE
        }

        btnSave.setOnClickListener {
            val tag = inputTag.editText?.text.toString().trim()
            if (tag.isEmpty()) {
                Snackbar.make(it, "Tag cannot be empty", Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            callback(tag)
            dismiss()
        }

        btnRemove.setOnClickListener {
            callback(null)
            dismiss()
        }

        btnClose.setOnClickListener {
            dismiss()
        }

    }

}