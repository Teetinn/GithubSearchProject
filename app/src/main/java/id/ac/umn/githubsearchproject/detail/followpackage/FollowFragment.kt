package id.ac.umn.githubsearchproject.detail.followpackage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import id.ac.umn.githubsearchproject.UserAdapter
import id.ac.umn.githubsearchproject.data.model.ResponseUser
import id.ac.umn.githubsearchproject.databinding.FragmentFollowBinding
import id.ac.umn.githubsearchproject.detail.DetailViewModel
import id.ac.umn.githubsearchproject.util.Result

class FollowFragment : Fragment() {

    private var binding: FragmentFollowBinding ?= null
    private val adapter by lazy{
        UserAdapter{

        }
    }

    private val viewModel by activityViewModels<DetailViewModel>()
    var type = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFollowBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.rvFollow?.apply{
            layoutManager = LinearLayoutManager(requireActivity())
            setHasFixedSize(true)
            adapter = this@FollowFragment.adapter
        }

        when(type){
            FOLLOWER ->{
                viewModel.resultFollowerUser.observe(viewLifecycleOwner, this::manageFollowResult)
            }

            FOLLOWING ->{
                viewModel.resultFollowingUser.observe(viewLifecycleOwner, this::manageFollowResult)
            }
        }
    }

    private fun manageFollowResult(state: Result){
        when (state){
            is Result.Success<*> -> {
                adapter.setData(state.data as MutableList<ResponseUser.Item>)
            }
            is Result.Error -> {
                Toast.makeText(requireActivity(), state.exception.message.toString(), Toast.LENGTH_SHORT).show()
            }
            is Result.Loading -> {
                binding?.progressBar?.isVisible = state.isLoading
            }
        }
    }

    companion object {
        const val FOLLOWING = 200
        const val FOLLOWER = 202
        fun newInstance(type: Int) = FollowFragment()
            .apply{
                this.type = type
            }
    }
}