# authorization-server
JWT(Json Web Token) based authorization server to authenticate user and provide public key used across all microservices

# prerequisites
config server should be up and run<br/>
<a href="https://github.com/JavaAdore/config-server">https://github.com/JavaAdore/config-server</a> <br/>
eureka server should be up and run<br/>
<a href="https://github.com/JavaAdore/eureka-server">https://github.com/JavaAdore/eureka-server</a> <br/>

zipkin server nice be up and run so you can track the request<br/>
<a href="https://github.com/JavaAdore/zipkin-server">https://github.com/JavaAdore/zipkin-server</a> <br/>


Postgres DB <br/>
RabbitMQ <br/>
Ensure rabbitMQ has exchange called "logExchange" <br/>
Ensure exchange "logExchange" has valid routing for messages with routing key "footPrint" ;<br/>
All Methods annotated by @FootPrint("EVENT NAME") pushs logs for event and it's result (event success or throwing busniness of unhandled exception) into the exchange and   <a href="https://github.com/JavaAdore/log-service">log-service</a> has listener on a queue ( the final destination of exchange logExchange with routing key footPrint and then logs the message in mongo db  <br/>


environment variables should be added

# ZIPKIN_SERVER_IP = 127.0.0.1
127.0.0.1 the ip of machine where zipkin server runs
# SLEUTH_LOGGING_LEVEL=info
level of sleuth loggin

# RABBITMQ_LISTENER_IP = 127.0.0.1
# RABBITMQ_LISTENER_PORT = 5672
# RABBITMQ_DEFAULT_USER = user
change user to username of rabbitmq
# RABBITMQ_DEFAULT_PASS = password
change password to username of rabbitmq


# POSTGRES_SERVER_IP    = 127.0.0.1
# POSTGRES_SERVER_PORT  = 5432
# POSTGRES_DBNAME 	    = postgres     
# OAUTH2_CLIENT_ID = client1
# OAUTH2_SECRET = secret
# OAUTH2_GOOGLE_CLIENT_ID = put here google client id for application


# EUREKA_SERVER_IP      = 127.0.0.1
# EUREKA_SERVER_PORT    = 8761


# Implicitly used services ( should be started up after Authorization-Server)
USER-SERVICE <br/>
<a href="https://github.com/JavaAdore/user-service">https://github.com/JavaAdore/user-service</a> <br/>
ROLE-SERVICE <br/>
<a href="https://github.com/JavaAdore/role-service">https://github.com/JavaAdore/user-service</a> <br/>
NOTIFICATION-SERVICE <br/>
<a href="https://github.com/JavaAdore/notification-service">https://github.com/JavaAdore/notification-service</a> <br/>





# Authorization Server provides the following functionalities

OAuth2AccessToken facebookLogin(User facebookUser) throws ServiceException;<br/>

OAuth2AccessToken googleLogin(GoogleIdToken idToken) throws ServiceException;<br/>

OAuth2AccessToken instagramLogin(InstagramProfile instagramProfile) throws ServiceException;<br/>

OAuth2AccessToken registerNewPublicUser(BaseUserInfo baseUserInfo) throws ServiceException;<br/>

void verifyUserNameOwnershipByCode(String code) throws ServiceException;<br/>

void sendForgetPassword(String username) throws ServiceException;<br/>

OAuth2AccessToken resetPasswordByCode(ResetPasswordModel resetPasswordModel) throws ServiceException;<br/>

OAuth2AccessToken resetPasswordByToken(ResetPasswordModel resetPasswordModel) throws ServiceException;<br/>

void changePassword(Long userLoginId , ChangePasswordModel changePasswordModel) throws ServiceException;<br/>

void requestVerificationToken(String username)throws ServiceException;<br/>

void registerNewSystemUser(SystemUserRegistrationInput systemUserRegistrationInput, Long entityId) throws ServiceException;<br/>

Long registerNewEntity(EntityModel entityModel) throws ServiceException;<br/>

# build
as root/Administration <br/>
mvn clean install docker:removeImage docker:build
# run
java -jar target/authorization-server.jar
