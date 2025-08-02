#!/bin/bash

set -e

echo "システムのパッケージを最新化します。"
sudo apt update
sudo apt upgrade -y

echo "unzip, maven, openjdk の最新版をインストールします。"
sudo apt install -y unzip maven openjdk-21-jdk curl

echo "Node.js (npm含む) のLTS版をインストールします。"
curl -fsSL https://deb.nodesource.com/setup_lts.x | sudo -E bash -
sudo apt-get install -y nodejs

echo "Node.jsバージョン:"
node -v
echo "npmバージョン:"
npm -v

echo "AWS CLIの最新版をインストールします。"
if ! command -v aws >/dev/null 2>&1; then
    cd /tmp
    curl "https://awscli.amazonaws.com/awscli-exe-linux-x86_64.zip" -o "awscliv2.zip"
    unzip -o awscliv2.zip
    sudo ./aws/install --update
    rm -rf awscliv2.zip aws
    cd -
else
    echo "AWS CLIはすでにインストールされています。"
    aws --version
fi

echo "AWS CDKの最新版をインストールします。"
sudo npm install -g aws-cdk@latest

echo "CDKプロジェクトを作成します。"
if [ -d "my-java-cdk-pj" ]; then
    rm -rf my-java-cdk-pj
fi
mkdir my-java-cdk-pj
cd my-java-cdk-pj
cdk init app -l java

echo "maven依存パッケージを最新化します。"
mvn dependency:resolve
mvn package

echo "cdk展開。"
cdk bootstrap
cdk synth
cdk deploy

echo "セットアップ完了"
echo "===== バージョン情報 ====="
echo -n "Java: "
java -version 2>&1 | head -n 1
echo -n "Maven: "
mvn -v | head -n 1
echo -n "Node.js: "
node -v
echo -n "npm: "
npm -v
echo -n "AWS CLI: "
aws --version
echo -n "AWS CDK: "
cdk --version
