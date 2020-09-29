# Cantina

This application is a WIP of an event sourced CQRS order/reservation service. This could be used to 
order tickets or food or products, the idea is that its generic enough to be used for anything that can 
be ordered or reserved.

## High Level Goals
 - CQRS
 - Event Sourced
 - DDD
 - Hexagonal Architecture
 - Pure FP
 
## Scala Goals
 - Only use implicits when necessary
 
### Ubiquitous Language

[Order Management System](https://en.wikipedia.org/wiki/Order_management_system)

 - Content management
  - Product info
  - Pricing
 - Inventory management
  - Quantity
  - Location
 - Order fulfillment
  - Picking
  - Shipping
 - Accounting
  - Payments
  - Invoicing
  