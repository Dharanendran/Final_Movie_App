package com.example.ticketbooking.signup.presentation

import android.app.Activity.RESULT_OK
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.os.Bundle
import android.provider.MediaStore
import android.text.Spannable
import android.text.SpannableString
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.load.engine.Resource
import com.example.ticketbooking.R
import com.example.ticketbooking.dependencyInjection.DependencyFactory
import com.example.ticketbooking.MainActivity
import com.example.ticketbooking.databinding.FragmentSignUpBinding
import com.example.ticketbooking.movie.presentation.MoviePageFragment
import com.example.ticketbooking.signIn.presentation.SignInPageFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetDialog

class SignUpPageFragment : Fragment(), SignUpPageViewModel.ToastMaker {

    private lateinit var fragmentSignUpBinding: FragmentSignUpBinding
    private lateinit var viewModel: SignUpPageViewModel
    private lateinit var activityContext: Context

    private lateinit var actionBarTitle: String

    interface Navigator{
        fun submitOnclickNavigate()
        fun onClickProfilePictureNavigate():Bitmap?
    }

    var navigator:Navigator? = null

    override fun onResume() {

        (activity as MainActivity).let {
            it.activityMainBinding.headerLayout.visibility = View.GONE
            it.activityMainBinding.bottomNavigationView.visibility = View.GONE
            it.supportActionBar?.show()
            actionBarTitle = it.supportActionBar?.title.toString()
            it.supportActionBar?.title = "New Account"
            it.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        super.onResume()

    }

    override fun onPause() {
        (activity as MainActivity).window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        super.onPause()

    }
    override fun onStop() {

        (activity as MainActivity).let {
            it.supportActionBar?.title = actionBarTitle
            it.supportActionBar?.hide()
            it.activityMainBinding.bottomNavigationView.visibility = View.VISIBLE
        }
        super.onStop()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        activityContext = activity?.applicationContext as Context

        viewModel = ViewModelProvider(activity as MainActivity)[SignUpPageViewModel::class.java].apply {

                toastMaker = this@SignUpPageFragment

                userNameExistCheckingUseCase = DependencyFactory.getInstance(activityContext).let {
                    it.getUserNameExistCheckingUseCase(it.getIsUserNameExistRepositoryImpl())
                }

                createUserAccountUseCase = DependencyFactory.getInstance(activityContext).let {
                    it.getCreateUserAccountUseCase(it.getCreateUserAccountRepositoryImpl())
                }

                validationUseCase = DependencyFactory.getInstance(activityContext).getValidationUseCase()

            }



        fragmentSignUpBinding = DataBindingUtil.inflate<FragmentSignUpBinding?>(
            inflater,
            R.layout.fragment_sign_up,
            container,
            false
        ).apply {
            viewModel = this@SignUpPageFragment.viewModel
            lifecycleOwner = this@SignUpPageFragment
        }

        return fragmentSignUpBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.profilePicture.observe(this.viewLifecycleOwner, Observer {
            it?.let { fragmentSignUpBinding.profileImageView.setImageBitmap(it) }
        })

        fragmentSignUpBinding.cameraImageView.setOnClickListener {
            viewModel.profilePicture.value = navigator?.onClickProfilePictureNavigate()

        }

        viewModel.isAccountCreated().observe(viewLifecycleOwner) { isCreated ->
            if (isCreated)
                navigator?.submitOnclickNavigate()
        }
    }


    override fun makeToast(message: String) { // also we can use live data for that
        Toast.makeText(activityContext, message, Toast.LENGTH_SHORT).show()
    }


//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (resultCode == RESULT_OK) {
//            when (requestCode) {
//                0 -> {
//                    val imageBitmap = data?.extras?.get("data") as Bitmap
//
//                }
//                1 -> {
//                    val imageUri = data?.data
//                    val inputStream = activityContext.contentResolver.openInputStream(imageUri!!)
//                    val imageBitmap = BitmapFactory.decodeStream(inputStream)
//                    viewModel.profilePicture.value = imageBitmap
//                    // Use the imageBitmap as needed, for example setting it to an ImageView
////                    imageView.setImageBitmap(imageBitmap)
//                }
//            }
//        }
//    }

}


