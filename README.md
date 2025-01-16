# Order Microservice API

## Overview

This microservice manages orders and associated products. It provides APIs to create orders, update their status, modify products, and calculate the total value of the order based on the quantities and prices of the products.

## Base URL

All endpoints are prefixed with `/orders`.

## Endpoints

### 1. **Create**
POST `/orders`

**Request Body**:

```json
{
    "userId": "123",
    "products": [
        {
            "productId": 1,
            "amount": 2
        },
        {
            "productId": 2,
            "amount": 1
        }
    ]
}
```
#### Responses:

**201 Created**: Order created successfully.
```json
{
    "id": 1,
    "userId": "123",
    "amount": 300,
    "status": "pending",
    "createdAt": "2025-01-14T10:00:00Z"
}
```
---
### 2. **Update**
PUT `/orders/{id}`

**Request Body**:

```json
{
    "products": [
        {
            "productId": 1,
            "amount": 2
        },
        {
            "productId": 2,
            "amount": 1
        }
    ]
}
```
#### Responses:

**200 OK**
```json
{
    "id": 1,
    "userId": "123",
    "amount": 300,
    "status": "pending",
    "createdAt": "2025-01-14T10:00:00Z"
}
```
---
### 3. **Status Update**
PATCH `/orders/{id}/status`

**Request Body**:

```json
{
    "status": "completed"
}
```
#### Responses:

**200 OK**
```json
{
    "id": 1,
    "userId": "123",
    "amount": 500,
    "status": "completed",
    "createdAt": "2025-01-14T10:00:00Z"
}
```
---
### 4. **Status Update**
DELETE `/orders/{id}`
#### Responses:

**200 OK**
```json
{
    "message": "Order deleted successfully"
}
```
**400 Bad Request**
```json
{
    "message": "Cannot delete a completed order"
}
```
---
### 5. **GET All**
GET `/orders`
#### Responses:

**200 OK**
```json
[
    {
        "id": 1,
        "userId": "123",
        "amount": 300,
        "status": "pending",
        "createdAt": "2025-01-14T10:00:00Z"
    },
    {
        "id": 2,
        "userId": "456",
        "amount": 500,
        "status": "completed",
        "createdAt": "2025-01-15T12:00:00Z"
    }
]
```
---
### 6. **GET by ID**
GET `/orders/{id}`
#### Responses:

**200 OK**
```json
{
    "id": 1,
    "userId": "123",
    "amount": 300,
    "status": "pending",
    "createdAt": "2025-01-14T10:00:00Z"
}
```
---
### 7. **GET Order Products**
GET `/orders/{id}/products`
#### Responses:

**200 OK**
```json
[
    {
        "productId": 1,
        "amount": 2
    },
    {
        "productId": 2,
        "amount": 1
    }
]
```