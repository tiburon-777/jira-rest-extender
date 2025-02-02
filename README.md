[![OCTOPUS CodeWare](https://www.tiburon.su/files/logo_octo_text_600_153.png)](https://www.tiburon.su)

REST API Extender for JIRA
================

REST API for automated JIRA configuration with URMS

Related Documentation
---------------------

* [Atlassian REST API design guidelines version 1](https://developer.atlassian.com/server/framework/atlassian-sdk/atlassian-rest-api-design-guidelines-version-1/)

Resources
---------

All resources produce JSON (media type:  `application/json`) results.

### Settings

Access important JIRA settings like the title, the base url, the mode
etc.

* #### `GET /rest/extender/1/settings`

  Get JIRA application settings.

  __Responses__

  ![Status 200][status-200]

  ```javascript
  {
    "baseurl": "http://localhost:2990/jira",
    "mode": "private",
    "title": "Your Company JIRA"
  }
  ```

  ![Status 401][status-401]

  Returned if the current user is not authenticated.

  ![Status 403][status-403]

  Returned if the current user is not an administrator.

* #### `PUT /rest/extender/1/settings`

  Set JIRA application settings.

  __Request Body__

  Media type: `application/json`

  Content: Settings, for example:

  ```javascript
  {
    "baseurl": "http://localhost:2990/jira",
    "mode": "private",
    "title": "Your Company JIRA"
  }
  ```

  __Request Parameters__

  None.

  __Responses__

  ![Status 200][status-200]

  Returned if request could be executed without major exceptions.

  The response will contain a list of errors that occurred while setting
  some specific values such as a string that was too long, for example:

  ```
  {
    "errorMessages": [
        "The length of the application title must not exceed 255 characters"
    ],
    "errors": {}
  }
  ```

  ![Status 401][status-401]

  Returned if the current user is not authenticated.

  ![Status 403][status-403]

  Returned if the current user is not an administrator.

### Licenses

The JIRA license API is a bit weird and needs to be well understood.
Just like in the web interface, different license keys can theoretically
be set for each application (JIRA Core, JIRA Software, etc.). However,
the entered license key is always set for all applications for which it
is valid.

For example:

1. JIRA Core and JIRA Software each have their own license key. If a
license key is now entered (in the web interface or via the REST API)
with which both applications can be licensed, this license key is stored
for both applications.

2. JIRA Core and JIRA Software use a common license key. If a license
key is now entered (in the web interface or via the REST API) with which
only JIRA Core can be licensed, the license key is also only stored for
JIRA Core.

So again, an entered license key is always stored for all applications
for which it is valid. The web interface might suggest that you can
select the desired application, but this is not true.

* #### `GET /rest/extender/1/licenses`

  Get the license keys together with the application keys of the
  applications using this license key.

  __Responses__

  ![Status 200][status-200]

  ```javascript
  {
    "licenses": [
      {
        "key": "AAA...",
        "applicationKeys": [
          "jira-software"
        ]
      },
      {
        "key": "AAA...",
        "applicationKeys": [
          "jira-core",
          "jira-servicedesk"
        ]
      }
    ]
  }
  ```

  ![Status 401][status-401]

  Returned if the current user is not authenticated.

  ![Status 403][status-403]

  Returned if the current user is not an administrator.

* #### `PUT /rest/extender/1/licenses`

  Set a license by its license key.

  __Request Body__

  Media type: `text/plain`

  Content: License key, for example:

  ```
  AAA...
  ```

  __Request Parameters__

  | parameter   | type      | description                                                                    |
  | ----------- | --------- | ------------------------------------------------------------------------------ |
  | `clear`     | _boolean_ | Clear all licenses before setting the new license, optional, defaults to false |

  __Responses__

  ![Status 200][status-200]

  ```javascript
  {
    "key": "AAA...",
    "applicationKeys": [
      "jira-core",
      "jira-servicedesk",
      "jira-software"
    ]
  }
  ```

  ![Status 401][status-401]

  Returned if the current user is not authenticated.

  ![Status 403][status-403]

  Returned if the current user is not an administrator.

### Syncronise Crowd User Directory

* #### `GET /rest/extender/1/directory`

  Get info about User Directories.

  __Responses__

  ![Status 200][status-200]

  ```javascript
  [
    {
      "id": 1,
      "name": "Jira Internal Directory",
      "active": true,
      "createdDate": 1362038271308,
      "updatedDate": 1362038271308,
      "lowerName": "jira internal directory",
      "description": "Jira default internal directory",
      "type": "INTERNAL",
      "implementationClass": "com.atlassian.crowd.directory.InternalDirectory",
      "lowerImplementationClass": "com.atlassian.crowd.directory.internaldirectory",
      "allowedOperations":["CREATE_GROUP", "CREATE_ROLE", "CREATE_USER", "DELETE_GROUP", "DELETE_ROLE", "DELETE_USER", "UPDATE_GROUP",…],
      "attributes":{
        "user_encryption_method": "atlassian-security"
      },
      "empty": false,
      "keys":["user_encryption_method"],
      "encryptionType": "atlassian-security"
    }
  ]
  ```

  ![Status 401][status-401]

  Returned if the current user is not authenticated.

  ![Status 403][status-403]

  Returned if the current user is not an administrator.

* #### `PUT /rest/extender/1/directory`

  Start sycronyse User Directory.

  __Request Parameters__

  | parameter | type   | description                                           |
  | --------- | ------ | ----------------------------------------------------- |
  | `id`      | _long_ | id of the User Directory, which should be syncronised |

  __Responses__

  ![Status 200][status-200]

  Returned if request could be executed without major exceptions.

  The response will contain a list of errors that occurred while setting
  some specific values such as a string that was too long, for example:

  ```
  {
    "message": "parameter should not be null!",
    "status-code": 500,
    "stack-trace": full_stack_trace_of_the_application
  }
  ```
  ![Status 401][status-401]

  Returned if the current user is not authenticated.

  ![Status 403][status-403]

  Returned if the current user is not an administrator.


[status-200]: https://img.shields.io/badge/status-200-brightgreen.svg
[status-400]: https://img.shields.io/badge/status-400-red.svg
[status-401]: https://img.shields.io/badge/status-401-red.svg
[status-403]: https://img.shields.io/badge/status-403-red.svg
[status-404]: https://img.shields.io/badge/status-404-red.svg

Implemented