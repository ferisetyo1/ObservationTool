package feri.com.observationtool.main.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import feri.com.observationtool.databinding.FragmentMenuRekamBinding
import feri.com.observationtool.detailrekaman.DetailRekamanActivity
import feri.com.observationtool.main.action.MenuRekamAction

class MenuRekamFragment : Fragment() {

    private lateinit var viewBinding: FragmentMenuRekamBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentMenuRekamBinding.inflate(inflater, container, false).apply {
            action = object : MenuRekamAction {
                override fun onClickMulaiRekam() {
                    startActivity(Intent(activity, DetailRekamanActivity::class.java))
                }
            }
        }

        return viewBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }
}
