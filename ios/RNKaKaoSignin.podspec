
Pod::Spec.new do |s|
  s.name         = "RNKaKaoSignin"
  s.version      = "1.0.0"
  s.summary      = "RNKaKaoSignin"
  s.description  = <<-DESC
                  RNKaKaoSignin
                   DESC
  s.homepage     = ""
  s.license      = "MIT"
  # s.license      = { :type => "MIT", :file => "FILE_LICENSE" }
  s.author             = { "author" => "author@domain.cn" }
  s.platform     = :ios, "7.0"
  s.source       = { :git => "git@github.com:sunyrora/react-native-kakao-signin.git", :tag => "master" }
  s.source_files  = "RNKaKaoSignin/**/*.{h,m}"
  s.requires_arc = true


  s.dependency "React"
  #s.dependency "others"

end

  