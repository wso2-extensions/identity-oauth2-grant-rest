# OAuth grant type for multi-factor authentication

Supported Versions : 5.10.0, 5.11.0, 6.0.0, 6.1.0

This connector provides an OAuth grant type for multi-factor authentication in WSO2 Identity Server.
It supports Identifier First, Basic Authenticator, SMS OTP and Email OTP authenticators with any number of authentication steps.
You can refer [here](Use-cases.md) for some use cases of this connector.

As per the [API spec](../components/org.wso2.carbon.identity.oauth2.grant.rest.endpoint/src/main/resources/AuthenticationRestAPI.yaml)  this connector contains 3 endpoints namely 
`/auth-steps`, `/authenticte` and `/init-authenticator` along with the `/api/identity/authn/v1` request path.

## /auth-steps

This API context can be used to retrieve the configured authentication steps 
of a given OAuth2/OpenId Connect Service Provider. The service provider is 
identified via the clientId. The API endpoints specify a set of error scenarios which can be 
found at WSO2 official documentation for this connector.

#### Sample curl command:

```agsl
curl --location 'https://<IS-HOST>/t/<tenant-domain>/api/identity/authn/v1/auth-steps?
clientId=_6_OAiUkvaAIsBwCdN8c4nVUfTga' \
--header 'accept: application/json' \
--header 'Content-Type: application/json' \
--data ''
```
## /authenticate

This API context can be used to process an authentication request and get an 
appropriate response. This API can be used to fulfill below two tasks,
Initialize the authentication with client ID via Basic Authentication and 
Identifier First. Process the authentication request of the selected authenticator.

The API endpoints specify a set of error scenarios which can be found at 
WSO2 official documentation for this connector.

<br/>

#### Sample curl request for above 1st task

For Identifier First
```agsl
curl --location 'https://<IS-HOST>/t/<tenant-domain>/api/identity/authn/v1/authenticate' \
--header 'accept: application/json' \
--header 'Content-Type: application/json' \
--header 'Authorization: Basic YWRtaW46YWRtaW4=' \
--data-raw '{
  "userIdentifier":"testuser",
  "clientId": "_6_OAiUkvaAIsBwCdN8c4nVUfTga",
  "authenticator": "IdentifierExecutor"
}'
```

For BasicAuthenticator

```agsl
curl --location 'https://<IS-HOST>/t/<tenant-domain>/api/identity/authn/v1/authenticate' \
--header 'accept: application/json' \
--header 'Content-Type: application/json' \
--header 'Authorization: Basic YWRtaW46YWRtaW4=' \
--data-raw '{
  "clientId": "DSJhjfdoufhdfkenjckrER",
  "authenticator": "BasicAuthenticator",
  "userIdentifier": "testuser",
  "password": "12345"
}'
```
<br/>

#### Sample curl request for above 2nd task
```agsl
curl --location 'https://<IS-HOST>/t/<tenant-domain>/api/identity/authn/v1/authenticate' \
--header 'accept: application/json' \
--header 'Content-Type: application/json' \
--data '{
  "flowId":"0e59c896-0bca-467a-bf79-e6fdbbb953f5",
  "password": "12345",
  "authenticator": "BasicAuthenticator"
}'
```

## /init-authenticator

This API context can be used to start authentication flow via a 
selected authenticator. The API endpoints specify a set of error 
scenarios which can be found at WSO2 official documentation for this connector.

```agsl
curl --location 'https://localhost:9443/api/identity/authn/v1/init-authenticator' \
--header 'accept: application/json' \
--header 'Content-Type: application/json' \
--data '{
  "flowId":"c9e2bed6-839c-4754-a2a1-5f4fac312055",
  "authenticator":"EmailOTP"
}'
```

## /oauth2/token

