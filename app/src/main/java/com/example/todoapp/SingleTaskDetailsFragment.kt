package com.example.todoapp

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import com.example.todoapp.DataBase.AppDataBase
import com.example.todoapp.DataBase.Entities.Tasks
import com.example.todoapp.databinding.FragmentSingleTaskDetailsBinding
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date

private const val ARG_PARAM1 = "param1"

class SingleTaskDetailsFragment : Fragment() {
    private var param1: Tasks? = null
    private lateinit var currentFilePath: String
    private lateinit var img: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getSerializable(ARG_PARAM1) as Tasks
        }
    }


    lateinit var tasks: Tasks
    private val takePhotoResult = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri == null) return@registerForActivityResult
        binding.TaskImage.setImageURI(uri)
        val openInputStream = requireActivity().contentResolver?.openInputStream(uri)
        val file = File(requireActivity().filesDir, "${System.currentTimeMillis()}.jpg")
        val fileOutputStream = FileOutputStream(file)
        openInputStream?.copyTo(fileOutputStream)
        currentFilePath = file.absolutePath
        openInputStream?.close()
    }
    @SuppressLint("SimpleDateFormat")
    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentFilePath = absolutePath
        }
    }
    private fun dispatchTakePictureIntent() {
        val photoFile: File? = try {
            createImageFile()
        } catch (ex: IOException) {
            null
        }

        photoFile?.let {
            val photoURI: Uri = FileProvider.getUriForFile(
                requireContext(),
                "com.example.todoapp",
                it
            )
            takePhotoResultCamera.launch(photoURI)
        }
    }
    private val takePhotoResultCamera = registerForActivityResult(ActivityResultContracts.TakePicture()) {
        if (it) {
            img.setImageURI(Uri.fromFile(File(currentFilePath)))
        }
    }
    private val appDatabase: AppDataBase by lazy {
        AppDataBase.getInstance(requireContext())
    }
    lateinit var binding: FragmentSingleTaskDetailsBinding
    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        tasks = param1!!
        binding = FragmentSingleTaskDetailsBinding.inflate(inflater, container, false)
        img = binding.TaskImage
        if (tasks.filePath.isNotEmpty()){
            currentFilePath = tasks.filePath
            binding.addImage.text = "Change Photo"
            binding.TaskImage.setImageURI(Uri.fromFile(File(currentFilePath)))
        }
        binding.DetailsTaskName.text = param1!!.task_name
        binding.TaskDateInfo.text = param1!!.timeData
        binding.TaskCategoryInfo.text = param1!!.task_category
        binding.TaskNameInfo.text = param1!!.task_name
        binding.addImage.setOnClickListener {
            takePhotoResult.launch("image/*")
            binding.TaskImage.visibility = View.VISIBLE
            binding.addImage.text = "Change Photo"
        }
        binding.Camera.setOnClickListener {
            dispatchTakePictureIntent()
        }
        binding.backToTaskMenu.setOnClickListener {
            if (currentFilePath.isNotEmpty()){
                tasks.filePath = currentFilePath
                appDatabase.getDao().updateTask(tasks)
            }
            parentFragmentManager.beginTransaction().replace(R.id.main, DefaultFragment()).commit()
        }
        binding.EditInfo.setOnClickListener {
            binding.DarkBackgroundChange.visibility = View.VISIBLE
            binding.ChangeTaskMenu.visibility = View.VISIBLE
            binding.ChangeTaskCategoryName.text = tasks.task_category
        }
        binding.ChangeTaskCategoryName.setOnClickListener {
            binding.ChangeTaskCategoryOptions.visibility = View.VISIBLE
        }
        binding.addTaskNoCategoryOption.setOnClickListener {
            binding.ChangeTaskCategoryOptions.visibility = View.GONE
            binding.ChangeTaskCategoryName.text = binding.addTaskNoCategoryOption.text
        }
        binding.addTaskBirthdayOption.setOnClickListener {
            binding.ChangeTaskCategoryOptions.visibility = View.GONE
            binding.ChangeTaskCategoryName.text = binding.addTaskBirthdayOption.text
        }
        binding.addTaskPersonalOption.setOnClickListener {
            binding.ChangeTaskCategoryOptions.visibility = View.GONE
            binding.ChangeTaskCategoryName.text = binding.addTaskPersonalOption.text
        }
        binding.addTaskWorkOption.setOnClickListener {
            binding.ChangeTaskCategoryOptions.visibility = View.GONE
            binding.ChangeTaskCategoryName.text = binding.addTaskWorkOption.text
        }
        binding.addTaskWishlistOption.setOnClickListener {
            binding.ChangeTaskCategoryOptions.visibility = View.GONE
            binding.ChangeTaskCategoryName.text = binding.addTaskWishlistOption.text
        }
        binding.changeCalendarImage.setOnClickListener {
            binding.TaskDateCard.visibility = View.VISIBLE
            binding.ChangeNewTaskName.isClickable = false
            binding.changeTaskDone.isClickable = false
            binding.ChangeTaskCategoryName.isClickable = false
        }
        binding.btnDone.setOnClickListener {
            if (!binding.ChangeTaskDateInfo.text.isNullOrEmpty()){
                tasks.timeData = binding.ChangeTaskDateInfo.text.toString()
            }
            binding.TaskDateCard.visibility = View.GONE
            binding.ChangeNewTaskName.isClickable = true
            binding.changeTaskDone.isClickable = true
            binding.ChangeTaskCategoryName.isClickable = true
        }
        binding.changeTaskDone.setOnClickListener {
            if (!binding.ChangeNewTaskName.text.isNullOrEmpty()){
                tasks.task_name = binding.ChangeNewTaskName.text.toString()
            }
            tasks.task_category = binding.ChangeTaskCategoryName.text.toString()
            appDatabase.getDao().updateTask(tasks)
            binding.DetailsTaskName.text = tasks.task_name
            binding.TaskDateInfo.text = tasks.timeData
            binding.TaskCategoryInfo.text = tasks.task_category
            binding.TaskNameInfo.text = tasks.task_name
            binding.DarkBackgroundChange.visibility = View.GONE
            binding.ChangeTaskMenu.visibility = View.GONE
        }
        return binding.root
    }


    companion object {
        @JvmStatic
        fun newInstance(param1: Tasks) =
            SingleTaskDetailsFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_PARAM1, param1)
                }
            }
    }
}