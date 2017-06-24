package com.company;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class Main {

    static List<Person> persons = Arrays.asList(new Person[]{
            new Person("Zelalem Belay", 44),
            new Person("Tssion Getnet", 18),
            new Person("Tamiru Endale", 22),
            new Person("Daniel Begashaw", 45),
            new Person("Simret Hagos", 31),
            new Person("Ephrem Gualan", 12)
    });

   static Address a = new Address("4312 Caleta", "75038", "Irving", "Texas");
    static Address a2 = new Address("2265 Crippen", "32904", "Melbourne", "Florida");


    public static void main(String[] args) {

        CompletableFuture<Person> personCompletableFuture = asyncSetAddresses();
        try {
            Person result = personCompletableFuture.get();
            System.out.println(result.getName()+"  "+ result.getAddress().getAddress());
            System.out.println("--------> "+Thread.currentThread().getName());

        } catch (InterruptedException  | ExecutionException e) {
            e.printStackTrace();
        }
    }

    public static void successful(Person person) {
        System.out.println(person.getName() + " was successfully done!");
    }


    public static CompletableFuture<Person> asyncSetAddresses() {
        CompletableFuture<Person> completableFutures = new CompletableFuture<>();

        Executors.newCachedThreadPool().submit(() -> {

            try {
                Thread.sleep(20);

                persons.forEach(p -> {
                    if(p.getAge()>25)
                    p.setAddress(a);
                    else p.setAddress(a2);

                    successful(p);
                    completableFutures.complete(p);
                });

                System.out.println(Thread.currentThread().getName());
                System.out.println("---------------------------------------------------------");

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return null;
        });

        return completableFutures;
    }

    static List<Person> persons1 = new ArrayList<>();

    public static CompletableFuture<Person> asyncCheckAndAddToList()
    {
        CompletableFuture<Person> personCompletableFuture = new
                CompletableFuture<>();

        List<Address> addresses = new ArrayList<>();
        addresses.add(a);
        addresses.add(a2);

        Executors.newCachedThreadPool().submit(()-> {
            persons1 = persons.stream().filter(p-> {
                p.getAge()>25
            }).collect(Collectors.toList());

        });

        return
    }

}
