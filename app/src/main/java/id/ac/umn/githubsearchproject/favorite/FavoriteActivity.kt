package id.ac.umn.githubsearchproject.favorite

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import id.ac.umn.githubsearchproject.UserAdapter
import id.ac.umn.githubsearchproject.data.local.DbModule
import id.ac.umn.githubsearchproject.databinding.ActivityFavoriteBinding
import id.ac.umn.githubsearchproject.detail.DetailActivity

class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding:ActivityFavoriteBinding

    private val adapter by lazy {
        UserAdapter{ user ->
            Intent(this, DetailActivity::class.java).apply{
                putExtra("item", user)
                startActivity(this)
            }
        }
    }

    private val viewModel by viewModels<FavoriteViewModel>{
        FavoriteViewModel.Factory(DbModule(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.rvFavorite.layoutManager = LinearLayoutManager(this)
        binding.rvFavorite.adapter = adapter


    }

    override fun onResume(){
        super.onResume()
        viewModel.getUserFavorite().observe(this){
            adapter.setData(it)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}