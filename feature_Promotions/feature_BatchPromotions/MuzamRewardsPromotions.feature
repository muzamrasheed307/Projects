Feature: Muzam Reward Promotion(MRP) (3622)


  #GetOneMRP
  @Smoke
  Scenario Outline: 3622_MuzamRewardPromotion - get an existing MRP with proper permissions and statistics QueryParameter,
  validate if response contains statistic's values and frequency of execution. validate data type of each field.

    Given Environment access token is generated for MRP with valid permissions
    When request is sent to get an existing MuzamRewardPromotion with <Id> and <statistics>
    Then response will contain Id of MRP and validate data type of all fields of a MRP
    And response body will not be null, status code will be 200, must not contain any error
    And if statistic is not null, log all components and their values of statistic
    Examples:
      | Id | statistics |
      | 16 | true       |
      | 16 | false      |


  @Reg
  Scenario Outline: 3622_MuzamRewardPromotion - get an existing MRP without proper permissions,
  and validate if response contains an error

    Given Environment access token is generated for a MRP without valid permissions
    When request is sent to get a MuzamRewardPromotion with valid <Id>
    Then response must contain <error>, and <statusCode> is 403
    Examples:
      | Id | statusCode | error |
      | 16 | 403        | true  |


  #Post
  @Reg
  Scenario Outline: 3622_MuzamRewardPromotion, creating a new Muzam reward promotion.If pointsOption=deductAllAvailablePoints,
  then fixedPointsToDeduct value must be null.MRP will not be created if any or all given values is/are invalid.
  If pointsOption=deductFixedPoints, then fixedPointsToDeduct value must not be null and -int.
  MRP will not be created if name is not unique. if pointsOption=deductFixedPoints, then fixedPointsToDeduct value must be int.

    Given Environment access token is generated for creating a MRP
    When request is sent to create a MuzamRewardPromotion with <name>, <frequency>, <pointsOption>, <fixedPointsToDeduct>, <locationId>, <clubId>, <internalRewardId>, <memberGroupId> and <tiersId>
    Then response will contain a new MuzamRewardPromotion Id
    And response body will have <statusCode>, any <error> if exist , and response body will not be null
    Examples:
      | locationId | clubId  | internalRewardId | memberGroupId | tiersId | name | frequency | fixedPointsToDeduct | pointsOption             | statusCode | error |
      #creating a MRP with valid values
      | 1          | 1065    | 111              | 2137          | 93      | MRP  | manual    | 0                   | deductAllAvailablePoints | 200        | false |
      #creating a MRP with an invalid locationId
      | 71001      | 1065    | 111              | 2137          | 93      | B1   | manual    | 0                   | deductAllAvailablePoints | 400        | true  |
      #creating a MRP with an invalid club
      | 1          | 1065001 | 111              | 2137          | 93      | B2   | manual    | 0                   | deductAllAvailablePoints | 400        | true  |
      #creating a MRP with an invalid internalRewardId
      | 1          | 1065    | 111001           | 2137          | 93      | B3   | manual    | 0                   | deductAllAvailablePoints | 400        | true  |
      #creating a MRP with an invalid memberGroupId
      | 1          | 1065    | 111              | 2124001       | 93      | B4   | manual    | 0                   | deductAllAvailablePoints | 400        | true  |
      #creating a MRP with an invalid tiersId
      | 1          | 1065    | 111              | 2137          | 215001  | B5   | manual    | 0                   | deductAllAvailablePoints | 400        | true  |
      #creating a MRP without unique name
      | 1          | 1065    | 111              | 2137          | 93      | MRP  | daily     | 0                   | deductAllAvailablePoints | 400        | true  |
      #creating a MRP with negative integer value of fixedPointsToDeduct
      | 1          | 1065    | 111              | 2137          | 93      | B7   | weekly    | -2                  | deductFixedPoints        | 400        | true  |


  @Reg
  Scenario: 3622_MuzamRewardPromotion - creating a new Muzam reward promotion with pointOption = Null
    Given Environment access token is generated for creating a MRP
    When request is sent to create a MuzamRewardPromotion with pointsOptions = null
    Then response will contain an error, and status code 400


  @Reg
  Scenario: 3622_MuzamRewardPromotion - creating a new Muzam reward promotion without proper permissions
    Given Environment access token is generated for creating a MRP with invalid permission
    When request is sent to create a MuzamRewardPromotion
    Then response will contain an error, and status code is 403


