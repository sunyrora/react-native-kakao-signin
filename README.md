
# react-native-ka-kao-signin

## Getting started

`$ npm install react-native-kakao-signin --save`

### Mostly automatic installation

`$ react-native link react-native-kakao-signin`

### Manual installation


#### iOS

1. In XCode, in the project navigator, right click `Libraries` ➜ `Add Files to [your project's name]`
2. Go to `node_modules` ➜ `react-native-ka-kao-signin` and add `RNKaKaoSignin.xcodeproj`
3. In XCode, in the project navigator, select your project. Add `libRNKaKaoSignin.a` to your project's `Build Phases` ➜ `Link Binary With Libraries`
4. Run your project (`Cmd+R`)<

#### Android

1. Open up `android/app/src/main/java/[...]/MainActivity.java`
  - Add `import com.reactlibrary.RNKaKaoSigninPackage;` to the imports at the top of the file
  - Add `new RNKaKaoSigninPackage()` to the list returned by the `getPackages()` method
2. Append the following lines to `android/settings.gradle`:
  	```
  	include ':react-native-ka-kao-signin'
  	project(':react-native-ka-kao-signin').projectDir = new File(rootProject.projectDir, 	'../node_modules/react-native-kakao-signin/android')
  	```
3. Insert the following lines inside the dependencies block in `android/app/build.gradle`:
  	```
      compile project(':react-native-kakao-signin')
  	```

## Usage - SigIn
```javascript

import KakaoSignin from 'react-native-kakao-signin'; 

async onSignInKakao() {
	// other code ...

	try {
		const res = await KakaoSignin.signIn();
		const resBody = await res.json();

		// return values
		/*
		{
			id,
			kaccount_email,
			
			// properties can be null if there are no data from the server
			properties: {
				profile_image,
				nickname
			}
		}
		*/

	} catch(err) {
		//Error handle..
		console.log("Login Error!!", err);
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
  