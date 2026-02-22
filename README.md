# Spring Client Credentials Flow Demo

## 🚀 A minimal example of OAuth2 Client Credentials Flow using

* **Java 25**
* **Spring Boot 4.0**
* **Spring Security 7.0**
* **Keycloak**

---

## 🛡️ What is Client Credentials Flow and when is it used?

The **OAuth2 Client Credentials Flow** is used when one service needs to securely communicate with another service **without any user involvement**.
This is a classic **machine-to-machine (M2M)** scenario.

<details>
<summary><b>Abstract protocol flow</b></summary>

```text
    +--------+                                           +---------------+
    |        |--(A)------- Authorization Grant --------->|               |
    |        |                                           |               |
    |        |<-(B)----------- Access Token -------------|               |
    |        |               & Refresh Token             |               |
    |        |                                           |               |
    |        |                            +----------+   |               |
    |        |--(C)---- Access Token ---->|          |   |               |
    |        |                            |          |   |               |
    |        |<-(D)- Protected Resource --| Resource |   | Authorization |
    | Client |                            |  Server  |   |     Server    |
    |        |--(E)---- Access Token ---->|          |   |               |
    |        |                            |          |   |               |
    |        |<-(F)- Invalid Token Error -|          |   |               |
    |        |                            +----------+   |               |
    |        |                                           |               |
    |        |--(G)----------- Refresh Token ----------->|               |
    |        |                                           |               |
    |        |<-(H)----------- Access Token -------------|               |
    +--------+           & Optional Refresh Token        +---------------+

    ----------------------------------------------------------------------

    (A)  The client requests an access token by authenticating with the
         authorization server and presenting an authorization grant.

    (B)  The authorization server authenticates the client and validates
         the authorization grant, and if valid, issues an access token
         and a refresh token.

    (C)  The client makes a protected resource request to the resource
         server by presenting the access token.

    (D)  The resource server validates the access token, and if valid,
         serves the request.

    (E)  Steps (C) and (D) repeat until the access token expires.  If the
         client knows the access token expired, it skips to step (G);
         otherwise, it makes another protected resource request.

    (F)  Since the access token is invalid, the resource server returns
         an invalid token error.

    (G)  The client requests a new access token by authenticating with
         the authorization server and presenting the refresh token.  The
         client authentication requirements are based on the client type
         and on the authorization server policies.

    (H)  The authorization server authenticates the client and validates
         the refresh token, and if valid, issues a new access token (and,
         optionally, a new refresh token).
```

</details>

---

## 🧩 Service-to-Service Example

### Resource Service

The **resource-service** exposes a simple `/orders` endpoint. It does not contain any complex business logic, it just returns a static list of orders:

<details>
<summary><b>Resource Service GET <code>/orders</code> response</b></summary>

```json
[
    {
        "id": 1,
        "productName": "MacBook Pro M3",
        "price": 2500
    },
    {
        "id": 2,
        "productName": "Dell XPS-15",
        "price": 2300
    },
    {
        "id": 3,
        "productName": "Lenovo ThinkPad X1 Carbon",
        "price": 2000
    }
]
```

</details>

### Client Service

The **client-service** calls this endpoint using **OAuth2 Client Credentials** authentication. It processes the response and wraps it into its own structure:

<details>
<summary><b>Client Service GET <code>/orders</code> response</b></summary>

```json
{
    "orders": [
        {
            "id": 1,
            "productName": "MacBook Pro M3",
            "price": 2500
        },
        {
            "id": 2,
            "productName": "Dell XPS-15",
            "price": 2300
        },
        {
            "id": 3,
            "productName": "Lenovo ThinkPad X1 Carbon",
            "price": 2000
        }
    ],
    "status": "VERIFIED_BY_CLIENT_SERVICE"
}
```

</details>

This demonstrates a typical microservice pattern where one service consumes another service's API and applies its own business logic.

---

## ⚡ Quick Start

### Prerequisites

- Docker & Docker Compose
- Java 25
- Postman (for testing)

### Installation & Setup

#### 1. Clone the repository

```bash
# Clone the project to your local machine
git clone https://github.com/https756/spring-client-credentials-flow.git

# Navigate to the project directory
cd spring-client-credentials-flow
```

#### 2. Configure environment

Create a `.env` file in the project root:

