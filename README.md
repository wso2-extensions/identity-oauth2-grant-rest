# WSO2 Identity Server - API Authentication Connector

Supported Versions : 5.10.0, 5.11.0, 6.0.0, 6.1.0

This connector has developed to provide an RESTful API interface for 
multi-factor authentication with WSO2 Identity Server. The connector itself 
contains its own grant type to get an access token at the end of multi-factor 
authentication steps. This would support Identifier First, Basic 
Authenticator, SMS OTP and Email OTP authenticators with any number 
of authentication steps.

As per the [API spec](https://app.swaggerhub.com/apis/KALUBOWILADC25132/wso-2_identity_server_api_authentication_connector/v1.0)  this connector contains 3 endpoints namely 
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

## Steps to configure

Stop the WSO2 Identity Server if running.

1) Extract the zip file and you may be able to find below artifacts.
   * `org.wso2.carbon.identity.oauth2.grant.rest.framework-x.x.x.jar`
   * `api#identity#authn#v1.war`
   * `identity.oauth2.grant.auth.rest.handler-1.0.0.jar`
   * `dbscrips` directory
   * `rest-auth.properties`

2) Copy `org.wso2.carbon.identity.oauth2.grant.rest.framework-x.x.x.jar` file into the 
`<IS-Home>/repository/components/dropins` directory.

3) Copy `api#identity#authn#v1.war`
file into the `<IS-Home>/repository/deployment/server` directory.

4) Copy `identity.oauth2.grant.auth.rest.handler-x.x.x.jar` file into the
`<IS-Home>/repository/components/libs` directory.

5) Get the `org.wso2.carbon.extension.identity.smsotp.common-x.x.x.jar` and 
`org.wso2.carbon.extension.identity.emailotp.common-x.x.x.jar` and copy in to 
the `<IS-Home>/repository/components/dropins` directory. 
(The 02 JARs are required to this connector in order to send the OTP for 
respective channel if [EmailOTP](https://github.com/wso2-extensions/identity-outbound-auth-sms-otp/tree/master/component/common) 
or [SMSOTP](https://github.com/wso2-extensions/identity-outbound-auth-email-otp/tree/master/component/common) 
would enable in an authentication steps)

6) Execute the [db-scripts](https://github.com/DInuwan97/identity-oauth2-grant-mfa/tree/dev-extenssion-features/components/org.wso2.carbon.identity.oauth2.grant.rest.framework/src/main/resources/dbscripts) 
in the WSO2 Identity DB.

7) Copy `rest-auth.properties` file into the 
`<IS-Home>/repository/conf` directory. 
(You can configure this property file configurations as your requirements. 
Customize error code,error message and description can be attached here. 
By default the file includes the default error messages.)


### deployment.toml configurations

#### Endpoint configurations

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

#### SMSOTP and EmailOTP Configurations
```toml
[[event_handler]]
name= "smsOtp"
properties.enabled=true
properties.tokenLength=6
properties.triggerNotification=true
properties.alphanumericToken=true
properties.showValidationFailureReason=true
properties.tokenValidityPeriod=120
properties.tokenRenewalInterval=60
properties.resendThrottleInterval=30
properties.maxValidationAttemptsAllowed=5

[[event_handler]]
name = "emailOtp"
properties.enabled=true
properties.tokenLength=6
properties.triggerNotification=true
properties.alphanumericToken=true
properties.showValidationFailureReason=true
properties.tokenValidityPeriod=120
properties.tokenRenewalInterval=60
properties.resendThrottleInterval=30
```

#### Grant Handler configurations
```toml
[[oauth.custom_grant_type]]
name="rest_auth_grant"
grant_handler="org.wso2.carbon.identity.oauth2.grant.rest.handler.AuthenticationGrantHandler"
grant_validator="org.wso2.carbon.identity.oauth2.grant.rest.handler.AuthenticationGrantValidator"
```

Finally start the WSO2 IS and test the flow.