npm i -g pnpm

Set-Location ./terminal
pnpm install
pnpm build
Set-Location ..

Copy-Item ./terminal/dist/index.html ./app/src/main/assets/index.html

# Ubuntu 20.10 and newer
sudo apt-get update
sudo apt-get -y install podman

$tag_name = Get-Date -Format "yyyy.mm.dd+HH.mm.ss"
$version = "$(Get-Date -Format "yyyy.mm.dd")-arm"

$version

"tag=$tag_name" >> $env:GITHUB_OUTPUT

(Get-Content ./app/build.gradle.kts).Replace('0.0.0-arm', $version) | Set-Content ./app/build.gradle.kts

$tag_name > "version"

$env:RUSTFLAGS = "" 
cargo install cross --git https://github.com/cross-rs/cross

Set-Location .\libloader

cross build --release --target aarch64-linux-android
cross build --release --target x86_64-linux-android
cross build --release --target armv7-linux-androideabi
cross build --release --target i686-linux-android

Set-Location ..

Copy-Item ./libloader/target/aarch64-linux-android/release/libloader -Destination ./app/src/main/jniLibs/arm64-v8a/libloader.so
Copy-Item ./libloader/target/x86_64-linux-android/release/libloader -Destination ./app/src/main/jniLibs/x86_64/libloader.so
Copy-Item ./libloader/target/armv7-linux-androideabi/release/libloader -Destination ./app/src/main/jniLibs/armeabi-v7a/libloader.so
Copy-Item ./libloader/target/i686-linux-android/release/libloader -Destination ./app/src/main/jniLibs/x86/libloader.so

chmod 777 ./gradlew
chmod 777 ./**/*