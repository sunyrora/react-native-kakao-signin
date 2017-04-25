
# react-native-kakao-signin

## Getting started

`$ npm install react-native-kakao-signin --save`

### Mostly automatic installation

`$ react-native link react-native-kakao-signin`

or

`$ yarn add react-native-kakao-signin`


## Kakao Environment for your Project
[IOS guid](https://developers.kakao.com/docs/ios#시작하기-개발환경-구성)

[Android guid](https://developers.kakao.com/docs/android#시작하기-개발환경-구성)

### Manual installation


#### iOS

1. In XCode, in the project navigator, right click `Libraries` ➜ `Add Files to [your project's name]`
2. Go to `node_modules` ➜ `react-native-kakao-signin` and add `RNKaKaoSignin.xcodeproj`
3. In XCode, in the project navigator, select your project. Add `libRNKaKaoSignin.a` to your project's `Build Phases` ➜ `Link Binary With Libraries`
4. Run your project (`Cmd+R`)<

***
* Your Project
	* Build Setting 
		* Header Search Paths: 
	$(PROJECT_DIR)/../node_modules/react-native-kakao-signin/ios
		* Framework search Paths
	$(PROJECT_DIR)/../node_modules/react-native-kakao-signin/ios

***

	* 'AppDelegate.m'
```js

#import <KakaoOpenSDK/KakaoOpenSDK.h>
...

- (BOOL)application:(UIApplication *)application openURL:(NSURL *)url
                                       sourceApplication:(NSString *)sourceApplication
                                              annotation:(id)annotation {
    ...
    if ([KOSession isKakaoAccountLoginCallback:url]) {
        return [KOSession handleOpenURL:url];
    }
    ...
}

- (BOOL)application:(UIApplication *)application openURL:(NSURL *)url
                                                 options:(NSDictionary<NSString *,id> *)options {
    ...
    if ([KOSession isKakaoAccountLoginCallback:url]) {
        return [KOSession handleOpenURL:url];
    }
    ...    
}

- (void)applicationDidBecomeActive:(UIApplication *)application
{
    [KOSession handleDidBecomeActive];
}

```


#### Android

1. Open up `android/app/src/main/java/[...]/MainActivity.java`
  - Add `com.sunyrora.kakaosignin;` to the imports at the top of the file
  - Add `new RNKaKaoSigninPackage()` to the list returned by the `getPackages()` method
2. Append the following lines to `android/settings.gradle`:
  	```
  	include ':react-native-kakao-signin'
  	project(':react-native-kakao-signin').projectDir = new File(rootProject.projectDir, 	'../node_modules/react-native-kakao-signin/android')
  	```
3. Insert the following lines inside the dependencies block in `android/app/build.gradle`:
  	```
      compile project(':react-native-kakao-signin')

***

* Your Project
	* 'MainApplication.java'
```js
import com.sunyrora.kakaosignin.RNKaKaoSigninPackage;

public class MainApplication extends Application implements ReactApplication {

 ....

    @Override
    protected List<ReactPackage> getPackages() {
      return Arrays.<ReactPackage>asList(
          new MainReactPackage(),
          new RNKaKaoSigninPackage() // <=== add this
      );
    }
}

```


## Usage - SigIn
```javascript

import KakaoSignin from 'react-native-kakao-signin'; 

async onSignInKakao() {
	// other code ...

	try {
		const res = await KakaoSignin.signIn();
		const resBody = await res.json();

	} catch(err) {
		//Error handle..
		console.log("Login Error!!", err);
	}
}
```

* return values(resBody)
```
	{
		id,
		kaccount_email,
		
		// properties can be null if there is no data from the server
		properties: {
			profile_image,
			nickname
		}
	}
```


## Usage - SigOut
```javascript

import KakaoSignin from 'react-native-kakao-signin'; 

// TODO: What to do with the module?
async onSignOutKakao() {
	// .....
	try {
		let res = await KakaoSignin.signOut(); // return true/false
	} catch(err) {
		// error handle
		console.log("SignOut Error!!", err);
	}

	// ....
}
```
  