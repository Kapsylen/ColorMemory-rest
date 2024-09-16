package sesv.dev.domain;

import lombok.Builder;
import lombok.Data;

import java.util.Random;

@Builder
@Data
public class ColorV2 {
    String rgbColor;

    public static ColorV2 getRandomColor () {
        Random rand = new Random();
        return new ColorV2(String.format("#%06x", rand.nextInt(256 * 256 * 256)));
    }
}
