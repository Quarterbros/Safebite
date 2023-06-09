package com.example.safebitecapstone.pages

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.example.safebitecapstone.API.ApiConfig
import com.example.safebitecapstone.API.DetectionPost
import com.example.safebitecapstone.API.DetectionResponse
import com.example.safebitecapstone.R
import com.example.safebitecapstone.databinding.FragmentDetectionBinding
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.yalantis.ucrop.UCrop
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.lang.ref.WeakReference
import java.util.*

class DetectionFragment : Fragment() {

    val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    companion object {
        const val CAMERA_X_RESULT = 200
    }

    private val GALLERY_REQUEST_CODE = 1234
    private val WRITE_EXTERNAL_STORAGE_CODE = 1

    lateinit var activityResultLauncher: ActivityResultLauncher<Intent>

    lateinit var finalUri: Uri
    var bitmap: Bitmap? = null

    private lateinit var binding: FragmentDetectionBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetectionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        checkPermission()
        requestPermissionPermit()
        binding.buttonProcess.isEnabled = false

        binding.buttonGallery.setOnClickListener {
            if(checkPermission()){
                pickFromGallery()
            }
            else{
                Toast.makeText(activity, "Allow all permissions before continue", Toast.LENGTH_SHORT).show()
            }
        }

        binding.buttonPhoto.setOnClickListener {
            if(checkPermission()){
                pickFromCamera()
            }
            else{
                Toast.makeText(activity, "Allow all permissions before continue", Toast.LENGTH_SHORT).show()
            }
        }

        binding.buttonDetect.setOnClickListener {
            saveEditedImage()
            processImage()

            binding.progressBar.visibility = View.VISIBLE
            binding.buttonDetect.visibility = View.GONE
            binding.resultScan.visibility = View.GONE


            binding.loadingInformation.visibility = View.VISIBLE
        }


        binding.editText.addTextChangedListener(object  : TextWatcher{
            override fun beforeTextChanged(title: CharSequence?, p1: Int, p2: Int, p3: Int) {


            }

            override fun onTextChanged(title: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (title!!.isNotEmpty()){
                    binding.buttonProcess.isEnabled = true

                    binding.buttonProcess.setOnClickListener {
                        val ingridient = binding.editTextIngridients.text.toString()
                        postDetection(ingridient)
                    }
                }

                else{
                    binding.buttonProcess.isEnabled = false
                }
            }

            override fun afterTextChanged(title: Editable?) {

            }

        })




        binding.buttonCancel.setOnClickListener {
            binding.buttonDetect.visibility = View.GONE
            binding.buttonCancel.visibility = View.GONE

            binding.buttonPhoto.visibility = View.VISIBLE
            binding.buttonGallery.visibility = View.VISIBLE
            binding.resultScan.visibility = View.GONE
            binding.progressBar.visibility = View.GONE
            binding.loadingInformation.visibility = View.GONE
            binding.titleEdittext.visibility = View.GONE
            binding.titleEdittextIngridients.visibility = View.GONE
            binding.editText.visibility = View.GONE
            binding.editTextIngridients.visibility = View.GONE
            binding.photoPlaceholder.setImageResource(R.drawable.img_placeholder)
        }

        binding.buttonCancelProcess.setOnClickListener {
            binding.buttonDetect.visibility = View.GONE
            binding.buttonCancelProcess.visibility = View.GONE


            binding.buttonPhoto.visibility = View.VISIBLE
            binding.buttonGallery.visibility = View.VISIBLE
            binding.buttonProcess.visibility = View.GONE
            binding.resultScan.visibility = View.GONE
            binding.progressBar.visibility = View.GONE
            binding.loadingInformation.visibility = View.GONE
            binding.titleEdittext.visibility = View.GONE
            binding.titleEdittextIngridients.visibility = View.GONE
            binding.editText.visibility = View.GONE
            binding.editTextIngridients.visibility = View.GONE
            binding.titleEdittext.visibility = View.GONE
            binding.titleEdittextIngridients.visibility = View.GONE
            binding.photoPlaceholder.setImageResource(R.drawable.img_placeholder)
        }

