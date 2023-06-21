package id.ac.umn.githubsearchproject.settings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import id.ac.umn.githubsearchproject.data.local.SettingPreferences
import id.ac.umn.githubsearchproject.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private val viewModel by viewModels<SettingsViewModel> {
        SettingsViewModel.Factory(SettingPreferences(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        viewModel.getMode().observe(this){
            if(it){
                binding.switchMode.text = "Dark Mode"
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }else{
                binding.switchMode.text = "Light Mode"
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            binding.switchMode.isChecked = it
        }

        binding.switchMode.setOnCheckedChangeListener{ _, isChecked ->
            viewModel.saveMode(isChecked)
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