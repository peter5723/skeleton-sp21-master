package capers;

import java.io.File;
import java.io.IOException;

import static capers.Utils.*;

/**
 * A repository for Capers
 *
 * @author
 * The structure of a Capers Repository is as follows:
 * <p>
 * .capers/ -- top level folder for all persistent data in your lab12 folder
 * - dogs/ -- folder containing all of the persistent data for dogs
 * - story -- file containing the current story
 * <p>
 *
 */
public class CapersRepository {
    /**
     * Current Working Directory.
     */
    static final File CWD = new File(System.getProperty("user.dir"));

    /**
     * Main metadata folder.
     */
    static final File CAPERS_FOLDER = Utils.join(".capers");
    //之前这里一直没有看懂，这个只是创建对应的File类就可以了

    /**
     * Does required filesystem operations to allow for persistence.
     * (creates any necessary folders or files)
     * Remember: recommended structure (you do not have to follow):
     * <p>
     * .capers/ -- top level folder for all persistent data in your lab12 folder
     * - dogs/ -- folder containing all of the persistent data for dogs
     * - story -- file containing the current story
     */
    public static void setupPersistence() throws IOException {

        if (!CAPERS_FOLDER.exists()) {
            CAPERS_FOLDER.mkdir();
        }
        if (!Dog.DOG_FOLDER.exists()) {
            Dog.DOG_FOLDER.mkdir();
        }
        File story = Utils.join(".capers", "story");
        if (!story.exists()) {
            story.createNewFile();
        }
    }

    /**
     * Appends the first non-command argument in args
     * to a file called `story` in the .capers directory.
     *
     * @param text String of the text to be appended to the story
     */
    public static void writeStory(String text) {
        File f = Utils.join(".capers", "story");

        String contents = Utils.readContentsAsString(f);
        StringBuilder s = new StringBuilder(contents);
        if (!text.endsWith("\n")) {
            text = text.concat("\n");
        } //这样可以保证每句话的末尾都有换行
        s.append(text);
        Utils.writeContents(f, s.toString());
        System.out.println(s.toString());
        //题目要求，看仔细点，总比狂怒好
    }

    /**
     * Creates and persistently saves a dog using the first
     * three non-command arguments of args (name, breed, age).
     * Also prints out the dog's information using toString().
     */
    public static void makeDog(String name, String breed, int age) {
        Dog d = new Dog(name, breed, age);
        d.saveDog();
        System.out.println(d.toString());
    }

    /**
     * Advances a dog's age persistently and prints out a celebratory message.
     * Also prints out the dog's information using toString().
     * Chooses dog to advance based on the first non-command argument of args.
     *
     * @param name String name of the Dog whose birthday we're celebrating.
     */
    public static void celebrateBirthday(String name) {
        File f = join(Dog.DOG_FOLDER, name);
        if (f.exists()) {
            Dog d = Dog.fromFile(name);
            d.haveBirthday();
            d.saveDog();
        }
    }
}
