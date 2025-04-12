pipeline {
    agent any

    environment {
        GIT_REPO = 'https://github.com/tok2sumit/DevOps-Backend.git'
        BRANCH = 'UAT'
        DEPLOY_DIR = '/home/ubuntu/CharityConnectBackend'
        JAR_NAME = 'CharityConnectBackend.jar'
        LOG_FILE = "${DEPLOY_DIR}/app.log"
    }

    stages {
        stage('Clone Repo') {
            steps {
                git branch: "${BRANCH}", url: "${GIT_REPO}"
            }
        }

        stage('Build App') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Deploy') {
            steps {
                sh '''
                echo "Stopping old app if running..."
                PID=$(pgrep -f $JAR_NAME || true)
                if [ ! -z "$PID" ]; then
                  kill -9 $PID
                  echo "Killed old app with PID $PID"
                fi

                echo "Deploying new JAR..."
                cp target/*.jar $DEPLOY_DIR/$JAR_NAME

                echo "Starting app..."
                nohup java -jar $DEPLOY_DIR/$JAR_NAME > $LOG_FILE 2>&1 &
                '''
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
