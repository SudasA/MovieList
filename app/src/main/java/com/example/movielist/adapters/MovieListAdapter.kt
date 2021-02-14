package com.example.movielist.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.movielist.MovieClickListener
import com.example.movielist.R
import com.example.movielist.data.model.Movie
import com.example.movielist.databinding.ViewHolderMovieBinding

class MovieListAdapter(private val movieClickListener: MovieClickListener) :
    RecyclerView.Adapter<MovieViewHolder>() {

    private var movies: MutableList<Movie> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
      val binding = ViewHolderMovieBinding
          .inflate(LayoutInflater.from(parent.context), parent, false)
          return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
       holder.onBind(movies[position])
        holder.itemView.setOnClickListener {
            movieClickListener.movieClicked(position)
        }
    }

    override fun getItemCount(): Int {
        return  movies.size
    }

    fun bindMovies(newList: List<Movie>) {
        movies.clear()
        movies.addAll(newList)
        notifyDataSetChanged()
    }

}

class MovieViewHolder(private val binding: ViewHolderMovieBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun onBind(movie: Movie) {
        binding.movie1Title.text = movie.title
        binding.movie1Genre.text = movie.genre
        binding.movie1Duration.text =
            itemView.context.resources.getString(R.string.duration, movie.duration)
        binding.movie1AgeLimit.text =
            itemView.context.resources.getString(R.string.age_limit, movie.ageLimit)
        binding.movie1Poster.setImageResource(movie.posterSmall)
        binding.movie1ReviewsCount.text =
            itemView.context.resources.getString(R.string.reviews, movie.reviewsCount)
        binding.movie1Rating.rating = movie.rating.toFloat()
    }

}