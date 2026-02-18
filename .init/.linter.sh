#!/bin/bash
cd /home/kavia/workspace/code-generation/pet-data-management-service-323128/pets_backend
./gradlew checkstyleMain
LINT_EXIT_CODE=$?
if [ $LINT_EXIT_CODE -ne 0 ]; then
   exit 1
fi

