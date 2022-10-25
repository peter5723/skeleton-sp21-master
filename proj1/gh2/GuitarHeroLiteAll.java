package gh2;

import edu.princeton.cs.algs4.StdAudio;
import edu.princeton.cs.algs4.StdDraw;

public class GuitarHeroLiteAll {

    public static final double CONCERT_A = 440.0;
    public static void main(String[] args) {
        String keyboard = "q2we4r5ty7u8i9op-[=zxdcfvgbnjmk,.;/' ";
        GuitarString[] guitarStrings = new GuitarString[37];
        for (int i = 0; i < 37; i++) {
            guitarStrings[i] = new GuitarString(CONCERT_A * Math.pow(2.0, ((double) i - 24.0) / 12.0));
        }
        int index = -1;
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();
                index = keyboard.indexOf(key);
                if (index == -1) {
                    continue;
                }
                guitarStrings[index].pluck();

            }
            if (index == -1) {
                continue;
            }
            double sample = guitarStrings[index].sample();
            StdAudio.play(sample);
            guitarStrings[index].tic();


        }
    }
}
