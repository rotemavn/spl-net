package bgu.spl.a2;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DeferredTest<T> {
	
	Deferred<Object> defer;

	@Before
	public void setUp() throws Exception {
		defer=new Deferred<Object>();
	}

	@After
	public void tearDown() throws Exception {
		defer=null;
		assertNull(defer);
	}

	@Test
	public void testGet() {
		try{
			defer.get();
		}
		catch (IllegalStateException e){

		}

		Object o=new Object();		
		defer.resolve(o);
		assertEquals(o, defer.get());
		
		
	}

	@Test
	public void testIsResolved() {
		assertEquals(false, defer.isResolved());
		
		defer.resolve(new Object());
		assertEquals(true, defer.isResolved());
	}

	@Test
	public void testResolve() {
		Object o=new Object();
		defer.resolve(o);
		
		assertEquals(o, defer.get());
		assertEquals(true, defer.isResolved());
		assertEquals(0,defer.getNumberOFCallbacks());
		
	}

	@Test
	public void testWhenResolved() {
		Runnable r=new Runnable() {
			
			@Override
			public void run() {
				int i=0;
				i++;;
				
			}
		};
		int initialSize=defer.getNumberOFCallbacks();
		defer.whenResolved(r);
		assertEquals(initialSize+1, defer.getNumberOFCallbacks());		
		
	}

}
