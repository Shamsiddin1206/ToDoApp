package com.example.todoapp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.Adapters.CategoryItemAdapter
import com.example.todoapp.Adapters.SingleTaskAdapter
import com.example.todoapp.DataBase.AppDataBase
import com.example.todoapp.DataBase.Entities.Tasks
import com.example.todoapp.databinding.FragmentTasksBinding

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class TasksFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    lateinit var updatedTask: Tasks
    lateinit var path:String
    val appDatabase: AppDataBase by lazy {
        AppDataBase.getInstance(requireContext())
    }
    lateinit var binding: FragmentTasksBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTasksBinding.inflate(inflater, container, false)
        val singleTaskAdapter = SingleTaskAdapter(appDatabase.getDao().getAllTasks() as MutableList<Tasks>, requireContext(), object : SingleTaskAdapter.OnPressed{
            override fun onPressed(tasks: Tasks) {
                parentFragmentManager.beginTransaction().replace(R.id.main, SingleTaskDetailsFragment.newInstance(tasks)).commit()
            }
        })
        binding.tasksListRecycler.adapter = singleTaskAdapter


        val itemTouchHelper = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                singleTaskAdapter.onItemDismiss(viewHolder.adapterPosition)
            }
        }
        val itemTouch = ItemTouchHelper(itemTouchHelper)
        itemTouch.attachToRecyclerView(binding.tasksListRecycler)
        singleTaskAdapter.notifyDataSetChanged()
        binding.tasksListRecycler.adapter = singleTaskAdapter

        val categoryItemAdapter = CategoryItemAdapter(CategoryList(), object : CategoryItemAdapter.OnPressed{
            @SuppressLint("NotifyDataSetChanged")
            override fun onPressed(string: String) {
                if (string == "All"){
                    singleTaskAdapter.list = appDatabase.getDao().getAllTasks() as MutableList<Tasks>
                }else{
                    singleTaskAdapter.list = appDatabase.getDao().getTasksByCategory(string) as MutableList<Tasks>
                }
                singleTaskAdapter.notifyDataSetChanged()
                binding.tasksListRecycler.adapter = singleTaskAdapter
            }

        })
        binding.tasksCategories.adapter = categoryItemAdapter

        binding.addTaskButton.setOnClickListener {
            binding.addTaskMenu.visibility = View.VISIBLE
            binding.DarkBackground.visibility = View.VISIBLE
        }

        binding.addTaskMenu.setOnClickListener {
            binding.addTaskCategoryOptions.visibility = View.GONE
        }

        binding.adTaskDone.setOnClickListener {
            if (!binding.addNewTaskName.text.isNullOrEmpty()){
                appDatabase.getDao().addTask(Tasks(task_name = binding.addNewTaskName.text.toString(),
                    task_category = binding.addTaskCategoryName.text.toString(), mode = false,
                    notificationTime = "21:00", timeData = "9/11/2023"))
                singleTaskAdapter.notifyDataSetChanged()
            }
            binding.addTaskMenu.visibility = View.GONE
            binding.DarkBackground.visibility = View.GONE
            singleTaskAdapter.list = appDatabase.getDao().getAllTasks() as MutableList<Tasks>
            binding.tasksListRecycler.adapter = singleTaskAdapter
        }

        binding.addTaskCategoryName.setOnClickListener {
            binding.addTaskCategoryOptions.visibility = View.VISIBLE
        }

        binding.addTaskNoCategoryOption.setOnClickListener {
            binding.addTaskCategoryOptions.visibility = View.GONE
            binding.addTaskCategoryName.text = binding.addTaskNoCategoryOption.text
        }
        binding.addTaskBirthdayOption.setOnClickListener {
            binding.addTaskCategoryOptions.visibility = View.GONE
            binding.addTaskCategoryName.text = binding.addTaskBirthdayOption.text
        }
        binding.addTaskPersonalOption.setOnClickListener {
            binding.addTaskCategoryOptions.visibility = View.GONE
            binding.addTaskCategoryName.text = binding.addTaskPersonalOption.text
        }
        binding.addTaskWorkOption.setOnClickListener {
            binding.addTaskCategoryOptions.visibility = View.GONE
            binding.addTaskCategoryName.text = binding.addTaskWorkOption.text
        }
        binding.addTaskWishlistOption.setOnClickListener {
            binding.addTaskCategoryOptions.visibility = View.GONE
            binding.addTaskCategoryName.text = binding.addTaskWishlistOption.text
        }

        return binding.root
    }

    fun CategoryList() : MutableList<String> {
        val list = mutableListOf<String>()
        list.add("All")
        list.add("Work")
        list.add("Personal")
        list.add("Wishlist")
        list.add("Birthday")
        return list
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TasksFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}