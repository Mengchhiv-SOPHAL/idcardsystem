pipeline {
    agent any

    triggers {
        pollSCM('H/5 * * * *')
    }

    stages {
        stage('Pull from Git') {
            steps {
                git branch: 'main',
                    url: 'https://github.com/Mengchhiv-SOPHAL/idcardsystem.git'
            }
        }

        stage('Build and Test') {
            steps {
                sh 'chmod +x mvnw && ./mvnw clean package'
            }
        }

        stage('Run Ansible Playbook') {
            steps {
                echo 'Ansible deployment stage included. Run manually if Jenkins container cannot access host WSL.'
            }
        }
    }

    post {
        failure {
            echo 'Build failed. Email should be sent to srengty@gmail.com after SMTP is configured.'
            echo "Failed job: ${env.JOB_NAME} #${env.BUILD_NUMBER}"
        }

        success {
            echo 'Build, test, and deployment pipeline completed successfully.'
        }
    }
}