package org.ice.mp3listsetmaker;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class AppTest {

	@Test
	void test() {
String args[] = {"53b17f0d","/mnt/datos/etc/mi musica/Aterciopelados/","aterciopelados" } ;
    	
    	App.main(args);
    	
        assertTrue( true );
	}

}
