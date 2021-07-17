package guru.springframework.reactiveexamples;

import guru.springframework.reactiveexamples.domain.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PersonRepositoryImplTest {

    private PersonRepository personRepository;

    @BeforeEach
    void setUp() {
        personRepository = new PersonRepositoryImpl();
    }

    @Test
    void getByIdBlock() {
        Mono<Person> personMono = personRepository.getById(1);
        Person person = personMono.block();
        System.out.println(person.toString());
    }

    @Test
    void getByIdSusbcribe() {
        Mono<Person> personMono = personRepository.getById(1);
        personMono.subscribe(person -> System.out.println(person.toString()));
    }

    @Test
    void getByIdMapFuntion() {
        Mono<Person> personMono = personRepository.getById(1);
        personMono.map(person -> {
            System.out.println(person.toString());
            return person.getFirstName();
        }).subscribe(System.out::println);
    }

    @Test
    void findAllBlockFirst() {
        Flux<Person> personflux = personRepository.findAll();
        Person person = personflux.blockFirst();
        System.out.println(person.toString());
    }

    @Test
    void findAllSubscribe() {
        Flux<Person> personflux = personRepository.findAll();
        personflux.subscribe(person -> System.out.println(person.toString()));
    }

    @Test
    void findAllToList() {
        Flux<Person> personflux = personRepository.findAll();
        Mono<List<Person>> listMono = personflux.collectList();
        listMono.subscribe(list -> list.forEach(person -> System.out.println(person.toString())));
    }

    @Test
    void findAllGetById() {
        Flux<Person> personflux = personRepository.findAll();
        personflux.filter(person -> person.getId().equals(2)).next().subscribe(person -> System.out.println(person.toString()));
    }

    @Test
    void findAllGetByIdNotFound() {
        Flux<Person> personflux = personRepository.findAll();
        personflux.filter(person -> person.getId().equals(8)).next().subscribe(person -> System.out.println(person.toString()));
    }

    @Test
    void findAllGetByIdNotFoundException() {
        int id = 8;
        Flux<Person> personflux = personRepository.findAll();
        personflux.filter(person -> person.getId().equals(id)).single().doOnError(throwable -> System.out.println("BOOM")).onErrorReturn(Person.builder().id(id).build()).subscribe(person -> System.out.println(person.toString()));
    }

    @Test
    void getByIdFound() {
        Person person = personRepository.getById(1).block();
        assertNotNull(person);
        assertEquals(1, person.getId());
        assertEquals("Sera", person.getFirstName());
    }

    @Test
    void getByIdNotFound() {
        Person person = personRepository.getById(10).block();
        assertNotNull(person);
        assertNull(person.getId());
    }
}