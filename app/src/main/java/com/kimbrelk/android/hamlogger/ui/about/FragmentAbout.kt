package com.kimbrelk.android.hamlogger.ui.about

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.google.firebase.analytics.FirebaseAnalytics
import com.kimbrelk.android.hamlogger.R
import com.kimbrelk.android.hamlogger.utils.Constants
import kotlinx.android.synthetic.main.frag_about.view.*

class FragmentAbout : Fragment {

    constructor() : super(R.layout.frag_about) {
        setHasOptionsMenu(true)
    }

    override fun onResume() {
        super.onResume()
        requireActivity().title = resources.getString(R.string.screen_about)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) : View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)!!
        view.btn_email.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data = Uri.parse("mailto:")
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf("feedback.karson.kimbrel@gmail.com"))
            intent.putExtra(Intent.EXTRA_SUBJECT, "Amateur Radio Logger App Feedback")
            intent.putExtra(Intent.EXTRA_TEXT, "")
            startActivity(Intent.createChooser(intent, "Send Email"))
            FirebaseAnalytics.getInstance(requireContext()).logEvent(Constants.EventIds.CONTACT_EMAIL, null)
        }
        view.btn_twitter.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("http://www.twitter.com/KimbrelK")
            startActivity(intent)
            FirebaseAnalytics.getInstance(requireContext()).logEvent(Constants.EventIds.CONTACT_TWITTER, null)
        }
        view.btn_my_apps.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://play.google.com/store/apps/developer?id=Karson+Kimbrel")
            startActivity(intent)
            FirebaseAnalytics.getInstance(requireContext()).logEvent(Constants.EventIds.CONTACT_PLAY, null)
        }
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.about, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem) : Boolean {
        when (item.itemId) {
            R.id.action_go_pro -> TODO()
            R.id.action_privacy_policy -> {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse("https://github.com/KarsonKimbrel/PrivacyPolicies/blob/master/hamlogger_privacy.md")
                startActivity(intent)
                FirebaseAnalytics.getInstance(requireContext()).logEvent(Constants.EventIds.TAP_PRIVACY, null )
            }
            R.id.action_rate -> {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse("market://details?id=com.kimbrelk.android.hamlogger")
                startActivity(intent)
                FirebaseAnalytics.getInstance(requireContext()).logEvent(Constants.EventIds.TAP_RATE, null )
            }
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }

}