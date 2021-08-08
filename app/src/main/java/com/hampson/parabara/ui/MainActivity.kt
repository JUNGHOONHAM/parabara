package com.hampson.parabara.ui

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.hampson.parabara.R
import com.hampson.parabara.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var  mBinding : ActivityMainBinding
    public lateinit var context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityMainBinding.inflate(layoutInflater)

        context = this

        setContentView(mBinding.root)

        // 네비게이션들을 담는 호스트
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.my_nav_host) as NavHostFragment

        // 네비게이션 컨트롤러
        val navController = navHostFragment.navController

        // 바텀 네비게이션뷰 와 네비게이션을 묶어준다
        NavigationUI.setupWithNavController(mBinding.bottomNav, navController)
    }
}