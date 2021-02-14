package com.example.movielist

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.movielist.adapters.MovieListAdapter
import com.example.movielist.adapters.MovieListItemDecoration
import com.example.movielist.data.model.loadMovies
import com.example.movielist.databinding.FragmentMoviesListBinding
import kotlinx.coroutines.launch


class FragmentMoviesList: Fragment() {

    private var _binding: FragmentMoviesListBinding? = null
    private val binding get() = _binding!!
    private var listenerMovie: MovieClickListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?{
        _binding = FragmentMoviesListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.movieListRv.addItemDecoration(
            MovieListItemDecoration(
                resources.getDimension(R.dimen.margin_6).toInt())
        )

        val adapter = listenerMovie?.let { MovieListAdapter(it) }
        binding.movieListRv.adapter = adapter
        lifecycleScope.launch {
            val movieList = context?.let { loadMovies(it) }
           adapter?.bindMovies(movieList)
            binding.progressBar.visibility = View.GONE
            binding.movieListRv.visibility = View.VISIBLE
        }
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (activity is MovieClickListener) {
            this.listenerMovie = activity as MovieClickListener
        }
        else {
            throw IllegalArgumentException("Activity must implement MovieClickListener")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onDetach() {
        super.onDetach()
        listenerMovie = null
    }
}
