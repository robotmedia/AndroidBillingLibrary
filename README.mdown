Update
======

In-app Billing v2 API is deprecated and will be shut down in January 2015. This library was developed for v2 a long time ago. If your app is still using this library, please migrate to the [v3 API](https://developer.android.com/google/play/billing/api.html) as soon as possible.

The project [Android Checkout Library](https://github.com/serso/android-checkout) by @serso supports v3 and attemps to provide [data compatibility](https://github.com/serso/android-checkout#billing-version-2) with AndroidBillingLibrary. We haven't verified this so please use it at your own discretion.


Android Billing Library
=======================

	requestPurchase("com.example.item")

That's how simple it should be to use [Android In-app Billing][1].

And with this library it is.

*Android Billing Library* implements in-app billing's full specification and offers high-level classes to use it. Transactions are stored in a local obfuscated database which can be easily queried.

Getting Started
===============

* Get acquainted with the [Android In-app Billing][1] documentation.

* Add *Android Billing Library* to your project.

* Open the *AndroidManifest.xml* of your application and add this permission...

`<uses-permission android:name="com.android.vending.BILLING" />`

...and this service and receiver inside the application element:

	<service android:name="net.robotmedia.billing.BillingService" />
	<receiver android:name="net.robotmedia.billing.BillingReceiver">
		<intent-filter>
			<action android:name="com.android.vending.billing.IN_APP_NOTIFY" />
			<action android:name="com.android.vending.billing.RESPONSE_CODE" />
			<action android:name="com.android.vending.billing.PURCHASE_STATE_CHANGED" />
		</intent-filter>
	</receiver>

* Subclass [AbstractBillingActivity][2] in the activity in which you want to use in-app billing. Or use [BillingController][3] if you need finer-grain control.

**That's it!**

Usage
=====

Subclassing AbstractBillingActivity
-----------------------------------

[AbstractBillingActivity][2] is an abstract activity that provides default integration with in-app billing (an analogous class for fragments is also provided). It is useful to get acquainted with the library, or for very simple applications that require in-app billing integration in only one activity. For more flexibility use [BillingController][3] directly.

When created your [AbstractBillingActivity][2] instance will check if in-app billing is supported, followed by a call to `onBillingChecked(boolean)`, which has to be implemented by the subclass.

Additionally, your [AbstractBillingActivity][2] subclass will attempt to restore all transactions, only once. This is necessary in case the user has previously installed the app and made purchases. Existing transactions will generate calls to `onPurchaseStateChange(String, PurchaseState)`, which has to be implemented by the subclass.

Starting a purchase is as simple as calling `requestPurchase(String)`. [AbstractBillingActivity][2] will start the Google Play intent automatically and `onPurchaseStateChange(String, PurchaseState)` will be called after the transaction is confirmed.

If you override any of the methods provided by [AbstractBillingActivity][2], make sure to call the superclass implementation.

BillingController
-----------------

[BillingController][3] provides high-level functions to interact with the Billing service and to query an obfuscated local transaction database.

Since most billing functions are asynchronous, [BillingController][3] notifies all registered [IBillingObserver][4] of the responses. 

Additionally, [BillingController][3] requires a `BillingController.IConfiguration` instance from which the public key required to validate signed messages and a salt to obfuscate transactions are obtained. A good place to provide the configuration is in the `Application` subclass.

Dungeons Redux
==============

[Dungeons Redux][5] is a sample app that shows how to use *Android Billing Library* via [BillingController][3]. It is a simplified version of the Dungeons in-app billing example provided by Google.

It should be noted that Dungeons Redux does not intend to be an example of how to use in-app billing in general.

Contact
=======

http://www.twitter.com/robotmedia | http://www.facebook.com/robotmedia | http://www.robotmedia.net

License
=======

Copyright 2011 Robot Media SL (http://www.robotmedia.net)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

[1]: http://developer.android.com/guide/market/billing/index.html
[2]: https://github.com/robotmedia/AndroidBillingLibrary/blob/master/AndroidBillingLibrary/src/net/robotmedia/billing/helper/AbstractBillingActivity.java
[3]: https://github.com/robotmedia/AndroidBillingLibrary/blob/master/AndroidBillingLibrary/src/net/robotmedia/billing/BillingController.java
[4]: https://github.com/robotmedia/AndroidBillingLibrary/blob/master/AndroidBillingLibrary/src/net/robotmedia/billing/IBillingObserver.java
[5]: https://github.com/robotmedia/AndroidBillingLibrary/tree/master/DungeonsRedux
