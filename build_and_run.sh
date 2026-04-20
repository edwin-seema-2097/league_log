#!/bin/bash

set -e

echo "======================================"
echo " 1. Cleaning previous build artifacts"
echo "======================================"
mvn clean

echo "======================================"
echo " 2. Compiling application"
echo "======================================"
mvn compile

echo "======================================"
echo " 3. Running unit tests"
echo "======================================"
mvn test


echo "======================================"
echo " 5. Running application"
echo "======================================"
java -cp target/classes za.co.league.LeagueRankApplication
