package org.wso2.carbon.identity.oauth2.grant.rest.endpoint.util;

/**
 *This DTO class contains the ErrorCode, ErrorMessage and ErrorDescritpion.
 */
public class ErrorUtil {

    private String errorCode;
    private String errorMessage;
    private String errorDescription;

    public String getErrorCode() {

        return errorCode;
    }

    public void setErrorCode(String errorCode) {

        this.errorCode = errorCode;
    }

    public String getErrorMessage() {

        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {

        this.errorMessage = errorMessage;
    }

    public String getErrorDescription() {

        return errorDescription;
    }

    public void setErrorDescription(String errorDescription) {

        this.errorDescription = errorDescription;
    }

    @Override
    public String toString() {
        return "ErrorUtil{" +
                "errorCode='" + errorCode + '\'' +
                ", errorMessage='" + errorMessage + '\'' +
                ", errorDescription='" + errorDescription + '\'' +
                '}';
    }
}
