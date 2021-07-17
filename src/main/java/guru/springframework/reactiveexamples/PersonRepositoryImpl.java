package guru.springframework.reactiveexamples;

import guru.springframework.reactiveexamples.domain.Person;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Created by jt on 2/27/21.
 */
public class PersonRepositoryImpl implements PersonRepository {

    private final Person sera = new Person(1, "Sera", "Aliaba");
    private final Person serg = new Person(2, "Serg", "Cevich");
    private final Person sebastian = new Person(3, "Sebastian", "Carly");
    private final Person silvia = new Person(4, "Silvia", "Almeida");

    @Override
    public Mono<Person> getById(Integer id) {
        return findAll().filter(person -> person.getId().equals(id)).single()
                .doOnError(throwable -> System.out.println("Person not found"))
                .onErrorReturn(Person.builder().build());
    }

    @Override
    public Flux<Person> findAll() {
        return Flux.just(sera, serg, sebastian, silvia);
    }
}
