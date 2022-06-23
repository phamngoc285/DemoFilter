package com.ngocpv.demofilter

import android.R.attr
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.namangarg.androiddocumentscannerandfilter.DocumentFilter
import com.ngocpv.demofilter.databinding.FragmentFirstBinding
import java.io.InputStream


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {
    private val MY_CAMERA_PERMISSION_CODE = 1001
    private val CAMERA_REQUEST = 101
    private val PICK_IMAGE = 102
    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.takePhoto.setOnClickListener {
//            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                if (checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//                    requestPermissions(
//                        arrayOf(Manifest.permission.CAMERA),
//                        MY_CAMERA_PERMISSION_CODE
//                    )
//                } else {
//                    val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//                    startActivityForResult(cameraIntent, CAMERA_REQUEST)
//                }
//            }
            val getIntent = Intent(Intent.ACTION_GET_CONTENT)
            getIntent.type = "image/*"

            val pickIntent =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            pickIntent.type = "image/*"

            val chooserIntent = Intent.createChooser(getIntent, "Select Image")
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(pickIntent))

            startActivityForResult(chooserIntent, PICK_IMAGE)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
//            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
////                Toast.makeText(requireContext(), "camera permission granted", Toast.LENGTH_LONG).show()
////                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
////                startActivityForResult(cameraIntent, CAMERA_REQUEST)
//                val intent = Intent()
//                intent.type = "image/*"
//                intent.action = Intent.ACTION_GET_CONTENT
//                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE)
//            } else {
//                Toast.makeText(requireContext(), "camera permission denied", Toast.LENGTH_LONG).show()
//            }
//        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode === PICK_IMAGE && resultCode === Activity.RESULT_OK) {
            if (data != null) {
                //Now you can do whatever you want with your inpustream, save it as file, upload to a server, decode a bitmap...
            }
            data?.data?.let { uri ->
                val inputStream: InputStream? = requireContext().contentResolver.openInputStream(uri)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                binding.originalImage.setImageBitmap(bitmap)
                binding.originalImage.visibility = View.VISIBLE
                val documentFilter = DocumentFilter()
                documentFilter.getShadowRemoval(bitmap) {
                  // Do your tasks here with the returned bitmap
                    binding.filteredImage.setImageBitmap(it)
                    binding.filteredImage.visibility = View.VISIBLE
                 }
            }
        } else if (requestCode == CAMERA_REQUEST && resultCode == AppCompatActivity.RESULT_OK) {
            val photo = data?.extras!!["data"] as Bitmap?
            binding.originalImage.setImageBitmap(photo)
            binding.originalImage.visibility = View.VISIBLE

            val documentFilter = DocumentFilter()
            // replace getFilter_Name with the filter you want to use

//            // replace getFilter_Name with the filter you want to use
//            documentFilter.getFilter_Name(photo, object : CallBack<Bitmap?>() {
//                fun onCompleted(bitmap: Bitmap?) {
//                    // Do your tasks here with the returned bitmap
//                }
//            })

//            documentFilter.getShadowRemoval(photo) {
//                // Do your tasks here with the returned bitmap
//                binding.originalImage.setImageBitmap()
//                binding.originalImage.visibility = View.VISIBLE
//            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}