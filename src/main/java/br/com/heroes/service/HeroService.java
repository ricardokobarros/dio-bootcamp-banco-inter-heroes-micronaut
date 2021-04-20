package br.com.heroes.service;

import br.com.heroes.entity.Hero;
import br.com.heroes.repositories.HeroRepository;
import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;
import lombok.RequiredArgsConstructor;

import javax.inject.Singleton;
import java.util.List;

@Singleton
@RequiredArgsConstructor
public class HeroService {

    private final HeroRepository heroRepository;

    public Single<List<Hero>> listAllHeroes(){
        return heroRepository.findAll();
    }

    public Single<Hero> saveHero(Hero hero){
        return heroRepository.save(hero);
    }

    public Maybe<Hero> listByIdHero(Long id) {
        return heroRepository.findById(id);
    }

    public Single<Hero> updateHero(Long id, Hero hero){
        return heroRepository.update(id,hero);
    }

    public Completable deleteHero(Long id) {
         return heroRepository.delete(id);
    }
}
