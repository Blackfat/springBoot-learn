package com.blackfat.debug;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author wangfeiyang
 * @Description
 * @create 2020-03-08 09:24
 * @since 1.0-SNAPSHOT
 */
@RestController
@RequestMapping("/persons")
public class PersonController {

    private static List<Person> personList = new ArrayList<>();

    static {
        personList.add(new Person(10001, "test1"));
        personList.add(new Person(10002, "test2"));
        personList.add(new Person(10003, "test3"));
        personList.add(new Person(10004, "test4"));
        personList.add(new Person(10005, "test5"));
    }

    @GetMapping("/")
    public List<Person> list() {
        return personList;
    }


    @GetMapping("/{id}")
    public Person get(@PathVariable("id") Integer id) {
        Person defaultPerson = new Person(88888, "default");
        return personList.stream().filter(person -> Objects.equals(person.getId(), id)).findFirst().orElse(defaultPerson);
    }

    @PostMapping("/")
    public void add(@RequestBody Person person) {
        personList.add(person);
    }

    @PutMapping("/")
    public void update(@RequestBody Person person) {
        personList.removeIf(p -> Objects.equals(p.getId(), person.getId()));
        personList.add(person);
    }
}
