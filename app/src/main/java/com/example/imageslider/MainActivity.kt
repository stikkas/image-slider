package com.example.imageslider

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.imageslider.databinding.ActivityMainBinding
import com.example.imageslider.model.slides

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private var viewPager: ViewPager2? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        viewPager = binding.viewpager
        viewPager?.let { vp ->
            vp.adapter = PagerAdapter(this, slides)
            binding.dotsIndicator.attachTo(vp)
            vp.clipToPadding = false
            vp.clipChildren = false
            vp.offscreenPageLimit = 2
            val pageMarginPx = resources.getDimensionPixelOffset(R.dimen.pageMargin)
            val offsetPx = resources.getDimensionPixelOffset(R.dimen.offset)
            vp.setPageTransformer { page, pos ->
                val viewPager = page.parent.parent as ViewPager2
                val offset = pos * -(2 * offsetPx + pageMarginPx)
                if (viewPager.orientation == ViewPager2.ORIENTATION_HORIZONTAL) {
                    if (ViewCompat.getLayoutDirection(viewPager) == ViewCompat.LAYOUT_DIRECTION_RTL) {
                        page.translationX = -offset
                    } else {
                        page.translationX = offset
                    }
                } else {
                    page.translationY = offset
                }
            }
            vp.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    if (position == 0) {
                        binding.buttonPrev.isEnabled = false
                    } else {
                        binding.buttonPrev.isEnabled = true
                        binding.buttonPrev.setOnClickListener {
                            vp.setCurrentItem(vp.currentItem - 1, false)
                        }
                    }

                    if (position == slides.size - 1) {
                        binding.buttonNext.text = "Finish"
                        binding.buttonNext.setOnClickListener {
                            vp.currentItem = 0
                        }
                    } else {
                        binding.buttonNext.text = "Next"
                        binding.buttonNext.setOnClickListener {
                            vp.setCurrentItem(vp.currentItem + 1, false)
                        }
                    }

                    sliderHandler.removeCallbacks(sliderRunnable)
                    sliderHandler.postDelayed(sliderRunnable, 2000)
                }
            })
        }
    }

    private val sliderHandler = Handler(Looper.getMainLooper())
    private val sliderRunnable = Runnable {
        viewPager?.let {
            if (it.currentItem == slides.size - 1) {
                it.currentItem = 0
            } else {
                it.currentItem += 1
            }
        }
    }

    override fun onPause() {
        super.onPause()
        sliderHandler.removeCallbacks(sliderRunnable)
    }

    override fun onResume() {
        super.onResume()
        sliderHandler.postDelayed(sliderRunnable, 2000)
    }
}