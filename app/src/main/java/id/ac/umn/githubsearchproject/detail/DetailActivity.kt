package id.ac.umn.githubsearchproject.detail

import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import coil.load
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import id.ac.umn.githubsearchproject.R
import id.ac.umn.githubsearchproject.data.local.DbModule
import id.ac.umn.githubsearchproject.data.model.ResponseUser
import id.ac.umn.githubsearchproject.data.model.ResponseUserDetail
import id.ac.umn.githubsearchproject.databinding.ActivityDetailBinding
import id.ac.umn.githubsearchproject.detail.followpackage.FollowFragment
import id.ac.umn.githubsearchproject.util.Result

class DetailActivity : AppCompatActivity() {

    private lateinit var binding:ActivityDetailBinding
    private val viewModel by viewModels<DetailViewModel>{
        DetailViewModel.Factory(DbModule(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val item = intent.getParcelableExtra<ResponseUser.Item>("item")
        val username = item?.login ?: ""

        viewModel.resultUserDetail.observe(this){
            when (it){
                is Result.Success<*> -> {
                    val user = it.data as ResponseUserDetail
                    binding.ivProfile.load(user.avatar_url)
                    binding.tvName.text = user.name
                    binding.tvUsername.text = user.login
                    binding.tvFollowers.text = "${user.followers} Followers"
                    binding.tvFollowing.text = "${user.following} Following"

                }
                is Result.Error -> {
                    Toast.makeText(this, it.exception.message.toString(), Toast.LENGTH_SHORT).show()
                }
                is Result.Loading -> {
                    binding.progressBar.isVisible = it.isLoading
                }
            }
        }
        viewModel.getUserDetail(username)

        val fragments = mutableListOf<Fragment>(
            FollowFragment.newInstance(FollowFragment.FOLLOWER),
            FollowFragment.newInstance(FollowFragment.FOLLOWING)
        )
        val headerFragment = mutableListOf(
            getString(R.string.tab1), getString(R.string.tab2)
        )

        val adapter = DetailAdapter(this, fragments)
        binding.viewPager.adapter = adapter

        TabLayoutMediator(binding.tabs, binding.viewPager){ tabs, position ->
            tabs.text = headerFragment[position]
        }.attach()

        binding.tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tabs : TabLayout.Tab?){
                 if(tabs?.position == 0){
                     viewModel.getFollowers(username)
                 }else{
                     viewModel.getFollowing(username)
                 }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?){

            }
        })
        viewModel.getFollowers(username)

        viewModel.resultFavSuccess.observe(this){
            binding.btnFavorite.changeIconColor(R.color.red)
        }

        viewModel.resultFavDelete.observe(this){
            binding.btnFavorite.changeIconColor(R.color.white)
        }

        binding.btnFavorite.setOnClickListener{
            viewModel.setFavorite(item)
        }

        viewModel.checkFavorite(item?.id ?: 0){
            binding.btnFavorite.changeIconColor(R.color.red)
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

fun FloatingActionButton.changeIconColor(@ColorRes color: Int){
    val color = ContextCompat.getColor(this.context, color)
    imageTintList = ColorStateList.valueOf(color)
}