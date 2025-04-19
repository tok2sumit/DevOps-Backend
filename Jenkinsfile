pipeline {
    agent any

    environment {
        DEPLOY_DIR = '/home/ubuntu/CharityConnectBackend'
        JAR_NAME = 'CharityConnect-0.0.1-SNAPSHOT.jar'
        LOG_FILE = "${DEPLOY_DIR}/app.log"
        DOWNLOAD_URL = 'https://github.com/tok2sumit/DevOps-Backend/releases/download/latest/CharityConnect-0.0.1-SNAPSHOT.jar'
        // if using a private repo with GitHub Token
        GITHUB_TOKEN = credentials('GITHUB_TOKEN')
    }

    stages {
        stage('Download JAR from GitHub Release') {
            steps {
                echo '⬇️ Downloading JAR from GitHub Releases...'

                sh """
                    echo "🧹 Cleaning old JAR..."
                    rm -f ${DEPLOY_DIR}/${JAR_NAME}

                    echo "📥 Downloading new JAR from GitHub..."
                    curl -L -o ${DEPLOY_DIR}/${JAR_NAME} "${DOWNLOAD_URL}"
                    # If using a private repo, use:
                    # curl -L -H "Authorization: token ${GITHUB_TOKEN}" -o ${DEPLOY_DIR}/${JAR_NAME} "${DOWNLOAD_URL}"
                """
            }
        }

        stage('Deploy') {
            steps {
                timeout(time: 2, unit: 'MINUTES') {
                    echo '🚀 Starting deployment...'
                    sh """
                        echo "🔎 Checking for running instance..."
                        PID=\$(pgrep -f ${JAR_NAME} || true)
                        if [ ! -z "\$PID" ]; then
                            echo "🛑 Stopping existing app (PID: \$PID)"
                            kill -9 \$PID
                        fi

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