```properties
# ==================================================
# Keycloak
# ==================================================
KEYCLOAK_SERVER_ADDRESS=ms-keycloak
KEYCLOAK_SERVER_PORT_EXTERNAL=8443
KEYCLOAK_SERVER_PORT_INTERNAL=8080
KEYCLOAK_BOOTSTRAP_ADMIN_USERNAME=admin
KEYCLOAK_BOOTSTRAP_ADMIN_PASSWORD=admin

# ==================================================
# Keycloak Postgres
# ==================================================
KEYCLOAK_POSTGRES_SERVER_ADDRESS=ms-keycloak-postgres
KEYCLOAK_POSTGRES_SERVER_PORT_EXTERNAL=5434
KEYCLOAK_POSTGRES_SERVER_PORT_INTERNAL=5432
KEYCLOAK_POSTGRES_DATABASE=keycloak-db
KEYCLOAK_POSTGRES_USERNAME=keycloak
KEYCLOAK_POSTGRES_PASSWORD=keycloak

# ==================================================
# Config Service
# ==================================================
CONFIG_SERVER_ADDRESS=ms-config-service
CONFIG_SERVER_PORT=8888

# ==================================================
# Resource Service
# ==================================================
RESOURCE_SERVER_ADDRESS=ms-resource-service
RESOURCE_SERVER_PORT=8081

# ==================================================
# Client Service
# ==================================================
CLIENT_SERVER_ADDRESS=ms-client-service
CLIENT_SERVER_PORT=8082
CLIENT_KC_CLIENT_SECRET=<YOUR_CLIENT_SECRET>
```

#### 3. Start Keycloak infrastructure

```bash
# Start only Postgres and Keycloak for initial configuration
docker compose up -d ms-keycloak-postgres ms-keycloak
```

#### 4. Configure Keycloak

> [!IMPORTANT]
> After Keycloak starts, open the [Security Admin Console](http://127.0.0.1:8443/admin/master/console/) and log in with `admin` / `admin`.

<details>
<summary><b>Step-by-step Keycloak configuration</b></summary>

| Step | Action                                                                                                                                                                                        | Screenshot                       |
|------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|----------------------------------|
| 1    | Create realm `client-credentials-flow` and switch to it                                                                                                                                       | [View image](.docs/images/1.png) |
| 2    | Create client `client-service`:<br>• Client authentication: **On**<br>• Allowed flow: **Service account roles**                                                                               | [View image](.docs/images/2.png) |
| 3    | Create client `resource-service`:<br>• Client authentication: **Off**<br>• Allowed flow: **Uncheck all**                                                                                      | [View image](.docs/images/3.png) |
| 4    | In `resource-service` client, create role `get-access` (under **Roles**)                                                                                                                      | [View image](.docs/images/4.png) |
| 5    | In `client-service` client, go to **Service account roles** and add role `resource-service.get-access`                                                                                        | [View image](.docs/images/5.png) |
| 6    | Create client scope `internal-api`:<br>• Type: **None**<br>• Include in token scope: **On**                                                                                                   | [View image](.docs/images/6.png) |
| 7    | In `client-service` client, under **Client scopes**, remove all unnecessary scopes, leave only:<br>`client-service-dedicated`, `internal-api`, `service_account`                              | [View image](.docs/images/7.png) |
| 8    | In `client-service-dedicated`, add Mapper **Audience Resolve**:<br>• Name: `scope resource-service.audience`<br>• Add to access token: **On**                                                 | [View image](.docs/images/8.png) |
| 9    | Add Mapper **User Client Role**:<br>• Client ID: `resource-service`<br>• Client Role prefix: `ROLE_`<br>• Multivalued: **On**<br>• Token Claim Name: `roles`<br>• Add to access token: **On** | [View image](.docs/images/9.png) |

</details>

#### 5. Complete the setup

After configuring Keycloak, copy the **Client secret** from the **Credentials** tab of the `client-service` client and add it to your `.env`:

```properties
CLIENT_KC_CLIENT_SECRET=<YOUR_CLIENT_SECRET>
```

#### 6. Restart all services

```bash
# Stop all containers
docker compose down

# Start the full infrastructure
docker compose up -d
```

---

## 📮 Testing with Postman

Test the API flows easily with a pre-configured Postman collection:
[Download](.docs/postman/client-credentials-flow.postman_collection.json) the collection.
