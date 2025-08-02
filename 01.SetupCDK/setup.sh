echo "前処理（unzip, npm のインストール）を始めます。"
sudo apt update
sudo apt install unzip npm maven openjdk-21-jdk -y

echo "AWSCLIのインストールを始めます。"
if ! command -v aws >/dev/null 2>&1; then
    if [ ! -d "aws" ]; then
        if [ ! -f "awscliv2.zip" ]; then
            curl "https://awscli.amazonaws.com/awscli-exe-linux-x86_64.zip" -o "awscliv2.zip"
        fi
        unzip awscliv2.zip
    fi
    sudo ./aws/install
else
    echo "AWS CLIはすでにインストールされています。"
fi

echo "AWS CDKのインストールを始めます。"
if ! command -v cdk >/dev/null 2>&1; then
    sudo npm install -g aws-cdk
else
    echo "AWS CDKはすでにインストールされています。"
fi

echo "CDKプロジェクトを作成します。"
if [ ! -d "my-java-cdk-pj" ]; then
    mkdir my-java-cdk-pj
    cd my-java-cdk-pj
    cdk init app -l java
else
    rm -rf my-java-cdk-pj
    mkdir my-java-cdk-pj
    cd my-java-cdk-pj
    cdk bootstrap
    cdk init app -l java
    mvn dependency:resolve
    mvn package
fi
