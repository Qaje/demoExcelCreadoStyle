package com.example.demo.controllers;

import com.example.demo.models.Movie;
import com.example.demo.repositories.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/movies")

public class MovieController {

    @Autowired
    private MovieRepository movieRepository;

    @GetMapping
    @CrossOrigin
    public List<Movie> getAllMovies(){
        return movieRepository.findAll();
    }

    @GetMapping("/{id}")
    @CrossOrigin
    public ResponseEntity<Movie> getMovieById(@PathVariable Long id){
        Optional<Movie> movie = movieRepository.findById(id);
        return movie.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @CrossOrigin
    public ResponseEntity<Movie> createMovie (@RequestBody Movie movie){
        Movie saveMovie = movieRepository.save(movie);
        return  ResponseEntity.status(HttpStatus.CREATED).body(saveMovie);
    }

    @DeleteMapping("/{id}")
    @CrossOrigin
    public ResponseEntity<Void> deleteMovie(@PathVariable Long id){
        if(!movieRepository.existsById(id))
        {
            return ResponseEntity.notFound().build();
        }
        movieRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    @CrossOrigin
    public ResponseEntity<Movie> editMovie(@PathVariable Long id,@RequestBody Movie updatedMovie){
        if(!movieRepository.existsById(id)){
            return ResponseEntity.notFound().build();
        }
        updatedMovie.setId(id);
        Movie savedMovie = movieRepository.save(updatedMovie);
        return ResponseEntity.ok(savedMovie);
    }

@CrossOrigin
@GetMapping("/vote/{id}/{rating}")
    public ResponseEntity<Movie> voteMovie(@PathVariable Long id, @PathVariable double rating){
        if(!movieRepository.existsById(id)){
            return ResponseEntity.notFound().build();
        }
        Optional<Movie> optional =  movieRepository.findById(id);
        Movie movie = optional.get();
        // movie rating
        // movie vote es numero total de votos
        double newRating = ( (movie.getVotes() * movie.getRating()) + rating)/ (movie.getVotes() +1);

        movie.setVotes(movie.getVotes()+1);
        movie.setRating(newRating);

        Movie saveMovie = movieRepository.save(movie);
        return ResponseEntity.ok(saveMovie);
    }


}
