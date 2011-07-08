/*   Copyright 2011 Robot Media SL (http://www.robotmedia.net)
*
*   Licensed under the Apache License, Version 2.0 (the "License");
*   you may not use this file except in compliance with the License.
*   You may obtain a copy of the License at
*
*       http://www.apache.org/licenses/LICENSE-2.0
*
*   Unless required by applicable law or agreed to in writing, software
*   distributed under the License is distributed on an "AS IS" BASIS,
*   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*   See the License for the specific language governing permissions and
*   limitations under the License.
*/

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
