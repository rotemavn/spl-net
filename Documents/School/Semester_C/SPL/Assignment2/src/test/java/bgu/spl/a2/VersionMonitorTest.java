package bgu.spl.a2;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import bgu.spl.a2.VersionMonitor;

public class VersionMonitorTest {
	
	VersionMonitor ver;

	@Before
	public void setUp() throws Exception {
		ver = new VersionMonitor();
	}

	@After
	public void tearDown() throws Exception {
		ver = null;
	}

	@Test
	public void testGetVersion() {
		assertEquals(0, ver.getVersion());
	}

	@Test
	public void testInc() {
		int currVer = ver.getVersion();
		ver.inc();
		assertEquals(currVer+1, ver.getVersion());
	}

	@Test
	public void testAwait() {
		try {
			int currVer = ver.getVersion();
			Thread t=new Thread(()->{try {
				ver.await(currVer);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}});			
			ver.inc();
			try{t.join();}
			catch (InterruptedException e){
				Assert.fail("the Thread should be in runnable state after the inc method");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
