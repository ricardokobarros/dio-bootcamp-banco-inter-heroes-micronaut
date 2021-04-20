package br.com.heroes.repositories;

import br.com.heroes.entity.Hero;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoCollection;
import io.micronaut.context.annotation.Value;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;
import lombok.Data;

import java.util.List;

import static com.mongodb.client.model.Filters.eq;

@Data
public class HeroRepository {

    private final MongoClient mongoClient;

    @Value("${mongodb.database}")
    private String database;

    public Single<List<Hero>> findAll() {
        return Flowable.fromPublisher(
                getCollection().find()
        ).toList();
    }

    public Single<Hero> save(Hero hero) {
        return Single.fromPublisher(getCollection().insertOne(hero))
                .map(success -> hero);

    }

    public Maybe<Hero> findById(Long id) {
        return Flowable.fromPublisher(
                getCollection()
                        .find(eq("_id", id))
                        .limit(1)
        ).firstElement();
    }

    public Single<Hero> update(Long id, Hero hero) {
        return Single.fromPublisher(
                getCollection()
                        .replaceOne(eq("_id", id), hero))
                .map(success -> hero);
    }

    public Completable delete(Long id) {
        return Completable.fromPublisher(
            getCollection()
                .deleteOne(eq("_id", id))
        );



    }

    private MongoCollection<Hero> getCollection() {
        return mongoClient
                .getDatabase(database)
                .getCollection("Hero", Hero.class);
    }

}