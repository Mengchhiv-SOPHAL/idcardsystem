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
                bat 'mvn clean package'
            }
        }

        stage('Run Ansible Playbook') {
            steps {
                bat 'wsl -u root bash -lc "cd /mnt/c/Users/Meng\\ chhiv/Documents/idcardsystem/idcardsystem && ansible-playbook -i inventory.ini task3-playbook.yml"'
            }
        }
    }

    post {
        failure {
            mail to: 'srengty@gmail.com',
                 subject: "Jenkins Build Failed: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                 body: "Build failed. Please check Jenkins console output. Commit author should be notified."
        }

        success {
            echo 'Build, test, and Ansible deployment completed successfully.'
        }
    }
}