REM combine username and password (on maven) : https://mixedanalytics.com/tools/basic-authentication-generator/
rem call ./gradlew jreleaserConfig
call ./gradlew --stop --warn
./gradlew --stop; Remove-Item -Recurse -Force build\jreleaser -ErrorAction SilentlyContinue; ./gradlew jreleaserConfig --stacktrace
REM call ./gradlew jreleaserConfig --stacktrace
call ./gradlew -x test publish --warn
call ./gradlew -x test jreleaserFullRelease
REM ./gradlew --stop; Remove-Item -Recurse -Force build\jreleaser -ErrorAction SilentlyContinue; ./gradlew jreleaserConfig --stacktrace
