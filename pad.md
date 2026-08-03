docker build -t us-central1-docker.pkg.dev/padel-server-504118/game-repo/game-server:0.2 .
./gradlew jar extractDeps --no-daemon -x test -PskipTestFromBuild
netstat -ano | findstr :4712
kubectl apply -f k8s/service.yaml
kubectl apply -f k8s/deployment.yaml
gcloud container clusters get-credentials game-cluster --region us-central1   
gcloud auth configure-docker us-central1-docker.pkg.dev
docker push us-central1-docker.pkg.dev/padel-server-504118/game-repo/game-server:0.2  
