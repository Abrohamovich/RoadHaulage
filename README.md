# Road Haulage

This project is a web application designed to connect buyers and hauliers for efficient road freight management. It provides a platform for buyers to create and manage transport requests, and for hauliers to respond to these requests and manage their deliveries.

## Features

*   **User Authentication and Authorization:**
    *   Users can create accounts with two distinct roles: `BUYER` and `HAULIER`.
    *   Users can manage their account information.
* **Buyer Functionality:**
    * Buyers can create new transport orders, specifying details such as:
        * Buyer information:
            * Name and surname.
            * Delivery address
            * Destination address
            * IBAN account number
            * Delivery cost
        * Cargo information:
            * Weight of the shipment
            * Dimensions (length-width-high)
        * Date of order fulfillment (auto-generated)
*   **Haulier Functionality:**
    *   Hauliers can view published transport orders.
    *   Hauliers can respond to orders:
        * Haulier information:
            * Name and Surname.
            * IBAN account number
        * Date of order acceptance (auto-generated)
    *   Once a haulier accepts an order, the order status is updated to "In Progress".
*   **Order Management:**
    *   Orders have different statuses:
        *   `Published`: The order is available for hauliers to view.
        *   `In Progress`: A haulier has accepted the order.
        *   `Completed`: The buyer has confirmed the order completion.

## Technologies Used (Example)

*   **Backend:** JDBC, JPA (Hibernate), Lombok, Spring (Boot, Data, Security)
*   **Frontend:** HTML, CSS, Thymeleaf
*   **Database:** MySQL
*   **Authentication:** Spring Security

## Usage

*   **For Buyers:**
    1.  Register an account with the `BUYER` role.
    2.  Log in to your account.
    3.  Create a new transport order by filling out the required information.
    4.  Publish the order.
    5.  Review responses from hauliers.
    6.  Accept a suitable haulier's response.
    7.  Confirm order completion when the delivery is made.
*   **For Hauliers:**
    1.  Register an account with the `HAULIER` role.
    2.  Log in to your account.
    3.  Browse published transport orders.
    4.  Respond to orders you are interested in.
    5.  Manage accepted orders and communicate with buyers.

## Contact

gerasimov08nikital@gmail.com