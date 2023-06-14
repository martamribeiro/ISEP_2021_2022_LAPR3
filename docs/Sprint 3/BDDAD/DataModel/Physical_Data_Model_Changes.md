### REVISED PHYSICAL DATA MODEL FOR SPRINT 3

#### Changes

* New Tables needed in order to run the business:
  * Warehouse
  * Truck 
  * TruckTrip
  * Seadists
  * Border
  * Route
  * Client
  * AuditContainerInManifest
  

* Changed Tables:
  * Port: added attribute maxCapacity
  * Shipment: added registration_code and changed shiptrip_id to route_id
  * ShipTrip: added route_id