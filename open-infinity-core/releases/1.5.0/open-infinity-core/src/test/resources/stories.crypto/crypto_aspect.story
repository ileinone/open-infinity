Encryption Aspect Policy

Narrative:

In order to handle encryption and decryption in standard way
As aspect with my given security standars
I want to encrypt and decrypt speficied object graphs content

Scenario: Encrypt Specified Fields

Given an aspect to be called
When executing business operation and encryption of an object graphs is needed
Then object graphs specified fields must be encrypted

Scenario: Encrypt All Fields

Given an aspect to be called
When executing business operation and encryption of an object graphs is needed
Then object graphs all fields must be encrypted

Scenario: Decrypt Specified Fields

Given an aspect to be called
When executing business operation and decryption of an object graphs is needed
Then object graphs spefied fields must be decrypted

Scenario: Decrypt All Fields

Given an aspect to be called
When executing business operation and decryption of an object graphs is needed
Then object graphs all fields must be decrypted