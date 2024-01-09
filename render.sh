#!/bin/bash

# Set the Render service name (replace YOUR_SERVICE_NAME with your service name)
SERVICE_NAME="Spring Boot LanguageApp"

# Build your project (replace the build command with your actual build command)
./mvnw clean install

# Deploy to Render using deploy hook
render deploy-hook --from ./target/LanguageAppMongoDb-0.0.1-SNAPSHOT.jar --config render.yaml $SERVICE_NAME
