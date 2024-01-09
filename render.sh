#!/bin/bash

# Set the Render deploy hook URL
RENDER_DEPLOY_HOOK_URL=$1

# Build your project (replace the build command with your actual build command)
mvn clean install

# Trigger Render deployment using curl with the secret
curl -X POST $RENDER_DEPLOY_HOOK_URL