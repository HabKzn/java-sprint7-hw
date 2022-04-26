package HTTP;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class test {
    public static void main(String[] args) {
        Gson gson = new Gson();
        ArrayList<Cat> cats = new ArrayList<>();
        cats.add(new Cat("Murka", 12));
        cats.add(new Cat("Barsik", 10));
        cats.add(new Cat("Ivan", 9));
        String catss = gson.toJson(cats);
       Type type = new TypeToken<ArrayList<Cat>>(){}.getType();
        ArrayList<Cat> newCats = gson.fromJson(catss, type);

        System.out.println(Arrays.toString(new String[]{String.valueOf(newCats)}));




        int[] a = {1, 2, 3, 4, 5};
        String b = gson.toJson(a);
        System.out.println(b);
        int[] c = gson.fromJson(b, int[].class);
        System.out.println(Arrays.toString(c));

    }
}


class Cat {
    String name;
    int age;

    public Cat(final String name, final int age) {
        this.name = name;
        this.age = age;
    }
}