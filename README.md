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
            * Category
            * Departure Address
            * Delivery Address
            * IBAN Account Number
            * Delivery Cost
        * Cargo information:
            * Weight of the shipment
            * Dimensions (length-width-height)
        * Date of order fulfillment (auto-generated)
    *   User-couriers can view published transport orders.
    *   User-couriers can respond to orders (update created orders), providing:
        * Courier information:
            * Name and Surname
            * IBAN Account Number
        * Date of order acceptance (auto-generated)
    *   Once a courier accepts an order, the order status is updated.

*   **Order Statuses:**
    *   CREATED: The order has been created but not yet published.
    *   PUBLISHED: The order is available for couriers to view.
    *   CHANGED: The order details have been updated by the customer.
    *   IN PROGRESS: A courier has accepted the order.
    *   COMPLETED: The customer has confirmed the order completion.

## Technologies Used

*   Backend: JPA (Hibernate), Lombok, Spring (Boot, Data, Security, Web)
*   Frontend: HTML, CSS, JS, Thymeleaf
*   Database: MySQL
*   Authentication: Spring Security with email verification

## Usage

### For Customers:
1. Register an account and verify your email.
2. Log in to your account.
3. Create a new transport order by filling out the required information.
4. Publish the order.
5. Review responses from couriers.
6. Accept a suitable courier's response.
7. Confirm order completion when the delivery is made.

### For Couriers:
1. Register an account and verify your email.
2. Log in to your account.
3. Browse published transport orders.
4. Respond to orders you are interested in.
5. Manage accepted orders and communicate with customers.

### For Admin
1. Log in to your account to use api endpoint via Postman, Insomnia...
2. Manage addresses by retrieving all, finding by ID, or updating them.
3. Manage orders by retrieving all, finding by ID, or updating them.
4. Manage order categories by retrieving all, finding by ID, or updating them.
5. Manage users by retrieving all, finding by ID or email.
6. Ensure to use the appropriate API endpoints for each action as described in the system documentation.

## Docker Deployment

To run this project using Docker, follow these steps:

1. **Ensure Docker and Docker Compose are installed** on your system.

2. **Clone the repository** if you haven't already:
   ```bash
   git clone https://github.com/Abrohamovich/RoadHaulage.git

3. **Run docker command** in project directory
   ```bash
   docker-compose up --build
   
## Contact

* Email: gerasimov08nikital@gmail.com
