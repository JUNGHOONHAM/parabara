package com.hampson.parabara.ui.parabox

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.hampson.parabara.databinding.FragmentParaboxBinding

class ParaboxFragment : Fragment() {

    private var mBinding : FragmentParaboxBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentParaboxBinding.inflate(inflater, container, false)



        return mBinding?.root
    }

    override fun onDestroyView() {
        mBinding = null
        super.onDestroyView()
    }
}