package org.ice.mp3listsetmaker;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class AppTest {

	@Test
	void test() {
String args[] = {"73b7da59","/mnt/datos/etc/mi musica/Gorillaz","gorillaz" } ;
    	
    	App.main(args);
    	
        assertTrue( true );
	}

}
