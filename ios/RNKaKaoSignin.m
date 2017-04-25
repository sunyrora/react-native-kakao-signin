
#import "RNKaKaoSignin.h"

#import <KakaoOpenSDK/KakaoOpenSDK.h>

@implementation RNKaKaoSignin

RCT_EXPORT_MODULE()

RCT_REMAP_METHOD(signIn,
                 signInResolver:(RCTPromiseResolveBlock)resolve
                 signInRejecter:(RCTPromiseRejectBlock)reject)
{
    KOSession *session = [KOSession sharedSession];
    // ensure old session was closed
    [session close];
    
    [session openWithCompletionHandler:^(NSError *error) {
        if ([session isOpen]) {
            // signIn success
            NSString* token = session.accessToken;
            resolve(token);
            
        } else {
            // failed
            reject(@"signIn failed.", @"", error);
        }
    }];
}

RCT_REMAP_METHOD(signOut,
                 signOutResolver:(RCTPromiseResolveBlock)resolve
                 signOutRejecter:(RCTPromiseRejectBlock)reject)
{
    KOSession *session = [KOSession sharedSession];
    [session close];
    
    [session logoutAndCloseWithCompletionHandler:^(BOOL success, NSError *error) {
        if (success) {
            // signOut success.
            resolve([NSNumber numberWithBool:success]);
        } else {
            // failed
            reject(@"signOut failed", @"", error);
        }
    }];
}

@end
  
