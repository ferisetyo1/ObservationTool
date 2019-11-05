package feri.com.observationtool.main.fragment


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import feri.com.observationtool.databinding.FragmentMenuListKelasBinding
import feri.com.observationtool.main.action.MenuListKelasAction
import feri.com.observationtool.tambahkelas.KelasActivity

class MenuListKelasFragment : Fragment() {

    private lateinit var viewBinding: FragmentMenuListKelasBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentMenuListKelasBinding.inflate(inflater, container, false)
            .apply {
            action = object : MenuListKelasAction {
                override fun OnClickTambahKelas() {
                    startActivity(Intent(activity,KelasActivity::class.java))
                }
            }
        }
        return viewBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }


}
