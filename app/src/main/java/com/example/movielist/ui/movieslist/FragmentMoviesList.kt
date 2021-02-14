package com.example.movielist.ui.movieslist

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.movielist.MovieClickListener
import com.example.movielist.R
import com.example.movielist.adapters.MovieListAdapter
import com.example.movielist.adapters.MovieListItemDecoration
import com.example.movielist.data.models.Movie
import com.example.movielist.databinding.FragmentMoviesListBinding



class FragmentMoviesList : Fragment() {

    lateinit var viewModel: MoviesListViewModel

    private var _binding: FragmentMoviesListBinding? = null
    private val binding get() = _binding!!
    private var listenerMovie: MovieClickListener? = null

    lateinit var moviesAdapter: MovieListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMoviesListBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(MoviesListViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        viewModel.moviesList.observe(this.viewLifecycleOwner, this::updateAdapter)
        viewModel.loadingState.observe(this.viewLifecycleOwner, this::setLoading)

        viewModel.load()
    }

    private fun setLoading(state: Status) {
        when (state) {
            Status.FIRST_LOADING -> {
                binding.progressBar.visibility = View.VISIBLE
                binding.movieListRv.visibility = View.INVISIBLE
            }
            Status.DONE -> {
                binding.progressBar.visibility = View.GONE
                binding.movieListRv.visibility = View.VISIBLE
            }
        }
    }

    private fun updateAdapter(movies: List<Movie>) {
        moviesAdapter.bindMovies(movies)
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (activity is MovieClickListener) {
            this.listenerMovie = activity as MovieClickListener
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

    private fun setupRecyclerView() {
        if (listenerMovie != null) {
            moviesAdapter = MovieListAdapter(listenerMovie!!)
        } else {
            throw IllegalArgumentException("No listener")
        }

        binding.movieListRv.apply {
            adapter = moviesAdapter
            addItemDecoration(
                MovieListItemDecoration(resources.getDimension(R.dimen.margin_6).toInt())
            )
        }
    }
}

