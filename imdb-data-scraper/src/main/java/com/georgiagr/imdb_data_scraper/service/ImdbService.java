package com.georgiagr.imdb_data_scraper.service;
import com.fasterxml.jackson.databind.JsonNode;
import com.georgiagr.imdb_data_scraper.model.Movie;
import com.georgiagr.imdb_data_scraper.repository.MovieRepository;
import com.georgiagr.imdb_data_scraper.response.MovieDetailsResponse;
import com.georgiagr.imdb_data_scraper.configuration.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Service
public class ImdbService {

	@Autowired
	MovieRepository movieRepository;

	@Autowired
	EntityManager entityManager;

	private final RestTemplate restTemplate = new RestTemplate();

	/*
	 * Finds movies by title.
	 *
	 * @param title the title of the movie to search for
	 * @return a list of movies that contain the title
	 */

	public List<Movie> findByTitle(String title) {

		if (title == null) {
			return null;
		}
		return movieRepository.findAllByTitleContaining(title);

		/* 2nd way with ENTITY MANAGER & CRITERIA BUILDER */
		/*
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Movie> cq = cb.createQuery(Movie.class);

		Root<Movie> root = cq.from(Movie.class);

		if (title != null && !title.isEmpty()) {
			//Predicate titlePredicate = cb.equal(cb.lower(root.get("title")), title.toLowerCase());
			Predicate titlePredicate = cb.like(root.get("title"), "%" + title + "%");
			cq.where(titlePredicate);
		}
		else {
			throw new IllegalArgumentException("Title parameter is required");
		}

		// Create and execute the TypedQuery to get the results
		TypedQuery<Movie> query = entityManager.createQuery(cq);
		return query.getResultList();
		 */
	}

	/*
	* Search the local database for movies matching the given title.
	* If found, it returns the movie details from the local database.
	* If not found, it queries the IMDb web service to find the movies,
	* and inserts any new movies into the local database before returning the results.
	*/

	public List<MovieDetailsResponse> findMovieByTitle(String title) {

		List<Movie> localResults = findByTitle(title);

		if (!localResults.isEmpty()) {
			for (Movie movie : localResults) {

				updateMovieDetails(movie);
			}
			List<MovieDetailsResponse> localResponses = MovieDetailsResponse.map(localResults);
			return localResponses;
		}

		// If no movies are found in the local database, search on IMDb web service
		String url = UriComponentsBuilder.fromHttpUrl(Config.GET_ENDPOINT_MOVIE_BY_TITLE)
				.queryParam("q", title)
				.toUriString();

		HttpHeaders headers = new HttpHeaders();
		headers.set(Config.HEADER_HOST, Config.HOST);
		headers.set(Config.HEADER_KEY, Config.API_KEY);

		HttpEntity<String> entity = new HttpEntity<>(headers);


		ResponseEntity<JsonNode> response = restTemplate.exchange(url, HttpMethod.GET, entity, JsonNode.class);

		JsonNode rootNode = response.getBody();
		JsonNode moviesNode = rootNode.get(Config.ROOT_NODE);
		List<MovieDetailsResponse> results = new ArrayList<>();

		// Check if the response contains movies
		if (moviesNode == null || !moviesNode.isArray()) {
			return results;
		}

		for (JsonNode movieNode : moviesNode) {
			String movie_title = movieNode.has(Config.JSON_TITLE) ? movieNode.get(Config.JSON_TITLE).asText() : null;

			if (movie_title == null || !movie_title.toLowerCase().contains(title.toLowerCase())) {
				continue;
			}

			Integer runningTime = movieNode.has(Config.JSON_TIME) ? movieNode.get(Config.JSON_TIME).asInt() : 0;
			Integer year = movieNode.has(Config.JSON_YEAR) ? movieNode.get(Config.JSON_YEAR).asInt() : 0;

			List<String> listActors = new ArrayList<>();
			if (movieNode.has(Config.JSON_ACTORS)) {
				for (JsonNode actorNode : movieNode.get(Config.JSON_ACTORS)) {
					if (actorNode.has(Config.JSON_NAME_ACTOR)) {
						listActors.add(actorNode.get(Config.JSON_NAME_ACTOR).asText());
					}
				}
			}

			String actors = String.join(", ", listActors);

			if (runningTime == 0 && year == 0) {
				continue;
			}

			MovieDetailsResponse movieDetails = new MovieDetailsResponse(movie_title, runningTime, year, actors);
			results.add(movieDetails);

			List<Movie> existingMovies = findByTitle(movieDetails.getTitle());
			if (existingMovies.isEmpty()) {
				insertToDb(movieDetails);
			}
		}
		return results;
	}

	/*
	 * Inserts movie details into the database.
	 * @param mr the movie details response to insert.
	 */
	public void insertToDb(MovieDetailsResponse mr) {
		Movie movie = new Movie();
		movie.setRunningTime(mr.getRunningTime());
		movie.setYear(mr.getYear());
		movie.setTitle(mr.getTitle());
		movie.setActors(mr.getActors());
		movieRepository.save(movie);
	}

	/*
	 * Updates movie details into the database.
	 * @param mv the movie entity to update.
	 */
	public void updateMovieDetails(Movie mv) {
		Movie movie = movieRepository.findByTitle(mv.getTitle());
		if (movie != null) {
			// Update the fields
			movie.setYear(mv.getYear());
			movie.setRunningTime(mv.getRunningTime());
			movie.setActors(String.join(", ", mv.getActors()));
			movieRepository.save(movie);
		}
		else {
			throw new IllegalArgumentException("Movie not found with title: " + mv.getTitle());
		}
	}
}
