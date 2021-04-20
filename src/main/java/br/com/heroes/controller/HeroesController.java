package br.com.heroes.controller;

import br.com.heroes.entity.Hero;
import br.com.heroes.service.HeroService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

import java.util.List;

@Controller("/heroes")
@Log
@RequiredArgsConstructor
public class HeroesController {

    private final HeroService heroService;

    @Get
    public Single<List<Hero>> getAllItems(){
        log.info("requesting the list off all heroes");
        return heroService.listAllHeroes();
    }

    @Get("/{id}")
    public Maybe<Hero> getByIdHero(Long id){
        log.info(String.format("requesting the hero with id %s",id));
        return heroService.listByIdHero(id);
    }

    @Post
    public HttpResponse<Single<Hero>> createHero(@Body Hero hero){
        log.info("a new hero was created");
        return HttpResponse.created(heroService.saveHero(hero));
    }

    @Put("/{id}")
    public Single<Hero> updateHero(Long id, @Body Hero hero){
        log.info("a new hero was updated");
        return heroService.updateHero(id,hero);
    }

    @Delete("/{id}")
    public Completable deleteHero(Long id){
        log.info(String.format("deleting the hero with id %s", id));
        return heroService.deleteHero(id);
    }


}
