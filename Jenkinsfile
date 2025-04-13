pipeline {
    agent any

    environment {
        GIT_REPO = 'https://github.com/tok2sumit/DevOps-Backend.git'
        BRANCH = 'UAT'
        DEPLOY_DIR = '/home/ubuntu/CharityConnectBackend'
        JAR_NAME = 'CharityConnect-0.0.1-SNAPSHOT.jar'
        LOG_FILE = "${DEPLOY_DIR}/app.log"
        GITHUB_CREDENTIALS_ID = 'Frontend-CharityConnect'
    }

    stages {
        stage('Clone Repo') {
            steps {
                echo '🔄 Cloning GitHub repository...'
                git credentialsId: "${GITHUB_CREDENTIALS_ID}", branch: "${BRANCH}", url: "${GIT_REPO}"
                echo '✅ Repository cloned.'
            }
        }

        stage('Build App') {
            steps {
                echo '🔨 Building Spring Boot application...'
                sh 'mvn clean package -DskipTests --batch-mode'
                echo '✅ Build complete.'
            }
        }

        stage('Deploy') {
            steps {
                timeout(time: 2, unit: 'MINUTES') {
                    echo '🚀 Starting deployment.....'
                    sh """
                        echo "🔎 Checking for running instance..."
                        PID=\$(pgrep -f ${JAR_NAME} || true)
                        if [ ! -z "\$PID" ]; then
                            echo "🛑 Stopping existing app (PID: \$PID)"
                            kill -9 \$PID
                        fi

                        echo "🧹 Cleaning old JAR..."
                        rm -f ${DEPLOY_DIR}/${JAR_NAME}

                        echo "📦 Copying new JAR to deployment directory..."
                        cp target/*.jar ${DEPLOY_DIR}/${JAR_NAME}

                        echo "▶️ Starting new app..."
                        nohup java -jar ${DEPLOY_DIR}/${JAR_NAME} > ${LOG_FILE} 2>&1 &

                        echo "✅ App started successfully in background."
                    """
                }
            }
        }
    }

    post {
        success {
            echo '✅ Deployment completed successfully.'
        }
        failure {
            echo '❌ Deployment failed.'
        }
    }
}
