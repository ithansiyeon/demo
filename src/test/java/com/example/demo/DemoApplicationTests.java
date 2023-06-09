package com.example.demo;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.xml.bind.DatatypeConverter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
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

	@Autowired
	EntityManager em;

	JPAQueryFactory query;

	@Value("${image.storage.tempDir}")
	private String imageStorageTempDir;

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
		decoder("data:image/png;base64,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","D:\\temp\\summernote_image\\test.jpg");
	}

	public static boolean decoder(String base64, String target){

		String data = base64.split(",")[1];

		byte[] imageBytes = DatatypeConverter.parseBase64Binary(data);

		try {

			BufferedImage bufImg = ImageIO.read(new ByteArrayInputStream(imageBytes));
			ImageIO.write(bufImg, "png", new File(target));

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			return false;
		}

		return true;

	}
}

