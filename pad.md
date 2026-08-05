docker build -t us-central1-docker.pkg.dev/padel-server-504118/game-repo/game-server:0.2 .
./gradlew jar extractDeps --no-daemon -x test -PskipTestFromBuild
netstat -ano | findstr :4712
kubectl apply -f k8s/service.yaml
kubectl apply -f k8s/deployment.yaml
gcloud container clusters get-credentials game-cluster --region us-central1   
gcloud auth configure-docker us-central1-docker.pkg.dev
docker push us-central1-docker.pkg.dev/padel-server-504118/game-repo/game-server:0.4  
us-central1-docker.pkg.dev/padel-server-504118/game-repo/game-server:0.4
Test-NetConnection -Port 4712 34.170.12.255
jpackage --input build/app-image --main-jar empty3-desktop-ux.jar --main-class one.empty3.apps.opad.PanelGraphics --dest build/dist --name Empty3Game --type app-image --java-options "--enable-native-access=ALL-UNNAMED" --java-options "--add-exports=java.desktop/sun.awt=ALL-UNNAMED" --java-options "--add-exports=java.desktop/sun.awt.windows=ALL-UNNAMED" --java-options "--add-opens=java.desktop/sun.awt.windows=ALL-UNNAMED"