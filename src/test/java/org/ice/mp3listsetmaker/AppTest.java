package org.ice.mp3listsetmaker;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class AppTest {

	@Test
	void test() {
		String args[] = { "4ba17b1e", "/mnt/datos/etc/mi musica/Blur", "blur" };
		App.main(args);
		assertTrue(true);
	}

}
