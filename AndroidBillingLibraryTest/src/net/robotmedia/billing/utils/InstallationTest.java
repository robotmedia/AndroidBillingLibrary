package net.robotmedia.billing.utils;

import android.test.AndroidTestCase;
import android.test.suitebuilder.annotation.MediumTest;
import android.text.TextUtils;

public class InstallationTest extends AndroidTestCase {

	@MediumTest
	public void testId() throws Exception {
		final String id = Installation.id(getContext());
		assertFalse(TextUtils.isEmpty(id));
	}
	
	@MediumTest
	public void testSameValue() throws Exception {
		final String id1 = Installation.id(getContext());
		final String id2 = Installation.id(getContext());
		final String id3 = Installation.id(getContext());
		assertEquals(id1, id2);
		assertEquals(id2, id3);
	}
}
