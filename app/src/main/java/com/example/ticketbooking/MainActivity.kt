package com.example.ticketbooking


import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.provider.MediaStore
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.ticketbooking.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*


fun log(msg: String) {
    Log.v("MainACt", msg)
}

var sharedPreference :SharedPreferences? = null

class MainActivity : AppCompatActivity() {

    companion object {
        const val SHARED_PREFERENCE_STRING_SIGN_IN: String = "isUserAlreadyLogged"
    }

    private val fragmentManager = supportFragmentManager
    private lateinit var viewModel:ActivityMainViewModel

    var setSearchQueryListener:(()->Unit)? = null

    lateinit var activityMainBinding: ActivityMainBinding
    lateinit var selectCityTextView:TextView

    private var router:Router? = null



    override fun onResume() {
        super.onResume()
        supportActionBar?.hide()
        scrollAnim()

    }

    override fun onPause() {
        viewModel.scrollAnimJob?.cancel()
        super.onPause()
    }


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
/////
        window.setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED, WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED)
      //////
        sharedPreference = getSharedPreferences("com.example.ticketbooking", MODE_PRIVATE )
        router = Router( applicationContext, fragmentManager )

        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewModel = ViewModelProvider(this)[ActivityMainViewModel::class.java]


        selectCityTextView = findViewById<TextView>(R.id.select_city_text_view)
        selectCityTextView.setOnClickListener {
            router?.showCityPage()
        }


        val isUserAlreadyLogged = sharedPreference?.getBoolean( SHARED_PREFERENCE_STRING_SIGN_IN, false)?:false

        if( isUserAlreadyLogged )
            router?.showMoviePage()
        else
            router?.showSignInPage()
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun scrollAnim(){

        val metrics = DisplayMetrics()
        val density = this.applicationContext.resources.displayMetrics.density
        windowManager.defaultDisplay.getMetrics(metrics)
        val textWidthInPx = Paint().apply{textSize = selectCityTextView.textSize }.measureText(selectCityTextView.text.toString())

        val textViewWidthInPx = (metrics.widthPixels / 5) * 2
        val padValueInPx = 40.0 * density
        val iconWidthInPx = selectCityTextView.compoundDrawables[2].intrinsicWidth * 2.5

        viewModel.textScrollAnim(textWidthInPx,textViewWidthInPx,padValueInPx,iconWidthInPx, selectCityTextView )

    }

}

class DialogFragment:Fragment()
{
    lateinit var bitmapGetter:(Bitmap)->Unit

    private val takePhotoLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->

        if(result.resultCode == Activity.RESULT_OK){
            val extras = result.data?.extras
            val bitmap = extras?.get("data") as Bitmap
            bitmapGetter(bitmap)
        }

    }

    private val getGalleryPhotoLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        Log.v("wdwdw","wefewf")

        if (result.resultCode == Activity.RESULT_OK && result.data != null) {
            val uri = result.data?.data
            val photoBitmap = MediaStore.Images.Media.getBitmap(context?.contentResolver, uri)
            Toast.makeText(context, photoBitmap.toString(), Toast.LENGTH_SHORT).show()
            bitmapGetter(photoBitmap)
        }
        Log.v("wdwdw","wefewf")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.bottom_sheet_profile_picture, null)
        view.setOnClickListener { parentFragmentManager.popBackStack() }

        view.findViewById<TextView>(R.id.capture_image).setOnClickListener {
            val takePhotoIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            takePhotoLauncher.launch(takePhotoIntent)
            (activity as MainActivity).onBackPressed()
        }

        view.findViewById<TextView>(R.id.open_gallery).setOnClickListener {
            val choosePhotoIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            getGalleryPhotoLauncher.launch(choosePhotoIntent)
            (activity as MainActivity).onBackPressed()
        }
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).bottomNavigationView.visibility = View.GONE
    }

    override fun onStart() {
        super.onStart()
        (activity as MainActivity).supportActionBar?.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.black, null)))
    }

    override fun onStop() {
        super.onStop()
        (activity as MainActivity).supportActionBar?.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.app_primary_color, null)))
    }

}