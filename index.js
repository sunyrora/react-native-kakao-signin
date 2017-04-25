
import { NativeModules } from 'react-native';
const { RNKaKaoSignin } = NativeModules;

const requestMeUrl = 'https://kapi.kakao.com/v1/user/me';

/**
 * requestMe - Returns user profile from Kakao API
 *
 * @param  {String} kakaoAccessToken Access token retrieved from Kakao Login API
 * @return {Promiise<Response>}      User profile response in a promise
 */
function requestMe(kakaoAccessToken) {
  console.log('Requesting user profile from Kakao API server.');
  let req = {
      method: 'GET',
      headers: {
        'Authorization': 'Bearer ' + kakaoAccessToken,
      },
    };

    return fetch(requestMeUrl, req);
  } 

const KakaoSignin = {
  async signIn() {
    try {
      let kakaoAccessToken = await RNKaKaoSignin.signIn();
      console.log("KakaoSignin::login:: ", kakaoAccessToken);

      return requestMe(kakaoAccessToken);

    } catch(err) {
      console.log("KakaoSignin::signIn error!!", err);
    }
  },
  signOut() {
    return RNKaKaoSignin.signOut();
  }
};

export default KakaoSignin;