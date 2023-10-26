# aws-serverless-deploy

This is a sample serverless deployment project that is used to register votes/polls from the users. Registered users in AWS Cognito can submit poll responses through an API Gateway REST endpoint which then adds the response to an SQS Queue. SQS then triggers the Lambda function which process the messages and logs them in a DynamoDB table. The lambda code can be customized to add more processing logic. For the sample implementation it directly stores the polling responses into the database as-is. This project uses AWS Java SDK2 for implementation.

Deployment: Pre-compiled version of the Lambda jar files is stored [here](https://amal-aws-micro-cert.s3.amazonaws.com/lambdacode/user-voting-service-1.0.0.jar).

Infrastructure for the whole sample deployment is available as an AWS Cloudformation template [here](https://amal-aws-micro-cert.s3.amazonaws.com/cloudformation/FantasyGamesAPINew.template).


![image](https://github.com/aamalraj/aws-serverless-deploy/assets/44780732/52021d75-479d-4f98-913f-747196e4bc71)