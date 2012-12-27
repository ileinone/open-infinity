Exception Translation Policy

Narrative:

In order to handle exceptions in known way
As a aspect with my exception translation policy
I want to translate unknown exceptions to known exceptions

Scenario: Known Application Exception

Given an aspect to be called
When executing business operation and application exception occurs
Then exception should be bypassed by the aspect and must be of type org.openinfinity.core.exception.ApplicationException

Scenario: Known System Exception

Given an aspect to be called
When executing business operation and system exception occurs
Then exception should be bypassed by the aspect and must be of type org.openinfinity.core.exception.SystemException

Scenario: Known Business Violation Exception

Given an aspect to be called
When executing business operation and business violation exception occurs
Then exception should be bypassed by the aspect and must be of type org.openinfinity.core.exception.BusinessViolationException

Scenario: Unknown Exception Occurs

Given an aspect to be called
When executing business operation and unknown exception occurs
Then exception should be converted by the aspect and must be of type org.openinfinity.core.exception.SystemException