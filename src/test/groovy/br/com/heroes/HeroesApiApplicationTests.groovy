package br.com.heroes

import br.com.heroes.entity.Hero
import br.com.heroes.repositories.HeroRepository
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.MediaType
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import spock.lang.Shared
import spock.lang.Specification

import javax.inject.Inject

@MicronautTest
class HeroesApiApplicationTests extends Specification {

    @Shared
    @Inject
    @Client("/")
    HttpClient client

    @Shared
    Hero hero;

    @Shared
    @Inject
    HeroRepository repository;

    String HEROES_ENDPOINT_LOCAL = "/heroes"

    def setupSpec() {
        hero = Hero.builder()
                .id(-1l)
                .name("batman")
                .universe("DC")
                .films(10)
                .build()
    }

    def cleanup() {
        repository.delete(-1l).subscribe()
    }

    def getOneHeroById() {
        given:
        HttpRequest request = HttpRequest.GET(HEROES_ENDPOINT_LOCAL.concat("/1"))
                .accept(MediaType.APPLICATION_JSON)
        HttpResponse<String> response = client.toBlocking().exchange(request, String)
        expect:
        response.status() == HttpStatus.OK
        response.body() != null
    }

    def getOneHeroNotFound() {
        given:
        HttpRequest request = HttpRequest.GET(HEROES_ENDPOINT_LOCAL.concat("/100"))
                .accept(MediaType.APPLICATION_JSON)

        when:
        client.toBlocking().retrieve(request, String)

        then:
        HttpClientResponseException ex = thrown()

        and:
        ex.status == HttpStatus.NOT_FOUND
    }

    def getAllHeroes(){
        given:
        HttpRequest request = HttpRequest.POST(HEROES_ENDPOINT_LOCAL, hero)
                .accept(MediaType.APPLICATION_JSON)
        client.toBlocking().exchange(request, String)
        request = HttpRequest.GET(HEROES_ENDPOINT_LOCAL)
                .accept(MediaType.APPLICATION_JSON)
        HttpResponse<List<Hero>> response = client.toBlocking().exchange(request, List<Hero>)
        expect:
        response.status() == HttpStatus.OK
        response.body().size()>=1
    }

    def deleteHero() {
        given:
        HttpRequest request = HttpRequest.DELETE(HEROES_ENDPOINT_LOCAL.concat("/7"))
                .accept(MediaType.APPLICATION_JSON)
        HttpResponse<String> response = client.toBlocking().exchange(request, String)
        expect:
        response.status() == HttpStatus.OK
        response.body() == null
    }

    def insertHero() {
        given:
        HttpRequest request = HttpRequest.POST(HEROES_ENDPOINT_LOCAL, hero)
                .accept(MediaType.APPLICATION_JSON)
        HttpResponse<String> response = client.toBlocking().exchange(request, String)
        expect:
        response.status() == HttpStatus.CREATED
    }

    def updateHero() {
        given:
        HttpRequest request = HttpRequest.POST(HEROES_ENDPOINT_LOCAL, hero)
                .accept(MediaType.APPLICATION_JSON)
        client.toBlocking().exchange(request, Hero)

        when:
        hero.setName("hulk")
        hero.setUniverse("marvel")
        hero.setFilms(8)
        request = HttpRequest.PUT(HEROES_ENDPOINT_LOCAL.concat("/-1"), hero)
                .accept(MediaType.APPLICATION_JSON)
        client.toBlocking().exchange(request, String)

        request = HttpRequest.GET(HEROES_ENDPOINT_LOCAL.concat("/-1"))
                .accept(MediaType.APPLICATION_JSON)
        HttpResponse<Hero> response = client.toBlocking().exchange(request, Hero)

        then:
        response.status() == HttpStatus.OK
        Hero hero1 = response.body()
        hero == hero1
    }

}
