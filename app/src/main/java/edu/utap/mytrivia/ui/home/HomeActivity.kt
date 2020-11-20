package edu.utap.mytrivia.ui.home

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import edu.utap.mytrivia.R
import edu.utap.mytrivia.ui.home.fragment.dashboard.viewModel.QuizDashboardViewModel
import edu.utap.mytrivia.ui.home.fragment.home.viewModel.QuizHomeViewModel

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {
    private val navController by lazy {
        supportFragmentManager.findFragmentById(R.id.home_nav_host_fragment)!!.findNavController()
    }
    private val viewModel: QuizDashboardViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        initViews()
    }

    private fun initViews() {

        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_quiz_dashboard
            )
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        viewModel.firebaseUserAuthLiveData.observe(this, {
            if (it == null) {
                navController.navigate(R.id.mainActivity)
                finish()
            }
        })

        navController.addOnDestinationChangedListener { _: NavController, nd: NavDestination, _: Bundle? ->
            if (nd.id == R.id.navigation_home || nd.id == R.id.navigation_quiz_dashboard) {
                navView.visibility = View.VISIBLE
            } else {
                navView.visibility = View.GONE
            }
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.log_out -> {
                signOut()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun signOut() {
        viewModel.signOut()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

}