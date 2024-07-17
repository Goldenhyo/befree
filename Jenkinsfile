pipeline {
    agent any 
    environment {
        DOCKER_COMPOSE_FILE = 'docker-compose.yml'
        REACT_TRIGGER_URL = 'http://3.36.84.228:8080/job/befree/build?token=react-trigger-token'
    }
    stages {
        stage('Checkout') {
            steps {
                // Git 저장소에서 소스 코드 체크아웃
                git branch: 'master', url: 'https://github.com/Goldenhyo/befreeApi.git'
            }
        }
        stage('Build') {
            steps {
                // Docker 이미지를 빌드
                script {
                    docker.build("befreejenkinsapi", "-f dockerfile .")
                }
            }
        }
        stage('Deploy') {
            steps {
                // Docker Compose를 사용하여 배포
                sh "docker compose -f ${DOCKER_COMPOSE_FILE} up -d"
            }
        }
        stage('Trigger React Build') {
            steps {
                script {
                    // React 프로젝트 빌드를 트리거하는 Webhook 호출
                    sh "curl -X POST ${REACT_TRIGGER_URL}"
                }
            }
        }
    }
    post {
        always {
            // 빌드 후 클린업 작업
            cleanWs()
        }
    }
}
