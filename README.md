# Road Haulage

Imagine that you are travelling from Odessa to Kiev, your car is half empty, and you are thinking:
‘How could you make some money in the process?

Our project is a platform where everyone can become a courier or find a person
who will transport cargo along the necessary route.
It is a place where people with empty boots find those who are looking for a
convenient and economical way to send a parcel, things, or even a small cargo.

## Features

### User Management
*   **User Authentication and Authorization:**
    *   Users can create accounts.
    *   Registration includes email verification.
    *   Users can manage their account information.    
    *   Each user can act as both a customer and a courier simultaneously.

### Order Management
*   **Order Functionality:**
    *   User-customers can create new transport orders, specifying details such as:
        * Customer information:
            * Name and Surname
        * Categories
        * Departure Address
        * Delivery Address
        * Delivery Cost
        * Cargo information:
            * Weight of the order
            * Dimensions (length-width-height)
        * Date of order fulfillment (auto-generated)
    *   User-couriers can view published transport orders.
    *   User-couriers can respond to orders (update created orders), providing:
        * Courier information:
            * Name and Surname
        * Date of order acceptance (auto-generated)
    *   Once a courier accepts an order, the order status is ACCEPTED.

*   **Order Statuses:**
    *   CREATED: The order has been created but not yet published.
    *   PUBLISHED: The order is available for couriers to view.
    *   CHANGED: The order details have been updated by the customer.
    *   ACCEPTED: A courier has accepted the order.
    *   COMPLETED: The customer has confirmed the order completion.

## Technologies Used

*   Backend: JPA (Hibernate), Lombok, Spring (Boot, Data, Security, Web)
*   Frontend: HTML, CSS, JS, Thymeleaf
*   Database: MySQL
*   Authentication: Spring Security with email verification

## Usage

### Standard:
1. Register an account and verify your email.
2. Log in to your account.

### As A Customer:

1. Create a new transport order by filling out the required information.
2. Publish the order.
3. Review responses from couriers.
4. Accept a suitable courier's response.
5. Confirm order completion when the delivery is made.

### As A Courier:
1. Browse published transport orders.
2. Respond to orders you are interested in.
3. Manage accepted orders.

### For Admin
1. Log in to your account to use api endpoint via Postman, Insomnia...
2. Manage addresses by retrieving all, finding by ID, or updating them.
3. Manage orders by retrieving all, finding by ID, or updating them.
4. Manage order categories by retrieving all, finding by ID, or updating them.
5. Manage users by retrieving all, finding by ID or email.
6. Ensure to use the appropriate API endpoints for each action as described in the system documentation.

## Docker Deployment

### From github source

1. **Ensure Docker is installed** on your system.

2. **Clone the repository** if you haven't already:
   ```bash
   git clone https://github.com/Abrohamovich/RoadHaulage.git
   ```
3. **Create .env** file with environment variables

    ```
    ADMIN_EMAIL=
    ADMIN_PASSWORD=#watch isValidPassword() method in RegisterServiceDefault
    ADMIN_PHONE_CODE=
    ADMIN_PHONE_NUMBER=
    MAIL_PASSWORD='abcd efgh klmn oprs'
    MAIL_USERNAME=
    MYSQL_DATABASE=
    JWT_SECRET=
    MYSQL_USER=
    MYSQL_PASSWORD=
    MYSQL_ROOT_PASSWORD=
    DATABASE_HOST=
    DATABASE_PORT=
    ```

4. **Run docker command** in project directory
   ```bash
   docker-compose up --build
   ```
### From Docker Hub

1. **Follow the link** to my repository and follow the instructions

    https://hub.docker.com/r/abrohamovich/roadhaulage

## Contact

* Email: gerasimov08nikital@gmail.com
