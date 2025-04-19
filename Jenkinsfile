pipeline {
    agent any

    environment {
        DEPLOY_DIR = '/home/ubuntu/CharityConnectBackend'
        JAR_NAME = 'CharityConnect-0.0.1-SNAPSHOT.jar'
        LOG_FILE = "${DEPLOY_DIR}/app.log"
        DOWNLOAD_URL = 'https://github.com/tok2sumit/DevOps-Backend/releases/download/latest/CharityConnect-0.0.1-SNAPSHOT.jar'
        GITHUB_TOKEN = credentials('GITHUB_TOKEN')
    }

    stages {
        stage('Verify JAR Exists in GitHub Release') {
            steps {
                script {
                    echo "🔍 Verifying if JAR exists at GitHub release URL..."
                    def responseCode = sh(
                        script: """
                            curl -s -o /dev/null -w "%{http_code}" \
                            -H "Authorization: token ${GITHUB_TOKEN}" \
                            ${DOWNLOAD_URL}
                        """,
                        returnStdout: true
                    ).trim()

                    if (responseCode != '200') {
                        error "❌ JAR not found at ${DOWNLOAD_URL}. Release may have failed or is incomplete."
                    } else {
                        echo "✅ JAR found. Proceeding to download and deploy."
                    }
                }
            }
        }

        stage('Download JAR from GitHub Releases') {
            steps {
                script {
                    echo "⬇️ Downloading JAR from GitHub Releases..."
                    sh """
                        echo "🧹 Cleaning old JAR..."
                        rm -f ${DEPLOY_DIR}/${JAR_NAME}

                        echo "📦 Downloading new JAR..."
                        curl -H "Authorization: token ${GITHUB_TOKEN}" \
                        -L ${DOWNLOAD_URL} \
                        -o ${DEPLOY_DIR}/${JAR_NAME}
                    """
                }
            }
        }

        stage('Deploy') {
            steps {
                timeout(time: 2, unit: 'MINUTES') {
                    echo '🚀 Starting deployment...'
                    sh """
                        echo "🔎 Checking for running instance..."
                        PID=\$(pgrep -f ${DEPLOY_DIR}/${JAR_NAME} || true)
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
