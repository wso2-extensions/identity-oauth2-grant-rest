--
-- Copyright (c) 2023, WSO2 LLC. (http://www.wso2.com).
--
-- WSO2 LLC. licenses this file to you under the Apache License,
-- Version 2.0 (the "License"); you may not use this file except
-- in compliance with the License.
-- You may obtain a copy of the License at
--
-- http://www.apache.org/licenses/LICENSE-2.0
--
-- Unless required by applicable law or agreed to in writing,
-- software distributed under the License is distributed on an
-- "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
-- KIND, either express or implied.  See the License for the
-- specific language governing permissions and limitations
-- under the License.
--

DELIMITER //

CREATE PROCEDURE DeleteExpiredOrInactiveFlows()
BEGIN
    -- Delete from IDN_REST_AUTH_AUTHENTICATED_STEPS
    DELETE FROM IDN_REST_AUTH_AUTHENTICATED_STEPS
    WHERE FLOW_ID_IDENTIFIER IN (
        SELECT FLOW_ID_IDENTIFIER
        FROM IDN_AUTH_REST_FLOW
        WHERE FLOW_ID_STATE = 'EXPIRED' OR FLOW_ID_STATE = 'INACTIVE'
    );

    -- Delete from IDN_REST_AUTH_USER
    DELETE FROM IDN_REST_AUTH_USER
    WHERE FLOW_ID_IDENTIFIER IN (
        SELECT FLOW_ID_IDENTIFIER
        FROM IDN_AUTH_REST_FLOW
        WHERE FLOW_ID_STATE = 'EXPIRED' OR FLOW_ID_STATE = 'INACTIVE'
    );

    -- Delete from IDN_AUTH_REST_FLOW
    DELETE FROM IDN_AUTH_REST_FLOW
    WHERE FLOW_ID_STATE = 'EXPIRED' OR FLOW_ID_STATE = 'INACTIVE';
END //

DELIMITER ;