Once all the authentication steps are completed, the authenticate API will return isAuthFlowCompleted as true and nextStep as -1 with a new flowId. This recent flowId can be used to obtain an access token through the following grant type. Please note that the same flowId cannot be used for more than one attempt to obtain an access token.
```agsl
curl --location 'https://localhost:9443/oauth2/token' \
--header 'Authorization: Basic S0hsR0phNWZmeEJEOVZXaDdSUDJXTTVvYWFBYTpYSTZoWFBLMjNzcUNDTzhUOHg5MGFhOFRRa0lh' \
--data-urlencode 'scope=openid' \
--data-urlencode 'flowId=5bbd44b9-180c-419e-a25b-a97d86da9a14' \
--data-urlencode 'grant_type=urn:ietf:params:oauth:grant-type:rest'
```
## Steps to deploy the connector

1) Stop the WSO2 Identity Server if running.

2) Extract the zip file and, you may be able to find below artifacts.
   * `api#identity#authn#v1.war`
   * `dbscrips` directory
   * `cleanup-scripts` directory.
   * `identity.oauth2.grant.auth.rest.handler-x.x.x.jar`
   * `org.wso2.carbon.identity.oauth2.grant.rest.core-x.x.x.jar`
   * `org.wso2.carbon.extension.identity.smsotp.common-x.x.x.jar`
   * `org.wso2.carbon.extension.identity.emailotp.common-x.x.x.jar`
   * `rest-auth.properties`

3) Copy `org.wso2.carbon.identity.oauth2.grant.rest.core-x.x.x.jar`,
   `org.wso2.carbon.extension.identity.smsotp.common-x.x.x.jar` and
   `org.wso2.carbon.extension.identity.emailotp.common-x.x.x.jar` files into the 
`<IS-Home>/repository/components/dropins` directory.

4) Copy `identity.oauth2.grant.auth.rest.handler-x.x.x.jar` file into the
`<IS-Home>/repository/components/libs` directory.

5) Copy `api#identity#authn#v1.war` file into the `<IS-Home>/repository/deployment/server/webapps` directory.

6) Execute the [db-scripts](../components/org.wso2.carbon.identity.oauth2.grant.rest.core/src/main/resources/dbscripts) on the WSO2 Identity DB.

7) Copy `rest-auth.properties` [file](../components/org.wso2.carbon.identity.oauth2.grant.rest.core/src/main/resources/rest-auth.properties) into the 
`<IS-Home>/repository/conf` directory. 
(Please note that copying this file is an optional task. You can configure this property file configurations as your 
   requirements. 
Customize error code,error message and description can be attached here. 
By default the file includes the default error messages.)

8) Add the following configurations into deployment.toml file.

- Endpoint configurations

```toml
[[resource.access_control]]
default_access = "allow"
context = "(.*)/api/identity/authn/v1/authenticate"
secure = "false"
http_method = "POST"

[[resource.access_control]]
default_access = "allow"
context = "(.*)/api/identity/authn/v1/init-authenticator"
secure = "false"
http_method = "POST"

[[resource.access_control]]
default_access = "allow"
context = "(.*)/api/identity/authn/v1/auth-steps"
secure = "false"
http_method = "GET"

[tenant_context.rewrite]
custom_webapps=["/api/identity/authn/v1/"]
```

- Grant Handler configurations
```toml
[[oauth.custom_grant_type]]
name="rest_auth_grant"
grant_handler="org.wso2.carbon.identity.oauth2.grant.rest.handler.AuthenticationGrantHandler"
grant_validator="org.wso2.carbon.identity.oauth2.grant.rest.handler.AuthenticationGrantValidator"
```

#### SMSOTP and EmailOTP service configurations
- For SMSOTP, follow the instructions provided [here](https://github.com/wso2-extensions/identity-outbound-auth-sms-otp/blob/master/docs/sms_otp_service.md)
- For EmailOTP, follow the instructions provided [here](https://github.com/wso2-extensions/identity-outbound-auth-email-otp/blob/master/docs/email_otp_service.md)


Finally, start the WSO2 IS and test the flow.

[Cleanup scripts](../components/org.wso2.carbon.identity.oauth2.grant.rest.core/src/main/resources/cleanup-scripts) and standard [error codes](errorCodes.md) for the 
REST API connector are available in the mentioned references.