package me.gauravbordoloi.expensereport.util

import android.content.*
import android.net.Uri
import timber.log.Timber

object IntentUtil {

    private val TAG = javaClass.simpleName

    fun openLink(context: Context?, url: String) {
        if (context == null) {
            return
        }
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        if (intent.resolveActivity(context.packageManager) != null) {
            context.startActivity(intent)
        }
    }

    fun shareWhatsApp(context: Context?, text: String) {
        try {
            val sendIntent = Intent()
            sendIntent.action = Intent.ACTION_SEND
            sendIntent.putExtra(Intent.EXTRA_TEXT, text)
            sendIntent.type = "text/plain"
            sendIntent.setPackage("com.whatsapp");
            context?.startActivity(sendIntent)
        } catch (e: ActivityNotFoundException) {
            e.printStackTrace()
            Timber.e(e, TAG, "shareWhatsApp")
        }
    }

    fun shareTwitter(context: Context?, text: String) {
        try {
            val sendIntent = Intent()
            sendIntent.action = Intent.ACTION_SEND
            sendIntent.putExtra(Intent.EXTRA_TEXT, text)
            sendIntent.type = "text/plain"
            sendIntent.setPackage("com.twitter.android");
            context?.startActivity(sendIntent)
        } catch (e: ActivityNotFoundException) {
            e.printStackTrace()
            Timber.e(e, TAG, "shareWhatsApp")
        }
    }

    fun shareFacebook(context: Context?, text: String) {
        try {
            val sendIntent = Intent()
            sendIntent.action = Intent.ACTION_SEND
            sendIntent.putExtra(Intent.EXTRA_TEXT, text)
            sendIntent.type = "text/plain"
            sendIntent.setPackage("com.facebook.katana");
            context?.startActivity(sendIntent)
        } catch (e: ActivityNotFoundException) {
            e.printStackTrace()
            Timber.e(e, TAG, "shareWhatsApp")
        }
    }

    fun copyText(context: Context?, text: String) {
        val clipboard = context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?
        val clip = ClipData.newPlainText("product share", text)
        clipboard?.setPrimaryClip(clip)
    }

    fun sendEmail(context: Context?, email: String, subject: String, text: String) {
        if (context == null) {
            return
        }
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, text)
        }
        if (intent.resolveActivity(context.packageManager) != null) {
            context.startActivity(intent)
        }
    }

    fun callPhone(context: Context?, phone: String) {
        if (context == null) {
            return
        }
        val intent = Intent(Intent.ACTION_CALL).apply {
            data = Uri.parse("tel:$phone")
        }
        if (intent.resolveActivity(context.packageManager) != null) {
            context.startActivity(intent)
        }
    }

}