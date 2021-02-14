package com.example.movielist.ui.moviesdetails

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.movielist.MovieClickListener
import com.example.movielist.R
import com.example.movielist.adapters.ActorListAdapter
import com.example.movielist.adapters.ActorListItemDecorator
import com.example.movielist.data.model.Movie
import com.example.movielist.databinding.FragmentMoviesDetailsBinding
import java.lang.IllegalArgumentException
import kotlin.properties.Delegates

const val MOVIE_ID_ARG = "Movie"

class FragmentMoviesDetails : Fragment() {

    private var movieId by Delegates.notNull<Int>()
    private val viewModel: MoviesDetailsViewModel by lazy {
        ViewModelProvider(
            this,
            MoviesDetailsViewModelFactory(requireActivity().application, movieId)
        ).get(MoviesDetailsViewModel::class.java)
    }

    private var _binding: FragmentMoviesDetailsBinding? = null
    private val binding get() = _binding!!
    private var listenerMovie: MovieClickListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMoviesDetailsBinding.inflate(inflater, container, false)

        val bundle: Bundle? = this.arguments
        movieId =
            bundle?.getInt(MOVIE_ID_ARG) ?: throw IllegalArgumentException("Couldn't find movieId")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.back.setOnClickListener {
            listenerMovie?.backPressed()
        }

        binding.actorsRv.addItemDecoration(
            ActorListItemDecorator(
                resources.getDimension(R.dimen.margin_8).toInt()
            )
        )

        viewModel.currentMovie.observe(this.viewLifecycleOwner, this::bindMovie)
        viewModel.loadingState.observe(this.viewLifecycleOwner, this::setLoading)

    }

    private fun bindMovie(movie: Movie) {
        movie.run {
            binding.movieTitle.text = title
            binding.genre.text = genres.toString()
                .subSequence(1, genres.toString().length - 1)
            context?.let { _context ->
                Glide.with(_context)
                    .load(movie.backdrop)
                    .placeholder(R.drawable.backdrop_placeholder)
                    .dontAnimate()
                    .into(binding.backgroundPoster)
            }
            binding.reviewsCount.text = resources.getString(R.string.reviews, votes)
            binding.rating.rating = ratings.div(2)
            binding.storylineBody.text = overview
            if (adult) {
                binding.ageLimit.text = resources.getString(R.string.age_adult)
            } else {
                binding.ageLimit.text = resources.getString(R.string.age_non_adult)
            }

            if (actors.isEmpty()) {
                binding.castTitle.visibility = View.INVISIBLE
            } else {
                binding.castTitle.visibility = View.VISIBLE
                val adapter = ActorListAdapter()
                adapter.bindActors(actors)
                binding.actorsRv.adapter = adapter
            }
        }
    }

    private fun setLoading(loading: Boolean) {
        binding.movieDetailsProgressBar.isVisible = loading
        binding.movieDetailsContainer.isVisible = !loading
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

    companion object {
        fun newInstance(movieId: Int): FragmentMoviesDetails {
            return FragmentMoviesDetails().apply {
                arguments = Bundle().apply {
                    putInt(MOVIE_ID_ARG, movieId)
                }
            }
        }
    }
}