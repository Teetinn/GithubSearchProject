package id.ac.umn.githubsearchproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import id.ac.umn.githubsearchproject.data.local.SettingPreferences
import id.ac.umn.githubsearchproject.data.model.ResponseUser
import id.ac.umn.githubsearchproject.databinding.ActivityMainBinding
import id.ac.umn.githubsearchproject.detail.DetailActivity
import id.ac.umn.githubsearchproject.favorite.FavoriteActivity
import id.ac.umn.githubsearchproject.settings.SettingsActivity
import id.ac.umn.githubsearchproject.util.Result

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val adapter by lazy {
        UserAdapter{ user ->
            Intent(this, DetailActivity::class.java).apply{
                putExtra("item", user)
                startActivity(this)
            }
        }
    }

    private val viewModel by viewModels<MainViewModel>{
        MainViewModel.Factory(SettingPreferences(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        viewModel.getMode().observe(this){
            if(it){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }else{
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        binding.rvUser.layoutManager = LinearLayoutManager(this)
        binding.rvUser.setHasFixedSize(true)
        binding.rvUser.adapter = adapter

        binding.searchBar.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.getUser(query.toString())
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean = false
        })

        viewModel.resultUser.observe(this){
            when (it){
                is Result.Success<*> -> {
                    adapter.setData(it.data as MutableList<ResponseUser.Item>)
                }
                is Result.Error -> {
                    Toast.makeText(this, it.exception.message.toString(), Toast.LENGTH_SHORT).show()
                }
                is Result.Loading -> {
                    binding.progressBar.isVisible = it.isLoading
                }
            }
        }
        viewModel.getUser()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_item, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.favorite ->{
                Intent(this, FavoriteActivity::class.java).apply{
                    startActivity(this)
                }
            }
            R.id.settings -> {
                Intent(this, SettingsActivity::class.java).apply {
                    startActivity(this)
                }
            }
        }

        return super.onOptionsItemSelected(item)
    }
}