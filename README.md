# Notificator

* Notification service for sending expired users received using SOAP messages from the `Team service` and then
  sending them to the `Router service` each team leader of his own team and common expired users to the lecturer.

## About

* When the service starts, two threads are created that access the request to receive expired users and then send them.
* SOAP request to get expired users for the last three days for a lecturer and for one day for a team lead in `Team service`
* SOAP request to send expired users to the `Router service`.
