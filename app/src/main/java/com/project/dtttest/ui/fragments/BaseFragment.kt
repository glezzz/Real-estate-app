package com.project.dtttest.ui.fragments

import androidx.fragment.app.Fragment
import com.project.dtttest.ui.activities.MainActivity

open class BaseFragment : Fragment() {
    /**
     * Marker interface for fragments.
     * When implemented by child fragment class means Bottom Navigation View should be hidden
     */
    interface HideNavigationBar

    override fun onStart() {
        super.onStart()
        (requireActivity() as MainActivity).hideBottomNavView(this is HideNavigationBar)
    }

}