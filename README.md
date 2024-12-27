# Road Haulage

Imagine that you are travelling from Odessa to Kiev, your car is half empty, and you are thinking: 
‘How could you make some money in the process?

Our project is a platform where everyone can become a courier or find a person 
who will transport cargo along the necessary route. 
It is a place where people with empty boots find those who are looking for a 
convenient and economical way to send a parcel, things or even a small cargo.

## Features

*   **User Authentication and Authorization:**
    *   Users can create accounts.
    *   Each user can be both first and second at the same time
    *   Users can manage their account information.
* **Estimate Functionality:**
    * User-customer can create new transport orders, specifying details such as:
        * Customer information:
            * Name and surname.
            * Category.
            * Delivery address
            * Shipping address
            * IBAN account number
            * Delivery cost
        * Cargo information:
            * Weight of the shipment
            * Dimensions (length-width-high)
        * Date of order fulfillment (auto-generated)
    *   User-courier can view published transport orders.
    *   User-courier can respond to orders(update created estimates):
        * Courier information:
            * Name and Surname.
            * IBAN account number
        * Date of order acceptance (auto-generated)
    *   Once a courier accepts an order, the order status is updated to "In Progress".
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

*   **For Customer:**
    1.  Register an account.
    2.  Log in to your account.
    3.  Create a new transport order by filling out the required information.
    4.  Publish the order.
    5.  Review responses from courier.
    6.  Accept a suitable haulier's response.
    7.  Confirm order completion when the delivery is made.
*   **For Courier:**
    1.  Register an account.
    2.  Log in to your account.
    3.  Browse published transport orders.
    4.  Respond to orders you are interested in.
    5.  Manage accepted orders and communicate with customers.

## Contact

* Email: gerasimov08nikital@gmail.com