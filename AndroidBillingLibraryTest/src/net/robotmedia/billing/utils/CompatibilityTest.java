package net.robotmedia.billing.utils;

import android.app.Service;
import android.test.AndroidTestCase;
import android.test.suitebuilder.annotation.SmallTest;

public class CompatibilityTest extends AndroidTestCase {
	
	@SmallTest
	public void testStartNotSticky() throws Exception {
		assertEquals(Compatibility.START_NOT_STICKY, Service.START_NOT_STICKY);
	}

}
