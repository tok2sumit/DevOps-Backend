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
                git credentialsId: "${GITHUB_CREDENTIALS_ID}", branch: "${BRANCH}", url: "${GIT_REPO}"
            }
        }

        stage('Build App') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Deploy') {
            steps {
                sh """
                echo "Stopping old app if running..."
                PID=\$(pgrep -f ${JAR_NAME} || true)
                if [ ! -z "\$PID" ]; then
                  kill -9 \$PID
                  echo "Killed old app with PID \$PID"
                fi

                echo "Deploying new JAR..."
                rm -f ${DEPLOY_DIR}/${JAR_NAME}
                cp target/*.jar ${DEPLOY_DIR}/${JAR_NAME}

                echo "Starting app..."
                nohup java -jar ${DEPLOY_DIR}/${JAR_NAME} > ${LOG_FILE} 2>&1 &
                """
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
