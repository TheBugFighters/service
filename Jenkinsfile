pipeline {
    agent any
    environment {
        DOCKER_PASSWORD = credentials("docker_password")
        DOCKER_USERNAME = credentials("docker_username")
    }

    stages {
        stage('Build & Test') {
            steps {
                sh './gradlew clean build'
            }
        }

        stage('Tag image') {
            steps {
                script {
                    GIT_TAG = sh([script: 'git fetch --tag && git tag', returnStdout: true]).trim()
                    env.MAJOR_VERSION = sh([script: 'git tag | sort --version-sort | tail -1 | cut -d . -f 1', returnStdout: true]).trim()
                    env.MINOR_VERSION = sh([script: 'git tag | sort --version-sort | tail -1 | cut -d . -f 2', returnStdout: true]).trim()
                    env.PATCH_VERSION = sh([script: 'git tag | sort --version-sort | tail -1 | cut -d . -f 3', returnStdout: true]).trim()
                    IMAGE_VERSION = "${MAJOR_VERSION}.${MINOR_VERSION}.${PATCH_VERSION}"
                }
                sh """\
                        docker build -t ${DOCKER_USER}/${IMAGE_NAME}:${IMAGE_VERSION} .
                        docker login docker.io -u ${DOCKER_USER} -p ${DOCKER_PASSWORD}
                        docker push ${DOCKER_USER}/${IMAGE_NAME}:${IMAGE_VERSION}
                   """.stripIndent()
            }
        }
    }
}
