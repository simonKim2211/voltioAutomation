pipeline {
    agent {
        label 'maven-selenium-agent'
    }

    parameters {
        string(name: 'UI_BRANCH', defaultValue: 'uitest', description: 'Branch containing UI automation tests')
        string(name: 'API_BRANCH', defaultValue: 'apitest', description: 'Branch containing API automation tests')
    }

    stages {
        stage('Check Tools') {
            steps {
                sh '''
                    java -version
                    mvn -version
                    git --version
                    chromium --version || true
                    chromedriver --version || true
                '''
            }
        }

        stage('Run API Tests') {
            steps {
                dir('api-tests') {
                    git branch: "${params.API_BRANCH}", url: 'https://github.com/simonKim2211/voltioAutomation/tree/apitest'

                    sh '''
                        mvn clean test
                    '''
                }
            }
            post {
                always {
                    archiveArtifacts artifacts: 'api-tests/target/**', allowEmptyArchive: true
                    junit 'api-tests/target/surefire-reports/*.xml'
                }
            }
        }

        stage('Run UI Tests') {
            steps {
                dir('ui-tests') {
                    git branch: "${params.UI_BRANCH}", url: 'https://github.com/simonKim2211/voltioAutomation/tree/uitest'

                    sh '''
                        mvn clean test
                    '''
                }
            }
            post {
                always {
                    archiveArtifacts artifacts: 'ui-tests/target/**', allowEmptyArchive: true
                    junit 'ui-tests/target/surefire-reports/*.xml'
                }
            }
        }
    }

    post {
        always {
            echo 'Test execution finished.'
        }
    }
}
