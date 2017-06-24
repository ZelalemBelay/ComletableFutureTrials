package com.adeytech;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class Main {

    static List<Person> persons = Arrays.asList(new Person[]{
            new Person("Zelalem Belay", 44),
            new Person("Tssion Getnet", 18),
            new Person("Tamiru Endale", 22),
            new Person("Daniel Begashaw", 45),
            new Person("Simret Hagos", 31),
            new Person("Ephrem Gualan", 56),
            new Person("Hellen Berhe", 122, new Address("1245 shoreline ", "45177", "Tokoma", "Washington")),
            new Person("Kemer Yusuf ", 111, new Address("1245 shoreline", "45177", "Tokoma", "Washington")),
            new Person("Thitina Mesfin", 113, new Address("56433 Belvue road ", "13744", "Belvue", "Washington")),
            new Person("Hagos Begashaw", 142, new Address("4356 Lake city ", "45122", "Seatle", "Washington"))
    });

    static Address a = new Address("4312 Caleta", "75038", "Irving", "Texas");
    static Address a2 = new Address("2265 Crippen", "32904", "Melbourne", "Florida");

    static List<Person> persons2 = new ArrayList<>();
    static List<Address> addresses1 = new ArrayList<>();

    public static void main(String[] args) {

        CompletableFuture<List<Person>> completableFutureSupply = CompletableFuture.supplyAsync(() -> {
                    persons.forEach(p -> {
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        if(p.getAge()>25 && p.getAge()<100)
                            p.setAddress(a);
                        else if(p.getAge()<25) p.setAddress(a2);

                        successful(p);
                    });

                    System.out.println(Thread.currentThread().getName());
                    System.out.println("---------------------------------------------------------:)");

                return persons;
            }
        );

        completableFutureSupply.thenApplyAsync(l -> {
            persons2 = l.stream().
                    map(person -> {
                                String x = "Mr. ".concat(person.getName());
                                person.setName(x);
                                return person;
                    }).
                    filter(p->
                    addresses1.contains(p.getAddress()))
                    .collect(Collectors.toList());

            return persons2;
        });

        try {

            System.out.println("-------------Method 1. Sequential------------");
            List<Person> ps = completableFutureSupply.get();
            completableFutureSupply.whenComplete((people, throwable) -> people.forEach(pp-> System.out.println(pp.getName())));

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("-------------Method 2. Non Sequential---ANY---------");

        CompletableFuture<List<Person>> personCompletableFuture = asyncSetAddresses();
        try {
            List<Person> result = personCompletableFuture.get();
            System.out.println("--------> "+Thread.currentThread().getName());

        } catch (InterruptedException  | ExecutionException e) {
            e.printStackTrace();
        }

        CompletableFuture<List<Person>> checkAndAddToList = asyncCheckAndAddToList();

        CompletableFuture<Void> listOfCompletableFuture = CompletableFuture.allOf(personCompletableFuture, checkAndAddToList);

        try {
            Void ps = listOfCompletableFuture.get();
            System.out.println(listOfCompletableFuture.isDone());

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void successful(Person person) {
        System.out.println(person.getName() + " was successfully done!");
    }


    public static CompletableFuture<List<Person>> asyncSetAddresses() {
        CompletableFuture<List<Person>> completableFutures = new CompletableFuture<>();

        Executors.newCachedThreadPool().submit(() -> {

            try {
                Thread.sleep(20);

                persons.forEach(p -> {
                    if(p.getAge()>25 && p.getAge()<100)
                    p.setAddress(a);
                    else if(p.getAge()<25) p.setAddress(a2);

                    successful(p);
                    completableFutures.complete(persons);
                });

                System.out.println(Thread.currentThread().getName());
                System.out.println("---------------------------------------------------------:)");

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return null;
        });

        return completableFutures;
    }

    static List<Person> persons1 = new ArrayList<>();

    public static CompletableFuture<List<Person>> asyncCheckAndAddToList()
    {
        CompletableFuture<List<Person>> personCompletableFuture = new
                CompletableFuture<>();

        List<Address> addresses = new ArrayList<>();
        addresses.add(a);
        addresses.add(a2);

        Executors.newCachedThreadPool().submit(()-> {
            persons1 = persons.stream().filter(p->
                addresses.contains(p.getAddress()))
                    .collect(Collectors.toList());

            personCompletableFuture.complete(persons1);
            return null;
        });

        return personCompletableFuture;
    }

}
