REM combine username and password (on maven) : https://mixedanalytics.com/tools/basic-authentication-generator/
rem call ./gradlew jreleaserConfig
REM call ./gradlew --stop --warn
Remove-Item -Recurse -Force build\jreleaser -ErrorAction SilentlyContinue; Remove-Item -Recurse -Force build\jreleaser-out -ErrorAction SilentlyContinue;./gradlew jreleaserConfig --stacktrace
call ./gradlew jreleaserConfig --stacktrace
call ./gradlew -x test publish --warn
call ./gradlew -x test jreleaserFullRelease --stacktrace
REM ./gradlew --stop; Remove-Item -Recurse -Force build\jreleaser -ErrorAction SilentlyContinue; ./gradlew jreleaserConfig --stacktrace