package com.example.movielist.adapters


import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.example.movielist.MovieClickListener
import com.example.movielist.R
import com.example.movielist.data.models.Movie
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
            movieClickListener.movieClicked(movies[position].id)
        }
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    fun bindMovies(newList: List<Movie>?) {
        if (newList != null) {
            movies.clear()
            movies.addAll(newList)
            notifyDataSetChanged()
        }
    }
}

class MovieViewHolder(private val binding: ViewHolderMovieBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun onBind(movie: Movie) {
        binding.movie1Title.text = movie.title
        binding.movie1Genre.text = movie.genres.toString()
            .subSequence(1, movie.genres.toString().length-1)
        binding.movie1Duration.text = itemView.context.resources.getString(R.string.duration, movie.runtime)
        if (movie.adult) {
            binding.movie1AgeLimit.text = itemView.context.resources.getString(R.string.age_adult)
        } else {
            binding.movie1AgeLimit.text = itemView.context.resources.getString(R.string.age_non_adult)
        }
        Glide.with(binding.root)
            .load(movie.poster)
            .apply(RequestOptions().dontTransform())
            .placeholder(R.drawable.background_poster_gradient)
            .listener(object : RequestListener<Drawable>{
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    binding.progressBar.visibility = View.GONE
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    binding.progressBar.visibility = View.GONE
                    return false
                }

            })
            .error(R.drawable.background_poster_gradient)
            .into(binding.movie1Poster)
        binding.movie1ReviewsCount.text = itemView.context.resources.getString(R.string.reviews, movie.votes)
        binding.movie1Rating.rating = movie.ratings/2
    }

}