#UPDATE
  @Reg
  Scenario Outline: 3622_MuzamRewardPromotion - updating a Muzam reward promotion. MRP will not be updated if any or all given values is/are invalid.
  MRP will not be updated if name is not unique. if pointsOption=deductFixedPoints, then pointOption must not be null and negative int value, must be positive int value.

    Given Environment access token is generated for updating a MRP
    When request is sent to update a MuzamRewardPromotion with <MRPId>, <name>, <frequency>, <pointsOption>, <fixedPointsToDeduct>, <locationId>, <clubId>, <internalRewardId>, <memberGroupId> and <tiersId>
    Then response will contain all fields of a MuzamRewardPromotion
    And response body will not be null, and contains <statusCode>
    And validate each field if updated, log <error> if exist
    Examples:
      | MRPId | locationId | clubId  | internalRewardId | memberGroupId | tiersId | name             | frequency | fixedPointsToDeduct | pointsOption             | statusCode | error |
      #updating a MRP with valid values
      | 16    | 1          | 1065    | 111              | 2137          | 93      | MRP (DoNotTouch) | manual    | 0                   | deductAllAvailablePoints | 200        | false |
      #updating a MRP with an invalid locationId
      | 16    | 71001      | 1065    | 111              | 2137          | 93      | MRP (DoNotTouch) | manual    | 0                   | deductAllAvailablePoints | 400        | true  |
      #updating a MRP with an invalid club
      | 16    | 1          | 1065001 | 111              | 2137          | 93      | MRP (DoNotTouch) | manual    | 0                   | deductAllAvailablePoints | 400        | true  |
      #updating a MRP with an invalid internalRewardId
      | 16    | 1          | 1065    | 111001           | 2137          | 93      | MRP (DoNotTouch) | manual    | 0                   | deductAllAvailablePoints | 400        | true  |
      #updating a MRP with an invalid memberGroupId
      | 16    | 1          | 1065    | 111              | 2124001       | 93      | MRP (DoNotTouch) | manual    | 0                   | deductAllAvailablePoints | 400        | true  |
      #updating a MRP with an invalid tiersId
      | 16    | 1          | 1065    | 111              | 2137          | 215001  | MRP (DoNotTouch) | manual    | 0                   | deductAllAvailablePoints | 400        | true  |
      #updating a MRP without unique name
      | 16    | 1          | 1065    | 111              | 2137          | 93      | 11% off          | daily     | 0                   | deductAllAvailablePoints | 400        | true  |
      #updating a MRP with negative integer value of fixedPointsToDeduct
      | 16    | 1          | 1065    | 111              | 2137          | 93      | MRP (DoNotTouch) | weekly    | -2                  | deductFixedPoints        | 400        | true  |
      #updating a MRP with positive integer value of fixedPointsToDeduct
      | 16    | 1          | 1065    | 111              | 2137          | 93      | MRP (DoNotTouch) | weekly    | 2                   | deductFixedPoints        | 200        | false |
      #updating a MRP with 0 value of fixedPointsToDeduct
      | 16    | 1          | 1065    | 111              | 2137          | 93      | MRP (DoNotTouch) | monthly   | 0                   | deductFixedPoints        | 200        | false |
      #updating a MRP with archive internal reward
      | 16    | 1          | 1065    | 184              | 2137          | 93      | MRP (DoNotTouch) | monthly   | 2                   | deductFixedPoints        | 400        | true  |


  @Reg
  Scenario: 3622_MuzamRewardPromotion - updating a new Muzam reward promotion with pointOption = Null
    Given Environment access token is generated to update a MRP
    When request is sent to update a MuzamRewardPromotion with pointsOptions = null
    Then response must contain an error, and statusCode is 400


  Scenario: 3622_MuzamRewardPromotion - updating a new Muzam reward promotion with pointOption = Null
    Given Environment access token is generated to update a MRP
    When request is sent to update a MuzamRewardPromotion with pointsOptions = null
    Then response must contain an error, and statusCode is 400


  @Reg
  Scenario Outline: 3622_MuzamRewardPromotion - Execute a Muzam reward promotion
    Given Environment access token is generated to execute a MRP
    When request is sent to execute a MuzamRewardPromotion with <ID> and <location ID>
    Then response statusCode should be 200
    Examples:
      | location ID | ID |
      | 1           | 14 |


  Scenario Outline: 3622_MuzamRewardPromotion - Execution history of a Muzam reward promotion
    Given Environment access token is generated to get the execution history of a MRP
    When request is sent to get execution history of a MRP with <ID>
    Then response statusCode should be 200 for execution history
    Examples:
      | ID |
      | 16 |

   #DELETE
  @Reg
  Scenario: 3622_MuzamRewardPromotion - delete an existing MRP and verify deletion succeeds when MRP is not executed and issued in any promotional rule

    Given Environment access token is generated for deleting a MRP with proper permissions
    When request is sent to delete a MuzamRewardPromotion
    Then response will contain status code 200


  @Smoke
  Scenario Outline: 3622_MuzamRewardPromotion - delete an existing MRP and verify deletion fails when MRP is executed and issued in promotional rule

    Given Environment access token is generated for deleting a MRP with valid MRP and id
    When request is sent to delete a MuzamRewardPromotion <id>
    Then response will contain error and status code is 409
    Examples:
      | id |
      | 16 |


  @Reg
  Scenario: 3622_MuzamRewardPromotion - delete a new Muzam reward promotion without proper permissions

    Given Environment access token is generated for deleting a MRP with invalid permission
    When request is sent to delete existing MuzamRewardPromotion
    Then response must contain an error and status code is 403





