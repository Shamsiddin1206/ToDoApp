package com.example.todoapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.todoapp.databinding.FragmentDefaultBinding

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class DefaultFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    lateinit var binding: FragmentDefaultBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDefaultBinding.inflate(inflater, container, false)
        parentFragmentManager.beginTransaction().add(R.id.changeable, TasksFragment()).commit()
        binding.navigationmenu.setOnNavigationItemSelectedListener{
            when(it.itemId){
                R.id.menu_tasks -> {
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.changeable, TasksFragment())
                        .commit()
                }
                R.id.menu_calendar -> {
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.changeable, CalendarFragment())
                        .commit()
                }
                R.id.menu_mine -> {
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.changeable, MineFragment())
                        .commit()
                }
            }
            true
        }

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DefaultFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}