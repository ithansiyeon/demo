package com.example.demo;

import jakarta.xml.bind.DatatypeConverter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

import static com.example.demo.utils.CalendarUtil.getCurrentSimpleDate;

@SpringBootTest
class DemoApplicationTests {

	@Value("${image.storage.tempDir}")
	private String imageStorageTempDir;

	@Value("${image.storage.Dir}")
	private String imageStorageDir;

	@Test
	void contextLoads() {
	}

	/**
	 * dir 만들기
	 */
	@Test
	public void makeFolder() {
		String currentSimpleDate = getCurrentSimpleDate();
		File dir = new File(imageStorageTempDir+currentSimpleDate);
		if(!dir.exists()) {
			dir.mkdirs();
		}
	}

	@Test
	public void base64ToFile() {
		decoder("data:image/gif;base64,R0lGODlhDwAOAPYBAAAAAP///+Pj5MzS6qy33bfA4sDI5dne8I6e0JGg0Zim1Jqo1aGu2H6SyZOj0HWMxYWYytbY3V98vLG801p9ulN6u2SHw0JvtYiix8vU4TBmsDBmrx9fqyJgrEB1uGCOxRpeqoGjygdXpwlYpw5aqBVhrGOUx6C+3N/p8+/0+dzd3gBVpQNWpgtdphBgqxpmqxpnqyBqsDB1tmqbx3CfzJe62Z+/3a/K46/J4r/V6c/f7t/q9AJaoQhcpAlcpVuSwh9sqC10rF+Xv5G20cXY5cba5wxnnZu7zXKfthhxmS6EloezvVqcqFeeqJq+w1SjqYC0sajNysrh3drp4trn4fD49O/18vb6+Pf5+OXs6Pz9/Pv8+////wAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACH5BAAAAAAALAAAAAAPAA4AAAeYgAEBThYVFxocJCwrL4KCW0gIDxIUGyAiKys6jlMhCg0TKhgdI5kzjksGDA4RKgInLpkwglhHAwQJEBmCOrErOAFUNQcFCx4uN4IpMis/AUwrJh8lKy45gjvMLQFPmZkymwG9mT4oVU2ZMSmCNt5ARYJaUDwrNgE03kHrjgFRRisxvAnhx09KEm9DCBLMoqQHEYUKrVyBGAgAOw==","/Users/siyeon/Desktop/temp/summernote_image/test.jpg");
	}

	public static boolean decoder(String base64, String target){

		String data = base64.split(",")[1];

		byte[] imageBytes = DatatypeConverter.parseBase64Binary(data);
		System.out.println("imageBytes.toString() = " + imageBytes.toString());

		try {

			BufferedImage bufImg = ImageIO.read(new ByteArrayInputStream(imageBytes));

			ImageIO.write(bufImg, "jpg", new File(target));

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			return false;
		}

		return true;

	}

}
