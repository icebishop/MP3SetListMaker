package org.ice.mp3listsetmaker;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class AppTest {

	@Test
	void test() {
		String args[] = { "1bae3528", "/mnt/datos/etc/mi musica/Pet Shop Boys", "blur" };
		App.main(args);
		assertTrue(true);
	}

}
