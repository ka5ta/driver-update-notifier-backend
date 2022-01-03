package com.ka5ta.drivers;

import com.ka5ta.drivers.Entities.Product;
import com.ka5ta.drivers.Scrapers.MsiLinkScraper;
import com.ka5ta.drivers.Scrapers.UnsupportedLinkException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class DriversApplicationTests {

	private MsiLinkScraper msi = new MsiLinkScraper();
	private String linkMSI = "https://www.msi.com/Motherboard/B450-TOMAHAWK-MAX";
	private String linkWrong = "wrong link";
	private Product product = new Product();

	@Test
	void contextLoads() {
	}

	@Test
	void whenLinkScraperTest_ThrowException(){


		/*Msi link test */
/*		UnsupportedLinkException exception = assertThrows(UnsupportedLinkException.class, () ->{
			msi.isLinkSupported(linkWrong);
		});

		String expectedMessage = "The link is wrong: "+ linkWrong;
		String actualMessage = exception.getMessage();

		assertTrue(actualMessage.contains(expectedMessage));*/
	}

	@Test
	void whenLinkScraperTest_Success(){

	}

}
