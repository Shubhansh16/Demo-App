package com.example.demoappt

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.demoappt.databinding.ActivityIntroBinding
import com.example.demoappt.fragments.FirstFragment
import com.example.demoappt.fragments.SecondFragment
import com.example.demoappt.fragments.ThirdFragment
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth


@Suppress("DEPRECATION")
class IntroActivity : Fragment() {

   private lateinit var binding: ActivityIntroBinding
   private lateinit var viewPager: ViewPager2
   private lateinit var auth: FirebaseAuth

   var currentPage=0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        TabLayoutMediator(binding.viewPagerTab, binding.viewPager){ tab, position->}.attach()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = ActivityIntroBinding.inflate(inflater,null,false)
        viewPager= binding.viewPager
        var activity = (activity as FragmentActivity).supportFragmentManager
        val pagerAdapter = ViewPagerAdapter(activity)
        binding.viewPager.adapter = pagerAdapter

        auth= FirebaseAuth.getInstance()
        val user = auth.currentUser

        viewPager.registerOnPageChangeCallback(object :ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int){
                if (position==0){
                    binding.prev.visibility= View.GONE
                    currentPage=0
                }
                else if(position==1){
                    binding.prev.visibility= View.VISIBLE
                    binding.next.text="Next"
                    currentPage=1
                }
                else if(position==2){
                    binding.prev.visibility = View.VISIBLE
                    binding.next.text="Finish"
                    currentPage=2
                }
                super.onPageSelected(position)
            }
        })

        binding.next.setOnClickListener{
            if (currentPage<2){
                currentPage++
                when(currentPage){
                    0->{
                        viewPager.currentItem=0
                    }
                    1->{
                        viewPager.currentItem=1
                        binding.prev.visibility = View.VISIBLE
                    }
                    2->{
                        viewPager.currentItem=2
                        binding.prev.text="Finish"
                        binding.prev.visibility= View.VISIBLE
                    }
                }
            }

            if(currentPage==2){

                Handler().postDelayed({
                    if (user!=null){
                        val discoverIntent= Intent(context, DiscoverActivity::class.java)
                        requireContext().startActivity(discoverIntent)
                    } else {
                        val intent = Intent(context, GoogleActivity::class.java)
                        requireContext().startActivity(intent)
                    }
                },2000)

            }
        }

        return binding.root
    }

    private inner class ViewPagerAdapter(fragmentManager: FragmentManager): FragmentStateAdapter(fragmentManager, lifecycle) {
        override fun getItemCount(): Int {
            return 3
        }

        override fun createFragment(position: Int): Fragment {
            return when(position){
                0-> {
                    FirstFragment()
                }
                1->{
                    SecondFragment()
                }
                else -> {
                    ThirdFragment()
                }
            }
        }
    }
}