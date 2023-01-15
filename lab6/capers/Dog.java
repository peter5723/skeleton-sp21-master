package capers;

import java.io.File;
import java.io.Serializable;

import static capers.Utils.*;

/**
 * Represents a dog that can be serialized.
 * <p>
 * 叫你不看这个todo
 */
public class Dog implements Serializable {

    /**
     * Folder that dogs live in.
     */
    static final File DOG_FOLDER = Utils.join(".capers", "dogs");
    /**
     * Age of dog.
     */
    private int age;
    /**
     * Breed of dog.
     */
    private String breed;
    /**
     * Name of dog.
     */
    private String name;

    /**
     * Creates a dog object with the specified parameters.
     *
     * @param name  Name of dog
     * @param breed Breed of dog
     * @param age   Age of dog
     */
    public Dog(String name, String breed, int age) {
        this.age = age;
        this.breed = breed;
        this.name = name;
    }

    /**
     * Reads in and deserializes a dog from a file with name NAME in DOG_FOLDER.
     *
     * @param name Name of dog to load
     * @return Dog read from file
     */
    public static Dog fromFile(String name) {
        File dogFile = join(DOG_FOLDER, name);
        return readObject(dogFile, Dog.class);
        //cnm，这里不知道为什么会报错，根本没办法，查又查不到
        //6,真几把6，是泛型的问题
        //static <T extends Serializable> T readObject(File file,
        //                                                 Class<T> expectedClass)
        //这个T类型必须继承Serializable类，否则会报错，所以在前面加上这句就可以了
        //无能狂怒是这样的
    }

    /**
     * Increases a dog's age and celebrates!
     */
    public void haveBirthday() {
        age += 1;
        System.out.println(toString());
        System.out.println("Happy birthday! Woof! Woof!");
    }

    /**
     * Saves a dog to a file for future use.
     */
    public void saveDog() {
        File f = join(DOG_FOLDER, this.name);

        writeObject(f, this);
        //這裡不應該判斷是否存在，因為沒有的時候需要新建，有的時候需要修改


    }

    @Override
    public String toString() {
        return String.format(
                "Woof! My name is %s and I am a %s! I am %d years old! Woof!",
                name, breed, age);
    }

}
