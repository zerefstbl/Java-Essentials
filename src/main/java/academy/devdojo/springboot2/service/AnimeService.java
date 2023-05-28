package academy.devdojo.springboot2.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import academy.devdojo.springboot2.domain.Anime;
import academy.devdojo.springboot2.exception.BadRequestException;
import academy.devdojo.springboot2.mapper.AnimeMapper;
import academy.devdojo.springboot2.repository.AnimeRepository;
import academy.devdojo.springboot2.requests.AnimePostRequestBody;
import academy.devdojo.springboot2.requests.AnimePutRequestBody;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class AnimeService {
  private final AnimeRepository animeRepository;

  public List<Anime> listAll() {
    return animeRepository.findAll();
  }

  public List<Anime> findByName(String name) {
    return animeRepository.findByName(name);
  }

  public Anime findByIdOrThrowBadRequestException(long id) {
    return animeRepository.findById(id)
        .orElseThrow(() -> new BadRequestException("Anime not found"));
  }

  @Transactional
  public Anime save(AnimePostRequestBody animePostRequestBody) {
    Anime save = animeRepository.save(AnimeMapper.INSTANCE.toAnime(animePostRequestBody));
    // if (true)
    // throw new RuntimeException("Bad Code");
    return save;
  }

  public void delete(long id) {
    animeRepository.delete(findByIdOrThrowBadRequestException(id));
  }

  public Anime replace(AnimePutRequestBody animePutRequestBody) {
    Anime savedAnime = findByIdOrThrowBadRequestException(animePutRequestBody.getId());
    Anime anime = AnimeMapper.INSTANCE.toAnime(animePutRequestBody);
    anime.setId(savedAnime.getId());
    animeRepository.save(anime);
    return anime;
  }
}
