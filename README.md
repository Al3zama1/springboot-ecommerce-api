# Ecommerce API

### Table of Contents
- [Project Description]() 
- [Project Features]()
- [Requirements Gathering]()
- [Roles / Authorities]()

## Project Description
Simple e-commerce application that allows customers to buy products
after they have created an account. The API has support for features such as account activation
and password reset through HTML emails.

## Project Features
- Account activation through HTML emails.
- Password change and reset through HTML emails.
- Authentication and Authorization based security (Spring Security).
- Dynamic HTML email generation through Apache FreeMarker templates.
- Email sending functionality through Java Mail API.

## Requirements Gathering
- **Admins:** Admins are the only user group allowed to create accounts for new employees.
  Additionally, admins are able to look at customer’s purchase history. Finally, admins can add, remove and restock items.
- **Employees:** Employees are sent a link by an administrator where they can sign up to the system. 
  Employees must have a unique identifier, name, last name, phone, address, email, and password. Employees are able to add, remove, and restock products in the system. 
  Employees are also able to look at the user's purchase history
- **Customer:** Customers must be authenticated to execute purchases.Customers need to have a unique identifier, name, last name, phone, address, email, and password. 
  Customers can not cancel an order once it has been shipped or delivered. Only while it is processing.
- **Order:** Each customer purchase creates an order which can contain 1 or more products. The order must have a unique identifier, orderDate, shipped date, customer to whom the order belongs, and a status.
- **OrderItem:**For each product in the customer order, a record is created. This record is linked to an order number and to a product. The record also contains the quantity for that specific product and the price of each item when the purchase was done.


## Roles / Authorities
- **Customer:** purchase products, cancel orders, and look at own purchase history.
- **Employee:** add, remove, restock products, and see customer purchase history.
- **Admin:** Same as employee, plus can add new employees.