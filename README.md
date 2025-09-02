# 👥 Group Management UI (GMUI)

G.M.U.I. is a web-based user interface for managing Groups in a system.  This is only used within the CADC
to provide Users with a simple way to manage their Groups.

## 🚀 Features

- 🧑‍🤝‍🧑 Create and manage user groups
- 👤 Add, edit, or remove members from groups
- 🔍 Search and filter members/groups
- 🌐 Responsive UI for desktop and mobile
- ♨️ Spring IoC for dependency injection

## 🛠 Tech Stack

| Frontend                         | Backend | Database | Other                         |
|----------------------------------|---------|----------|-------------------------------|
| WET-BOEW 4 JavaScript and jQuery | JDK 11  | \*None\* | Relies on an IVOA GMS Service |

## 🐳 Installation with Docker

```bash
docker pull bucket.canfar.net/cadc/group-management-ui:latest
docker run -d -p 8080:8080 bucket.canfar.net/cadc/group-management-ui:latest
```

### Prerequisites

- Java JDK 11 or higher
- Docker (optional)
- An IVOA GMS Service running and accessible
- The `access` web service to enable logins from the browser.
- Swagger UI 5.22 (tested) for API documentation, available at `${RPS}/resources/swagger-ui`.

### Configuration

Configuration relies on setting up files to be mounted into the container. The following files are required:

| Location                           | Description                                                                                                                                                                                                                        |
|------------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `/config/cadc-registry.properties` | Local lookup for determining the URI to lookup in the Registry for the location of a service endpoint.  See the OpenCADC [cadc-registry](https://github.com/opencadc/reg/tree/main/cadc-registry#cadc-registryproperties) project. |
| `/config/cadc-log.properties`      | Logging configuration for the application.  See the OpenCADC [cadc-log](https://github.com/opencadc/core/tree/main/cadc-log#cadc-logproperties-optional) project.                                                                  |
| `/config/RsaSignaturePub.key`      | Public key to decrypt the CADC Single Sign-On (SSO) cookie value.                                                                                                                                                                  |

See the [Spring XML Configuration](./src/main/webapp/WEB-INF/gmui_web_RestletBeans.xml) for more details on the configuration and routing.

### Clone the Repository

```bash
git clone https://github.com/your-username/group-management-app.git
cd group-management-app
./gradlew -i clean build test
```

Will build a WAR file in the `build/libs` directory.

## Cache Management
The Group Management UI relies on a cache for Groups and Users.  The cache is automatically refreshed when the application starts, but can also be manually refreshed by sending a POST request to the `/gmui/services/associations` endpoint.