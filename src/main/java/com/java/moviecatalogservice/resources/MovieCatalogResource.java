package com.java.moviecatalogservice.resources;

import com.java.moviecatalogservice.models.CatalogItem;
import com.java.moviecatalogservice.models.Movie;
import com.java.moviecatalogservice.models.Rating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {

    /** @Autowired is the consumer saying I need this dependency whereas
     * @Bean is the producer saying I will be needed */
    @Autowired
    RestTemplate restTemplate;

    @Autowired
    WebClient.Builder webClientBuilder;

    @RequestMapping("/{userId}")
    public List<CatalogItem> getCatalog(@PathVariable("userId") String userId) {

        // Get all rated movie Ids
        // For each movie Id, call movie service to get the details
        // Put them all together

        /** This part will be taken care of by calling Rating info microservice to avoid hardcoding */
        List<Rating> ratings = Arrays.asList(
                new Rating("123", 4),
                new Rating("124", 3)
        );

        /** Url should be dynamic to avoid issues */
        return ratings.stream().map(rating -> {
             Movie movie = restTemplate.getForObject("http://localhost:8082/movies/" + rating.getMovieId(), Movie.class);

             /** Use of Webclient instead of RestTemplate */
            /* Movie movie = webClientBuilder.build()
                    .get()
                    .uri("http://localhost:8082/movies/" + rating.getMovieId())
                    .retrieve()
                    .bodyToMono(Movie.class)
                    .block();*/

            return new CatalogItem(movie.getName(), "Action", rating.getRating());
        }).collect(Collectors.toList());

        /** Hard coded in initial step to just return a simple object */
    /* return Collections.singletonList(
                new CatalogItem("Iron Man", "Action", 4)
        );*/

    }

}