        binding.resultScan.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(result: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(result: CharSequence?, p1: Int, p2: Int, p3: Int) {

                if (result?.isEmpty() != true){
                    binding.buttonProcess.isEnabled = false
                    binding.editText.visibility = View.VISIBLE
                    binding.editTextIngridients.visibility = View.VISIBLE
                    binding.titleEdittext.visibility = View.VISIBLE
                    binding.titleEdittextIngridients.visibility = View.VISIBLE
                    binding.progressBar.visibility = View.GONE
                    binding.buttonProcess.visibility = View.VISIBLE
                    binding.buttonCancel.visibility = View.GONE
                    binding.buttonCancelProcess.visibility = View.VISIBLE
                    binding.loadingInformation.visibility = View.GONE

                    binding.editTextIngridients.setText(binding.resultScan.text)
                }
                else{
                    binding.progressBar.visibility = View.VISIBLE
                }
            }
            override fun afterTextChanged(result: Editable?) {
            }
        })

        activityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
                if (result.resultCode == AppCompatActivity.RESULT_OK) {
                    val extras: Bundle? = result.data?.extras
                    val imageUri: Uri
                    val imageBitmap = extras?.get("data") as Bitmap

                    val imageResult: WeakReference<Bitmap> = WeakReference(
                        Bitmap.createScaledBitmap(
                            imageBitmap, imageBitmap.width, imageBitmap.height, false
                        ).copy(
                            Bitmap.Config.RGB_565, true
                        )
                    )
                    val bm = imageResult.get()
                    imageUri = saveImage(bm, requireContext())
                    launchImageCrop(imageUri)
                }
            }
    }


    private fun setImage(uri: Uri?){
        Glide.with(this)
            .load(uri)
            .into(binding.photoPlaceholder)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when(requestCode) {
            WRITE_EXTERNAL_STORAGE_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                }
                else {
                    Toast.makeText(activity, "Enable permissions", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun saveEditedImage() {
        bitmap = MediaStore.Images.Media.getBitmap(activity?.contentResolver, finalUri)
    }


    private fun saveImage(image: Bitmap?, context: Context): Uri {
        val imageFolder= File(context.cacheDir,"images")
        var uri: Uri? = null

        try {
            imageFolder.mkdirs()
            val file: File = File(imageFolder,"captured_image.png")
            val stream: FileOutputStream = FileOutputStream(file)
            image?.compress(Bitmap.CompressFormat.JPEG,100,stream)
            stream.flush()
            stream.close()
            uri= FileProvider.getUriForFile(requireContext().applicationContext,"com.example.safebitecapstone.provider",file)

        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }catch (e: IOException){
            e.printStackTrace()
        }
        return uri!!
    }

    private fun pickFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        val mimeTypes = arrayOf("image/jpeg", "image/png", "image/jpg")
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
        intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        startActivityForResult(intent, GALLERY_REQUEST_CODE)
    }

    private fun pickFromCamera(){
        val intent = Intent(activity, CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }


    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_X_RESULT) {
            val myFile = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.data?.getSerializableExtra("picture", File::class.java)
            } else {
                @Suppress("DEPRECATION")
                it.data?.getSerializableExtra("picture")
            } as? File


            myFile?.let { file ->
                val imageUri: Uri = saveImage(BitmapFactory.decodeFile(file.path), requireContext())
                launchImageCrop(imageUri)
            }
        }
    }
    private fun processImage(){
        if (bitmap!=null) {
            val image = bitmap?.let {
                InputImage.fromBitmap(it, 0)
            }

            image?.let {
                recognizer.process(it)
                    .addOnSuccessListener { visionText ->
                        binding.resultScan.text = visionText.text
                    }
                    .addOnFailureListener { e ->
                    }
            }
        }
        else{
            Toast.makeText(activity, "Please select photo", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            GALLERY_REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    data?.data?.let { uri ->
                        launchImageCrop(uri)
                    }
                }
            }
        }

        if (resultCode == AppCompatActivity.RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            val resultUri :Uri ?= UCrop.getOutput(data!!)
            setImage(resultUri!!)
            finalUri=resultUri
            binding.buttonDetect.visibility = View.VISIBLE
            binding.buttonCancel.visibility = View.VISIBLE
            binding.buttonPhoto.visibility = View.GONE
            binding.buttonGallery.visibility = View.GONE
        }
    }

    private fun launchImageCrop(uri: Uri) {
        val destination:String = StringBuilder(UUID.randomUUID().toString()).toString()
        val options:UCrop.Options = UCrop.Options()

        context?.let {
            UCrop.of(Uri.parse(uri.toString()), Uri.fromFile(File(activity?.cacheDir,destination)))
                .withOptions(options)
                .withAspectRatio(0F, 0F)
                .useSourceImageAspectRatio()
                .withMaxResultSize(2000, 2000)
                .start(it, this)
        }
    }

    private fun checkPermission(): Boolean {
        return context?.let {
            ContextCompat.checkSelfPermission(
                it,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        } == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissionPermit() {
        val listPermission = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
        )
        requestPermissions(listPermission, 100)
    }

    private fun postDetection(text: String) {
        showLoading(true)

        val client = ApiConfig.getApiService().postDetection(DetectionPost(text))
        client.enqueue(object : Callback<DetectionResponse> {
            override fun onResponse(
                call: Call<DetectionResponse>,
                response: Response<DetectionResponse>
            ) {
                showLoading(false)
                val responseBody = response.body()

                if (response.isSuccessful && responseBody != null) {
                    setResultData(responseBody.result)
                } else {
                    Toast.makeText(requireContext(), "Server error, please try again later", Toast.LENGTH_SHORT).show()
                    Log.e(ContentValues.TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<DetectionResponse>, t: Throwable) {
                showLoading(false)
                Log.e(ContentValues.TAG, "onFailure: ${t.message}")
            }
        })
    }

    private fun setResultData(data : com.example.safebitecapstone.API.Result){
        val intent = Intent(activity, DetailScanActivity::class.java)

        val titleScan = binding.editText.text.toString()
        val ingridientEdited = binding.editTextIngridients.text.toString()
        intent.putExtra(DetailScanActivity.EXTRA_TITLE, titleScan)
        intent.putExtra(DetailScanActivity.EXTRA_IMG, finalUri)
        intent.putExtra(DetailScanActivity.EXTRA_HALAL, data.halalHaramPrediction)
        intent.putExtra(DetailScanActivity.EXTRA_ALLERGY, data.allergiesPrediction)
        intent.putExtra(DetailScanActivity.EXTRA_DISEASE, data.diseasesPrediction)
        intent.putExtra(DetailScanActivity.EXTRA_INGRIDIENT, ingridientEdited)

        startActivity(intent)
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}